package org.jeesl.util.comparator.xml;

import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.jeesl.model.xml.system.revision.Diagram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlXpathComparator implements Comparator<Diagram>
{
	final static Logger logger = LoggerFactory.getLogger(XmlXpathComparator.class);


    public XmlXpathComparator()
    {
        
    }

    @Override public int compare(Diagram a, Diagram b)
    {
		CompareToBuilder ctb = new CompareToBuilder();
		ctb.append(a.getPosition(), b.getPosition());
		return ctb.toComparison();
    }
}