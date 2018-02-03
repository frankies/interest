说明
===

```
/**  可以通过引入ant脚本中target产生 gradle的task ***/
//这里设置传入ant 的引用jar 库
ant.properties['refJar'] =  configurations.compile.asPath  
ant.importBuild('test-build.xml') { antTargetName ->
    'test-' + antTargetName //为了防止与gradle中的task命名冲突，这里允许重命名。
}

/**
 测试打印出Ant所有的Target
**/
task testSiteMesh {
    doLast { 
        def antTargetsNames = ant.references.get("ant.targets").collect { it.name }
        println "\nAnt Targets: ${antTargetsNames}\n"  
         ant.echo('===== Finish ======')
        } 
}


```

- 命令使用
 - `gradle testSiteMesh` 打印所有的ant相关target名称
 - `gradle test-sitemeshfilesetWithDecorator` 调用build.xml中定义的`sitemeshfilesetWithDecorator`任务