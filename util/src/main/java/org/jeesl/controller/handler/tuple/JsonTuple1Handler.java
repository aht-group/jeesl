package org.jeesl.controller.handler.tuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jeesl.controller.processor.finance.AmountRounder;
import org.jeesl.controller.util.comparator.json.Tuple1Comparator;
import org.jeesl.factory.json.io.db.tuple.JsonTupleFactory;
import org.jeesl.interfaces.controller.report.JeeslComparatorProvider;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.ejb.io.db.JeeslCq;
import org.jeesl.model.json.io.db.tuple.JsonTuple;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;
import org.jeesl.model.json.io.db.tuple.instance.JsonTuple1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonTuple1Handler <A extends EjbWithId> extends JsonTupleHandler
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JsonTuple1Handler.class);
	
	private JeeslComparatorProvider<A> jcpA; public void setComparatorProviderA(JeeslComparatorProvider<A> jcpA) {this.jcpA = jcpA;}
	private final Tuple1Comparator<A> cpTuple;
	
	protected final Class<A> cA; public Class<A> getClassA() {return cA;}
	
	protected final Map<Long,A> mapA;
	
	private int sizeA; public int getSizeA() {return sizeA;}
	private final List<A> listA; public List<A> getListA() {return listA;}
	
	private final Map<A,JsonTuple1<A>> map1;
	public Map<A,JsonTuple1<A>> getMapA() {return map1;}
	
	@Deprecated
	public Map<A,JsonTuple1<A>> getMap1() {return map1;}

	public static <A extends EjbWithId> JsonTuple1Handler<A> instance(Class<A> cA) {return new JsonTuple1Handler<>(cA);}
	public JsonTuple1Handler(Class<A> cA)
	{
		this.cA=cA;
		
		mapA = new HashMap<Long,A>();
		map1 = new HashMap<A,JsonTuple1<A>>();
		
		listA = new ArrayList<A>();
		
		cpTuple = new Tuple1Comparator<A>();
		
		dimension = 1;
	}
	
	public void clear()
	{
		mapA.clear();
		listA.clear();
		map1.clear();
	}

	public JsonTuple1Handler<A> init(JsonTuples1<A> tuples)
	{
		this.clear();
		for(JsonTuple1<A> t : tuples.getTuples())
		{
			size++;
			if(t.getSum()!=null) {t.setSum(AmountRounder.two(t.getSum()/sumDivider));}
			
			if(Objects.isNull(t.getEjb1()))
			{
				if(mapA.containsKey(t.getId1())) {t.setEjb1(mapA.get(t.getId1()));}
				else
				{
					try
					{
						t.setEjb1(cA.newInstance());
						if(t.getId1()!=null){t.getEjb1().setId(t.getId1());}
						else {t.getEjb1().setId(0);}
					}
					catch (InstantiationException | IllegalAccessException e) {e.printStackTrace();}
				}
			}
			
			if(!mapA.containsKey(t.getId1())) {mapA.put(t.getId1(),t.getEjb1());}
			if(!map1.containsKey(t.getEjb1())) {map1.put(t.getEjb1(), t);}
		}
		initListA(null);
		return this;
	}
	
	protected void initTupleA(JsonTuple1<A> t)
	{
		if(t.getEjb1()==null)
		{
			if(mapA.containsKey(t.getId1())) {t.setEjb1(mapA.get(t.getId1()));}
			else
			{
				try {t.setEjb1(cA.newInstance()); t.getEjb1().setId(t.getId1());}
				catch (InstantiationException | IllegalAccessException e) {e.printStackTrace();}
			}
		}
		if(!mapA.containsKey(t.getId1())) {mapA.put(t.getId1(),t.getEjb1());}
	}
	
	public void initListA(JeeslFacade facade)
	{
		listA.clear();
		if(facade==null) {listA.addAll(mapA.values());}
		else {listA.addAll(facade.find(cA,new ArrayList<Long>(mapA.keySet())));}
		sizeA = listA.size();
		if(jcpA!=null && jcpA.provides(cA)){Collections.sort(listA, jcpA.provide(cA));}
	}	
	
	public boolean contains(A a) {return map1.containsKey(a);}
	public JsonTuple value(A a)
	{
		JsonTuple1<A> json = map1.get(a);
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
	
	public void applyDefault(long value)
	{
		for(A a : listA)
		{
			if(!this.contains(a))
			{
				JsonTuple1<A> t = JsonTupleFactory.build1(a, 0, JeeslCq.Agg.count);
				map1.put(a, t);
			}
		}
	}
	
	public void orderDescending()
	{
		cpTuple.setMap(map1);
		Collections.sort(listA,cpTuple);
		Collections.reverse(listA);
	}
	
	public void debug(boolean debug)
	{
		logger.info(cA.getSimpleName()+" "+listA.size());
	}
}