ALTER TABLE public.SecurityContext ADD COLUMN mfaLabel character varying(255);
UPDATE public.SecurityContext SET mfaLabel='XXX';
ALTER TABLE public.SecurityContext ALTER COLUMN mfaLabel SET NOT NULL;