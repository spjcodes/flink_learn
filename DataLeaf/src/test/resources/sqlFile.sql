set "sql-client.verbose" = true;
CREATE TABLE userBehavior (
                              `user_id` BIGINT,
                              `item_id` BIGINT,
                              `behavior` STRING,
                              `ts` TIMESTAMP(3) METADATA FROM 'timestamp'
) WITH (
    'connector' = 'kafka',
    'topic' = 'user_behavior',
    'properties.bootstrap.servers' = 'node02:9092',
    'properties.group.id' = 'testGroup',
    'scan.startup.mode' = 'latest-offset',
    'format' = 'json'
);

select * from userBehavior;

create table produceInfo （
`item_id` STRING,
`name` STRING,
`price` INTEGER
) WITH (
    "connector" = "kafka",
    "topic" = "produceInfo",
    "properties.bootstrap.servers" = "node02:9092,node03:9092,node05:9092",
    "properties.group.id" = "cg1",
    "scan.startup.mode" = "latest-offset",
    "format" = "json"
);

select item_id, `name`, price from userBehavior left join produceInfo on userBehavior.item_id = produceInfo.item_id;