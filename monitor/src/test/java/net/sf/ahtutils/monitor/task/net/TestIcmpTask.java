package net.sf.ahtutils.monitor.task.net;

import org.jeesl.controller.monitoring.result.net.IcmpResult;
import org.jeesl.controller.monitoring.result.net.IcmpResults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.UtilsMonitorTestBootstrap;

public class TestIcmpTask
{
	final static Logger logger = LoggerFactory.getLogger(TestIcmpTask.class);
	
	@Test
	public void test() throws Exception
	{
		IcmpTask task = new IcmpTask("localhost");
		IcmpResults results = task.call();
		for(IcmpResult result : results.getList())
		{
			Assertions.assertEquals(IcmpResult.Code.REACHABLE, result.getCode());
		}
		
	}
	
	public static void main(String args[]) throws Exception
	{
		UtilsMonitorTestBootstrap.init();
		
		IcmpTask task = new IcmpTask("192.168.1.11");
		IcmpResults results = task.call();
		for(IcmpResult result : results.getList())
		{
			logger.info(result.toString());
		}
	}
}