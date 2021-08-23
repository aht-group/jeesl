package net.sf.ahtutils.report.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import net.sf.exlp.util.xml.JDomUtil;

import org.apache.commons.lang3.SystemUtils;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;


public class JasperFieldGenerator {
	
	public static void main (String[] args)
	{
		StringBuffer sb   = new StringBuffer();
		try {
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				
				String str = br.readLine();
				
				if ("".equalsIgnoreCase(str)) {
					System.out.println("... creating JDOM from Text.");
					break;
				}
				
				sb.append(str);
				sb.append(SystemUtils.LINE_SEPARATOR);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("----------------- JDOM Content -----------------");
		Document doc = JDomUtil.load(new ByteArrayInputStream(sb.toString().getBytes()));
	//	JDomUtil.debug(doc);
		
		XPathFactory xpfac = XPathFactory.instance();
		for (Namespace ns : doc.getNamespacesIntroduced())
		{
			System.out.println("NS: " +ns.getPrefix() +" -> " +ns.getURI());
		}
		Namespace[] namespace = (Namespace[]) doc.getNamespacesInScope().toArray();
		System.out.println(namespace.toString());
		XPathExpression<Attribute> xp = xpfac.compile("//meis:report/ur:report/title/@text", Filters.attribute(), null, namespace);
		for (Attribute att : xp.evaluate(doc)) {
		  System.out.println("We have id(s) " + att.getValue());
		}
		
	}
	
	

}
