ALTER TABLE public.systemjobcache ADD COLUMN size bigint;
UPDATE public.systemjobcache SET size=0;
ALTER TABLE public.systemjobcache ALTER COLUMN size SET NOT NULL;