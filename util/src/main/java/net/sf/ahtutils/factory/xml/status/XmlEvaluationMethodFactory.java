package net.sf.ahtutils.factory.xml.status;

import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.EvaluationMethod;
import net.sf.ahtutils.xml.status.Status;

public class XmlEvaluationMethodFactory <S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription>
{
	final static Logger logger = LoggerFactory.getLogger(XmlEvaluationMethodFactory.class);
		
	private EvaluationMethod q;
	
	public XmlEvaluationMethodFactory(EvaluationMethod q)
	{
		this.q=q;
	}
	
	public EvaluationMethod build(S ejb){return build(ejb,null);}
	public EvaluationMethod build(S ejb, String group)
	{
		EvaluationMethod xml = new EvaluationMethod();
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(ejb.getPosition());}
		xml.setGroup(group);
		
		if(Objects.nonNull(q.getLangs()))
		{
			XmlLangsFactory<L> f = new XmlLangsFactory<L>(q.getLangs());
			xml.setLangs(f.getUtilsLangs(ejb.getName()));
		}
		if(Objects.nonNull(q.getDescriptions()))
		{

		}
		
		return xml;
	}
	
	public static EvaluationMethod build(String code)
	{
		EvaluationMethod xml = new EvaluationMethod();
		xml.setCode(code);
		return xml;
	}
	
	public static EvaluationMethod build(Status status)
	{
		EvaluationMethod xml = new EvaluationMethod();
		xml.setCode(status.getCode());
		xml.setDescriptions(status.getDescriptions());
		xml.setLangs(status.getLangs());
		return xml;
	}
}