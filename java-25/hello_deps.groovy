///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.codehaus.groovy:groovy:3.0.9
//DEPS org.apache.commons:commons-lang3:3.12.0

import org.apache.commons.lang3.StringUtils

def message = "hello world"
println StringUtils.capitalize(message)
