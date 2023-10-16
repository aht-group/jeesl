package net.sf.ahtutils.monitor.task.net;

import org.jeesl.controller.monitoring.result.net.DnsResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.Lookup;

import net.sf.ahtutils.test.UtilsMonitorTestBootstrap;

public class TestDnsTask
{
	final static Logger logger = LoggerFactory.getLogger(TestDnsTask.class);
	
	@Test
	public void testEnum()
	{
		Assertions.assertEquals(Lookup.SUCCESSFUL, DnsResult.Code.SUCCESSFUL.ordinal());
		Assertions.assertEquals(Lookup.UNRECOVERABLE, DnsResult.Code.UNRECOVERABLE.ordinal());
		Assertions.assertEquals(Lookup.TRY_AGAIN, DnsResult.Code.TRY_AGAIN.ordinal());
		Assertions.assertEquals(Lookup.HOST_NOT_FOUND, DnsResult.Code.HOST_NOT_FOUND.ordinal());
		Assertions.assertEquals(Lookup.TYPE_NOT_FOUND, DnsResult.Code.TYPE_NOT_FOUND.ordinal());
	}
	
	public static void main(String args[]) throws Exception
	{
		UtilsMonitorTestBootstrap.init();
		
		DnsTask dnsTask = new DnsTask("192.168.1.11","www.heise.de");
		DnsResult dnsResult = dnsTask.call();
		
		logger.info(dnsResult.toString());
	}
}