alter table payment_log_record drop column timestamp;
alter table payment_log_record add column timestamp bigint not null default 0;