use movieInfo;

show tables;

-- 电影信息表
drop table if exists movieInfo.movies;
create table if not exists movieInfo.movies (
    movieId bigint(10) primary key
    ,movieTitle        varchar(128)
    ,releaseDate       varchar(26)
    ,videoReleaseDate  varchar(26)
    ,IMDbURL           varchar(128)
    ,`unknown`         varchar(24)
    ,`Action`          varchar(24)
    ,Adventure         varchar(24)
    ,Animation         varchar(24)
    ,Childrens         varchar(24)
    ,Comedy            varchar(24)
    ,Crime             varchar(24)
    ,Documentary       varchar(24)
    ,Drama             varchar(24)
    ,Fantasy           varchar(24)
    ,FilmNoir          varchar(24)
    ,Horror            varchar(24)
    ,Musical           varchar(24)
    ,Mystery           varchar(24)
    ,Romance           varchar(24)
    ,SciFi             varchar(24)
    ,Thriller          varchar(24)
    ,War               varchar(24)
    ,Western           varchar(24)
    );

-- 用户电影评分信息表
drop table if exists movieInfo.userMovieRatingInfo;
CREATE TABLE `userMovieRatingInfo` (
    `userId` varchar(10) NOT NULL,
    `age` varchar(3) DEFAULT NULL,
    `gender` varchar(1) DEFAULT NULL,
    `occupation` varchar(20) DEFAULT NULL,
    `zipCode` varchar(10) DEFAULT NULL,
    `movieId` varchar(10) NOT NULL,
    `movieTitle` varchar(128) DEFAULT NULL,
    `releaseDate` varchar(20) DEFAULT NULL,
    `videoReleaseDate` varchar(20) DEFAULT NULL,
    `IMDbURL` varchar(256) DEFAULT NULL,
    `type` varchar(180) DEFAULT NULL,
    `rating` int DEFAULT NULL,
    `timestamp` varchar(14) DEFAULT NULL,
    updateTime timestamp default CURRENT_TIMESTAMP,
    createTime timestamp default CURRENT_TIMESTAMP,
    PRIMARY KEY (`userId`, `movieId`)
);

------------------------------------------------- doris --------------------------------------------------------------------
-- doris table
create table if not exists dblearn.movies (
     movieId bigint
    ,movieTitle        varchar(128)
    ,releaseDate       varchar(26)
    ,videoReleaseDate  varchar(26)
    ,IMDbURL           varchar(128)
    ,`unknown`         tinyint
    ,`Action`          tinyint
    ,Adventure         tinyint
    ,Animation         tinyint
    ,Childrens         tinyint
    ,Comedy            tinyint
    ,Crime             tinyint
    ,Documentary       tinyint
    ,Drama             tinyint
    ,Fantasy           tinyint
    ,FilmNoir          tinyint
    ,Horror            tinyint
    ,Musical           tinyint
    ,Mystery           tinyint
    ,Romance           tinyint
    ,SciFi             tinyint
    ,Thriller          tinyint
    ,War               tinyint
    ,Western           tinyint
)
ENGINE=olap
DUPLICATE KEY(movieId, movieTitle)
DISTRIBUTED BY HASH(movieId) BUCKETS 3
PROPERTIES (
  "replication_num" = "1"
);
/*import data
-- stream load
curl --location-trusted -u root:root.123 \
-H "label:movie_all_01" -H "column_separator:|" \
-T  http://node01:8030/api/dblearn/movies/_stream_load

-- broker load
LOAD LABEL dblearn.tb_aggsp_201707081
 (
 DATA INFILE("hdfs://node01:8020/tmp/u.item")
 INTO TABLE `movies` COLUMNS TERMINATED BY "|"
 )WITH BROKER broker_name
(
 "username"="root",
 "password"="root"
)
PROPERTIES
(
 "timeout"="3600",
 "max_filter_ratio"="0.1"
);

 */


-- 用户电影评分信息表
-- 用户电影评分信息表
drop table if exists dblearn.userMovieRatingInfo;
CREATE TABLE if not exists `userMovieRatingInfo` (
    `userId` varchar(10) NOT NULL,
    `age` smallint (3) DEFAULT NULL,
    `gender` char DEFAULT NULL,
    `occupation` varchar(20) DEFAULT NULL,
    `zipCode` varchar(10) DEFAULT NULL,
    `movieId` bigint(10) NOT NULL,
    `movieTitle` varchar(150) DEFAULT NULL,
    `releaseDate` varchar(20) DEFAULT NULL,
    `videoReleaseDate` varchar(20) DEFAULT NULL,
    `IMDbURL` varchar(256) DEFAULT NULL,
    `type` varchar(180) DEFAULT NULL,
    `rating` smallint DEFAULT NULL,
    `timestamp` datetime DEFAULT NULL,
    createTime datetime ,
    updateTime datetime
)
engine = olap
duplicate key (userId, age)
DISTRIBUTED BY HASH(movieId) BUCKETS 3
PROPERTIES (
"replication_num" = "1"
);



