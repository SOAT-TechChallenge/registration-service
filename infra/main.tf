terraform {
  required_version = ">= 1.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
  backend "s3" {
    bucket = "techchallenge-tf"
    key    = "registration-service/terraform.tfstate"
    region = "us-east-1"
  }
}

provider "aws" {
  region = "us-east-1"
}

data "aws_vpc" "default" {
  default = true
}

data "aws_subnets" "all" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.default.id]
  }
}

data "aws_iam_role" "lab_role" {
  name = "LabRole"
}

resource "aws_security_group" "alb_sg" {
  name        = "registration-service-alb-sg"
  description = "Security group for ALB - Allow from API Gateway"
  vpc_id      = data.aws_vpc.default.id

  # Permite apenas do API Gateway (que usa IPs dinâmicos)
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]  # API Gateway usa IPs dinâmicos
    description = "Allow from API Gateway"
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "registration-service-alb-sg"
  }
}

resource "aws_security_group" "ecs_sg" {
  name        = "registration-service-ecs-sg"
  description = "Security group for ECS tasks"
  vpc_id      = data.aws_vpc.default.id

  ingress {
    from_port       = 8080
    to_port         = 8080
    protocol        = "tcp"
    security_groups = [aws_security_group.alb_sg.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "registration-service-ecs-sg"
  }
}

resource "aws_security_group" "rds_sg" {
  name        = "registration-rds-sg"
  description = "Security group for RDS PostgreSQL"
  vpc_id      = data.aws_vpc.default.id

  ingress {
    from_port       = 5432
    to_port         = 5432
    protocol        = "tcp"
    security_groups = [aws_security_group.ecs_sg.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "registration-rds-sg"
  }
}

resource "aws_db_subnet_group" "registration" {
  name       = "registration-db-subnet-group"
  subnet_ids = slice(data.aws_subnets.all.ids, 0, min(2, length(data.aws_subnets.all.ids)))

  tags = {
    Name = "registration-db-subnet-group"
  }
}

resource "aws_db_instance" "registration_db" {
  identifier              = "registration-db"
  instance_class          = "db.t3.small"
  allocated_storage       = 20
  engine                  = "postgres"
  engine_version          = "15.14"
  username                = "registration_user"
  password                = "Registration123!"
  db_name                 = "registration_db"
  parameter_group_name    = "default.postgres15"
  skip_final_snapshot     = true
  publicly_accessible     = false
  vpc_security_group_ids  = [aws_security_group.rds_sg.id]
  db_subnet_group_name    = aws_db_subnet_group.registration.name

  apply_immediately       = true
  backup_retention_period = 0
  deletion_protection     = false

  tags = {
    Name = "registration-db"
  }
}

resource "aws_lb" "registration_alb" {
  name               = "registration-service-alb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb_sg.id]
  subnets            = slice(data.aws_subnets.all.ids, 0, min(2, length(data.aws_subnets.all.ids)))

  enable_deletion_protection = false

  tags = {
    Name = "registration-service-alb"
  }
}

resource "aws_lb_target_group" "registration_tg" {
  name        = "registration-tg"
  port        = 8080
  protocol    = "HTTP"
  vpc_id      = data.aws_vpc.default.id
  target_type = "ip"

  health_check {
    path                = "/"
    interval            = 60
    timeout             = 30
    healthy_threshold   = 2
    unhealthy_threshold = 5
    matcher             = "200-399"
  }

  tags = {
    Name = "registration-tg"
  }
}

resource "aws_lb_listener" "registration_listener" {
  load_balancer_arn = aws_lb.registration_alb.arn
  port              = "80"
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.registration_tg.arn
  }
}

resource "aws_ecs_cluster" "registration_cluster" {
  name = "registration-cluster"

  setting {
    name  = "containerInsights"
    value = "disabled"
  }

  tags = {
    Name = "registration-cluster"
  }
}

resource "aws_ecs_task_definition" "registration_task" {
  family                   = "registration-service"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = 1024
  memory                   = 2048
  execution_role_arn       = data.aws_iam_role.lab_role.arn

  container_definitions = jsonencode([{
    name  = "registration-service"
    image = "leynerbueno/registration-service:latest"
    portMappings = [{
      containerPort = 8080
      hostPort      = 8080
      protocol      = "tcp"
    }]
    essential = true

    healthCheck = {
      command     = ["CMD-SHELL", "wget -q -O - http://localhost:8080/ || exit 1"]
      interval    = 60
      timeout     = 20
      retries     = 3
      startPeriod = 120
    }

    logConfiguration = {
      logDriver = "awslogs"
      options = {
        awslogs-group         = "/ecs/registration-service"
        awslogs-region        = "us-east-1"
        awslogs-stream-prefix = "ecs"
      }
    }

    environment = [
      {
        name  = "SERVER_PORT"
        value = "8080"
      },
      {
        name  = "SPRING_DATASOURCE_URL"
        value = "jdbc:postgresql://${aws_db_instance.registration_db.address}:5432/registration_db"
      },
      {
        name  = "SPRING_DATASOURCE_USERNAME"
        value = "registration_user"
      },
      {
        name  = "SPRING_DATASOURCE_PASSWORD"
        value = "Registration123!"
      },
      {
        name  = "SPRING_JPA_HIBERNATE_DDL_AUTO"
        value = "update"
      },
      {
        name  = "SPRING_PROFILES_ACTIVE"
        value = "prod"
      },
      {
        name  = "SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE"
        value = "5"
      },
      {
        name  = "SPRING_DATASOURCE_HIKARI_MINIMUM_IDLE"
        value = "2"
      },
      {
        name  = "SPRING_DATASOURCE_HIKARI_CONNECTION_TIMEOUT"
        value = "30000"
      },
      {
        name  = "SPRING_DATASOURCE_HIKARI_IDLE_TIMEOUT"
        value = "300000"
      },
      {
        name  = "SPRING_DATASOURCE_HIKARI_MAX_LIFETIME"
        value = "1200000"
      },
      {
        name  = "SERVER_TOMCAT_MAX_THREADS"
        value = "50"
      },
      {
        name  = "SERVER_TOMCAT_MAX_CONNECTIONS"
        value = "100"
      },
      {
        name  = "SPRING_LIFECYCLE_TIMEOUT_PER_SHUTDOWN_PHASE"
        value = "30s"
      },
      {
        name  = "MANAGEMENT_ENDPOINT_HEALTH_SHOW-DETAILS"
        value = "always"
      },
      {
        name  = "MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE"
        value = "health,info,metrics"
      }
    ]
  }])

  tags = {
    Name = "registration-service-task"
  }
}

resource "aws_ecs_service" "registration_service" {
  name            = "registration-service"
  cluster         = aws_ecs_cluster.registration_cluster.id
  task_definition = aws_ecs_task_definition.registration_task.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  health_check_grace_period_seconds = 600

  network_configuration {
    security_groups  = [aws_security_group.ecs_sg.id]
    subnets          = slice(data.aws_subnets.all.ids, 0, min(2, length(data.aws_subnets.all.ids)))
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.registration_tg.arn
    container_name   = "registration-service"
    container_port   = 8080
  }

  deployment_controller {
    type = "ECS"
  }

  deployment_circuit_breaker {
    enable   = true
    rollback = true
  }

  tags = {
    Name = "registration-service"
  }
}

resource "aws_cloudwatch_log_group" "registration_service" {
  name              = "/ecs/registration-service"
  retention_in_days = 7
}