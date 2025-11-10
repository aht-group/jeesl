package org.jeesl.interfaces.controller.processor;

import java.util.List;

import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiContext;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiData;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface SsiMappingProcessor <CONTEXT extends JeeslIoSsiContext<?,?>,
										DATA extends JeeslIoSsiData<CONTEXT,?,?,?>,
										JSON extends Object>
{
	
	CONTEXT getMapping();
	
	void loadContext();
	
	Class<?> getClassA();
	Class<?> getClassB();
	Class<?> getClassC();
	Class<JSON> getClassJson();
	Class<?> getClassLocal();
	Class<?> getClassTarget();

	void evaluateData(List<DATA> datas);
//	DATA evaluate(DATA data, JSON json); Not possible, beacause often additional information required
	
	void linkData(List<DATA> datas);
	void ignoreData(List<DATA> datas);
	void unignoreData(List<DATA> datas);
	
	void applyRefA(EjbWithId ejb, DATA data);
	void applyRefB(EjbWithId ejb, DATA data);
	void applyRefC(EjbWithId ejb, DATA data);
}