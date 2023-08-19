package org.jeesl.factory.xml.module.workflow;

import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.factory.xml.system.security.XmlRoleFactory;
import org.jeesl.factory.xml.system.status.XmlTypeFactory;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowModificationLevel;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowPermissionType;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowStage;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowStagePermission;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.model.xml.module.workflow.Permission;
import org.jeesl.model.xml.system.security.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.factory.xml.status.XmlLevelFactory;

public class XmlPermissionFactory<L extends JeeslLang, D extends JeeslDescription,
								WS extends JeeslWorkflowStage<L,D,?,?,WSP,?,?>,
								WSP extends JeeslWorkflowStagePermission<WS,WPT,WML,SR>,
								WPT extends JeeslWorkflowPermissionType<L,D,WPT,?>,
								WML extends JeeslWorkflowModificationLevel<L,D,WML,?>,
								SR extends JeeslSecurityRole<L,D,?,?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlPermissionFactory.class);
	
	private final Permission q;
	
	private XmlTypeFactory<L,D,WPT> xfType;
	private XmlLevelFactory<L,D,WML> xfLevel;
	private XmlLangsFactory<L> xfLangs;
	
//	public XmlPermissionFactory(QueryWf query) {this(query.getLocaleCode(),query.getStage());}
	public XmlPermissionFactory(String localeCode, Permission q)
	{
		this.q=q;
		if(q.isSetType()) {xfType = new XmlTypeFactory<>(localeCode,q.getType());}
		if(q.isSetLevel()) {xfLevel = new XmlLevelFactory<>(localeCode,q.getLevel());}
		
		if(q.isSetRole() && q.getRole().isSetLangs()) {xfLangs = new XmlLangsFactory<>(q.getRole().getLangs());}
//		if(q.isSetDescriptions()) {xfDescription = new XmlDescriptionsFactory<>(q.getDescriptions());}
	}
	
	public static Permission build(){return new Permission();}
	
	public Permission build(WSP permission)
	{
		Permission xml = build();
		if(q.isSetId()) {xml.setId(permission.getId());}
		if(q.isSetPosition()) {xml.setPosition(permission.getPosition());}
		if(q.isSetType()) {xml.setType(xfType.build(permission.getType()));}
		if(q.isSetLevel()) {xml.setLevel(xfLevel.build(permission.getModificationLevel()));}
		if(q.isSetRole())
		{
			Role role = XmlRoleFactory.build(permission.getRole().getCode());
			if(q.getRole().isSetLangs()) {role.setLangs(xfLangs.getUtilsLangs(permission.getRole().getName()));}
			xml.setRole(role);
		}		
		return xml;
	}
}