package org.jeesl.factory.xml.system.io.attribute;

import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeData;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeItem;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeOption;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.xml.io.attribute.Attribute;
import org.jeesl.model.xml.jeesl.QueryAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlAttributeFactory <L extends JeeslLang, D extends JeeslDescription,
									CRITERIA extends JeeslAttributeCriteria<L,D,?,?,?,OPTION,?>,
									OPTION extends JeeslAttributeOption<L,D,CRITERIA>,
									ITEM extends JeeslAttributeItem<CRITERIA,?>,
									DATA extends JeeslAttributeData<CRITERIA,OPTION,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlAttributeFactory.class);
	
	private final String localeCode;
	private final Attribute q;
	
	private final XmlOptionFactory<L,D,OPTION> xfOption;
	
	public XmlAttributeFactory(QueryAttribute query) {this(query.getLocaleCode(),query.getAttribute());}
	public XmlAttributeFactory(String localeCode, Attribute q)
	{
		this.localeCode=localeCode;
		this.q=q;
		xfOption = new XmlOptionFactory<L,D,OPTION>(localeCode);
	}
	
	public static Attribute build(){return new Attribute();}
	
	public Attribute build(DATA data)
	{
		Attribute xml = build();
		if(q.isSetCode()) {xml.setCode(data.getCriteria().getCode());}
		if(q.isSetLabel() && localeCode!=null && data.getCriteria().getName().containsKey(localeCode)) {xml.setLabel(data.getCriteria().getName().get(localeCode).getLang());}
		
		if(data.getCriteria().getType().getCode().equals(JeeslAttributeType.Code.selectOne.toString()) && data.getValueOption()!=null) {xml.setOption(xfOption.build(data.getValueOption()));}
		if(data.getCriteria().getType().getCode().equals(JeeslAttributeType.Code.bool.toString()) && data.getValueBoolean()!=null) {xml.setBool(data.getValueBoolean());}
		
		return xml;
	}
	
	public Attribute build(ITEM item)
	{
		Attribute xml = build();
		xml.setCode(item.getCriteria().getCode());
		if(q.isSetLabel() && localeCode!=null && item.getCriteria().getName().containsKey(localeCode)) {xml.setLabel(item.getCriteria().getName().get(localeCode).getLang());}
		return xml;
	}
}