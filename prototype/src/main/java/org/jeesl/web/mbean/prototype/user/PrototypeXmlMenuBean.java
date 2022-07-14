package org.jeesl.web.mbean.prototype.user;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Map;

import org.jeesl.controller.monitoring.counter.ProcessingTimeTracker;
import org.jeesl.jsf.menu.MenuXmlBuilder;
import org.jeesl.model.xml.system.navigation.Breadcrumb;
import org.jeesl.model.xml.system.navigation.Menu;
import org.jeesl.model.xml.system.navigation.MenuItem;
import org.jeesl.web.mbean.jsf.user.AbstractMenuXmlBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;
import net.sf.ahtutils.xml.access.Access;
import net.sf.ahtutils.xml.security.Security;
import net.sf.exlp.util.xml.JaxbUtil;

public class PrototypeXmlMenuBean extends AbstractMenuXmlBean implements Serializable
{
	final static Logger logger = LoggerFactory.getLogger(PrototypeXmlMenuBean.class);
	private static final long serialVersionUID = 1L;
	
	protected Menu menu;
	protected MenuXmlBuilder mfMain;
	
	public PrototypeXmlMenuBean()
	{
		
	}
	
    public void initAccess(String views, String menu)
    {
		ProcessingTimeTracker ptt = ProcessingTimeTracker.instance().start();;
		try
		{
			super.initMaps();
			Access xmlAccess = JaxbUtil.loadJAXB(this.getClass().getClassLoader(),"/"+views, Access.class);
			Menu xmlMenuMain = JaxbUtil.loadJAXB(this.getClass().getClassLoader(),"/"+menu, Menu.class);
			
			if(logger.isTraceEnabled()){logger.info("main.root="+rootMain);}

			mfMain = new MenuXmlBuilder(xmlMenuMain,xmlAccess,getLang(),rootMain);
			mfMain.setAlwaysUpToLevel(1);
		}
		catch (FileNotFoundException e){throw new IllegalStateException("Class: " +e.getClass().getName() +" Message: " +e.getMessage());}
		logger.info(AbstractLogMessage.postConstruct(ptt));
    }
    
    public void initSecurity(String views, String menu)
    {
		ProcessingTimeTracker ptt = ProcessingTimeTracker.instance().start();;
		try
		{
			super.initMaps();
			Security security = JaxbUtil.loadJAXB(this.getClass().getClassLoader(),"/"+views, Security.class);
			Menu xmlMenuMain = JaxbUtil.loadJAXB(this.getClass().getClassLoader(),"/"+menu, Menu.class);
			
			if(logger.isTraceEnabled()){logger.info("main.root="+rootMain);}

			mfMain = new MenuXmlBuilder(xmlMenuMain,security,getLang(),rootMain);
			mfMain.setAlwaysUpToLevel(1);
		}
		catch (FileNotFoundException e){throw new IllegalStateException("Class: " +e.getClass().getName() +" Message: " +e.getMessage());}
		logger.info(AbstractLogMessage.postConstruct(ptt));
    }
	
	@Override protected void buildViewAllowedMap() {}
	
	public void userLoggedIn(Map<String,Boolean> allowedViews)
	{
		this.clear(true);
		mapViewAllowed = allowedViews;
	}
	
	public void clearAndRemoveChilds(String key, String dynKey)
	{
		mfMain.removeChilds(key,dynKey);
		mapMenu.remove(key);
		mapSub.remove(key);
		mapBreadcrumb.remove(key);
	}
	
	protected String getLang(){return "en";}
	
	public Menu build() {return super.menu(mfMain, rootMain);}
	public Menu build(String code){return super.menu(mfMain, code);}
	
	public Breadcrumb breadcrumb(String code){return super.breadcrumb(mfMain, false, code, true, true);}
	public Breadcrumb breadcrumbDyn(String code,Menu dynamic){return super.breadcrumb(mfMain, false, code, true, true, dynamic);}
	
	public MenuItem sub(String code){return super.sub(mfMain, code);}
	public MenuItem subDyn(String code,Menu dynamic){return super.subDyn(mfMain,code,dynamic);}
}