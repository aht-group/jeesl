package org.jeesl.util.query.xpath;

import java.util.List;

import org.apache.commons.jxpath.JXPathContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.access.Access;
import net.sf.ahtutils.xml.access.Views;
import net.sf.ahtutils.xml.security.Roles;
import net.sf.ahtutils.xml.security.Security;
import net.sf.ahtutils.xml.security.Staffs;
import net.sf.ahtutils.xml.security.View;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class SecurityXpath
{
	final static Logger logger = LoggerFactory.getLogger(SecurityXpath.class);

	public static synchronized View getMenuItem(Views views,String code) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(views);

		StringBuffer sb = new StringBuffer();
		sb.append("/view[@code='").append(code).append("']");

		@SuppressWarnings("unchecked")
		List<View> listResult = context.selectNodes(sb.toString());
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+View.class.getSimpleName()+" for code="+code);}
		else if(listResult.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+View.class.getSimpleName()+" for code="+code);}
		return listResult.get(0);
	}

	public static synchronized Roles getRoles(Roles roles,String type) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(roles);

		StringBuffer sb = new StringBuffer();
		sb.append("/roles[@type='").append(type).append("']");

		@SuppressWarnings("unchecked")
		List<Roles> listResult = context.selectNodes(sb.toString());
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+Roles.class.getSimpleName()+" for type="+type);}
		else if(listResult.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+Roles.class.getSimpleName()+" for type="+type);}
		return listResult.get(0);
	}

	public static synchronized View getView(Security security, String code) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(security);

		StringBuffer sb = new StringBuffer();
		sb.append("//view[@code='").append(code).append("']");

		@SuppressWarnings("unchecked")
		List<View> listResult = context.selectNodes(sb.toString());
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+View.class.getSimpleName()+" for code="+code);}
		else if(listResult.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+View.class.getSimpleName()+" for code="+code);}
		return listResult.get(0);
	}

	public static synchronized View getMenuItem(Access access,String code) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(access);

		StringBuffer sb = new StringBuffer();
		sb.append("//view[@code='").append(code).append("']");

		@SuppressWarnings("unchecked")
		List<View> listResult = context.selectNodes(sb.toString());
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+View.class.getSimpleName()+" for code="+code);}
		else if(listResult.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+View.class.getSimpleName()+" for code="+code);}
		return listResult.get(0);
	}

	public static synchronized net.sf.ahtutils.xml.security.Category getCategory(Security security,String code) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(security);

		StringBuffer sb = new StringBuffer();
		sb.append("//category[@code='").append(code).append("']");

		@SuppressWarnings("unchecked")
		List<net.sf.ahtutils.xml.security.Category> listResult = context.selectNodes(sb.toString());
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+net.sf.ahtutils.xml.security.Category.class.getSimpleName()+" for code="+code);}
		else if(listResult.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+net.sf.ahtutils.xml.security.Category.class.getSimpleName()+" for code="+code);}
		return listResult.get(0);
	}
	public static synchronized net.sf.ahtutils.xml.security.Role getRole(Security security,String code) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(security);

		StringBuffer sb = new StringBuffer();
		sb.append("//role[@code='").append(code).append("']");

		@SuppressWarnings("unchecked")
		List<net.sf.ahtutils.xml.security.Role> listResult = context.selectNodes(sb.toString());
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+net.sf.ahtutils.xml.security.Role.class.getSimpleName()+" for code="+code);}
		else if(listResult.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+net.sf.ahtutils.xml.security.Role.class.getSimpleName()+" for code="+code);}
		return listResult.get(0);
	}

	public static Staffs getStaffsForDomain(List<Staffs> staffs, String domainCode) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		StringBuffer sb = new StringBuffer();
		sb.append("/domain[@code='").append(domainCode).append("']/..");

		JXPathContext context = JXPathContext.newContext(staffs);
		@SuppressWarnings("unchecked")
		List<Staffs> listResult = context.selectNodes(sb.toString());
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+Staffs.class.getSimpleName()+" for code="+domainCode);}
		else if(listResult.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+Staffs.class.getSimpleName()+" for code="+domainCode);}
		return listResult.get(0);
	}

	public static synchronized List<net.sf.ahtutils.xml.security.Role> getUseCaseRoles(Security security,String useCaseCode) throws ExlpXpathNotFoundException
	{
		JXPathContext context = JXPathContext.newContext(security);

		StringBuffer sb = new StringBuffer();
		sb.append("//role[usecases/usecase[@code='").append(useCaseCode).append("']]");

		@SuppressWarnings("unchecked")
		List<net.sf.ahtutils.xml.security.Role> listResult = context.selectNodes(sb.toString());
		return listResult;
	}

}