alter table booking_log drop column timestamp;
alter table booking_log add column timestamp bigint not null default 0;