create type booking_status as enum ('FAILED', 'SUCCESS');

create table if not exists booking (
    id uuid primary key,
    order_id uuid
);

create table if not exists booking_log (
    id uuid primary key,
    itemId uuid not null,
    status booking_status not null,
    amount integer not null,
    timestamp timestamp not null,
    booking_id uuid
);