## Deploy the required Confluent Cloud Infrastructure

The [Confluent Provider](https://registry.terraform.io/providers/confluentinc/confluent/latest/docs) is a plugin for Terraform that allows for the lifecycle management of Confluent resources. 

Skip this step if you have already have a working Confluent Cloud environment and you have the requirements fulfilled as stated below.  


Clone the project if you do not have it locally.

```sh
git clone https://github.com/lokeshallam/ISO20022-message-processing
```

Navigate to the terraform folder. Export the required environment variables , Confluent Cloud API key and secret.

```sh
cd terraform/DeployClusters
terraform init
terraform plan

```



Review and apply the changes

```sh
terraform apply
```

Capture the API key and secret for Kafka cluster and Schema Registry cluster and update cloud connection connection configuration file for the producer and consumer applciations.



