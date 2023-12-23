package org.jeesl.factory.xml.system.io.attribute;

import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCategory;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeData;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeItem;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeOption;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.xml.io.attribute.Attributes;
import org.jeesl.model.xml.jeesl.QueryAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlAttributesFactory <L extends JeeslLang, D extends JeeslDescription,
									CAT extends JeeslAttributeCategory<L,D,?,CAT,?>,
									
									CRITERIA extends JeeslAttributeCriteria<L,D,?,CAT,?,OPTION,SET>,
									OPTION extends JeeslAttributeOption<L,D,CRITERIA>,
									SET extends JeeslAttributeSet<L,D,?,CAT,ITEM>,
									ITEM extends JeeslAttributeItem<CRITERIA,SET>,
									DATA extends JeeslAttributeData<CRITERIA,OPTION,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlAttributesFactory.class);
	
	private final Attributes q;
	
	private XmlAttributeFactory<L,D,CRITERIA,OPTION,ITEM,DATA> xfAttribute;
	
	public XmlAttributesFactory(QueryAttribute query) {this(query.getLocaleCode(),query.getAttributes());}
	public XmlAttributesFactory(String localeCode, Attributes q)
	{
		this.q=q;
		if(!q.getAttribute().isEmpty()) {xfAttribute = new XmlAttributeFactory<L,D,CRITERIA,OPTION,ITEM,DATA>(localeCode,q.getAttribute().get(0));}
	}
	
	public static Attributes build() {return new Attributes();}
	
	public <E extends Enum<E>> Attributes build(E code, SET set)
	{
		Attributes xml = build();
		xml.setCode(code.toString());
		
		if(!q.getAttribute().isEmpty())
		{
			for(ITEM item : set.getItems())
			{
				xml.getAttribute().add(xfAttribute.build(item));
			}
		}
		
		return xml;
	}
}