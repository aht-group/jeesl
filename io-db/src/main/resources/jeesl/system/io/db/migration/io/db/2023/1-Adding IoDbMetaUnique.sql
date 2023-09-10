create table IoDbMetaUnique (id  bigserial not null, position int4 not null, column_id int8, constraint_id int8, primary key (id));
alter table IoDbMetaUnique add constraint FKab8wgjgmbxhyj1kfr0dmerdpd foreign key (column_id) references IoDbMetaColumn;
alter table IoDbMetaUnique add constraint FKn3d9mo0wgfkgwxpg79dw46a6u foreign key (constraint_id) references IoDbMetaConstraint;