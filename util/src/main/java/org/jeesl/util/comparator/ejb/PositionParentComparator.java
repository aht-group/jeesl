package org.jeesl.util.comparator.ejb;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionParent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PositionParentComparator<T extends EjbWithPositionParent> implements Comparator<T>
{
	final static Logger logger = LoggerFactory.getLogger(PositionParentComparator.class);

	private Method m;
	
	public PositionParentComparator(Class<T> c)
	{
		m = null;
		try
		{
			T prototype = c.newInstance();
			String attribute = prototype.resolveParentAttribute();
			attribute = attribute.substring(0,1).toUpperCase()+attribute.substring(1,attribute.length());
//			logger.info("M: "+attribute);
			m = c.getDeclaredMethod("get"+attribute);
//			logger.info("Method: "+m.getName());
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		catch (NoSuchMethodException e) {e.printStackTrace();}
		catch (SecurityException e) {e.printStackTrace();}
	}
	
	public int compare(T a, T b)
    {
		  CompareToBuilder ctb = new CompareToBuilder();
		  
		  EjbWithPosition pA = null;
		  EjbWithPosition pB = null;
		  try
		  {
			Object oA = m.invoke(a);
			if(oA instanceof EjbWithPosition){pA = (EjbWithPosition)oA;}
			
			Object oB = m.invoke(b);
			if(oB instanceof EjbWithPosition){pB = (EjbWithPosition)oB;}
		  }
		  catch (IllegalAccessException e) {e.printStackTrace();}
		  catch (IllegalArgumentException e) {e.printStackTrace();}
		  catch (InvocationTargetException e) {e.printStackTrace();}
		  
		  if(pA!=null && pB!=null)
		  {
			  ctb.append(pA.getPosition(),pB.getPosition());
		  }
		  
		  ctb.append(a.getPosition(), b.getPosition());
		  return ctb.toComparison();
    }
}