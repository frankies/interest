#!/bin/bash
set -eo pipefail

 find . -name "*.lockfile" |xargs -i rm -f {};
./gradlew -I ../generateLockFile.gradle clean generateAllLockfiles --write-locks -i -V -q 
 
 rm -f scan-results/*

 FILTER='--severity CRITICAL,LOW,UNKNOWN'

 trivy fs  --scanners license --format template --template "@html.tpl" -o scan-results/license-report.html .


 trivy fs  --scanners license  --format json --output scan-results/root-licenses.json . 
 
exit 0
 
 trivy fs -s CRITICAL,LOW,UNKNOWN   --scanners license  --format table --output scan-results/root-licenses.txt .

 trivy fs -s CRITICAL,LOW,UNKNOWN   --scanners license  --table-mode summary --format table   .

 grep -C2 '====' scan-results/root-licenses.txt


echo ""
echo "| Name | CRITICAL | HIGH | UNKNOWN | Total |"
echo "| ---- | -------- | ---- | ------- | ----- |"
awk '
BEGIN { RS=""; FS="\n" }
{
for (i = 1; i <= NF; i++) {
  if ($i ~ /^=+$/) {
    name = $(i-1)
    gsub(/^\s+|\s+$/, "", name)
    gsub(/ \(.*\)/, "", name)
    gsub(/\/gradle\.lockfile/, "", name)
    total_line = $(i+1)
    if (total_line ~ /Total:/) {
        match(total_line, /Total: ([0-9]+)/, m)
        total = m[1]
        match(total_line, /CRITICAL: ([0-9]+)/, m)
        critical = m[1]
        match(total_line, /HIGH: ([0-9]+)/, m)
        high = m[1]
        match(total_line, /UNKNOWN: ([0-9]+)/, m)
        unknown = m[1]
        print "| " name " | " critical " | " high " | " unknown " | " total " |"
    }
 }
 }
}' scan-results/root-licenses.txt