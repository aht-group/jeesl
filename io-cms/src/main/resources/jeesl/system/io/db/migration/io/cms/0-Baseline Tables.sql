create table envers.IoMarkup_ (id int8 not null, REV int8 not null, REVTYPE int2, content text, lkey varchar(255), type_id int8, primary key (id, REV));
create table IoCms (id  bigserial not null, position int4 not null, toc boolean not null, category_id int8, root_id int8, primary key (id));
create table IoCmsJtLang (cms_id int8 not null, lang_id int8 not null, primary key (cms_id, lang_id));
create table IoCmsJtLanguage (cms_id int8 not null, locale_id int8 not null);
create table IoCmsSection (id  bigserial not null, code varchar(255), position int4 not null, visible boolean not null, section_id int8, primary key (id));
create table IoCmsSectionJtLang (section_id int8 not null, lang_id int8 not null, primary key (section_id, lang_id));
create table IoCmsVisibility (id  bigserial not null, visible boolean not null, primary key (id));
create table IoMarkup (id  bigserial not null, content text, lkey varchar(255), type_id int8, primary key (id));