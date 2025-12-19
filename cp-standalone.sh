#!/bin/bash
docker-compose cp wildfly:/opt/jboss/wildfly/standalone/configuration/standalone.xml ./$*
