create table CalDay (id  bigserial not null, record date, day_id int8, month_id int8, year_id int8, primary key (id));
create table CalHour (id  bigserial not null, time time, day_id int8, hour_id int8, primary key (id));
create table CalSubscription (id  bigserial not null, code varchar(255), category_id int8, primary key (id));