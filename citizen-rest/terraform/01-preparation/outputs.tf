output "springboot_ami_id" {
  value       = aws_ami_from_instance.springboot_app_ami.id
  description = "To ID της εικόνας για τα Spring Boot VMs"
}

output "dbms_ami_id" {
  value       = aws_ami_from_instance.dbms_ami.id
  description = "To ID της εικόνας για το DBMS VM"
}