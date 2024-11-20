create table MmgClassification (id  bigserial not null, code varchar(255), position int4 not null, rref int8 not null, visible boolean not null, graphic_id int8, parent_id int8, realm_id int8, primary key (id));
create table MmgClassificationJtLang (classification_id int8 not null, lang_id int8 not null, primary key (classification_id, lang_id));
create table MmgGallery (id  bigserial not null, primary key (id));
create table MmgItem (id  bigserial not null, lastModifiedAt timestamp, ldtImage timestamp, ldtUpload timestamp, visible boolean not null, frContainer_id int8, gallery_id int8, lastModifiedBy_id int8, primary key (id));
create table MmgItemJtLang (item_id int8 not null, lang_id int8 not null, primary key (item_id, lang_id));