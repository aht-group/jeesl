package org.jeesl.util.query.ejb.module;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.jeesl.interfaces.model.module.journal.JeeslJournalItem;
import org.jeesl.interfaces.model.module.journal.JeeslJournalDomain;
import org.jeesl.interfaces.model.module.journal.JeeslJournalBook;
import org.jeesl.interfaces.model.module.journal.JeeslJournalImpact;
import org.jeesl.interfaces.model.module.journal.JeeslJournalScope;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.util.query.ejb.AbstractEjbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbJournalQuery <BOOK extends JeeslJournalBook<SCOPE,ITEM>,
								SCOPE extends JeeslJournalDomain<?,?,SCOPE,?>,
								ITEM extends JeeslJournalItem<?,?,?,?,BOOK,IMPACT,CONF,USER>,
								IMPACT extends JeeslJournalImpact<?,?,IMPACT,?>,
								CONF extends JeeslJournalScope<?,?,CONF,?>,
								USER extends EjbWithId>
			extends AbstractEjbQuery
{
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LoggerFactory.getLogger(EjbJournalQuery.class);
	
	public EjbJournalQuery()
	{       
		reset();
	}
	
	@Override public void reset()
	{
		
	}
	
	//Fetches
	public <E extends Enum<E>> EjbJournalQuery<BOOK,SCOPE,ITEM,IMPACT,CONF,USER> addRootFetch(E e){if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	public EjbJournalQuery<BOOK,SCOPE,ITEM,IMPACT,CONF,USER> distinct(boolean distinct) {super.setDistinct(distinct); return this;}
	
	//Lists
	@Override public EjbJournalQuery<BOOK,SCOPE,ITEM,IMPACT,CONF,USER> id(EjbWithId id) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.add(id.getId()); return this;}
	@Override public <T extends EjbWithId> EjbJournalQuery<BOOK,SCOPE,ITEM,IMPACT,CONF,USER> ids(List<T> ids) {logger.error("NYI"); return this;}
	@Override public EjbJournalQuery<BOOK,SCOPE,ITEM,IMPACT,CONF,USER> idList(List<Long> list) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(list); return this;}
	@Override public EjbJournalQuery<BOOK,SCOPE,ITEM,IMPACT,CONF,USER> codeList(List<String> list) {if(Objects.isNull(codeList)) {codeList = new ArrayList<>();} codeList.addAll(list); return this;}

	//LocalDate
	public EjbJournalQuery<BOOK,SCOPE,ITEM,IMPACT,CONF,USER> ld1(LocalDate ld1) {this.localDate1 = ld1; return this;}
	public EjbJournalQuery<BOOK,SCOPE,ITEM,IMPACT,CONF,USER> ld2(LocalDate ld2) {this.localDate2 = ld2; return this;}
	public EjbJournalQuery<BOOK,SCOPE,ITEM,IMPACT,CONF,USER> ld3(LocalDate ld3) {this.ld3 = ld3; return this;}
	
	
	private List<BOOK> books;
	public EjbJournalQuery<BOOK,SCOPE,ITEM,IMPACT,CONF,USER> add(BOOK calendar) {if(Objects.isNull(books)) {books = new ArrayList<>();} books.add(calendar); return this;}
	public EjbJournalQuery<BOOK,SCOPE,ITEM,IMPACT,CONF,USER> addCalendars(List<BOOK> list) {if(Objects.isNull(books)) {books = new ArrayList<>();} books.addAll(list); return this;}
	public EjbJournalQuery<BOOK,SCOPE,ITEM,IMPACT,CONF,USER> addCalendars(Collection<BOOK> collection) {if(Objects.isNull(books)) {books = new ArrayList<>();} books.addAll(collection); return this;}
}