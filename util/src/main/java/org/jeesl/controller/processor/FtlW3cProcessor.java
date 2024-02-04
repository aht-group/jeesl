package org.jeesl.controller.processor;

import org.exlp.util.jx.JaxbUtil;
import org.jdom2.JDOMException;
import org.w3c.dom.Document;

import net.sf.exlp.util.xml.JDomUtil;

public class FtlW3cProcessor
{	
	public static Document jaxb2W3c(Object jaxb)
	{
		org.jdom2.Document jdoc = JaxbUtil.toDocument(jaxb);
		jdoc = JDomUtil.unsetNameSpace(jdoc);
		return JDomUtil.toW3CDocument(jdoc);
	}
	
	public static Document text2W3c(String txt)
	{
		org.jdom2.Document jdoc = null;
		try {
			jdoc = JDomUtil.txtToDoc(txt);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jdoc = JDomUtil.unsetNameSpace(jdoc);
		return JDomUtil.toW3CDocument(jdoc);
	}
}