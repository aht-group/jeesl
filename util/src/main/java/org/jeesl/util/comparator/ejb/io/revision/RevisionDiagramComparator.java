package org.jeesl.util.comparator.ejb.io.revision;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.io.revision.core.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.revision.er.JeeslRevisionDiagram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RevisionDiagramComparator<C extends JeeslRevisionCategory<?,?,C,?>,
								ERD extends JeeslRevisionDiagram<?,?,C>>
{
	final static Logger logger = LoggerFactory.getLogger(RevisionDiagramComparator.class);

    public enum Type {category};

    public Comparator<ERD> factory(Type type)
    {
        Comparator<ERD> c = null;
        RevisionDiagramComparator<C,ERD> factory = new RevisionDiagramComparator<C,ERD>();
        switch (type)
        {
            case category: c = factory.new CategoryComparator();break;
        }

        return c;
    }

    private class CategoryComparator implements Comparator<ERD>
    {
        @Override
		public int compare(ERD a, ERD b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  if(a.getCategory()!=null && b.getCategory()!=null){ctb.append(a.getCategory().getPosition(), b.getCategory().getPosition());}
			  ctb.append(a.getPosition(), b.getPosition());
			  return ctb.toComparison();
        }
    }
}