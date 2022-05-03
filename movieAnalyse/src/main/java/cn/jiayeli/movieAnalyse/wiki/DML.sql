use movieInfo;

show tables;

-- 电影信息表
drop table if exists movies;
create table if not exists movies (
    movieId bigint(10) primary key
    ,movieTitle        varchar(128)
    ,releaseDate       varchar(26)
    ,videoReleaseDate  varchar(26)
    ,IMDbURL           varchar(128)
    ,`unknown`           varchar(24)
    ,`Action`            varchar(24)
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

