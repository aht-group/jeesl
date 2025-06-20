alter table IoReportStyle add constraint UK262p54elvh9jgql94ekei93cj unique (code);
alter table IoReportStyleJtDescription add constraint UK_cb5f34yjl0olwlnhgdtsqfbp unique (description_id);
alter table IoReportStyleJtLang add constraint UK_1i0o0h1abiq8lkuvjbf2tksnr unique (lang_id);
alter table IoReportStyle add constraint fk_IoReportStyle_alignment foreign key (alignment_id) references IoStatus;
alter table IoReportStyleJtDescription add constraint FK6hvbkt7gydm45d7uukn4hgsp foreign key (description_id) references IoDescription;
alter table IoReportStyleJtDescription add constraint FKe12a6ckl95f7jtwuc44x9e9rq foreign key (style_id) references IoReportStyle;
alter table IoReportStyleJtLang add constraint FKffj55nak3cqfis3wfl8hep5lo foreign key (lang_id) references IoLang;
alter table IoReportStyleJtLang add constraint FK6lwba031q0nmljdnag98xh8t5 foreign key (style_id) references IoReportStyle;