output "alb_dns_name" {
  description = "DNS name of the ALB para conectar ao API Gateway"
  value       = aws_lb.registration_alb.dns_name
}

output "alb_arn" {
  description = "ARN do ALB"
  value       = aws_lb.registration_alb.arn
}

output "alb_security_group_id" {
  description = "ID do Security Group do ALB"
  value       = aws_security_group.alb_sg.id
}

output "service_name" {
  description = "Nome do serviço para usar no API Gateway"
  value       = "registration"
}

output "health_check_endpoint" {
  description = "Endpoint para health check"
  value       = "http://${aws_lb.registration_alb.dns_name}/actuator/health"
}

output "swagger_url" {
  description = "URL do Swagger UI (apenas desenvolvimento)"
  value       = "http://${aws_lb.registration_alb.dns_name}/swagger-ui/index.html"
}

# Mantenha os outputs existentes
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