package org.jeesl.factory.txt.system.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtSecurityViewFactory <L extends JeeslLang, D extends JeeslDescription,
										 C extends JeeslSecurityCategory<L,D>,
										 V extends JeeslSecurityView<L,D,C,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(TxtSecurityViewFactory.class);
    
	public static <L extends JeeslLang, D extends JeeslDescription,
					C extends JeeslSecurityCategory<L,D>,
					V extends JeeslSecurityView<L,D,C,?,?,?>,
					E extends Enum<E>>
			TxtSecurityViewFactory<L,D,C,V> instance(E localeCode)
	{
		return new TxtSecurityViewFactory<>(localeCode.toString());
	}
	public static <L extends JeeslLang, D extends JeeslDescription,
	 				C extends JeeslSecurityCategory<L,D>,
	 				V extends JeeslSecurityView<L,D,C,?,?,?>>
			TxtSecurityViewFactory<L,D,C,V> instance(String localeCode)
	{
		return new TxtSecurityViewFactory<>(localeCode);
	}
	
    public TxtSecurityViewFactory(String localeCode)
    {
    	
    } 
    
    public String debug(V view)
    {
    	StringBuffer sb = new StringBuffer();
    	sb.append(view.getCategory().getPosition()).append(".").append(view.getPosition());
    	sb.append(" ").append(view.getCode());
    	return sb.toString();
    }
    
    public String codes(Collection<V> collection)
    {
    	List<String> list = new ArrayList<>();
		for(V v : collection)
		{
			list.add(v.getCode());
		}
		return StringUtils.join(list,", "); 
    }
}