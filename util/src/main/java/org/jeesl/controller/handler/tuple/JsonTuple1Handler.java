package org.jeesl.controller.handler.tuple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.controller.processor.finance.AmountRounder;
import org.jeesl.factory.json.system.io.db.tuple.JsonTupleFactory;
import org.jeesl.interfaces.controller.report.JeeslComparatorProvider;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.db.tuple.JsonTuple;
import org.jeesl.model.json.db.tuple.t1.Json1Tuple;
import org.jeesl.model.json.db.tuple.t1.Json1Tuples;
import org.jeesl.util.comparator.json.Tuple1Comparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonTuple1Handler <A extends EjbWithId> extends JsonTupleHandler implements Serializable
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JsonTuple1Handler.class);
	
	private JeeslComparatorProvider<A> jcpA; public void setComparatorProviderA(JeeslComparatorProvider<A> jcpA) {this.jcpA = jcpA;}
	private final Tuple1Comparator<A> cpTuple;
	
	protected final Class<A> cA;
	protected final Map<Long,A> mapA;
	
	private int sizeA; public int getSizeA() {return sizeA;}
	private final List<A> listA; public List<A> getListA() {return listA;}
	private final Map<A,Json1Tuple<A>> map1; public Map<A,Json1Tuple<A>> getMapA() {return map1;} public Map<A,Json1Tuple<A>> getMap1() {return map1;}

	public JsonTuple1Handler(Class<A> cA)
	{
		this.cA=cA;
		
		mapA = new HashMap<Long,A>();
		listA = new ArrayList<A>();
		map1 = new HashMap<A,Json1Tuple<A>>();
		
		cpTuple = new Tuple1Comparator<A>();
		
		dimension = 1;
	}
	
	public void clear()
	{
		mapA.clear();
		listA.clear();
		map1.clear();
	}

	public void init(Json1Tuples<A> tuples)
	{
		clear();
		for(Json1Tuple<A> t : tuples.getTuples())
		{
			if(t.getSum()!=null) {t.setSum(AmountRounder.two(t.getSum()/sumDivider));}
			
			if(t.getEjb()==null)
			{
				if(mapA.containsKey(t.getId())) {t.setEjb(mapA.get(t.getId()));}
				else
				{
					try
					{
						t.setEjb(cA.newInstance());
						if(t.getId()!=null){t.getEjb().setId(t.getId());}
						else {t.getEjb().setId(0);}
					}
					catch (InstantiationException | IllegalAccessException e) {e.printStackTrace();}
				}
			}
			
			if(!mapA.containsKey(t.getId())) {mapA.put(t.getId(),t.getEjb());}
			if(!map1.containsKey(t.getEjb())) {map1.put(t.getEjb(), t);}
		}
		initListA(null);
	}
	
	protected void initListA(JeeslFacade fJeesl)
	{
		if(fJeesl==null){listA.addAll(mapA.values());}
		else{listA.addAll(fJeesl.find(cA,new ArrayList<Long>(mapA.keySet())));}
		sizeA = listA.size();
		if(jcpA!=null && jcpA.provides(cA)){Collections.sort(listA, jcpA.provide(cA));}
	}
	
	public boolean contains(A a){return map1.containsKey(a);}
	public JsonTuple value(A a)
	{
		Json1Tuple<A> json = map1.get(a);
		return JsonTupleFactory.build(json);
	}
	
	public Long count(A a)
	{
		if(!this.contains(a)){return null;}
		{
			return map1.get(a).getCount();
		}
	}
	public Double sum1(A a)
	{
		if(!this.contains(a)){return null;}
		{
			return map1.get(a).getSum1();
		}
	}
	
	public void orderDescending()
	{
		cpTuple.setMap(map1);
		Collections.sort(listA,cpTuple);
		Collections.reverse(listA);
	}
}