CREATE TABLE `qrtz_trigger_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_name` varchar(200) NOT NULL,
  `job_group` varchar(200) NOT NULL,
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `trigger_address` varchar(128) NOT NULL COMMENT 'ip地址加端口号，如192.168.0.1:8080',
  `trigger_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '触发时间，精确到毫秒',
  `lead_time` bigint NOT NULL COMMENT '耗时',
  `callback_url` varchar(4096) NOT NULL COMMENT '回调地址',
  `callback_count` tinyint NOT NULL COMMENT '回调次数',
  `session_id` varchar(64) NOT NULL  COMMENT 'session_id',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1.成功；2.失败；3.重复', -- 需要改成 trigger_status
  `execute_desc` varchar(512) NOT NULL DEFAULT '' COMMENT '执行描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `qrtz_trigger_lock` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_name` varchar(200) NOT NULL,
  `job_group` varchar(200) NOT NULL,
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `trigger_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `qrtz_job_extend` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_name` varchar(200) NOT NULL,
  `job_group` varchar(200) NOT NULL,
  `job_code` varchar(200) NOT NULL,
  `callback_url` varchar(4096) NOT NULL,
  `app_key` varchar(128) NOT NULL,
  `job_desc` varchar(128) NOT NULL,
  `job_create_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `qrtz_app_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(128) NOT NULL,
  `app_desc` varchar(128) NOT NULL,
  `app_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0.禁用；1.成功；',
  `app_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `app_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `app_author` varchar(128) DEFAULT '',
  `app_email` varchar(128) DEFAULT '',
  `app_phone` varchar(64) DEFAULT '',
  `app_key` varchar(128) NOT NULL,
  `app_secret` varchar(256) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

