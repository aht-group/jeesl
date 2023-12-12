package org.jeesl.web.mbean.prototype.module.am;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslAmFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.AmFactoryBuilder;
import org.jeesl.factory.ejb.module.am.EjbActivityFactory;
import org.jeesl.factory.ejb.module.am.EjbActivityProjectFactory;
import org.jeesl.interfaces.model.module.am.JeesAmProject;
import org.jeesl.interfaces.model.module.am.JeeslAmActivity;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class AbstractAmProjectBean <L extends JeeslLang,D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									REALM extends JeeslTenantRealm<L,D,REALM,?>,
									ACTIVITY extends JeeslAmActivity<L,D,REALM,ACTIVITY,PROJ>,
									PROJ extends JeesAmProject<L,D,REALM,ACTIVITY,PROJ>
									>
		extends AbstractAdminBean<L,D,LOC> implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAmProjectBean.class);

	private final AmFactoryBuilder<L,D,LOC,REALM,ACTIVITY,PROJ> fbAm;
	protected final EjbActivityFactory<REALM,ACTIVITY,PROJ> efActivity;
	protected final EjbActivityProjectFactory<REALM,ACTIVITY,PROJ> efProject;

	protected JeeslAmFacade<L,D,LOC,REALM,ACTIVITY,PROJ> fAm;


	protected List<PROJ> projects; public List<PROJ> getProjects() {return projects;}


	protected PROJ project; public PROJ getProject() {return project;} public void setProject(PROJ project) {this.project = project;}

	public AbstractAmProjectBean(AmFactoryBuilder<L,D,LOC,REALM,ACTIVITY,PROJ> fbAm)
	{
		super(fbAm.getClassL(),fbAm.getClassD());

		this.fbAm = fbAm;
		efProject = fbAm.ejbProject();
		efActivity = fbAm.ejbActivity();
	}

	protected void postConstructAm(JeeslTranslationBean<L,D,LOC> bTranslation, String currentLocaleCode,
			List<LOC> locales, JeeslFacesMessageBean bMessage,
			JeeslAmFacade<L,D,LOC,REALM,ACTIVITY,PROJ> fAm)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fAm=fAm;
		refreshList();
	}

	private void refreshList()
	{
		projects = fAm.all(fbAm.getClassProject());
	}


	public void selectProject() throws JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.selectEntity(project));
		project = fAm.find(fbAm.getClassProject(), project);
	}

	public void saveProject() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(project));}
		project = fAm.save(project);
		if(Objects.isNull(project.getRoot()))
		{
			ACTIVITY activity = efActivity.build(project, null);
			activity.setName(efLang.clone(project.getName()));
			activity.setCode(project.getCode());
			activity.setDescription(efDescription.clone(project.getDescription()));
			activity = fAm.save(activity);
			project.setRoot(activity);
		}
		project = fAm.save(project);
		if(debugOnInfo){logger.info("-----" + project.toString() +"-------");}
		refreshList();
	}

	public void addProject()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbAm.getClassProject()));}
		project = efProject.build();
		project.setName(efLang.createEmpty(localeCodes));
		project.setDescription(efDescription.createEmpty(localeCodes));
	}

	public void deleteProject() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.deleteEntity(project));}
		fAm.deleteActivityTree(project.getRoot());
		fAm.rm(project);
		bMessage.growlDeleted(project);
		project=null;
		
		refreshList();
	}

	public void cancelProject()
	{
		project = null;
	}


}