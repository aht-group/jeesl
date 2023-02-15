package org.jeesl.factory.txt.module.rmmv;

import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvClassification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtRmmvClassificationFactory
{
	final static Logger logger = LoggerFactory.getLogger(TxtRmmvClassificationFactory.class);
	    
	public static <C extends JeeslRmmvClassification<?,?,C,?>> String positions(C classification)
	{
		StringBuilder sb = new StringBuilder();
		
		positions(sb,classification);
		
		return sb.reverse().toString();
	}

	private static <C extends JeeslRmmvClassification<?,?,C,?>> void positions(StringBuilder sb, C classification)
	{
		sb.append(classification.getPosition());
		if(classification.getParent()!=null)
		{
			sb.append(".");
			TxtRmmvClassificationFactory.positions(sb,classification.getParent());
		}
	}
}