package org.jeesl.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;

/**
 *
 * This utility can be used when a setter should be called without knowing a priori the exact type cast to be made
 */
public class SetterHelper
{
	
    /**
     * Uses reflection to set a property. The parameter will be casted as needed.
     * @param propertyName The objects property, e.g. name or age
     * @param targetObject The object for which the parameter should be set
     * @param parameter The parameter, e.g. "Mr Smith" or "35"
     */
    public static void set(String propertyName, Object targetObject, Object parameter)
    {
        try {
            // System.out.println("Property " +propertyName +" is not handled and will be set by standard logic");
            // System.out.println("Value to be set is " +parameter);
            // System.out.println("Target Object is " +targetObject.getClass().getCanonicalName());
            Object[] arguments = new Object[1];
            Method setter = findSetMethod(targetObject, propertyName);
            // The old way to get the write Method was not working in some cases
            // PropertyUtils.getPropertyDescriptor(targetObject, propertyName).getWriteMethod();
            arguments[0] = prepareParameter(parameter, setter.getParameterTypes()[0]);

            setter.invoke(targetObject, arguments);	
        } catch (Exception e)
        {
            System.err.println("Could not set " +propertyName + " to " +parameter.toString());
            //e.printStackTrace();
        }

    }

    /**
     * Performs the type cast to match the required class
     * Often, the exact class might not be the right class for the setter to be called (e.g. having a Double for setting an int setter)
     * @param parameter The value
     * @param targetClass The class accepted by the setter
     * @return
     */
    public static Object prepareParameter(Object parameter, Class targetClass)
    {
        Object correctParameter = parameter;
        String parameterClass = targetClass.getCanonicalName();
        // Needed to correct the Class of the general number
        if (parameterClass.equals("long"))
        {
            Number number = (Number) parameter;
            correctParameter = number.longValue();
        }

        // Needed to correct the Class of the general number
        else if (parameterClass.equals("int"))
        {
            if (parameter.getClass().equals(String.class))
            {
                String param = (String) parameter;
                correctParameter = Integer.parseInt(param);
            }
            else
            {
                Number number = (Number) parameter;
                correctParameter = number.intValue();
            }
        }

        // Needed to correct the Class of the general number
        else if (parameterClass.equals("java.lang.Integer"))
        {
            Number number = (Number) parameter;
            correctParameter = new Integer(number.intValue());
        }

        // Needed to correct the Class of the general number
        else if (parameterClass.equals("java.lang.Double"))
        {
            Number number = (Number) parameter;
            correctParameter = new Double(number.doubleValue());
        }

        // This is important if the String is a Number, Excel will format the cell to be a "general number"
        else if (parameterClass.equals("java.lang.String"))
        {
            if (parameter.getClass().getName().equals("java.lang.Double"))
            {
               Double n			= (Double) parameter;
                // This version is not working! It produces results such as this:
                // Result 2147483647 taken from 3.57305101E9 (toString of n)
                // correctParameter	= "" +n.intValue();
                BigDecimal bd           = new BigDecimal(n);
                correctParameter	= bd.toPlainString();
            }
            else
            {
                correctParameter = parameter +"";
            }
        }

        // This is important if the Boolean is a Number or String
        else if (parameterClass.equals("java.lang.Boolean") || parameterClass.equals("boolean"))
        {
            if (parameter.getClass().equals(String.class))
            {
                String value = (String) parameter;
                if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("1") || value.equalsIgnoreCase("1"))
                {
                    correctParameter = true;
                }
                else
                {
                    correctParameter = false;
                }
            }
            if (parameter.getClass().equals(Number.class))
            {
                Number number = (Number) parameter;
                if (number.intValue() == 0)
                {
                    correctParameter = false;
                }
            }
        }
        return correctParameter;
    }

    /**
     * In some cases, the simple use of PropertyUtils is not sufficient to find the correct set___ method,
     * so here also the superclass is included when searching for it
     * @param o         The Object that you want to invoke the setter on
     * @param property  The property name (e.g. Name)
     * @return          The correct set___ method for the property (e.g. setName)
     */
    public static Method findSetMethod(Object o, String property)
    {
        String methodName		= "set" +property;
        
        // Now find the correct method
        Method[] methods = o.getClass().getDeclaredMethods();
        Method m         = null;
        for (Method method : methods)
        {
            if (method.getName().equals(methodName))
            {
                if (Modifier.isPrivate(method.getModifiers()))
                {
                    method.setAccessible(true);
                }
                m = method;
            }
        }

        if (m == null)
        {
            // Using superclass
            Method[] methodsOfParent = o.getClass().getSuperclass().getDeclaredMethods();
            for (Method method : methodsOfParent)
            {
                if (method.getName().equals(methodName))
                {
                    if (Modifier.isPrivate(method.getModifiers()))
                    {
                        method.setAccessible(true);
                    }
                    m = method;
                }
            }
        }
        return m;
    }
}
