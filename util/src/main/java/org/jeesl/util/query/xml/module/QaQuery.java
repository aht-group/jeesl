package org.jeesl.util.query.xml.module;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.jeesl.factory.xml.dev.qa.XmlGroupFactory;
import org.jeesl.factory.xml.dev.qa.XmlGroupsFactory;
import org.jeesl.factory.xml.dev.qa.XmlResultFactory;
import org.jeesl.factory.xml.system.lang.XmlDescriptionFactory;
import org.jeesl.factory.xml.system.status.XmlStatusFactory;
import org.jeesl.model.xml.system.security.Role;
import org.jeesl.model.xml.system.security.Staff;
import org.jeesl.util.query.xml.system.SecurityQuery;

import net.sf.ahtutils.factory.xml.status.XmlStatementFactory;
import net.sf.ahtutils.xml.aht.Query;
import net.sf.ahtutils.xml.qa.Category;
import net.sf.ahtutils.xml.qa.Comment;
import net.sf.ahtutils.xml.qa.Description;
import net.sf.ahtutils.xml.qa.Expected;
import net.sf.ahtutils.xml.qa.Group;
import net.sf.ahtutils.xml.qa.Groups;
import net.sf.ahtutils.xml.qa.Info;
import net.sf.ahtutils.xml.qa.PreCondition;
import net.sf.ahtutils.xml.qa.Reference;
import net.sf.ahtutils.xml.qa.Result;
import net.sf.ahtutils.xml.qa.Results;
import net.sf.ahtutils.xml.qa.Steps;
import net.sf.ahtutils.xml.qa.Test;
import net.sf.ahtutils.xml.status.Status;
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
		xml.setId(0);
		xml.setRole(SecurityQuery.role());
		xml.setUser(SecurityQuery.user());
		return xml;
	}
	
	public static Result result()
	{
		Role role = new Role();
		role.setCode("");
		
		Staff staff = new Staff();
		staff.setId(0);
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
