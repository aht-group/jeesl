package org.jeesl.util.comparator.ejb.io.maven;

import java.util.Comparator;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.model.ejb.io.maven.module.IoMavenUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbMavenUsageComparator
{
	final static Logger logger = LoggerFactory.getLogger(EjbMavenUsageComparator.class);

    public enum Type {version};

    public EjbMavenUsageComparator()
    {
    	
    }
    
    public static Comparator<IoMavenUsage> instance(Type type)
    {
    	EjbMavenUsageComparator factory = new EjbMavenUsageComparator();
    	 
        Comparator<IoMavenUsage> c = null;
        switch (type)
        {
            case version: c = factory.new PositionCodeComparator(); break;
        }

        return c;
    }

    private class PositionCodeComparator implements Comparator<IoMavenUsage>
    {
        @Override public int compare(IoMavenUsage a, IoMavenUsage b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  ctb.append(a.getVersion().getPosition(), b.getVersion().getPosition());
			  
			  int aPosition = a.getModule().getPosition(); if(Objects.nonNull(a.getModule().getParent())) {aPosition = a.getModule().getParent().getPosition();}
			  int bPosition = b.getModule().getPosition(); if(Objects.nonNull(b.getModule().getParent())) {bPosition = b.getModule().getParent().getPosition();}
			  ctb.append(aPosition,bPosition);
			  
			  if(ObjectUtils.allNotNull(a.getModule().getParent(),b.getModule().getParent())) {ctb.append(a.getModule().getPosition(), b.getModule().getPosition());}
			  
			  ctb.append(a.getId(), b.getId());
			  return ctb.toComparison();
        }
    }
}