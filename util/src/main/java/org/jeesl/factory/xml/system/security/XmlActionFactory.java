package org.jeesl.factory.xml.system.security;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.security.Action;
import net.sf.ahtutils.xml.status.Descriptions;
import net.sf.ahtutils.xml.status.Langs;

public class XmlActionFactory <L extends JeeslLang, D extends JeeslDescription, 
								C extends JeeslSecurityCategory<L,D>,
								R extends JeeslSecurityRole<L,D,C,V,U,A,?>,
								V extends JeeslSecurityView<L,D,C,R,U,A>,
								U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
								A extends JeeslSecurityAction<L,D,R,V,U,AT>,
								AT extends JeeslSecurityTemplate<L,D,C>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlActionFactory.class);
		
	private net.sf.ahtutils.xml.security.Action q;
	private net.sf.ahtutils.xml.access.Action qAcl;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescription;
	private XmlViewFactory<L,D,C,R,V> xfView;
	private XmlTemplateFactory<L,D,C,AT> xfTemplate;
	
	public XmlActionFactory(net.sf.ahtutils.xml.security.Action q)
	{
		this.q=q;
		if(q.isSetLangs()) {xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(q.isSetDescriptions()) {xfDescription = new XmlDescriptionsFactory<D>(q.getDescriptions());}
		if(q.isSetView()) {xfView = new XmlViewFactory<>(q.getView());}
		if(q.isSetTemplate()){xfTemplate= new XmlTemplateFactory<>(q.getTemplate());}
	}
	public XmlActionFactory(net.sf.ahtutils.xml.access.Action qAcl)
	{
		this.qAcl=qAcl;
		if(qAcl.isSetTemplate()){xfTemplate = new XmlTemplateFactory<>(qAcl.getTemplate());}
	}

	public net.sf.ahtutils.xml.security.Action build(A action)
	{
		Action xml = new Action();
		if(q.isSetCode()){xml.setCode(action.getCode());}
		if(q.isSetPosition()){xml.setPosition(action.getPosition());}
		if(q.isSetVisible()){xml.setVisible(action.isVisible());}
		
		if(q.isSetLangs()) {xml.setLangs(xfLangs.getUtilsLangs(action.getName()));}
		if(q.isSetDescriptions()) {xml.setDescriptions(xfDescription.create(action.getDescription()));}
		if(q.isSetView()) {xml.setView(xfView.build(action.getView()));}
		
		if(q.isSetTemplate() && action.getTemplate()!=null) {xml.setTemplate(xfTemplate.build(action.getTemplate()));}
		
		return xml;
	}
	
	public net.sf.ahtutils.xml.access.Action create(A action)
	{
		net.sf.ahtutils.xml.access.Action xml = new net.sf.ahtutils.xml.access.Action();
		if(qAcl.isSetCode()){xml.setCode(action.getCode());}
		
		boolean processTemplate = action.getTemplate()!=null;
		
		if(qAcl.isSetLangs() && !processTemplate)
		{
			XmlLangsFactory<L> f = new XmlLangsFactory<L>(qAcl.getLangs());
			xml.setLangs(f.getUtilsLangs(action.getName()));
		}
		
		if(qAcl.isSetDescriptions() && !processTemplate)
		{
			XmlDescriptionsFactory<D> f = new XmlDescriptionsFactory<D>(qAcl.getDescriptions());
			xml.setDescriptions(f.create(action.getDescription()));
		}
		
		if(qAcl.isSetTemplate() && processTemplate) {xml.setTemplate(xfTemplate.build(action.getTemplate()));}
		
		return xml;
	}
	
	public static net.sf.ahtutils.xml.security.Action build(String code)
	{
		net.sf.ahtutils.xml.security.Action xml = new net.sf.ahtutils.xml.security.Action();
		xml.setCode(code);
		return xml;
	}
	
	public static Langs toLangs(net.sf.ahtutils.xml.access.Action action)
	{		
		if(action.getTemplate()==null) {return action.getLangs();}
		else {return action.getTemplate().getLangs();}
	}	
	public static Langs toLangs(net.sf.ahtutils.xml.security.Action action)
	{		
		if(action.getTemplate()==null) {return action.getLangs();}
		else {return action.getTemplate().getLangs();}
	}
	
	public static Descriptions toDescriptions(net.sf.ahtutils.xml.access.Action action)
	{		
		if(action.getTemplate()==null) {return action.getDescriptions();}
		else {return action.getTemplate().getDescriptions();}
	}
	public static Descriptions toDescriptions(net.sf.ahtutils.xml.security.Action action)
	{		
		if(action.getTemplate()==null) {return action.getDescriptions();}
		else {return action.getTemplate().getDescriptions();}
	}
}