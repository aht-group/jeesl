package org.jeesl.interfaces.controller.processor;

import java.util.List;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiData;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiMapping;

public interface SsiMappingProcessor <MAPPING extends JeeslIoSsiMapping<?,?>,
										DATA extends JeeslIoSsiData<MAPPING,?>
,JSON extends Object
										>
{
	MAPPING getMapping();
	
	void initMappings() throws JeeslNotFoundException;
	
	Class<?> getClassA();
	Class<?> getClassB();
	Class<?> getClassC();
	Class<JSON> getClassJson();
	Class<?> getClassLocal();
	Class<?> getClassTarget();

	void evaluateData(List<DATA> datas);
	void linkData(List<DATA> datas);
	void ignoreData(List<DATA> datas);
	void unignoreData(List<DATA> datas);
}
