package org.jeesl.util.comparator.pojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringComparator
{
	final static Logger logger = LoggerFactory.getLogger(StringComparator.class);
    
    public static boolean empty(String s)
    {
        if(s==null){return true;}
        else {return s.trim().isEmpty();}
    }
}