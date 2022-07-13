package org.jeesl.jsf.handler.s1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeesl.controller.handler.tree.TreeUpdateParameter;
import org.jeesl.interfaces.controller.handler.tree.bean.JeeslS1TreeBean;
import org.jeesl.interfaces.controller.handler.tree.cache.JeeslTree3Cache;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectOne3Handler <L1 extends EjbWithId, L2 extends EjbWithId, L3 extends EjbWithId> extends SelectOne2Handler<L1,L2>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(SelectOne3Handler.class);
	
	private final JeeslTree3Cache<L1,L2,L3> cache3;
		
	protected boolean showLevel3; public boolean isShowLevel3() {return showLevel3;}
	
	protected final Set<L3> ignore3;
	protected final List<L3> list3; public List<L3> getList3() {return list3;}
	
	protected L3 l3; public L3 getL3() {return l3;} public void setL3(L3 l3) {this.l3 = l3;}
	protected String xpath3; public String getXpath3() {return xpath3;}
	
	public SelectOne3Handler(JeeslS1TreeBean callback, JeeslTree3Cache<L1,L2,L3> cache3)
	{
		super(callback,cache3);
		this.cache3=cache3;

		list3 = new ArrayList<L3>();
		ignore3 = new HashSet<L3>();
		
		showLevel3 = true;
		xpath3 = "@id";
	}
	
	protected void reset3() {reset3(false,false,true);}
	protected void reset3(boolean r1, boolean r2, boolean r3)
	{
		super.reset2(r1,r2);
		if(r3) {l3=null;}
	}
	
	// Adding Allowed elements, e.g. defined by a Security Context
	protected void addAllowedL3(List<L3> list)
	{
		for(L3 ejb : list)
		{

		}
	}
		
	protected void selectDefaultL3(TreeUpdateParameter tup)
	{
		if(debugOnInfo) {logger.info("selectDefaultL3 "+tup.toString());}
		reset3();
		if(!list2.isEmpty()) {cascade3(list3.get(0),tup);}
	}
	
	public void uiSelect3() {cascade3(l3,TreeUpdateParameter.build(false,true,true,true));}
	protected void cascade3(L3 ejb, TreeUpdateParameter tup)
	{
		l3 = ejb;
		if(debugOnInfo) {logger.info("cascade3 "+ejb.getClass().getSimpleName()+": ["+l3.toString()+"] "+TreeUpdateParameter.class.getSimpleName()+": ["+tup.toString()+"]");}
		
		clearL3List();
		
		if(tup.isFillParent()) {cascade2(getParentForL3(l3),tup.copy().selectChild(false).callback(false));}
		if(tup.isFillChilds()) {fillL4List();}
		if(tup.isSelectChild()) {selectDefaultL4(tup.copy().fillParent(false).callback(false));}
		if(tup.isCallback() && callback!=null) {callback.s1TreeSelected(this);}
	}
	
	@Override protected void fillL3List()
	{
		
		List<L3> childs = cache3.getCachedChildsForL2(l2);
		if(debugOnInfo) {logger.info("Filling Level-3-List, Checking "+childs.size()+" elements");}
		list3.clear();
		for(L3 ejb : childs)
		{
//			L1 parent = getParentForL2(ejb);
			boolean isCascade = ejb.equals(l3);
			boolean isAllow = true;//allowChild2.contains(ejb);
			boolean isInPath = true;//allowPath2.contains(ejb);
			boolean isParentsAllowed = true;//allowChild1.contains(parent);
			boolean isNotIgnore = !ignore2.contains(ejb);	
			if(evaluateToAddChild(ejb,isCascade,isAllow,isInPath,isParentsAllowed,isNotIgnore)) {list3.add(ejb);}
		}
	}
	
	//Methods need to be implemented in next Level
	protected L2 getParentForL3(L3 l3) {logger.warn("getParentForL3 "+SelectOne1Handler.warnMessageOverrideImplementation);return null;}
	protected void clearL4List() {logger.warn("clearL4List "+warnMessageOverrideNextLevel);}
	protected void fillL4List() {logger.warn("fillL4List "+warnMessageOverrideNextLevel);}
	protected void selectDefaultL4(TreeUpdateParameter tup) {logger.warn("selectDefaultL4 "+warnMessageOverrideNextLevel);}
}