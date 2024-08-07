create table account
(
    id text primary key
);

create table notation
(
    id          uuid default gen_random_uuid() primary key,
    source_text text not null,
    target_text text not null,
    user_id     text not null,

    foreign key (user_id) references account (id) on delete cascade
);
