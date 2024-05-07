package org.jeesl.factory.txt.system.constraint;

import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtConstraintFactory <CONSTRAINT extends JeeslConstraint<?,?,?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(TxtConstraintFactory.class);

	private final String localeCode;
	
	public static <CONSTRAINT extends JeeslConstraint<?,?,?,?,?,?>, LOC extends Enum<LOC>> TxtConstraintFactory<CONSTRAINT> instance(LOC locale)
	{
		return new TxtConstraintFactory<>(locale.toString());
	}
	public static <CONSTRAINT extends JeeslConstraint<?,?,?,?,?,?>> TxtConstraintFactory<CONSTRAINT> instance(String locale)
	{
		return new TxtConstraintFactory<>(locale.toString());
	}
	
	private TxtConstraintFactory(String localeCode)
	{
		this.localeCode=localeCode;
	}
	
	public String debug(CONSTRAINT c)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(c.getCode());
		
		return sb.toString();
				
	}
}