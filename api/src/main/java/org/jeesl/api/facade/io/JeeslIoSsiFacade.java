package org.jeesl.api.facade.io;

import java.util.List;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiCredential;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiAttribute;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiCleaning;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiData;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiLink;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiMapping;
import org.jeesl.interfaces.model.io.ssi.maintenance.EjbWithSsiDataCleaning;
import org.jeesl.interfaces.model.system.job.EjbWithMigrationJob1;
import org.jeesl.interfaces.model.system.job.JeeslJobStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.db.tuple.t1.Json1Tuples;
import org.jeesl.model.json.db.tuple.two.Json2Tuples;

public interface JeeslIoSsiFacade <L extends JeeslLang,D extends JeeslDescription,
									SYSTEM extends JeeslIoSsiSystem<L,D>,
									CRED extends JeeslIoSsiCredential<SYSTEM>,
									MAPPING extends JeeslIoSsiMapping<SYSTEM,ENTITY>,
									ATTRIBUTE extends JeeslIoSsiAttribute<MAPPING,ENTITY>,
									DATA extends JeeslIoSsiData<MAPPING,LINK,JOB>,
									LINK extends JeeslIoSsiLink<L,D,LINK,?>,
									ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>,
									CLEANING extends JeeslIoSsiCleaning<L,D,CLEANING,?>,
									JOB extends JeeslJobStatus<L,D,JOB,?>,
									HOST extends JeeslIoSsiHost<L,D,SYSTEM>
									>
			extends JeeslFacade
{	
	MAPPING fMapping(Class<?> json, Class<?> ejb) throws JeeslNotFoundException;
	DATA fIoSsiData(MAPPING mapping, String code) throws JeeslNotFoundException;
	<A extends EjbWithId> DATA fIoSsiData(MAPPING mapping, String code, A a) throws JeeslNotFoundException;
		
	<T extends EjbWithId> DATA fIoSsiData(MAPPING mapping, T ejb) throws JeeslNotFoundException;
	List<DATA> fIoSsiData(MAPPING mapping, List<LINK> links);
	<A extends EjbWithId> List<DATA> fIoSsiData(MAPPING mapping, List<LINK> links, A a);
	<A extends EjbWithId, B extends EjbWithId> List<DATA> fIoSsiData(MAPPING mapping, List<LINK> links, A a, B b, Integer maxSize);
	
	Json1Tuples<LINK> tpIoSsiLinkForMapping(MAPPING mapping);
	<A extends EjbWithId> Json1Tuples<LINK> tpIoSsiLinkForMapping(MAPPING mapping, A a);
	<A extends EjbWithId, B extends EjbWithId> Json1Tuples<LINK> tpIoSsiLinkForMapping(MAPPING mapping, A a, B b);
	
	Json1Tuples<MAPPING> tpMapping();
	Json2Tuples<MAPPING,LINK> tpMappingLink();
	<A extends EjbWithId, B extends EjbWithId> Json2Tuples<LINK,B> tpMappingB(Class<B> classB, MAPPING mapping, A a);
	
	<T extends EjbWithSsiDataCleaning<CLEANING>> List<T> fEntitiesWithoutSsiDataCleaning(Class<T> c, int maxResult);
	<T extends EjbWithSsiDataCleaning<CLEANING>> Json1Tuples<CLEANING> tpcSsiDataCleaning(Class<T> c);
	
	List<DATA> fSsiDataWithPendingJob1(int maxResult, boolean includeNull, Long refA, Long refB, Long refC);
}