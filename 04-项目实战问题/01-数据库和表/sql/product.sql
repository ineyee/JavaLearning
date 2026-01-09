DROP TABLE IF EXISTS `product`;

CREATE TABLE `product`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '-- 主键、自增',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '-- 该字段我们定义为非必传，即 DATETIME? 类型而非 DATETIME 类型，因此我们需要提供默认值——当前时间\n-- 只要我们给这个字段提供了默认值，实际开发中就无需再手动给这个字段赋值了，让数据库自动维护即可\n-- 无论服务器所在时区是什么时区，MySQL 在自动维护 DATETIME 类型的字段时默认都存储的是零时区的时间，所以客户端拿到时间后展示时需要手动转换为各个时区对应的时间展示；当然有些数据库 GUI 工具——如 Navicat ——在展示这些零时区的时间时可能会转换为当前时区的时间展示，这个我们不用管它；总之，通过 SQL 语句读取到的时间和返回给客户端的时间肯定是零时区的\n',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '-- 该字段我们定义为非必传，即 DATETIME? 类型而非 DATETIME 类型，因此我们需要提供默认值——当前时间；并且我们设置当更新了某条数据的任意字段时，都用最新的当前时间更新一下该字段\n-- 只要我们给这个字段提供了默认值，实际开发中就无需再手动给这个字段赋值了，让数据库自动维护即可\n',
  `name` varchar(100) NOT NULL COMMENT '-- 该字段我们定义为必传，即 String 类型而非 String? 类型，因此我们不需要提供默认值\n-- 该字段还得唯一\n',
  `desc` varchar(100) NULL DEFAULT '' COMMENT '-- 该字段我们定义为非必传，即 String? 类型而非 String 类型，因此我们需要提供默认值\n',
  `price` double NOT NULL COMMENT '-- 该字段我们定义为非必传，即 Double? 类型而非 Double 类型，因此我们需要提供默认值\n',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_product_name`(`name`)
);

