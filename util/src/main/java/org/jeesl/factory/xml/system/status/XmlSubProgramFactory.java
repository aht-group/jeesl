package org.jeesl.factory.xml.system.status;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xml.system.io.locale.XmlLabelFactory;
import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.locale.status.SubProgram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlSubProgramFactory<S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription>
{
	final static Logger logger = LoggerFactory.getLogger(XmlSubProgramFactory.class);
		
	private SubProgram q;
	
	private XmlLabelFactory<L> xfLabel;
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescription;
	
	public XmlSubProgramFactory(SubProgram q){this(null,q);}
	public XmlSubProgramFactory(String localeCode, SubProgram q)
	{
		this.q=q;
		
		if(Objects.nonNull(q.getLabel())) {xfLabel = new XmlLabelFactory<>(localeCode);}
		if(Objects.nonNull(q.getLangs())){xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(Objects.nonNull(q.getDescriptions())) {xfDescription = new XmlDescriptionsFactory<D>(q.getDescriptions());}
	}
	
	public SubProgram build(S ejb){return build(ejb,null);}
	public SubProgram build(S ejb, String group)
	{
		SubProgram xml = new SubProgram();
		if(Objects.nonNull(q.getId())) {xml.setId(ejb.getId());}
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(ejb.getPosition());}
		xml.setGroup(group);
		
		if(Objects.nonNull(q.getLabel())) {xml.setLabel(xfLabel.build(ejb));}
		if(Objects.nonNull(q.getLangs())) {xml.setLangs(xfLangs.getUtilsLangs(ejb.getName()));}
		if(Objects.nonNull(q.getDescriptions())) {xml.setDescriptions(xfDescription.create(ejb.getDescription()));}

		return xml;
	}
	
	public static <E extends Enum<E>> SubProgram build(E code){return build(code.toString());}
	public static <E extends Enum<E>> SubProgram build(String code){return build(code.toString(),null);}
	public static SubProgram build(String code,String label)
	{
		SubProgram xml = new SubProgram();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
}