variable "confluent_cloud_api_key" {
  description = "Confluent Cloud API Key"
  type        = string
  default = ""   #Add the Confluent Cloud API Key
}

variable "confluent_cloud_api_secret" {
  description = "Confluent Cloud API Secret"
  type        = string
  sensitive   = true
  default = ""    #Add the Confluent Cloud API secret
}


variable "kafkacluster_id" {
  description = "Confluent Cloud Kafka cluster ID"
  type        = string
  default = ""   #Add the Confluent Cloud API Key
}


variable "environment_id" {
  description = "Confluent Environment ID"
  type        = string
  default = ""   #Add the Confluent Cloud API Key
}

variable "topic_name_xml" {
  description = "Confluent Cloud topic name"
  type        = string
  sensitive   = false
  default = ""    #Kafka Topic name
}

variable "topic_name_avro" {
  description = "Confluent Cloud topic name"
  type        = string
  sensitive   = false
  default = ""    #Kafka Topic name
}

variable "schemaregistry_id" {
  description = "Confluent Schema Registry ID"
  type        = string
  default = ""   #Add the Confluent Cloud API Key
}

variable "schema_registry_api_key" {
  description = "Confluent Schema Registry API Key"
  type        = string
  default = ""   #Add the Confluent Cloud API Key
}

variable "schema_registry_api_secret" {
  description = "Confluent Schema Registry API Secret"
  type        = string
  sensitive   = true
  default = ""    #Add the Confluent Cloud API secret
}
