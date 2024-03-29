create table SystemConstraint (id  bigserial not null, code varchar(255), nr varchar(255), position int4 not null, level_id int8, scope_id int8, type_id int8, primary key (id));
create table SystemConstraintAlgorithm (id  bigserial not null, code varchar(255), position int4 not null, category_id int8, primary key (id));
create table SystemConstraintAlgorithmJtDescription (algorithm_id int8 not null, description_id int8 not null, primary key (algorithm_id, description_id));
create table SystemConstraintAlgorithmJtLang (algorithm_id int8 not null, lang_id int8 not null, primary key (algorithm_id, lang_id));
create table SystemConstraintJtDescription (constraint_id int8 not null, lang_id int8 not null, primary key (constraint_id, lang_id));
create table SystemConstraintJtLang (constraint_id int8 not null, lang_id int8 not null, primary key (constraint_id, lang_id));
create table SystemConstraintResolution (id  bigserial not null, position int4 not null, constraint_id int8, type_id int8, primary key (id));
create table SystemConstraintResolutionJtDescription (resolution_id int8 not null, description_id int8 not null, primary key (resolution_id, description_id));
create table SystemConstraintResolutionJtLang (resolution_id int8 not null, lang_id int8 not null, primary key (resolution_id, lang_id));
create table SystemConstraintScope (id  bigserial not null, code varchar(255), position int4 not null, category_id int8, primary key (id));
create table SystemConstraintScopeJtDescription (scope_id int8 not null, description_id int8 not null, primary key (scope_id, description_id));
create table SystemConstraintScopeJtLang (scope_id int8 not null, lang_id int8 not null, primary key (scope_id, lang_id));