FROM jboss/wildfly:latest

ARG APP_WAR=app.war
# 拷贝 WAR 包和 marker 文件到 deployments 目录
COPY --chown=jboss:0  $APP_WAR /opt/jboss/wildfly/standalone/
#COPY --chown=jboss:jboss   logging.properties /opt/jboss/wildfly/standalone/configuration/logging.properties
COPY --chown=jboss:0 --chmod=755 startup.sh /opt/jboss/wildfly/

RUN mv /opt/jboss/wildfly/standalone/configuration /opt/jboss/wildfly/standalone/configuration2

CMD ["/bin/bash", "/opt/jboss/wildfly/startup.sh"]
# CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]