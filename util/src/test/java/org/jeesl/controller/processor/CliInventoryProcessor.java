package org.jeesl.controller.processor;

import org.exlp.util.jx.JaxbUtil;
import org.jeesl.controller.processor.pcinventory.PcInventoryPostProcessor;
import org.jeesl.controller.processor.pcinventory.PcInventoryProcessor;
import org.jeesl.model.xml.module.inventory.Inventory;
import org.jeesl.test.JeeslBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.StringIO;

public class CliInventoryProcessor
{
	final static Logger logger = LoggerFactory.getLogger(CliInventoryProcessor.class);
	
	public static void main(String[] args) throws Exception
	{
		JeeslBootstrap.init();
		
		Inventory inventory = new Inventory();
		String s = StringIO.loadTxt("src/test/resources/data/txt/inventory.txt");
		
		PcInventoryProcessor pcIP = new PcInventoryProcessor();
		PcInventoryPostProcessor pcIPP = new PcInventoryPostProcessor();
		
		inventory.getComputer().add(pcIPP.postProcess(pcIP.transform(s)));

		JaxbUtil.info(inventory);
	}
}