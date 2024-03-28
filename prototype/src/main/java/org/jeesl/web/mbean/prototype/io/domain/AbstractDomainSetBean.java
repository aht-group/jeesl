package org.jeesl.web.mbean.prototype.io.domain;

import java.io.Serializable;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoDomainFacade;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.IoDomainFactoryBuilder;
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
import org.jeesl.jsf.handler.PositionListReorderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDomainSetBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
						DOMAIN extends JeeslDomain<L,ENTITY>,
						QUERY extends JeeslDomainQuery<L,D,DOMAIN,PATH>,
						PATH extends JeeslDomainPath<L,D,QUERY,ENTITY,ATTRIBUTE>,
						ENTITY extends JeeslRevisionEntity<L,D,?,?,ATTRIBUTE,?>,
						ATTRIBUTE extends JeeslRevisionAttribute<L,D,ENTITY,?,?>,
						SET extends JeeslDomainSet<L,D,DOMAIN>,
						ITEM extends JeeslDomainItem<QUERY,SET>>
					extends AbstractDomainBean<L,D,LOC,DOMAIN,QUERY,PATH,ENTITY,ATTRIBUTE,SET,ITEM>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractDomainSetBean.class);

	private List<SET> sets; public List<SET> getSets(){return sets;}
	private List<DOMAIN> domains; public List<DOMAIN> getDomains(){return domains;}
	private List<ITEM> items; public List<ITEM> getItems(){return items;}
	
	private SET set; public SET getSet() {return set;} public void setSet(SET set) {this.set = set;}
	private ITEM item; public ITEM getItem() {return item;} public void setItem(ITEM item) {this.item = item;}
	
	public AbstractDomainSetBean(IoDomainFactoryBuilder<L,D,DOMAIN,QUERY,PATH,ENTITY,ATTRIBUTE,SET,ITEM> fbDomain)
	{
		super(fbDomain);
	}
	
	protected void postConstructDomainQuery(String userLocale, JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
			JeeslIoDomainFacade<L,D,DOMAIN,QUERY,PATH,ENTITY,ATTRIBUTE,SET,ITEM> fDomain)
	{
		super.postConstructDomain(bTranslation,bMessage,fDomain);
		domains = fDomain.all(fbDomain.getClassDomain());
		reloadSets();
	}
	
	private void reset(boolean rSet, boolean rItem)
	{
		if(rSet){}
		if(rItem){}
	}
	
	private void reloadSets()
	{
		sets = fDomain.allOrderedPosition(fbDomain.getClassDomainSet());
	}
	
	public void addSet()
	{
		logger.info(AbstractLogMessage.createEntity(fbDomain.getClassDomainSet()));
		set = fbDomain.ejbSet().build(null,sets);
		set.setName(efLang.createEmpty(localeCodes));
		set.setDescription(efDescription.createEmpty(localeCodes));
	}
	
	public void saveSet() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(set));
		set.setDomain(fDomain.find(fbDomain.getClassDomain(),set.getDomain()));
		set = fDomain.save(set);
		reloadSets();
		reloadItems();
	}
	
	public void selectSet()
	{
		reset(false,true);
		logger.info(AbstractLogMessage.selectEntity(set));
		set = efLang.persistMissingLangs(fDomain,localeCodes,set);
		set = efDescription.persistMissingLangs(fDomain,localeCodes,set);

		reloadItems();
	}
	
	private void reloadItems()
	{
		queries = fDomain.allForParent(fbDomain.getClassDomainQuery(), set.getDomain());
		items = fDomain.allForParent(fbDomain.getClassDomainItem(), set);
	}
	
	public void addItem()
	{
		logger.info(AbstractLogMessage.createEntity(fbDomain.getClassDomainItem()));
		item = fbDomain.ejbItem().build(set,null,items);
	}
	
	public void saveItem() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(item));
		item.setQuery(fDomain.find(fbDomain.getClassDomainQuery(),item.getQuery()));
		item = fDomain.save(item);
		reloadItems();
	}
	
	public void deleteItem() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.deleteEntity(item));
//		domain.setEntity(fAnalysis.find(fbAnalysis.getClassDomainEntity(),domain.getEntity()));
		fDomain.rm(item);
		reloadItems();
		reset(false,true);
	}
	
	public void selectItem()
	{
		reset(false,false);
		logger.info(AbstractLogMessage.selectEntity(item));
	}
	
	public void reorderSets() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fDomain, sets);}
	public void reorderItems() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fDomain, items);}
}