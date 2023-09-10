create table IoSsiSystem (id  bigserial not null, code varchar(255), fqdn varchar(255), primary key (id));
create table IoSsiSystemJtDescription (system_id int8 not null, description_id int8 not null, primary key (system_id, description_id));
create table IoSsiSystemJtLang (system_id int8 not null, lang_id int8 not null, primary key (system_id, lang_id));