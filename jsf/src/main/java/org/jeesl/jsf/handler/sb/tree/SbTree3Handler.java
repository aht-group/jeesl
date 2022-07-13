package org.jeesl.jsf.handler.sb.tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeesl.controller.handler.tree.TreeUpdateParameter;
import org.jeesl.interfaces.controller.handler.tree.bean.JeeslTreeSelected;
import org.jeesl.interfaces.controller.handler.tree.cache.JeeslTree3Cache;
import org.jeesl.interfaces.controller.handler.tree.store.JeeslTree3Store;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SbTree3Handler <L1 extends EjbWithId, L2 extends EjbWithId, L3 extends EjbWithId> extends SbTree2Handler<L1,L2>
{
	final static Logger logger = LoggerFactory.getLogger(SbTree3Handler.class);
	
	private final JeeslTree3Store<L1,L2,L3> store3;
	private final JeeslTree3Cache<L1,L2,L3> cache3;

	protected final Set<L3> allowChild3;
	protected final Set<L3> allowPath3;
	protected final Set<L3> ignore3;
	
	protected final List<L3> list3; public List<L3> getList3() {return list3;}
	
	protected L3 l3; public L3 getL3(){return l3;} public void setL3(L3 l3){this.l3 = l3;}
	protected String xpath3; public String getXpath3() {return xpath3;}
	
	public SbTree3Handler(JeeslTreeSelected callback, JeeslTree3Cache<L1,L2,L3> cache3, JeeslTree3Store<L1,L2,L3> store3)
	{
		super(callback,cache3,store3);
		this.cache3=cache3;
		this.store3=store3;
		
		list3 = new ArrayList<L3>();
		
		allowChild3 = new HashSet<L3>();
		allowPath3 = new HashSet<L3>();
		ignore3 = new HashSet<L3>();
		
		showLevel3 = true;
		xpath3 = "@id";
	}
	
	// Methods to reset the Selections
	protected void reset3() {reset3(false,false,true);}
	protected void reset3(boolean r1, boolean r2, boolean r3)
	{
		super.reset2(r1,r2);
		if(r3) {l3=null;}
	}
	
	// Adding Allowed elements, e.g. defined by a Security Context
	protected void addAllowedL3(List<L3> list)
	{
		if(debugOnInfo) {logger.info("Adding "+list.size()+" to AllowChild3");}
		for(L3 ejb : list)
		{
			if(!allowChild3.contains(ejb))
			{
				if(debugOnInfo) {logger.info("\t"+ejb.toString());}
				allowChild3.add(ejb);
			}
			super.addAllowedPathL2(getParentForL3(ejb));
		}
	}
	protected void addAllowedPathL3(L3 ejb)
	{
		if(!allowPath3.contains(ejb)) {allowPath3.add(ejb);}
		super.addAllowedPathL2(getParentForL3(ejb));
	}
	
	// Default Selection from a Security Context
	protected void selectSecurity3()
	{
		if(debugOnInfo) {logger.info("Checking for Security Level 3 Select");}
		if(!allowChild3.isEmpty())
		{
			L3 ejb = new ArrayList<L3>(allowChild3).get(0);
			if(debugOnInfo) {logger.info("selectSecurity3 "+ejb.getClass().getSimpleName()+" "+ejb.toString());}
			cascade3(ejb,TreeUpdateParameter.build(true,true,true,true,true));
		}
		else {selectSecurity2();}
	}
	
	// Selection from UI and cascading of event
	public void uiSelect3(L3 ejb) {cascade3(ejb,TreeUpdateParameter.build(false,true,true,true,true));}
	protected void cascade3(L3 ejb, TreeUpdateParameter tup)
	{
		if(debugOnInfo) {logger.info("cascade3 "+ejb.getClass().getSimpleName()+": ["+ejb.toString()+"] "+TreeUpdateParameter.class.getSimpleName()+": ["+tup.toString()+"]");}
		this.l3=ejb;
		if(store3!=null) {store3.storeTreeLevel3(ejb);}
		clearL4List();
		if(tup.isFillParent()) {cascade2(getParentForL3(l3),tup.copy().selectChild(false).callback(false));}
		if(tup.isFillChilds()) {fillL4List();}
		if(tup.isSelectChild()) {selectDefaultL4(tup.copy().fillParent(false).callback(false));}
		if(tup.isCallback() && callback!=null) {callback.sbTreeSelected();}
//		if(hup.isFireEvent()) {fireEvent();}
	}
	
	//Methods called from previous level and @Overriden here (clear,fill,select)
	@Override protected void clearL3List() {list3.clear();}
	@Override protected void fillL3List()
	{
		List<L3> childs = cache3.getCachedChildsForL2(l2);
		if(debugOnInfo) {logger.info("Filling Level-3-List, Checking "+childs.size()+" elements");}
		for(L3 ejb : childs)
		{
			L2 parent = getParentForL3(ejb);
			boolean isCascade = ejb.equals(l3);
			boolean isAllow = allowChild3.contains(ejb);
			boolean isInPath = allowPath3.contains(ejb);
			
			boolean isParentsAllowedChild = allowChild1.contains(getParentForL2(parent)) || allowChild2.contains(parent);
			boolean isParentsAllowed = isParentsAllowedChild;
			boolean isNotIgnore = !ignore3.contains(ejb);	
			if(evaluateToAddChild(ejb,isCascade,isAllow,isInPath,isParentsAllowed,isNotIgnore)) {list3.add(ejb);}
		}
	}
	@Override protected void selectDefaultL3(TreeUpdateParameter tup)
	{
		if(debugOnInfo) {logger.info("selectDefaultL3 "+tup.toString());}
		reset3();
		if(!list3.isEmpty()) {cascade3(list3.get(0),tup);}
	}
	
	//Methods need to be implemented in next Level
	protected L2 getParentForL3(L3 l3) {logger.warn("getParentForL3 "+SbTree1Handler.warnMessageOverrideImplementation);return null;}
	protected void clearL4List() {logger.warn(warnMessageOverrideNextLevel);}
	protected void fillL4List() {logger.warn(warnMessageOverrideNextLevel);}
	protected void selectDefaultL4(TreeUpdateParameter tup) {logger.warn(warnMessageOverrideNextLevel);}
	
	// Debugging
	public void debug(boolean debug)
	{
		if(debug)
		{
			super.debug(debug);
			StringBuilder sb = new StringBuilder();
			sb.append("\tLevel 3 ").append(list3.size()).append("elements ");
			if(l3!=null) {sb.append(l3.getClass().getSimpleName()).append(": ").append(l3.toString());}else {sb.append(": null");}
			logger.info(sb.toString());
		}
	}
	public void debugAllow3(boolean debug)
	{
		if(debug)
		{
			logger.info("Allow3: "+allowChild2.size()+" elements");
			for(L3 ejb : new ArrayList<L3>(allowChild3))
			{
				logger.info("\t"+ejb.toString());
			}		
		}
	}
}