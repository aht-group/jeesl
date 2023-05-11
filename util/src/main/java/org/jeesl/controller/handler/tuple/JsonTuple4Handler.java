package org.jeesl.controller.handler.tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.io.db.tuple.instance.JsonTuple3;
import org.jeesl.model.json.io.db.tuple.instance.JsonTuple4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonTuple4Handler <A extends EjbWithId, B extends EjbWithId, C extends EjbWithId, D extends EjbWithId>
							extends JsonTuple3Handler<A,B,C>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JsonTuple4Handler.class);

//	private JeeslComparatorProvider<D> jppD; public void setComparatorProviderD(JeeslComparatorProvider<D> jppD) {this.jppD = jppD;}
	
	private final Class<D> cD;
	
	protected final Map<Long,D> mapD;
	private final Map<A,Map<B,Map<C,JsonTuple3<A,B,C>>>> map4; public Map<A,Map<B,Map<C,JsonTuple3<A,B,C>>>> getMap4() {return map4;}
	
	private final List<D> listD; public List<D> getListD() {return listD;}
	private final List<JsonTuple4<A,B,C,D>> tuples4; public List<JsonTuple4<A,B,C,D>> getTuples4() {return tuples4;}

	private int sizeC; public int getSizeC() {return sizeC;}
	
	public static <A extends EjbWithId, B extends EjbWithId, C extends EjbWithId, D extends EjbWithId> JsonTuple4Handler<A,B,C,D> instance(Class<A> cA, Class<B> cB, Class<C> cC, Class<D> cD) {return new JsonTuple4Handler<>(cA,cB,cC,cD);}
	public JsonTuple4Handler(Class<A> cA, Class<B> cB, Class<C> cC, Class<D> cD)
	{
		super(cA,cB,cC);
		this.cD=cD;
		
		mapD = new HashMap<>();
		listD = new ArrayList<>();
		map4 = new HashMap<A,Map<B,Map<C,JsonTuple3<A,B,C>>>>();
		tuples4 = new ArrayList<>();
		
		dimension = 4;
	}
	
	public void clear()
	{
		super.clear();
		map4.clear();
		mapD.clear();
		listD.clear();
		tuples4.clear();
	}
	
	// .. copy from T3h
}