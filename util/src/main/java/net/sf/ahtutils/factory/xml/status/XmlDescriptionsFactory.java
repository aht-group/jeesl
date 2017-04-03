package net.sf.ahtutils.factory.xml.status;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import org.jeesl.util.comparator.xml.status.DescriptionComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.xml.status.Description;
import net.sf.ahtutils.xml.status.Descriptions;

public class XmlDescriptionsFactory<D extends UtilsDescription>
{
	final static Logger logger = LoggerFactory.getLogger(XmlDescriptionsFactory.class);
		
	private static Comparator<Description> comparator = DescriptionComparator.factory(DescriptionComparator.Type.key);;
	
	private Descriptions q;
	
	public XmlDescriptionsFactory(Descriptions q)
	{
		this.q=q;
	}	
	
	public Descriptions create(Map<String,D> mapEjb)
	{
		Descriptions xml = new Descriptions();
		
		if(q.isSetDescription() && mapEjb!=null)
		{
			XmlDescriptionFactory<D> f = new XmlDescriptionFactory<D>(q.getDescription().get(0));
			for(D ejb : mapEjb.values())
			{
				xml.getDescription().add(f.create(ejb));
			}
		}
		Collections.sort(xml.getDescription(), comparator);
		return xml;
	}
	
	public static Descriptions build()
	{
		return new Descriptions();
	}
	
	public static Descriptions build(Description description)
	{
		Descriptions xml = build();
		xml.getDescription().add(description);
		return xml;
	}
	
	public static Descriptions build(String[] langs)
	{
		Descriptions xml = build();
		for(String lang : langs)
		{
			xml.getDescription().add(XmlDescriptionFactory.create(lang,""));
		}
		return xml;
	}
}