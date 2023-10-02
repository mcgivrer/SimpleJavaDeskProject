#!/bin/bash

curl -s "https://get.sdkman.io" | bash

source "$HOME/.sdkman/bin/sdkman-init.sh"

sdk env install
sdk env use
export PATH=$PATH:.
ln -s build.sh build
chmod +x build
echo "Read at work!"
