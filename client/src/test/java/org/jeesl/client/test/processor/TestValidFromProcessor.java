package org.jeesl.client.test.processor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.exlp.util.system.DateUtil;
import org.jeesl.client.model.ejb.ValidFrom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.controller.processor.ValidFromProcessor;
import net.sf.ahtutils.test.AbstractJeeslTest;

public class TestValidFromProcessor extends AbstractJeeslTest
{
	final static Logger logger = LoggerFactory.getLogger(TestValidFromProcessor.class);
	
	private List<ValidFrom> list;
	
	@BeforeEach
	public void init()
	{
		list = new ArrayList<ValidFrom>();
		
		LocalDateTime ldt1 = LocalDateTime.of(2012, 2, 29, 11, 52, 0);
		LocalDateTime ldt2 = LocalDateTime.of(2009, 12, 15, 0, 0, 0);
		LocalDateTime ldt3 = LocalDateTime.of(2009, 11, 15, 0, 0, 0);
		
		ValidFrom v1 = new ValidFrom(); v1.setId(1); v1.setValidFrom(DateUtil.toDate(ldt1)); list.add(v1);
		ValidFrom v2 = new ValidFrom(); v2.setId(2); v2.setValidFrom(DateUtil.toDate(ldt2)); list.add(v2);
		ValidFrom v3 = new ValidFrom(); v3.setId(3); v3.setValidFrom(DateUtil.toDate(ldt3)); list.add(v3);
	}
 
    @Test
    public void testSimple()
    {	
    	ValidFromProcessor<ValidFrom> vfp = new ValidFromProcessor<ValidFrom>(list);
    	
    	LocalDateTime ldt = LocalDateTime.of(2012, 3, 1, 0, 0, 0);
    	
    	List<ValidFrom> result = vfp.getValid(DateUtil.toDate(ldt));
    	Assertions.assertEquals(1, result.size());
    	Assertions.assertEquals(1, result.get(0).getId());
    }
}