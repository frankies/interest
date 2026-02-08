///usr/bin/env jbang "$0" "$@" ; exit $?
// Give a sample code of jbang script
//DEPS ch.qos.logback:logback-classic:1.5.18

void main() {
    var log = org.slf4j.LoggerFactory.getLogger("MyApp");
    log.info("Hello, JBang with Logback!");
}
