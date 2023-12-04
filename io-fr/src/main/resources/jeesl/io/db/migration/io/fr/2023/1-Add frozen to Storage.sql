ALTER TABLE public.iofilestorage ADD COLUMN frozen boolean;
UPDATE public.iofilestorage SET frozen='f';
ALTER TABLE public.iofilestorage ALTER COLUMN frozen SET NOT NULL;