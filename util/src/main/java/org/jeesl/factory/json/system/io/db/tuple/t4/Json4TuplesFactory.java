package org.jeesl.factory.json.system.io.db.tuple.t4;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Tuple;

import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.factory.json.system.io.db.tuple.t3.Json3TuplesFactory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.db.tuple.t4.Json4Tuple;
import org.jeesl.model.json.db.tuple.t4.Json4Tuples;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Json4TuplesFactory <A extends EjbWithId, B extends EjbWithId, C extends EjbWithId, D extends EjbWithId>
			extends Json3TuplesFactory<A,B,C>
{
	final static Logger logger = LoggerFactory.getLogger(Json4TuplesFactory.class);
	
	private final Json4TupleFactory<A,B,C,D> jtf;
	
	private final Class<D> cD;
	private final Set<Long> setId4;
	private final Map<Long,D> mapD; public Map<Long,D> getMapD() {return mapD;}

	private Json4Tuples<A,B,C,D> tuples; public Json4Tuples<A,B,C,D> getTuples4() {return tuples;} public void setTuples4(Json4Tuples<A,B,C,D> tuples) {this.tuples = tuples;}

	public Json4TuplesFactory(Class<A> cA, Class<B> cB, Class<C> cC, Class<D> cD)
	{
		super(cA,cB,cC);

		this.cD=cD;
		setId4 = new HashSet<Long>();
		mapD = new HashMap<Long,D>();
		
		jtf = new Json4TupleFactory<A,B,C,D>();
	}
	
	protected void clear()
	{
		super.clear();
		setId4.clear();
		mapD.clear();
	}
	
	public void init(JeeslFacade fUtils, Json4Tuples<A,B,C,D> json)
	{
		clear();
		this.tuples = json;
		
		for(Json4Tuple<A,B,C,D> t : json.getTuples())
		{
			setA.add(t.getId1());
			setB.add(t.getId2());
			setC.add(t.getId3());
			setId4.add(t.getId4());
		}
		
		logger.info("Futils:"+(fUtils!=null));
		
		mapA.putAll(EjbIdFactory.toIdMap(fUtils.find(cA, setA)));
		mapB.putAll(EjbIdFactory.toIdMap(fUtils.find(cB, setB)));
		mapC.putAll(EjbIdFactory.toIdMap(fUtils.find(cC, setC)));
		mapD.putAll(EjbIdFactory.toIdMap(fUtils.find(cD, setId4)));
	}
	
	public Json4Tuples<A,B,C,D> build4Sum(List<Tuple> tuples)
	{
		Json4Tuples<A,B,C,D> json = new Json4Tuples<A,B,C,D>();
		for(Tuple t : tuples)
        {
        	json.getTuples().add(jtf.buildSum(t));
        }
		return json;
	}
	
	public Json4Tuples<A,B,C,D> build4Count(List<Tuple> tuples)
	{
		Json4Tuples<A,B,C,D> json = new Json4Tuples<A,B,C,D>();
		for(Tuple t : tuples)
        {
        	json.getTuples().add(jtf.buildCount(t));
        }
		return json;
	}
}