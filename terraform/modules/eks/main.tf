data "aws_db_instance" "rds" {
  db_instance_identifier = "mydb-instance"
}

# Deployment do FastFood API
resource "kubernetes_manifest" "fastfood_deployment" {
  depends_on = [kubernetes_secret.fastfood_secret]  # Garante que o EKS seja provisionado antes do Kubernetes

  manifest = {
    apiVersion = "apps/v1"
    kind       = "Deployment"
    metadata = {
      name = "fastfood-api"
    }
    spec = {
      replicas = 2
      selector = {
        matchLabels = {
          app = "fastfood-api"
        }
      }
      template = {
        metadata = {
          labels = {
            app = "fastfood-api"
          }
        }
        spec = {
          containers = [{
            name  = "fastfood-api"
            image = "zodh/fastfood-api:latest"
            ports = [{
              containerPort = 8080
            }]
            env = [
              {
                name  = "FASTFOOD_DATABASE_URL"
                value = "jdbc:postgresql://${data.aws_db_instance.rds.endpoint}:5432/${data.aws_db_instance.rds.db_name}"
              },
              {
                name  = "FASTFOOD_API_PORT"
                value = "8080"
              },
              {
                name  = "FASTFOOD_API_ENVIRONMENT"
                value = "dev-local"
              },
              {
                name = "FASTFOOD_DATABASE_USER"
                valueFrom = {
                  secretKeyRef = {
                    name = kubernetes_secret.fastfood_secret.metadata[0].name
                    key  = "POSTGRES_USER"
                  }
                }
              },
              {
                name = "FASTFOOD_DATABASE_PASSWORD"
                valueFrom = {
                  secretKeyRef = {
                    name = kubernetes_secret.fastfood_secret.metadata[0].name
                    key  = "POSTGRES_PASSWORD"
                  }
                }
              },
              {
                name = "FASTFOOD_MAIL_PASSWORD"
                valueFrom = {
                  secretKeyRef = {
                    name = kubernetes_secret.fastfood_secret.metadata[0].name
                    key  = "FASTFOOD_MAIL_PASSWORD"
                  }
                }
              },
              {
                name  = "PAYMENT_API_URL"
                value = "http://payment-api:8081/payments"
              }
            ]
          }]
        }
      }
    }
  }
}

# Deploy do Service
resource "kubernetes_manifest" "fastfood_service" {
  depends_on = [kubernetes_secret.fastfood_secret]  # Garante que o EKS seja provisionado antes do Kubernetes

  manifest = {
    apiVersion = "v1"
    kind       = "Service"
    metadata = {
      name = "fastfood-api"
    }
    spec = {
      selector = {
        app = "fastfood-api"
      }
      ports = [{
        port       = 8080
        protocol   = "TCP"
        targetPort = 8080
      }]
      type = "LoadBalancer"
    }
  }
}

# Deploy do Horizontal Pod Autoscaler (HPA)
resource "kubernetes_manifest" "fastfood_hpa" {
  depends_on = [kubernetes_secret.fastfood_secret]  # Garante que o EKS seja provisionado antes do Kubernetes
  manifest = {
    apiVersion = "autoscaling/v2"
    kind       = "HorizontalPodAutoscaler"
    metadata = {
      name = "fastfood-api-hpa"
    }
    spec = {
      scaleTargetRef = {
        apiVersion = "apps/v1"
        kind       = "Deployment"
        name       = "fastfood-api"
      }
      minReplicas = 1
      maxReplicas = 5
      metrics = [{
        type = "Resource"
        resource = {
          name   = "cpu"
          target = {
            type                  = "Utilization"
            averageUtilization    = 70
          }
        }
      }]
    }
  }
}

resource "kubernetes_secret" "fastfood_secret" {
  metadata {
    name = "fastfood-secret"
  }

  data = {
    POSTGRES_USER          = base64encode(var.postgres_user)
    POSTGRES_PASSWORD      = base64encode(var.postgres_password)
    FASTFOOD_MAIL_PASSWORD = base64encode(var.fastfood_mail_password)
  }

  type = "Opaque"
}

# ============== API GATEWAY ==============

# Criar o API Gateway
resource "aws_apigatewayv2_api" "fastfood_api" {
  name          = "fastfood-api"
  protocol_type = "HTTP"
}

# Recurso para capturar o LoadBalancer criado pelo Kubernetes
data "aws_lb" "fastfood_lb" {
  depends_on = [kubernetes_manifest.fastfood_service]

  # Filtrando o LoadBalancer pela tag criada pelo Kubernetes
  filter {
    name   = "tag:k8s.io/service-name"
    values = ["fastfood-api"]
  }
}

# Recurso para o API Gateway com a integração correta
resource "aws_apigatewayv2_integration" "fastfood_integration" {
  api_id             = aws_apigatewayv2_api.fastfood_api.id
  integration_type   = "HTTP_PROXY"
  integration_uri    = "http://${data.aws_lb.fastfood_lb.dns_name}:8080"
  connection_type    = "INTERNET"
  payload_format_version = "1.0"
}

# Criar rota para encaminhar requisições
resource "aws_apigatewayv2_route" "fastfood_route" {
  api_id    = aws_apigatewayv2_api.fastfood_api.id
  route_key = "ANY /{proxy+}"  # Aceita todas as rotas e métodos HTTP
  target    = "integrations/${aws_apigatewayv2_integration.fastfood_integration.id}"
}

# Configurar o estágio (deployment)
resource "aws_apigatewayv2_stage" "fastfood_stage" {
  api_id      = aws_apigatewayv2_api.fastfood_api.id
  name        = "prod"
  auto_deploy = true
}