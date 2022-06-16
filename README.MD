# 科洛服务核心系列mod

### 当前模组

1. koroworldserver-datasave
2. koroworldserver-multiserver

### 配置文件：
#### datasave
没有特别的配置
#### multiserver
MultiServer.setting
```properties
#右边为默认值
#是否返回上一次下线服务器
BackLastServer = true
#默认服务器
defaultServer = lobby
```
servers.json
```json
{
  "Server_Name": "Server_Address"
}
```
服务器地址可以使用srv的mc地址。
**重要！**
必须能从外网访问！