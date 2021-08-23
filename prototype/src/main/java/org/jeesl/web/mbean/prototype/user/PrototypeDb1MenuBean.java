package org.jeesl.web.mbean.prototype.user;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

import org.jeesl.controller.monitoring.counter.ProcessingTimeTracker;
import org.jeesl.factory.xml.system.navigation.XmlMenuItemFactory;
import org.jeesl.jsf.menu.MenuXmlBuilder;
import org.jeesl.model.xml.system.navigation.Breadcrumb;
import org.jeesl.model.xml.system.navigation.Menu;
import org.jeesl.model.xml.system.navigation.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;
import net.sf.ahtutils.xml.access.Access;
import net.sf.exlp.util.xml.JaxbUtil;

public class PrototypeDb1MenuBean implements Serializable
{
	final static Logger logger = LoggerFactory.getLogger(PrototypeDb1MenuBean.class);
	private static final long serialVersionUID = 1L;
	
	private boolean debugOnInfo; protected void setLogInfo(boolean log) {debugOnInfo = log;}
	protected static final String rootMain = "root";
	protected Map<String,Menu> mapMenu;
	protected Map<String,MenuItem> mapSub;
	protected Map<String,Breadcrumb> mapBreadcrumb;
	
	protected Map<String,Boolean> mapViewAllowed;
	protected boolean userLoggedIn;
	
	protected Menu menu;
	protected MenuXmlBuilder mfMain;
	
	protected String localeCode;

	public PrototypeDb1MenuBean()
	{
		userLoggedIn = false;
		localeCode = "en";
		debugOnInfo = false;
	}
	
	@Deprecated //This should be done with fSecurity
	public void initAccess(String views, String menu, String localeCode)
    {
		this.localeCode=localeCode;
		ProcessingTimeTracker ptt = new ProcessingTimeTracker(true);
		
		try
		{
			this.initMaps();
			Access xmlAccess = JaxbUtil.loadJAXB(this.getClass().getClassLoader(),"/"+views, Access.class);
			Menu xmlMenuMain = JaxbUtil.loadJAXB(this.getClass().getClassLoader(),"/"+menu, Menu.class);
			
			if(debugOnInfo && logger.isInfoEnabled()){logger.info("main.root="+rootMain);}

			mfMain = new MenuXmlBuilder(xmlMenuMain,xmlAccess,getLang(),rootMain);
			mfMain.setAlwaysUpToLevel(1);
		}
		catch (FileNotFoundException e)
		{
			throw new IllegalStateException("Class: " +e.getClass().getName() +" Message: " +e.getMessage());
		}
		logger.info(AbstractLogMessage.postConstruct(ptt));
    }
	
    public void initMaps() throws FileNotFoundException
    {
		mapMenu = new Hashtable<String,Menu>();
		mapSub = new Hashtable<String,MenuItem>();
		mapBreadcrumb = new Hashtable<String,Breadcrumb>();
    }
    
	public void clear(){clear(localeCode,false);}
	public void clear(String localeCode, boolean userLoggedIn)
	{
		this.localeCode=localeCode;
		if(debugOnInfo && logger.isInfoEnabled()){logger.info("Clearing hashtables ... userLoggedIn:"+userLoggedIn);}
		this.userLoggedIn=userLoggedIn;
		mapMenu.clear();
		mapSub.clear();
		mapBreadcrumb.clear();
		mapViewAllowed = null;
		if(mfMain!=null){mfMain.switchLang(localeCode);}
	}
	
	protected void buildViewAllowedMap()
	{
		
	}

	// *******************************************
	// Menu
	protected Menu menu(MenuXmlBuilder mf, String code) {return menu(mf,code,userLoggedIn);}
	protected Menu menu(MenuXmlBuilder mf, String code, boolean loggedIn)
	{
		buildViewAllowedMap();
		if(code==null || code.length()==0){code=rootMain;}
		if(!mapMenu.containsKey(code))
		{
			ProcessingTimeTracker ptt = null;
			if(logger.isTraceEnabled()){ptt = new ProcessingTimeTracker(true);}
			synchronized(mf)
			{
				Menu m = mf.build(mapViewAllowed,code,loggedIn);
				if(debugOnInfo)
				{
					logger.info("Menu for: "+code);
					JaxbUtil.info(m);
				}
				mapMenu.put(code, m);
			}
			if(logger.isTraceEnabled()){logger.trace(AbstractLogMessage.time("Menu creation for "+code,ptt));}
		}
		return mapMenu.get(code);
	}
	
	/**
	 * Breadcrumb
	 */
	public Breadcrumb breadcrumb(MenuXmlBuilder mf,String code){return breadcrumb(mf,false,code,false,false);}
	public Breadcrumb breadcrumb(MenuXmlBuilder mf,boolean withRoot, String code, boolean withFirst, boolean withChilds){return breadcrumb(mf,withRoot,code,withFirst,withChilds,null);}
	public Breadcrumb breadcrumb(MenuXmlBuilder mf,boolean withRoot, String code, boolean withFirst, boolean withChilds, Menu dynamicMenu)
	{
		if(!mapBreadcrumb.containsKey(code))
		{
			ProcessingTimeTracker ptt = null;
			if(logger.isTraceEnabled()){ptt = new ProcessingTimeTracker(true);}
			synchronized(mf)
			{
				boolean mapMenuContainsCode = mapMenu.containsKey(code);
//				logger.info("breadcrumb contains "+code+"?"+mapMenuContainsCode);
				if(!mapMenuContainsCode)
				{
//					logger.info("Building Menu");
					if(dynamicMenu!=null){mf.addDynamicNodes(dynamicMenu);}
					menu(mf,code);
				}
				Breadcrumb bOrig = mf.breadcrumb(withRoot,code);
				Breadcrumb bClone = new Breadcrumb();
				int startIndex=0;
				if(bOrig.getMenuItem().size()>1 && !withFirst){startIndex=1;}
				for(int i=startIndex;i<bOrig.getMenuItem().size();i++)
				{
					bClone.getMenuItem().add(XmlMenuItemFactory.clone(bOrig.getMenuItem().get(i)));
				}
				JaxbUtil.trace(bClone);
				//Issue Utils-228
		/*		if(b.getMenuItem().size()>1 && !withFirst)
				{
					b.getMenuItem().remove(0);
				}
				*/
				if(withChilds)
				{
					for(MenuItem mi : bClone.getMenuItem())
					{
						for(MenuItem subOrig : sub(mf, mi.getCode()).getMenuItem())
						{
							mi.getMenuItem().add(XmlMenuItemFactory.clone(subOrig));
						}
					}
				}
				if(debugOnInfo) {JaxbUtil.info(bClone);}
				mapBreadcrumb.put(code,bClone);
				if(logger.isTraceEnabled())
				{
					JaxbUtil.info(mapBreadcrumb.get(code));
				}
			}
			if(logger.isTraceEnabled()){logger.trace(AbstractLogMessage.time("Breadcrumb creation for "+code,ptt));}
		}
		return mapBreadcrumb.get(code);
	}
	
	// ******************************************
	// SubMenu
	public MenuItem sub(MenuXmlBuilder mf, String code){return subDyn(mf,code,null);}
	public MenuItem subDyn(MenuXmlBuilder mf, String code, Menu dynamicMenu)
	{
		boolean mapSubContaines = mapSub.containsKey(code);
		if(debugOnInfo && logger.isInfoEnabled()){logger.info("Creating subMenu for '"+code+"' dynamic:"+(dynamicMenu!=null)+" mapSub.contains:"+mapSubContaines);}
		
		if(!mapSubContaines)
		{
			ProcessingTimeTracker ptt=null;
			if(debugOnInfo && logger.isInfoEnabled()){ptt = new ProcessingTimeTracker(true);}
			synchronized(mf)
			{
				if(!mapMenu.containsKey(code))
				{
					if(dynamicMenu!=null){mf.addDynamicNodes(dynamicMenu);}
					menu(mf,code);
				}
				Menu m = mapMenu.get(code);
				mapSub.put(code,mf.subMenu(m,code));
			}
//			JaxbUtil.info(mapSub.get(code));
			if(debugOnInfo){logger.info(AbstractLogMessage.time("Submenu creation for "+code,ptt));}
		}
		if(debugOnInfo){JaxbUtil.info(mapSub.get(code));}
		return mapSub.get(code);
	}
	
	public void userLoggedIn(Map<String,Boolean> allowedViews)
	{
		this.clear(localeCode,true);
		if(debugOnInfo){logger.info("User Logged In: "+allowedViews.size());}
		mapViewAllowed = allowedViews;
	}
	
	public void clearAndRemoveChilds(String key, String dynKey)
	{
		mfMain.removeChilds(key,dynKey);
		mapMenu.remove(key);
		mapSub.remove(key);
		mapBreadcrumb.remove(key);
	}
	
	protected String getLang(){return localeCode;}
	
	public Menu build() {return this.menu(mfMain, rootMain);}
	public Menu build(String code){return this.menu(mfMain, code);}
	
	public Breadcrumb breadcrumb(String code){return this.breadcrumb(mfMain, false, code, true, true);}
	public Breadcrumb breadcrumbDyn(String code,Menu dynamic){return this.breadcrumb(mfMain, false, code, true, true, dynamic);}
	
	public MenuItem sub(String code){return this.sub(mfMain, code);}
	public MenuItem subDyn(String code,Menu dynamic){return this.subDyn(mfMain,code,dynamic);}
}