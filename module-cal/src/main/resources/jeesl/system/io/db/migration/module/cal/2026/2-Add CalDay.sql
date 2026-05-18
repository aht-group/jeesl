create table CalDay (id  bigserial not null, record date, day_id int8, month_id int8, year_id int8, primary key (id));
alter table CalDay add constraint fk_CalDay_day foreign key (day_id) references IoStatus;
alter table CalDay add constraint fk_CalDay_month foreign key (month_id) references IoStatus;
alter table CalDay add constraint fk_CalDay_year foreign key (year_id) references IoStatus;