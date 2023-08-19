package org.jeesl.controller.util.comparator.ejb.system.security;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityViewComparator<V extends JeeslSecurityView<?,?,?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(SecurityViewComparator.class);

    public enum Type {position};

    public SecurityViewComparator()
    {
    	
    }
    
    public Comparator<V> factory(Type type)
    {
        Comparator<V> c = null;
        SecurityViewComparator<V> factory = new SecurityViewComparator<V>();
        switch (type)
        {
            case position: c = factory.new PositionCodeComparator();break;
        }

        return c;
    }

    private class PositionCodeComparator implements Comparator<V>
    {
        public int compare(V a, V b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  ctb.append(a.getCategory().getPosition(), b.getCategory().getPosition());
			  ctb.append(a.getPosition(), b.getPosition());
			  return ctb.toComparison();
        }
    }
}