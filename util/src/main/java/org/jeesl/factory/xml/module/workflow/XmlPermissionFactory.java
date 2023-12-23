package org.jeesl.factory.xml.module.workflow;

import java.util.Objects;

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
								SR extends JeeslSecurityRole<L,D,?,?,?,?>>
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
		if(Objects.nonNull(q.getType())) {xfType = new XmlTypeFactory<>(localeCode,q.getType());}
		if(Objects.nonNull(q.getLevel())) {xfLevel = new XmlLevelFactory<>(localeCode,q.getLevel());}
		
		if(Objects.nonNull(q.getRole()) && Objects.nonNull(q.getRole().getLangs())) {xfLangs = new XmlLangsFactory<>(q.getRole().getLangs());}
//		if(Objects.nonNull(q.getDescriptions())) {xfDescription = new XmlDescriptionsFactory<>(q.getDescriptions());}
	}
	
	public static Permission build(){return new Permission();}
	
	public Permission build(WSP permission)
	{
		Permission xml = build();
		if(Objects.nonNull(q.getId())) {xml.setId(permission.getId());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(permission.getPosition());}
		if(Objects.nonNull(q.getType())) {xml.setType(xfType.build(permission.getType()));}
		if(Objects.nonNull(q.getLevel())) {xml.setLevel(xfLevel.build(permission.getModificationLevel()));}
		if(Objects.nonNull(q.getRole()))
		{
			Role role = XmlRoleFactory.build(permission.getRole().getCode());
			if(Objects.nonNull(q.getRole().getLangs())) {role.setLangs(xfLangs.getUtilsLangs(permission.getRole().getName()));}
			xml.setRole(role);
		}		
		return xml;
	}
}