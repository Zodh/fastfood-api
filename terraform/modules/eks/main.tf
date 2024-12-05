data "aws_db_instance" "rds" {
  db_instance_identifier = "mydb-instance"
}

data "aws_eks_cluster" "cluster" {
  name = "fastfood-api"
}

data "aws_eks_cluster_auth" "cluster" {
  name = data.aws_eks_cluster.cluster.name
}

data "kubernetes_secret" "fastfood_secret" {
  metadata {
    name = "fastfood-secret"
  }
}

locals {
  postgres_user     = base64decode(data.kubernetes_secret.fastfood_secret.data["POSTGRES_USER"])
  postgres_password = base64decode(data.kubernetes_secret.fastfood_secret.data["POSTGRES_PASSWORD"])
  fastfood_mail_password = base64decode(data.kubernetes_secret.fastfood_secret.data["FASTFOOD_MAIL_PASSWORD"])
}

provider "kubernetes" {
  host                   = data.aws_eks_cluster.cluster.endpoint
  cluster_ca_certificate = base64decode(data.aws_eks_cluster.cluster.certificate_authority[0].data)
  token                  = data.aws_eks_cluster_auth.cluster.token
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
            image = "zodh/fastfood-api:latest"
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