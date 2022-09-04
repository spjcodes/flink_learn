# dataLeaf
> data leaf flink stream web platform

```mermaid
graph LR
flinkCluster[flinkCluster]
platformBackend[paltformBackEnd]
webPlatform[webPlatformFrontEnd]
executorSql(executorSqlRequest)

executorSql --> webPlatform --> platformBackend --> flinkCluster

```
