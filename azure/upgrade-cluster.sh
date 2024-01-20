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

# Get the account key
accountKey=$(az storage account keys list -g $resourceGroupName -n $storageAccountName --query [0].value -o tsv)

# Connect to AKS cluster
sudo az aks install-cli # Install kubectl locally
az aks get-credentials --overwrite-existing --resource-group $resourceGroupName --name $aksClusterName # Connect to the cluster

export STORAGE_ACCOUNT_NAME="$storageAccountName"
export STORAGE_ACCOUNT_KEY="$accountKey"
export STORAGE_ACCOUNT_URL="https://$storageAccountName.blob.core.windows.net/"
export STORAGE_ACCOUNT_CONTAINER="$containerName"

# Run a Helm chart
helm upgrade $helmKafkaChartName --set azure.container=$STORAGE_ACCOUNT_CONTAINER --set azure.endpoint=$STORAGE_ACCOUNT_URL --set azure.account.name=$STORAGE_ACCOUNT_NAME --set azure.account.key=$STORAGE_ACCOUNT_KEY --wait $helmKafkaChartDirectory

# Run a Helm chart
helm upgrade $helmApplicationChartName --wait $helmApplicationChartDirectory


