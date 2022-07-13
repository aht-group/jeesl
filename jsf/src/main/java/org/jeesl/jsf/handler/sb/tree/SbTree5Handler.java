package org.jeesl.jsf.handler.sb.tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeesl.controller.handler.tree.TreeUpdateParameter;
import org.jeesl.interfaces.controller.handler.tree.bean.JeeslSbTreeBean;
import org.jeesl.interfaces.controller.handler.tree.cache.JeeslTree5Cache;
import org.jeesl.interfaces.controller.handler.tree.store.JeeslTree5Store;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SbTree5Handler <L1 extends EjbWithId, L2 extends EjbWithId, L3 extends EjbWithId, L4 extends EjbWithId, L5 extends EjbWithId> extends SbTree4Handler<L1,L2,L3,L4>
{
	final static Logger logger = LoggerFactory.getLogger(SbTree5Handler.class);
	
	private final JeeslTree5Store<L1,L2,L3,L4,L5> store5;
	private final JeeslTree5Cache<L1,L2,L3,L4,L5> cache5;
	
	protected final Set<L5> allowChild5;
	protected final Set<L5> allowPath5;
	protected final Set<L5> ignore5;
	
	protected final List<L5> list5; public List<L5> getList5() {return list5;}
	
	protected L5 l5; public L5 getL5(){return l5;} public void setL5(L5 l5){this.l5 = l5;}
	protected String xpath5; public String getXpath5() {return xpath5;}
	
	public SbTree5Handler(JeeslSbTreeBean callback, JeeslTree5Cache<L1,L2,L3,L4,L5> cache5, JeeslTree5Store<L1,L2,L3,L4,L5> store5)
	{
		super(callback,cache5,store5);
		this.cache5=cache5;
		this.store5=store5;
		
		list5 = new ArrayList<L5>();
		
		allowChild5 = new HashSet<L5>();
		allowPath5 = new HashSet<L5>();
		ignore5 = new HashSet<L5>();
		
		showLevel5 = true;
		xpath5 = "@id";
	}
	
	// Methods to reset the Selections
	protected void reset5() {reset5(false,false,false,false,true);}
	protected void reset5(boolean r1, boolean r2, boolean r3, boolean r4, boolean r5)
	{
		super.reset4(r1,r2,r3,r4);
		if(r5) {l5=null;}
	}
	
	// Adding Allowed elements, e.g. defined by a Security Context
	protected void addAllowedL5(List<L5> list)
	{
		for(L5 ejb : list)
		{
			if(!allowChild5.contains(ejb)) {allowChild5.add(ejb);}
			super.addAllowedPathL4(getParentForL5(ejb));
		}
	}
	protected void addAllowedPathL5(L5 ejb)
	{
		if(!allowPath5.contains(ejb)) {allowPath5.add(ejb);}
		super.addAllowedPathL4(getParentForL5(ejb));
	}
	
	// Default Selection from a Security Context
	protected void selectSecurity5()
	{
		if(debugOnInfo) {logger.info("Checking for Security Level 5 Select");}
		if(!allowChild5.isEmpty())
		{
			L5 ejb = new ArrayList<L5>(allowChild5).get(0);
			if(debugOnInfo) {logger.info("selectSecurity5 "+ejb.getClass().getSimpleName()+" "+ejb.toString());}
			cascade5(ejb,TreeUpdateParameter.build(true,true,true,true));
		}
		else {selectSecurity4();}
	}
	
	// Selection from UI and cascading of event
	public void uiSelect5(L5 ejb)
	{
//		if(store5!=null) {store5.setLastLevel(5);}
		cascade5(ejb,TreeUpdateParameter.build(false,true,true,true));
	}
	protected void cascade5(L5 ejb, TreeUpdateParameter tup)
	{
		if(debugOnInfo) {logger.info("cascade5 "+ejb.getClass().getSimpleName()+": ["+ejb.toString()+"] "+TreeUpdateParameter.class.getSimpleName()+": ["+tup.toString()+"]");}
		this.l5=ejb;
		if(store5!=null) {store5.storeTreeLevel5(ejb);}
		clearL6List();
		if(tup.isFillParent()) {cascade4(getParentForL5(l5),tup.copy().selectChild(false).callback(false));}
		if(tup.isFillChilds()) {fillL6List();}
		if(tup.isSelectChild()) {selectDefaultL6(tup.copy().fillParent(false).callback(false));}
		if(tup.isCallback() && callback!=null) {callback.sbTreeSelected();}
//		if(hup.isFireEvent()) {fireEvent();}
	}
	
	//Methods called from previous level and @Overriden here (clear,fill,select)
	@Override protected void clearL5List() {list5.clear();}
	@Override protected void fillL5List()
	{
		List<L5> childs = cache5.getCachedChildsForL4(l4);
		if(debugOnInfo) {logger.info("Filling Level-5-List, Checking "+childs.size()+" elements");}
		for(L5 ejb : childs)
		{
			L4 parent = getParentForL5(ejb);
			boolean isCascade = ejb.equals(l5);
			boolean isAllow = allowChild5.contains(ejb);
			boolean isInPath = allowPath5.contains(ejb);
			
			boolean isParentsAllowedChild = allowChild1.contains(getParentForL2(getParentForL3(getParentForL4(parent)))) || allowChild2.contains(getParentForL3(getParentForL4(parent))) || allowChild3.contains(getParentForL4(parent)) || allowChild4.contains(parent);
			boolean isParentsAllowed = isParentsAllowedChild;
			boolean isNotIgnore = !ignore5.contains(ejb);	
			if(evaluateToAddChild(ejb,isCascade,isAllow,isInPath,isParentsAllowed,isNotIgnore)) {list5.add(ejb);}
		}
	}
	@Override protected void selectDefaultL5(TreeUpdateParameter tup)
	{
		if(debugOnInfo) {logger.info("selectDefaultL5 "+tup.toString());}
		reset5();
		if(!list5.isEmpty()) {cascade5(list5.get(0),tup);}
	}
	
	//Methods need to be implemented in next Level
	protected L4 getParentForL5(L5 l5) {logger.warn("getParentForL4 "+SbTree1Handler.warnMessageOverrideImplementation);return null;}
	protected void clearL6List() {logger.warn(warnMessageOverrideNextLevel);}
	protected void fillL6List() {logger.warn(warnMessageOverrideNextLevel);}
	protected void selectDefaultL6(TreeUpdateParameter tup) {logger.warn(warnMessageOverrideNextLevel);}
	
	// Debugging
	public void debug(boolean debug)
	{
		if(debug)
		{
			super.debug(debug);
			StringBuilder sb = new StringBuilder();
			sb.append("\tLevel 5 ").append(list5.size()).append("elements ");
			if(l4!=null) {sb.append(l5.getClass().getSimpleName()).append(": ").append(l5.toString());}else {sb.append(": null");}
			logger.info(sb.toString());
		}
	}
	public void debugAllow4(boolean debug)
	{
		if(debug)
		{
			logger.info("Allow5: "+allowChild5.size()+" elements");
			for(L5 ejb : new ArrayList<L5>(allowChild5))
			{
				logger.info("\t"+ejb.toString());
			}		
		}
	}
}