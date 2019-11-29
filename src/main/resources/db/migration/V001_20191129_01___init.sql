CREATE TABLE if not exists `trglogs` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `tablename` varchar(128) DEFAULT NULL COMMENT '表名',
  `operating_datetime` timestamp NULL DEFAULT NULL COMMENT '操作时间',
  `operating_type` char(1) DEFAULT NULL COMMENT '操作类型(i:插入u:更新d:删除)',
  `guid` varchar(50) DEFAULT NULL COMMENT '业务表id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='触发器日志表';

/* 删除的触发器 */
DELIMITER $$
DROP TRIGGER IF EXISTS `tri_posts_after_delete`$$
CREATE
    TRIGGER `tri_posts_after_delete` AFTER DELETE ON `posts`
    FOR EACH ROW BEGIN
    IF old.status='0' THEN
    INSERT INTO trglogs(tablename,operating_datetime,operating_type,guid) VALUES('posts', NOW(), 'd', old.id);
    END IF;
END;
$$
DELIMITER ;


/* 更新的触发器 */
DELIMITER $$
DROP TRIGGER IF EXISTS `tri_posts_after_update`$$
CREATE
    TRIGGER `tri_posts_after_update` AFTER UPDATE ON `posts`
    FOR EACH ROW BEGIN
    IF new.status='0' THEN
    INSERT INTO trglogs(tablename,operating_datetime,operating_type,guid) VALUES('posts', NOW(), 'u', new.id);
    END IF;
END;
$$
DELIMITER ;


/* 新增的触发器 */
DELIMITER $$
DROP TRIGGER IF EXISTS `tri_posts_after_insert`$$
CREATE
    TRIGGER `tri_posts_after_insert` AFTER INSERT ON `posts`
    FOR EACH ROW BEGIN
    IF new.status='0' THEN
    INSERT INTO trglogs(tablename,operating_datetime,operating_type,guid) VALUES('posts', NOW(), 'i', new.id);
    END IF;
END;
$$
DELIMITER ;


/* 手动触发一次日志 */
INSERT INTO trglogs(tablename,operating_datetime,operating_type,guid) #VALUES('posts', NOW(), 'u', new.id);
SELECT 'posts', NOW(), 'u', id
FROM `posts`
WHERE `status`='0';