#!/usr/bin/env bash

curl -X POST \
  -F "file=@saa.pdf" \
  http://localhost:10091/summarize
