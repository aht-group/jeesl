package org.jeesl.util;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Named;

import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.beanutils.PropertyUtils;
import org.exlp.util.io.StringUtil;

@Named
public class ReflectionUtil
{
    final static Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * Inspect if an Object has some given method
     * @param object     The parent Object (e.g. Person)
     * @param methodName The name of the method (e.g. setName)
     * @return Is method there? (e.g. true)
     */
    public static Boolean hasMethod(Object object, String methodName)
    {
        Method[] methods  = object.getClass().getMethods();
        Boolean hasMethod = false;
        for (Method method : methods)
        {
            if (method.getName().equals(methodName))
            {
                hasMethod = true;
            }
        }
        return hasMethod;
    }

    /**
     * Intended to get the class of the first argument to be passed to a method
     * @param object     The parent Object (e.g. Person)
     * @param methodName The name of the method (e.g. setName)
     * @return Class of the first parameter (e.g. java.lang.String)
     */
     public static Class<?> getParameterClass(Object object, String methodName)
     {
        logger.trace("Getting parameter class for " +methodName +" in class " +object.getClass().getSimpleName());

        // Now find the correct method
        Method[] methods = object.getClass().getMethods();
        Class<?> parameter  = null;
        for (Method method : methods)
        {
            if (method.getName().equals(methodName))
            {
                parameter = method.getParameterTypes()[0];
            }
        }
        return parameter;
     }

    /*
     * First skeleton for traversing through .-separated methods (get or create/set)
     * NOT READY YET!
     * @param parent      The parent Object
     * @param expression  .-separated fields (eg. person.name.lastName)
     * @return The parent Object now including the value to be set
     */
    public static Object resolveExpression(Object parent, String expression) throws Exception
    {
        if(parent != null)
        {
            if (expression.contains("."))
            {
                int elements = expression.split("\\.").length;
                if (logger.isTraceEnabled()){logger.trace("Still resolving " +elements);}
                String pathToProperty = expression.substring(0, expression.indexOf("."));
                if (logger.isTraceEnabled()){logger.trace("Path to Property " +pathToProperty);}
                String getter         = "get" +(pathToProperty.substring(0, 1).toUpperCase() + pathToProperty.substring(1));
                if (logger.isTraceEnabled()){logger.trace("Getter: " +getter);}
                Object nextObject = simpleInvokeMethod(getter,
                                new Object[] { },
                                parent.getClass(),
                                parent);
                return resolveExpression(nextObject, expression.substring(expression.indexOf(".")+1, expression.length()));
            }
            else
            {
                String methodName = "get" +(expression.substring(0, 1).toUpperCase() + expression.substring(1));
                if (logger.isTraceEnabled()){logger.trace("Requesting value by invoking " +methodName + "()");}
                return simpleInvokeMethod(methodName,
                                new Object[] { },
                                parent.getClass(),
                                parent);
            }
        }
        else
        {
            return null;
        }
    }

    /*
    * Invoke a method on a given object
    * @param methodName   Method to be invoked (e.g. "setId")
    * @param parameters   Array of parameters to be used as they appear in the method (e.g. [Long id])
    * @param targetClass  Targeted class
    * @param target       The actual object that holds the method to be invoked
    * @return The returned object (e.g. the long value of the ID) or NULL if no method with @param methodName was found
    */
    public static Object simpleInvokeMethod(String   methodName,
                            Object[] parameters,
                            Class<?> targetClass,
                            Object   target)        throws Exception
    {
        if (logger.isTraceEnabled()){logger.trace("Invoking " +methodName);}

        // Now find the correct method
        if (logger.isTraceEnabled()){logger.trace("Searching for methods of class " +targetClass.getName());}
        Method[] methods = targetClass.getMethods();
        Method m         = null;
        for (Method method : methods)
        {
            if (logger.isTraceEnabled()){logger.trace("Found method " +method.getName());}
            if (method.getName().equals(methodName))
            {
                    m = method;
            }
        }
        if (m != null) 
        {
            if (Modifier.isPrivate(m.getModifiers()))
            {
                m.setAccessible(true);
            }
        return m.invoke(target, parameters);
        }
        else
        {
            return null;
        }
    }

    public static String toTable(Class<?> c) throws JeeslNotFoundException
    {
        Annotation a = c.getAnnotation(Table.class);
        if(a!=null)
        {
            Table t = (Table)a;
            return t.name();
        }
        return "--";
    }

    public static String getReverseMapping(String src, String aSrc, String dst) throws JeeslNotFoundException
    {
        try
        {
            Class<?> cSrc = Class.forName(src);
            Class<?> cDst = Class.forName(dst);

            return getReverseMapping(cSrc,aSrc,cDst);
        }
        catch (ClassNotFoundException e) {throw new JeeslNotFoundException(e.getMessage());}
     }

     private static String getReverseMapping(Class<?> cSrc, String aSrc, Class<?> cDst) throws JeeslNotFoundException
     {
        if(logger.isTraceEnabled())
        {
            logger.trace(StringUtil.stars());
            logger.trace("Reverse Mapping");
            logger.trace("cSrs: "+cSrc.getName());
            logger.trace("aSrc: "+aSrc);
            logger.trace("cDst: "+cDst.getName());
        }

        List<Field> fields = toFields(cDst);
        for(Field f : fields)
        {
            if(f.getType().getName().equals(cSrc.getName()))
            {
                    return f.getName();
            }
        }

        throw new JeeslNotFoundException("No reverse mapping found ("+cSrc.getName()+"."+aSrc+") -> "+cDst.getName());
     }

    public static List<Field> toFields(Class<?> c)
    {
            List<Field> fields = new ArrayList<Field>();
            fields.addAll(Arrays.asList(c.getDeclaredFields()));

            Class<?> cSuper = c.getSuperclass();
            Annotation a = cSuper.getAnnotation(MappedSuperclass.class);
            if(a!=null)
            {
                    fields.addAll(toFields(cSuper));
            }

            return fields;
    }

    /**
     * Read all properties of a class and return a list of their names as Strings
     * @param clazz The Class to get the properties list for
     * @return  The list of property names
     */
    public static List<String> getPropertiesAsList(Class clazz)
    {
        List<String> propertyNames = new ArrayList<>();
        for (PropertyDescriptor desc : PropertyUtils.getPropertyDescriptors(clazz))
        {
            if (desc.getWriteMethod() != null)
            {
                try
                {
                    propertyNames.add(desc.getName());
                }
                catch (IllegalArgumentException e) {logger.error("Could not read property name" +e.getMessage());}
            }
            else
            {
                System.out.println("No SETTER available for property " +desc.getName() +" - skipping");
            }
        }
        return propertyNames;
    }
    
    public static Class getTypeOfList(Object object, String property) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
	PropertyDescriptor desc = PropertyUtils.getPropertyDescriptor(object, property);
	for (TypeVariable t : desc.getReadMethod().getTypeParameters())
	{
	    if (logger.isTraceEnabled()){logger.trace("t " +t.getTypeName());}
	}
	Field field = object.getClass().getField(property); 

        ParameterizedType type = (ParameterizedType) field.getGenericType();

        for (Type typeArgument : type.getActualTypeArguments())
        {
            if (logger.isTraceEnabled()){logger.trace("  " + typeArgument);}
        }
	return type.getRawType().getClass();
    }

	public static Class getTypeOfMapValues(Object object, String property) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
		Field field = object.getClass().getDeclaredField(property); 
		ParameterizedType type = (ParameterizedType) field.getGenericType();

		Class c = null;

		// !!! This needs to be solved more robust later instead of using this String based approach
        for (Type typeArgument : type.getActualTypeArguments())
        {
            if (logger.isTraceEnabled()){logger.trace("  " + typeArgument);}
			String stringRepresentation = type.toString();
			String secondParameter		= type.toString().substring(stringRepresentation.indexOf(", ")+2, stringRepresentation.length()-1);
			try 
			{
				c = Class.forName(secondParameter);
			} catch (ClassNotFoundException ex) {
				logger.error("Could not create class for the name " +secondParameter);
			}
        }
		return c;
    }
}
