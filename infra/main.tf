terraform {
  required_version = ">= 1.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = "us-east-1"
}

# Data sources para VPC e subnets existentes
data "aws_vpc" "existing" {
  filter {
    name   = "tag:Name"
    values = ["techchallenge-vpc"]
  }
}

data "aws_subnets" "public" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.existing.id]
  }

  filter {
    name   = "tag:Name"
    values = ["techchallenge-vpc-public-*"]
  }
}

data "aws_subnets" "private" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.existing.id]
  }

  filter {
    name   = "tag:Name"
    values = ["techchallenge-vpc-private-*"]
  }
}

# Security Group para o RDS
resource "aws_security_group" "rds_sg" {
  name        = "registration-rds-sg"
  description = "Security group for RDS PostgreSQL"
  vpc_id      = data.aws_vpc.existing.id

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

# RDS PostgreSQL
resource "aws_db_instance" "registration_db" {
  identifier              = "registration-db"
  instance_class          = "db.t3.micro"
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

  tags = {
    Name = "registration-db"
  }
}

# DB Subnet Group
resource "aws_db_subnet_group" "registration" {
  name       = "registration-db-subnet-group"
  subnet_ids = data.aws_subnets.private.ids

  tags = {
    Name = "registration-db-subnet-group"
  }
}

# Security Group para o ALB
resource "aws_security_group" "alb_sg" {
  name        = "registration-service-alb-sg"
  description = "Security group for ALB"
  vpc_id      = data.aws_vpc.existing.id

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
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

# Security Group para ECS
resource "aws_security_group" "ecs_sg" {
  name        = "registration-service-ecs-sg"
  description = "Security group for ECS tasks"
  vpc_id      = data.aws_vpc.existing.id

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

# ALB
resource "aws_lb" "registration_alb" {
  name               = "registration-service-alb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb_sg.id]
  subnets            = data.aws_subnets.public.ids

  enable_deletion_protection = false

  tags = {
    Name = "registration-service-alb"
  }
}

# Target Group
resource "aws_lb_target_group" "registration_tg" {
  name        = "registration-tg"
  port        = 8080
  protocol    = "HTTP"
  vpc_id      = data.aws_vpc.existing.id
  target_type = "ip"

  tags = {
    Name = "registration-tg"
  }
}

# Listener do ALB
resource "aws_lb_listener" "registration_listener" {
  load_balancer_arn = aws_lb.registration_alb.arn
  port              = "80"
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.registration_tg.arn
  }
}

# ECS Cluster
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

# ECS Task Definition
resource "aws_ecs_task_definition" "registration_task" {
  family                   = "registration-service"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = 256
  memory                   = 512

  container_definitions = jsonencode([{
    name  = "registration-service"
    image = "breno091073/registration-service:latest"
    portMappings = [{
      containerPort = 8080
      hostPort      = 8080
      protocol      = "tcp"
    }]

    essential = true

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
      }
    ]
  }])

  tags = {
    Name = "registration-service-task"
  }
}

# ECS Service
resource "aws_ecs_service" "registration_service" {
  name            = "registration-service"
  cluster         = aws_ecs_cluster.registration_cluster.id
  task_definition = aws_ecs_task_definition.registration_task.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    security_groups = [aws_security_group.ecs_sg.id]
    subnets         = data.aws_subnets.private.ids
    assign_public_ip = false
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.registration_tg.arn
    container_name   = "registration-service"
    container_port   = 8080
  }

  health_check_grace_period_seconds = 180

  depends_on = [aws_lb_listener.registration_listener]

  tags = {
    Name = "registration-service"
  }
}

# CloudWatch Log Group
resource "aws_cloudwatch_log_group" "registration_logs" {
  name              = "/ecs/registration-service"
  retention_in_days = 7

  tags = {
    Name = "registration-service-logs"
  }
}