package org.jeesl.controller.handler.tuple;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections4.ListUtils;
import org.jeesl.controller.processor.finance.AmountRounder;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.factory.json.io.db.tuple.JsonTupleFactory;
import org.jeesl.interfaces.controller.report.JeeslComparatorProvider;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.io.db.tuple.JsonTuple;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples2;
import org.jeesl.model.json.io.db.tuple.instance.JsonTuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonTuple2Handler <A extends EjbWithId, B extends EjbWithId>
							extends JsonTuple1Handler<A>
							implements Serializable
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JsonTuple2Handler.class);

	private JeeslComparatorProvider<B> jcpB; public void setComparatorProviderB(JeeslComparatorProvider<B> jppB) {this.jcpB = jppB;}

	protected final Class<B> cB; public Class<B> getClassB() {return cB;}

	protected final Map<Long,B> mapB;
	private final Map<A,Map<B,JsonTuple2<A,B>>> map;
	public Map<A,Map<B,JsonTuple2<A,B>>> getMap() {return map;}	//Deprecated
	public Map<A,Map<B,JsonTuple2<A,B>>> getMap2() {return map;} //Deprecated
	public Map<A,Map<B,JsonTuple2<A,B>>> getMapB() {return map;}
	
	private Map<A,Map<B,List<JsonTuple2<A,B>>>> mapListB; public Map<A,Map<B,List<JsonTuple2<A,B>>>> getMapListB() {return mapListB;}
	
	private final List<B> listB; public List<B> getListB() {return listB;}
	private final List<JsonTuple2<A,B>> tuples2; public List<JsonTuple2<A,B>> getTuples2() {return tuples2;}
	
	private boolean withList; public JsonTuple2Handler<A,B> withList(boolean value) {this.withList=value; return this;}
	private int sizeB; public int getSizeB() {return sizeB;}
	
	public static <A extends EjbWithId, B extends EjbWithId> JsonTuple2Handler<A,B> instance(Class<A> cA, Class<B> cB) {return new JsonTuple2Handler<>(cA,cB);}
	public JsonTuple2Handler(Class<A> cA, Class<B> cB)
	{
		super(cA);
		this.cB=cB;
		
		mapB = new HashMap<Long,B>();
		listB = new ArrayList<B>();
		map = new HashMap<A,Map<B,JsonTuple2<A,B>>>();
		tuples2 = new ArrayList<JsonTuple2<A,B>>();
		
		dimension = 2;
	}
	
	public void clear()
	{
		super.clear();
		map.clear();
		mapB.clear();
		listB.clear();
	}
	public void clearFrom(A a)
	{
		if(mapA.containsKey(a.getId())) {mapA.remove(a.getId());}
		if(map.containsKey(a)) {map.remove(a);}
	}
	
	public JsonTuple2Handler<A,B> load(JsonTuples2<A,B> tuples)
	{
		this.clear();
		this.append(tuples);
		initListA(null);
		initListB(null);
		tuples2.addAll(tuples.getTuples());
		return this;
	}
	
	public void append(JsonTuples2<A,B> tuples)
	{
		if(Objects.nonNull(tuples))
		{
			for(JsonTuple2<A,B> t : tuples.getTuples())
			{
				size++;
				if(t.getSum()!=null) {t.setSum(AmountRounder.two(t.getSum()/sumDivider));}
				if(t.getSum1()!=null) {t.setSum1(AmountRounder.two(t.getSum1()/sumDivider));}
				if(t.getSum2()!=null) {t.setSum2(AmountRounder.two(t.getSum2()/sumDivider));}
				if(t.getSum3()!=null) {t.setSum3(AmountRounder.two(t.getSum3()/sumDivider));}
				if(Objects.nonNull(t.getSum4())) {t.setSum4(AmountRounder.two(t.getSum4()/sumDivider));}
				
				if(t.getEjb1()==null)
				{
					if(mapA.containsKey(t.getId1())) {t.setEjb1(mapA.get(t.getId1()));}
					else
					{
						try{t.setEjb1(cA.getDeclaredConstructor().newInstance()); t.getEjb1().setId(t.getId1());}
						catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {e.printStackTrace();}
					}
				}
				if(t.getEjb2()==null)
				{
					if(mapB.containsKey(t.getId2())) {t.setEjb2(mapB.get(t.getId2()));}
					else
					{
						if(t.getId2()!=null)
						{
							try{t.setEjb2(cB.getDeclaredConstructor().newInstance()); t.getEjb2().setId(t.getId2());}
							catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {e.printStackTrace();}
						}
					}
				}
				
				if(!mapA.containsKey(t.getId1())) {mapA.put(t.getId1(),t.getEjb1());}
				if(!mapB.containsKey(t.getId2())) {mapB.put(t.getId2(),t.getEjb2());}
				
				if(!map.containsKey(t.getEjb1())) {map.put(t.getEjb1(), new HashMap<>());}
				map.get(t.getEjb1()).put(t.getEjb2(), t);
				
				if(withList)
				{
					if(Objects.isNull(mapListB)) {mapListB = new HashMap<>();}
					if(!mapListB.containsKey(t.getEjb1())) {mapListB.put(t.getEjb1(), new HashMap<>());}
					if(!mapListB.get(t.getEjb1()).containsKey(t.getEjb2())) {mapListB.get(t.getEjb1()).put(t.getEjb2(), new ArrayList<>());}
					mapListB.get(t.getEjb1()).get(t.getEjb2()).add(t);
				}
			}
		}
	}
	
	public void init(JsonTuples2<A,B> tuples, JeeslFacade fUtils, boolean loadA, boolean loadB)
	{
		clear();
		Set<Long> setIdA = new HashSet<>();
		Set<Long> setIdB = new HashSet<>();
		
		for(JsonTuple2<A,B> t : tuples.getTuples())
		{
			setIdA.add(t.getId1());
			setIdB.add(t.getId2());
		}
		
		Map<Long,A> mapA = null;
		Map<Long,B> mapB = null; 
		
		if(loadA) {mapA = EjbIdFactory.toIdMap(fUtils.find(cA, setIdA));}
		if(loadB) {mapB = EjbIdFactory.toIdMap(fUtils.find(cB, setIdB));}
		
		for(JsonTuple2<A,B> t : tuples.getTuples())
		{
			try
			{
				if(loadA){t.setEjb1(mapA.get(t.getId1()));}
				else
				{
					A a = cA.getDeclaredConstructor().newInstance();
					a.setId(t.getId1());
					t.setEjb1(a);
				}
				
				if(loadB){t.setEjb2(mapB.get(t.getId2()));}
				else
				{
					B b = cB.getDeclaredConstructor().newInstance();
					b.setId(t.getId2());
					t.setEjb2(b);
				}
			}
			catch (InstantiationException e) {e.printStackTrace();}
			catch (IllegalAccessException e) {e.printStackTrace();}
			catch (IllegalArgumentException e) {e.printStackTrace();}
			catch (InvocationTargetException e) {e.printStackTrace();}
			catch (NoSuchMethodException e) {e.printStackTrace();}
			catch (SecurityException e) {e.printStackTrace();}
		}
		load(tuples);
	}
	
	protected void initTupleB(JsonTuple2<A,B> t)
	{
		if(t.getEjb2()==null)
		{
			if(mapB.containsKey(t.getId2())) {t.setEjb2(mapB.get(t.getId2()));}
			else
			{
				try{t.setEjb2(cB.getDeclaredConstructor().newInstance()); t.getEjb2().setId(t.getId2());}
				catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {e.printStackTrace();}
			}
		}
		if(!mapB.containsKey(t.getId2())) {mapB.put(t.getId2(),t.getEjb2());}
	}
	
	public void initListB(JeeslFacade fJeesl)
	{
		if(Objects.isNull(fJeesl)) {listB.addAll(mapB.values());}
		else
		{
			listB.clear();
			
			int pSize = 5000;
			AtomicInteger i = new AtomicInteger(1);
			for(List<Long> partition : ListUtils.partition(new ArrayList<Long>(mapB.keySet()),pSize))
			{
				logger.info("Loading "+i.getAndIncrement()+" ("+listB.size()+"/"+mapB.size()+") "+cB.getSimpleName());
				listB.addAll(fJeesl.find(cB,new ArrayList<>(partition)));
			}
		}
		
		sizeB = listB.size();
		if(jcpB!=null && jcpB.provides(cB)){Collections.sort(listB, jcpB.provide(cB));}
	}
	
	public boolean contains(A a, B b) {return map.containsKey(a) && map.get(a).containsKey(b);}
	
	public JsonTuple value(A a, B b)
	{
		JsonTuple2<A,B> json = map.get(a).get(b);
		return JsonTupleFactory.build(json);
	}
	
	public double sum(A a, B b) {return map.get(a).get(b).getSum();}
	public double sum1(A a, B b) {return map.get(a).get(b).getSum1();}
	
	public void debug(boolean debug)
	{
		super.debug(debug);
		logger.info(cB.getSimpleName()+" "+listB.size());
	}
	
	public int size()
	{
		int size=0;
		for(A a : map.keySet())
		{
			size = size + map.get(a).size();
		}
		return size;
	}
}