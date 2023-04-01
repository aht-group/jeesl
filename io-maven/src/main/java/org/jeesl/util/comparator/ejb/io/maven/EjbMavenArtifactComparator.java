package org.jeesl.util.comparator.ejb.io.maven;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenArtifact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbMavenArtifactComparator
{
	final static Logger logger = LoggerFactory.getLogger(EjbMavenArtifactComparator.class);

    public enum Type {code};

    public EjbMavenArtifactComparator()
    {
    	
    }
    
    public static Comparator<IoMavenArtifact> instance(Type type)
    {
    	EjbMavenArtifactComparator factory = new EjbMavenArtifactComparator();
    	 
        Comparator<IoMavenArtifact> c = null;
        switch (type)
        {
            case code: c = factory.new PositionCodeComparator(); break;
        }

        return c;
    }

    private class PositionCodeComparator implements Comparator<IoMavenArtifact>
    {
        @Override public int compare(IoMavenArtifact a, IoMavenArtifact b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  ctb.append(a.getGroup().getCode(), b.getGroup().getCode());
			  ctb.append(a.getCode(), b.getCode());
			  ctb.append(a.getId(), b.getId());
			  return ctb.toComparison();
        }
    }
}