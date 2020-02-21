package org.jeesl.factory.json.system.io.db.tuple.t3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Tuple;

import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.model.json.db.tuple.t3.Json3Tuple;
import org.jeesl.model.json.db.tuple.t3.Json3Tuples;
import org.jeesl.model.json.db.tuple.two.Json2Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.model.interfaces.with.EjbWithId;

public class Json3TuplesFactory <A extends EjbWithId, B extends EjbWithId, C extends EjbWithId>
{
	final static Logger logger = LoggerFactory.getLogger(Json3TuplesFactory.class);
	
	private JeeslFacade fUtils; public JeeslFacade getfUtils() {return fUtils;} public void setfUtils(JeeslFacade fUtils) {this.fUtils = fUtils;}
	private final Json3TupleFactory<A,B,C> jtf;
	
	protected final Class<A> cA;
	protected final Class<B> cB;
	protected final Class<C> cC;
	
	protected final Set<Long> setA;
	protected final Set<Long> setB;
	protected final Set<Long> setC;
	
	protected final Map<Long,A> mapA; public Map<Long,A> getMapA() {return mapA;}
	protected final Map<Long,B> mapB; public Map<Long,B> getMapB() {return mapB;}
	protected final Map<Long,C> mapC; public Map<Long,C> getMapC() {return mapC;}

	private Json3Tuples<A,B,C> tuples; public Json3Tuples<A,B,C> get3Tuples() {return tuples;} public void set3Tuples(Json3Tuples<A, B, C> tuples) {this.tuples = tuples;}

	public Json3TuplesFactory(Class<A> cA, Class<B> cB, Class<C> cC)
	{
//		super(cA,cB);
		this.cA=cA;
		this.cB=cB;
		this.cC=cC;
		
		setA = new HashSet<Long>();
		setB = new HashSet<Long>();
		setC = new HashSet<Long>();
		
		mapA = new HashMap<Long,A>();
		mapB = new HashMap<Long,B>();
		mapC = new HashMap<Long,C>();
		
		jtf = new Json3TupleFactory<A,B,C>();
	}
	
	protected void clear()
	{
		setA.clear();
		setB.clear();
		setC.clear();
		
		mapA.clear();
		mapB.clear();
		mapC.clear();
	}
	
	private void ejb3Load(Json3Tuples<A,B,C> json)
	{ 
		clear();
		for(Json3Tuple<A,B,C> t : json.getTuples())
		{
			setA.add(t.getId1());
			setB.add(t.getId2());
			setC.add(t.getId3());
		}
		
		if(fUtils==null)
		{	// A object is created and the corresponding id is set
			for(Json3Tuple<A,B,C> t : json.getTuples())
			{
				try
				{
					t.setEjb1(cA.newInstance());t.getEjb1().setId(t.getId1());
					t.setEjb2(cB.newInstance());t.getEjb2().setId(t.getId2());
					t.setEjb3(cC.newInstance());t.getEjb3().setId(t.getId3());
				}
				catch (InstantiationException | IllegalAccessException e) {e.printStackTrace();}
			}
		}
		else
		{
			// Here we really load the objects from the DB
			
			mapA.putAll(EjbIdFactory.toIdMap(fUtils.find(cA,setA)));
			mapB.putAll(EjbIdFactory.toIdMap(fUtils.find(cB,setB)));
			mapC.putAll(EjbIdFactory.toIdMap(fUtils.find(cC,setC)));
			
			for(Json3Tuple<A,B,C> t : json.getTuples())
			{
				t.setEjb1(mapA.get(t.getId1()));
				t.setEjb2(mapB.get(t.getId2()));
				t.setEjb3(mapC.get(t.getId3()));
			}
		}
		this.tuples=json;
	}
	
	public Json3Tuples<A,B,C> build3Sum(List<Tuple> tuples)
	{
		Json3Tuples<A,B,C> json = new Json3Tuples<A,B,C>();
		for(Tuple t : tuples)
        {
        	json.getTuples().add(jtf.buildSum(t));
        }
		ejb3Load(json);
		return json;
	}
	
	public Json3Tuples<A,B,C> build3Count(List<Tuple> tuples)
	{
		Json3Tuples<A,B,C> json = new Json3Tuples<A,B,C>();
		for(Tuple t : tuples)
        {
        	json.getTuples().add(jtf.buildCount(t));
        }
		ejb3Load(json);
		return json;
	}
	
	public Json3Tuples<A,B,C> build3CountInterger4(List<Tuple> tuples)
	{
		Json3Tuples<A,B,C> json = new Json3Tuples<A,B,C>();
		for(Tuple t : tuples)
        {
        	json.getTuples().add(jtf.buildCountInteger4(t));
        }
		ejb3Load(json);
		return json;
	}
}