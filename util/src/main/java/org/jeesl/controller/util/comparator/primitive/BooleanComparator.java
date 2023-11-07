package org.jeesl.controller.util.comparator.primitive;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BooleanComparator
{
	final static Logger logger = LoggerFactory.getLogger(BooleanComparator.class);
    
	private Boolean value;
	
	public static BooleanComparator instance() {return new BooleanComparator();}
	private BooleanComparator()
	{
		value = null;
	}
	
	public Boolean and(Boolean other)
	{
		if(Objects.isNull(value)) {value=other;}
		else if(Objects.nonNull(other))
		{
			value = value.booleanValue() && other.booleanValue();
		}
		return value;
	}
	public boolean toResult(boolean fallback)
	{
		if(Objects.nonNull(value)) {return value;}
		else {return fallback;}
	}
	
	
	public static boolean inactive(Boolean b)
    {
       return !active(b);
    }
    public static boolean active(Boolean b)
    {
        if(b==null){return false;}
        else {return b;}
    }
    
    public static int emptyInt(Object o)
    {
    	if(o==null) {return 0;}
    	else {return 1;}
    }
}