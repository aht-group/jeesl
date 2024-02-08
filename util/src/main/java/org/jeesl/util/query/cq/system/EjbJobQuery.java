package org.jeesl.util.query.cq.system;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.interfaces.model.system.job.template.JeeslJobTemplate;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.AbstractEjbQuery;
import org.jeesl.interfaces.util.query.system.JeeslJobQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbJobQuery <TEMPLATE extends JeeslJobTemplate<?,?,?,?,?,?>>
							extends AbstractEjbQuery implements JeeslJobQuery<TEMPLATE>
{
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LoggerFactory.getLogger(EjbJobQuery.class);
	
	public static <TEMPLATE extends JeeslJobTemplate<?,?,?,?,?,?>>
		EjbJobQuery<TEMPLATE> instance()
	{
		return new EjbJobQuery<>();
	}
	
	private EjbJobQuery()
	{       
		reset();
	}
	
	@Override public void reset()
	{

	}
	
	private Boolean tupleLoad;
	@Override public Boolean getTupleLoad() {return tupleLoad;}
	
	//Fetches
	public <E extends Enum<E>> EjbJobQuery<TEMPLATE> addRootFetch(E e){if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	public EjbJobQuery<TEMPLATE> distinct(boolean distinct) {super.setDistinct(distinct); return this;}
	
	//Lists
	@Override public EjbJobQuery<TEMPLATE> id(EjbWithId id) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.add(id.getId()); return this;}
	@Override public <T extends EjbWithId> EjbJobQuery<TEMPLATE> ids(List<T> ids) {logger.error("NYI"); return this;}
	@Override public EjbJobQuery<TEMPLATE> idList(List<Long> list) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(list); return this;}
	@Override public EjbJobQuery<TEMPLATE> codeList(List<String> list) {if(Objects.isNull(codeList)) {codeList = new ArrayList<>();} codeList.addAll(list); return this;}

	//LocalDate
	public EjbJobQuery<TEMPLATE> ld1(LocalDate ld1) {this.localDate1 = ld1; return this;}
	public EjbJobQuery<TEMPLATE> ld2(LocalDate ld2) {this.localDate2 = ld2; return this;}
	public EjbJobQuery<TEMPLATE> ld3(LocalDate ld3) {this.ld3 = ld3; return this;}
	
	
	
	private List<TEMPLATE> systemJobTemplates;
	@Override public List<TEMPLATE> getSystemJobTemplates() {return systemJobTemplates;}
	@Override public JeeslJobQuery<TEMPLATE> add(TEMPLATE template)  {if(Objects.isNull(systemJobTemplates)) {systemJobTemplates = new ArrayList<>();} systemJobTemplates.add(template); return this;}

}