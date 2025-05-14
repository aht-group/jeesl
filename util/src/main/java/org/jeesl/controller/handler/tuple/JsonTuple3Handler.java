package org.jeesl.controller.handler.tuple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.controller.processor.finance.AmountRounder;
import org.jeesl.factory.json.io.db.tuple.JsonTupleFactory;
import org.jeesl.interfaces.controller.report.JeeslComparatorProvider;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.io.db.tuple.JsonTuple;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples3;
import org.jeesl.model.json.io.db.tuple.instance.JsonTuple3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonTuple3Handler <A extends EjbWithId, B extends EjbWithId, C extends EjbWithId>
							extends JsonTuple2Handler<A,B> implements Serializable
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JsonTuple3Handler.class);

	private JeeslComparatorProvider<C> jppC; public void setComparatorProviderC(JeeslComparatorProvider<C> jppC) {this.jppC = jppC;}
	
	private final Class<C> cC; public Class<C> getClassC() {return cC;}

	protected final Map<Long,C> mapC;
	private final Map<A,Map<B,Map<C,JsonTuple3<A,B,C>>>> map3; public Map<A,Map<B,Map<C,JsonTuple3<A,B,C>>>> getMap3() {return map3;}
	
	private final List<C> listC; public List<C> getListC() {return listC;}
	private final List<JsonTuple3<A,B,C>> tuples3; public List<JsonTuple3<A,B,C>> getTuples3() {return tuples3;}

	private int sizeC; public int getSizeC() {return sizeC;}
	
	public static <A extends EjbWithId, B extends EjbWithId, C extends EjbWithId> JsonTuple3Handler<A,B,C> instance(Class<A> cA, Class<B> cB, Class<C> cC) {return new JsonTuple3Handler<>(cA,cB,cC);}
	public JsonTuple3Handler(Class<A> cA, Class<B> cB, Class<C> cC)
	{
		super(cA,cB);
		this.cC=cC;
		
		mapC = new HashMap<Long,C>();
		listC = new ArrayList<C>();
		map3 = new HashMap<A,Map<B,Map<C,JsonTuple3<A,B,C>>>>();
		tuples3 = new ArrayList<JsonTuple3<A,B,C>>();
		
		dimension = 3;
	}
	
	public void clear()
	{
		super.clear();
		map3.clear();
		mapC.clear();
		listC.clear();
		tuples3.clear();
	}
	
	public int size()
	{
		int size=0;
		for(A a : map3.keySet())
		{
			Map<B,Map<C,JsonTuple3<A,B,C>>> mB = map3.get(a);
			for(B b : mB.keySet())
			{
				Map<C,JsonTuple3<A,B,C>> mC = mB.get(b);
				size = size+mC.size();
			}
		}
		return size;
	}
	
	public JsonTuple3Handler<A,B,C> init(JsonTuples3<A,B,C> tuples) {init(null,tuples,false); return this;}
	public void init(JeeslFacade fJeesl, JsonTuples3<A,B,C> tuples, boolean load){init(fJeesl,tuples,true,true,true);}
	public void init(JeeslFacade fJeesl, JsonTuples3<A,B,C> tuples, boolean loadA, boolean loadB, boolean loadC)
	{
		this.clear();
	
		for(JsonTuple3<A,B,C> t : tuples.getTuples())
		{
			size++;
			if(t.getSum()!=null) {t.setSum(AmountRounder.two(t.getSum()/sumDivider));}
			
			super.initTupleA(t);
			super.initTupleB(t);
			this.initTupleC(t);
					
			if(!map3.containsKey(t.getEjb1())) {map3.put(t.getEjb1(), new HashMap<B,Map<C,JsonTuple3<A,B,C>>>());}
			if(!map3.get(t.getEjb1()).containsKey(t.getEjb2())) {map3.get(t.getEjb1()).put(t.getEjb2(), new HashMap<C,JsonTuple3<A,B,C>>());}
			map3.get(t.getEjb1()).get(t.getEjb2()).put(t.getEjb3(), t);
		}
	
		super.initListA(fJeesl);
		super.initListB(fJeesl);
		this.initListC(fJeesl);
		tuples3.addAll(tuples.getTuples());
	}
	
	protected void initTupleC(JsonTuple3<A,B,C> t)
	{
		if(t.getEjb3()==null)
		{
			if(mapC.containsKey(t.getId3())) {t.setEjb3(mapC.get(t.getId3()));}
			else
			{
				try{t.setEjb3(cC.newInstance()); t.getEjb3().setId(t.getId3());}
				catch (InstantiationException | IllegalAccessException e) {e.printStackTrace();}
			}
		}
		if(!mapC.containsKey(t.getId3())) {mapC.put(t.getId3(),t.getEjb3());}
	}
	
	public void reloadABC(JeeslFacade facade) {super.initListA(facade); super.initListB(facade); this.initListC(facade);}
	public void reloadBC(JeeslFacade facade) {super.initListB(facade); this.initListC(facade);}
	public void reloadC(JeeslFacade facade) {this.initListC(facade);}

	public void initListC(JeeslFacade facade)
	{
		if(facade==null){listC.addAll(mapC.values());}
		else {listC.clear(); listC.addAll(facade.find(cC, new ArrayList<>(mapC.keySet())));}
		
		sizeC = listC.size();
		if(jppC!=null && jppC.provides(cC)){Collections.sort(listC, jppC.provide(cC));}
	}
	
	public List<JsonTuple3<A,B,C>> toList(List<JsonTuple3<A,B,C>> list, A a, B b, C c)
	{
		List<JsonTuple3<A,B,C>> result = new ArrayList<>();
		if(ObjectUtils.isNotEmpty(list))
		{
			for(JsonTuple3<A,B,C> t : list)
			{
				if(t.getId1()==a.getId() && t.getId2()==b.getId() && t.getId3()== c.getId())
				{
					result.add(t);
				}
			}
		}
		return result;
	}
	
	public boolean contains(A a, B b, C c) {return map3.containsKey(a) && map3.get(a).containsKey(b) &&  map3.get(a).get(b).containsKey(c);}
	public JsonTuple value(A a, B b, C c) {return JsonTupleFactory.build(map3.get(a).get(b).get(c));}
	
	public void debug(boolean debug)
	{
		super.debug(debug);
		logger.info(cC.getSimpleName()+" "+listC.size());
	}
}