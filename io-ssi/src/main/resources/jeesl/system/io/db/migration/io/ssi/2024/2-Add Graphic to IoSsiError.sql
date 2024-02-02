ALTER TABLE public.IoSsiError ADD COLUMN graphic_id bigint;
ALTER TABLE public.IoSsiError ADD CONSTRAINT fk_IoSsiError_graphic FOREIGN KEY (graphic_id) REFERENCES public.iographic (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
