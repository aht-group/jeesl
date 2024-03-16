package net.sf.ahtutils.jsf.menu;

import org.exlp.util.jx.JaxbUtil;
import org.jeesl.model.xml.system.navigation.Menu;
import org.jeesl.test.JeeslJsfTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TstMenu 
{
	final static Logger logger = LoggerFactory.getLogger(TstMenu.class);
	
	public TstMenu()
	{
		
	}
	
	public void debug()
	{
		logger.debug("Debugging XML menu");
		Menu menu = DummyMenuFactory.create();
		JaxbUtil.debug(menu);
	}
	
	public static void main(String[] args)
    {
		JeeslJsfTestBootstrap.init();		
			
		TstMenu test = new TstMenu();
		test.debug();
    }
}