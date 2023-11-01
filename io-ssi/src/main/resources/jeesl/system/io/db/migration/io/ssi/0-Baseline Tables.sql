create table IoSsiHost (id  bigserial not null, code varchar(255), fqdn varchar(255), hardwareManufacturer varchar(255), hardwareModel varchar(255), ipAddr varchar(255), memory varchar(255), os varchar(255), position int4 not null, system_id int8, primary key (id));
create table IoSsiHostJtDescription (host_id int8 not null, description_id int8 not null, primary key (host_id, description_id));
create table IoSsiHostJtLang (host_id int8 not null, lang_id int8 not null, primary key (host_id, lang_id));
create table IoSsiSystem (id  bigserial not null, code varchar(255), fqdn varchar(255), primary key (id));
create table IoSsiSystemJtDescription (system_id int8 not null, description_id int8 not null, primary key (system_id, description_id));
create table IoSsiSystemJtLang (system_id int8 not null, lang_id int8 not null, primary key (system_id, lang_id));
create table IoSsiContext (id  bigserial not null, classA_id int8, classB_id int8, classC_id int8, entity_id int8, json_id int8, system_id int8, primary key (id));