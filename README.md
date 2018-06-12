[[TOC]]

# Web页面调用客户端的本地应用的例子

1.先执行 `01.prepare.reg`文件，将信息导入到注册表中


 ```
 Windows Registry Editor Version 5.00

[HKEY_CLASSES_ROOT\test]
"URL Protocol"=""
@="URL:test Protocol"

[HKEY_CLASSES_ROOT\test\Shell]

[HKEY_CLASSES_ROOT\test\Shell\Open]

[HKEY_CLASSES_ROOT\test\Shell\Open\Command]
@="\"D:\\applet-test\\test.bat\" \"%1\""
 ```

**说明:**
​    
  - `test`是自定义的链接访问协议，`"URL Protocol"=""`是关键。
  - `HKEY_CLASSES_ROOT\test\Shell\Open\Command` 的键值是指定`exe`可执行程序的完整路径，请修改成
       `test.bat`存放的路径。

2. 双击`test.html`打开页面，点击"test"链接