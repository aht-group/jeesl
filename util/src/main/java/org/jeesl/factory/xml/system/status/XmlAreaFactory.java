package org.jeesl.factory.xml.system.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xml.system.io.locale.XmlLabelFactory;
import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Area;
import net.sf.ahtutils.xml.status.Status;

public class XmlAreaFactory<S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription>
{
	final static Logger logger = LoggerFactory.getLogger(XmlAreaFactory.class);
	
	private static boolean errorPrinted = false;
	
	private String localeCode;
	private Area q;
	
	private XmlLabelFactory<L> xfLabel;
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescription;
	
	public XmlAreaFactory(Area q){this(null,q);}
	public XmlAreaFactory(String localeCode,Area q)
	{
		this.localeCode=localeCode;
		this.q=q;
		
		if(Objects.nonNull(q.getLangs())){xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(Objects.nonNull(q.getLabel())) {xfLabel = new XmlLabelFactory<>(localeCode);}
		if(Objects.nonNull(q.getDescriptions())) {xfDescription = new XmlDescriptionsFactory<D>(q.getDescriptions());}
	}
	
	public Area build(S ejb){return build(ejb,null);}
	public Area build(S ejb, String group)
	{
		Area xml = new Area();
		if(Objects.nonNull(q.getId())) {xml.setId(ejb.getId());}
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(ejb.getPosition());}
		xml.setGroup(group);
		
		if(Objects.nonNull(q.getLangs())) {xml.setLangs(xfLangs.getUtilsLangs(ejb.getName()));}
		if(Objects.nonNull(q.getDescriptions())) {xml.setDescriptions(xfDescription.create(ejb.getDescription()));}
		if(Objects.nonNull(q.getLabel())) {xml.setLabel(xfLabel.build(ejb));}
		
		return xml;
	}
	
	public static <E extends Enum<E>> Area build(E code){return create(code.toString());}
	public static Area create(String code)
	{
		Area xml = new Area();
		xml.setCode(code);
		return xml;
	}
	
	public static Area label(String code, String label)
	{
		Area xml = new Area();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
	
	public static Area id()
	{
		Area xml = new Area();
		xml.setId(0l);
		return xml;
	}
	
	public static List<Long> toIds(List<Area> list)
	{
		List<Long> result = new ArrayList<Long>();
		for(Area c : list)
		{
			if(Objects.nonNull(c.getId())) {result.add(c.getId());}
		}
		return result;
	}
	
	public static Area build(Status status)
	{
		Area type = new Area();
		type.setCode(status.getCode());
		type.setDescriptions(status.getDescriptions());
		type.setLangs(status.getLangs());
		return type;
	}
}