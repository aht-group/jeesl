create table SecurityOauthKey (id  bigserial not null, code varchar(255), json text, name varchar(255), record timestamp, visible boolean not null, type_id int8, primary key (id));
create table SecurityOauthToken (id  bigserial not null, code varchar(255), nonce varchar(255), user_id int8, primary key (id));
alter table SecurityOauthKey add constraint fk_SecurityOauthKey_type foreign key (type_id) references IoStatus;
alter table SecurityOauthToken add constraint fk_SecurityOauthToken_user foreign key (user_id) references SecurityUser;