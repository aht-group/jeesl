package org.jeesl.jsf.function;

import java.lang.reflect.*;
import java.util.Map;
import java.util.Objects;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithName;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Title
{
	final static Logger logger = LoggerFactory.getLogger(Title.class);
	
    private Title() {}
    
    public static String titleFor(Object object, String localeCode)
    {
		String title = "nothing matched";
		if (Objects.isNull(object))
		{
			return "is Null";
		}
		if (object instanceof EjbWithName)
		{
			EjbWithName withName = (EjbWithName) object;
			if (withName.getName()!=null)
			{
				title = ((EjbWithName) object).getName();
			} else
			{
				title = "withName has no name configured";
			}
		}
		else if (object instanceof EjbWithLang)
		{
			EjbWithLang withLang = (EjbWithLang) object;
			if (withLang.getName()!=null && withLang.getName().containsKey(localeCode))
			{
				Map<String, JeeslLang> translationsMap = (Map<String, JeeslLang>) withLang.getName();
				title = translationsMap.get(localeCode).getLang();
			} else
			{
				title = "withLang has no entry for " +localeCode;
			}
		}
		else if (hasGetNameReturningMap(object.getClass()))
		{
			EjbWithLang withLang = (EjbWithLang) object;
			if (withLang.getName()!=null && withLang.getName().containsKey(localeCode))
			{
				Map<String, JeeslLang> translationsMap = (Map<String, JeeslLang>) withLang.getName();
				title = translationsMap.get(localeCode).getLang();
			} else
			{
				title = "withLang has no entry for " +localeCode;
			}
			
		}
	//	logger.info("Found " +title +" for entity of type " +object.getClass().getSimpleName() +" for " +localeCode);
    	return title;
    }
	
	public static boolean hasGetNameReturningMap(Class c) 
	{
		if (Objects.isNull(c)){return false;}
        try {
            Method method = c.getMethod("getName");

            // Check if return type is parameterized and a Map
            Type returnType = method.getGenericReturnType();

            if (returnType instanceof ParameterizedType) {
                ParameterizedType paramType = (ParameterizedType) returnType;

                // Check raw type is Map
                if (paramType.getRawType() instanceof Class && Map.class.isAssignableFrom((Class<?>) paramType.getRawType())) {
                    Type[] typeArgs = paramType.getActualTypeArguments();

                    // Check key type is String and value type is the given class
                    if (typeArgs.length == 2 &&
                        typeArgs[0] == String.class &&
                        typeArgs[1] instanceof Class &&
                        JeeslLang.class.isAssignableFrom((Class<?>) typeArgs[1])) {
		                return true;
                    }
                }
            }

        } catch (NoSuchMethodException e) {
            // Method not found
            return false;
        }
		return false;
	}
}
