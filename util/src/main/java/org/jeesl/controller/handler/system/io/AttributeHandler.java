package org.jeesl.controller.handler.system.io;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jeesl.api.bean.JeeslAttributeBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoAttributeFacade;
import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoAttributeFactoryBuilder;
import org.jeesl.factory.ejb.io.attribute.EjbAttributeContainerFactory;
import org.jeesl.factory.ejb.io.attribute.EjbAttributeDataFactory;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.bean.AttributeBean;
import org.jeesl.interfaces.controller.handler.JeeslAttributeHandler;
import org.jeesl.interfaces.controller.handler.system.io.JeeslLogger;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCategory;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeContainer;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeData;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeItem;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeOption;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeType;
import org.jeesl.interfaces.model.module.attribute.JeeslWithAttributeContainer;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.util.query.ejb.io.EjbIoAttributeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttributeHandler<L extends JeeslLang, D extends JeeslDescription,
								R extends JeeslTenantRealm<L,D,R,?>,
								CAT extends JeeslAttributeCategory<L,D,R,CAT,?>,
								
								CRITERIA extends JeeslAttributeCriteria<L,D,R,CAT,TYPE,OPTION,SET>,
								TYPE extends JeeslAttributeType<L,D,TYPE,?>,
								OPTION extends JeeslAttributeOption<L,D,CRITERIA>,
								SET extends JeeslAttributeSet<L,D,R,CAT,ITEM>,
								ITEM extends JeeslAttributeItem<CRITERIA,SET>,
								CONTAINER extends JeeslAttributeContainer<SET,DATA>,
								DATA extends JeeslAttributeData<CRITERIA,OPTION,CONTAINER>>
	implements Serializable,JeeslAttributeHandler<CONTAINER>
{
	final static Logger logger = LoggerFactory.getLogger(AttributeHandler.class);
	private static final long serialVersionUID = 1L;

	private JeeslLogger jogger; public JeeslLogger getJogger() {return jogger;} public void setJogger(JeeslLogger jogger) {this.jogger = jogger;}
	private boolean debugOnInfo; public void setDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo = debugOnInfo;}
	private boolean showDescription; public boolean isShowDescription() {return showDescription;}
	
	private final JeeslIoAttributeFacade<R,CAT,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> fAttribute;
	private final JeeslAttributeBean<R,CAT,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> bAttribute;
	private final AttributeBean<CONTAINER> bean;
	
	private final IoAttributeFactoryBuilder<L,D,R,CAT,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> fbAttribute;
	private final EjbAttributeContainerFactory<SET,CONTAINER> efContainer;
	private final EjbAttributeDataFactory<CRITERIA,OPTION,CONTAINER,DATA> efData;
	
	private final Map<CRITERIA,DATA> data; public Map<CRITERIA,DATA> getData() {return data;}
	private final Map<CRITERIA,String[]> options; public Map<CRITERIA,String[]> getOptions() {return options;}
	private final Map<CONTAINER,Map<CRITERIA,DATA>> containers; public Map<CONTAINER, Map<CRITERIA, DATA>> getContainers() {return containers;}
	private final Map<CRITERIA,List<CONTAINER>> nestedContainers; public Map<CRITERIA, List<CONTAINER>> getNestedContainers() {return nestedContainers;}
	
	private SET attributeSet; public SET getAttributeSet() {return attributeSet;}
	private CONTAINER container;
	
	public AttributeHandler(JeeslFacesMessageBean bMessage,
			final JeeslIoAttributeFacade<R,CAT,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> fAttribute,
			final JeeslAttributeBean<R,CAT,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> bAttribute,
			final IoAttributeFactoryBuilder<L,D,R,CAT,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> fbAttribute,
			final AttributeBean<CONTAINER> bean)
	{
		this.fAttribute=fAttribute;
		this.bAttribute=bAttribute;
		this.fbAttribute=fbAttribute;
		this.bean=bean;
		
		debugOnInfo = false;
		showDescription = false;
		
		efContainer = fbAttribute.ejbContainer();
		efData = fbAttribute.ejbData();
		
		data = new HashMap<>();
		options = new HashMap<CRITERIA,String[]>();
		containers = new HashMap<CONTAINER,Map<CRITERIA,DATA>>();
		nestedContainers = new HashMap<>();
	}
	
	public void toggleDescription()
	{
		showDescription = !showDescription;
		if(debugOnInfo) {logger.info("Changed Description to "+showDescription);}
	}
	
	public <RREF extends EjbWithId, E extends Enum<E>> void init(E code)
	{
		try {init(fAttribute.fByCode(fbAttribute.getClassSet(), code));}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
	}
	public  void init(SET set)
	{
		this.attributeSet = set;
		if(debugOnInfo) {logger.info("Initialized with Attribute Set: "+this.attributeSet.toString());}
	}
	
	public void reset()
	{
		data.clear();
		options.clear();
		container = null;
	}
	
	public void reloadData()
	{
		data.clear();
		options.clear();
		
		if(debugOnInfo) {logger.info(this.getClass().getSimpleName()+" loading data for container: "+container.toString());}
		for(DATA d : fAttribute.fAttributeData(container))
		{
			if(debugOnInfo)
			{
				StringBuilder sb = new StringBuilder();
				sb.append("\t ").append(d.getCriteria().getPosition()).append(" ").append(d.getCriteria().getCode());
				if(d.getCriteria().getType().getCode().equals("selectMany")) {sb.append(" selectMany:").append(d.getValueOptions().size());}
				logger.info(sb.toString());
			}
			data.put(d.getCriteria(),d);
			if(d.getValueOptions().isEmpty()) {options.put(d.getCriteria(),new String[0]);}
			else
			{
				String[] tmp = new String[d.getValueOptions().size()];
				for(int i=0;i<d.getValueOptions().size();i++)
				{
					tmp[i]=d.getValueOptions().get(i).getCode();
				}
				options.put(d.getCriteria(),tmp);
			}
		}
	}
	
	public void save() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info("aaargh");
		if(debugOnInfo){logger.info(this.getClass().getName()+" saveData");}
		if(bean!=null) {bean.save(this);}
	}
	
	public <W extends JeeslWithAttributeContainer<CONTAINER>> void prepare(W ejb)
	{
		if(ejb!=null && ejb.getAttributeContainer()!=null)
		{
			container = ejb.getAttributeContainer();
			prepare();
		}
		else
		{
			container=null;
			prepare(container);
		}
	}
	
	public void prepare(CONTAINER container)
	{
		if(container!=null)
		{
			this.container=container;
		}
		else
		{
			this.container = efContainer.build(attributeSet);
		}		
		prepare();
	}
	
	private void prepare()
	{
		data.clear();
		if(EjbIdFactory.isSaved(container))
		{
			reloadData();
		}
		if(bAttribute.getMapCriteria().containsKey(attributeSet))
		{
			if(debugOnInfo) {logger.info(this.getClass().getSimpleName()+" preparing for "+attributeSet.getCode()+" with "+bAttribute.getMapCriteria().get(attributeSet).size()+" "+fbAttribute.getClassCriteria().getSimpleName());}
			for(CRITERIA c : bAttribute.getMapCriteria().get(attributeSet))
			{
				if(!data.containsKey(c))
				{
					DATA d = efData.build(container,c);
					if(c.getType().getCode().equals(JeeslAttributeType.Code.selectOne.toString()) && !BooleanComparator.active(c.getAllowEmpty()))
					{
						if(bAttribute.getMapOption().containsKey(c) && !bAttribute.getMapOption().isEmpty())
						d.setValueOption(bAttribute.getMapOption().get(c).get(0));
					}
					data.put(c,d);
				}
			}
		}
		else
		{
			logger.warn("The "+fbAttribute.getClassSet().getSimpleName()+" "+attributeSet.getCode()+" is not cached in "+bAttribute.getClass().getSimpleName());
		}
		if(debugOnInfo) {logger.info(this.getClass().getSimpleName()+" prepared for "+attributeSet.getCode()+" with "+data.size()+" "+fbAttribute.getClassData());}
	}
	
	public CONTAINER saveContainer() throws JeeslConstraintViolationException, JeeslLockingException
	
	{
		if(EjbIdFactory.isUnSaved(container))
		{
			container = fAttribute.save(container);
		}
		logger.info("Saving data: "+data.values().size());
		for(DATA d : data.values())
		{
			d.setContainer(container);
			if(options.containsKey(d.getCriteria()))
			{
				String[] tmp = options.get(d.getCriteria());
				List<OPTION> l = new ArrayList<OPTION>();
				for(String s : tmp)
				{
					for(OPTION o : bAttribute.getMapOption().get(d.getCriteria()))
					{
						if(o.getCode().equals(s)) {l.add(o);}
					}
					logger.info("\t"+s);
				}
				if(debugOnInfo) {logger.info(this.getClass().getSimpleName()+" Saving options "+d.getCriteria().getPosition()+" "+d.getCriteria().getCode()+" options:"+l.size());}
				if(l.isEmpty()){d.setValueOptions(null);}
				else {d.setValueOptions(l);}
			}
			else {d.setValueOptions(null);}
			
			if(d.getCriteria().getType().getCode().equals(JeeslAttributeType.Code.selectOne.toString()))
			{
				if(d.getValueOption()!=null)
				{
					d.setValueOption(fAttribute.find(fbAttribute.getClassOption(), d.getValueOption()));
				}
			}
			
			fAttribute.save(d);
		}
		reloadData();
		return container;
	}
	
	public <W extends JeeslWithAttributeContainer<CONTAINER>> void loadContainers(List<W> list)
	{
		if(debugOnInfo) {logger.info("Loading "+list.size()+" "+fbAttribute.getClassContainer().getSimpleName());}	
		
//		containers.clear();
//		for(W w : list)
//		{
//			Map<CRITERIA,DATA> map = new HashMap<CRITERIA,DATA>();
//			for(DATA d : fAttribute.fAttributeData(w.getAttributeContainer()))
//			{
//				map.put(d.getCriteria(),d);
//			}
//			containers.put(w.getAttributeContainer(), map);
//		}
//		if(Objects.nonNull(jogger)) {logger.info(jogger.milestone(this.getClass().getSimpleName()+"-loadContainers","for-loop",containers.size()));}
		
		containers.clear();
		EjbIoAttributeQuery<CRITERIA,CONTAINER,DATA> query = new EjbIoAttributeQuery<>();
		for(W w : list) {query.add(w.getAttributeContainer());}
		containers.putAll(efData.toMapContainerCriteriaDta(fAttribute.fAttributeData(query)));
		if(Objects.nonNull(jogger)) {logger.info(jogger.milestone(this.getClass().getSimpleName()+"-loadContainers","query",containers.size()));}
	}
}