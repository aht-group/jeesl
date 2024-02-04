package org.jeesl.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.io.FilenameUtils;
import org.exlp.util.jx.JaxbUtil;
import org.exlp.util.system.DateUtil;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractJeeslXmlTest<T extends Object>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractJeeslXmlTest.class);

	private boolean debug;
	protected static File fXml;
	
	private File xmlFile;
	
	private Class<T> cXml;
	
	public AbstractJeeslXmlTest(){this(null,null);}
	public AbstractJeeslXmlTest(Class<T> cXml,String xmlDirSuffix){this(cXml,"data"+File.separator+"xml",xmlDirSuffix);}
	public AbstractJeeslXmlTest(Class<T> cXml,String xmlDirPrefix, String xmlDirSuffix)
	{
		debug=true;
		this.cXml=cXml;
		if(cXml!=null)
		{
			try
			{
				T t = cXml.newInstance();
				xmlFile = new File(getXmlDir(xmlDirPrefix,xmlDirSuffix),t.getClass().getSimpleName()+".xml");
			}
			catch (InstantiationException e) {e.printStackTrace();}
			catch (IllegalAccessException e) {e.printStackTrace();}
		}
	}
	
	private File getXmlDir(String prefix, String suffix)
    {
        File f = new File(".");
        String s = FilenameUtils.normalizeNoEndSeparator(f.getAbsolutePath());

        f = new File(s);
        return new File(f,"src"+File.separator+"test"+File.separator+"resources"+File.separator+prefix+File.separator+suffix);
    }
	
	protected static XMLGregorianCalendar getDefaultXmlDate()
	{
		LocalDateTime ldt = LocalDateTime.of(2011,11,11,11,11,11);
		return DateUtil.toXmlGc(ldt);
	}
	
	protected void saveReferenceXml()
	{
		Object xml = build(true);
		logger.debug("Saving Reference XML");
		if(debug){JaxbUtil.info(xml);}
    	JaxbUtil.save(xmlFile,xml,true);
	}
	
    @Test
    public void xml() throws FileNotFoundException
    {
    	T actual = build(true);
    	T expected = JaxbUtil.loadJAXB(xmlFile.getAbsolutePath(), cXml);
    	Assert.assertEquals("Actual XML differes from expected XML",JaxbUtil.toString(expected),JaxbUtil.toString(actual));
    }
    
    //TODO declare as abstract
    protected T build(boolean withChilds){return null;}
}