/* 更新的触发器 */
DELIMITER $$
DROP TRIGGER IF EXISTS `tri_posts_after_update`$$
CREATE
    TRIGGER `tri_posts_after_update` AFTER UPDATE ON `posts`
    FOR EACH ROW BEGIN
    IF new.status='0' AND new.update_time>old.update_time THEN
    INSERT INTO trglogs(tablename,operating_datetime,operating_type,guid) VALUES('posts', NOW(), 'u', new.id);
    END IF;
END;
$$
DELIMITER ;