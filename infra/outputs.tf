output "alb_dns_name" {
  description = "DNS name of the ALB"
  value       = aws_lb.registration_alb.dns_name
}

output "service_url" {
  description = "URL para acessar o serviço"
  value       = "http://${aws_lb.registration_alb.dns_name}"
}

output "database_endpoint" {
  description = "Endpoint do banco de dados RDS"
  value       = aws_db_instance.registration_db.address
}

output "database_port" {
  description = "Porta do banco de dados"
  value       = aws_db_instance.registration_db.port
}

output "database_name" {
  description = "Nome do banco de dados"
  value       = aws_db_instance.registration_db.db_name
}

output "database_username" {
  description = "Usuário do banco de dados"
  value       = aws_db_instance.registration_db.username
}

output "ecs_cluster_name" {
  description = "Name of the ECS cluster"
  value       = aws_ecs_cluster.registration_cluster.name
}

output "ecs_service_name" {
  description = "Name of the ECS service"
  value       = aws_ecs_service.registration_service.name
}