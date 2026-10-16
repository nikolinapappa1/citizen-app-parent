terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

# 1. Δημιουργία Εικόνας (AMI) για τη Spring Boot RESTful Υπηρεσία
resource "aws_ami_from_instance" "springboot_app_ami" {
  name               = "citizen-rest-app-image"
  source_instance_id = "i-1234567890abcdef0" # ID προσωρινού VM builder

  tags = {
    Name        = "SpringBoot-CitizenRest-AMI"
    Environment = "Preparation"
  }
}

# 2. Δημιουργία Εικόνας (AMI) για το ΣΔΒΔ (DBMS)
resource "aws_ami_from_instance" "dbms_ami" {
  name               = "citizen-dbms-image"
  source_instance_id = "i-00987654321fedcba" # ID προσωρινού VM builder

  tags = {
    Name        = "DBMS-MySQL-AMI"
    Environment = "Preparation"
  }
}