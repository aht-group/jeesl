package org.jeesl.jsf.handler.sb.tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeesl.controller.handler.tree.TreeUpdateParameter;
import org.jeesl.interfaces.controller.handler.tree.bean.JeeslTreeSelected;
import org.jeesl.interfaces.controller.handler.tree.cache.JeeslTree4Cache;
import org.jeesl.interfaces.controller.handler.tree.store.JeeslTree4Store;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SbTree4Handler <L1 extends EjbWithId, L2 extends EjbWithId, L3 extends EjbWithId, L4 extends EjbWithId> extends SbTree3Handler<L1,L2,L3>
{
	final static Logger logger = LoggerFactory.getLogger(SbTree4Handler.class);
	
	private final JeeslTree4Store<L1,L2,L3,L4> store4;
	private final JeeslTree4Cache<L1,L2,L3,L4> cache4;
	
	
	
	protected final Set<L4> allowChild4;
	protected final Set<L4> allowPath4;
	protected final Set<L4> ignore4;
	
	protected final List<L4> list4; public List<L4> getList4() {return list4;}
	
	protected L4 l4; public L4 getL4(){return l4;} public void setL4(L4 l4){this.l4 = l4;}
	protected String xpath4; public String getXpath4() {return xpath4;}
	
	public SbTree4Handler(JeeslTreeSelected callback, JeeslTree4Cache<L1,L2,L3,L4> cache4, JeeslTree4Store<L1,L2,L3,L4> store4)
	{
		super(callback,cache4,store4);
		this.cache4=cache4;
		this.store4=store4;
		
		list4 = new ArrayList<L4>();
		
		allowChild4 = new HashSet<L4>();
		allowPath4 = new HashSet<L4>();
		ignore4 = new HashSet<L4>();
		
		showLevel4 = true;
		xpath4 = "@id";
	}
	
	// Methods to reset the Selections
	protected void reset4() {reset4(false,false,false,true);}
	protected void reset4(boolean r1, boolean r2, boolean r3, boolean r4)
	{
		super.reset3(r1,r2,r3);
		if(r4) {l4=null;}
	}
	
	// Adding Allowed elements, e.g. defined by a Security Context
	protected void addAllowedL4(List<L4> list)
	{
		for(L4 ejb : list)
		{
			if(!allowChild4.contains(ejb)) {allowChild4.add(ejb);}
			super.addAllowedPathL3(getParentForL4(ejb));
		}
	}
	protected void addAllowedPathL4(L4 ejb)
	{
		if(!allowPath4.contains(ejb)) {allowPath4.add(ejb);}
		super.addAllowedPathL3(getParentForL4(ejb));
	}
	
	// Default Selection from a Security Context
	protected void selectSecurity4()
	{
		if(debugOnInfo) {logger.info("Checking for Security Level 4 Select");}
		if(!allowChild4.isEmpty())
		{
			L4 ejb = new ArrayList<L4>(allowChild4).get(0);
			if(debugOnInfo) {logger.info("selectSecurity4 "+ejb.getClass().getSimpleName()+" "+ejb.toString());}
			cascade4(ejb,TreeUpdateParameter.build(true,true,true,true));
		}
		else {selectSecurity3();}
	}
	
	// Selection from UI and cascading of event
	public void uiSelect4(L4 ejb) {cascade4(ejb,TreeUpdateParameter.build(false,true,true,true));}
	protected void cascade4(L4 ejb, TreeUpdateParameter tup)
	{
		if(debugOnInfo) {logger.info("cascade4 "+ejb.getClass().getSimpleName()+": ["+ejb.toString()+"] "+TreeUpdateParameter.class.getSimpleName()+": ["+tup.toString()+"]");}
		this.l4=ejb;
		if(store4!=null) {store4.storeTreeLevel4(ejb);}
		clearL5List();
		if(tup.isFillParent()) {cascade3(getParentForL4(l4),tup.copy().selectChild(false).callback(false));}
		if(tup.isFillChilds()) {fillL5List();}
		if(tup.isSelectChild()) {selectDefaultL5(tup.copy().fillParent(false).callback(false));}
		if(tup.isCallback() && callback!=null) {callback.sbTreeSelected();}
//		if(hup.isFireEvent()) {fireEvent();}
	}
	
	//Methods called from previous level and @Overriden here (clear,fill,select)
	@Override protected void clearL4List() {list4.clear();}
	@Override protected void fillL4List()
	{
		List<L4> childs = cache4.getCachedChildsForL3(l3);
		if(debugOnInfo) {logger.info("Filling Level-4-List, Checking "+childs.size()+" elements");}
		for(L4 ejb : childs)
		{
			L3 parent = getParentForL4(ejb);
			boolean isCascade = ejb.equals(l4);
			boolean isAllow = allowChild4.contains(ejb);
			boolean isInPath = allowPath4.contains(ejb);
			
			boolean isParentsAllowedChild = allowChild1.contains(getParentForL2(getParentForL3(parent))) || allowChild2.contains(getParentForL3(parent)) || allowChild3.contains(parent);
			boolean isParentsAllowed = isParentsAllowedChild;
			boolean isNotIgnore = !ignore4.contains(ejb);	
			if(evaluateToAddChild(ejb,isCascade,isAllow,isInPath,isParentsAllowed,isNotIgnore)) {list4.add(ejb);}
		}
	}
	@Override protected void selectDefaultL4(TreeUpdateParameter tup)
	{
		if(debugOnInfo) {logger.info("selectDefaultL4 "+tup.toString());}
		reset4();
		if(!list4.isEmpty()) {cascade4(list4.get(0),tup);}
	}
	
	//Methods need to be implemented in next Level
	protected L3 getParentForL4(L4 l4) {logger.warn("getParentForL4 "+SbTree1Handler.warnMessageOverrideImplementation);return null;}
	protected void clearL5List() {logger.warn(warnMessageOverrideNextLevel);}
	protected void fillL5List() {logger.warn(warnMessageOverrideNextLevel);}
	protected void selectDefaultL5(TreeUpdateParameter tup) {logger.warn(warnMessageOverrideNextLevel);}
	
	// Debugging
	public void debug(boolean debug)
	{
		if(debug)
		{
			super.debug(debug);
			StringBuilder sb = new StringBuilder();
			sb.append("\tLevel 4 ").append(list4.size()).append("elements ");
			if(l4!=null) {sb.append(l4.getClass().getSimpleName()).append(": ").append(l4.toString());}else {sb.append(": null");}
			logger.info(sb.toString());
		}
	}
	public void debugAllow4(boolean debug)
	{
		if(debug)
		{
			logger.info("Allow4: "+allowChild2.size()+" elements");
			for(L4 ejb : new ArrayList<L4>(allowChild4))
			{
				logger.info("\t"+ejb.toString());
			}		
		}
	}
}