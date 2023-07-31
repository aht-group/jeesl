ALTER TABLE IoSsiPort RENAME TO IoSsiNat;
ALTER TABLE IF EXISTS public.iossinat ADD COLUMN service_id bigint;
INSERT INTO public.iostatus(type,id, code, "position",visible) VALUES ('ioSsiNatService', nextval('iostatus_id_seq'::regclass),'unknown', 1, 't');
ALTER TABLE IF EXISTS public.iossinat ADD CONSTRAINT fk8b7hpnrursxttp9py1in2euqn FOREIGN KEY (service_id) REFERENCES public.iostatus (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
UPDATE IoSsiNat SET service_id=(SELECT id FROM iostatus WHERE type='ioSsiNatService' and code='unknown');
ALTER TABLE IoSsiNat ALTER COLUMN service_id SET NOT NULL;