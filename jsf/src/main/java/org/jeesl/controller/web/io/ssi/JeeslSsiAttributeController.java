package org.jeesl.controller.web.io.ssi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.facade.io.JeeslIoSsiFacade;
import org.jeesl.controller.util.comparator.ejb.io.label.LabelEntityComparator;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.factory.builder.io.ssi.IoSsiCoreFactoryBuilder;
import org.jeesl.factory.builder.io.ssi.IoSsiDataFactoryBuilder;
import org.jeesl.factory.ejb.io.ssi.data.EjbIoSsiAttributeFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiCredential;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiAttribute;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiCleaning;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiData;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiStatus;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiContext;
import org.jeesl.interfaces.model.system.job.core.JeeslJobStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class JeeslSsiAttributeController <L extends JeeslLang,D extends JeeslDescription,
										SYSTEM extends JeeslIoSsiSystem<L,D>,
										CRED extends JeeslIoSsiCredential<SYSTEM>,
										MAPPING extends JeeslIoSsiContext<SYSTEM,ENTITY>,
										ATTRIBUTE extends JeeslIoSsiAttribute<MAPPING,ENTITY>,
										DATA extends JeeslIoSsiData<MAPPING,LINK,?,JOB>,
										LINK extends JeeslIoSsiStatus<L,D,LINK,?>,
										ENTITY extends JeeslRevisionEntity<L,D,?,?,?,?>,
										CLEANING extends JeeslIoSsiCleaning<L,D,CLEANING,?>,
										JOB extends JeeslJobStatus<L,D,JOB,?>,
										HOST extends JeeslIoSsiHost<L,D,SYSTEM>>
						implements Serializable,SbSingleBean
{
	//NYI, just a copy
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslSsiAttributeController.class);

	private final IoSsiCoreFactoryBuilder<L,D,SYSTEM,CRED,?> fbSsiCore;
	private final IoSsiDataFactoryBuilder<L,D,SYSTEM,MAPPING,ATTRIBUTE,DATA,LINK,?,ENTITY,CLEANING,JOB> fbSsiData;
	private final IoRevisionFactoryBuilder<L,D,?,?,?,?,?,ENTITY,?,?,?,?,?,?> fbRevision;

	private JeeslIoSsiFacade<SYSTEM,CRED,MAPPING,ATTRIBUTE,DATA,LINK,?,ENTITY,CLEANING,JOB,HOST> fSsi;

	private final SbSingleHandler<SYSTEM> sbhSystem; public SbSingleHandler<SYSTEM> getSbhSystem() {return sbhSystem;}
	private final SbSingleHandler<MAPPING> sbhMapping; public SbSingleHandler<MAPPING> getSbhMapping() {return sbhMapping;}
	private final SbSingleHandler<ENTITY> sbhEntity; public SbSingleHandler<ENTITY> getSbhEntity() {return sbhEntity;}

	private final EjbIoSsiAttributeFactory<MAPPING,ATTRIBUTE,ENTITY> efAttribute;

	private final Comparator<ENTITY> cpEntity;

	private final List<ENTITY> entities; public List<ENTITY> getEntities() {return entities;}
	private final List<ATTRIBUTE> attributes; public List<ATTRIBUTE> getAttributes() {return attributes;}

	private ATTRIBUTE attribute; public ATTRIBUTE getAttribute() {return attribute;} public void setAttribute(ATTRIBUTE attribute) {this.attribute = attribute;}


	public JeeslSsiAttributeController(final IoSsiCoreFactoryBuilder<L,D,SYSTEM,CRED,?> fbSsiCore,
									final IoSsiDataFactoryBuilder<L,D,SYSTEM,MAPPING,ATTRIBUTE,DATA,LINK,?,ENTITY,CLEANING,JOB> fbSsiData,
									final IoRevisionFactoryBuilder<L,D,?,?,?,?,?,ENTITY,?,?,?,?,?,?> fbRevision)
	{
		this.fbSsiCore=fbSsiCore;
		this.fbSsiData=fbSsiData;
		this.fbRevision=fbRevision;
		entities = new ArrayList<>();
		attributes = new ArrayList<>();

		sbhSystem = new SbSingleHandler<SYSTEM>(fbSsiCore.getClassSystem(),this);
		sbhMapping = new SbSingleHandler<MAPPING>(fbSsiData.getClassMapping(),this);
		sbhEntity = new SbSingleHandler<ENTITY>(fbRevision.getClassEntity(),this);

		cpEntity = fbRevision.cpEjbEntity(LabelEntityComparator.Type.position);

		efAttribute = fbSsiData.ejbAttribute();
	}

	public void postConstructSsiAttribute(JeeslIoSsiFacade<SYSTEM,CRED,MAPPING,ATTRIBUTE,DATA,LINK,?,ENTITY,CLEANING,JOB,HOST> fSsi)
	{
		this.fSsi=fSsi;
		entities.addAll(fSsi.all(fbRevision.getClassEntity()));
		Collections.sort(entities,cpEntity);

		sbhSystem.setList(fSsi.all(fbSsiCore.getClassSystem()));
		sbhSystem.silentCallback();
	}

	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		reset(true,true);
		if(item instanceof JeeslIoSsiSystem)
		{
			logger.info("item instanceof JeeslIoSsiSystem");
			sbhMapping.clear();
			sbhEntity.clear();
			sbhMapping.setList(fSsi.allForParent(fbSsiData.getClassMapping(),sbhSystem.getSelection()));
			sbhMapping.silentCallback();
		}
		else if(item instanceof JeeslIoSsiContext)
		{
			sbhEntity.clear();
			sbhEntity.setList(efAttribute.toListEntity(fSsi.allForParent(fbSsiData.getClassAttribute(),sbhMapping.getSelection())));
			sbhEntity.silentCallback();
		}
		else if(item instanceof JeeslRevisionEntity)
		{
			reload();
		}
	}

	private void reload()
	{
		reset(true,false);
		if(sbhMapping.isSelected() && sbhEntity.isSelected())
		{
			attributes.addAll(fSsi.allForParent(fbSsiData.getClassAttribute(), JeeslIoSsiAttribute.Attributes.mapping, sbhMapping.getSelection(), JeeslIoSsiAttribute.Attributes.entity, sbhEntity.getSelection()));
		}
	}

	private void reset(boolean rAttributes, boolean rAttribute)
	{
		if(rAttributes) {attributes.clear();}
		if(rAttribute) {attribute=null;}
	}

	public void selectAttribute()
	{
		logger.info(AbstractLogMessage.selectEntity(attribute));
	}

	public void addAttribute()
	{
		attribute = efAttribute.build(sbhMapping.getSelection());
		if(sbhEntity.isSelected()) {attribute.setEntity(sbhEntity.getSelection());}
	}

	public void saveAttribute() throws JeeslConstraintViolationException, JeeslLockingException
	{
		attribute.setMapping(fSsi.find(fbSsiData.getClassMapping(),attribute.getMapping()));
		attribute.setEntity(fSsi.find(fbRevision.getClassEntity(),attribute.getEntity()));
		attribute = fSsi.save(attribute);
		if(!sbhEntity.getList().contains(attribute.getEntity())) {sbhEntity.getList().add(attribute.getEntity()); if(!sbhEntity.isSelected()) {sbhEntity.setSelection(attribute.getEntity());}}
		reload();
	}

	public void deleteAttribute() throws JeeslConstraintViolationException, JeeslLockingException
	{
		fSsi.rm(attribute);
		reset(false,true);
		reload();
	}
}