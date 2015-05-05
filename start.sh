#!/bin/bash
set -e

# Check for maven command.
command -v mvn >/dev/null 2>&1 || { echo >&2 "Command 'mvn' (maven) is requred for this script to run correctly."; exit 1; }

# Compile and run the project.
mvn compile test
