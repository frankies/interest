#!/bin/bash
set -exuo pipefail
cp /opt/jboss/wildfly/standalone/*.war /opt/jboss/wildfly/standalone/deployments/
cp -R /opt/jboss/wildfly/standalone/configuration2/* /opt/jboss/wildfly/standalone/configuration


/opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 
#-Djava.util.logging.manager=org.jboss.logmanager.LogManager  -Djava.util.logging.config.file="/opt/jboss/wildfly/standalone/tmp/logging.properties"