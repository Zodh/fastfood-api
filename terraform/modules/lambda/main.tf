provider "aws" {
  region = var.aws_region
}

data "aws_eks_cluster" "cluster" {
  name = "fastfood-api"
}

data "aws_eks_cluster_auth" "cluster" {
  name = data.aws_eks_cluster.cluster.name
}

data "aws_db_instance" "rds" {
  db_instance_identifier = "mydb-instance"
}

data "kubernetes_secret" "fastfood_secret" {
  metadata {
    name = "fastfood-secret"
  }
}

provider "kubernetes" {
  host                   = data.aws_eks_cluster.cluster.endpoint
  cluster_ca_certificate = base64decode(data.aws_eks_cluster.cluster.certificate_authority[0].data)
  token                  = data.aws_eks_cluster_auth.cluster.token
}

locals {
  postgres_user     = base64decode(data.kubernetes_secret.fastfood_secret.data["POSTGRES_USER"])
  postgres_password = base64decode(data.kubernetes_secret.fastfood_secret.data["POSTGRES_PASSWORD"])
}

# Cria recurso lambda
resource "aws_lambda_function" "eks_invoker" {
  function_name = "eks-invoker"
  runtime       = "nodejs20.x"  # Atualizado para Node.js 20
  handler       = "index.lambdaHandler"  # Handler no código Node.js (ajustado para Node.js)
  role          = var.labRole
  filename      = "lambda_function.zip"  # Caminho para o arquivo zip do código Lambda

  environment {
    variables = {
      CLUSTER_NAME = "fastfood-api"
      DB_NAME      = "postgres"
      DB_HOST      = data.aws_db_instance.rds.endpoint
      DB_USER      = local.postgres_user
      DB_PASSWORD  = local.postgres_password
    }
  }
}


# Cria recurso API Gateway
resource "aws_api_gateway_rest_api" "eks_api" {
  name = "EKS_API_Gateway"
}

# Recurso dinâmico base "/{proxy+}"
resource "aws_api_gateway_resource" "proxy" {
  rest_api_id = aws_api_gateway_rest_api.eks_api.id
  parent_id   = aws_api_gateway_rest_api.eks_api.root_resource_id
  path_part   = "{proxy+}"
}

# Método para todas as requisições
resource "aws_api_gateway_method" "proxy_method" {
  rest_api_id   = aws_api_gateway_rest_api.eks_api.id
  resource_id   = aws_api_gateway_resource.proxy.id
  http_method   = "ANY"
  authorization = "NONE"
}

# Integração com o Lambda
resource "aws_api_gateway_integration" "lambda_integration" {
  rest_api_id = aws_api_gateway_rest_api.eks_api.id
  resource_id = aws_api_gateway_resource.proxy.id
  http_method = aws_api_gateway_method.proxy_method.http_method

  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = aws_lambda_function.eks_invoker.invoke_arn
}

# Permissão para o Lambda ser invocado pelo API Gateway
resource "aws_lambda_permission" "api_gateway_permission" {
  statement_id  = "AllowExecutionFromAPIGateway"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.eks_invoker.arn
  principal     = "apigateway.amazonaws.com"
}

resource "aws_api_gateway_deployment" "eks_api_deployment" {
  rest_api_id = aws_api_gateway_rest_api.eks_api.id
  stage_name  = "prod"

  depends_on = [
    aws_api_gateway_method.proxy_method
  ]
}