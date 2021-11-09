package org.jeesl.web.mbean.prototype.io.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.bean.JeeslAttributeBean;
import org.jeesl.api.facade.io.JeeslIoAttributeFacade;
import org.jeesl.factory.builder.io.IoAttributeFactoryBuilder;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCategory;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeContainer;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeData;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeItem;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeOption;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.util.comparator.pojo.BooleanComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAppAttributeBean <L extends JeeslLang, D extends JeeslDescription,
											R extends JeeslTenantRealm<L,D,R,?>,
											CAT extends JeeslAttributeCategory<L,D,R,CAT,?>,
											CATEGORY extends JeeslStatus<L,D,CATEGORY>,
											CRITERIA extends JeeslAttributeCriteria<L,D,R,CAT,CATEGORY,TYPE,OPTION>,
											TYPE extends JeeslStatus<L,D,TYPE>,
											OPTION extends JeeslAttributeOption<L,D,CRITERIA>,
											SET extends JeeslAttributeSet<L,D,R,CAT,CATEGORY,ITEM>,
											ITEM extends JeeslAttributeItem<CRITERIA,SET>,
											CONTAINER extends JeeslAttributeContainer<SET,DATA>,
											DATA extends JeeslAttributeData<CRITERIA,OPTION,CONTAINER>>
					implements JeeslAttributeBean<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAppAttributeBean.class);

	private JeeslIoAttributeFacade<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> fAttribute;
	private final IoAttributeFactoryBuilder<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> fbAttribute;

	public AbstractAppAttributeBean(IoAttributeFactoryBuilder<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> fbAttribute)
	{
		this.fbAttribute=fbAttribute;
		categories = new ArrayList<CATEGORY>();
		types = new ArrayList<TYPE>();
		mapCriteria = new HashMap<SET,List<CRITERIA>>();
		mapTableHeader = new HashMap<SET,List<CRITERIA>>();
		mapOption = new HashMap<CRITERIA,List<OPTION>>();
	}
	
	public void initSuper(JeeslIoAttributeFacade<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> fAttribute)
	{
		this.fAttribute=fAttribute;
		
		reloadCategories();
		reloadTypes();
		reloadCategories();
		reloadCriteria();
		reloadOptions();
	}
	
	private final List<CATEGORY> categories;
	@Override public List<CATEGORY> getCategories(){return categories;}
	@Override public void reloadCategories() {categories.clear();categories.addAll(fAttribute.allOrderedPositionVisible(fbAttribute.getClassCategory()));}
	
	private final List<TYPE> types;
	@Override public List<TYPE> getTypes(){return types;}
	@Override public void reloadTypes()
	{
		types.clear();
		for(TYPE type : fAttribute.allOrderedPositionVisible(fbAttribute.getClassType()))
		{
			boolean add=false;
			for(JeeslAttributeType.Code t : JeeslAttributeType.Code.values())
			{
				if(type.getCode().equals(t.toString())) {add=true;}
			}
			if(add) {types.add(type);}
		}
	}
	
	private final Map<SET,List<CRITERIA>> mapCriteria; @Override  public Map<SET,List<CRITERIA>> getMapCriteria() {return mapCriteria;}
	private final Map<SET,List<CRITERIA>> mapTableHeader; @Override  public Map<SET,List<CRITERIA>> getMapTableHeader() {return mapTableHeader;}
	private void reloadCriteria()
	{
		mapCriteria.clear();
		mapTableHeader.clear();
		
		for(SET s : fAttribute.all(fbAttribute.getClassSet()))
		{
			updateSet(s);
		}
	}
	
	public void updateSet(SET s)
	{
		List<CRITERIA> listCriteria = new ArrayList<CRITERIA>();
		List<CRITERIA> listTable = new ArrayList<CRITERIA>();
		
		for(ITEM item : fAttribute.allOrderedPositionVisibleParent(fbAttribute.getClassItem(),s))
		{
			listCriteria.add(item.getCriteria());
			if(BooleanComparator.active(item.getTableHeader())){listTable.add(item.getCriteria());}
		}
		mapCriteria.put(s, listCriteria);
		mapTableHeader.put(s, listTable);
	}
	
	/* 
	 * Updates the Hashmap where the given criteria is used
	 */
	@Override public void updateCriteria(CRITERIA criteria)
	{
		for(List<CRITERIA> list : mapCriteria.values())
		{
			int index = -1;
			for(int i=0;i<list.size();i++){if(list.get(i).equals(criteria)){index=i;break;}}
			if(index>=0){list.set(index,criteria);}
		}
		for(List<CRITERIA> list : mapTableHeader.values())
		{
			int index = -1;
			for(int i=0;i<list.size();i++){if(list.get(i).equals(criteria)){index=i;break;}}
			if(index>=0){list.set(index,criteria);}
		}
		
	}
	
	private final Map<CRITERIA,List<OPTION>> mapOption; @Override public Map<CRITERIA,List<OPTION>> getMapOption() {return mapOption;}
	private void reloadOptions()
	{
		mapOption.clear();
		for(OPTION o : fAttribute.allOrderedPosition(fbAttribute.getClassOption()))
		{
			if(!mapOption.containsKey(o.getCriteria())){mapOption.put(o.getCriteria(),new ArrayList<OPTION>());}
			mapOption.get(o.getCriteria()).add(o);
		}
	}
		
	protected String statistics()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Statistics");
		for(SET s : mapCriteria.keySet())
		{
			sb.append(s.getCode()+" "+mapCriteria.get(s).size());
		}
		return sb.toString();
	}
}