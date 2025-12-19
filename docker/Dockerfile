FROM eclipse-temurin:8-jdk

MAINTAINER amoy

# Set the WILDFLY_VERSION env variable
ENV WILDFLY_VERSION 20.0.1.Final
ENV WILDFLY_SHA1 95366b4a0c8f2e6e74e3e4000a98371046c83eeb
ENV JBOSS_HOME /opt/jboss/wildfly

USER root

#COPY wildfly-$WILDFLY_VERSION.tar.gz /tmp/

RUN useradd -m -s /bin/bash jboss

# Add the WildFly distribution to /opt, and make wildfly the owner of the extracted tar content
# Make sure the distribution is available from a well-known place
RUN mkdir /opt/jboss \
    && cd $HOME \
    && curl -O https://download.jboss.org/wildfly/$WILDFLY_VERSION/wildfly-$WILDFLY_VERSION.tar.gz \
    && sha1sum wildfly-$WILDFLY_VERSION.tar.gz | grep $WILDFLY_SHA1 \
#    && mv /tmp/wildfly-$WILDFLY_VERSION.tar.gz ./ \
    && tar xf wildfly-$WILDFLY_VERSION.tar.gz \
    && mv $HOME/wildfly-$WILDFLY_VERSION $JBOSS_HOME \
    && rm wildfly-$WILDFLY_VERSION.tar.gz \
    && chown -R jboss:0 ${JBOSS_HOME} \
    && chmod -R g+rw ${JBOSS_HOME}

# Ensure signals are forwarded to the JVM process correctly for graceful shutdown
ENV LAUNCH_JBOSS_IN_BACKGROUND true

USER jboss

# Expose the ports we're interested in
EXPOSE 8080


##====== Custom ======================
# Copy configuration to target directorys.
# COPY ./build/bin/* /opt/jboss/wildfly/bin/

# # 
COPY ./mgmt-groups.properties /opt/jboss/wildfly/domain/configuration/
COPY ./mgmt-users.properties /opt/jboss/wildfly/domain/configuration/

# #
# COPY ./build/configuration/*.properties /opt/jboss/wildfly/standalone/configuration/
# COPY ./build/configuration/standalone.xml /opt/jboss/wildfly/standalone/configuration/


USER root
RUN chmod +x ${JBOSS_HOME}/bin/standalone.sh && \
    chown -R jboss:0 ${JBOSS_HOME}


# Prevent chinese characters truncating when 'LANG' is empty.
RUN localedef -f UTF-8 -i en_US en_US.UTF-8
ENV LANG="en_US.UTF-8" \
    LANGUAGE="en_US:en" \
    LC_ALL="en_US.UTF-8"

USER jboss
WORKDIR /opt/jboss/wildfly

##==========================================

# HEALTHCHECK --interval=30s --timeout=30s --start-period=5s --retries=3 CMD [ "curl", "-f", "http://localhost:8080" ] || exit 1

# Set the default command to run on boot
# This will boot WildFly in the standalone mode and bind to all interface
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]