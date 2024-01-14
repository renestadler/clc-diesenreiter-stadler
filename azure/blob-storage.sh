#!/bin/bash
# script to create an Azure Blob Storage
# tutorial: https://learn.microsoft.com/en-us/azure/storage/blobs/storage-quickstart-blobs-cli

resourceGroupName='clc3-diesenreiter-stadler'
location='eastus'
storageAccountName='clc3blob'
subscription='TODO'
cotainerName='clc3-blob-storage-container'
$envFilePath='.env'

# Create a storage account
az storage account create --name $storageAccountName --resource-group $resourceGroupName --location $location --sku Standard_ZRS --encryption-services blob


# Get the account key
key=$(az storage account keys list -g $resourceGroupName -n $storageAccountName --query [0].value -o tsv)

# Create a container to store blobs
az storage container create --account-name $storageAccountName --name $cotainerName --account-key $key --auth-mode key

# Write varibales to .env file 
echo "STORAGE_ACCOUNT_NAME=$storageAccountName" > "$envFilePath"
echo "STORAGE_ACCOUNT_KEY=$key" >> "$envFilePath"
echo "STORAGE_ACCOUNT_URL=https://$storageAccountName.blob.core.windows.net" >> "$envFilePath"
echo "STORAGE_ACCOUNT_CONTAINER=$cotainerName" >> "$envFilePath"







