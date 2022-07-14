package org.jeesl.jsf.menu;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jeesl.controller.monitoring.counter.ProcessingTimeTracker;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.jsf.JeeslMenuException;
import org.jeesl.factory.xml.system.navigation.XmlMenuItemFactory;
import org.jeesl.interfaces.controller.builder.MenuBuilder;
import org.jeesl.model.xml.system.navigation.Breadcrumb;
import org.jeesl.model.xml.system.navigation.Menu;
import org.jeesl.model.xml.system.navigation.MenuItem;
import org.jeesl.model.xml.system.navigation.UrlMapping;
import org.jeesl.util.query.xpath.NavigationXpath;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;
import net.sf.ahtutils.xml.access.Access;
import net.sf.ahtutils.xml.security.Security;
import net.sf.ahtutils.xml.status.Lang;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.util.xml.JaxbUtil;

public class MenuXmlBuilder implements MenuBuilder
{
	final static Logger logger = LoggerFactory.getLogger(MenuXmlBuilder.class);
	
	private final boolean debugOnInfo = false;
	
	private boolean oldImplementation;
	private String lang;

	private String contextRoot;
	
	public void setContextRoot(String contextRoot) {this.contextRoot = contextRoot;}

	private boolean noRestrictions;
	private Map<String,Boolean> mapViewAllowed;
	private Map<String,Map<String,String>> translationsMenu,translationsAccess;
	private Map<String,net.sf.ahtutils.xml.access.View> mapAccessViews;
	private Map<String,net.sf.ahtutils.xml.security.View> mapSecurityViews;
	private Map<String,MenuItem> mapMenuItems;
	private Map<String,String> mapParent;
	
	private String rootNode;
	private DirectedGraph<String, DefaultEdge> graph;
	
	private int alwaysUpToLevel;public void setAlwaysUpToLevel(int alwaysUpToLevel) {this.alwaysUpToLevel = alwaysUpToLevel;}
	
	public MenuXmlBuilder(Menu menu,String lang){this(menu,null,lang, UUID.randomUUID().toString(),true);}
	public MenuXmlBuilder(Menu menu,String lang, String rootNode){this(menu,null,lang,rootNode,true);}	
	public MenuXmlBuilder(Menu menu, Access access,String lang){this(menu,access,lang, UUID.randomUUID().toString(),false);}
	public MenuXmlBuilder(Menu menu, Access access,String lang, String rootNode){this(menu,access,lang, rootNode,false);}
	public MenuXmlBuilder(Menu menu, Access access,String lang, String rootNode, boolean noRestrictions)
	{	
		this.rootNode=rootNode;
		this.noRestrictions=noRestrictions;
		
		translationsMenu = new Hashtable<String,Map<String,String>>();
		translationsAccess = new Hashtable<String,Map<String,String>>();
		mapMenuItems = new Hashtable<String,MenuItem>();
		mapParent = new Hashtable<String,String>();
		
		this.switchLang(lang);
		
		processMenu(menu);
		
		if(logger.isTraceEnabled())
		{
			logger.info("Graph: "+graph);
			logger.info("mapMenuItems.size()"+mapMenuItems.size());
		}
		
		oldImplementation = true;
		mapAccessViews = new Hashtable<String,net.sf.ahtutils.xml.access.View>();
		
		if(access!=null){buildViewMap(access);}
		alwaysUpToLevel = 1;
	}
	
	public MenuXmlBuilder(Menu menu, Security security,String localeCode, String rootNode)
	{
		
		this.rootNode=rootNode;
		this.noRestrictions=false;
		
		translationsMenu = new Hashtable<String,Map<String,String>>();
		translationsAccess = new Hashtable<String,Map<String,String>>();
		mapMenuItems = new Hashtable<String,MenuItem>();
		mapParent = new Hashtable<String,String>();
		
		this.switchLang(localeCode);
		
		processMenu(menu);
		
		if(logger.isTraceEnabled())
		{
			logger.info("Graph: "+graph);
			logger.info("mapMenuItems.size()"+mapMenuItems.size());
		}
		
		oldImplementation = false;
		mapSecurityViews = new Hashtable<String,net.sf.ahtutils.xml.security.View>();
		
		if(security!=null){buildViewMap(security);}
		alwaysUpToLevel = 1;
	}
	
	private void processMenu(Menu menu)
	{
		graph = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
		graph.addVertex(rootNode);
		
		logger.trace("Added Root: "+rootNode);
	     	
		for(MenuItem mi : menu.getMenuItem())
		{
			processMenuItem(rootNode,mi);
		}
	}
	
	private void processMenuItem(String parentNode, MenuItem mi)
	{
		mapParent.put(mi.getCode(), parentNode);
		graph.addVertex(mi.getCode());
		graph.addEdge(parentNode, mi.getCode());
		
		if(mi.isSetLangs())
		{
			for(Lang l : mi.getLangs().getLang())
			{
				checkLang(l.getKey());
				translationsMenu.get(l.getKey()).put(mi.getCode(), l.getTranslation());
			}
		}
		for(MenuItem miChild : mi.getMenuItem())
		{
			processMenuItem(mi.getCode(),miChild);
		}
		mi.getMenuItem().clear();
		mapMenuItems.put(mi.getCode(), mi);
	}
	
	private void buildViewMap(Access access)
	{
		for(net.sf.ahtutils.xml.access.Category c : access.getCategory())
		{
			if(c.isSetViews())
			{
				for(net.sf.ahtutils.xml.access.View v : c.getViews().getView())
				{
					mapAccessViews.put(v.getCode(), v);
					if(v.isSetLangs())
					{
						for(Lang l : v.getLangs().getLang())
						{
							checkLang(l.getKey());
							translationsAccess.get(l.getKey()).put(v.getCode(), l.getTranslation());
						}
					}
				}
			}
		}
	}
	
	private void buildViewMap(Security security)
	{
		for(net.sf.ahtutils.xml.security.Category c : security.getCategory())
		{
			if(c.isSetTmp())
			{
				for(net.sf.ahtutils.xml.security.View v : c.getTmp().getView())
				{
					mapSecurityViews.put(v.getCode(), v);
					if(v.isSetLangs())
					{
						for(Lang l : v.getLangs().getLang())
						{
							checkLang(l.getKey());
							translationsAccess.get(l.getKey()).put(v.getCode(), l.getTranslation());
						}
					}
				}
			}
		}
	}
	
	public Menu build()
	{
		noRestrictions=true;
		return build(null,rootNode);
	}
	public Menu build(String codeCurrent) {return build(null,codeCurrent);}
	public Menu build(Map<String,Boolean> mapViewAllowed) {return build(mapViewAllowed,rootNode);}
	public Menu build(Map<String,Boolean> mapViewAllowed, String codeCurrent){return build(mapViewAllowed,codeCurrent,false);}
	public Menu build(Map<String,Boolean> mapViewAllowed, String codeCurrent, boolean isLoggedIn)
	{
//		logger.info("Building Menu root:"+rootNode+" current:"+codeCurrent+" alwaysUpToLevel:"+alwaysUpToLevel);
		this.mapViewAllowed=mapViewAllowed;
		Menu result = new Menu();
		
		ProcessingTimeTracker ptt = null;
		if(logger.isTraceEnabled()){ptt = ProcessingTimeTracker.instance().start();;}
		try
		{
			if(oldImplementation){result.getMenuItem().addAll(processChildsOld(1,rootNode,codeCurrent,isLoggedIn));}
			else {result.getMenuItem().addAll(processChildsNew(1,rootNode,codeCurrent,isLoggedIn));}
		}
		catch (JeeslNotFoundException e) {logger.warn(e.getMessage());}
		if(logger.isTraceEnabled()){logger.info(AbstractLogMessage.time("build "+codeCurrent,ptt));}
		
		return result;
	}
	
	private List<MenuItem> processChildsOld(int level, String node, String codeCurrent, boolean isLoggedIn) throws JeeslNotFoundException
	{
		List<MenuItem> result = new ArrayList<MenuItem>();
		
		Iterator<DefaultEdge> iterator = graph.outgoingEdgesOf(node).iterator();
		
		while(iterator.hasNext())
		{
			DefaultEdge edge = iterator.next();
			MenuItem miAdd = null;
			MenuItem mi = mapMenuItems.get(graph.getEdgeTarget(edge));
			if(mi.isSetView())
			{
				if(!mapAccessViews.containsKey(mi.getView().getCode())){throw new JeeslNotFoundException("No view with code="+mi.getView().getCode());}
				net.sf.ahtutils.xml.access.View view = mapAccessViews.get(mi.getView().getCode());
				if(noRestrictions
						|| view.isPublic()
						|| (view.isSetOnlyLoginRequired() && view.isOnlyLoginRequired() && isLoggedIn)
						|| (mapViewAllowed!=null && mapViewAllowed.containsKey(mi.getView().getCode()) && mapViewAllowed.get(mi.getView().getCode())))
				{
					miAdd = processItemOld(mi,codeCurrent,view);
				}
			}
			else
			{
				miAdd = processItemOld(mi,codeCurrent,null);
			}
			if(miAdd!=null)
			{
				if(mi.getCode().equals(codeCurrent)){miAdd.setActive(true);}
				else{miAdd.setActive(false);}
				
				boolean currentIsChild = false;
				
				DijkstraShortestPath<String, DefaultEdge> dsp;
				List<DefaultEdge> path = null;
				
				try
				{
					dsp = new DijkstraShortestPath<String, DefaultEdge>(graph, mi.getCode(), codeCurrent);
					path = dsp.getPathEdgeList();
				}
				catch (IllegalArgumentException e)
				{
					logger.error("Error in graph from "+mi.getCode()+" to "+codeCurrent);
					logger.error(e.getMessage());
				}
				
				if(path!=null)
				{
					currentIsChild = true;
					miAdd.setActive(true);
				}
				
				if(level<alwaysUpToLevel || currentIsChild)
				{
					miAdd.getMenuItem().addAll(processChildsOld(level+1,mi.getCode(),codeCurrent,isLoggedIn));
					
				}
				mapMenuItems.get(miAdd.getCode()).setName(miAdd.getName());
				mapMenuItems.get(miAdd.getCode()).setHref(miAdd.getHref());
				result.add(miAdd);
			}
		}
		return result;
	}
	
	private List<MenuItem> processChildsNew(int level, String node, String codeCurrent, boolean isLoggedIn) throws JeeslNotFoundException
	{
		List<MenuItem> result = new ArrayList<MenuItem>();
		
		Iterator<DefaultEdge> iterator = graph.outgoingEdgesOf(node).iterator();
		
		while(iterator.hasNext())
		{
			DefaultEdge edge = iterator.next();
			MenuItem miAdd = null;
			MenuItem mi = mapMenuItems.get(graph.getEdgeTarget(edge));
			if(mi.isSetView())
			{
				if(!mapSecurityViews.containsKey(mi.getView().getCode())){throw new JeeslNotFoundException("No view with code="+mi.getView().getCode());}
				net.sf.ahtutils.xml.security.View view = mapSecurityViews.get(mi.getView().getCode());
				if(noRestrictions
						|| view.getAccess().isPublicUser()
						|| (view.getAccess().isAuthenticatedUser() && isLoggedIn)
						|| (mapViewAllowed!=null && mapViewAllowed.containsKey(mi.getView().getCode()) && mapViewAllowed.get(mi.getView().getCode())))
				{
					miAdd = processItemNew(mi,codeCurrent,view);
				}
			}
			else
			{
				miAdd = processItemNew(mi,codeCurrent,null);
			}
			if(miAdd!=null)
			{
				if(mi.getCode().equals(codeCurrent)){miAdd.setActive(true);}
				else{miAdd.setActive(false);}
				
				boolean currentIsChild = false;
				
				DijkstraShortestPath<String, DefaultEdge> dsp;
				List<DefaultEdge> path = null;
				
				try
				{
					dsp = new DijkstraShortestPath<String, DefaultEdge>(graph, mi.getCode(), codeCurrent);
					path = dsp.getPathEdgeList();
				}
				catch (IllegalArgumentException e)
				{
					logger.error("Error in graph from "+mi.getCode()+" to "+codeCurrent);
					logger.error(e.getMessage());
				}
				
				if(path!=null)
				{
					currentIsChild = true;
					miAdd.setActive(true);
				}
				
				if(level<alwaysUpToLevel || currentIsChild)
				{
					if(oldImplementation){miAdd.getMenuItem().addAll(processChildsOld(level+1,mi.getCode(),codeCurrent,isLoggedIn));}
					else
					{
						logger.warn("NYI");
					}
					
				}
				mapMenuItems.get(miAdd.getCode()).setName(miAdd.getName());
				mapMenuItems.get(miAdd.getCode()).setHref(miAdd.getHref());
				result.add(miAdd);
			}
		}
		return result;
	}
	
	private MenuItem processItemOld(MenuItem miOrig, String codeCurrent, net.sf.ahtutils.xml.access.View view) throws JeeslNotFoundException
	{
		MenuItem mi = new MenuItem();
		mi.setCode(miOrig.getCode());
		if(miOrig.isSetLangs())
		{
			mi.setName(getNameFromMenuItem(miOrig.getCode()));
		}
		else if(miOrig.isSetView())
		{
			mi.setName(getNameFromViewsOld(view,miOrig.getView()));
		}
		else
		{
			logger.warn("Translation missing!!");
			mi.setName("Translation missing");	
		}
		
		if(miOrig.isSetHref())
		{
			mi.setHref(miOrig.getHref());
		}
		else if(miOrig.isSetView())
		{
			mi.setHref(getHrefFromViewsOld(miOrig.getView()));
		}
		else
		{
//			mi.setHref("#");
		}
		return mi;
	}
	
	private MenuItem processItemNew(MenuItem miOrig, String codeCurrent, net.sf.ahtutils.xml.security.View view) throws JeeslNotFoundException
	{
		MenuItem mi = new MenuItem();
		mi.setCode(miOrig.getCode());
		if(miOrig.isSetLangs())
		{
			mi.setName(getNameFromMenuItem(miOrig.getCode()));
		}
		else if(miOrig.isSetView())
		{
			mi.setName(getNameFromViewsNew(view,miOrig.getView()));
		}
		else
		{
			logger.warn("Translation missing!!");
			mi.setName("Translation missing");	
		}
		
		if(miOrig.isSetHref())
		{
			mi.setHref(miOrig.getHref());
		}
		else if(miOrig.isSetView())
		{
			mi.setHref(getHrefFromViewsNew(miOrig.getView()));
		}
		else
		{
//			mi.setHref("#");
		}
		return mi;
	}
	
	private String getNameFromMenuItem(String code)
	{
		if(!translationsMenu.containsKey(lang)){return "???no-lang-for-"+lang+"???";}
		else if(!translationsMenu.get(lang).containsKey(code)){return "???"+code+"???";}
		else {return translationsMenu.get(lang).get(code);}
	}
	
	private String getNameFromViewsOld(net.sf.ahtutils.xml.access.View view, net.sf.ahtutils.xml.access.View viewCode)
	{
		StringBuffer sbLabel = new StringBuffer();
		if(!translationsAccess.containsKey(lang)){return "???no-lang-for-"+lang+"???";}
		else if(!translationsAccess.get(lang).containsKey(view.getCode())){sbLabel.append("???"+view.getCode()+"???");}
		else {sbLabel.append(translationsAccess.get(lang).get(view.getCode()));}
		
		if(viewCode.isSetLabel())
		{
			if(sbLabel.length()>0){sbLabel.append(" ");}
			sbLabel.append(viewCode.getLabel());	
		}
		return sbLabel.toString();
	}
	
	private String getNameFromViewsNew(net.sf.ahtutils.xml.security.View view, net.sf.ahtutils.xml.access.View viewCode)
	{
		StringBuffer sbLabel = new StringBuffer();
		if(!translationsAccess.containsKey(lang)){return "???no-lang-for-"+lang+"???";}
		else if(!translationsAccess.get(lang).containsKey(view.getCode())){sbLabel.append("???"+view.getCode()+"???");}
		else {sbLabel.append(translationsAccess.get(lang).get(view.getCode()));}
		
		if(viewCode.isSetLabel())
		{
			if(sbLabel.length()>0){sbLabel.append(" ");}
			sbLabel.append(viewCode.getLabel());	
		}
		return sbLabel.toString();
	}
	
	private String getHrefFromViewsOld(net.sf.ahtutils.xml.access.View viewCode)
	{
		net.sf.ahtutils.xml.access.View view = mapAccessViews.get(viewCode.getCode());
		if(view.isSetNavigation() && view.getNavigation().isSetUrlMapping())
		{
			UrlMapping urlMapping = view.getNavigation().getUrlMapping();
			StringBuffer sb = new StringBuffer();
			if(contextRoot!=null)
			{
				sb.append("/").append(contextRoot);
			}
			if(urlMapping.isSetUrl())
			{
				sb.append(urlMapping.getUrl());
				if(viewCode.isSetUrlParameter()){sb.append(viewCode.getUrlParameter());}
			}
			else{sb.append(urlMapping.getValue());}
			return sb.toString();
		}

		return null;
	}
	
	private String getHrefFromViewsNew(net.sf.ahtutils.xml.access.View viewCode)
	{
		net.sf.ahtutils.xml.security.View view = mapSecurityViews.get(viewCode.getCode());
		if(view.isSetNavigation() && view.getNavigation().isSetUrlMapping())
		{
			UrlMapping urlMapping = view.getNavigation().getUrlMapping();
			StringBuffer sb = new StringBuffer();
			if(contextRoot!=null)
			{
				sb.append("/").append(contextRoot);
			}
			if(urlMapping.isSetUrl())
			{
				sb.append(urlMapping.getUrl());
				if(viewCode.isSetUrlParameter()){sb.append(viewCode.getUrlParameter());}
			}
			else{sb.append(urlMapping.getValue());}
			return sb.toString();
		}

		return null;
	}
	
	public void removeChilds(String dynamicRoot, String dynKey)
	{
		Iterator<DefaultEdge> iterator = graph.outgoingEdgesOf(dynamicRoot).iterator();
		List<DefaultEdge> list = new ArrayList<DefaultEdge>();
		while(iterator.hasNext())
		{
			DefaultEdge edge = iterator.next();
			String target = graph.getEdgeTarget(edge);
			if(target.startsWith(dynKey))
			{
				list.add(edge);
			}
		}
		graph.removeAllEdges(list);
	}
	
	private void removeEdgeToTemplate(String dynamicRoot, String templateCode)
	{
		Iterator<DefaultEdge> iterator = graph.getAllEdges(dynamicRoot, templateCode).iterator();
		List<DefaultEdge> list = new ArrayList<DefaultEdge>();
		while(iterator.hasNext()){list.add(iterator.next());}
		graph.removeAllEdges(list);
	}
	
	public void addDynamicNodes(Menu dynamicMenu)
	{
		for(MenuItem mi : dynamicMenu.getMenuItem())
		{
			removeEdgeToTemplate(dynamicMenu.getCode(), mi.getView().getCode());
			
			if(logger.isTraceEnabled())
			{
				logger.info("Adding Edge "+dynamicMenu.getCode() +" -> "+ mi.getCode());
			}
			
			graph.addVertex(mi.getCode());
			graph.addEdge(dynamicMenu.getCode(), mi.getCode());
			
			mapMenuItems.put(mi.getCode(), mi);
		}
	}
	
	public Breadcrumb breadcrumb(String code){return breadcrumb(false,code);}
	public Breadcrumb breadcrumb(boolean withRoot, String code)
	{
		if(logger.isTraceEnabled())
		{
			logger.info("Breadcrumb withRoot:"+withRoot+" code:"+code);
			logger.info("Graph contains root:"+rootNode+"?"+rootNode);
			logger.info("Graph contains vertex:"+code+"? "+graph.containsVertex(code));
			logger.info("Graph contains edge:"+graph.containsEdge("monitoringTransfers","monitoringTransfersDyntransfersAnalyst"));
//			logger.info(graph.toString());
		}

		Breadcrumb result = new Breadcrumb();		
		try
		{
			DijkstraShortestPath<String, DefaultEdge> dsp = new DijkstraShortestPath<String, DefaultEdge>(graph,rootNode, code);
			List<DefaultEdge> path = dsp.getPathEdgeList();
			if(logger.isTraceEnabled())
			{
				logger.info("Path!=null"+(path!=null));
			}
			
			if(path.size()>0)
			{
				result.getMenuItem().add(mapMenuItems.get(graph.getEdgeSource(path.get(0))));
			}
			for(DefaultEdge de : path)
			{
				String src = graph.getEdgeTarget(de);
				result.getMenuItem().add(mapMenuItems.get(src));
			}
			if(!withRoot){result.getMenuItem().remove(0);}
		}
		catch(IllegalArgumentException e)
		{
			if(code==null){logger.error("The provided code is NULL");}
			logger.error("Breadcrumb from "+rootNode+"->"+code+" "+e.getMessage());
			logger.error(graph.toString());
		}
		return result;
	}
	
	public MenuItem subMenu(Menu menu, String code)
	{
		if(debugOnInfo)
		{
			logger.info("subMenu "+code);
			JaxbUtil.info(menu);
		}
		MenuItem result = new MenuItem();

		if(code.equals(rootNode))
		{
			result.getMenuItem().addAll(menu.getMenuItem());
		}
		else
		{
			try
			{
				MenuItem item = NavigationXpath.getMenuItem(menu,code);
				for(MenuItem sub : item.getMenuItem())
				{
					MenuItem i = XmlMenuItemFactory.build(sub);
					result.getMenuItem().add(i);
				}
			}
			catch (ExlpXpathNotFoundException e)
			{
				StringBuilder sb = new StringBuilder();
				sb.append("In the following menu the item");
				sb.append(" with code=").append(code).append("can not be found.");
				logger.error(sb.toString()+" The menu xml will be shown:");
				JaxbUtil.info(menu);
				throw new JeeslMenuException(sb.toString());
			}		
		}
		return result;
	}
	
	public void switchLang(String lang)
	{
		this.lang = lang;
	}
	
	private void checkLang(String checkLanguage)
	{
		if(!translationsMenu.containsKey(checkLanguage)){translationsMenu.put(checkLanguage, new Hashtable<String,String>());}
		if(!translationsAccess.containsKey(checkLanguage)){translationsAccess.put(checkLanguage, new Hashtable<String,String>());}
	}
	
	public String getParent(String code)
	{
		return mapParent.get(code);
	}
}