˵��
===

```
/**  ����ͨ������ant�ű���target���� gradle��task ***/
//�������ô���ant ������jar ��
ant.properties['refJar'] =  configurations.compile.asPath  
ant.importBuild('test-build.xml') { antTargetName ->
    'test-' + antTargetName //Ϊ�˷�ֹ��gradle�е�task������ͻ������������������
}

/**
 ���Դ�ӡ��Ant���е�Target
**/
task testSiteMesh {
    doLast { 
        def antTargetsNames = ant.references.get("ant.targets").collect { it.name }
        println "\nAnt Targets: ${antTargetsNames}\n"  
         ant.echo('===== Finish ======')
        } 
}


```

- ����ʹ��
 - `gradle testSiteMesh` ��ӡ���е�ant���target����
 - `gradle test-sitemeshfilesetWithDecorator` ����build.xml�ж����`sitemeshfilesetWithDecorator`����