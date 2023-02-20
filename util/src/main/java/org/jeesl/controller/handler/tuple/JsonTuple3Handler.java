package org.jeesl.controller.handler.tuple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.controller.processor.finance.AmountRounder;
import org.jeesl.factory.json.io.db.tuple.JsonTupleFactory;
import org.jeesl.interfaces.controller.report.JeeslComparatorProvider;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.db.tuple.t3.Json3Tuple;
import org.jeesl.model.json.db.tuple.t3.Json3Tuples;
import org.jeesl.model.json.io.db.tuple.JsonTuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonTuple3Handler <A extends EjbWithId, B extends EjbWithId, C extends EjbWithId>
							extends JsonTuple2Handler<A,B> implements Serializable
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JsonTuple3Handler.class);

	private JeeslComparatorProvider<C> jppC; public void setComparatorProviderC(JeeslComparatorProvider<C> jppC) {this.jppC = jppC;}
	
	private final Class<C> cC;
	
	protected final Map<Long,C> mapC;
	private final Map<A,Map<B,Map<C,Json3Tuple<A,B,C>>>> map3; public Map<A,Map<B,Map<C,Json3Tuple<A,B,C>>>> getMap3() {return map3;}
	
	private final List<C> listC; public List<C> getListC() {return listC;}
	private final List<Json3Tuple<A,B,C>> tuples3; public List<Json3Tuple<A,B,C>> getTuples3() {return tuples3;}

	private int sizeC; public int getSizeC() {return sizeC;}
	
	public static <A extends EjbWithId, B extends EjbWithId, C extends EjbWithId> JsonTuple3Handler<A,B,C> instance(Class<A> cA, Class<B> cB, Class<C> cC) {return new JsonTuple3Handler<>(cA,cB,cC);}
	public JsonTuple3Handler(Class<A> cA, Class<B> cB, Class<C> cC)
	{
		super(cA,cB);
		this.cC=cC;
		
		mapC = new HashMap<Long,C>();
		listC = new ArrayList<C>();
		map3 = new HashMap<A,Map<B,Map<C,Json3Tuple<A,B,C>>>>();
		tuples3 = new ArrayList<Json3Tuple<A,B,C>>();
		
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
	
	public JsonTuple3Handler<A,B,C> init(Json3Tuples<A,B,C> tuples) {init(null,tuples,false); return this;}
	public void init(JeeslFacade fJeesl, Json3Tuples<A,B,C> tuples, boolean load){init(fJeesl,tuples,true,true,true);}
	public void init(JeeslFacade fJeesl, Json3Tuples<A,B,C> tuples, boolean loadA, boolean loadB, boolean loadC)
	{
		clear();
	
		for(Json3Tuple<A,B,C> t : tuples.getTuples())
		{
			size++;
			if(t.getSum()!=null) {t.setSum(AmountRounder.two(t.getSum()/sumDivider));}
			
			if(t.getEjb1()==null)
			{
				if(mapA.containsKey(t.getId1())) {t.setEjb1(mapA.get(t.getId1()));}
				else
				{
					try{t.setEjb1(cA.newInstance()); t.getEjb1().setId(t.getId1());}
					catch (InstantiationException | IllegalAccessException e) {e.printStackTrace();}
				}
			}
			if(t.getEjb2()==null)
			{
				if(mapB.containsKey(t.getId2())) {t.setEjb2(mapB.get(t.getId2()));}
				else
				{
					try{t.setEjb2(cB.newInstance()); t.getEjb2().setId(t.getId2());}
					catch (InstantiationException | IllegalAccessException e) {e.printStackTrace();}
				}
			}
			if(t.getEjb3()==null)
			{
				if(mapC.containsKey(t.getId3())) {t.setEjb3(mapC.get(t.getId3()));}
				else
				{
					try{t.setEjb3(cC.newInstance()); t.getEjb3().setId(t.getId3());}
					catch (InstantiationException | IllegalAccessException e) {e.printStackTrace();}
				}
			}
			
			if(!mapA.containsKey(t.getId1())) {mapA.put(t.getId1(),t.getEjb1());}
			if(!mapB.containsKey(t.getId2())) {mapB.put(t.getId2(),t.getEjb2());}
			if(!mapC.containsKey(t.getId3())) {mapC.put(t.getId3(),t.getEjb3());}
				
			if(!map3.containsKey(t.getEjb1())) {map3.put(t.getEjb1(), new HashMap<B,Map<C,Json3Tuple<A,B,C>>>());}
			if(!map3.get(t.getEjb1()).containsKey(t.getEjb2())) {map3.get(t.getEjb1()).put(t.getEjb2(), new HashMap<C,Json3Tuple<A,B,C>>());}
			map3.get(t.getEjb1()).get(t.getEjb2()).put(t.getEjb3(), t);
		}
	
		super.initListA(fJeesl);
		super.initListB(fJeesl);
		this.initListC(fJeesl);
		tuples3.addAll(tuples.getTuples());
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
	
	public List<Json3Tuple<A,B,C>> toList(List<Json3Tuple<A,B,C>> list, A a, B b, C c)
	{
		List<Json3Tuple<A,B,C>> result = new ArrayList<>();
		for(Json3Tuple<A,B,C> t : list)
		{
			if(t.getId1()==a.getId() && t.getId2()==b.getId() && t.getId3()== c.getId())
			{
				result.add(t);
			}
		}
		return result;
	}
	
	public boolean contains(A a, B b, C c) {return map3.containsKey(a) && map3.get(a).containsKey(b) &&  map3.get(a).get(b).containsKey(c);}
	
	public JsonTuple value(A a, B b, C c)
	{
		Json3Tuple<A,B,C> json = map3.get(a).get(b).get(c);
		return JsonTupleFactory.build(json);
	}
}