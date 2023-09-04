create sequence iostatus_id_seq start 1 increment 1;
create table IoDescription (id  bigserial not null, lang text, lkey varchar(255), styled boolean, primary key (id));
create table IoGraphic (id  bigserial not null, color varchar(255), colorBorder varchar(255), data bytea, size int4, sizeBorder int4, versionLock int8, style_id int8, type_id int8, primary key (id));
create table IoGraphicComponent (id  bigserial not null, color varchar(255), css boolean not null, offsetX float8 not null, offsetY float8 not null, position int4 not null, rotation float8 not null, size float8 not null, visible boolean not null, graphic_id int8, shape_id int8, primary key (id));
create table IoLang (id  bigserial not null, lang varchar(255), lkey varchar(255), primary key (id));
create table IoStatus (type varchar(32) not null, id int8 not null, code varchar(255), image varchar(255), imageAlt varchar(255), locked boolean, position int4 not null, style varchar(255), symbol varchar(255), visible boolean not null, graphic_id int8, parent_id int8, primary key (id));
create table IoStatusJtDescription (status_id int8 not null, description_id int8 not null, primary key (status_id, description_id));
create table IoStatusJtLang (status_id int8 not null, lang_id int8 not null, primary key (status_id, lang_id));
alter table IoStatus add constraint UKtcfqyk8qmj517jwroiobdrool unique (type, code);