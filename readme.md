# Exeute Restful HTTP Reqeust in VSCODE

[2025/10/23]



## Run httpbin server by docker 

```shell
docker run -p 80:80 kennethreitz/httpbin
```

## Change the 'baseURL' in .vscode/settings.json

```json
{

   "rest-client.environmentVariables": {
    "$shared": {
        ...
        "baseURL": "http://localhost"
    },
```