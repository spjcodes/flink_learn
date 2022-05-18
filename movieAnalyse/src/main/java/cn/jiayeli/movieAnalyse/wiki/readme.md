# data flow

```mermaid
graph LR
1(dataGenerator) --> 2[mysql] -.-> 3((flinkCDC)) --sync.->  4{{kafka}} ==> 5>phoenix]

```

```sql
-- 用户电影评分信息表
drop table if exists movieInfo.userMovieRatingInfo;
create table if not exists movieInfo.userMovieRatingInfo(
    userId varchar(10)
    ,age varchar(3)
    ,gender varchar(1)
    ,occupation varchar(20)
    ,zipCode varchar(10)
    ,movieId varchar(10)
    ,movieTitle varchar(128)
    ,releaseDate varchar(20)
    ,videoReleaseDate varchar(20)
    ,IMDbURL varchar(128)
    ,type varchar(180)
    ,rating int
    ,timestamp   varchar(14)
    ,PRIMARY KEY (`userId`, `movieId`)
);
```

# analyse
## data example
```json
{
  "userId":"178",
  "age":"26",
  "gender":"M",
  "occupation":"other",
  "zipCode":"49512",
  "movieId":"568",
  "movieTitle":"Speed (1994)",
  "releaseDate":"01-Jan-1994",
  "videoReleaseDate":"",
  "IMDbURL":"http://us.imdb.com/M/title-exact?Speed%20(1994/I)",
  "type":"Horror|Musical|Mystery",
  "rating":4,
  "timestamp":"882826555"
}
```

## 评分最高的电影TOP10
```sql
select 
  movieId,
  movieTitle,
  count(rating) as totalRating
from movieInfo.userMovieRatingInfo
group by movieId, movieTitle, rating
order by totalRating
;
```
## 最受欢迎的电影分类TOP10

## 发行当年最受欢迎的电影及类别TOP10

## 同类电影最受欢迎TOp10

## 用户最爱的电影（评分维度）

## 用户最爱的电影类型

## 用户职业对电影类型的影响
eg:
职业：程序员  
电影类别：
    爱情  喜爱度：12%  
    科幻  喜爱度：50%  





