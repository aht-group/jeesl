package org.jeesl.util.query.xml;

import java.util.Hashtable;
import java.util.Map;

import org.jeesl.factory.xml.system.status.XmlStyleFactory;
import org.jeesl.factory.xml.system.status.XmlStylesFactory;
import org.jeesl.factory.xml.system.status.XmlTypeFactory;
import org.jeesl.factory.xml.system.symbol.XmlColorFactory;
import org.jeesl.factory.xml.system.symbol.XmlColorsFactory;
import org.jeesl.factory.xml.system.symbol.XmlSizeFactory;
import org.jeesl.factory.xml.system.symbol.XmlSizesFactory;

import net.sf.ahtutils.xml.aht.Query;
import net.sf.ahtutils.xml.symbol.Graphic;
import net.sf.ahtutils.xml.symbol.Symbol;
import net.sf.exlp.xml.io.Data;
import net.sf.exlp.xml.io.File;

public class SymbolQuery
{
	public static enum Key {GraphicExport}
	
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
				case GraphicExport: q.setGraphic(exportGraphic());break;
			}
			mQueries.put(key, q);
		}
		Query q = mQueries.get(key);
		q.setLang(lang);
		return q;
	}
	
	private static Symbol exportSymbol()
	{		
		Symbol xml = new Symbol();
		xml.setColors(XmlColorsFactory.build(XmlColorFactory.build("","")));
		xml.setStyles(XmlStylesFactory.build(XmlStyleFactory.build("")));
		xml.setSizes(XmlSizesFactory.build(XmlSizeFactory.build("", 0)));
		return xml;
	}
	
	private static Graphic exportGraphic()
	{		
		File f = new File();
		f.setData(new Data());
		
		Graphic g = new Graphic();
		g.setId(0);
		
		g.setType(XmlTypeFactory.create(""));
		g.setFile(f);
		g.setSymbol(exportSymbol());
		
		return g;
	}	
}