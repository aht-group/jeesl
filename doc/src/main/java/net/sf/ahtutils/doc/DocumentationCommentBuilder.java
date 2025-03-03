package net.sf.ahtutils.doc;

import java.util.List;
import java.util.Map;

import org.jeesl.exception.processing.UtilsConfigurationException;
import org.openfuxml.factory.xml.ofx.content.XmlRawFactory;
import org.openfuxml.model.xml.core.ofx.Comment;
import org.openfuxml.util.OfxCommentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.ofx.status.OfxStatusTableFactory.Code;

public class DocumentationCommentBuilder
{
	final static Logger logger = LoggerFactory.getLogger(DocumentationCommentBuilder.class);
	
	public static void translationKeys(Comment comment, org.exlp.interfaces.system.property.Configuration config, String key) throws UtilsConfigurationException
	{
		configKeyReference(comment, config, key, "Translation Keys are defined in");
	}
	
	public static void configKeyReference(Comment comment, org.exlp.interfaces.system.property.Configuration config, String key, String description) throws UtilsConfigurationException
	{
		if(config.containsKey(key))
		{
			comment.getRaw().add(XmlRawFactory.build(description+": "+config.getString(key)));
		}
		else {throw new UtilsConfigurationException("Cannot find key:"+key+" in config");}
	}
	
	public static void tableHeaders(Comment comment,Map<Code,String> headers)
	{
		int i=1;
		for(Code code : Code.values())
		{
			tableKey(comment,headers.get(code),"Table Header ("+i+")");
			i++;
		}	
	}
	public static void tableHeaders(Comment comment,String[] headerKeys)
	{
		int i=1;
		for(String s : headerKeys)
		{
			tableKey(comment,s,"Table Header ("+i+")");
			i++;
		}	
	}
	public static void tableHeaders(Comment comment,List<String> headerKeys)
	{
		int i=1;
		for(String s : headerKeys)
		{
			tableKey(comment,s,"Table Header ("+i+")");
			i++;
		}	
	}
	
	public static void tableKey(Comment comment, String key){tableKey(comment,key,null);}
	public static void tableKey(Comment comment, String key, String description)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(" - ");
		sb.append(key);
		if(description!=null){sb.append(" (").append(description).append(")");}
		
		comment.getRaw().add(XmlRawFactory.build(sb.toString()));
	}
	
	@Deprecated
	public static void fixedId(Comment comment, String id)
	{
		OfxCommentBuilder.fixedId(comment, id);
	}
	
	@Deprecated
	public static void doNotModify(Comment comment)
	{
		OfxCommentBuilder.doNotModify(comment);
	}
	
	public static void ofxClassifier(Comment comment,String[] classifier)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("The following ofx classifiers are used: ");
		for(String s : classifier)
		{
			sb.append(s).append(", ");
		}
		sb.delete(sb.length()-2, sb.length());

		comment.getRaw().add(XmlRawFactory.build(sb.toString()));
	}
}