package org.jeesl.jsf.handler.sb.tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeesl.controller.handler.tree.TreeUpdateParameter;
import org.jeesl.interfaces.controller.handler.tree.bean.JeeslTreeSelected;
import org.jeesl.interfaces.controller.handler.tree.cache.JeeslTree2Cache;
import org.jeesl.interfaces.controller.handler.tree.store.JeeslTree2Store;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SbTree2Handler <L1 extends EjbWithId, L2 extends EjbWithId> extends SbTree1Handler<L1>
{
	final static Logger logger = LoggerFactory.getLogger(SbTree2Handler.class);
	
	private final JeeslTree2Store<L1,L2> store2;
	private final JeeslTree2Cache<L1,L2> cache2;
	
	protected final Set<L2> allowChild2;
	protected final Set<L2> allowPath2;
	protected final Set<L2> ignore2;
	
	protected final List<L2> list2; public List<L2> getList2() {return list2;}
	
	protected L2 l2; public L2 getL2(){return l2;} public void setL2(L2 district){this.l2 = district;}
	protected String xpath2; public String getXpath2() {return xpath2;}
	
	public SbTree2Handler(JeeslTreeSelected callback, JeeslTree2Cache<L1,L2> cache2, JeeslTree2Store<L1,L2> store2)
	{
		super(callback,cache2,store2);
		this.cache2=cache2;
		this.store2=store2;
		
		list2 = new ArrayList<L2>();
		
		allowChild2 = new HashSet<L2>();
		allowPath2 = new HashSet<L2>();
		ignore2 = new HashSet<L2>();
		
		showLevel2 = true;
		xpath2 = "@id";
	}
	
	// Methods to reset the Selections
	protected void reset2() {reset2(false,true);}
	protected void reset2(boolean r1, boolean r2)
	{
		super.reset1(r1);
		if(r2) {l2=null;}
	}
	
	// Adding Allowed elements, e.g. defined by a Security Context
	protected void addAllowedL2(List<L2> list)
	{
		for(L2 ejb : list)
		{
			if(!allowChild2.contains(ejb)) {allowChild2.add(ejb);}
			super.addAllowedPathL1(getParentForL2(ejb));
		}
	}
	protected void addAllowedPathL2(L2 ejb)
	{
		if(!allowPath2.contains(ejb)) {allowPath2.add(ejb);}
		super.addAllowedPathL1(getParentForL2(ejb));
	}
	
	// Default Selection from a Security Context
	protected void selectSecurity2()
	{
		if(debugOnInfo) {logger.info("Checking for Security Level 2 Select");}
		if(!allowChild2.isEmpty())
		{
			L2 ejb = new ArrayList<L2>(allowChild2).get(0);
			if(debugOnInfo) {logger.info("selectSecurity2 "+ejb.getClass().getSimpleName()+" "+ejb.toString());}
			cascade2(ejb,TreeUpdateParameter.build(true,true,true,true,true));
		}
		else {selectSecurity1();}
	}
	
	// Selection from UI and cascading of event
	public void uiSelect2(L2 ejb) {cascade2(ejb,TreeUpdateParameter.build(false,true,true,true,true));}
	protected void cascade2(L2 ejb, TreeUpdateParameter tup)
	{
		if(debugOnInfo) {logger.info("cascade2 "+ejb.getClass().getSimpleName()+": ["+ejb.toString()+"] "+TreeUpdateParameter.class.getSimpleName()+": ["+tup.toString()+"]");}
		this.l2=ejb;
		if(store2!=null) {store2.storeTreeLevel2(ejb);}
		clearL3List();
		if(tup.isFillParent()) {cascade1(getParentForL2(l2),tup.copy().selectChild(false).callback(false));}
		if(tup.isFillChilds()) {fillL3List();}
		if(tup.isSelectChild()) {selectDefaultL3(tup.copy().fillParent(false).callback(false));}
		if(tup.isCallback() && callback!=null) {callback.sbTreeSelected();}
//		if(hup.isFireEvent()) {fireEvent();}
	}
	
	//Methods called from previous level and @Overriden here (clear,fill,select)
	@Override protected void clearL2List() {list2.clear();}
	@Override protected void fillL2List()
	{
		List<L2> childs = cache2.getCachedChildsForL1(l1);
		if(debugOnInfo) {logger.info("Filling Level-2-List, Checking "+childs.size()+" elements");}
		for(L2 ejb : childs)
		{
			L1 parent = getParentForL2(ejb);
			boolean isCascade = ejb.equals(l2);
			boolean isAllow = allowChild2.contains(ejb);
			boolean isInPath = allowPath2.contains(ejb);
			boolean isParentsAllowed = allowChild1.contains(parent);
			boolean isNotIgnore = !ignore2.contains(ejb);	
			if(evaluateToAddChild(ejb,isCascade,isAllow,isInPath,isParentsAllowed,isNotIgnore)) {list2.add(ejb);}
		}
	}
	@Override protected void selectDefaultL2(TreeUpdateParameter tup)
	{
		if(debugOnInfo) {logger.info("selectDefaultL2 "+tup.toString());}
		reset2();
		if(!list2.isEmpty()) {cascade2(list2.get(0),tup);}
	}
	
	//Methods need to be implemented in next Level
	protected L1 getParentForL2(L2 l2) {logger.warn("getParentForL2 "+SbTree1Handler.warnMessageOverrideImplementation);return null;}
	protected void clearL3List() {logger.warn(warnMessageOverrideNextLevel);}
	protected void fillL3List() {logger.warn(warnMessageOverrideNextLevel);}
	protected void selectDefaultL3(TreeUpdateParameter tup) {logger.warn(warnMessageOverrideNextLevel);}
	
	// Debugging
	public void debug(boolean debug)
	{
		if(debug)
		{
			super.debug(debug);
			StringBuilder sb = new StringBuilder();
			sb.append("\tLevel 2 ").append(list2.size()).append("elements ");
			if(l2!=null) {sb.append(l2.getClass().getSimpleName()).append(": ").append(l2.toString());}else {sb.append(": null");}
			logger.info(sb.toString());
		}
	}
	public void debugAllow2(boolean debug)
	{
		if(debug)
		{
			logger.info("Allow2: "+allowChild2.size()+" elements");
			for(L2 ejb : new ArrayList<L2>(allowChild2))
			{
				logger.info("\t"+ejb.toString());
			}		
		}
	}
	
	public List<L2> listAllowed2()
	{
		List<L2> result = new ArrayList<L2>();
		
		if(viewIsGlobal)
		{
			if(debugOnInfo) {logger.info("Generating listAllowed2");}
			for(L1 l1 : list1)
			{
				if(debugOnInfo) {logger.info("\t"+l1.toString());}
				List<L2> childs = cache2.getCachedChildsForL1(l1);
				if(debugOnInfo) {for(L2 child : childs) {logger.info("\t\t"+child.toString());}}
				result.addAll(childs);
			}
		}
		else
		{
			for(L1 l1 : list1)
			{
				for(L2 ejb : cache2.getCachedChildsForL1(l1))
				{
					L1 parent = getParentForL2(ejb);
					boolean isCascade = ejb.equals(l2);
					boolean isAllow = allowChild2.contains(ejb);
					boolean isInPath = allowPath2.contains(ejb);
					boolean isParentsAllowed = allowChild1.contains(parent);
					boolean isNotIgnore = !ignore2.contains(ejb);	
					if(evaluateToAddChild(ejb,isCascade,isAllow,isInPath,isParentsAllowed,isNotIgnore)) {result.add(ejb);}
				}
			}
		}
		
		if(debugOnInfo) {logger.info("Listing all allowed2 "+result.size()+" global:"+viewIsGlobal);}
		return result;
	}
}