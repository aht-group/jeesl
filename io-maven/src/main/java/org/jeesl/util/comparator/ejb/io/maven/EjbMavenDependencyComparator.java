package org.jeesl.util.comparator.ejb.io.maven;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenDependency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbMavenDependencyComparator
{
	final static Logger logger = LoggerFactory.getLogger(EjbMavenDependencyComparator.class);

    public enum Type {code};

    public EjbMavenDependencyComparator()
    {
    	
    }
    
    public static Comparator<IoMavenDependency> instance(Type type)
    {
    	EjbMavenDependencyComparator factory = new EjbMavenDependencyComparator();
    	 
        Comparator<IoMavenDependency> c = null;
        switch (type)
        {
            case code: c = factory.new CodeComparator(); break;
        }

        return c;
    }

	private class CodeComparator implements Comparator<IoMavenDependency>
    {
		@Override public int compare(IoMavenDependency a, IoMavenDependency b)
        {
        	CompareToBuilder ctb = new CompareToBuilder();
        	ctb.append(a.getArtifact().getArtifact().getGroup().getCode(), b.getArtifact().getArtifact().getGroup().getCode());
        	ctb.append(a.getArtifact().getArtifact().getCode(), b.getArtifact().getArtifact().getCode());
        	ctb.append(a.getArtifact().getCode(), b.getArtifact().getCode());
        	ctb.append(a.getId(), b.getId());
        	
        	ctb.append(a.getDependsOn().getArtifact().getGroup().getCode(), b.getDependsOn().getArtifact().getGroup().getCode());
        	ctb.append(a.getDependsOn().getArtifact().getCode(), b.getDependsOn().getArtifact().getCode());
        	ctb.append(a.getDependsOn().getCode(), b.getDependsOn().getCode());
        	ctb.append(a.getId(), b.getId());
        	return ctb.toComparison();
        }
    }
}