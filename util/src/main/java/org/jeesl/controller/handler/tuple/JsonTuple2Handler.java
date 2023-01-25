package org.jeesl.controller.handler.tuple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.jeesl.controller.processor.finance.AmountRounder;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.factory.json.system.io.db.tuple.JsonTupleFactory;
import org.jeesl.interfaces.controller.report.JeeslComparatorProvider;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.db.tuple.JsonTuple;
import org.jeesl.model.json.db.tuple.two.Json2Tuple;
import org.jeesl.model.json.db.tuple.two.Json2Tuples;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.JsonUtil;

public class JsonTuple2Handler <A extends EjbWithId, B extends EjbWithId>
							extends JsonTuple1Handler<A>
							implements Serializable
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JsonTuple2Handler.class);

	private JeeslComparatorProvider<B> jcpB; public void setComparatorProviderB(JeeslComparatorProvider<B> jppB) {this.jcpB = jppB;}

	protected final Class<B> cB;
	
	protected final Map<Long,B> mapB;
	private final Map<A,Map<B,Json2Tuple<A,B>>> map; public Map<A,Map<B,Json2Tuple<A,B>>> getMap() {return map;}
	public Map<A,Map<B,Json2Tuple<A,B>>> getMap2() {return map;}
	
	private final List<B> listB; public List<B> getListB() {return listB;}
	private final List<Json2Tuple<A,B>> tuples2; public List<Json2Tuple<A,B>> getTuples2() {return tuples2;}
	
	private int sizeB; public int getSizeB() {return sizeB;}
	
	public JsonTuple2Handler(Class<A> cA, Class<B> cB)
	{
		super(cA);
		this.cB=cB;
		
		mapB = new HashMap<Long,B>();
		listB = new ArrayList<B>();
		map = new HashMap<A,Map<B,Json2Tuple<A,B>>>();
		tuples2 = new ArrayList<Json2Tuple<A,B>>();
		
		dimension = 2;
	}
	
	public void clear()
	{
		super.clear();
		map.clear();
		mapB.clear();
		listB.clear();
	}

	public void init(Json2Tuples<A,B> tuples, JeeslFacade fUtils, boolean loadA, boolean loadB)
	{
		clear();
		Set<Long> setIdA = new HashSet<>();
		Set<Long> setIdB = new HashSet<>();
		
		for(Json2Tuple<A,B> t : tuples.getTuples())
		{
			setIdA.add(t.getId1());
			setIdB.add(t.getId2());
		}
		
		Map<Long,A> mapA = null;
		Map<Long,B> mapB = null; 
		
		if(loadA) {mapA = EjbIdFactory.toIdMap(fUtils.find(cA, setIdA));}
		if(loadB) {mapB = EjbIdFactory.toIdMap(fUtils.find(cB, setIdB));}
		
		for(Json2Tuple<A,B> t : tuples.getTuples())
		{
			try
			{
				if(loadA){t.setEjb1(mapA.get(t.getId1()));}
				else
				{
					A a = cA.newInstance();
					a.setId(t.getId1());
					t.setEjb1(a);
				}
				
				if(loadB){t.setEjb2(mapB.get(t.getId2()));}
				else
				{
					B b = cB.newInstance();
					b.setId(t.getId2());
					t.setEjb2(b);
				}
			}
			catch (InstantiationException e) {e.printStackTrace();}
			catch (IllegalAccessException e) {e.printStackTrace();}
		}
		init(tuples);
		
	}
	public void init(Json2Tuples<A,B> tuples) {init(null,tuples);}
	public void init(JeeslFacade fJeesl, Json2Tuples<A,B> tuples)
	{
		clear();
	
		for(Json2Tuple<A,B> t : tuples.getTuples())
		{
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
					try{t.setEjb1(cA.newInstance()); t.getEjb1().setId(t.getId1());}
					catch (InstantiationException | IllegalAccessException e) {e.printStackTrace();}
				}
			}
			if(t.getEjb2()==null)
			{
				if(mapB.containsKey(t.getId2())) {t.setEjb2(mapB.get(t.getId2()));}
				else
				{
					if(t.getId2()!=null)
					{
						try{t.setEjb2(cB.newInstance()); t.getEjb2().setId(t.getId2());}
						catch (InstantiationException | IllegalAccessException e) {e.printStackTrace();}
					}
				}
			}
			
			if(!mapA.containsKey(t.getId1())) {mapA.put(t.getId1(),t.getEjb1());}
			if(!mapB.containsKey(t.getId2())) {mapB.put(t.getId2(),t.getEjb2());}
			
			if(!map.containsKey(t.getEjb1())) {map.put(t.getEjb1(), new HashMap<B,Json2Tuple<A,B>>());}
			map.get(t.getEjb1()).put(t.getEjb2(), t);
		}
	
		initListA(fJeesl);
		initListB(fJeesl);
		tuples2.addAll(tuples.getTuples());
	}
	
	public void initListB(JeeslFacade fJeesl)
	{
		if(fJeesl==null){listB.addAll(mapB.values());}
		else{listB.clear(); listB.addAll(fJeesl.find(cB,new ArrayList<Long>(mapB.keySet())));}
		sizeB = listB.size();
		if(jcpB!=null && jcpB.provides(cB)){Collections.sort(listB, jcpB.provide(cB));}
	}
	
	public boolean contains(A a, B b) {return map.containsKey(a) && map.get(a).containsKey(b);}
	
	public JsonTuple value(A a, B b)
	{
		Json2Tuple<A,B> json = map.get(a).get(b);
		return JsonTupleFactory.build(json);
	}
	
	public double sum(A a, B b) {return map.get(a).get(b).getSum();}
	public double sum1(A a, B b) {return map.get(a).get(b).getSum1();}
}