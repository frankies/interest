///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.3
//DEPS info.picocli:picocli-codegen:4.6.3
//NATIVE_OPTIONS --no-fallback -H:+ReportExceptionStackTraces

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "greet", mixinStandardHelpOptions = true)
class GreetApp implements Runnable {
    @Parameters(description = "Name to greet")
    String name = "World";

    public static void main(String[] args) {
        new CommandLine(new GreetApp()).execute(args);
    }

    public void run() {
        System.out.println("Hello " + name + "!");
    }
}
