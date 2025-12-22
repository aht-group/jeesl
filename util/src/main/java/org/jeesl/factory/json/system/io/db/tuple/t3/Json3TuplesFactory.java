package org.jeesl.factory.json.system.io.db.tuple.t3;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Tuple;

import org.exlp.util.io.JsonUtil;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.factory.json.io.db.tuple.JsonTupleFactory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.ejb.io.db.JeeslCq;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples3;
import org.jeesl.model.json.io.db.tuple.instance.JsonTuple3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private JsonTuples3<A,B,C> tuples; public JsonTuples3<A,B,C> get3Tuples() {return tuples;} public void set3Tuples(JsonTuples3<A, B, C> tuples) {this.tuples = tuples;}

	public static <A extends EjbWithId, B extends EjbWithId, C extends EjbWithId> Json3TuplesFactory<A,B,C> instance(Class<A> cA, Class<B> cB, Class<C> cC) {return new Json3TuplesFactory<>(cA,cB,cC);}
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
	
	public Json3TuplesFactory<A,B,C> tupleLoad(JeeslFacade facade, boolean load)
	{
		if(load) {this.fUtils=facade;}
		return this;
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
	
	private void ejb3Load(JsonTuples3<A,B,C> json)
	{ 
		clear();
		for(JsonTuple3<A,B,C> t : json.getTuples())
		{
			setA.add(t.getId1());
			setB.add(t.getId2());
			setC.add(t.getId3());
		}
		
		if(Objects.isNull(fUtils))
		{	// A object is created and the corresponding id is set
			for(JsonTuple3<A,B,C> t : json.getTuples())
			{
				try
				{
					t.setEjb1(cA.getDeclaredConstructor().newInstance()); if(Objects.nonNull(t.getId1())) {t.getEjb1().setId(t.getId1());}
					t.setEjb2(cB.getDeclaredConstructor().newInstance()); if(Objects.nonNull(t.getId2())) {t.getEjb2().setId(t.getId2());}
					t.setEjb3(cC.getDeclaredConstructor().newInstance()); if(Objects.nonNull(t.getId3())) {t.getEjb3().setId(t.getId3());}
				}
				catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {e.printStackTrace();}
			}
		}
		else
		{
			// Here we really load the objects from the DB
			mapA.putAll(EjbIdFactory.toIdMap(fUtils.find(cA,setA)));
			mapB.putAll(EjbIdFactory.toIdMap(fUtils.find(cB,setB)));
			mapC.putAll(EjbIdFactory.toIdMap(fUtils.find(cC,setC)));
			
			for(JsonTuple3<A,B,C> t : json.getTuples())
			{
				t.setEjb1(mapA.get(t.getId1()));
				t.setEjb2(mapB.get(t.getId2()));
				t.setEjb3(mapC.get(t.getId3()));
			}
		}
		this.tuples=json;
	}
	
	public JsonTuples3<A,B,C> build(List<Tuple> tuples, JeeslCq.Agg...types)
	{
		JsonTuples3<A,B,C> json = new JsonTuples3<>();
		for(Tuple t : tuples){json.getTuples().add(JsonTupleFactory.build3(t,types));}
		ejb3Load(json);
		return json;
	}

	public JsonTuples3<A,B,C> build3CountInterger4(List<Tuple> tuples)
	{
		JsonTuples3<A,B,C> json = new JsonTuples3<A,B,C>();
		for(Tuple t : tuples)
        {
        	json.getTuples().add(jtf.buildCountInteger4(t));
        }
		ejb3Load(json);
		return json;
	}
}