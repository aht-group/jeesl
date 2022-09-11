package org.jeesl.controller.processor.system.constraint;

import org.jeesl.util.ReflectionUtil;

import net.sf.ahtutils.xml.system.Constraint;
import net.sf.ahtutils.xml.system.ConstraintAttribute;
import net.sf.ahtutils.xml.system.ConstraintScope;

public class ConstraintsChecker
{
	public static boolean notNull(Object object, String attribute) throws Exception
	{
		return ReflectionUtil.resolveExpression(object, attribute) != null;
	}

	public static String notNull(Object object, String attribute, ConstraintScope scope) throws Exception
	{
		if(!notNull(object, attribute))
		{
			for (Constraint con : scope.getConstraint())
			{
				if(con.getType().getCode().equals("notNull"))
				{
					for(ConstraintAttribute a : con.getConstraintAttribute())
					{
						if(a.getAttribute().equals(attribute))
						{
							return con.getDescriptions().getDescription().get(0).getValue();
						}
					}
				}
			}
		}
		return null;
	}

//	// returns true if the start Date is before the end Date
//	public static boolean dateSequence(Object object, String attribute) throws Exception
//	{
//		return ((Date)ReflectionsUtil.resolveExpression(object, attribute + "Start"))
//							.compareTo(((Date)ReflectionsUtil.resolveExpression(object, attribute + "Start"))) < 0;
//	}
}