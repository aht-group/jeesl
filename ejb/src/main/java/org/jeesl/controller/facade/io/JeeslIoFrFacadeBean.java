package org.jeesl.controller.facade.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.io.JeeslIoFrFacade;
import org.jeesl.controller.facade.JeeslFacadeBean;
import org.jeesl.controller.handler.system.io.fr.storage.FileRepositoryAmazonS3;
import org.jeesl.controller.handler.system.io.fr.storage.FileRepositoryFileStorage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoFileRepositoryFactoryBuilder;
import org.jeesl.factory.json.system.io.db.tuple.JsonTupleFactory;
import org.jeesl.factory.json.system.io.db.tuple.t1.Json1TuplesFactory;
import org.jeesl.factory.json.system.io.db.tuple.t2.Json2TuplesFactory;
import org.jeesl.interfaces.controller.handler.system.io.JeeslFileRepositoryStore;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileMeta;
import org.jeesl.interfaces.model.io.fr.JeeslFileReplication;
import org.jeesl.interfaces.model.io.fr.JeeslFileReplicationType;
import org.jeesl.interfaces.model.io.fr.JeeslFileStatus;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorage;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorageEngine;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorageType;
import org.jeesl.interfaces.model.io.fr.JeeslFileType;
import org.jeesl.interfaces.model.io.fr.JeeslWithFileRepositoryContainer;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.json.db.tuple.t1.Json1Tuples;
import org.jeesl.model.json.db.tuple.two.Json2Tuples;
import org.jeesl.util.comparator.pojo.BooleanComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.HashUtil;

public class JeeslIoFrFacadeBean<L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslStatus<L,D,LOC>,
									SYSTEM extends JeeslIoSsiSystem<L,D>,
									STORAGE extends JeeslFileStorage<L,D,SYSTEM,STYPE,ENGINE>,
									STYPE extends JeeslFileStorageType<L,D,STYPE,?>,
									ENGINE extends JeeslFileStorageEngine<L,D,ENGINE,?>,
									CONTAINER extends JeeslFileContainer<STORAGE,META>,
									META extends JeeslFileMeta<D,CONTAINER,TYPE,RSTATUS>,
									TYPE extends JeeslFileType<L,D,TYPE,?>,
									REPLICATION extends JeeslFileReplication<L,D,SYSTEM,STORAGE,RTYPE>,
									RTYPE extends JeeslFileReplicationType<L,D,RTYPE,?>,
									RSTATUS extends JeeslFileStatus<L,D,RSTATUS,?>>
					extends JeeslFacadeBean
					implements JeeslIoFrFacade<L,D,SYSTEM,STORAGE,STYPE,ENGINE,CONTAINER,META,TYPE,REPLICATION,RTYPE,RSTATUS>
{	
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoFrFacadeBean.class);

	private final IoFileRepositoryFactoryBuilder<L,D,LOC,SYSTEM,STORAGE,STYPE,ENGINE,CONTAINER,META,TYPE,REPLICATION,RTYPE,RSTATUS> fbFile;
	private final Map<STORAGE,JeeslFileRepositoryStore<META>> mapStorages;
	
	public JeeslIoFrFacadeBean(EntityManager em, IoFileRepositoryFactoryBuilder<L,D,LOC,SYSTEM,STORAGE,STYPE,ENGINE,CONTAINER,META,TYPE,REPLICATION,RTYPE,RSTATUS> fbFile)
	{
		super(em);
		this.fbFile=fbFile;
		mapStorages = new HashMap<STORAGE,JeeslFileRepositoryStore<META>>();
	}
	
	private JeeslFileRepositoryStore<META> build(STORAGE storage)
	{
		if(!mapStorages.containsKey(storage))
		{
			switch(JeeslFileStorageEngine.Code.valueOf(storage.getEngine().getCode()))
			{
				case fs: mapStorages.put(storage, new FileRepositoryFileStorage<STORAGE,META>(storage));break;
				case amazonS3: mapStorages.put(storage, new FileRepositoryAmazonS3<STORAGE,META>(storage));break;
				default: logger.error("NYI: "+storage.getEngine().getCode());
			}
		}
		return mapStorages.get(storage);
	}

	@Override public META saveToFileRepository(META meta, byte[] bytes) throws JeeslConstraintViolationException, JeeslLockingException
	{
		meta.setMd5Hash(HashUtil.hash(bytes));
		meta = this.saveProtected(meta);
		this.build(meta.getContainer().getStorage()).saveToFileRepository(meta,bytes);
		return meta;
	}
	
	@Override public byte[] loadFromFileRepository(META meta) throws JeeslNotFoundException
	{
		return build(meta.getContainer().getStorage()).loadFromFileRepository(meta);
	}

	@Override public void delteFileFromRepository(META meta) throws JeeslConstraintViolationException, JeeslLockingException
	{
		meta = this.find(fbFile.getClassMeta(),meta);
		boolean keep = BooleanComparator.active(meta.getContainer().getStorage().getKeepRemoved());
		if(!keep)
		{
			build(meta.getContainer().getStorage()).delteFileFromRepository(meta);
		}
		
		logger.info("Removing Meta "+meta.getContainer().getMetas().size()+" keep:"+keep+" "+meta.getCode());
		meta.getContainer().getMetas().remove(meta);
		logger.trace("Removing Meta "+meta.getContainer().getMetas().size());
		this.rm(meta);
	}
	
	@Override public CONTAINER moveContainer(CONTAINER container, STORAGE destination) throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		container = this.find(fbFile.getClassContainer(),container);
		JeeslFileRepositoryStore<META> sourceRepo = this.build(container.getStorage());
		JeeslFileRepositoryStore<META> destinationRepo = this.build(destination);
		
		for(META meta : container.getMetas())
		{
			byte[] bytes = sourceRepo.loadFromFileRepository(meta);
			destinationRepo.saveToFileRepository(meta, bytes);
		}
		container.setStorage(destination);
		container = this.save(container);
		for(META meta : container.getMetas())
		{
			sourceRepo.delteFileFromRepository(meta);
		}
		
		return container;
	}

	@Override public Json1Tuples<STORAGE> tpsIoFileByStorage()
	{
		Json1TuplesFactory<STORAGE> jtf = new Json1TuplesFactory<>(this,fbFile.getClassStorage());
		jtf.setfUtils(this);
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<META> item = cQ.from(fbFile.getClassMeta());
		
		Expression<Long> eSum = cB.sum(item.<Long>get(JeeslFileMeta.Attributes.size.toString()));
		Join<META,CONTAINER> jContainer = item.join(JeeslFileMeta.Attributes.container.toString());
		Path<STORAGE> pStorage = jContainer.get(JeeslFileContainer.Attributes.storage.toString());
		
		cQ.groupBy(pStorage.get("id"));
		cQ.multiselect(pStorage.get("id"),eSum);

		TypedQuery<Tuple> tQ = em.createQuery(cQ);
        return jtf.buildV2(tQ.getResultList(),JsonTupleFactory.Type.count);
	}
	
	@Override public Json2Tuples<STORAGE,TYPE> tpcIoFileByStorageType()
	{
		Json2TuplesFactory<STORAGE,TYPE> jtf = new Json2TuplesFactory<>(this,fbFile.getClassStorage(),fbFile.getClassType());
		jtf.setfUtils(this);
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<META> item = cQ.from(fbFile.getClassMeta());
		
		Expression<Long> eCount = cB.count(item.<Long>get("id"));
		Join<META,CONTAINER> jContainer = item.join(JeeslFileMeta.Attributes.container.toString());
		Path<STORAGE> pStorage = jContainer.get(JeeslFileContainer.Attributes.storage.toString());
		Path<TYPE> pType = item.get(JeeslFileMeta.Attributes.type.toString());
		
		cQ.groupBy(pStorage.get("id"),pType.get("id"));
		cQ.multiselect(pStorage.get("id"),pType.get("id"),eCount);

		TypedQuery<Tuple> tQ = em.createQuery(cQ);
        return jtf.build(tQ.getResultList(),JsonTupleFactory.Type.count);
	}

	@Override public <OWNER extends JeeslWithFileRepositoryContainer<CONTAINER>> List<META> fIoFrMetas(Class<OWNER> c, List<OWNER> owners)
	{
		
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<META> cQ = cB.createQuery(fbFile.getClassMeta());
		Root<OWNER> owner = cQ.from(c);
		
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(owner.in(owners));
		
		Join<OWNER,CONTAINER> jContainer = owner.join(JeeslWithFileRepositoryContainer.Attributes.frContainer.toString());
		ListJoin<CONTAINER,META> jMeta = jContainer.joinList(JeeslFileContainer.Attributes.metas.toString());
			
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(jMeta).distinct(true);

		TypedQuery<META> tQ = em.createQuery(cQ);
		return tQ.getResultList();
	}
}