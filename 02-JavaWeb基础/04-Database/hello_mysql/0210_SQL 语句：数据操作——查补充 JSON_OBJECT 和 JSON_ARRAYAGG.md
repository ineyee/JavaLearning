## 一、单表查询

比如我们设计了如下产品表：

| id（主键） |    name     |   cover    | price |
| :--------: | :---------: | :--------: | :---: |
|     1      |   iPhoneX   | 初代全面屏 | 8888  |
|     2      | 华为 Mate70 |   很厉害   | 6666  |
|     3      |   小米 10   |  也很厉害  | 4444  |

创建表：

```SQL
CREATE TABLE IF NOT EXISTS t_product (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL UNIQUE,	
  `desc` VARCHAR(100) DEFAULT(""),	
	price DOUBLE NOT NULL
);
```

给产品表中插入数据：

```SQL
INSERT INTO t_product (name, `desc`, price)
VALUES ('iPhoneX', '初代全面屏', 8888), ('华为 Mate70', '很厉害', 6666), ('小米 10', '也很厉害', 4444);
```

查询：

```SQL
SELECT *
FROM t_product
WHERE price >= 6666;
```

查询结果：

| id（主键） |    name     |   cover    | price |
| :--------: | :---------: | :--------: | :---: |
|     1      |   iPhoneX   | 初代全面屏 | 8888  |
|     2      | 华为 Mate70 |   很厉害   | 6666  |

当我们在代码里通过数据库驱动执行查询时，数据库驱动其实默认就会把查询出来的一条一条的结果搞成一个数组，然后每一条数据是一个 map、作为数组的元素，也就是说数据库驱动会把上面的查询结果搞成一个 JSON 返回给我们，我们可以顺势拿着这个 JSON 返回给客户端，这正是我们期望的现象，单表查询时默认就是这样的：

```JSON
[
  {
    "id": 1,
    "name": "iPhoneX",
    "desc": "初代全面屏",
    "price": 8888
  },
  {
    "id": 2,
    "name": "华为 Mate70",
    "desc": "很厉害",
    "price": 6666
  }
]
```

## 二、一对一和一对多表结构的查询

比如我们设计了如下歌手表（主表）和歌曲表（从表、外键都是在从表里）:

| id（主键） |  name  | sex |
| :--------: | :----: | :-: |
|     1      | 周杰伦 | ''  |
|     2      |  李健  | 男  |
|     3      | 梁静茹 | 女  |

| id（主键） |    name    |       cover        | singer_id（外键） |
| :--------: | :--------: | :----------------: | :---------------: |
|     1      |   七里香   | https://七里香.jpg |         1         |
|     2      |    晴天    |  https://晴天.jpg  |         1         |
|     3      | 贝加尔湖畔 |         ''         |         2         |
|     4      |     画     |   https://画.jpg   |       NULL        |
|     5      |    成都    |  https://成都.jpg  |       NULL        |

创建表（先创建主表后创建从表）：

```SQL
CREATE TABLE IF NOT EXISTS t_singer (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  sex VARCHAR(100) DEFAULT('')
);
```

```SQL
CREATE TABLE IF NOT EXISTS t_song (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  cover VARCHAR(100) DEFAULT(''),
	
  -- 外键字段，必须与 t_singer.id 类型一致
  singer_id BIGINT,
  -- 在 CREATE 语句的最后添加一个外键约束，含义为：
  -- 当前表中的外键 singer_id，引用的是 t_singer 表中的 id
  -- ON UPDATE 和 ON DELETE 是指当我们修改或删除 t_singer.id 时，t_song 应该做出什么反应，默认情况下这个值是 RESTRICT —— 即如果某个字段被外键关联着、那么在修改或删除这个字段时直接报错、也就是说不允许删除或修改；我们通常会手动设置为 CASCADE —— 即允许修改或删除这个字段、并且如果是修改这个字段那就跟随修改另外一张表里的数据、如果是删除这个字段那就跟随删除另外一张表里的数据
  FOREIGN KEY (singer_id) REFERENCES t_singer(id) ON UPDATE CASCADE ON DELETE CASCADE
);
```

给表中插入数据：

```SQL
INSERT INTO t_singer (name, sex)
VALUES ('周杰伦', ''), ('李健', '男'), ('梁静茹', '女');
```

```SQL
INSERT INTO t_song (name, cover, singer_id)
VALUES ('七里香', 'https://七里香.jpg', 1), ('晴天', 'https://晴天.jpg', 1), ('风吹麦浪', '', 2), ('画', 'https://画.jpg', NULL), ('成都', 'https://成都.jpg', NULL);
```

查询（`默认的查询语句`）：

```SQL
SELECT
	-- 这里是在给表1和表2的字段取别名，避免查询结果里的字段名冲突
	-- t_song 的字段对应 FROM t_song，代表是从 t_song 里的查询
  t_song.id AS song_id,
  t_song.name AS song_name,
  t_song.cover AS song_cover,
	-- t_singer 的字段对应 LEFT JOIN t_singer，代表是从 t_singer 里的查询
  t_singer.id AS singer_id,
  t_singer.name AS singer_name,
  t_singer.sex AS singer_sex
-- t_song 是表1
FROM t_song
-- t_singer 是表2，表1.外键 = 表2.主键
LEFT JOIN t_singer ON t_song.singer_id = t_singer.id;
```

查询结果（`默认的查询语句会把两张表的数据组合起来，平铺成一张表返回给我们`）：

| song_id | song_name |     song_cover     | singer_id | singer_name | singer_sex |
| :-----: | :-------: | :----------------: | :-------: | :---------: | :--------: |
|    1    |  七里香   | https://七里香.jpg |     1     |   周杰伦    |            |
|    2    |   屋顶    |  https://屋顶.jpg  |     1     |   周杰伦    |            |
|    3    | 风吹麦浪  |                    |     2     |    李健     |     男     |
|    4    |    画     |   https://画.jpg   |   NULL    |    NULL     |    NULL    |
|    5    |   成都    |  https://成都.jpg  |   NULL    |    NULL     |    NULL    |

当我们在代码里通过数据库驱动执行查询时，数据库驱动默认会把上面的查询结果搞成一个 JSON 返回给我们：

```JSON
[
  {
    "songId": 1,
    "songName": "七里香",
    "songCover": "https://七里香.jpg",
    "singerId": 1,
    "singerName": "周杰伦",
    "singerSex": ""
  },
  {
    "songId": 2,
    "songName": "屋顶",
    "songCover": "https://屋顶.jpg",
    "singerId": 1,
    "singerName": "周杰伦",
    "singerSex": ""
  },
  {
    "songId": 3,
    "songName": "风吹麦浪",
    "songCover": "",
    "singerId": 2,
    "singerName": "李健",
    "singerSex": "男"
  },
  {
    "songId": 4,
    "songName": "画",
    "songCover": "https://画.jpg",
    "singerId": null,
    "singerName": null,
    "singerSex": null
  },
  {
    "songId": 5,
    "songName": "成都",
    "songCover": "https://成都.jpg",
    "singerId": null,
    "singerName": null,
    "singerSex": null
  }
]
```

把上面的 JSON 返回给客户端也没毛病，但是当数据变得复杂时，这样“平铺的数据结构”会不利于理解，所以实际开发中我们都是让`从表的对象嵌套主表的对象（因为从表里的数据是主要被查询的数据，而主表是被 JOIN 的）`，把如下的 JSON 返回给客户端：

```JSON
[
  {
    // 这里是从表对象
    "songId": 1,
    "songName": "七里香",
    "songCover": "https://七里香.jpg",
    // 这里是主表对象
    "singer": {
      "id": 1,
      "name": "周杰伦",
      "sex": ""
    }
  },
  {
    "songId": 2,
    "songName": "屋顶",
    "songCover": "https://屋顶.jpg",
    "singer": {
      "id": 1,
      "name": "周杰伦",
      "sex": ""
    }
  },
  {
    "songId": 3,
    "songName": "风吹麦浪",
    "songCover": "",
    "singer": {
      "id": 2,
      "name": "李健",
      "sex": "男"
    }
  },
  {
    "songId": 4,
    "songName": "画",
    "songCover": "https://画.jpg",
    "singer": {
      "id": null,
      "name": null,
      "sex": null
    }
  },
  {
    "songId": 5,
    "songName": "成都",
    "songCover": "https://成都.jpg",
    "singer": {
      "id": null,
      "name": null,
      "sex": null
    }
  }
]
```

我们期望的 JSON 就反过来要求查询结果应该是这样一张表：

| song_id | song_name |     song_cover     |                 singer                  |
| :-----: | :-------: | :----------------: | :-------------------------------------: |
|    1    |  七里香   | https://七里香.jpg | {"id": 1, "name": "周杰伦", "sex": ""}  |
|    2    |   屋顶    |  https://屋顶.jpg  | {"id": 1, "name": "周杰伦", "sex": ""}  |
|    3    | 风吹麦浪  |                    | {"id": 2, "name": "李健", "sex": "男"}  |
|    4    |    画     |   https://画.jpg   | {"id": null, "name": null, "sex": null} |
|    5    |   成都    |  https://成都.jpg  | {"id": null, "name": null, "sex": null} |

而要想实现这样的查询结果，就不能用默认的查询语句了，得用一下 `JSON_OBJECT 这个聚合函数——用来把查询到的数据搞成一个 map`：

```SQL
SELECT
	-- 这里是在给表1和表2的字段取别名，避免查询结果里的字段名冲突
	-- t_song 的字段对应 FROM t_song，代表是从 t_song 里的查询
  t_song.id AS song_id,
  t_song.name AS song_name,
  t_song.cover AS song_cover,
  -- t_singer 的字段对应 LEFT JOIN t_singer，代表是从 t_singer 里的查询
  -- 这里就是在把主表里的字段搞成一个对象，而不再是平铺
  -- 格式：JSON_OBJECT(
  --        '对象的自定义属性名1', 表里对应字段1的值,
  --        '对象的自定义属性名2', 表里对应字段2的值,
  --        '对象的自定义属性名3', 表里对应字段3的值
  --        ...
  --      ) AS '对象的 key'
  JSON_OBJECT(
    'id', t_singer.id,
    'name', t_singer.name,
    'sex', t_singer.sex
  ) AS 'singer'
-- t_song 是表1
FROM t_song
-- t_singer 是表2，表1.外键 = 表2.主键
LEFT JOIN t_singer ON t_song.singer_id = t_singer.id;
```

## 三、多对多表结构的查询

比如我们设计了如下歌手表（主表 1）、歌曲表（主表 2）和中间表（外键都是在中间表里，中间表包含两个外键，分别指向主表 1 和主表 2 的主键）:

| id（主键） |  name  | sex |
| :--------: | :----: | :-: |
|     1      | 周杰伦 | ''  |
|     2      |  李健  | 男  |
|     3      | 梁静茹 | 女  |
|     4      |  温岚  | 女  |
|     5      |  孙俪  | 女  |

| id（主键） |   name   |       cover        |
| :--------: | :------: | :----------------: |
|     1      |  七里香  | https://七里香.jpg |
|     2      |   屋顶   |  https://屋顶.jpg  |
|     3      | 风吹麦浪 |         ''         |
|     4      |    画    |   https://画.jpg   |
|     5      |   成都   |  https://成都.jpg  |

| id（主键） | songId（外键） | singerId（外键） |
| :--------: | :------------: | :--------------: |
|     1      |       1        |        1         |
|     2      |       2        |        1         |
|     3      |       2        |        4         |
|     4      |       3        |        2         |
|     5      |       3        |        5         |
|     6      |       4        |       NULL       |
|     7      |       5        |       NULL       |

创建表（先创建主表、两个主表的创建顺序随意，然后创建中间表即从表）：

```SQL
CREATE TABLE IF NOT EXISTS `t_singer` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `sex` VARCHAR(100) DEFAULT('')
);
```

```SQL
CREATE TABLE IF NOT EXISTS `t_song` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `cover` VARCHAR(100) DEFAULT('')
);
```

```SQL
CREATE TABLE IF NOT EXISTS `t_singer_song` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  
  -- 外键字段，必须与 t_singer.id 类型一致
  `singerId` BIGINT,
  -- 外键字段，必须与 t_song.id 类型一致
  `songId` BIGINT,
  -- 在 CREATE 语句的最后添加一个外键约束，含义为：
  -- 当前表中的外键 `singerId`，引用的是 `t_singer` 表中的 `id`
  -- ON UPDATE 和 ON DELETE 是指当我们删除或修改 t_singer.id t_singer_song 应该做出什么反应，默认情况下是这个值是 RESTRICT —— 即如果某个字段被外键关联着、那么在删除或修改这个字段时直接报错、也就是说不允许删除或修改；我们通常会手动设置为 CASCADE —— 即允许删除或修改这个字段、并且如果是删除这个字段那就跟随删除另外一张表里的数据、如果是修改这个字段那就跟随修改另外一张表里的数据
  FOREIGN KEY (`singerId`) REFERENCES `t_singer`(`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (`songId`) REFERENCES `t_song`(`id`) ON UPDATE CASCADE ON DELETE CASCADE
);
```

给表中插入数据：

```SQL
INSERT INTO `t_singer` (`name`, `sex`) VALUES ('周杰伦', '');
INSERT INTO `t_singer` (`name`, `sex`) VALUES ('李健', '男');
INSERT INTO `t_singer` (`name`, `sex`) VALUES ('梁静茹', '女');
INSERT INTO `t_singer` (`name`, `sex`) VALUES ('温岚', '女');
INSERT INTO `t_singer` (`name`, `sex`) VALUES ('孙俪', '女');
```

```SQL
INSERT INTO `t_song` (`name`, `cover`) VALUES ('七里香', 'https://七里香.jpg');
INSERT INTO `t_song` (`name`, `cover`) VALUES ('屋顶', 'https://屋顶.jpg');
INSERT INTO `t_song` (`name`, `cover`) VALUES ('风吹麦浪', '');
INSERT INTO `t_song` (`name`, `cover`) VALUES ('画', 'https://画.jpg');
INSERT INTO `t_song` (`name`, `cover`) VALUES ('成都', 'https://成都.jpg');
```

```SQL
INSERT INTO `t_singer_song` (`songId`, `singerId`) VALUES (1, 1);
INSERT INTO `t_singer_song` (`songId`, `singerId`) VALUES (2, 1);
INSERT INTO `t_singer_song` (`songId`, `singerId`) VALUES (2, 4);
INSERT INTO `t_singer_song` (`songId`, `singerId`) VALUES (3, 2);
INSERT INTO `t_singer_song` (`songId`, `singerId`) VALUES (3, 5);
INSERT INTO `t_singer_song` (`songId`, `singerId`) VALUES (4, NULL);
INSERT INTO `t_singer_song` (`songId`, `singerId`) VALUES (5, NULL);
```

查询（`默认的查询语句，这里我们是想查出来所有的歌曲，所以应该以主表2——歌曲表——为主要查询的表，而把主表1——歌手表——作为 LEFT JOIN 的表；当然以主表1——歌手表——为主要查询的表，而把主表2——歌曲表——作为 RIGHT JOIN 的表也可以查询出来一模一样的结果`）：

```SQL
SELECT
-- 这里是在给主表2和主表1的字段取别名，避免查询结果里的字段名冲突
  `t_song`.`id` AS `songId`,
  `t_song`.`name` AS `songName`,
  `t_song`.`cover` AS `songCover`,
  `t_singer`.`id` AS `singerId`,
  `t_singer`.`name` AS `singerName`,
  `t_singer`.`sex` AS `singerSex`
-- `t_song`是主表2
FROM `t_song`
-- `t_singer_song`是中间表，中间表.外键 = 表2.主键
LEFT JOIN `t_singer_song` ON `t_singer_song`.`songId` = `t_song`.`id`
-- `t_singer`是主表1，中间表.外键 = 表1.主键
LEFT JOIN `t_singer` ON `t_singer_song`.`singerId` = `t_singer`.`id`;
```

查询结果（`默认的查询语句会把三张表的数据组合起来，平铺成一张表返回给我们`）：

| songId | songName |     songCover      | singerId | singerName | singerSex |
| :----: | :------: | :----------------: | :------: | :--------: | :-------: |
|   1    |  七里香  | https://七里香.jpg |    1     |   周杰伦   |    ''     |
|   2    |   屋顶   |  https://屋顶.jpg  |    1     |   周杰伦   |    ''     |
|   2    |   屋顶   |  https://屋顶.jpg  |    4     |    温岚    |    女     |
|   3    | 风吹麦浪 |                    |    2     |    李健    |    男     |
|   3    | 风吹麦浪 |                    |    5     |    孙俪    |    女     |
|   4    |    画    |   https://画.jpg   |   NULL   |    NULL    |   NULL    |
|   5    |   成都   |  https://成都.jpg  |   NULL   |    NULL    |   NULL    |

当我们在代码里通过数据库驱动执行查询时，数据库驱动默认会把上面的查询结果搞成一个 JSON 返回给我们：

```JSON
[
  {
    "songId": 1,
    "songName": "七里香",
    "songCover": "https://七里香.jpg",
    "singerId": 1,
    "singerName": "周杰伦",
    "singerSex": ""
  },
  { // 这首歌既属于周杰伦，也属于温岚，歌曲信息记录冗余
    "songId": 2,
    "songName": "屋顶",
    "songCover": "https://屋顶.jpg",
    "singerId": 1,
    "singerName": "周杰伦",
    "singerSex": ""
  },
  {
    "songId": 2,
    "songName": "屋顶",
    "songCover": "https://屋顶.jpg",
    "singerId": 4,
    "singerName": "温岚",
    "singerSex": "女"
  },
  { // 这首歌既属于李健，也属于孙俪，歌曲信息记录冗余
    "songId": 3,
    "songName": "风吹麦浪",
    "songCover": "",
    "singerId": 2,
    "singerName": "李健",
    "singerSex": "男"
  },
  {
    "songId": 3,
    "songName": "风吹麦浪",
    "songCover": "",
    "singerId": 5,
    "singerName": "孙俪",
    "singerSex": "女"
  },
  {
    "songId": 4,
    "songName": "画",
    "songCover": "https://画.jpg",
    "singerId": null,
    "singerName": null,
    "singerSex": null
  },
  {
    "songId": 5,
    "songName": "成都",
    "songCover": "https://成都.jpg",
    "singerId": null,
    "singerName": null,
    "singerSex": null
  }
]
```

把上面的 JSON 返回给客户端也没毛病，但是一来当数据变得复杂时，这样“平铺的数据结构”非常不利于理解，二来数据记录存在大量冗余，所以实际开发中我们都是让`主表2——歌曲表——的对象嵌套主表1——歌手表——的对象（解决平铺问题，哪个表是主要查询的，哪个表的对象就处于外层、另一个表的对象被嵌套在内层） + 主表1——歌手表——的对象合并成一个数组（解决数据记录冗余问题，被嵌套的对象合并成数组）`（这里的数据处理方式适用于做歌曲列表，要把所有的歌曲列出来 + 展示出来这首歌所有的歌手；如果我们要做歌手列表、点击歌手可以看到歌手详情里有多少首歌，那就把 LEFT JOIN 换成 RIGHT JOIN 以主表为主查询所有的歌手 + 把同一个歌手的所有歌曲搞成一个数组就行了，或者干脆换一下主表 1 和主表 2、以主表 1——歌手表——查询为主，所以要根据实际场景决定数据处理方式，处理方式不是写死必须得怎样），把如下的 JSON 返回给客户端：

```JSON
[
  {
    // 这里是从表对象
    "songId": 1,
    "songName": "七里香",
    "songCover": "https://七里香.jpg",
    // 这里是主表对象数组
    "singers": [
      // 这里是主表对象
      {
        "id": 1,
        "name": "周杰伦",
        "sex": ""
      }
    ]
  },
  {
    "songId": 2,
    "songName": "屋顶",
    "songCover": "https://屋顶.jpg",
    "singers": [
      {
        "id": 1,
        "name": "周杰伦",
        "sex": ""
      },
      {
        "id": 4,
        "name": "温岚",
        "sex": "女"
      }
    ]
  },
  {
    "songId": 3,
    "songName": "风吹麦浪",
    "songCover": "",
    "singers": [
      {
        "id": 2,
        "name": "李健",
        "sex": "男"
      },
      {
        "id": 5,
        "name": "孙俪",
        "sex": "女"
      }
    ]
  },
  {
    "songId": 4,
    "songName": "画",
    "songCover": "https://画.jpg",
    "singers": [
      {
        "id": null,
        "name": null,
        "sex": null
      }
    ]
  },
  {
    "songId": 5,
    "songName": "成都",
    "songCover": "https://成都.jpg",
    "singers": [
      {
        "id": null,
        "name": null,
        "sex": null
      }
    ]
  }
]
```

我们期望的 JSON 就反过来要求查询结果应该是这样一张表：

| songId | songName |     songCover      |                                     singers                                      |
| :----: | :------: | :----------------: | :------------------------------------------------------------------------------: |
|   1    |  七里香  | https://七里香.jpg |                     [{"id": 1, "name": "周杰伦", "sex": ""}]                     |
|   2    |   屋顶   |  https://屋顶.jpg  | [{"id": 1, "name": "周杰伦", "sex": ""}, {"id": 4, "name": "温岚", "sex": "女"}] |
|   3    | 风吹麦浪 |                    | [{"id": 2, "name": "李健", "sex": "男"}, {"id": 5, "name": "孙俪", "sex": "女"}] |
|   4    |    画    |   https://画.jpg   |                    [{"id": null, "name": null, "sex": null}]                     |
|   5    |   成都   |  https://成都.jpg  |                    [{"id": null, "name": null, "sex": null}]                     |

而要想实现这样的查询结果，就不能用默认的查询语句了，得用一下 `JSON_ARRAYAGG 这个聚合函数——用来把查询到的数据搞成一个数组`，而 `JSON_ARRAYAGG 必须配合 GROUP BY 先把数据分组后才能把各组的数据搞成分组，GROUP BY 通常情况下都是按 FROM 的那个表来分、因为我们主要查询的是这个表`：

```SQL
SELECT
	-- t_song 的字段对应 FROM `t_song`，代表是从 t_song 里的查询
	-- 这里是在给主表2——歌曲表——的字段取别名
  `t_song`.`id` AS `songId`,
  `t_song`.`name` AS `songName`,
  `t_song`.`cover` AS `songCover`,
	-- t_singer 的字段对应 LEFT JOIN `t_singer`，代表是从 t_singer 里的查询
	-- 这里就是在把主表1——歌手表——对象合并成一个数组，数组里又放的是一个一个的主表1对象
	-- 格式：JSON_ARRAYAGG(主表1对象) AS '数组的 key'
  JSON_ARRAYAGG(
    -- 这里就是在把主表1里的字段搞成一个对象，而不再是平铺
    -- 格式：JSON_OBJECT(
    --        '对象的自定义属性名1', 表里对应字段1的值,
    --        '对象的自定义属性名2', 表里对应字段2的值,
    --        '对象的自定义属性名3', 表里对应字段3的值
    --        ...
    --      ) 注意数组里放对象时就不再需要 AS '对象的 key' 了，直接放对象即可
    JSON_OBJECT(
      'id', `t_singer`.`id`,
      'name', `t_singer`.`name`,
      'sex', `t_singer`.`sex`
    )
  ) AS 'singers'
-- `t_song`是主表2
FROM `t_song`
-- `t_singer_song`是中间表，中间表.外键 = 表2.主键
LEFT JOIN `t_singer_song` ON `t_singer_song`.`songId` = `t_song`.`id`
-- `t_singer`是主表1，中间表.外键 = 表1.主键
LEFT JOIN `t_singer` ON `t_singer_song`.`singerId` = `t_singer`.`id`
-- 通过主表2——歌曲表——的主键进行分组（即主要查询、外层对象的主键）
GROUP BY `t_song`.`id`;
```
