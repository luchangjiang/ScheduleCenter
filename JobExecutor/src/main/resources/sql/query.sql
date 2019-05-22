
# 查询间隔时间
SELECT
	a.id aid,
	b.id bid,
	a.trigger_time,
	b.trigger_time,
	UNIX_TIMESTAMP(b.trigger_time) - UNIX_TIMESTAMP(a.trigger_time)
FROM
	qrtz_trigger_log a,
	qrtz_trigger_log b
WHERE
	a.id = b.id - 1
AND UNIX_TIMESTAMP(b.trigger_time) - UNIX_TIMESTAMP(a.trigger_time) < 1
AND a.id > 0;


select address, max(c), min(c), avg(c) from (select from_unixtime(floor(unix_timestamp(trigger_time))) time,trigger_address address,  count(*) c from QRTZ_TRIGGER_LOG group by time, trigger_address) t group by address;



select address, max(c), min(c), avg(c) from (select from_unixtime(floor(unix_timestamp(trigger_time))) time,trigger_address address,  count(*) c from QRTZ_TRIGGER_LOG group by time, trigger_address) t group by address;

select from_unixtime(floor(unix_timestamp(trigger_time))) time,trigger_address address,  count(*) c from QRTZ_TRIGGER_LOG group by time, trigger_address having c > 20;

select count(*) from qrtz_trigger_log;

select count(*) from qrtz_job_extend;

select job_id, count(*) from (select job_id, count(*) c from qrtz_trigger_log group by job_id) t where c > 1;


select * from qrtz_trigger_log where job_id = '181AD34535C54CBBAE46646885A18865';

select trigger_time, job_id, count(*) c from qrtz_trigger_log group by job_id, trigger_time having count(*) > 1;

select  job_id, count(*) c from qrtz_trigger_log group by job_id  having c > 1

select job_id, trigger_address, trigger_time from qrtz_trigger_log where job_id in(select job_id from qrtz_trigger_log group by job_id  having count(*) > 1) order by job_id;

truncate table QRTZ_CRON_TRIGGERS;
truncate table QRTZ_TRIGGERS;
truncate table QRTZ_FIRED_TRIGGERS;
truncate table QRTZ_JOB_DETAILS;
truncate table QRTZ_JOB_EXTEND;
truncate table QRTZ_PAUSED_TRIGGER_GRPS;
truncate table QRTZ_SIMPLE_TRIGGERS;
truncate table QRTZ_TRIGGER_LOCK;
truncate table QRTZ_TRIGGER_LOG;
truncate table QRTZ_TRIGGERS;


