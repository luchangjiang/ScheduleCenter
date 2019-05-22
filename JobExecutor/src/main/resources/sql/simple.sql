SET sql_mode='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';

CREATE TABLE QRTZ_JOB_DETAILS
(
  SCHED_NAME VARCHAR(120) NOT NULL,
  JOB_NAME  VARCHAR(200) NOT NULL,
  JOB_GROUP VARCHAR(200) NOT NULL,
  DESCRIPTION VARCHAR(250) NULL,
  JOB_CLASS_NAME   VARCHAR(250) NOT NULL,
  IS_DURABLE VARCHAR(1) NOT NULL,
  IS_NONCONCURRENT VARCHAR(1) NOT NULL,
  IS_UPDATE_DATA VARCHAR(1) NOT NULL,
  REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
  JOB_DATA BLOB NULL,
  PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务详情表（qurtz官方表） \r\n  存储每一个已配置的 Job 的详细信息';

CREATE TABLE QRTZ_TRIGGERS
(
  SCHED_NAME VARCHAR(120) NOT NULL,
  TRIGGER_NAME VARCHAR(200) NOT NULL,
  TRIGGER_GROUP VARCHAR(200) NOT NULL,
  JOB_NAME  VARCHAR(200) NOT NULL,
  JOB_GROUP VARCHAR(200) NOT NULL,
  DESCRIPTION VARCHAR(250) NULL,
  NEXT_FIRE_TIME BIGINT(13) NULL,
  PREV_FIRE_TIME BIGINT(13) NULL,
  PRIORITY INTEGER NULL,
  TRIGGER_STATE VARCHAR(16) NOT NULL,
  TRIGGER_TYPE VARCHAR(8) NOT NULL,
  START_TIME BIGINT(13) NOT NULL,
  END_TIME BIGINT(13) NULL,
  CALENDAR_NAME VARCHAR(200) NULL,
  MISFIRE_INSTR SMALLINT(2) NULL,
  JOB_DATA BLOB NULL,
  PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
  FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
  REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='触发器信息表（qurtz官方表） \r\n  存储已配置的 Trigger 的信息';


CREATE TABLE QRTZ_CRON_TRIGGERS
(
  SCHED_NAME VARCHAR(120) NOT NULL,
  TRIGGER_NAME VARCHAR(200) NOT NULL,
  TRIGGER_GROUP VARCHAR(200) NOT NULL,
  CRON_EXPRESSION VARCHAR(200) NOT NULL,
  TIME_ZONE_ID VARCHAR(80),
  PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
  FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
  REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CRON信息表（qurtz官方表） \r\n  存储 Cron Trigger，包括 Cron 表达式和时区信息';



CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS
(
  SCHED_NAME VARCHAR(120) NOT NULL,
  TRIGGER_GROUP  VARCHAR(200) NOT NULL,
  PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务暂停信息表（qurtz官方表） \r\n  存储已暂停的 Trigger 组的信息';

CREATE TABLE QRTZ_FIRED_TRIGGERS
(
  SCHED_NAME VARCHAR(120) NOT NULL,
  ENTRY_ID VARCHAR(95) NOT NULL,
  TRIGGER_NAME VARCHAR(200) NOT NULL,
  TRIGGER_GROUP VARCHAR(200) NOT NULL,
  INSTANCE_NAME VARCHAR(200) NOT NULL,
  FIRED_TIME BIGINT(13) NOT NULL,
  SCHED_TIME BIGINT(13) NOT NULL,
  PRIORITY INTEGER NOT NULL,
  STATE VARCHAR(16) NOT NULL,
  JOB_NAME VARCHAR(200) NULL,
  JOB_GROUP VARCHAR(200) NULL,
  IS_NONCONCURRENT VARCHAR(1) NULL,
  REQUESTS_RECOVERY VARCHAR(1) NULL,
  PRIMARY KEY (SCHED_NAME,ENTRY_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='触发执行表（qurtz官方表） \r\n 存储与已触发的 Trigger 相关的状态信息，以及相联 Job 的执行信息';

CREATE TABLE QRTZ_SIMPLE_TRIGGERS
(
  SCHED_NAME VARCHAR(120) NOT NULL,
  TRIGGER_NAME VARCHAR(200) NOT NULL,
  TRIGGER_GROUP VARCHAR(200) NOT NULL,
  REPEAT_COUNT BIGINT(7) NOT NULL,
  REPEAT_INTERVAL BIGINT(12) NOT NULL,
  TIMES_TRIGGERED BIGINT(10) NOT NULL,
  PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
  FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
  REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='存储简单的触发器（qurtz官方表）\r\n 包括重复次数，间隔，以及已触的次数';

CREATE TABLE QRTZ_SCHEDULER_STATE
(
  SCHED_NAME VARCHAR(120) NOT NULL,
  INSTANCE_NAME VARCHAR(200) NOT NULL,
  LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
  CHECKIN_INTERVAL BIGINT(13) NOT NULL,
  PRIMARY KEY (SCHED_NAME,INSTANCE_NAME)
);

CREATE TABLE QRTZ_LOCKS
(
  SCHED_NAME VARCHAR(120) NOT NULL,
  LOCK_NAME  VARCHAR(40) NOT NULL,
  PRIMARY KEY (SCHED_NAME,LOCK_NAME)
);



# 新增表
CREATE TABLE `QRTZ_TRIGGER_LOG` (
  `id` varchar(36) NOT NULL COMMENT '32位uuid字符串做主键',
  `job_id` varchar(36) NOT NULL COMMENT '任务ID',
  `job_name` varchar(200) NOT NULL COMMENT '任务名称',
  `job_code` varchar(200) NOT NULL COMMENT '任务标识',
  `job_desc` varchar(128) NOT NULL COMMENT '任务描述',
  `job_group` varchar(200) NOT NULL COMMENT '任务组',
  `app_key` varchar(200) NOT NULL COMMENT 'APPKEY',
  `trigger_name` varchar(200) NOT NULL COMMENT '触发器名称',
  `trigger_group` varchar(200) NOT NULL  COMMENT '触发器组',
  `trigger_address` varchar(128) NOT NULL COMMENT 'ip地址加端口号，如192.168.0.1:8080',
  `trigger_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '触发时间，精确到毫秒',
  `lead_time` bigint NOT NULL COMMENT '耗时',
  `callback_url` varchar(4096) NOT NULL COMMENT '回调地址',
  `callback_count` tinyint NOT NULL COMMENT '回调次数',
  `session_id` varchar(64) NOT NULL  COMMENT 'SessionId',
  `execute_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '执行状态：1.成功；2.失败；3.重复',
  `execute_desc` varchar(512) NOT NULL DEFAULT '' COMMENT '执行描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务触发记录表 \r\n 记录每次触发执行的日志记录';


CREATE TABLE `QRTZ_TRIGGER_LOCK` (
  `id` varchar(36) NOT NULL COMMENT '32位uuid字符串做主键',
  `job_name` varchar(200) NOT NULL COMMENT '任务名称',
  `job_group` varchar(200) NOT NULL COMMENT '任务组',
  `trigger_name` varchar(200) NOT NULL COMMENT '触发器名称',
  `trigger_group` varchar(200) NOT NULL COMMENT '触发器组',
  `trigger_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='触发器乐观锁表 \r\n 以触发时间为版本控制';


CREATE TABLE `QRTZ_JOB_EXTEND` (
  `id` varchar(36) NOT NULL COMMENT '32位uuid字符串做主键',
  `job_name` varchar(200) NOT NULL COMMENT '任务名称',
  `job_group` varchar(200) NOT NULL COMMENT '任务组',
  `job_code` varchar(200) NOT NULL COMMENT '任务代码',
  `callback_url` varchar(4096) NOT NULL COMMENT '回调地址',
  `app_key` varchar(128) NOT NULL COMMENT '应用key',
  `job_desc` varchar(128) NOT NULL  COMMENT '任务描述',
  `job_create_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3)  COMMENT '任务创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务信息拓展表 \r\n 用来存储公司业务需要的任务信息';


CREATE TABLE `QRTZ_APP_INFO` (
  `id` varchar(36) NOT NULL COMMENT '32位uuid字符串做主键',
  `app_name` varchar(128) NOT NULL COMMENT '任务名称',
  `app_desc` varchar(128) NOT NULL  DEFAULT '' COMMENT '任务描述',
  `app_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0.禁用；1.成功；',
  `app_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '应用创建时间',
  `app_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '应用修改时间',
  `app_author` varchar(128) NOT NULL  DEFAULT ''  COMMENT '应用作者',
  `app_email` varchar(128) NOT NULL  DEFAULT ''  COMMENT '应用相关人邮箱',
  `app_phone` varchar(64) NOT NULL  DEFAULT ''  COMMENT '应用相关人电话',
  `app_key` varchar(128) NOT NULL  COMMENT '应用key',
  `app_secret` varchar(256) NOT NULL COMMENT '应用秘钥',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用信息表 \r\n 用来存储应用的基本信息';


CREATE TABLE `MONITOR_OBJECT` (
  `id` varchar(36) NOT NULL DEFAULT '' COMMENT '预警对象Id,guid字符串做主键',
  `obj_name` varchar(128) NOT NULL COMMENT '预警对象的名称',
  `obj_code` varchar(32) NOT NULL DEFAULT '' COMMENT '监控对象代码',
  `obj_desc` varchar(256) NOT NULL COMMENT '预警对象的描述',
  `obj_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '预警对象的状态 1.启用；2.禁用；',
  `obj_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `obj_policy_settings` varchar(2048) NOT NULL DEFAULT '' COMMENT '监控策略配置,json对象',
  `obj_receiver_settings` varchar(2048) NOT NULL DEFAULT '' COMMENT '预警方式及接收人员配置,json对象',
  `obj_show_order` int(11) NOT NULL DEFAULT '0' COMMENT '展示权重 倒序',
  `obj_extend` varchar(2048) NOT NULL DEFAULT '' COMMENT '预留扩展字段,json对象',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='预警对象配置表\r\n主要用来记录需要监控预警对象的相关预警策略';


CREATE TABLE `MONITOR_LOG` (
  `id` varchar(36) NOT NULL DEFAULT '' COMMENT '预警日志id,32位guid字符串做主键',
  `obj_id` varchar(36) NOT NULL DEFAULT '' COMMENT '监控对象id',
  `obj_name` varchar(128) NOT NULL DEFAULT '' COMMENT '监控对象名称',
  `obj_code` varchar(32) NOT NULL DEFAULT '' COMMENT '监控对象代码',
  `obj_desc` varchar(256) NOT NULL DEFAULT '' COMMENT '监控对象描述',
  `log_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '日志生成时间',
  `obj_policy_settings` varchar(2048) NOT NULL DEFAULT '' COMMENT '监控策略配置，记录预警时的该对象的监控策略，json对象字符串',
  `obj_receiver_settings` varchar(2048) NOT NULL DEFAULT '' COMMENT '预警时的预警方式及预警接收人员配置，json对象',
  `log_context` varchar(4096) NOT NULL DEFAULT '' COMMENT '预警文本',
  `log_extend` varchar(2048) NOT NULL DEFAULT '' COMMENT '预警预留的扩展字段,json对象',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='监控平台预警日志表\r\n用来记录所有的预警日志';


CREATE TABLE `QRTZ_JOB_ASYNC_RESULT` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_id` varchar(36) NOT NULL COMMENT '任务ID',
  `job_name` varchar(200) NOT NULL COMMENT '任务名称',
  `job_code` varchar(200) NOT NULL COMMENT '任务标识',
  `app_key` varchar(200) NOT NULL COMMENT 'APPKEY',
  `trigger_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '触发时间，精确到毫秒',
  `log_id` varchar(64) NOT NULL COMMENT 'logId',
  `session_id` varchar(64) NOT NULL COMMENT 'SessionId',
  PRIMARY KEY (`id`),
  KEY `qrtz_job_async_result_index_trigger_time` (`trigger_time`),
  KEY `qrtz_trigger_log_index_log_id` (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='异步任务结果表';



# 索引
ALTER TABLE `QRTZ_TRIGGER_LOG` ADD INDEX qrtz_trigger_log_index_trigger_time ( `trigger_time` );
ALTER TABLE `QRTZ_JOB_EXTEND` ADD INDEX qrtz_job_extend_index_job_create_time ( `job_create_time` );
ALTER TABLE `QRTZ_TRIGGER_LOG` ADD INDEX qrtz_trigger_log_index_execute_status ( `execute_status` );
ALTER TABLE `QRTZ_TRIGGER_LOG` ADD INDEX qrtz_trigger_log_index_job_id ( `job_id` );
ALTER TABLE `QRTZ_TRIGGER_LOCK` ADD INDEX qrtz_trigger_lock_index_job_name ( `job_name` );


ALTER TABLE `QRTZ_JOB_EXTEND` ADD INDEX qrtz_job_extend_index_job_name ( `job_name` );
ALTER TABLE `QRTZ_APP_INFO` ADD INDEX qrtz_app_info_index_app_key ( `app_key` );

ALTER TABLE `QRTZ_JOB_EXTEND` ADD COLUMN job_type tinyint NOT NULL DEFAULT '1' COMMENT '任务类型：1.同步任务；2.异步任务；';
ALTER TABLE `QRTZ_JOB_EXTEND` ADD COLUMN result_url varchar(512) NOT NULL DEFAULT '' COMMENT '结果地址';
ALTER TABLE `QRTZ_JOB_EXTEND` ADD COLUMN result_wait_time int(11) NOT NULL DEFAULT '0' COMMENT '结果时间';

ALTER TABLE `QRTZ_TRIGGER_LOG` ADD COLUMN job_type tinyint NOT NULL DEFAULT '1' COMMENT '任务类型：1.同步任务；2.异步任务；';
ALTER TABLE `QRTZ_TRIGGER_LOG` ADD COLUMN result_url varchar(512) NOT NULL DEFAULT '' COMMENT '结果地址';
ALTER TABLE `QRTZ_TRIGGER_LOG` ADD COLUMN result_wait_time int(11) NOT NULL DEFAULT '0' COMMENT '结果等待时间';
ALTER TABLE `QRTZ_TRIGGER_LOG` ADD COLUMN result_update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '结果更新时间';


