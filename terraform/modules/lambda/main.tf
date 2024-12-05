# Cria recurso lambda
resource "aws_lambda_function" "eks_invoker" {
  function_name = "eks-invoker"
  runtime       = "python3.9"
  handler       = "lambda_function.lambda_handler"
  role          = var.labRole
  filename      = "lambda_function.zip"

  environment {
    variables = {
      CLUSTER_NAME = "fastfood-api"
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
}