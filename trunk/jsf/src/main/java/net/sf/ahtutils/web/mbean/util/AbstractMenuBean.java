package net.sf.ahtutils.web.mbean.util;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

import net.sf.ahtutils.factory.xml.navigation.XmlMenuItemFactory;
import net.sf.ahtutils.jsf.menu.MenuFactory;
import net.sf.ahtutils.monitor.ProcessingTimeTracker;
import net.sf.ahtutils.xml.navigation.Breadcrumb;
import net.sf.ahtutils.xml.navigation.Menu;
import net.sf.ahtutils.xml.navigation.MenuItem;
import net.sf.exlp.util.xml.JaxbUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMenuBean implements Serializable
{
	final static Logger logger = LoggerFactory.getLogger(AbstractMenuBean.class);
	private static final long serialVersionUID = 1L;
	
	protected static final String rootMain = "root";
	protected Map<String,Menu> mapMenu;
	protected Map<String,MenuItem> mapSub;
	protected Map<String,Breadcrumb> mapBreadcrumb;
	
	protected Map<String,Boolean> mapViewAllowed;
	protected boolean userLoggedIn;

	public AbstractMenuBean()
	{
		userLoggedIn = false;	
	}
	
    public void initMaps() throws FileNotFoundException
    {
		mapMenu = new Hashtable<String,Menu>();
		mapSub = new Hashtable<String,MenuItem>();
		mapBreadcrumb = new Hashtable<String,Breadcrumb>();
    }
    
	public void clear(){clear(false);}
	public void clear(boolean userLoggedIn)
	{
		this.userLoggedIn=userLoggedIn;
		mapMenu.clear();
		mapSub.clear();
		mapBreadcrumb.clear();
		mapViewAllowed = null;
	}
	
	protected void buildViewAllowedMap()
	{
		logger.warn("This should never been called here. A @Override in extended class is required");
	}
	

	// ******************************************
	// Menu
	protected Menu menu(MenuFactory mf, String code) {return menu(mf,code,userLoggedIn);}
	protected Menu menu(MenuFactory mf, String code, boolean loggedIn)
	{
		buildViewAllowedMap();
		if(code==null || code.length()==0){code=rootMain;}
		if(!mapMenu.containsKey(code))
		{
			ProcessingTimeTracker ptt = null;
			if(logger.isTraceEnabled()){ptt = new ProcessingTimeTracker(true);}
			synchronized(mf)
			{
				mapMenu.put(code, mf.build(mapViewAllowed,code,loggedIn));
			}
			if(logger.isTraceEnabled()){logger.trace(AbstractLogMessage.time("Menu creation for "+code,ptt));}
		}
		return mapMenu.get(code);
	}
	
	/**
	 * Breadcrumb
	 */
	public Breadcrumb breadcrumb(MenuFactory mf,String code){return breadcrumb(mf,false,code,false,false);}
	public Breadcrumb breadcrumb(MenuFactory mf,boolean withRoot, String code, boolean withFirst, boolean withChilds){return breadcrumb(mf,withRoot,code,withFirst,withChilds,null);}
	public Breadcrumb breadcrumb(MenuFactory mf,boolean withRoot, String code, boolean withFirst, boolean withChilds, Menu dynamicMenu)
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
	public MenuItem sub(MenuFactory mf, String code){return subDyn(mf,code,null);}
	public MenuItem subDyn(MenuFactory mf, String code, Menu dynamicMenu)
	{
		boolean mapSubContaines = mapSub.containsKey(code);
//		logger.info("Creating sub... dynamic?"+(dynamicMenu!=null)+" mapContains:"+mapSubContaines);
		
		if(!mapSubContaines)
		{
			ProcessingTimeTracker ptt=null;
			if(logger.isTraceEnabled()){ptt = new ProcessingTimeTracker(true);}
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
//			JaxbUtil.trace(mapSub.get(code));
			if(logger.isTraceEnabled()){logger.trace(AbstractLogMessage.time("Submenu creation for "+code,ptt));}
		}
		return mapSub.get(code);
	}
}