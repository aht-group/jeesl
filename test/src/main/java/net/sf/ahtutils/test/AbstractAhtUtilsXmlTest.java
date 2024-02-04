package net.sf.ahtutils.test;

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

public class AbstractAhtUtilsXmlTest <T extends Object>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractAhtUtilsXmlTest.class);

	private boolean debug;
	protected static File fXml;
	
	private File xmlFile;
	
	private Class<T> cXml;
	
	public AbstractAhtUtilsXmlTest(){this(null,null);}
	public AbstractAhtUtilsXmlTest(Class<T> cXml,String xmlDirSuffix)
	{
		debug=true;
		this.cXml=cXml;
		if(cXml!=null)
		{
			try
			{
				T t = cXml.newInstance();
				xmlFile = new File(getXmlDir(xmlDirSuffix),t.getClass().getSimpleName()+".xml");
			}
			catch (InstantiationException e) {e.printStackTrace();}
			catch (IllegalAccessException e) {e.printStackTrace();}
		}
	}
	
	protected void assertJaxbEquals(Object expected, Object actual)
	{
		Assert.assertEquals("Actual XML differes from expected XML",JaxbUtil.toString(expected),JaxbUtil.toString(actual));
	}
	
	protected static XMLGregorianCalendar getDefaultXmlDate()
	{
		LocalDateTime ldt = LocalDateTime.of(2011,11,11,11,11,11);
		return DateUtil.toXmlGc(ldt);
	}
	
	public void saveReferenceXml() {save(build(true),xmlFile,false);}
	protected void save(Object xml, File f){save(xml,f,true);}
	protected void save(Object xml, File f, boolean formatted)
	{
		logger.debug("Saving Reference XML to "+f.getAbsolutePath());
		if(debug){JaxbUtil.info(xml);}
    	JaxbUtil.save(f, xml, true);
	}
	
	protected static void setXmlFile(String dirSuffix, Class<?> cl)
	{
		setXmlFile(dirSuffix,cl.getSimpleName());
	}
	
	protected static void setXmlFile(String dirSuffix, String namePrefix)
	{
		fXml = new File(getXmlDir(dirSuffix),namePrefix+".xml");
	}
	
	protected static File getXmlDir(String suffix)
    {
        File f = new File(".");
        String s = FilenameUtils.normalizeNoEndSeparator(f.getAbsolutePath());

        // This hack is for intelliJ
 //       if(!s.endsWith("xml")){f = new File(s,"xml");}
//        else {f = new File(s);}

        f = new File(s);
        return new File(f,"src"+File.separator+"test"+File.separator+"resources"+File.separator+"data"+File.separator+"xml"+File.separator+suffix);
    }
	
    @Test
    public void xml() throws FileNotFoundException
    {
    	//TODO remove !=null
    	if(cXml!=null)
		{
    		T actual = build(true);
        	T expected = JaxbUtil.loadJAXB(xmlFile.getAbsolutePath(), cXml);
        	assertJaxbEquals(expected, actual);
		}
    }
    
    //TODO declare as abstract
    protected T build(boolean withChilds){return null;}
}