package org.jeesl.model.xml.dev.qa;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.qa.Comment;

public class TestXmlComment extends AbstractXmlQaTest<Comment>
{
	final static Logger logger = LoggerFactory.getLogger(net.sf.ahtutils.xml.qa.Test.class);
	
	public TestXmlComment(){super(Comment.class);}
	public static Comment create(boolean withChildren){return (new TestXmlComment()).build(withChildren);}
    
    public Comment build(boolean withChildren)
    {
    	Comment xml = new Comment();
    	xml.setValue("myComment");
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlComment test = new TestXmlComment();
		test.saveReferenceXml();
    }
}