package net.sf.ahtutils.controller.factory.xml.acl;

import java.util.List;

import net.sf.ahtutils.model.interfaces.acl.UtilsAclCategoryGroup;
import net.sf.ahtutils.model.interfaces.acl.UtilsAclCategoryUsecase;
import net.sf.ahtutils.model.interfaces.acl.UtilsAclGroup;
import net.sf.ahtutils.model.interfaces.acl.UtilsAclView;
import net.sf.ahtutils.xml.access.Groups;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlGroupsFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlGroupsFactory.class);
		
	private Groups qRoles;
	private String lang;
	
	public XmlGroupsFactory(Groups qRoles, String lang)
	{
		this.qRoles=qRoles;
		this.lang=lang;
	}
	
	public <L extends JeeslLang,D extends JeeslDescription,CU extends UtilsAclCategoryUsecase<L,D,CU,U>,CR extends UtilsAclCategoryGroup<L,D,CU,CR,U,R>,U extends UtilsAclView<L,D,CU,U>,R extends UtilsAclGroup<L,D,CU,CR,U,R>>
		Groups getRoles(List<R> lRoles)
	{
		Groups roles = new Groups();
		
		if(qRoles.isSetGroup())
		{
			XmlGroupFactory f = new XmlGroupFactory(qRoles.getGroup().get(0),lang);
			for(R aclRole : lRoles)
			{
				roles.getGroup().add(f.getRole(aclRole));
			}
		}
		return roles;
	}
}