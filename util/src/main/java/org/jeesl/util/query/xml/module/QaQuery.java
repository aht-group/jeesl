package org.jeesl.util.query.xml.module;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.jeesl.factory.xml.dev.qa.XmlGroupFactory;
import org.jeesl.factory.xml.dev.qa.XmlGroupsFactory;
import org.jeesl.factory.xml.dev.qa.XmlResultFactory;
import org.jeesl.factory.xml.system.lang.XmlDescriptionFactory;
import org.jeesl.factory.xml.system.status.XmlStatusFactory;
import org.jeesl.model.xml.io.locale.status.Status;
import org.jeesl.model.xml.module.dev.qa.Category;
import org.jeesl.model.xml.module.dev.qa.Comment;
import org.jeesl.model.xml.module.dev.qa.Description;
import org.jeesl.model.xml.module.dev.qa.Expected;
import org.jeesl.model.xml.module.dev.qa.Group;
import org.jeesl.model.xml.module.dev.qa.Groups;
import org.jeesl.model.xml.module.dev.qa.Info;
import org.jeesl.model.xml.module.dev.qa.PreCondition;
import org.jeesl.model.xml.module.dev.qa.Reference;
import org.jeesl.model.xml.module.dev.qa.Result;
import org.jeesl.model.xml.module.dev.qa.Results;
import org.jeesl.model.xml.module.dev.qa.Steps;
import org.jeesl.model.xml.module.dev.qa.Test;
import org.jeesl.model.xml.system.security.Role;
import org.jeesl.model.xml.system.security.Staff;
import org.jeesl.util.query.xml.system.SecurityQuery;

import net.sf.ahtutils.factory.xml.status.XmlStatementFactory;
import net.sf.ahtutils.xml.aht.Query;
import net.sf.exlp.util.DateUtil;

public class QaQuery
{
	public static enum Key {staff,exTest}
	
	private static Map<Key,Query> mQueries;
	
	public static Query get(Key key){return get(key,null);}
	public static Query get(Key key,String lang)
	{
		if(mQueries==null){mQueries = new Hashtable<Key,Query>();}
		if(!mQueries.containsKey(key))
		{
			Query q = new Query();
			switch(key)
			{
				case staff: q.setStaff(staff());break;
				case exTest: q.setTest(exTest());break;
			}
			mQueries.put(key, q);
		}
		Query q = mQueries.get(key);
		q.setLang(lang);
		return q;
	}
	
	public static Test exTest()
	{
		Info info = new Info();
		info.setComment(new Comment());
		info.setStatus(XmlStatusFactory.create(""));
		
		Test xml = test();
		xml.setDuration(0);
		xml.setResults(new Results());
		xml.getResults().getResult().add(result());
		xml.setInfo(info);
		xml.setVisible(true);
		
		xml.setGroups(XmlGroupsFactory.build());
		xml.getGroups().getGroup().add(XmlGroupFactory.build(""));
		
		return xml;
	}
	
	public static Staff staff()
	{
		Staff xml = new Staff();
		xml.setId(0l);
		xml.setRole(SecurityQuery.role());
		xml.setUser(SecurityQuery.user());
		return xml;
	}
	
	public static Result result()
	{
		Role role = new Role();
		role.setCode("");
		
		Staff staff = new Staff();
		staff.setId(0l);
		staff.setRole(role);
		staff.setUser(SecurityQuery.user());
		
		Status status = XmlStatusFactory.create("");
		status.setImage("");
		
		Result xml = new Result();
		xml.setId(0);
		xml.setStaff(staff);
		xml.setRecord(DateUtil.toXmlGc(new Date()));
		xml.setStatus(status);
		xml.setActual(XmlResultFactory.buildActual(""));
		xml.setComment(XmlResultFactory.buildComment(""));
		
		return xml;
	}
	
	public static Category category()
	{
		Category xml = new Category();
		xml.setId(0);
		xml.setCode("");
		xml.setName("");
		return xml;
	}
	
	public static Group group()
	{
		Group xml = new Group();
		xml.setId(0);
		xml.setName("");
		xml.setDescription(XmlDescriptionFactory.build(""));
		return xml;
	}
	
	public static Category frDuration()
	{
		Group g = new Group();
		g.setId(0);
		
		Groups groups = new Groups();
		groups.getGroup().add(g);
		
		Test test = new Test();
		test.setCode("");
		test.setName("");
		test.setVisible(true);
		test.setDuration(0);
		test.setGroups(groups);
		
		Category xml = category();
		xml.getTest().add(test);
		return xml;
	}
	
	public static Test test()
	{
		Test xml = new Test();
		xml.setId(0);
		xml.setCode("");
		xml.setName("");
		
		xml.setReference(new Reference());
		xml.setPreCondition(new PreCondition());
		xml.setDescription(new Description());
		xml.setSteps(new Steps());
		xml.setExpected(new Expected());
		
		xml.setStatus(XmlStatusFactory.create(""));
		xml.setStatement(XmlStatementFactory.build(""));
		
		return xml;
	}
}
