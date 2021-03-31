package org.jeesl.factory.builder.io;

import org.jeesl.api.bean.JeeslAttributeBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoAttributeFacade;
import org.jeesl.controller.handler.system.io.AttributeHandler;
import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.attribute.EjbAttributeContainerFactory;
import org.jeesl.factory.ejb.io.attribute.EjbAttributeCriteriaFactory;
import org.jeesl.factory.ejb.io.attribute.EjbAttributeDataFactory;
import org.jeesl.factory.ejb.io.attribute.EjbAttributeItemFactory;
import org.jeesl.factory.ejb.io.attribute.EjbAttributeOptionFactory;
import org.jeesl.factory.ejb.io.attribute.EjbAttributeSetFactory;
import org.jeesl.factory.xml.system.io.attribute.XmlAttributeFactory;
import org.jeesl.factory.xml.system.io.attribute.XmlAttributesFactory;
import org.jeesl.interfaces.bean.AttributeBean;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeContainer;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeData;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeItem;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeOption;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.jeesl.QueryAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoAttributeFactoryBuilder<L extends JeeslLang, D extends JeeslDescription,
									CATEGORY extends JeeslStatus<CATEGORY,L,D>,
									CRITERIA extends JeeslAttributeCriteria<L,D,CATEGORY,TYPE,OPTION>,
									TYPE extends JeeslStatus<TYPE,L,D>,
									OPTION extends JeeslAttributeOption<L,D,CRITERIA>,
									SET extends JeeslAttributeSet<L,D,CATEGORY,ITEM>,
									ITEM extends JeeslAttributeItem<CRITERIA,SET>,
									CONTAINER extends JeeslAttributeContainer<SET,DATA>,
									DATA extends JeeslAttributeData<CRITERIA,OPTION,CONTAINER>>
				extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(IoAttributeFactoryBuilder.class);

	private final Class<CATEGORY> cCat; public Class<CATEGORY> getClassCategory() {return cCat;}
	private final Class<CRITERIA> cCriteria; public Class<CRITERIA> getClassCriteria() {return cCriteria;}
	private final Class<TYPE> cType; public Class<TYPE> getClassType() {return cType;}
	private final Class<OPTION> cOption; public Class<OPTION> getClassOption() {return cOption;}
	private final Class<SET> cSet; public Class<SET> getClassSet() {return cSet;}
	private final Class<ITEM> cItem; public Class<ITEM> getClassItem() {return cItem;}
	private final Class<CONTAINER> cContainer; public Class<CONTAINER> getClassContainer() {return cContainer;}
	private final Class<DATA> cData; public Class<DATA> getClassData() {return cData;}
    
	public IoAttributeFactoryBuilder(final Class<L> cL, final Class<D> cD, final Class<CATEGORY> cCat, final Class<CRITERIA> cCriteria, final Class<TYPE> cType, final Class<OPTION> cOption, final Class<SET> cSet, final Class<ITEM> cItem, final Class<CONTAINER> cContainer, final Class<DATA> cData)
	{
		super(cL,cD);
		this.cCat=cCat;
		this.cCriteria=cCriteria;
		this.cType=cType;
		this.cOption=cOption;
		this.cSet=cSet;
		this.cItem=cItem;
		this.cContainer=cContainer;
		this.cData=cData;
	}
	
	public EjbAttributeCriteriaFactory<L,D,CATEGORY,CRITERIA,TYPE> ejbCriteria(){return new EjbAttributeCriteriaFactory<L,D,CATEGORY,CRITERIA,TYPE>(cCriteria);}
	public EjbAttributeOptionFactory<CRITERIA,OPTION> ejbOption() {return new EjbAttributeOptionFactory<CRITERIA,OPTION>(cOption);}
	public EjbAttributeSetFactory<L,D,CATEGORY,SET,ITEM> ejbSet() {return new EjbAttributeSetFactory<L,D,CATEGORY,SET,ITEM>(cSet);}
	public EjbAttributeItemFactory<CRITERIA,SET,ITEM> ejbItem() {return new EjbAttributeItemFactory<>(cItem);}
	public EjbAttributeContainerFactory<SET,CONTAINER> ejbContainer() {return new EjbAttributeContainerFactory<SET,CONTAINER>(cContainer);}
	public EjbAttributeDataFactory<CRITERIA,OPTION,CONTAINER,DATA> ejbData() {return new EjbAttributeDataFactory<CRITERIA,OPTION,CONTAINER,DATA>(cData);}
	
	public XmlAttributesFactory<L,D,CATEGORY,CRITERIA,OPTION,SET,ITEM,DATA> xmlAttributes(QueryAttribute query) {return new XmlAttributesFactory<>(query);}
	public XmlAttributeFactory<L,D,CRITERIA,OPTION,ITEM,DATA> xmlAttribute(QueryAttribute query) {return new XmlAttributeFactory<L,D,CRITERIA,OPTION,ITEM,DATA>(query);}
	
	public AttributeHandler<L,D,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> handler(JeeslFacesMessageBean bMessage, JeeslIoAttributeFacade<L,D,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> fAttribute, JeeslAttributeBean<L,D,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> bAttribute, AttributeBean<CONTAINER> bean)
	{
		return new AttributeHandler<L,D,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA>(bMessage,fAttribute,bAttribute,this,bean);
	}
}