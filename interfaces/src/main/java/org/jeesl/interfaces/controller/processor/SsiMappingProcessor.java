package org.jeesl.interfaces.controller.processor;

import java.util.List;

import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiData;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiMapping;

public interface SsiMappingProcessor <MAPPING extends JeeslIoSsiMapping<?,?>,
										DATA extends JeeslIoSsiData<MAPPING,?,?>,
										JSON extends Object
										>
{
	
	MAPPING getMapping();
	
	void initMappings();
	
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
}