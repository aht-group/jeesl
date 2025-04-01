package org.jeesl.util.query.ejb;

import java.lang.annotation.Annotation;

import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslInterfaceAnnotationQuery
{
	final static Logger logger = LoggerFactory.getLogger(JeeslInterfaceAnnotationQuery.class);
	
	public static boolean isAnnotationPresent(Class<? extends Annotation> annotation, Class<?> c)
	{	
//		logger.info("Checking "+c.getSimpleName()+" has "+annotation.getSimpleName());
		Class<?> a = JeeslInterfaceAnnotationQuery.findClass(annotation,c);
		if(a!=null) {return true;}
		else {return false;}
	}
	
	public static Class<?> findClass(Class<? extends Annotation> annotation, Class<?> c)
	{
		for(Class<?> i : c.getInterfaces())
		{
			if(i.isAnnotationPresent(annotation))
			{
//				logger.info(i.getSimpleName()+" has");
				return i;
			}
		}
		for(Class<?> i : c.getInterfaces())
		{
			return JeeslInterfaceAnnotationQuery.findClass(annotation,i);
		}
		return null;
	}
	

}