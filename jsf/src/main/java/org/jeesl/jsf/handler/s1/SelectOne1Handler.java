package org.jeesl.jsf.handler.s1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeesl.controller.handler.tree.TreeUpdateParameter;
import org.jeesl.interfaces.controller.handler.tree.bean.JeeslS1TreeBean;
import org.jeesl.interfaces.controller.handler.tree.cache.JeeslTree1Cache;
import org.jeesl.interfaces.controller.handler.tree.implementation.JeeslSelectOneTreeHandler;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectOne1Handler <L1 extends EjbWithId> implements JeeslSelectOneTreeHandler
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(SelectOne1Handler.class);
	
	protected static final String warnMessageOverrideNextLevel = "This should not be caleld here, @Override this method in the next hierarchy level!";
	protected static final String warnMessageOverrideImplementation = "This should not be caleld here, @Override this method in final implementation class!";
	
	protected final JeeslS1TreeBean callback;
	private final JeeslTree1Cache<L1> cache1;
	
	protected boolean debugOnInfo; public void setDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo = debugOnInfo;}
	
	protected boolean showLevel1; public boolean isShowLevel1() {return showLevel1;}
	
	protected final Set<L1> ignore1;
	protected final List<L1> list1; public List<L1> getList1() {return list1;}
	
	protected L1 l1; public L1 getL1() {return l1;} public void setL1(L1 l1) {this.l1 = l1;}
	protected String xpath1; public String getXpath1() {return xpath1;}
	
	public SelectOne1Handler(JeeslS1TreeBean callback, JeeslTree1Cache<L1> cache1)
	{
		this.callback=callback;
		this.cache1=cache1;

		list1 = new ArrayList<L1>();
		
		ignore1 = new HashSet<L1>();
		
		showLevel1 = true;
		
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
		logger.warn("NYI");
//		for(L1 p : list)
//		{
//			
//		}
	}
	
	protected void selectDefaultL1(TreeUpdateParameter tup)
	{
		if(debugOnInfo) {logger.info("selectDefaultL1 "+tup.toString());}
		reset1();
		L1 ejb = null; if(!list1.isEmpty()) {list1.get(0);}
		cascade1(ejb,tup);
	}
		
	// Selection from UI and cascading of event
	public void uiSelect1() {cascade1(l1, TreeUpdateParameter.build(false,true,true,true));}
	protected void cascade1(L1 ejb, TreeUpdateParameter tup)
	{
		l1 = ejb;
		if(debugOnInfo) {logger.info(toCascadeDebug(1,ejb,tup));}

		clearL2List();
			
		if(tup.isFillParent()) {}
		if(tup.isFillChilds()) {fillL2List();}
		if(tup.isSelectChild()) {selectDefaultL2(tup.copy().callback(false).fillParent(false));}
		if(tup.isCallback() && callback!=null) {callback.s1TreeSelected(this);}
//		if(tup.isFireEvent()) {fireEvent();}
	}
	
	protected void fill1List()
	{
		List<L1> childs = cache1.getCachedL1();
		if(debugOnInfo) {logger.info("Filling Level-1-List, Checking "+childs.size()+" elements");}
		list1.clear();
		for(L1 ejb : cache1.getCachedL1())
		{
			boolean isCascade = l1!=null && ejb.equals(l1);
			boolean isAllow = true;//allowChild1.contains(ejb);
			boolean isNotIgnore = !ignore1.contains(ejb);
			boolean isInPath = true;//allowPath1.contains(ejb);
			if(evaluateToAddChild(ejb,isCascade,isAllow,isInPath,isInPath,isNotIgnore)) {list1.add(ejb);}
		}
	}
	
	protected boolean evaluateToAddChild(EjbWithId ejb,
											boolean isCascade,			// element is used in the cascade
											boolean isAllowed,			// element is explicitly Allowed
											boolean isInPath,			// element is in path
											boolean isParentsAllowed,	// element.parents is explicitly allowed
											boolean isNotIgnore			// element is not on ignore list
											)
	{
		boolean or3 = isCascade || isAllowed || isInPath;
		boolean result = (or3 || isParentsAllowed) && isNotIgnore;
		if(debugOnInfo)
		{
			logger.info("\t"+ejb.toString()+" Final:"+result);
//			logger.info("\t\tisCascade:"+isCascade);
//			logger.info("\t\tisAllow:"+isAllowed);
//			logger.info("\t\tisInPath:"+isInPath);
//			logger.info("\t\tisParentsAllowed:"+isParentsAllowed);
//			logger.info("\t\tisNotIgnore2:"+isNotIgnore);
		}
		return result;
	}
	
	//Methods need to be implemented in next Level
	protected void clearL2List() {logger.warn(warnMessageOverrideNextLevel);}
	protected void fillL2List() {logger.warn(warnMessageOverrideNextLevel);}
	protected void selectDefaultL2(TreeUpdateParameter hlup) {logger.warn(warnMessageOverrideNextLevel);}
	
	protected String toCascadeDebug(int level, EjbWithId ejb, TreeUpdateParameter tup)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("cascade").append(level);
		if(ejb!=null)
		{
			sb.append(ejb.getClass().getSimpleName());
			sb.append(": [").append(ejb.toString()).append("]");
		}
		sb.append(TreeUpdateParameter.class.getSimpleName()+": ["+tup.toString()+"]");
		return sb.toString();
	}
	
	public void debug(boolean debug)
	{
		if(debug)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("\tList1 (").append(list1.size()).append("): ");
			
			sb.append("Selected1: [");
			if(l1!=null) {sb.append(l1.getClass().getSimpleName()).append(" ").append(l1.toString());} else {sb.append(": null");}
			sb.append("]");
			
			logger.info(sb.toString());
			logger.info(sb.toString());
		}
	}
}