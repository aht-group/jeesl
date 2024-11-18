package org.jeesl.util.query.ejb.system;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.interfaces.model.system.job.core.JeeslJobStatus;
import org.jeesl.interfaces.model.system.job.template.JeeslJobTemplate;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.system.JeeslJobQuery;
import org.jeesl.util.query.ejb.AbstractEjbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbJobQuery <TEMPLATE extends JeeslJobTemplate<?,?,?,?,?,?>,
							STATUS extends JeeslJobStatus<?,?,STATUS,?>>
							extends AbstractEjbQuery implements JeeslJobQuery<TEMPLATE,STATUS>
{
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LoggerFactory.getLogger(EjbJobQuery.class);
	
	public static <TEMPLATE extends JeeslJobTemplate<?,?,?,?,?,?>, STATUS extends JeeslJobStatus<?,?,STATUS,?>>
		EjbJobQuery<TEMPLATE,STATUS> instance()
	{
		return new EjbJobQuery<>();
	}
	
	public EjbJobQuery()
	{       
		reset();
	}
	
	@Override public void reset()
	{

	}
	
	private Boolean tupleLoad;
	@Override public Boolean getTupleLoad() {return tupleLoad;}
	
	//Fetches
	public <E extends Enum<E>> EjbJobQuery<TEMPLATE,STATUS> addRootFetch(E e){if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	public EjbJobQuery<TEMPLATE,STATUS> distinct(boolean distinct) {super.setDistinct(distinct); return this;}
	
	//Lists
	@Override public EjbJobQuery<TEMPLATE,STATUS> id(EjbWithId id) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.add(id.getId()); return this;}
	@Override public <T extends EjbWithId> EjbJobQuery<TEMPLATE,STATUS> ids(List<T> ids) {logger.error("NYI"); return this;}
	@Override public EjbJobQuery<TEMPLATE,STATUS> idList(List<Long> list) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(list); return this;}
	@Override public EjbJobQuery<TEMPLATE,STATUS> codeList(List<String> list) {if(Objects.isNull(codeList)) {codeList = new ArrayList<>();} codeList.addAll(list); return this;}

	//LocalDate
	public EjbJobQuery<TEMPLATE,STATUS> ld1(LocalDate ld1) {this.localDate1 = ld1; return this;}
	public EjbJobQuery<TEMPLATE,STATUS> ld2(LocalDate ld2) {this.localDate2 = ld2; return this;}
	public EjbJobQuery<TEMPLATE,STATUS> ld3(LocalDate ld3) {this.ld3 = ld3; return this;}
	
	
	
	private List<TEMPLATE> systemJobTemplates;
	@Override public List<TEMPLATE> getSystemJobTemplates() {return systemJobTemplates;}
	public JeeslJobQuery<TEMPLATE,STATUS> add(TEMPLATE template)  {if(Objects.isNull(systemJobTemplates)) {systemJobTemplates = new ArrayList<>();} systemJobTemplates.add(template); return this;}
	
	private List<STATUS> systemJobStatus;
	@Override public List<STATUS> getSystemJobStatus() {return systemJobStatus;}

}