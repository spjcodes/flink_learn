package cn.jiayeli.doris.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CREATE TABLE table1
 * (
 *     siteid INT DEFAULT '10',
 *     citycode SMALLINT,
 *     username VARCHAR(32) DEFAULT '',
 *     pv BIGINT SUM DEFAULT '0'
 * )
 * AGGREGATE KEY(siteid, citycode, username)
 * DISTRIBUTED BY HASH(siteid) BUCKETS 10
 * PROPERTIES("replication_num" = "1");
 *
 * CREATE TABLE table2
 * (
 *     event_day DATE,
 *     siteid INT DEFAULT '10',
 *     citycode SMALLINT,
 *     username VARCHAR(32) DEFAULT '',
 *     pv BIGINT SUM DEFAULT '0'
 * )
 * AGGREGATE KEY(event_day, siteid, citycode, username)
 * PARTITION BY RANGE(event_day)
 * (
 *     PARTITION p201706 VALUES LESS THAN ('2017-07-01'),
 *     PARTITION p201707 VALUES LESS THAN ('2017-08-01'),
 *     PARTITION p201708 VALUES LESS THAN ('2017-09-01')
 * )
 * DISTRIBUTED BY HASH(siteid) BUCKETS 10
 * PROPERTIES("replication_num" = "1");
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLog {

    private int siteid;  // | INT
    private int citycode;  // | SMALLINT
    private String username;  // | VARCHAR(32)
    private long pv;        // | BIGINT
}
