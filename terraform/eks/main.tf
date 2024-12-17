locals {
  postgres_user     = base64decode(data.kubernetes_secret.fastfood_secret.data["POSTGRES_USER"])
  postgres_password = base64decode(data.kubernetes_secret.fastfood_secret.data["POSTGRES_PASSWORD"])
  fastfood_mail_password = base64decode(data.kubernetes_secret.fastfood_secret.data["FASTFOOD_MAIL_PASSWORD"])
}

# Deployment do FastFood API
resource "kubernetes_manifest" "fastfood_deployment" {
  manifest = {
    apiVersion = "apps/v1"
    kind       = "Deployment"
    metadata = {
      name = "fastfood-api"
      namespace = "default"
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
            image = "guilhermemmnn/fastfood-api:latest"
            ports = [{
              containerPort = 8080
            }]
            env = [
              {
                name  = "FASTFOOD_DATABASE_URL"
                value = "jdbc:postgresql://${data.aws_db_instance.rds.endpoint}/${data.aws_db_instance.rds.db_name}"
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
                value = local.postgres_user
              },
              {
                name = "FASTFOOD_DATABASE_PASSWORD"
                value = local.postgres_password
              },
              {
                name = "FASTFOOD_MAIL_PASSWORD"
                value = local.fastfood_mail_password
              },
              {
                name  = "PAYMENT_API_URL"
                value = "https://${data.aws_api_gateway_rest_api.eks_api.id}.execute-api.us-east-1.amazonaws.com/prod/payments"
              },
              {
                name  = "API_GATEWAY_URL"
                value = "https://${data.aws_api_gateway_rest_api.eks_api.id}.execute-api.us-east-1.amazonaws.com/prod"
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
  manifest = {
    apiVersion = "v1"
    kind       = "Service"
    metadata = {
      name = "fastfood-api"
      namespace = "default"
    }
    spec = {
      selector = {
        app = "fastfood-api"
      }
      ports = [{
        port       = 80
        protocol   = "TCP"
        targetPort = 8080
      }]
      type = "LoadBalancer"
    }
  }
}

# Deploy do Horizontal Pod Autoscaler (HPA)
resource "kubernetes_manifest" "fastfood_hpa" {
  manifest = {
    apiVersion = "autoscaling/v2"
    kind       = "HorizontalPodAutoscaler"
    metadata = {
      name = "fastfood-api-hpa"
      namespace = "default"
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