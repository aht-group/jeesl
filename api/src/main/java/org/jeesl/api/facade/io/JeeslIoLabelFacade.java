package org.jeesl.api.facade.io;

import java.util.Date;
import java.util.List;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.label.entity.EjbWithRevisionAttributes;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionMissingLabel;
import org.jeesl.interfaces.model.io.label.er.JeeslRevisionDiagram;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionEntityMapping;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionScope;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionScopeType;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionView;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionViewMapping;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.io.JeeslIoLabelQuery;
import org.jeesl.model.json.system.io.revision.JsonRevision;

public interface JeeslIoLabelFacade <L extends JeeslLang,D extends JeeslDescription,
									RC extends JeeslRevisionCategory<L,D,RC,?>,
									RV extends JeeslRevisionView<L,D,RVM>,
									RVM extends JeeslRevisionViewMapping<RV,RE,REM>,
									RS extends JeeslRevisionScope<L,D,RC,RA>,
									RST extends JeeslRevisionScopeType<L,D,RST,?>,
									RE extends JeeslRevisionEntity<L,D,RC,REM,RA,ERD>,
									REM extends JeeslRevisionEntityMapping<RS,RST,RE>,
									RA extends JeeslRevisionAttribute<L,D,RE,?,?>,				
									ERD extends JeeslRevisionDiagram<L,D,RC>,
									RML extends JeeslRevisionMissingLabel>
			extends JeeslFacade
{
	public static int typeCreate = 0;

	public static enum Scope{live,revision}

	RV load(Class<RV> cView, RV view);
	RS load(Class<RS> cScope, RS scope);
	RE load(Class<RE> cEntity, RE entity);

	List<RE> findLabelEntities(JeeslIoLabelQuery<RE> query);
	List<RA> fLabelAttributes(JeeslIoLabelQuery<RE> query);
	
	List<RE> findLabelEntities(RC categories, ERD diagram);
	List<RS> findRevisionScopes(List<RC> categories, boolean showInvisibleScopes);
	
	
	void rm(Class<RVM> cMappingView, RVM mapping) throws JeeslConstraintViolationException;

	<W extends EjbWithRevisionAttributes<RA>> RA save(Class<W> cW, W entity, RA attribute) throws JeeslLockingException, JeeslConstraintViolationException;
	<W extends EjbWithRevisionAttributes<RA>> void rm(Class<W> cW, W entity, RA attribute) throws JeeslLockingException, JeeslConstraintViolationException;

	<T extends EjbWithId> T jpaTree(Class<T> c, String jpa, long id) throws JeeslNotFoundException;

	<T extends EjbWithId> List<T> revisions(Class<T> c, List<Long> ids);

	<T extends EjbWithId> List<Long> ids(Class<T> c, JeeslIoLabelFacade.Scope scope);
	<T extends EjbWithId> List<JsonRevision> findCreated(Class<T> c, Date from, Date to);

	void addMissingLabel(RML rML);
	void cleanMissingLabels(Class<RML> cRml);
	RE fRevisionEntity(String jscn) throws JeeslNotFoundException;
}