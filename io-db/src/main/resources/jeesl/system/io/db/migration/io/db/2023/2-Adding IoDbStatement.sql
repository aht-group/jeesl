create table IoDbStatement (id  bigserial not null, average float8 not null, calls int4 not null, code varchar(255), record timestamp, remark text, rows int4 not null, sql text, total float8 not null, group_id int8, host_id int8, primary key (id));
create table IoDbStatementGroup (id  bigserial not null, code varchar(255), name varchar(255), position int4 not null, remark text, system_id int8, primary key (id));
alter table IoDbStatement add constraint UKc76oo5tmpvey2xujhr4qh49df unique (host_id, group_id, record);
alter table IoDbStatement add constraint UKk7yhawmf90bmkwboqqdsj3uvy unique (code);
alter table IoDbStatement add constraint FK9jw839s86vbyj6l3flrx92niu foreign key (group_id) references IoDbStatementGroup;
alter table IoDbStatement add constraint FKfvll21c27n186v7vwqkbwi5qs foreign key (host_id) references IoSsiHost;
alter table IoDbStatementGroup add constraint FKqlqmb0i25ru5f740e3ibu06g0 foreign key (system_id) references IoSsiSystem;