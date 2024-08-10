alter table AomCompanyJtScope add constraint UKfg2dutnto5609n9g130obnhq2 unique (company_id, scope_id);
alter table AomCompany add constraint FKbm6sbpr8fchwfy7j14vl1t7yr foreign key (realm_id) references IoStatus;
alter table AomCompanyJtScope add constraint FK8g5dfsilcr8ksbs98676lsbt2 foreign key (scope_id) references IoStatus;
alter table AomCompanyJtScope add constraint FKd2aps4bb8eits7qe7vvrqkogr foreign key (company_id) references AomCompany;