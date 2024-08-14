package org.jeesl.util.query.xml;

import java.util.Hashtable;
import java.util.Map;

import org.jeesl.model.xml.io.locale.status.Category;
import org.jeesl.model.xml.io.locale.status.Condition;
import org.jeesl.model.xml.io.locale.status.Description;
import org.jeesl.model.xml.io.locale.status.Descriptions;
import org.jeesl.model.xml.io.locale.status.Lang;
import org.jeesl.model.xml.io.locale.status.Langs;
import org.jeesl.model.xml.io.locale.status.Level;
import org.jeesl.model.xml.io.locale.status.Model;
import org.jeesl.model.xml.io.locale.status.Parent;
import org.jeesl.model.xml.io.locale.status.Quarter;
import org.jeesl.model.xml.io.locale.status.Reason;
import org.jeesl.model.xml.io.locale.status.Result;
import org.jeesl.model.xml.io.locale.status.Scope;
import org.jeesl.model.xml.io.locale.status.Scopes;
import org.jeesl.model.xml.io.locale.status.Source;
import org.jeesl.model.xml.io.locale.status.Status;
import org.jeesl.model.xml.io.locale.status.SubType;
import org.jeesl.model.xml.io.locale.status.Type;
import org.jeesl.model.xml.io.locale.status.Verification;

import net.sf.ahtutils.xml.aht.Query;

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
		parent.setId(0l);
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
		xml.setId(0l);
		xml.setCode("");
		xml.setLabel("");
		return xml;
	}
	
	public static Type typeLabel()
	{		
		Type xml = new Type();
		xml.setId(0l);
		xml.setCode("");
		xml.setLabel("");
		return xml;
	}
	
	public static Quarter quarterLabel()
	{		
		Quarter xml = new Quarter();
		xml.setId(0l);
		xml.setCode("");
		xml.setLabel("");
		return xml;
	}
	
	public static Level levelLabel()
	{		
		Level xml = new Level();
		xml.setId(0l);
		xml.setCode("");
		xml.setLabel("");
		return xml;
	}
	
	public static Result resultLabel()
	{		
		Result xml = new Result();
		xml.setId(0l);
		xml.setCode("");
		xml.setLabel("");
		return xml;
	}
	
	public static SubType subTypeLabel()
	{		
		SubType xml = new SubType();
		xml.setId(0l);
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
	
	public static Source sourceLabel()
	{		
		Source xml = new Source();
		xml.setId(0l);
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
