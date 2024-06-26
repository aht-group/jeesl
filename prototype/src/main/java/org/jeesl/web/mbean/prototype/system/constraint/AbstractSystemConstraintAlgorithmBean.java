package org.jeesl.web.mbean.prototype.system.constraint;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.system.JeeslSystemConstraintFacade;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.system.ConstraintFactoryBuilder;
import org.jeesl.factory.ejb.system.constraint.algorithm.EjbConstraintAlgorithmFactory;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.model.system.constraint.algorithm.JeeslConstraintAlgorithm;
import org.jeesl.interfaces.model.system.constraint.algorithm.JeeslConstraintAlgorithmGroup;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraint;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintCategory;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintLevel;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintResolution;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintScope;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSystemConstraintAlgorithmBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
														GROUP extends JeeslConstraintAlgorithmGroup<L,D,GROUP,?>,
														ALGORITHM extends JeeslConstraintAlgorithm<L,D,GROUP>,
														SCOPE extends JeeslConstraintScope<L,D,CONCAT>,
														CONCAT extends JeeslConstraintCategory<L,D,CONCAT,?>,
														CONSTRAINT extends JeeslConstraint<L,D,SCOPE,CONCAT,LEVEL,TYPE>,
														LEVEL extends JeeslConstraintLevel<L,D,LEVEL,?>,
														TYPE extends JeeslConstraintType<L,D,TYPE,?>,
														RESOLUTION extends JeeslConstraintResolution<L,D,CONSTRAINT>>
					extends AbstractSystemConstraintBean<L,D,LOC,GROUP,ALGORITHM,SCOPE,CONCAT,CONSTRAINT,LEVEL,TYPE,RESOLUTION>
					implements Serializable//,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractSystemConstraintAlgorithmBean.class);
	
	protected final SbMultiHandler<GROUP> sbhAlgorithmCategory; public SbMultiHandler<GROUP> getSbhAlgorithmCategory() {return sbhAlgorithmCategory;}
	
	protected final EjbConstraintAlgorithmFactory<L,D,GROUP,ALGORITHM> efAlgorithm;
	
	private List<ALGORITHM> algorithms; public List<ALGORITHM> getAlgorithms() {return algorithms;}

	private ALGORITHM algorithm; public ALGORITHM getAlgorithm() {return algorithm;} public void setAlgorithm(ALGORITHM algorithm) {this.algorithm = algorithm;}

	
	public AbstractSystemConstraintAlgorithmBean(ConstraintFactoryBuilder<L,D,GROUP,ALGORITHM,SCOPE,CONCAT,CONSTRAINT,LEVEL,TYPE,RESOLUTION> fbConstraint)
	{
		super(fbConstraint);
		efAlgorithm = fbConstraint.algorithm();
	
		sbhAlgorithmCategory = new SbMultiHandler<GROUP>(fbConstraint.getClassAlgorithmCategory(),this);
	}
	
	protected void initConstraintAlgorithms(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
											JeeslSystemConstraintFacade<L,D,ALGORITHM,GROUP,SCOPE,CONSTRAINT,CONCAT,LEVEL,TYPE,RESOLUTION> fConstraint)
	{
		super.initConstraint(bTranslation,bMessage,fConstraint);
		initImplementationSettings();
		reloadAlgorithms();
	}
	
	protected abstract void initImplementationSettings();
	
	@Override public void toggled(SbToggleSelection handler, Class<?> c)
	{
		logger.info(AbstractLogMessage.toggled(c));
//		if(fbAttribute.getClassCategory().isAssignableFrom(c))
		{
			reloadAlgorithms();
		}
//		sbhCategory.debug(true);
	}
	
	public void resetAlgorithm() {reset(true);}
	private void reset(boolean rAlgorithm)
	{
		if(rAlgorithm) {algorithm=null;}
	}
	
	private void reloadAlgorithms()
	{
		algorithms = fConstraint.allForParents(fbConstraint.getClassAlgorithm(), sbhAlgorithmCategory.getSelected());
		if(debugOnInfo) {logger.info(AbstractLogMessage.reloaded(fbConstraint.getClassAlgorithm(),algorithms));}
	}
	
	public void addAlgorithm()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(fbConstraint.getClassAlgorithm()));}
		algorithm = efAlgorithm.build(null,algorithms);
		algorithm.setName(efLang.createEmpty(localeCodes));
		algorithm.setDescription(efDescription.createEmpty(localeCodes));
		reset(false);
	}
	
	public void saveAlgorithm() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(algorithm));}
		algorithm.setCategory(fConstraint.find(fbConstraint.getClassAlgorithmCategory(),algorithm.getCategory()));
		algorithm = fConstraint.save(algorithm);
		reloadAlgorithms();
	}
	
	public void selectAlgorithm()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(algorithm));}
		algorithm = efLang.persistMissingLangs(fConstraint,localeCodes,algorithm);
		algorithm = efDescription.persistMissingLangs(fConstraint,localeCodes,algorithm);
		reloadAlgorithms();
		reset(false);
	}
	
	public void deleteAlgorithm() throws JeeslConstraintViolationException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.deleteEntity(algorithm));}
		fConstraint.rm(algorithm);
		reloadAlgorithms();
		reset(true);
	}
	
	protected void reorderAlgorithms() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fConstraint, fbConstraint.getClassAlgorithm(),algorithms);Collections.sort(algorithms,cpAlgorithm);}
}