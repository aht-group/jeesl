alter table CalDay add constraint fk_CalDay_day foreign key (day_id) references IoStatus;
alter table CalDay add constraint fk_CalDay_month foreign key (month_id) references IoStatus;
alter table CalDay add constraint fk_CalDay_year foreign key (year_id) references IoStatus;
alter table CalSubscription add constraint fk_CalSubscription_category foreign key (category_id) references IoStatus;