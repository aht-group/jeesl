package org.jeesl.jsf.handler.s1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeesl.controller.handler.tree.TreeUpdateParameter;
import org.jeesl.interfaces.controller.handler.tree.bean.JeeslS1TreeBean;
import org.jeesl.interfaces.controller.handler.tree.cache.JeeslTree1Cache;
import org.jeesl.interfaces.controller.handler.tree.cache.JeeslTree2Cache;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.tree.SbTree1Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectOne2Handler <L1 extends EjbWithId, L2 extends EjbWithId> extends SelectOne1Handler<L1>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(SelectOne2Handler.class);
	
	private final JeeslTree2Cache<L1,L2> cache2;
		
	protected boolean showLevel2; public boolean isShowLevel2() {return showLevel2;}
	
	protected final Set<L2> ignore2;
	protected final List<L2> list2; public List<L2> getList2() {return list2;}
	
	protected L2 l2; public L2 getL2() {return l2;} public void setL2(L2 l2) {this.l2 = l2;}
	protected String xpath2; public String getXpath2() {return xpath2;}
	
	public SelectOne2Handler(JeeslS1TreeBean callback, JeeslTree2Cache<L1,L2> cache2)
	{
		super(callback,cache2);
		this.cache2=cache2;

		list2 = new ArrayList<L2>();
		ignore2 = new HashSet<L2>();
		
		showLevel2 = true;
		xpath2 = "@id";
	}
	
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

		}
	}
		
	@Override protected void selectDefaultL2(TreeUpdateParameter tup)
	{
		if(debugOnInfo) {logger.info("selectDefaultL2 "+tup.toString());}
		reset2();
		if(!list2.isEmpty()) {cascade2(list2.get(0),tup);}
	}
	
	public void uiSelect2() {cascade2(l2,TreeUpdateParameter.build(false,true,true,true));}
	protected void cascade2(L2 ejb, TreeUpdateParameter tup)
	{
		l2 = ejb;
		if(debugOnInfo) {logger.info("cascade2 "+ejb.getClass().getSimpleName()+": ["+l1.toString()+"] "+TreeUpdateParameter.class.getSimpleName()+": ["+tup.toString()+"]");}
		
		clearL3List();
		
		if(tup.isFillParent()) {cascade1(getParentForL2(l2),tup.copy().selectChild(false).callback(false));}
		if(tup.isFillChilds()) {fillL3List();}
		if(tup.isSelectChild()) {selectDefaultL3(tup.copy().fillParent(false).callback(false));}
		if(tup.isCallback() && callback!=null) {callback.s1TreeSelected(this);}
	}
	
	@Override protected void fillL2List()
	{
		
		List<L2> childs = cache2.getCachedChildsForL1(l1);
		if(debugOnInfo) {logger.info("Filling Level-2-List, Checking "+childs.size()+" elements");}
		list2.clear();
		for(L2 ejb : childs)
		{
//			L1 parent = getParentForL2(ejb);
			boolean isCascade = ejb.equals(l2);
			boolean isAllow = true;//allowChild2.contains(ejb);
			boolean isInPath = true;//allowPath2.contains(ejb);
			boolean isParentsAllowed = true;//allowChild1.contains(parent);
			boolean isNotIgnore = !ignore2.contains(ejb);	
			if(evaluateToAddChild(ejb,isCascade,isAllow,isInPath,isParentsAllowed,isNotIgnore)) {list2.add(ejb);}
		}
	}
	
	//Methods need to be implemented in next Level
	protected L1 getParentForL2(L2 l2) {logger.warn("getParentForL2 "+SelectOne1Handler.warnMessageOverrideImplementation);return null;}
	protected void clearL3List() {logger.warn("clearL3List "+warnMessageOverrideNextLevel);}
	protected void fillL3List() {logger.warn("fillL3List "+warnMessageOverrideNextLevel);}
	protected void selectDefaultL3(TreeUpdateParameter tup) {logger.warn("selectDefaultL3 "+warnMessageOverrideNextLevel);}
}