We will now register the avro schema generated in above step in Confluent Schema Registry using Confluent Terraform Provider.


Clone the project if you do not have it locally.

```sh
git clone https://github.com/lokeshallam/ISO20022-message-processing
```


```sh
cd terraform/RegisterSchemaDefinition
```


Update the schema file location under the two `confluent_schema` blocks in main.tf file.

```sh
schema = file("Schema File location") << Update the location 

```

Export the required environment variables for this module along with Confluent Cloud API key and secret.

```sh
export TF_VAR_kafkacluster_id=lkc-xxxx
export TF_VAR_schemaregistry_id=lsrc-xxxx

export TF_VAR_environment_id=env-xxxxx
export TF_VAR_schema_registry_api_key=xxxx
export TF_VAR_schema_registry_api_secret=xxxx
export TF_VAR_topic_name_xml=payment-iso-remittance-xml
export TF_VAR_topic_name_avro=payment-iso-remittance


terraform init
terraform plan 


```

Review the changes and apply

```sh
terraform apply
```


Schema is now registered in Confluent Schema Registry.