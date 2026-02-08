#!/bin/bash

if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <TestFile.java>"
    exit 1
fi

jbang  cli@junit-team execute --scan-classpath -cp `jbang info classpath  $1` 
