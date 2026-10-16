variable "aws_region" {
  type        = string
  default     = "eu-central-1"
  description = "Περιοχή AWS"
}

variable "github_repo" {
  type        = string
  default     = "https://github.com/nikolinapappa1/citizen-app-parent.git"
  description = "Το GitHub repository της Spring Boot εφαρμογής"
}