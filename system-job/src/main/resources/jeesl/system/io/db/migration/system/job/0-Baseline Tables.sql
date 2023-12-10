create table SystemJob (id  bigserial not null, attempts int4, code text, jsonFilter text, name varchar(255), processingCounter int4, recordComplete timestamp, recordCreation timestamp, recordStart timestamp, priority_id int8, status_id int8, template_id int8, user_id int8, primary key (id));
create table SystemJobCache (id  bigserial not null, code text, data bytea, record timestamp, size int8 not null, validUntil timestamp, template_id int8, primary key (id));
create table SystemJobRobot (id  bigserial not null, code varchar(255), position int4 not null, record timestamp, visible boolean not null, primary key (id));
create table SystemJobRobotJtDescription (robot_id int8 not null, description_id int8 not null, primary key (robot_id, description_id));
create table SystemJobRobotJtLang (robot_id int8 not null, lang_id int8 not null, primary key (robot_id, lang_id));
create table SystemJobTemplate (id  bigserial not null, code varchar(255), timeout int4 not null, category_id int8, expiration_id int8, priority_id int8, type_id int8, primary key (id));
create table SystemJobTemplateJtDescription (template_id int8 not null, description_id int8 not null, primary key (template_id, description_id));
create table SystemJobTemplateJtLang (template_id int8 not null, lang_id int8 not null, primary key (template_id, lang_id));