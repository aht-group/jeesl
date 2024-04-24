package org.jeesl.util.query.ejb.io;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.io.JeeslIoLabelQuery;
import org.jeesl.model.ejb.io.db.JeeslCqLiteral;
import org.jeesl.util.query.cq.CqOrdering;
import org.jeesl.util.query.ejb.AbstractEjbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoLabelQuery<ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>
>
			extends AbstractEjbQuery
			implements JeeslIoLabelQuery<ENTITY>
{
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LoggerFactory.getLogger(EjbIoLabelQuery.class);
	
	public EjbIoLabelQuery()
	{       
		reset();
	}
	
	@Override public void reset()
	{
		super.reset();
	}
	
	//Fetches
	public <E extends Enum<E>> EjbIoLabelQuery<ENTITY> addRootFetch(E e){if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	public EjbIoLabelQuery<ENTITY> distinct(boolean distinct) {super.setDistinct(distinct); return this;}
	
	public EjbIoLabelQuery<ENTITY> orderBy(CqOrdering ordering) {super.addOrdering(ordering); return this;}
	
	//Lists
	@Override public EjbIoLabelQuery<ENTITY> id(EjbWithId id) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.add(id.getId()); return this;}
	@Override public <T extends EjbWithId> EjbIoLabelQuery<ENTITY> ids(List<T> ids) {logger.error("NYI"); return this;}
	@Override public EjbIoLabelQuery<ENTITY> idList(List<Long> list) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(list); return this;}
	@Override public EjbIoLabelQuery<ENTITY> codeList(List<String> list) {if(Objects.isNull(codeList)) {codeList = new ArrayList<>();} codeList.addAll(list); return this;}

	@Override public EjbIoLabelQuery<ENTITY> add(JeeslCqLiteral literal) {super.addCqLiteral(literal); return this;}
}