package org.jeesl.client.test.processor;

import java.util.ArrayList;
import java.util.List;

import net.sf.ahtutils.controller.processor.ValidFromProcessor;
import net.sf.ahtutils.test.AbstractJeeslTest;

import org.jeesl.client.model.ejb.ValidFrom;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestValidFromProcessor extends AbstractJeeslTest
{
	final static Logger logger = LoggerFactory.getLogger(TestValidFromProcessor.class);
	
	private List<ValidFrom> list;
	
	@BeforeEach
	public void init()
	{
		list = new ArrayList<ValidFrom>();
		
		DateTime dt1 = new DateTime(2012, 2, 29, 11, 52, 0);
		DateTime dt2 = new DateTime(2009, 12, 15, 0, 0, 0);
		DateTime dt3 = new DateTime(2009, 11, 15, 0, 0, 0);
		
		ValidFrom v1 = new ValidFrom();v1.setId(1);v1.setValidFrom(dt1.toDate());list.add(v1);
		ValidFrom v2 = new ValidFrom();v2.setId(2);v2.setValidFrom(dt2.toDate());list.add(v2);
		ValidFrom v3 = new ValidFrom();v3.setId(3);v3.setValidFrom(dt3.toDate());list.add(v3);
	}
 
    @Test
    public void testSimple()
    {	
    	ValidFromProcessor<ValidFrom> vfp = new ValidFromProcessor<ValidFrom>(list);
    	
    	DateTime dt = new DateTime(2012, 3, 1, 0, 0, 0);
    	
    	List<ValidFrom> result = vfp.getValid(dt.toDate());
    	Assertions.assertEquals(1, result.size());
    	Assertions.assertEquals(1, result.get(0).getId());
    }
}