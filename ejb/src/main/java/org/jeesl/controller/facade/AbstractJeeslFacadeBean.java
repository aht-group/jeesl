package org.jeesl.controller.facade;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.facade.ParentPredicate;
import org.jeesl.interfaces.model.marker.EjbEquals;
import org.jeesl.interfaces.model.marker.jpa.EjbMergeable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicFigure;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.tenant.JeeslWithTenantSupport;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.parent.EjbWithValidFromAndParent;
import org.jeesl.interfaces.model.with.parent.JeeslWithParentAttributeStatus;
import org.jeesl.interfaces.model.with.parent.JeeslWithParentAttributeType;
import org.jeesl.interfaces.model.with.primitive.bool.EjbWithVisible;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNrString;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithType;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithTypeCode;
import org.jeesl.interfaces.model.with.primitive.date.EjbWithRecord;
import org.jeesl.interfaces.model.with.primitive.date.EjbWithTimeline;
import org.jeesl.interfaces.model.with.primitive.date.EjbWithValidFrom;
import org.jeesl.interfaces.model.with.primitive.date.EjbWithValidFromUntil;
import org.jeesl.interfaces.model.with.primitive.date.EjbWithYear;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithNr;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionParent;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionType;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionTypeVisible;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisibleParent;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithEmail;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithName;
import org.jeesl.interfaces.model.with.system.status.JeeslWithCategory;
import org.jeesl.interfaces.model.with.system.status.JeeslWithContext;
import org.jeesl.interfaces.model.with.system.status.JeeslWithStatus;
import org.jeesl.interfaces.model.with.system.status.JeeslWithType;
import org.jeesl.model.json.db.tuple.t1.Json1Tuples;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractJeeslFacadeBean implements JeeslFacade
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(AbstractJeeslFacadeBean.class);
	
	protected JeeslFacadeBean fJeesl;
	
	@Override public <E extends EjbEquals<T>, T extends EjbWithId> boolean equalsAttributes(Class<T> c,E object){return fJeesl.equalsAttributes(c,object);}
	@Override public <L extends JeeslLang,D extends JeeslDescription, S extends EjbWithId,G extends JeeslGraphic<L,D,GT,F,FS>, GT extends JeeslGraphicType<L,D,GT,G>, F extends JeeslGraphicFigure<L,D,G,GT,F,FS>, FS extends JeeslStatus<L,D,FS>> S loadGraphic(Class<S> cS, S status){return fJeesl.loadGraphic(cS,status);}
	
	// Persist
	@Override public <T extends EjbSaveable> void save(List<T> list) throws JeeslConstraintViolationException,JeeslLockingException {fJeesl.save(list);}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override public <T extends EjbSaveable> T saveTransaction(T o) throws JeeslConstraintViolationException,JeeslLockingException {return fJeesl.save(o);}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override public <T extends EjbSaveable> void saveTransaction(List<T> list) throws JeeslConstraintViolationException,JeeslLockingException {fJeesl.save(list);}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override public <T extends EjbMergeable> T mergeTransaction(T o) throws JeeslConstraintViolationException, JeeslLockingException {return fJeesl.mergeTransaction(o);}

	@Override public <T extends EjbMergeable> T merge(T o) throws JeeslConstraintViolationException, JeeslLockingException{return fJeesl.merge(o);}
	
	@Override public <T extends EjbSaveable> T save(T o) throws JeeslConstraintViolationException,JeeslLockingException {return fJeesl.save(o);}
	public <T extends EjbWithId> T saveProtected(T o) throws JeeslConstraintViolationException,JeeslLockingException {return fJeesl.saveProtected(o);}
	
	public <T extends Object> T persist(T o) throws JeeslConstraintViolationException {return fJeesl.persist(o);}
	public <T extends Object> T update(T o) throws JeeslConstraintViolationException, JeeslLockingException {return fJeesl.update(o);}
	
	// Remove
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) public <T extends EjbRemoveable> void rmTransaction(T o) throws JeeslConstraintViolationException {rmProtected(o);}
	public <T extends EjbRemoveable> void rm(T o) throws JeeslConstraintViolationException {rmProtected(o);}
	public <T extends EjbRemoveable> void rm(Set<T> set) throws JeeslConstraintViolationException {fJeesl.rm(set);}
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) public <T extends EjbRemoveable> void rmTransaction(List<T> list) throws JeeslConstraintViolationException {fJeesl.rm(list);}
	public <T extends EjbRemoveable> void rm(List<T> list) throws JeeslConstraintViolationException {fJeesl.rm(list);}
	public <T extends Object> void rmProtected(T o) throws JeeslConstraintViolationException {fJeesl.rmProtected(o);}
	
	// Finder
	public <T extends EjbWithId> T find(Class<T> type, T t) {return fJeesl.find(type, t);}
	public <T extends Object> T find(Class<T> type, long id) throws JeeslNotFoundException {return fJeesl.find(type, id);}
	public <T extends EjbWithName> T fByName(Class<T> type, String name) throws JeeslNotFoundException {return fJeesl.fByName(type, name);}
	@Override public <T extends EjbWithEmail> T fByEmail(Class<T> c, String email) throws JeeslNotFoundException {return fJeesl.fByEmail(c, email);}
	@Override public <T extends EjbWithId> List<T> find(Class<T> c, List<Long> ids) {return fJeesl.find(c, ids);}
	@Override public <T extends EjbWithId> List<T> find(Class<T> c, Set<Long> ids){return fJeesl.find(c, ids);}
	@Override public <T extends EjbWithId> long maxId(Class<T> c) {return fJeesl.maxId(c);}
	
	//Code
	@Override public <T extends EjbWithCode, E extends Enum<E>> T fByEnum(Class<T> c, E code) {return fJeesl.fByEnum(c,code);}
	@Override public <T extends EjbWithCode, E extends Enum<E>> T fByCode(Class<T> c, E code) throws JeeslNotFoundException {return fJeesl.fByCode(c, code);}
	@Override public <T extends EjbWithCode> T fByCode(Class<T> c, String code) throws JeeslNotFoundException {return fJeesl.fByCode(c, code);}
	@Override public <T extends EjbWithNrString> T fByNr(Class<T> c, String nr) throws JeeslNotFoundException {return fJeesl.fByNr(c, nr);}
	@Override public <T extends EjbWithTypeCode> T fByTypeCode(Class<T> c, String type, String code) throws JeeslNotFoundException {return fJeesl.fByTypeCode(c,type,code);}
	@Override public <T extends EjbWithNonUniqueCode> List<T> allByCode(Class<T> c, String code) {return fJeesl.allByCode(c,code);}
	
	// All
	@Override public <T extends Object> List<T> all(Class<T> type) {return fJeesl.all(type);}
	@Override public <T extends Object> List<T> all(Class<T> type,int maxResults) {return fJeesl.all(type,maxResults);}
	
	@Override public <T extends EjbWithType> List<T> allForType(Class<T> cl, String type) {return fJeesl.allForType(cl, type);}
	
	// List
	@Override public <T extends EjbWithId> List<T> list(Class<T> c, List<Long> list) {return fJeesl.list(c,list);}
	@Override public List<Long> listId(String nativeQuery) {return fJeesl.listId(nativeQuery);}
	
	@Override public <C extends JeeslStatus<?,?,C>, W extends JeeslWithContext<C>> List<W> allForContext(Class<W> w, C context) {return fJeesl.allForContext(w,context);}
	@Override public <L extends JeeslLang, D extends JeeslDescription, C extends JeeslStatus<L,D,C>, W extends JeeslWithCategory<C>> List<W> allForCategory(Class<W> w, C category) {return fJeesl.allForCategory(w, category);}
	@Override public <L extends JeeslLang, D extends JeeslDescription, T extends JeeslStatus<L,D,T>, W extends JeeslWithType<T>> List<W> allForType(Class<W> w, T type) {return fJeesl.allForType(w, type);}
	@Override public <L extends JeeslLang, D extends JeeslDescription, S extends JeeslStatus<L,D,S>, W extends JeeslWithStatus<S>> List<W> allForStatus(Class<W> w, S status) {return fJeesl.allForStatus(w, status);}
	
	// Ordering
	public <T extends EjbWithPosition> List<T> allOrderedPosition(Class<T> type) {return fJeesl.allOrderedPosition(type);}
	@Override public <T extends EjbWithPositionType, E extends Enum<E>> List<T> allOrderedPosition(Class<T> type, E enu) {return fJeesl.allOrderedPosition(type,enu);}
	@Override public <T extends EjbWithPositionTypeVisible, E extends Enum<E>> List<T> allOrderedPositionVisible(Class<T> type,E enu) {return fJeesl.allOrderedPositionVisible(type,enu);}
	public <T extends EjbWithPositionVisible> List<T> allOrderedPositionVisible(Class<T> cl) {return fJeesl.allOrderedPositionVisible(cl);}
	@Override public <T extends EjbWithPositionParent, P extends EjbWithId> List<T> allOrderedPositionParent(Class<T> cl, P parent) {return fJeesl.allOrderedPositionParent(cl,parent);}
	@Override public <T extends EjbWithPositionVisibleParent, P extends EjbWithId> List<T> allOrderedPositionVisibleParent(Class<T> cl, P parent) {return fJeesl.allOrderedPositionVisibleParent(cl,parent);}
	@Override public <T extends EjbWithCode> List<T> allOrderedCode(Class<T> c) {return fJeesl.allOrderedCode(c);}
	@Override public <T extends EjbWithName> List<T> allOrderedName(Class<T> c) {return fJeesl.allOrderedName(c);}
	public <T extends Object> List<T> allOrdered(Class<T> cl, String by, boolean ascending) {return fJeesl.allOrdered(cl, by, ascending);}
	public <T extends EjbWithRecord> List<T> allOrderedRecord(Class<T> type, boolean ascending) {return fJeesl.allOrderedRecord(type,ascending);}
	@Override public <T extends EjbWithRecord, I extends EjbWithId> List<T> allOrderedParentRecordBetween(Class<T> cl, boolean ascending, String p1Name, I p1,Date from, Date to) {return fJeesl.allOrderedParentRecordBetween(cl, ascending, p1Name, p1, from, to);}
	@Override public <T extends EjbWithRecord, P extends EjbWithId> List<T> allOrderedParentsRecordBetween(Class<T> c, boolean ascending, String p1Name, List<P> parents, Date from, Date to){return fJeesl.allOrderedParentsRecordBetween(c, ascending, p1Name, parents, from, to);}
	public <T, I extends EjbWithId> List<T> allOrderedParent(Class<T> cl,String by, boolean ascending, String p1Name, I p1) {return fJeesl.allOrderedParent(cl, by, ascending, p1Name, p1);}
	
	// MCS
	@Override public <T extends JeeslWithTenantSupport<REALM>, REALM extends JeeslTenantRealm<?,?,REALM,?>, RREF extends EjbWithId> List<T> all(Class<T> c, REALM realm, RREF rref){return fJeesl.all(c,realm,rref);}
	@Override public <T extends EjbWithNonUniqueCode, REALM extends JeeslTenantRealm<?, ?, ?, ?>, RREF extends EjbWithId, E extends Enum<E>> T fByEnum(Class<T> c, REALM realm, RREF rref, E code) {return fJeesl.fByEnum(c,realm,rref,code);}
	@Override public <T extends EjbWithNonUniqueCode, REALM extends JeeslTenantRealm<?, ?, ?, ?>, RREF extends EjbWithId> T fByCode(Class<T> c, REALM realm, RREF rref, String code) throws JeeslNotFoundException {return fJeesl.fByCode(c,realm,rref,code);}
	
	// Parent
	@Override public <T extends EjbWithParentAttributeResolver, I extends EjbWithId> T oneForParent(Class<T> type, I p1) throws JeeslNotFoundException{return fJeesl.oneForParent(type, p1);}
	@Override public <T extends EjbWithParentAttributeResolver, I extends EjbWithId> List<T> allForParent(Class<T> type, I p1) {return fJeesl.allForParent(type, p1);}
	@Override public <T extends JeeslWithParentAttributeStatus<STATUS>, P extends EjbWithId, STATUS extends JeeslStatus<?,?,STATUS>> List<T> allForParentStatus(Class<T> c, P parent, List<STATUS> status) {return fJeesl.allForParentStatus(c, parent, status);}
	@Override public <T extends JeeslWithParentAttributeType<TYPE>, P extends EjbWithId, TYPE extends JeeslStatus<?,?,TYPE>> List<T> allForParentType(Class<T> c, P parent, List<TYPE> type) {return fJeesl.allForParentType(c, parent, type);}
	@Override public <T extends EjbWithParentAttributeResolver, I extends EjbWithId> List<T> allForParents(Class<T> c, List<I> parents) {{return fJeesl.allForParents(c, parents);}}
	@Override public <T extends EjbWithId, A1 extends Enum<A1>, P1 extends EjbWithId> List<T> allForParent(Class<T> type, A1 a1, P1 p1){return fJeesl.allForParent(type,a1,p1);}
	@Override public <T extends EjbWithId, I extends EjbWithId> List<T> allForParent(Class<T> type, String p1Name, I p1, int maxResults) {return fJeesl.allForParent(type, p1Name, p1,maxResults);}
	public <T extends EjbWithId, I extends EjbWithId> List<T> allForParent(Class<T> type, String p1Name, I p1, String p2Name, I p2){return fJeesl.allForParent(type, p1Name, p1, p2Name, p2);}
	public <T extends EjbWithId, I extends EjbWithId> T oneForParent(Class<T> cl, String p1Name, I p1) throws JeeslNotFoundException {return fJeesl.oneForParent(cl, p1Name, p1);}
	public <T extends EjbWithNr, P extends EjbWithId> T fByNr(Class<T> type, String parentName, P parent, long nr) throws JeeslNotFoundException {return fJeesl.fByNr(type, parentName, parent, nr);}
	public <T extends EjbWithId, P extends EjbWithId> T oneForParents(Class<T> cl, List<ParentPredicate<P>> parents) throws JeeslNotFoundException {return fJeesl.oneForParents(cl, parents);}
	public <T extends EjbWithId, I extends EjbWithId> T oneForParents(Class<T> cl, String p1Name, I p1, String p2Name, I p2) throws JeeslNotFoundException {return fJeesl.oneForParents(cl, p1Name, p1, p2Name, p2);}
	public <T extends EjbWithId, I extends EjbWithId> T oneForParents(Class<T> cl, String p1Name, I p1, String p2Name, I p2, String p3Name, I p3) throws JeeslNotFoundException {return fJeesl.oneForParents(cl,p1Name,p1,p2Name,p2,p3Name,p3);}
	@Override public <T extends EjbWithId, P extends EjbWithId> List<T> allForOrParents(Class<T> cl, List<ParentPredicate<P>> parents) {return fJeesl.allForOrParents(cl, parents);}
	@Override public <T extends EjbWithId, OR1 extends EjbWithId, OR2 extends EjbWithId> List<T> allForOrOrParents(Class<T> cl, List<ParentPredicate<OR1>> or1, List<ParentPredicate<OR2>> or2){return fJeesl.allForOrOrParents(cl, or1,or2);}
	public <T extends EjbWithRecord, AND extends EjbWithId, OR extends EjbWithId> List<T> allOrderedForParents(Class<T> queryClass, List<ParentPredicate<AND>> lpAnd,List<ParentPredicate<OR>> lpOr, boolean ascending) {return fJeesl.allOrderedForParents(queryClass, lpAnd, lpOr, ascending);}
	public <T extends EjbWithId, OR extends EjbWithId, AND extends EjbWithId> List<T> fForAndOrParents(Class<T> queryClass, List<ParentPredicate<AND>> lpAnd, List<ParentPredicate<OR>> lpOr) {return fJeesl.fForAndOrParents(queryClass, lpAnd, lpOr);}
	
	// GrandParent
	@Override public <T extends EjbWithId, P extends EjbWithId, GP extends EjbWithId> List<T> allForGrandParent(Class<T> queryClass, Class<P> pClass, String pName, GP grandParent, String gpName) {return fJeesl.allForGrandParent(queryClass,pClass,pName,grandParent,gpName);}
	public <T extends EjbWithId, P extends EjbWithId, OR extends EjbWithId, AND extends EjbWithId> List<T> fForAndOrGrandParents(Class<T> queryClass, Class<P> parentClass, String parentName, List<ParentPredicate<AND>> lpAnd, List<ParentPredicate<OR>> lpOr){return fJeesl.fForAndOrGrandParents(queryClass, parentClass, parentName, lpAnd, lpOr);}
	public <T extends EjbWithId, P extends EjbWithId, OR1 extends EjbWithId, OR2 extends EjbWithId> List<T> fGrandParents(Class<T> queryClass, Class<P> parentClass, String parentName, List<ParentPredicate<OR1>> lpOr1, List<ParentPredicate<OR2>> lpOr2){return fJeesl.fGrandParents(queryClass, parentClass, parentName, lpOr1, lpOr2);}
	
	// ValidFrom
	@Override public <T extends EjbWithValidFromUntil> T oneInRange(Class<T> c, Date record) throws JeeslNotFoundException {return fJeesl.oneInRange(c, record);}
	@Override public <T extends EjbWithValidFromAndParent, P extends EjbWithId> T fFirstValidFrom(Class<T> c, P parent, Date validFrom) throws JeeslNotFoundException {return fJeesl.fFirstValidFrom(c,parent,validFrom);}
	@Override public <T extends EjbWithValidFrom> T fFirstValidFrom(Class<T> type, String parentName, long id, Date validFrom) throws JeeslNotFoundException {return fJeesl.fFirstValidFrom(type, parentName, id, validFrom);}
	@Override public <T extends EjbWithValidFrom> List<T> allOrderedValidFrom(Class<T> cl, boolean ascending){return fJeesl.allOrderedValidFrom(cl,ascending);}
	
	//Record
	public <T extends EjbWithRecord> List<T> inInterval(Class<T> clRecord, Date from, Date to) {return fJeesl.inInterval(clRecord, from, to);}
	public <T extends EjbWithRecord> T fFirst(Class<T> clRecord) {return fJeesl.fFirst(clRecord);}
	public <T extends EjbWithRecord> T fLast(Class<T> clRecord) {return fJeesl.fLast(clRecord);}
	
	//Timeline
	public <T extends EjbWithTimeline> List<T> between(Class<T> clTimeline, Date from, Date to) {return fJeesl.between(clTimeline, from, to);}
	public <T extends EjbWithTimeline, AND extends EjbWithId, OR extends EjbWithId> List<T> between(Class<T> clTimeline, Date from, Date to, List<ParentPredicate<AND>> lpAnd, List<ParentPredicate<OR>> lpOr) {return fJeesl.between(clTimeline, from, to, lpAnd, lpOr);}
	
	//Year
	public <T extends EjbWithYear, P extends EjbWithId> T fByYear(Class<T> type, String p1Name, P p, int year) throws JeeslNotFoundException {return fJeesl.fByYear(type, p1Name, p, year);}

	//Visibility
	@Override public <T extends EjbWithVisible, P extends EjbWithId> List<T> allVisible(Class<T> cl) {return fJeesl.allVisible(cl);}

	@Override
	public <L extends JeeslLang, T extends EjbWithId> List<T> fByNameAndLang(Class<T> type, Class<L> langType,
			String lang, String name) throws JeeslNotFoundException {
		return fJeesl.fByNameAndLang(type, langType, lang, name);
	}
	
	@Override public <W extends JeeslWithType<T>, T extends JeeslStatus<?, ?, T>> Json1Tuples<T> tpcWithType(Class<W> cWith,Class<T> cType) {return fJeesl.tpcWithType(cWith,cType);}

	//@Override public <L extends UtilsLang, D extends UtilsDescription, C extends UtilsSecurityCategory<L, D, C, R, V, U, A, USER>, R extends UtilsSecurityRole<L, D, C, R, V, U, A, USER>, V extends UtilsSecurityView<L, D, C, R, V, U, A, USER>, U extends UtilsSecurityUsecase<L, D, C, R, V, U, A, USER>, A extends UtilsSecurityAction<L, D, C, R, V, U, A, USER>, USER extends UtilsUser<L, D, C, R, V, U, A, USER>> List<USER> likeNameFirstLast(Class<USER> c, String query) {return fUtils.likeNameFirstLast(c,query);}
}