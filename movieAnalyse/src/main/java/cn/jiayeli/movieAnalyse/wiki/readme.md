# data flow

```mermaid
graph LR
1(dataGenerator) --> 2[mysql] -.-> 3((flinkCDC)) --sync.->  4{{kafka}} ==> 5>phoenix]

```




