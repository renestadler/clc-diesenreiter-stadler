#!/bin/bash

# Set variables 
resourceGroupName='clc3-diesenreiter-stadler'
location='westeurope'
storageAccountName='clc3blobdiesenstadler'
containerName='clc3-blob-storage-container'
aksClusterName='clc3-aks-cluster'
helmKafkaChartName='clc3-kafka-chart'
helmKafkaChartDirectory='../helm/kafka/'
helmApplicationChartName='clc3-application-chart'
helmApplicationChartDirectory='../helm/tiered-storage-application/'

# Create a resource group
az group create --name $resourceGroupName --location $location

# Create a storage account
az storage account create --name $storageAccountName --resource-group $resourceGroupName --location $location --sku Standard_ZRS --encryption-services blob

# Get the account key
accountKey=$(az storage account keys list -g $resourceGroupName -n $storageAccountName --query [0].value -o tsv)

# Create a container to store blobs
az storage container create --account-name $storageAccountName --name $containerName --account-key $accountKey --auth-mode key

# Create an AKS cluster
az aks create --resource-group $resourceGroupName --name $aksClusterName --location $location --generate-ssh-keys
sudo az aks install-cli # Install kubectl locally
az aks get-credentials --overwrite-existing --resource-group $resourceGroupName --name $aksClusterName # Connect to the cluster

export STORAGE_ACCOUNT_NAME="$storageAccountName"
export STORAGE_ACCOUNT_KEY="$accountKey"
export STORAGE_ACCOUNT_URL="https://$storageAccountName.blob.core.windows.net/"
export STORAGE_ACCOUNT_CONTAINER="$containerName"

helm repo add strimzi https://strimzi.io/charts/

helm install strimzi-operator --wait strimzi/strimzi-kafka-operator

# Run a Helm chart
helm install --set azure.container=$STORAGE_ACCOUNT_CONTAINER --set azure.endpoint=$STORAGE_ACCOUNT_URL --set azure.account.name=$STORAGE_ACCOUNT_NAME --set azure.account.key=$STORAGE_ACCOUNT_KEY --wait --wait-for-jobs $helmKafkaChartName $helmKafkaChartDirectory

# Run a Helm chart
helm install $helmApplicationChartName --wait $helmApplicationChartDirectory


