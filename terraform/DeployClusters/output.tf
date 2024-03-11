output "resource-ids" {
  value = <<-EOT
  Environment ID:   ${confluent_environment.staging.id}
  Kafka Cluster ID: ${confluent_kafka_cluster.basic.id}
  Schema Registry Cluster ID: ${confluent_schema_registry_cluster.essentials.id}
  Bootstrap Endpoint: ${confluent_kafka_cluster.bootstrap_endpoint}

  Service Accounts and their Kafka API Keys (API Keys inherit the permissions granted to the owner):

  API key and Secret to use in producer and Consumer configurations
  ${confluent_service_account.app-producer-consumer.display_name}:                    ${confluent_service_account.app-producer-consumer.id}
  ${confluent_service_account.app-producer-consumer.display_name}'s Kafka API Key:    "${confluent_api_key.app-producer-consumer-kafka-api-key.id}"
  ${confluent_service_account.app-producer-consumer.display_name}'s Kafka API Secret: "${confluent_api_key.app-producer-consumer-kafka-api-key.secret}"

  Schema Registry API key and Secret 
  ${confluent_service_account.env-manager.display_name}:                    ${confluent_service_account.env-manager.id}
  ${confluent_service_account.env-manager.display_name}'s Kafka API Key:    "${confluent_api_key.env-manager-schema-registry-api-key.id}"
  ${confluent_service_account.env-manager.display_name}'s Kafka API Secret: "${confluent_api_key.env-manager-schema-registry-api-key.secret}"

  EOT

  sensitive = true
}