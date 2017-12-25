# 自启动脚本安装步骤：

1. 先将.sh 脚本复制到要服务要执行的启动或停止命令的目录下。
2. 对脚本赋于可执行权限，
    `chmod +x install_service.sh.sh`

3. 安装服务：
  - 指定服务运行时操作系统的用户名
    
	``` 
	./install_service.sh -u jboss TestService
	```
	> 这时将于`jboss` 用户运行服务
	
  - 如果没有指定用户名，默认是`jboss`用户。

4. 卸载服务
  
 ```
	./install_service.sh -i TestService
 ```
	
	