# Output para exibir o ID da VPC criada
output "vpc_id" {
  value       = data.aws_vpc.eks_vpc.id
  description = "O ID da VPC criada"
}

output "private_subnet_id" {
  value       = data.aws_subnet.eks_private_subnet.id
  description = "O ID da primeira subnet privada criada"
}

output "private_subnet_id_2" {
  value       = data.aws_subnet.eks_private_subnet2.id
  description = "O ID da segunda subnet privada criada"
}