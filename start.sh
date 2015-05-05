#!/bin/bash
set -e

function check_commands {
  if [[ -n $1 ]]; then
    command -v $1 >/dev/null 2>&1 || { echo >&2 "Command '$1' is requred for this script to run correctly."; exit 1; }
    shift
    check_commands $@
  fi
}

# Check for maven command.
check_commands mvn

# Compile and run the project.
mvn compile test
