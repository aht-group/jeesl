package org.jeesl.web.mbean.prototype.io.revision;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoRevisionFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.factory.builder.system.LocaleFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionMissingLabel;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class AbstractAdminRevisionMissingLabelBean <L extends JeeslLang, D extends JeeslDescription,
													LOC extends JeeslLocale<L,D,LOC,?>,
													RML extends JeeslRevisionMissingLabel>
	extends AbstractAdminBean<L,D,LOC> implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminRevisionMissingLabelBean.class);

	private RML missingLabel;public RML getMissingLabel() {return missingLabel;} public void setMissingLabel(RML missingLabel) {this.missingLabel = missingLabel;}
	private JeeslIoRevisionFacade<L,D,?,?,?,?,?,?,?,?,?,?,?,RML> fRevision;
	private List<RML> missingLabels; public List<RML> getMissingLabels() {return missingLabels;}
	private IoRevisionFactoryBuilder<L,D,?,?,?,?,?,?,?,?,?,?,?,RML> fbRevision;

	private SbMultiHandler<LOC> sbhStatus;
	public SbMultiHandler<LOC> getSbhStatus() {return sbhStatus;}

	private LocaleFactoryBuilder<L,D,LOC> fbLocale;

	public AbstractAdminRevisionMissingLabelBean(final IoRevisionFactoryBuilder<L,D,?,?,?,?,?,?,?,?,?,?,?,RML> fbRevision, LocaleFactoryBuilder<L,D,LOC> fbLocale)
	{
		super(fbRevision.getClassL(), fbRevision.getClassD());
		this.fbRevision = fbRevision;
		this.fbLocale = fbLocale;
		this.sbhStatus = new SbMultiHandler<LOC>(fbLocale.getClassLocale(), this);
	}

	protected void postConstructMissingEntity(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage, JeeslIoRevisionFacade<L,D,?,?,?,?,?,?,?,?,?,?,?,RML> fRevision, String defaultLangCode) {
		super.initJeeslAdmin(bTranslation, bMessage);
		this.fRevision = fRevision;
		List<LOC> langList =fRevision.allOrderedPositionVisible(fbLocale.getClassLocale());
		sbhStatus.setList(langList);
		LOC selecedLang = null;
		for (LOC lang : langList)
		{
			if (lang.getCode().equals(defaultLangCode))
			{
				selecedLang = lang;
			}
		}
		sbhStatus.preSelect(selecedLang);

		if (fRevision == null) {logger.warn(JeeslIoRevisionFacade.class.getSimpleName() + " is NULL");}
		reloadMissingLabels();
	}

	public void reloadMissingLabels()
	{
		if (debugOnInfo) {logger.info("reloadMissingLabels");}

		missingLabels = fRevision.all(fbRevision.getClassMissingRevision());
		missingLabels = sbhStatusfilter(missingLabels);
		if (debugOnInfo) {logger.info(AbstractLogMessage.reloaded(fbRevision.getClassMissingRevision(), missingLabels));}
	}

	private List<RML> sbhStatusfilter(List<RML> missingLabels)
	{
		if (debugOnInfo) {logger.info("sbhStatusfilter");}

		if (sbhStatus.getSelected().size() > 0)
		{
			ArrayList<String> langKeyList = new ArrayList<String>();
			for (LOC lang : sbhStatus.getSelected())
			{
				langKeyList.add(lang.getCode());
			}
			ArrayList<RML> filteredMissingLabels = new ArrayList<RML>();
			for (RML rml : missingLabels)
			{
				//if (debugOnInfo) {logger.info("processing : " + rml.getMissingCode());}
				if(langKeyList.contains(rml.getMissingLocal())) {
					//if (debugOnInfo) {logger.info("found : " +rml.getMissingCode());}
					filteredMissingLabels.add(rml);
				}
			}
			return filteredMissingLabels;
		}
		return missingLabels;
	}

	public void removeMissingLabels() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		if (debugOnInfo) {logger.info("clean missing revision table.........");}
		fRevision.cleanMissingLabels(fbRevision.getClassMissingRevision());
		reloadMissingLabels();
	}

	public void selectEntity()
	{
		if (debugOnInfo) {logger.info("select Missing entity");}
	}

	@Override
	public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		reloadMissingLabels();
	}
}