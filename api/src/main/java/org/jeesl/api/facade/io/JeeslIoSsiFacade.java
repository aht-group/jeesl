package org.jeesl.api.facade.io;

import java.util.List;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiCredential;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiAttribute;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiCleaning;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiContext;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiData;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiLink;
import org.jeesl.interfaces.model.io.ssi.maintenance.EjbWithSsiDataCleaning;
import org.jeesl.interfaces.model.system.job.JeeslJobStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.io.EjbIoSsiQuery;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples2;

public interface JeeslIoSsiFacade <L extends JeeslLang,D extends JeeslDescription,
									SYSTEM extends JeeslIoSsiSystem<L,D>,
									CRED extends JeeslIoSsiCredential<SYSTEM>,
									CTX extends JeeslIoSsiContext<SYSTEM,ENTITY>,
									ATTRIBUTE extends JeeslIoSsiAttribute<CTX,ENTITY>,
									DATA extends JeeslIoSsiData<CTX,LINK,JOB>,
									LINK extends JeeslIoSsiLink<L,D,LINK,?>,
									ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>,
									CLEANING extends JeeslIoSsiCleaning<L,D,CLEANING,?>,
									JOB extends JeeslJobStatus<L,D,JOB,?>,
									HOST extends JeeslIoSsiHost<L,D,SYSTEM>
									>
			extends JeeslFacade
{	
	<E extends Enum<E>> CRED fSsiCredential(SYSTEM system, E code) throws JeeslNotFoundException;
	CTX fMapping(Class<?> json, Class<?> ejb) throws JeeslNotFoundException;
	DATA fIoSsiData(CTX mapping, String code) throws JeeslNotFoundException;
	<A extends EjbWithId> DATA fIoSsiData(CTX mapping, String code, A a) throws JeeslNotFoundException;
		
	<T extends EjbWithId> DATA fIoSsiData(CTX mapping, T ejb) throws JeeslNotFoundException;
	
	
	<A extends EjbWithId, B extends EjbWithId> List<DATA> fIoSsiData(CTX mapping, List<LINK> links, A a, B b, Integer maxSize);
	
	JsonTuples1<LINK> tpIoSsiLinkForMapping(CTX mapping);
	<A extends EjbWithId> JsonTuples1<LINK> tpIoSsiLinkForMapping(CTX mapping, A a);
	<A extends EjbWithId, B extends EjbWithId> JsonTuples1<LINK> tpIoSsiLinkForMapping(CTX mapping, A a, B b);
	<A extends EjbWithId, B extends EjbWithId> JsonTuples2<LINK,JOB> tpcIoSsiLinkJobForMapping(CTX mapping, A a, B b);
	
	JsonTuples1<CTX> tpMapping();
	JsonTuples2<CTX,LINK> tpMappingLink(List<CTX> list);
	<A extends EjbWithId, B extends EjbWithId> JsonTuples2<LINK,B> tpMappingB(Class<B> classB, CTX mapping, A a);
	
	Long cSsiData(EjbIoSsiQuery<CTX,LINK> query);
	List<DATA> fSsiData(EjbIoSsiQuery<CTX,LINK> query);
	List<DATA> fIoSsiData(CTX mapping, List<LINK> links);
	<A extends EjbWithId> List<DATA> fIoSsiData(CTX mapping, List<LINK> links, A a);
	List<DATA> fSsiDataWithJob1(CTX mapping, LINK link, JOB job, int maxResult, boolean includeNull, Long refA, Long refB, Long refC);
	
	<T extends EjbWithSsiDataCleaning<CLEANING>> List<T> fEntitiesWithoutSsiDataCleaning(Class<T> c, int maxResult);
	<T extends EjbWithSsiDataCleaning<CLEANING>> JsonTuples1<CLEANING> tpcSsiDataCleaning(Class<T> c);
}