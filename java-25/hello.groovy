///usr/bin/env jbang "$0" "$@" ; exit $?
//GROOVY 3.0.9

def name = args.length > 0 ? args[0] : "World"
println "Hello $name"
