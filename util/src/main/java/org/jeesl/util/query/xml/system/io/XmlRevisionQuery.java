package org.jeesl.util.query.xml.system.io;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.jeesl.factory.xml.system.io.revision.XmlRelationFactory;
import org.jeesl.factory.xml.system.io.revision.XmlRevisionFactory;
import org.jeesl.factory.xml.system.security.XmlUserFactory;
import org.jeesl.factory.xml.system.status.XmlCategoryFactory;
import org.jeesl.factory.xml.system.status.XmlTypeFactory;
import org.jeesl.factory.xml.system.util.text.XmlRemarkFactory;
import org.jeesl.model.xml.io.label.Attribute;
import org.jeesl.model.xml.io.label.Diagram;
import org.jeesl.model.xml.io.label.Entity;
import org.jeesl.model.xml.io.label.Relation;
import org.jeesl.model.xml.io.label.Revision;
import org.jeesl.model.xml.jeesl.QueryRevision;
import org.jeesl.model.xml.system.security.User;
import org.jeesl.util.query.xml.XmlStatusQuery;

import net.sf.exlp.util.DateUtil;


public class XmlRevisionQuery
{
	public static enum Key {xEntity,xDiagram,rRevision}
	
	private static Map<Key,QueryRevision> mQueries;
	
	public static QueryRevision get(Key key){return get(key,null);}
	public static QueryRevision get(Key key,String localeCode)
	{
		if(mQueries==null){mQueries = new Hashtable<Key,QueryRevision>();}
		if(!mQueries.containsKey(key))
		{
			QueryRevision q = new QueryRevision();
			switch(key)
			{
				case xEntity: q.setEntity(xEntity());break;
				case xDiagram: q.setDiagram(xDiagram());break;
				case rRevision: q.setRevision(rRevision());break;
			}
			mQueries.put(key, q);
		}
		QueryRevision q = mQueries.get(key);
		q.setLocaleCode(localeCode);
		return q;
	}
	
	private static Entity xEntity()
	{	
		Entity xml = new Entity();
		xml.setId(0l);
		xml.setCode("");
		xml.setPosition(0);
		xml.setVisible(true);
		xml.setTimeseries(true);
		xml.setDocumentation(true);
		xml.setCategory(XmlCategoryFactory.create(""));
		xml.setLangs(XmlStatusQuery.langs());
		xml.setDescriptions(XmlStatusQuery.descriptions());
		xml.setRemark(XmlRemarkFactory.build(""));
		xml.getAttribute().add(xAttribute());
		
		Diagram diagram = new Diagram();
		diagram.setId(0l);
		diagram.setCode("");
		xml.setDiagram(diagram);
		
		return xml;
	}
	
	private static Attribute xAttribute()
	{		
		Attribute xml = new Attribute();
		xml.setCode("");
		xml.setPosition(0);
		xml.setXpath("");
		
		xml.setWeb(true);
		xml.setPrint(true);
		xml.setName(true);
		xml.setEnclosure(true);
		xml.setUi(true);
		xml.setBean(true);
		xml.setConstruction(true);
		
		xml.setType(XmlTypeFactory.create(""));
		
		xml.setLangs(XmlStatusQuery.langs());
		xml.setDescriptions(XmlStatusQuery.descriptions());
		xml.setRemark(XmlRemarkFactory.build(""));
		
		Relation relation = XmlRelationFactory.build();
		relation.setOwner(true);
		relation.setDocOptionsTable(true);
		relation.setDocOptionsInline(true);
		relation.setEntity(exRelEntity());		
		relation.setType(XmlTypeFactory.create(""));
		xml.setRelation(relation);

		return xml;
	}
	
	public static Entity exRelEntity()
	{		
		Entity xml = new Entity();
		
		xml.setId(0l);
		xml.setCode("");
	
		return xml;
	}
	
	public static Diagram xDiagram()
	{		
		Diagram xml = new Diagram();
		xml.setId(0l);
		xml.setCode("");
		xml.setPosition(0);
		xml.setDocumentation(true);
		xml.setCategory(XmlCategoryFactory.create(""));
		xml.setLangs(XmlStatusQuery.langs());
		xml.setDescriptions(XmlStatusQuery.descriptions());
		return xml;
	}
	
	private static Revision rRevision()
	{
		User user = XmlUserFactory.build("","","");
		user.setId(0l);
		user.setName("");
		
		Revision xml = XmlRevisionFactory.build();
		xml.setRecord(DateUtil.toXmlGc(new Date()));
		xml.setUser(user);
		return xml;
	}
}