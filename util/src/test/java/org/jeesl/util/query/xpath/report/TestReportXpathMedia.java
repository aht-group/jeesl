package org.jeesl.util.query.xpath.report;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.AbstractJeeslUtilTest;
import org.jeesl.model.xml.io.report.Media;
import org.jeesl.model.xml.system.io.report.TestXmlMedia;
import org.jeesl.util.query.xpath.ReportXpath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;
import net.sf.exlp.util.xml.JaxbUtil;

public class TestReportXpathMedia extends AbstractJeeslUtilTest
{
	final static Logger logger = LoggerFactory.getLogger(TestReportXpathMedia.class);
    
	private Media media1,media2,media3,media4;
	private List<Media> mediaList;
	
	@Before
	public void iniMedia()
	{
		media1 = TestXmlMedia.create(false);media1.setType("t1");
    	media1.setDir("d1");
    	
    	media2 = TestXmlMedia.create(false);media2.setType("t2");

    	media3 = TestXmlMedia.create(false);media3.setType("t3");
    	media4 = TestXmlMedia.create(false);media4.setType("t3");
    	
    	mediaList = new ArrayList<Media>();
    	mediaList.add(media1);
    	mediaList.add(media2);
    	mediaList.add(media3);
    	mediaList.add(media4);
	}
	
	@Test
	public void testType1() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		Media test = ReportXpath.getMedia(mediaList, media1.getType());
		Assert.assertEquals(JaxbUtil.toString(media1),JaxbUtil.toString(test));
	}
	    
	@Test
	public void testType2() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		Media test = ReportXpath.getMedia(mediaList, media2.getType());
		Assert.assertEquals(JaxbUtil.toString(media2),JaxbUtil.toString(test));
	}

	@Test(expected=ExlpXpathNotFoundException.class)
	public void testNotFound() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		ReportXpath.getMedia(mediaList, "nullCode");
	}
	    
	    @Test(expected=ExlpXpathNotUniqueException.class)
	    public void testUnique() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	    {
	    	ReportXpath.getMedia(mediaList, media3.getType());
	    }
}