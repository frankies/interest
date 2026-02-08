# My Script Documentation

This is a script written in Markdown that JBang can execute.

```java
class Demo {
    void greet() {
        System.out.println("Hello from Markdown!");
    }
}
```

```jshelllanguage
new Demo().greet();
```

You can even use dependencies:

```jsh
//DEPS com.github.lalyos:jfiglet:0.0.8
import com.github.lalyos.jfiglet.FigletFont;

System.out.println(FigletFont.convertOneLine(
    "Hello " + ((args.length > 0) ? args[0] : "jbang")
));
```

Arguments work too:

```java
if(args.length == 0) {
    System.out.println("You have no arguments!");
} else {
    System.out.printf("You have %s arguments! First is %s%n", args.length, args[0]);
}
```