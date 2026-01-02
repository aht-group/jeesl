package org.jeesl.web.mbean.prototype.io.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoDomainFacade;
import org.jeesl.controller.util.comparator.ejb.io.label.LabelEntityComparator;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.IoDomainFactoryBuilder;
import org.jeesl.factory.ejb.io.domain.EjbSurveyDomainFactory;
import org.jeesl.factory.ejb.io.domain.EjbSurveyDomainPathFactory;
import org.jeesl.factory.ejb.io.domain.EjbSurveyDomainQueryFactory;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.model.io.domain.JeeslDomain;
import org.jeesl.interfaces.model.io.domain.JeeslDomainItem;
import org.jeesl.interfaces.model.io.domain.JeeslDomainPath;
import org.jeesl.interfaces.model.io.domain.JeeslDomainQuery;
import org.jeesl.interfaces.model.io.domain.JeeslDomainSet;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDomainQueryBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
						DOMAIN extends JeeslDomain<L,ENTITY>,
						QUERY extends JeeslDomainQuery<L,D,DOMAIN,PATH>,
						PATH extends JeeslDomainPath<L,D,QUERY,ENTITY,DATTRIBUTE>,
						ENTITY extends JeeslRevisionEntity<L,D,?,?,DATTRIBUTE,?>,
						DATTRIBUTE extends JeeslRevisionAttribute<L,D,ENTITY,?,?>,
						SET extends JeeslDomainSet<L,D,DOMAIN>,
						ITEM extends JeeslDomainItem<QUERY,SET>>
					extends AbstractDomainBean<L,D,LOC,DOMAIN,QUERY,PATH,ENTITY,DATTRIBUTE,SET,ITEM>
					implements Serializable,SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractDomainQueryBean.class);

	protected List<ENTITY> entities; public List<ENTITY> getEntities(){return entities;}
	protected List<PATH> paths; public List<PATH> getPaths(){return paths;}
	protected List<DATTRIBUTE> attributes; public List<DATTRIBUTE> getAttributes(){return attributes;}
	
	private DOMAIN domain; public DOMAIN getDomain() {return domain;} public void setDomain(DOMAIN domain) {this.domain = domain;}
	private QUERY query; public QUERY getQuery() {return query;} public void setQuery(QUERY query) {this.query = query;}
	private PATH path; public PATH getPath() {return path;} public void setPath(PATH path) {this.path = path;}
	private ENTITY entity; public ENTITY getEntity() {return entity;}
	
	protected final SbSingleHandler<DOMAIN> sbhDomain; public SbSingleHandler<DOMAIN> getSbhDomain() {return sbhDomain;}
	
	private final EjbSurveyDomainFactory<L,D,DOMAIN,ENTITY> efDomain;
	private final EjbSurveyDomainQueryFactory<L,D,DOMAIN,QUERY,PATH> efDomainQuery;
	private final EjbSurveyDomainPathFactory<L,D,QUERY,PATH,ENTITY,DATTRIBUTE> efDomainPath;
	
	protected final Comparator<ENTITY> cpDomainEntity;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AbstractDomainQueryBean(IoDomainFactoryBuilder<L,D,DOMAIN,QUERY,PATH,ENTITY,DATTRIBUTE,SET,ITEM> fbDomain)
	{
		super(fbDomain);
		
		sbhDomain = new SbSingleHandler<DOMAIN>(fbDomain.getClassDomain(),this);
		sbhDomain.setDebugOnInfo(true);
		
		efDomain = fbDomain.ejbDomain();
		efDomainQuery = fbDomain.ejbDomainQuery();
		efDomainPath = fbDomain.ejbDomainPath();
		
		cpDomainEntity = new LabelEntityComparator().factory(LabelEntityComparator.Type.position);
	}
	
	protected void postConstructDomainQuery(String userLocale, JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
			JeeslIoDomainFacade<L,D,DOMAIN,QUERY,PATH,ENTITY,DATTRIBUTE,SET,ITEM> fDomain)
	{
		super.postConstructDomain(bTranslation,bMessage,fDomain);
//		super.initSuperSurvey(bTranslation.getLangKeys(),bMessage,fTemplate,fCore,fAnalysis,bSurvey);
//		initPageSettings();
		
		entities = fDomain.allOrderedPositionVisible(fbDomain.getClassDomainEntity());
		Collections.sort(entities,cpDomainEntity);
		reloadDomains();
		if(sbhDomain.getHasSome())
		{
			sbhDomain.setDefault();
			domain = sbhDomain.getSelection();
			reloadQueries();
		}
		else
		{
			addDomain();
			sbhDomain.selectSbSingle(domain);
			sbhDomain.selectSbSingle(domain);
		}
	}
	
//	@Override protected void initPageSettings(){}
	
	@SuppressWarnings("unchecked")
	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId ejb)
	{
		if(ejb==null) {reset(true,true,true,true);}
		else if(JeeslDomain.class.isAssignableFrom(ejb.getClass()))
		{
			domain = (DOMAIN)ejb;
			if(EjbIdFactory.isSaved(domain))
			{
				selectDomain();
			}
			logger.info("Twice:"+sbhDomain.getTwiceSelected()+" for "+domain.toString());
		}
		else
		{
			logger.info("NOT Assignable");
		}
	}
	
	private void reset(boolean rDomain, boolean rQuery, boolean rPath, boolean rEntity)
	{
		if(rDomain){domain = null;}
		if(rQuery){query = null;}
		if(rPath){path = null;}
		if(rEntity){entity = null;}
	}
	
	private void reloadDomains()
	{
		sbhDomain.setList(fDomain.all(fbDomain.getClassDomain()));
	}
	
	public void addDomain()
	{
		logger.info(AbstractLogMessage.createEntity(fbDomain.getClassDomain()));
		domain = efDomain.build(null,sbhDomain.getList());
		domain.setName(efLang.createEmpty(localeCodes));
	}
	
	public void saveDomain() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(domain));
		domain.setEntity(fDomain.find(fbDomain.getClassDomainEntity(),domain.getEntity()));
		domain = fDomain.save(domain);
		sbhDomain.setSelection(domain);
		reloadDomains();
		reloadQueries();
	}
	
	public void selectDomain()
	{
		reset(false,true,true,true);
		logger.info(AbstractLogMessage.selectEntity(domain));
		domain = efLang.persistMissingLangs(fDomain,localeCodes,domain);
		sbhDomain.setSelection(domain);
		reloadQueries();
	}
	
	private void reloadQueries()
	{
		queries = fDomain.allForParent(fbDomain.getClassDomainQuery(), domain);
	}
	
	public void addQuery()
	{
		logger.info(AbstractLogMessage.createEntity(fbDomain.getClassDomainQuery()));
		query = efDomainQuery.build(domain, queries);
		query.setName(efLang.createEmpty(localeCodes));
		query.setDescription(efDescription.createEmpty(localeCodes));
	}
	
	public void saveQuery() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(query));
//		domain.setEntity(fAnalysis.find(fbAnalysis.getClassDomainEntity(),domain.getEntity()));
		query = fDomain.save(query);
		reloadQueries();
		reloadPaths();
		if(paths.isEmpty())
		{
			path = fDomain.save(efDomainPath.build(query,domain.getEntity(),paths));
			reloadPaths();
		}
	}
	
	public void deleteQuery() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.deleteEntity(query));
//		domain.setEntity(fAnalysis.find(fbAnalysis.getClassDomainEntity(),domain.getEntity()));
		fDomain.rm(query);
		reloadQueries();
		reset(false,true,true,true);
	}
	
	public void selectQuery()
	{
		reset(false,false,true,true);
		logger.info(AbstractLogMessage.selectEntity(query));
		query = efLang.persistMissingLangs(fDomain,localeCodes,query);
		query = efDescription.persistMissingLangs(fDomain,localeCodes,query);
		reloadPaths();
	}
	
	private void reloadPaths()
	{
		paths = fDomain.allForParent(fbDomain.getClassDomainPath(), query);
		if(!paths.isEmpty())
		{
			PATH p = paths.get(paths.size()-1);
			if(p.getAttribute()!=null)
			{
				if(p.getAttribute().getRelation()!=null)
				{
					entity = p.getAttribute().getEntity();
				}
			}
		}
	}
	
	private void reloadPath()
	{
		entity = null;
		if(path.getAttribute()!=null)
		{
			if(path.getAttribute().getRelation()!=null)
			{
				entity = path.getAttribute().getEntity();
			}
		}
	}
	
	public void addPath()
	{
		logger.info(AbstractLogMessage.createEntity(fbDomain.getClassDomainQuery()));
		path = efDomainPath.build(query,entity,paths);
		reloadAttributes();
	}
	
	public void savePath() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(path));
//		domain.setEntity(fAnalysis.find(fbAnalysis.getClassDomainEntity(),domain.getEntity()));
		path = fDomain.save(path);
		reloadPaths();
		reloadAttributes();
		reloadPath();
	}
	
	public void selectPath()
	{
		reset(false,false,false,true);
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(path));}
		reloadAttributes();
		reloadPath();
	}
	
	public void deletePath() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.deleteEntity(path));
//		domain.setEntity(fAnalysis.find(fbAnalysis.getClassDomainEntity(),domain.getEntity()));
		fDomain.rm(path);
		reloadPaths();
		reset(false,false,true,true);
	}
	
	private void reloadAttributes()
	{
		attributes = fDomain.fDomainAttributes(path.getEntity());
		if(debugOnInfo) {logger.info(AbstractLogMessage.reloaded(fbDomain.getClassDomainAttribute(), attributes, path.getEntity()));}
	}
	
	public void reorderDomains() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fDomain, sbhDomain.getList());}
	public void reorderQueries() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fDomain, queries);}
}