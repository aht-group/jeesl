#!/bin/bash

# Read devDependencies from package.json
devDependencies=$(jq -r '.devDependencies | keys[]' package.json)

# Loop over each dependency and install it globally
for package in $devDependencies
do
    echo "Installing $package globally..."
    npm install -g "$package"
done

echo "All dev dependencies packages have been installed globally."

# Read devDependencies from package.json
dependencies=$(jq -r '.dependencies | keys[]' package.json)

# Loop over each dependency and install it globally
for package in $dependencies
do
    echo "Installing $package globally..."
    npm install -g "$package"
done

echo "All packages have been installed globally."