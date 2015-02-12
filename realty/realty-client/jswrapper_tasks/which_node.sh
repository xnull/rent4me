#!/bin/sh

SCRIPT_DIR=$(dirname $0)

NODE_VERSION='v0.10.33-linux-x64'
NODE_DIR="${SCRIPT_DIR}/../../.gradle/nodejs/node-${NODE_VERSION}"
echo "${NODE_DIR}"
