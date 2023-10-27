package org.jeesl.interfaces.cache.io.ssi;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiContext;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiStatus;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples2;

public interface JeeslIoSsiContextCache <CONTEXT extends JeeslIoSsiContext<?,?>,
											STATUS extends JeeslIoSsiStatus<?,?,STATUS,?>>
							extends Serializable
{	
	JsonTuples2<CONTEXT,STATUS> cacheGetTuples2(Class<?> c, CONTEXT context);
	void cachePutTuples2(Class<?> c, CONTEXT context, JsonTuples2<CONTEXT,STATUS> tuples);
}