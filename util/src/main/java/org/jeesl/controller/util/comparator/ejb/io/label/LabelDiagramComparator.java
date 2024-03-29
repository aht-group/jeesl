package org.jeesl.controller.util.comparator.ejb.io.label;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.label.er.JeeslRevisionDiagram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LabelDiagramComparator<C extends JeeslRevisionCategory<?,?,C,?>,
								ERD extends JeeslRevisionDiagram<?,?,C>>
{
	final static Logger logger = LoggerFactory.getLogger(LabelDiagramComparator.class);

    public enum Type {category};

    public Comparator<ERD> factory(Type type)
    {
        Comparator<ERD> c = null;
        LabelDiagramComparator<C,ERD> factory = new LabelDiagramComparator<C,ERD>();
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