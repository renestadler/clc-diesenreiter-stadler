#!/bin/bash
# script to deploy a Azure Kubernetes Service (AKS) cluster and run a helm chart
# tutorial: https://learn.microsoft.com/en-us/azure/aks/quickstart-helm?tabs=azure-cli

# Set variables 
resourceGroupName='clc3-diesenreiter-stadler'
location='eastus'
containerRegistryName='clc3helmacr'
aksClusterName='clc3-aks-cluster'
appImageName='clc3-app:v1'
dockerFileName='Dockerfile' 
helmChartName='clc3-helm-chart'
helmChartDirectory='clc3-helm-chart-dir/'

# Create a resource group
az group create --name $resourceGroupName --location $location

# Create an Azure Container Registry
az acr create --resource-group $resourceGroupName --name $containerRegistryName --sku Basic

# Create an AKS cluster
az aks create --resource-group $resourceGroupName --name $aksClusterName --location $location --attach-acr $containerRegistryName --generate-ssh-keys
az aks install-cli # Install kubectl locally
az aks get-credentials --resource-group $resourceGroupName --name $aksClusterName # Connect to the cluster

# Build and push image to the Azure Container Registry
az acr build --image $appImageName --registry $containerRegistryName --file $dockerFileName . #docker file wird nur gebraucht, wenn da vorgaben zum build drin sind

# Run a Helm chart
helm install $helmChartName $helmChartDirectory


# Clean up resources
az group delete --name $resourceGroupName --yes --no-wait
