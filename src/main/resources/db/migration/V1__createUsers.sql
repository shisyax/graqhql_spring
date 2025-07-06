create table users (
    id bigint primary key auto_increment,
    name varchar(50) not null unique,
    email varchar(100) not null unique,
    created_at timestamp with time zone default current_timestamp,
    updated_at timestamp with time zone default current_timestamp
);