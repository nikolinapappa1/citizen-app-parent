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

# --- SECURITY GROUPS ---

# 1. Security Group για τον Load Balancer (Δέχεται HTTP από έξω)
resource "aws_security_group" "lb_sg" {
  name        = "alb-security-group"
  description = "Allow HTTP traffic to Load Balancer"
  vpc_id      = var.vpc_id

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# 2. Security Group για τα 3 App VMs (Δέχονται κίνηση ΜΟΝΟ από τον Load Balancer)
resource "aws_security_group" "app_sg" {
  name        = "app-security-group"
  description = "Allow traffic from LB to App VMs"
  vpc_id      = var.vpc_id

  ingress {
    from_port       = 8080
    to_port         = 8080
    protocol        = "tcp"
    security_groups = [aws_security_group.lb_sg.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# 3. Security Group για το DB VM (Δέχεται κίνηση ΜΟΝΟ από τα App VMs)
resource "aws_security_group" "db_sg" {
  name        = "db-security-group"
  description = "Allow MySQL traffic from App VMs"
  vpc_id      = var.vpc_id

  ingress {
    from_port       = 3306
    to_port         = 3306
    protocol        = "tcp"
    security_groups = [aws_security_group.app_sg.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# --- INSTANCES ---

# 1. Στιγμιότυπο Βάσης Δεδομένων (DBMS)
resource "aws_instance" "db_instance" {
  ami                    = var.dbms_ami_id
  instance_type          = "t2.micro"
  vpc_security_group_ids = [aws_security_group.db_sg.id]
  subnet_id              = var.subnet_ids[0]

  tags = {
    Name = "DBMS-MySQL-Instance"
  }
}

# 2. Τρία (3) Στιγμιότυπα Spring Boot RESTful Yπηρεσίας
resource "aws_instance" "app_instances" {
  count                  = var.app_instance_count
  ami                    = var.springboot_ami_id
  instance_type          = "t2.micro"
  vpc_security_group_ids = [aws_security_group.app_sg.id]
  subnet_id              = var.subnet_ids[count.index % length(var.subnet_ids)]

  user_data = <<-EOF
              #!/bin/bash
              echo "DB_HOST=${aws_instance.db_instance.private_ip}" >> /etc/environment
              EOF

  tags = {
    Name = "SpringBoot-App-Instance-${count.index + 1}"
  }
}

# --- LOAD BALANCER ---

resource "aws_lb" "app_lb" {
  name               = "citizen-app-lb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.lb_sg.id]
  subnets            = var.subnet_ids
}

resource "aws_lb_target_group" "app_tg" {
  name     = "citizen-app-tg"
  port     = 8080
  protocol = "HTTP"
  vpc_id   = var.vpc_id

  health_check {
    path                = "/actuator/health"
    port                = "8080"
    healthy_threshold   = 2
    unhealthy_threshold = 2
  }
}

resource "aws_lb_target_group_attachment" "app_tg_attach" {
  count            = var.app_instance_count
  target_group_arn = aws_lb_target_group.app_tg.arn
  target_id        = aws_instance.app_instances[count.index].id
  port             = 8080
}

resource "aws_lb_listener" "front_end" {
  load_balancer_arn = aws_lb.app_lb.arn
  port              = "80"
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.app_tg.arn
  }
}