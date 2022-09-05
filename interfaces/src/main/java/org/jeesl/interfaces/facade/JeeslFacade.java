package org.jeesl.interfaces.facade;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.model.marker.EjbEquals;
import org.jeesl.interfaces.model.marker.jpa.EjbMergeable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.tenant.JeeslWithTenantSupport;
import org.jeesl.interfaces.model.with.date.ju.EjbWithRecord;
import org.jeesl.interfaces.model.with.date.ju.EjbWithTimeline;
import org.jeesl.interfaces.model.with.date.ju.EjbWithValidFrom;
import org.jeesl.interfaces.model.with.date.ju.EjbWithValidFromUntil;
import org.jeesl.interfaces.model.with.date.primitive.EjbWithYear;
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

public interface JeeslFacade extends JeeslIdFacade
{
	<L extends JeeslLang,D extends JeeslDescription, S extends EjbWithId, G extends JeeslGraphic<GT,F,FS>, GT extends JeeslGraphicType<L,D,GT,G>, F extends JeeslGraphicComponent<L,D,G,GT,F,FS>, FS extends JeeslGraphicShape<L,D,FS,G>> S loadGraphic(Class<S> cS, S status);
	
	//NAME
	<T extends EjbWithName> T fByName(Class<T> type, String name) throws JeeslNotFoundException;

	//EQUALS
	<E extends EjbEquals<T>,T extends EjbWithId> boolean equalsAttributes(Class<T> c,E object); 
	
	//CODE
	<T extends EjbWithCode, E extends Enum<E>> T fByEnum(Class<T> c, E code);
	<T extends EjbWithCode, E extends Enum<E>> T fByCode(Class<T> c, E code) throws JeeslNotFoundException;
	<T extends EjbWithCode> T fByCode(Class<T> c, String code) throws JeeslNotFoundException;
	<T extends EjbWithNrString> T fByNr(Class<T> c, String nr) throws JeeslNotFoundException;
	<T extends EjbWithTypeCode> T fByTypeCode(Class<T> c, String type, String code) throws JeeslNotFoundException;
	<T extends EjbWithNonUniqueCode> List<T> allByCode(Class<T> c, String code);
	
	<T extends EjbWithNr, P extends EjbWithId> T fByNr(Class<T> type, String parentName, P parent, long nr) throws JeeslNotFoundException;
		
	<T extends EjbWithType> List<T> allForType(Class<T> c, String type);
	
	//Category,Type,Status ...
	<C extends JeeslStatus<?,?,C>, W extends JeeslWithContext<C>> List<W> allForContext(Class<W> w, C context);
	<L extends JeeslLang, D extends JeeslDescription, C extends JeeslStatus<L,D,C>, W extends JeeslWithCategory<C>> List<W> allForCategory(Class<W> w, C category);
	<L extends JeeslLang, D extends JeeslDescription, T extends JeeslStatus<L,D,T>, W extends JeeslWithType<T>> List<W> allForType(Class<W> w, T type);
	<L extends JeeslLang, D extends JeeslDescription, S extends JeeslStatus<L,D,S>, W extends JeeslWithStatus<S>> List<W> allForStatus(Class<W> w, S status);
	
	// ORDERING
	<T extends Object> List<T> allOrdered(Class<T> cl, String by, boolean ascending);
	<T extends Object,I extends EjbWithId> List<T> allOrderedParent(Class<T> cl, String by, boolean ascending,String p1Name, I p1);
	<T extends EjbWithCode> List<T> allOrderedCode(Class<T> cl);
	<T extends EjbWithName> List<T> allOrderedName(Class<T> cl);
	<T extends EjbWithPosition> List<T> allOrderedPosition(Class<T> c);
	<T extends EjbWithPositionType, E extends Enum<E>> List<T> allOrderedPosition(Class<T> type, E enu);
	<T extends EjbWithPositionTypeVisible, E extends Enum<E>> List<T> allOrderedPositionVisible(Class<T> type, E enu);
	<T extends EjbWithPositionVisible> List<T> allOrderedPositionVisible(Class<T> type);
	<T extends EjbWithPositionParent, P extends EjbWithId> List<T> allOrderedPositionParent(Class<T> cl, P parent);
	<T extends EjbWithPositionVisibleParent, P extends EjbWithId> List<T> allOrderedPositionVisibleParent(Class<T> cl, P parent);
	<T extends EjbWithRecord> List<T> allOrderedRecord(Class<T> type, boolean ascending);
	<T extends EjbWithRecord, AND extends EjbWithId, OR extends EjbWithId> List<T> allOrderedForParents(Class<T> queryClass, List<ParentPredicate<AND>> lpAnd, List<ParentPredicate<OR>> lpOr,boolean ascending);
	<T extends EjbWithValidFrom> List<T> allOrderedValidFrom(Class<T> cl, boolean ascending);
	
	// MCS
	<T extends JeeslWithTenantSupport<REALM>, REALM extends JeeslTenantRealm<?,?,REALM,?>, RREF extends EjbWithId> List<T> all(Class<T> c, REALM realm, RREF rref);
	<T extends EjbWithNonUniqueCode, REALM extends JeeslTenantRealm<?,?,?,?>, RREF extends EjbWithId, E extends Enum<E>> T fByEnum(Class<T> type, REALM realm, RREF rref, E code);
	<T extends EjbWithNonUniqueCode, REALM extends JeeslTenantRealm<?,?,?,?>, RREF extends EjbWithId> T fByCode(Class<T> type, REALM realm, RREF rref, String code) throws JeeslNotFoundException;
	
	//Visibility
	<T extends EjbWithVisible, P extends EjbWithId> List<T> allVisible(Class<T> cl);
	
	//Persist
	<T extends Object> T persist(T o) throws JeeslConstraintViolationException;
	<T extends Object> T update(T o) throws JeeslConstraintViolationException,JeeslLockingException;
	
	<T extends EjbMergeable> T merge(T o) throws JeeslConstraintViolationException, JeeslLockingException;
	<T extends EjbMergeable> T mergeTransaction(T o) throws JeeslConstraintViolationException, JeeslLockingException;
	
	<T extends EjbSaveable> T save(T o) throws JeeslConstraintViolationException,JeeslLockingException;
	<T extends EjbSaveable> T save2(T o) throws JeeslConstraintViolationException,JeeslLockingException;
	<T extends EjbSaveable> void save(List<T> list) throws JeeslConstraintViolationException,JeeslLockingException;
	<T extends EjbSaveable> T saveTransaction(T o) throws JeeslConstraintViolationException,JeeslLockingException;
	<T extends EjbSaveable> void saveTransaction(List<T> list) throws JeeslConstraintViolationException,JeeslLockingException;
	
	<T extends EjbRemoveable> void rmTransaction(T o) throws JeeslConstraintViolationException;
	<T extends EjbRemoveable> void rm(T o) throws JeeslConstraintViolationException;
	<T extends EjbRemoveable> void rm(List<T> list) throws JeeslConstraintViolationException;
	<T extends EjbRemoveable> void rmTransaction(List<T> list) throws JeeslConstraintViolationException;
	<T extends EjbRemoveable> void rm(Set<T> set) throws JeeslConstraintViolationException;
	
	//Parent
	<T extends EjbWithParentAttributeResolver, P extends EjbWithId> List<T> allForParent(Class<T> c, P parent);
	<T extends JeeslWithParentAttributeStatus<STATUS>, P extends EjbWithId, STATUS extends JeeslStatus<?,?,STATUS>> List<T> allForParentStatus(Class<T> type, P parent, List<STATUS> status);
	<T extends JeeslWithParentAttributeType<TYPE>, P extends EjbWithId, TYPE extends JeeslStatus<?,?,TYPE>> List<T> allForParentType(Class<T> c, P parent, List<TYPE> type);
	<T extends EjbWithParentAttributeResolver, I extends EjbWithId> List<T> allForParents(Class<T> type, List<I> parents);
//	<T extends EjbWithId, I extends EjbWithId, A1 extends Enum<A1>> List<T> allForParent(Class<T> type, String p1Name, I p1);
	<T extends EjbWithId, A1 extends Enum<A1>, P1 extends EjbWithId> List<T> allForParent(Class<T> type, A1 a1, P1 p1);
	<T extends EjbWithId, I extends EjbWithId> List<T> allForParent(Class<T> type, String p1Name, I p1, int maxResults);
	<T extends EjbWithParentAttributeResolver, I extends EjbWithId> T oneForParent(Class<T> cl, I p1) throws JeeslNotFoundException;
	<T extends EjbWithId, I extends EjbWithId> T oneForParent(Class<T> cl, String p1Name, I p1) throws JeeslNotFoundException;
	<T extends EjbWithId, I extends EjbWithId> T oneForParents(Class<T> cl, String p1Name, I p1, String p2Name, I p2) throws JeeslNotFoundException;
	<T extends EjbWithId, I extends EjbWithId> T oneForParents(Class<T> cl, String p1Name, I p1, String p2Name, I p2, String p3Name, I p3) throws JeeslNotFoundException;
	<T extends EjbWithId, P extends EjbWithId> T oneForParents(Class<T> cl, List<ParentPredicate<P>> parents) throws JeeslNotFoundException;
	<T extends EjbWithId, I extends EjbWithId> List<T> allForParent(Class<T> type, String p1Name, I p1, String p2Name, I p2);
	<T extends EjbWithId, P extends EjbWithId> List<T> allForOrParents(Class<T> cl, List<ParentPredicate<P>> parents);
	<T extends EjbWithId, OR1 extends EjbWithId, OR2 extends EjbWithId> List<T> allForOrOrParents(Class<T> cl, List<ParentPredicate<OR1>> or1, List<ParentPredicate<OR2>> or2);
	<T extends EjbWithId, OR extends EjbWithId, AND extends EjbWithId> List<T> fForAndOrParents(Class<T> queryClass, List<ParentPredicate<AND>> lpAnd, List<ParentPredicate<OR>> lpOr);
	<T extends EjbWithId, P extends EjbWithId, GP extends EjbWithId> List<T> allForGrandParent(Class<T> queryClass, Class<P> pClass, String pName, GP grandParent, String gpName);
	<T extends EjbWithId, P extends EjbWithId, OR extends EjbWithId, AND extends EjbWithId> List<T> fForAndOrGrandParents(Class<T> queryClass, Class<P> parentClass, String parentName, List<ParentPredicate<AND>> lpAnd, List<ParentPredicate<OR>> lpOr);
	<T extends EjbWithId, P extends EjbWithId, OR1 extends EjbWithId, OR2 extends EjbWithId> List<T> fGrandParents(Class<T> queryClass, Class<P> parentClass, String parentName, List<ParentPredicate<OR1>> lpOr1, List<ParentPredicate<OR2>> lpOr2);
	
	//Record
	<T extends EjbWithRecord, P extends EjbWithId> List<T> allOrderedParentRecordBetween(Class<T> c, boolean ascending,String p1Name, P p1, Date from, Date to);
	<T extends EjbWithRecord, P extends EjbWithId> List<T> allOrderedParentsRecordBetween(Class<T> c, boolean ascending,String p1Name, List<P> parents, Date from, Date to);
	<T extends EjbWithRecord> List<T> inInterval(Class<T> clRecord, Date from, Date to);
	<T extends EjbWithRecord> T fFirst(Class<T> clRecord);
	<T extends EjbWithRecord> T fLast(Class<T> clRecord);
	
	//ValidFrom
	<T extends EjbWithValidFromUntil> T oneInRange(Class<T> c,Date record) throws JeeslNotFoundException;
	<T extends EjbWithValidFromAndParent, P extends EjbWithId> T fFirstValidFrom(Class<T> c, P parent, Date validFrom) throws JeeslNotFoundException;
	<T extends EjbWithValidFrom> T fFirstValidFrom(Class<T> type, String parentName, long id, Date validFrom) throws JeeslNotFoundException;
	
	//Timeline
	<T extends EjbWithTimeline> List<T> between(Class<T> clTracker, Date from, Date to);
	<T extends EjbWithTimeline, AND extends EjbWithId, OR extends EjbWithId> List<T> between(Class<T> clTimeline,Date from, Date to, List<ParentPredicate<AND>> lpAnd, List<ParentPredicate<OR>> lpOr);
	
	//Year
	<T extends EjbWithYear,P extends EjbWithId> T fByYear(Class<T> type, String p1Name, P p, int year) throws JeeslNotFoundException;
	
	//User
//	<L extends UtilsLang,D extends UtilsDescription,C extends UtilsSecurityCategory<L,D,C,R,V,U,A,AT,USER>,R extends UtilsSecurityRole<L,D,C,R,V,U,A,AT,USER>,V extends UtilsSecurityView<L,D,C,R,V,U,A,AT,USER>,U extends UtilsSecurityUsecase<L,D,C,R,V,U,A,AT,USER>,A extends UtilsSecurityAction<L,D,C,R,V,U,A,AT,USER>, USER extends UtilsUser<L,D,C,R,V,U,A,AT,USER>> List<USER> likeNameFirstLast(Class<USER> c, String query);
	<T extends EjbWithEmail> T fByEmail(Class<T> clazz, String email) throws JeeslNotFoundException;
	<L extends JeeslLang,T extends EjbWithId> List<T> fByNameAndLang(Class<T> type, Class<L> langType,  String lang, String name ) throws JeeslNotFoundException;

	<W extends JeeslWithType<T>, T extends JeeslStatus<?,?,T>> Json1Tuples<T> tpcWithType(Class<W> cWith, Class<T> cType);
}
