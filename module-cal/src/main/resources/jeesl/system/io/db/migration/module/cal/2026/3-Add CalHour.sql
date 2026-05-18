create table CalHour (id  bigserial not null, time time, day_id int8, hour_id int8, primary key (id));
alter table CalHour add constraint fk_CalHour_day foreign key (day_id) references CalDay;
alter table CalHour add constraint fk_CalHour_hour foreign key (hour_id) references IoStatus;