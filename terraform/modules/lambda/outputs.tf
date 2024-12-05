# Output para o endpoint HTTPS do API Gateway
output "api_gateway_endpoint" {
  value = "https://${aws_api_gateway_rest_api.eks_api.id}.execute-api.us-east-1.amazonaws.com/${aws_api_gateway_deployment.eks_api_deployment.stage_name}"
  description = "Endpoint HTTPS global do API Gateway para acessar os recursos da API."
}