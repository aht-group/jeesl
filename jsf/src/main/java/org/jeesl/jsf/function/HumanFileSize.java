package org.jeesl.jsf.function;

public final class HumanFileSize
{
	public HumanFileSize()
    {

    }
    
    public static String humanFileSize(Long value)
    {
    	if(value==null) {return "";}
    	return humanReadableByteCount(value,true);
    }
    public static String humanFileSize(Integer value)
    {
    	if(value==null) {return "";}
    	return humanReadableByteCount(value.longValue(),true);
    }
    
    public static String humanReadableByteCount(long bytes, boolean si)
    {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}