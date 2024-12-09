#!/bin/bash

if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <github_pat>"
    exit 1
fi

# We are storing the curl result in a variable because otherwise the stdin would be closed and the eval would not work resulting in a "curl: (23) Failed writing body" error
curl --silent --fail https://raw.githubusercontent.com/food2gether/flux-base/refs/heads/main/bin/minikube-setup.sh | bash /dev/stdin "$1" "session-service"
