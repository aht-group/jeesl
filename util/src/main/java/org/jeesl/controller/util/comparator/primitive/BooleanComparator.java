package org.jeesl.controller.util.comparator.primitive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BooleanComparator
{
	final static Logger logger = LoggerFactory.getLogger(BooleanComparator.class);
    
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