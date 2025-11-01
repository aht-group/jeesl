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
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiError;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiStatus;
import org.jeesl.interfaces.model.io.ssi.maintenance.EjbWithSsiDataCleaning;
import org.jeesl.interfaces.model.system.job.core.JeeslJobStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.io.JeeslIoSsiQuery;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples2;

public interface JeeslIoSsiFacade <SYSTEM extends JeeslIoSsiSystem<?,?>,
									CRED extends JeeslIoSsiCredential<SYSTEM>,
									CTX extends JeeslIoSsiContext<SYSTEM,ENTITY>,
									ATTRIBUTE extends JeeslIoSsiAttribute<CTX,ENTITY>,
									DATA extends JeeslIoSsiData<CTX,STATUS,?,JOB>,
									STATUS extends JeeslIoSsiStatus<?,?,STATUS,?>,
									ERROR extends JeeslIoSsiError<?,?,CTX,?>,
									ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>,
									CLEANING extends JeeslIoSsiCleaning<?,?,CLEANING,?>,
									JOB extends JeeslJobStatus<?,?,JOB,?>,
									HOST extends JeeslIoSsiHost<?,?,SYSTEM>
									>
			extends JeeslFacade
{	
	HOST fSsiHost(SYSTEM system, String code) throws JeeslNotFoundException;
	<E extends Enum<E>> CRED fSsiCredential(SYSTEM system, E code);
	List<CRED> fSsiCredentials(JeeslIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR> query);
	CTX fSsiContext(Class<?> json, Class<?> ejb) throws JeeslNotFoundException;
	DATA fIoSsiData(CTX mapping, String code) throws JeeslNotFoundException;
	<A extends EjbWithId> DATA fIoSsiData(CTX mapping, String code, A a) throws JeeslNotFoundException;
	<A extends EjbWithId, B extends EjbWithId> DATA fIoSsiData(CTX mapping, String code, A a, B b) throws JeeslNotFoundException;
		
	<T extends EjbWithId> DATA fIoSsiData(CTX mapping, T ejb) throws JeeslNotFoundException;
	
	JsonTuples1<STATUS> tpIoSsiDataByStatus(JeeslIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR> query);
	JsonTuples2<STATUS,ERROR> tpIoSsiDataByStatusError(JeeslIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR> query);
	
	<A extends EjbWithId, B extends EjbWithId> JsonTuples1<ERROR> tpcIoSsiErrorContext(CTX mapping, A a, B b);
	<A extends EjbWithId, B extends EjbWithId> JsonTuples2<STATUS,JOB> tpcIoSsiStatusJobForContext(CTX mapping, A a, B b);

	JsonTuples1<CTX> tpMapping();
	JsonTuples2<CTX,STATUS> tpcContextStatus(List<CTX> list);
	<A extends EjbWithId, B extends EjbWithId> JsonTuples2<STATUS,B> tpMappingB(Class<B> classB, CTX mapping, A a);

	Long cSsiData(JeeslIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR> query);
	List<DATA> fSsiData(JeeslIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR> query);
	List<DATA> fIoSsiData(CTX mapping, List<STATUS> links);
	<A extends EjbWithId, B extends EjbWithId> List<DATA> fIoSsiData(CTX mapping, List<STATUS> links, A a, B b, Integer maxSize);
	<A extends EjbWithId> List<DATA> fIoSsiData(CTX mapping, List<STATUS> links, A a);
	List<DATA> fSsiDataWithJob1(CTX mapping, STATUS link, JOB job, int maxResult, boolean includeNull, Long refA, Long refB, Long refC);

	List<ATTRIBUTE> fSsiAttributes(JeeslIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR> query);
	
	<T extends EjbWithSsiDataCleaning<CLEANING>> List<T> fEntitiesWithoutSsiDataCleaning(Class<T> c, int maxResult);
	<T extends EjbWithSsiDataCleaning<CLEANING>> JsonTuples1<CLEANING> tpcSsiDataCleaning(Class<T> c);
}