package org.jeesl.util.query.xml;

import java.util.Hashtable;
import java.util.Map;

import net.sf.ahtutils.xml.aht.Query;
import net.sf.ahtutils.xml.status.Category;
import net.sf.ahtutils.xml.status.Condition;
import net.sf.ahtutils.xml.status.Description;
import net.sf.ahtutils.xml.status.Descriptions;
import net.sf.ahtutils.xml.status.Lang;
import net.sf.ahtutils.xml.status.Langs;
import net.sf.ahtutils.xml.status.Level;
import net.sf.ahtutils.xml.status.Model;
import net.sf.ahtutils.xml.status.Parent;
import net.sf.ahtutils.xml.status.Reason;
import net.sf.ahtutils.xml.status.Result;
import net.sf.ahtutils.xml.status.Scope;
import net.sf.ahtutils.xml.status.Scopes;
import net.sf.ahtutils.xml.status.Status;
import net.sf.ahtutils.xml.status.SubType;
import net.sf.ahtutils.xml.status.Type;
import net.sf.ahtutils.xml.status.Verification;

public class XmlStatusQuery
{
	public static enum Key {StatusExport,Langs,extractType,statusLabel,typeLabel,categoryLabel,modelLabel}
	
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
				case StatusExport: q.setStatus(statusExport());break;
				case Langs: q.setLangs(langs());break;
				case extractType: q.setType(extractType());break;
				case statusLabel: q.setStatus(statusLabel());break;
				case typeLabel: q.setType(typeLabel());break;
				case categoryLabel: q.setType(typeLabel());break;
				case modelLabel: q.setModel(modelLabel());break;
			}
			mQueries.put(key, q);
		}
		Query q = mQueries.get(key);
		q.setLang(lang);
		return q;
	}
	
	public static Status statusExport()
	{
		Parent parent = new Parent();
		parent.setId(0);
		parent.setCode("");
		parent.setPosition(0);
				
		Status xml = new Status();
		xml.setCode("");
		xml.setImage("");
		xml.setStyle("");
		xml.setPosition(0);
		xml.setSymbol("");
		xml.setVisible(true);
		xml.setLangs(langs());
		xml.setDescriptions(descriptions());
		xml.setParent(parent);
		
		return xml;
	}
	
	public static Status statusLabel()
	{		
		Status xml = new Status();
		xml.setId(0);
		xml.setCode("");
		xml.setLabel("");
		return xml;
	}
	
	public static Type typeLabel()
	{		
		Type xml = new Type();
		xml.setId(0);
		xml.setCode("");
		xml.setLabel("");
		return xml;
	}
	
	public static Level levelLabel()
	{		
		Level xml = new Level();
		xml.setId(0);
		xml.setCode("");
		xml.setLabel("");
		return xml;
	}
	
	public static Result resultLabel()
	{		
		Result xml = new Result();
		xml.setId(0);
		xml.setCode("");
		xml.setLabel("");
		return xml;
	}
	
	public static SubType subTypeLabel()
	{		
		SubType xml = new SubType();
		xml.setId(0);
		xml.setCode("");
		xml.setLabel("");
		return xml;
	}
	
	public static Category categoryLabel()
	{		
		Category xml = new Category();
//		xml.setId(0);
		xml.setCode("");
		xml.setLabel("");
		return xml;
	}
	
	public static Condition conditionLabel()
	{		
		Condition xml = new Condition();
//		xml.setId(0);
		xml.setCode("");
		xml.setLabel("");
		return xml;
	}
	
	private static Model modelLabel()
	{		
		Model xml = new Model();
		xml.setCode("");
		xml.setLabel("");
		return xml;
	}
	
	public static Reason reasonLabel()
	{		
		Reason xml = new Reason();
		xml.setCode("");
		xml.setLabel("");
		return xml;
	}
	
	public static Verification verificationLabel()
	{		
		Verification xml = new Verification();
		xml.setCode("");
		xml.setLabel("");
		return xml;
	}
	
	public static Scopes scopes()
	{
		Scope scope = new Scope();
		scope.setCode("");
		scope.setLabel("");
		
		Scopes scopes = new Scopes();
		scopes.getScope().add(scope);
		return scopes;
	}
	
	public static Descriptions descriptions()
	{
		Description d = new Description();
		d.setKey("");
		d.setValue("");
		
		Descriptions xml = new Descriptions();
		xml.getDescription().add(d);
    	return xml;
	}
	
	public static Langs langs()
	{
		Lang l = new Lang();
		l.setKey("");
		l.setTranslation("");
		
		Langs xml = new Langs();
		xml.getLang().add(l);
    	return xml;
	}
	
	public static Type extractType()
	{
		Type xml = new Type();
		xml.setCode("");
		xml.setLangs(langs());
		return xml;
	}
}
