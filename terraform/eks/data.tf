data "aws_db_instance" "rds" {
  db_instance_identifier = "mydb-instance"
}

data "aws_eks_cluster" "cluster" {
  name = "fastfood-api"
}

data "aws_api_gateway_rest_api" "eks_api" {
  name = "EKS_API_Gateway"
}

data "aws_eks_cluster_auth" "cluster" {
  name = data.aws_eks_cluster.cluster.name
}

data "kubernetes_secret" "fastfood_secret" {
  metadata {
    name = "fastfood-secret"
  }
}