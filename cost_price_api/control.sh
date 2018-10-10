#!/bin/bash
JARFile="target/cost_price_api-1.0-SNAPSHOT-exec.jar"
PIDFile="cost_price_api.pid"
JVM_OPTS="-Xmx1g $2"

../scripts/control.sh $1 "$JARFile" "$PIDFile" "$JVM_OPTS"
