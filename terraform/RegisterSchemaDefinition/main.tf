terraform {
  required_providers {
    confluent = {
      source  = "confluentinc/confluent"
      version = "1.65.0"
    }
  }
}

provider "confluent" {
  cloud_api_key    = var.confluent_cloud_api_key
  cloud_api_secret = var.confluent_cloud_api_secret
}



data "confluent_schema_registry_cluster" "schema_registry" {
  id = var.schemaregistry_id
  environment {
    id = var.environment_id
  }

}

data "confluent_kafka_cluster" "basic" {
  id = var.kafkacluster_id
  environment {
    id = var.environment_id
  }
}


resource "confluent_schema" "remittanceAdvice_xml" {
  schema_registry_cluster {
    id = data.confluent_schema_registry_cluster.schema_registry.id
  }
  rest_endpoint = data.confluent_schema_registry_cluster.schema_registry.rest_endpoint
  # https://developer.confluent.io/learn-kafka/schema-registry/schema-subjects/#topicnamestrategy
  subject_name = "${var.topic_name_xml}-value"
  format = "AVRO"
  schema = file("../../xml-avro-schema-generator/src/main/resources/remittanceAdvice.asvc")
  credentials {
    key    = var.schema_registry_api_key
    secret = var.schema_registry_api_secret
  }
}

resource "confluent_schema" "remittanceAdvice_avro" {
  schema_registry_cluster {
    id = data.confluent_schema_registry_cluster.schema_registry.id
  }
  rest_endpoint = data.confluent_schema_registry_cluster.schema_registry.rest_endpoint
  # https://developer.confluent.io/learn-kafka/schema-registry/schema-subjects/#topicnamestrategy
  subject_name = "${var.topic_name_avro}-value"
  format = "AVRO"
  schema = file("../../xml-avro-schema-generator/src/main/resources/remittanceAdvice.asvc")
  credentials {
    key    = var.schema_registry_api_key
    secret = var.schema_registry_api_secret
  }
} 