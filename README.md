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
    * Create an AKS cluster and install the Storage Application
        ```shell 
        ./azure/install-cluster.sh 
        ```
    * Upgrade the Storage Application and Kafka
        ``` shell
        ./azure/upgrade-cluster.sh
        ```
4. Generate data using the __Client Application__
    * Create a Jar file
    * Open a terminal in the client folder of the project
    * Run the command 
        ```shell 
        Java –jar app.jar <number_of_customers_to_create> <number_of_articles_to_create> <number_of_invoices_to_create>
        ```

5. Delete all resources 
    ```shell 
    ./azure/remove-cluster.sh 
    ```
    





## Deployment scripts

### install-cluster.sh 
1. Create a Resource Group: Initiates the creation of a resource group to encapsulate the AKS cluster.

2. Create a Storage Account: Generates an account within the resource group where the AKS cluster is located.

3. Create a Container for Blobs: Sets up a container for storing blobs within the created Storage Account.

4. Retrieve the Storage Account Key: Retrieves the key associated with the Storage Account to enable access.

5. Export Connection Variables: Save connection variables as environment variables for retrieval in the Helm files.

6. Create an AKS Cluster: Execute commands to create the AKS cluster and provide a scalable and managed Kubernetes service.

7. Run two Helm Charts: Execute charts on the newly created AKS cluster to start Kafka and the Storage Application.


### upgrade-cluster.sh 
1. Retrieve the Storage Account Key: Retrieves the key associated with the Storage Account to enable access.

2. Execute two Helm Charts: Executes charts on the newly created AKS cluster to upgrade Kafka and the application.



### remove-cluster.sh 
Delete the Resource Group and thereby all resources created on Azure.


