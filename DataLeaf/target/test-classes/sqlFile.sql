CREATE TABLE MyTable1
(
    `count` bigint,
    word    VARCHAR(256)
)
WITH ('connector' = 'datagen');

CREATE TABLE MyTable2
(
    `count` bigint,
    word    VARCHAR(256)
)
WITH ('connector' = 'datagen');

/*
SELECT `count`, word
FROM MyTable1
WHERE word LIKE 'F%'
UNION ALL
SELECT `count`, word
FROM MyTable2;
*/

SET 'table.local-time-zone' = 'Europe/Berlin';

EXPLAIN PLAN FOR SELECT `count`, word FROM MyTable1 WHERE word LIKE 'F%';