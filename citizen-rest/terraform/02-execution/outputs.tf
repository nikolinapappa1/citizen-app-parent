output "load_balancer_dns" {
  value       = aws_lb.app_lb.dns_name
  description = "Το δημόσιο DNS URL του Load Balancer για προσπέλαση της εφαρμογής"
}

output "db_private_ip" {
  value       = aws_instance.db_instance.private_ip
  description = "Η ιδιωτική IP της Βάσης Δεδομένων"
}