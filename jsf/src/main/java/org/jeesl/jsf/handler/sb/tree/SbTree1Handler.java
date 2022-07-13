package org.jeesl.jsf.handler.sb.tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeesl.controller.handler.tree.TreeUpdateParameter;
import org.jeesl.interfaces.controller.handler.tree.bean.JeeslSbTreeBean;
import org.jeesl.interfaces.controller.handler.tree.cache.JeeslTree1Cache;
import org.jeesl.interfaces.controller.handler.tree.store.JeeslTree1Store;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SbTree1Handler <L1 extends EjbWithId>
{
	final static Logger logger = LoggerFactory.getLogger(SbTree1Handler.class);
	
	protected static final String warnMessageOverrideNextLevel = "This should not be caleld here, @Override this method in the next hierarchy level!";
	protected static final String warnMessageOverrideImplementation = "This should not be caleld here, @Override this method in final implementation class!";
	
	protected final JeeslSbTreeBean callback;
	private final JeeslTree1Cache<L1> cache1;
	private final JeeslTree1Store<L1> store1;
	
	protected boolean showLevel1; public boolean isShowLevel1() {return showLevel1;}
	protected boolean showLevel2; public boolean isShowLevel2() {return showLevel2;}
	protected boolean showLevel3; public boolean isShowLevel3() {return showLevel3;} public void setShowLevel3(boolean showLevel3) {this.showLevel3 = showLevel3;}
	protected boolean showLevel4; public boolean isShowLevel4() {return showLevel4;} public void setShowLevel4(boolean showLevel4) {this.showLevel4 = showLevel4;}
	protected boolean showLevel5; public boolean isShowLevel5() {return showLevel5;} public void setShowLevel5(boolean showLevel5) {this.showLevel5 = showLevel5;}
	
	protected boolean debugOnInfo; public void setDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo = debugOnInfo;}
	protected boolean viewIsGlobal;
	
	protected final Set<L1> allowChild1;
	protected final Set<L1> allowPath1;
	protected final Set<L1> ignore1;
	
	protected final List<L1> list1; public List<L1> getList1() {return list1;}
	
	protected L1 l1; public L1 getL1(){return l1;} public void setL1(L1 district){this.l1 = district;}
	protected String xpath1; public String getXpath1() {return xpath1;}
	
	public SbTree1Handler(JeeslSbTreeBean callback, JeeslTree1Cache<L1> cache1, JeeslTree1Store<L1> store1)
	{
		this.callback=callback;
		this.cache1=cache1;
		this.store1=store1;
		
		list1 = new ArrayList<L1>();
		
		allowChild1 = new HashSet<L1>();
		allowPath1 = new HashSet<L1>();
		ignore1 = new HashSet<L1>();
		
		showLevel1 = true;
		showLevel2 = false;
		showLevel3 = false;
		showLevel4 = false;
		showLevel5 = false;
		
		xpath1 = "@id";
	}
	
	// Methods to reset the Selections
	protected void reset1(){reset1(true);}
	protected void reset1(boolean r1)
	{
		if(r1) {l1=null;}
	}
	
	// Adding Allowed elementes, e.g. defined by a Security Context
	protected void addAllowedChildL1(List<L1> list)
	{
		for(L1 p : list)
		{
			if(!allowChild1.contains(p)) {allowChild1.add(p);}
		}
	}
	protected void addAllowedPathL1(L1 ejb)
	{
		if(!allowPath1.contains(ejb)) {allowPath1.add(ejb);}
	}
	
	// Default Selection from a Security Context
	protected void selectSecurity1()
	{
		if(debugOnInfo) {logger.info("Checking for Security Level 1 Select");}
		
		if(!allowChild1.isEmpty())
		{
			L1 ejb = new ArrayList<L1>(allowChild1).get(0);
			if(debugOnInfo) {logger.info("selectSecurity1 "+ejb.getClass().getSimpleName()+" "+ejb.toString());}
			cascade1(ejb,TreeUpdateParameter.build(true,true,true,true));
		}
	}
	
	// Selection from UI and cascading of event
	public void uiSelect1(L1 province) {cascade1(province,TreeUpdateParameter.build(false,true,true,true));}
	protected void cascade1(L1 ejb, TreeUpdateParameter tup)
	{
		if(debugOnInfo) {logger.info("cascade1 "+ejb.getClass().getSimpleName()+": ["+ejb.toString()+"] "+TreeUpdateParameter.class.getSimpleName()+": ["+tup.toString()+"]");}
		this.l1=ejb;
		if(store1!=null) {store1.storeTreeLevel1(ejb);}
		clearL2List();
			
		if(tup.isFillParent()) {fillThis();}
		if(tup.isFillChilds()) {fillL2List();}
		if(tup.isSelectChild()) {selectDefaultL2(tup.copy().callback(false).fillParent(false));}
		if(tup.isCallback() && callback!=null) {callback.sbTreeSelected();}
	}
	
	private void fillThis()
	{
		List<L1> childs = cache1.getCachedL1();
		if(debugOnInfo) {logger.info("Filling Level-1-List, Checking "+childs.size()+" elements");}
		list1.clear();
		for(L1 ejb : cache1.getCachedL1())
		{
			boolean isCascade = ejb.equals(l1);
			boolean isAllow = allowChild1.contains(ejb);
			boolean isNotIgnore = !ignore1.contains(ejb);
			boolean isInPath = allowPath1.contains(ejb);
			if(evaluateToAddChild(ejb,isCascade,isAllow,isInPath,isInPath,isNotIgnore)) {list1.add(ejb);}
		}
	}
	
	protected boolean evaluateToAddChild(EjbWithId ejb,
											boolean isCascade,			// element is used in the cascade
											boolean isAllowed,			// element is explicitly Allowed
									//		boolean isParentInList,		// element.parent is part of parent list
											boolean isInPath,		// element is in path
											boolean isParentsAllowed,	// element.parents is explicitly allowed
											boolean isNotIgnore			// element is not on ignore list
											)
	{
		boolean or3 = isCascade || viewIsGlobal || isAllowed || isInPath;
		boolean result = (or3 || isParentsAllowed) && isNotIgnore;
		if(debugOnInfo)
		{
			logger.info("\t"+ejb.toString()+" Final:"+result);
			logger.info("\t\tisCascade:"+isCascade);
			logger.info("\t\tviewIsGlobal:"+viewIsGlobal);
			logger.info("\t\tisAllow:"+isAllowed);
			logger.info("\t\tisInPath:"+isInPath);
			logger.info("\t\tisParentsAllowed:"+isParentsAllowed);
			logger.info("\t\tisNotIgnore2:"+isNotIgnore);
		}
		return result;
	}
	
	//Methods need to be implemented in next Level
	protected void clearL2List() {logger.warn(warnMessageOverrideNextLevel);}
	protected void fillL2List() {logger.warn(warnMessageOverrideNextLevel);}
	protected void selectDefaultL2(TreeUpdateParameter hlup) {logger.warn(warnMessageOverrideNextLevel);}
	
	public void debug(boolean debug)
	{
		if(debug)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("\tList1 (").append(list1.size()).append("): ");
			
			sb.append("Selected1: [");
			if(l1!=null) {sb.append(l1.getClass().getSimpleName()).append(" ").append(l1.toString());}else {sb.append(": null");}
			sb.append("]");
			
			sb.append(" Allow1 ("+allowChild1.size()+"): ");
			logger.info(sb.toString());
			for(L1 l : allowChild1){sb.append(l.toString()+" ");}
			logger.info(sb.toString());
		}
	}
}