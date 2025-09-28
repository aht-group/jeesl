package org.jeesl.controller.facade.jx.io;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.io.JeeslIoFrFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.controller.facade.jx.predicate.LiteralPredicateBuilder;
import org.jeesl.controller.facade.jx.predicate.TimePredicateBuilder;
import org.jeesl.controller.handler.system.io.fr.storage.FileRepositoryAmazonS3;
import org.jeesl.controller.handler.system.io.fr.storage.FileRepositoryFileStorage;
import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoFileRepositoryFactoryBuilder;
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
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.util.query.io.JeeslIoFrQuery;
import org.jeesl.model.ejb.io.db.JeeslCq;
import org.jeesl.model.ejb.io.db.JeeslCqLiteral;
import org.jeesl.model.ejb.io.db.JeeslCqTime;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples2;
import org.jeesl.util.query.cq.CqLiteral;
import org.jeesl.util.query.cq.CqOrdering;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.HashUtil;

public class JeeslIoFrFacadeBean<L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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

	@Override public JsonTuples1<STORAGE> tpsIoFileByStorage()
	{

		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<META> item = cQ.from(fbFile.getClassMeta());

		Expression<Long> eSum = cB.sum(item.<Long>get(JeeslFileMeta.Attributes.size.toString()));
		Join<META,CONTAINER> jContainer = item.join(JeeslFileMeta.Attributes.container.toString());
		Path<STORAGE> pStorage = jContainer.get(JeeslFileContainer.Attributes.storage.toString());

		cQ.groupBy(pStorage.get("id"));
		cQ.multiselect(pStorage.get("id"),eSum);

		TypedQuery<Tuple> tQ = em.createQuery(cQ);
		Json1TuplesFactory<STORAGE> jtf = Json1TuplesFactory.instance(fbFile.getClassStorage()).tupleLoad(this,true);
        return jtf.buildV2(tQ.getResultList(),JeeslCq.Agg.count);
	}

	@Override public JsonTuples2<STORAGE,TYPE> tpcIoFileByStorageType()
	{
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
		Json2TuplesFactory<STORAGE,TYPE> jtf = Json2TuplesFactory.instance(fbFile.getClassStorage(),fbFile.getClassType()).tupleLoad(this,true);
        return jtf.build(tQ.getResultList(),JeeslCq.Agg.count);
	}

	@Override public List<CONTAINER> fIoFrContainer(JeeslIoFrQuery<STORAGE,CONTAINER> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<CONTAINER> cQ = cB.createQuery(fbFile.getClassContainer());
		Root<CONTAINER> root = cQ.from(fbFile.getClassContainer());

		cQ.select(root);
		cQ.where(cB.and(this.pContainer(cB, query, root)));

		TypedQuery<CONTAINER> tQ = em.createQuery(cQ);
		super.pagination(tQ, query);
		return tQ.getResultList();
	}

	@Override public <OWNER extends JeeslWithFileRepositoryContainer<CONTAINER>> List<META> fIoFrMetas(Class<OWNER> c, List<OWNER> owners)
	{
		if(ObjectUtils.isEmpty(owners)) {return new ArrayList<>();}

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

	@Override public Long cIoFrMetas(JeeslIoFrQuery<STORAGE,CONTAINER> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Long> cQ = cB.createQuery(Long.class);
		Root<META> root = cQ.from(fbFile.getClassMeta());

		cQ.select(cB.count(root));
		cQ.where(cB.and(this.pMeta(cB, query, root)));

		TypedQuery<Long> tQ = em.createQuery(cQ);
		return tQ.getSingleResult();
	}
	@Override public List<META> fIoFrMetas(JeeslIoFrQuery<STORAGE,CONTAINER> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<META> cQ = cB.createQuery(fbFile.getClassMeta());
		Root<META> root = cQ.from(fbFile.getClassMeta());

		cQ.select(root);
		cQ.where(cB.and(this.pMeta(cB, query, root)));

		TypedQuery<META> tQ = em.createQuery(cQ);
		super.pagination(tQ, query);
		return tQ.getResultList();
	}

	public Predicate[] pContainer(CriteriaBuilder cB, JeeslIoFrQuery<STORAGE,CONTAINER> query, Root<CONTAINER> root)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();

		if(ObjectUtils.isNotEmpty(query.getIoFrContainers()))
		{
			predicates.add(root.in(query.getIoFrContainers()));
		}
		if(ObjectUtils.isNotEmpty(query.getIoFrStorages()))
		{
			Path<STORAGE> pStorage = root.get(JeeslFileContainer.Attributes.storage.toString());
			predicates.add(pStorage.in(query.getIoFrStorages()));
		}

		for(JeeslCqLiteral cq : ListUtils.emptyIfNull(query.getCqLiterals()))
		{
			if(cq.getPath().equals(CqLiteral.path(JeeslFileContainer.Attributes.metas,JeeslFileMeta.Attributes.category)))
			{
				ListJoin<CONTAINER,META> jMeta = root.joinList(JeeslFileContainer.Attributes.metas.toString());
				Expression<String> e = jMeta.get(JeeslFileMeta.Attributes.category.toString());
				LiteralPredicateBuilder.add(cB, predicates, cq, e);
			}
			else {logger.warn("No Handling for "+cq.nyi(fbFile.getClassContainer()));}
		}


		return predicates.toArray(new Predicate[predicates.size()]);
	}

	public Predicate[] pMeta(CriteriaBuilder cB, JeeslIoFrQuery<STORAGE,CONTAINER> query, Root<META> root)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();

		Path<CONTAINER> pContainer = null;

		if(ObjectUtils.isNotEmpty(query.getIoFrContainers()))
		{
			if(Objects.isNull(pContainer)) {pContainer = root.get(JeeslFileMeta.Attributes.container.toString());}
			predicates.add(pContainer.in(query.getIoFrContainers()));
		}
		if(ObjectUtils.isNotEmpty(query.getIoFrStorages()))
		{
			if(Objects.isNull(pContainer)) {pContainer = root.get(JeeslFileMeta.Attributes.container.toString());}
			Path<STORAGE> pStorage = pContainer.get(JeeslFileContainer.Attributes.storage.toString());
			predicates.add(pStorage.in(query.getIoFrStorages()));
		}

		for(JeeslCqLiteral cq : ListUtils.emptyIfNull(query.getCqLiterals()))
		{
			if(cq.getPath().equals(CqLiteral.path(JeeslFileMeta.Attributes.category)))
			{
				Expression<String> e = root.get(JeeslFileMeta.Attributes.category.toString());
				LiteralPredicateBuilder.add(cB, predicates, cq, e);
			}
			else if(cq.getPath().equals(CqLiteral.path(JeeslFileMeta.Attributes.code)))
			{
				Expression<String> e = root.get(JeeslFileMeta.Attributes.code.toString());
				LiteralPredicateBuilder.add(cB, predicates, cq, e);
			}
			else {logger.warn(cq.nyi(fbFile.getClassContainer()));}
		}
		for(JeeslCqTime cq : ListUtils.emptyIfNull(query.getCqTimes()))
		{
			if(cq.getPath().equals(CqOrdering.path(JeeslFileMeta.Attributes.record+"x")))
			{
				Expression<LocalDateTime> e = root.get(JeeslFileMeta.Attributes.record.toString());
				TimePredicateBuilder.jtTime(cB, predicates,cq, e);
			}
			else {logger.warn("No Handling for "+cq.nyi(fbFile.getClassMeta()));}
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}