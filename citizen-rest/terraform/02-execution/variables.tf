variable "aws_region" {
  type        = string
  default     = "eu-central-1"
  description = "Περιοχή AWS"
}

variable "vpc_id" {
  type        = string
  description = "Το ID του VPC"
}

variable "subnet_ids" {
  type        = list(string)
  description = "Λίστα από Subnet IDs για τα VMs και τον Load Balancer"
}

variable "springboot_ami_id" {
  type        = string
  description = "To AMI ID της Spring Boot εφαρμογής (από τη φάση preparation)"
}

variable "dbms_ami_id" {
  type        = string
  description = "To AMI ID της Βάσης Δεδομένων (από τη φάση preparation)"
}

variable "app_instance_count" {
  type        = number
  default     = 3
  description = "Αριθμός στιγμιοτύπων της Spring Boot υπηρεσίας"
}