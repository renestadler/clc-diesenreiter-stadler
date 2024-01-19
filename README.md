# Kafka Tiered Storage in the Azure Cloud
In many organisations, the long-term storage of business data is a challenge, especially when it comes to large volumes of data that need to be retained over a long period of time. Traditional storage solutions can be expensive, and scalability and efficiency requirements are often a challenge. In this context, this project plans to evaluate and apply Kafka Tiered Storage in the Azure Cloud to enable efficient long-term data storage of business data.

## Architecture
![Architecture](.\readme-imgs\architecture.png)

### Client Application
Generates a specified number of articles, customers, and invoices. Moreover, it coordinates REST calls to the Storage application, leveraging the dynamically created data. This enables the generation of sufficient data to assess the system's proficiency in handling substantial volumes of information.

### Storage Application
Handles REST calls to store articles, customers, and invoices with streamlined processes for efficient data storage and retrieval. The applications maintains an audit log spanning the last seven years, facilitating easy retrieval for auditing purposes. Kafka is used to establish a connection with Azure Blob Storage.

### Azure Blob Storage
Azure Blob Storage serves as our primary repository for long-term data storage. The log data is stored in 1 MB packages directly in the blob memory. This approach is chosen because the audit data is accessed at most once a month, eliminating the need to store "HOT" data.




## Needed Software
* [Azure CLI](https://learn.microsoft.com/en-us/cli/azure/install-azure-cli-windows?tabs=azure-cli) (version 2.0.64 or later)



## Prequisities
1. Create an [Azure account](https://azure.microsoft.com/en-in/free).
2. Sign in to your azure account using the azure cli.
    * Open a terminal (e.g. powershell) and run the following command
        ```shell
        az login
        ```
    * The Azure CLI will either open your default browser to sign in or  instructs you to open a browser page.



## Deployment

1. Open a terminal in the root folder of the project and run the following commands:
    * Create an AKS cluster 
        ```shell 
        ./azure/aks-cluster.sh 
        ```
    * Create the Blob Storage 
        ``` shell
        ./azure/blob-storage.sh
        ```
4. Generate data using the __Client Application__
    * TODO
    





## Deployment scripts

### aks-cluster.sh
Contains the Azure CLI commands to create an AKS cluster by executing the following steps:
1. Create a resource group
2. Create an Azure Container Registry
3. Create an AKS cluster
4. Run a helm chart

If you want to delete the cluster run
``` shell
az group delete --name clc3-diesenreiter-stelzer --yes --no-wait 
```


### blob-storage.sh

Contains the Azure CLI commands to create a Azure blob storage by executing the following steps:
1. Create a storage account in the same resource group as the AKS ckuster (note that you need to run aks-cluster.sh beforehand for the resource group to be created)
2. Create a container to store .blobs
3. Get the storage account key
4. Export connection varibles to an environment file


If you want to delete the cluster run
```shell
 az group delete --name clc3-diesenreiter-stelzer --yes --no-wait 
 ```


