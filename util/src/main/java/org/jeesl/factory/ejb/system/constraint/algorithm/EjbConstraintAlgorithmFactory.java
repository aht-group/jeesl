package org.jeesl.factory.ejb.system.constraint.algorithm;

import java.util.List;

import org.jeesl.interfaces.model.system.constraint.algorithm.JeeslConstraintAlgorithm;
import org.jeesl.interfaces.model.system.constraint.algorithm.JeeslConstraintAlgorithmGroup;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbConstraintAlgorithmFactory <L extends JeeslLang, D extends JeeslDescription,
											ALGCAT extends JeeslConstraintAlgorithmGroup<L,D,ALGCAT,?>,
											ALGO extends JeeslConstraintAlgorithm<L,D,ALGCAT>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbConstraintAlgorithmFactory.class);
	
	private final Class<ALGO> cAlgorithm;
	
	public EjbConstraintAlgorithmFactory(final Class<ALGO> cAlgorithm)
	{
        this.cAlgorithm = cAlgorithm;
	}
	
	public ALGO build(ALGCAT category, List<ALGO> list)
	{
		ALGO ejb = null;
		try
		{
			ejb = cAlgorithm.newInstance();
			ejb.setPosition(0);
			ejb.setCategory(category);
			if(list==null) {ejb.setPosition(1);}else {ejb.setPosition(list.size()+1);}
			
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}