# Kafka Tiered Storage in the Azure Cloud

## Architecture


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

1. Open a terminal in the root folder of the project
2. Create an AKS cluster by running ``` aks-cluster.sh``` in the terminal



## Deployment scripts

### aks-cluster.sh
Contains the Azure CLI commands to create an AKS cluster by executing the following steps:
1. Create a resource group
2. Create an Azure Container Registry
3. Create an AKS cluster
4. Run a helm chart

If you want to delete the cluster run
``` az group delete --name clc3-diesenreiter-stelzer --yes --no-wait ```


### blob-storage.sh

Contains the Azure CLI commands to create a Azure blob storage by executing the following steps:
1. Create a storage account in the same resource group as the AKS ckuster (note that you need to run aks-cluster.sh beforehand for the resource group to be created)
2. Create a container to store blobs
3. Get the storage account key
4. Export connection varibles to an environment file


If you want to delete the cluster run
``` az group delete --name clc3-diesenreiter-stelzer --yes --no-wait ```


