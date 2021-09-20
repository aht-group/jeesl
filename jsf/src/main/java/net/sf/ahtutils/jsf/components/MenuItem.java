package net.sf.ahtutils.jsf.components;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FacesComponent(value="net.sf.ahtutils.jsf.components.MenuItem")
public class MenuItem extends UINamingContainer
{
	final static Logger logger = LoggerFactory.getLogger(MenuItem.class);

	public String styleClassLi(boolean active, boolean first, boolean last, String cssActive, String cssLi, String cssFirst, String cssLast)
	{
		StringBuffer sb = new StringBuffer();
		if(active){sb.append(cssActive);}
		if(cssLi!=null){sb.append(" ").append(cssLi);}
		if(first && cssFirst!=null){sb.append(" ").append(cssFirst);}
		if(last && cssLast!=null){sb.append(" ").append(cssLast);}
		return sb.toString();
	}
}