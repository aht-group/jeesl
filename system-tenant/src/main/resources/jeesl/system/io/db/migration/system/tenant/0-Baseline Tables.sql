create sequence TenantStatus_id_seq start 1 increment 1;
create table TenantStatus (type varchar(31) not null, id int8 not null, code varchar(255), locked boolean, position int4 not null, rref int8 not null, style varchar(255), symbol varchar(255), visible boolean not null, graphic_id int8, parent_id int8, realm_id int8, primary key (id));
create table TenantStatusJtDescription (status_id int8 not null, description_id int8 not null, primary key (status_id, description_id));
create table TenantStatusJtLang (status_id int8 not null, lang_id int8 not null, primary key (status_id, lang_id));