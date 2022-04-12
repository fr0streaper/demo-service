alter table user_account_financial_log_record drop column timestamp;
alter table user_account_financial_log_record add column timestamp bigint not null default 0;