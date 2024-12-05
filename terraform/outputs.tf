# Output para exibir o ID da VPC criada
output "vpc_id" {
  value       = aws_vpc.eks_vpc.id
  description = "O ID da VPC criada"
}

# Output para exibir o ID da subnet pública criada
output "public_subnet_id" {
  value       = aws_subnet.eks_public_subnet.id
  description = "O ID da subnet pública criada"
}

# Output para exibir o ID da primeira subnet privada criada
output "private_subnet_id" {
  value       = aws_subnet.eks_private_subnet.id
  description = "O ID da primeira subnet privada criada"
}

# Output para exibir o ID da segunda subnet privada criada
output "private_subnet_id_2" {
  value       = aws_subnet.eks_private_subnet_2.id
  description = "O ID da segunda subnet privada criada"
}

output "eks_node_group_id" {
  value = aws_eks_node_group.eks_node_group.id
}

output "eks_cluster_name" {
  value = aws_eks_cluster.eks-cluster.name
}