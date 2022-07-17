-- create a kafka source table
CREATE TABLE KafkaTable (
                            `user_id` BIGINT,
                            `item_id` BIGINT,
                            `behavior` STRING,
                            `ts` TIMESTAMP(3) METADATA FROM 'timestamp'
) WITH (
    'connector' = 'kafka',
    'topic' = 'test',
    'properties.bootstrap.servers' = 'node02:9092,node03:9092',
    'properties.group.id' = 'testGroup',
    'scan.startup.mode' = 'earliest-offset',
    'format' = 'csv'
);
