package org.jeesl.web.rest.system.security;

import java.util.Collections;
import java.util.Comparator;

import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.api.rest.rs.system.security.JeeslSecurityRestExport;
import org.jeesl.controller.util.comparator.ejb.system.security.SecurityActionComparator;
import org.jeesl.controller.util.comparator.ejb.system.security.SecurityRoleComparator;
import org.jeesl.controller.util.comparator.ejb.system.security.SecurityUsecaseComparator;
import org.jeesl.controller.util.comparator.ejb.system.security.SecurityViewComparator;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.xml.system.security.XmlActionFactory;
import org.jeesl.factory.xml.system.security.XmlActionsFactory;
import org.jeesl.factory.xml.system.security.XmlCategoryFactory;
import org.jeesl.factory.xml.system.security.XmlRoleFactory;
import org.jeesl.factory.xml.system.security.XmlRolesFactory;
import org.jeesl.factory.xml.system.security.XmlSecurityFactory;
import org.jeesl.factory.xml.system.security.XmlStaffFactory;
import org.jeesl.factory.xml.system.security.XmlTemplateFactory;
import org.jeesl.factory.xml.system.security.XmlTemplatesFactory;
import org.jeesl.factory.xml.system.security.XmlUsecaseFactory;
import org.jeesl.factory.xml.system.security.XmlUsecasesFactory;
import org.jeesl.factory.xml.system.security.XmlViewsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.doc.JeeslSecurityOnlineHelp;
import org.jeesl.interfaces.model.system.security.doc.JeeslSecurityOnlineTutorial;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityArea;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslStaff;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.xml.system.security.Action;
import org.jeesl.model.xml.system.security.Role;
import org.jeesl.model.xml.system.security.Roles;
import org.jeesl.model.xml.system.security.Security;
import org.jeesl.model.xml.system.security.Staffs;
import org.jeesl.model.xml.system.security.Template;
import org.jeesl.model.xml.system.security.Tmp;
import org.jeesl.model.xml.system.security.View;
import org.jeesl.model.xml.system.security.Views;
import org.jeesl.util.query.xml.system.SecurityQuery;
import org.jeesl.web.rest.system.security.updater.SecurityRoleUpdater;
import org.jeesl.web.rest.system.security.updater.SecurityTemplateUpdater;
import org.jeesl.web.rest.system.security.updater.SecurityUsecaseUpdater;
import org.jeesl.web.rest.system.security.updater.SecurityViewUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.rest.security.UtilsSecurityViewImport;
import net.sf.ahtutils.xml.sync.DataUpdate;

public class SecurityRestService <L extends JeeslLang,D extends JeeslDescription,
								C extends JeeslSecurityCategory<L,D>,
								R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
								V extends JeeslSecurityView<L,D,C,R,U,A>,
								U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
								A extends JeeslSecurityAction<L,D,R,V,U,AT>,
								AT extends JeeslSecurityTemplate<L,D,C>,
								CTX extends JeeslSecurityContext<L,D>,
								M extends JeeslSecurityMenu<L,V,CTX,M>,
								AR extends JeeslSecurityArea<L,D,V>,
								OT extends JeeslSecurityOnlineTutorial<L,D,V>,
								OH extends JeeslSecurityOnlineHelp<V,?,?>,
								USER extends JeeslUser<R>>
				implements JeeslSecurityRestExport,UtilsSecurityViewImport
{
	final static Logger logger = LoggerFactory.getLogger(SecurityRestService.class);
	
	private JeeslSecurityFacade<C,R,V,U,A,USER> fSecurity;
	private final SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,OT,OH,?,?,USER> fbSecurity;
	
	
	private XmlCategoryFactory<L,D,C,R,V,U,A,AT,USER> fCategory;
	private org.jeesl.factory.xml.system.security.XmlViewFactory<L,D,C,R,V> xfView;
	private org.jeesl.factory.xml.system.security.XmlViewFactory<L,D,C,R,V> xfViewOld;
	private XmlRoleFactory<L,D,C,R,V,U,A,AT,USER> xfRole,fRoleDescription;
	private XmlActionFactory<L,D,C,R,V,U,A,AT> xfAction,xfActionDoc;

	private XmlTemplateFactory<L,D,C,AT> fTemplate;
	private XmlUsecaseFactory<L,D,C,R,V,U,A,AT,USER> fUsecase,fUsecaseDoc;
	
	protected Comparator<R> comparatorRole;
	private Comparator<V> comparatorView;
	private Comparator<U> comparatorUsecase;
	private Comparator<A> comparatorAction;
	
	private SecurityViewUpdater<L,D,C,R,V,U,A,AT,CTX,M,AR,OT,OH,USER> viewUpdater;
	private SecurityTemplateUpdater<L,D,C,R,V,U,A,AT,CTX,M,AR,OT,OH,USER> initTemplates;
	private SecurityRoleUpdater<L,D,C,R,V,U,A,AT,CTX,M,AR,OT,OH,USER> initRoles;
	private SecurityUsecaseUpdater<L,D,C,R,V,U,A,AT,CTX,M,AR,OT,OH,USER> initUsecases;
	
	private SecurityRestService(JeeslSecurityFacade<C,R,V,U,A,USER> fSecurity,
			SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,OT,OH,?,?,USER> fbSecurity)
	{
		this.fSecurity=fSecurity;
		this.fbSecurity=fbSecurity;
		
		fCategory = new XmlCategoryFactory<L,D,C,R,V,U,A,AT,USER>(null,SecurityQuery.exCategory());
		xfView = new org.jeesl.factory.xml.system.security.XmlViewFactory<>(SecurityQuery.exView());
		xfRole = new XmlRoleFactory<L,D,C,R,V,U,A,AT,USER>(SecurityQuery.exRole());
		fRoleDescription = new XmlRoleFactory<L,D,C,R,V,U,A,AT,USER>(SecurityQuery.role());
		xfAction = new XmlActionFactory<L,D,C,R,V,U,A,AT>(SecurityQuery.exAction());
		xfActionDoc = new XmlActionFactory<>(SecurityQuery.exAction());
		fTemplate = new XmlTemplateFactory<>(SecurityQuery.exTemplate());
		fUsecase = new XmlUsecaseFactory<L,D,C,R,V,U,A,AT,USER>(SecurityQuery.exUsecase());
		fUsecaseDoc = new XmlUsecaseFactory<L,D,C,R,V,U,A,AT,USER>(SecurityQuery.docUsecase());
		
		comparatorRole = (new SecurityRoleComparator<C,R>()).factory(SecurityRoleComparator.Type.position);
		comparatorView = (new SecurityViewComparator<V>()).factory(SecurityViewComparator.Type.position);
		comparatorUsecase = (new SecurityUsecaseComparator<L,D,C,R,V,U,A,AT,USER>()).factory(SecurityUsecaseComparator.Type.position);
		comparatorAction = (new SecurityActionComparator<C,V,A,AT>()).factory(SecurityActionComparator.Type.position);
		
		viewUpdater = new SecurityViewUpdater<>(fbSecurity,fSecurity);
		initTemplates = new SecurityTemplateUpdater<>(fbSecurity,fSecurity);
		initRoles = new SecurityRoleUpdater<>(fbSecurity,fSecurity);
		initUsecases = new SecurityUsecaseUpdater<>(fbSecurity,fSecurity);
	}
	
	public static <L extends JeeslLang,D extends JeeslDescription,
					C extends JeeslSecurityCategory<L,D>,
					R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
					V extends JeeslSecurityView<L,D,C,R,U,A>,
					U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
					A extends JeeslSecurityAction<L,D,R,V,U,AT>,
					CTX extends JeeslSecurityContext<L,D>,
					M extends JeeslSecurityMenu<L,V,CTX,M>,
					AT extends JeeslSecurityTemplate<L,D,C>,
					AR extends JeeslSecurityArea<L,D,V>,
					OT extends JeeslSecurityOnlineTutorial<L,D,V>,
					OH extends JeeslSecurityOnlineHelp<V,?,?>,
					USER extends JeeslUser<R>>
		SecurityRestService<L,D,C,R,V,U,A,AT,CTX,M,AR,OT,OH,USER>
		factory(JeeslSecurityFacade<C,R,V,U,A,USER> fSecurity,
				SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,OT,OH,?,?,USER> fbSecurity)
	{
		return new SecurityRestService<>(fSecurity,fbSecurity);
	}
	
	public DataUpdate iuSecurityTemplates(Security templates){return initTemplates.iuSecurityTemplates(templates);}
	public DataUpdate iuSecurityViews(Security views){return viewUpdater.iuViewsAccess(views);}
	public DataUpdate importSecurityViews(Security views){return viewUpdater.iuViews(views);}
	
	public DataUpdate iuSecurityRoles(Security roles){return initRoles.iuSecurityRoles(roles);}
	public DataUpdate iuSecurityUsecases(Security usecases){return initUsecases.iuSecurityUsecases(usecases);}

	public <STAFF extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> Staffs exportStaffs(Class<STAFF> cStaff)
	{
		XmlStaffFactory<L,D,C,R,V,U,A,AT,USER,STAFF,D1,D2> f = new XmlStaffFactory<L,D,C,R,V,U,A,AT,USER,STAFF,D1,D2>(SecurityQuery.exStaff());
		
		Staffs staffs = new Staffs();
		
		for(STAFF ejb : fSecurity.all(cStaff))
		{
			staffs.getStaff().add(f.build(ejb));
		}
		
		return staffs;
	}

	@Override public Security exportSecurityViews()
	{
		Security xml = XmlSecurityFactory.build();		
		for(C category : fSecurity.allOrderedPosition(fbSecurity.getClassCategory()))
		{
			if(category.getType().equals(JeeslSecurityCategory.Type.view.toString()))
			{
				try
				{
					org.jeesl.model.xml.system.security.Category xCategory = fCategory.build(category);
					xCategory.setViews(XmlViewsFactory.build());
					xCategory.setTmp(new Tmp());
					for(V eView : fSecurity.allForCategory(fbSecurity.getClassView(), fbSecurity.getClassCategory(), category.getCode()))
					{
						eView = fSecurity.load(fbSecurity.getClassView(),eView);
						org.jeesl.model.xml.system.security.View xView = xfView.build(eView);
						xView.setActions(XmlActionsFactory.build());
						for(A action : eView.getActions())
						{
							org.jeesl.model.xml.system.security.Action xAction = xfAction.build(action);							
							xView.getActions().getAction().add(xAction);
						}					
						xCategory.getTmp().getView().add(xView);
					}
					
					xml.getCategory().add(xCategory);
				}
				catch (JeeslNotFoundException e) {e.printStackTrace();}
			}
		}		
		return xml;
	}

	@Override public Security exportSecurityRoles()
	{
		Security xml = XmlSecurityFactory.build();
		for(C category : fSecurity.allOrderedPosition(fbSecurity.getClassCategory()))
		{
			if(category.getType().equals(JeeslSecurityCategory.Type.role.toString()))
			{
				try
				{
					org.jeesl.model.xml.system.security.Category xCat = fCategory.build(category);
					xCat.setRoles(XmlRolesFactory.build());
					for(R role : fSecurity.allForCategory(fbSecurity.getClassRole(), fbSecurity.getClassCategory(), category.getCode()))
					{
						role = fSecurity.load(role,false);
						Collections.sort(role.getUsecases(),comparatorUsecase);
						Role xRole = xfRole.build(role);
						xCat.getRoles().getRole().add(xRole);
					}
					xml.getCategory().add(xCat);
				}
				catch (JeeslNotFoundException e) {e.printStackTrace();}
			}
		}		
		return xml;
	}
	
	@Override public Security exportSecurityActions()
	{
		Security xml = XmlSecurityFactory.build();
		for(C category : fSecurity.allOrderedPosition(fbSecurity.getClassCategory()))
		{
			if(category.getType().equals(JeeslSecurityCategory.Type.action.toString()))
			{
				try
				{
					org.jeesl.model.xml.system.security.Category xmlCat = fCategory.build(category);
					xmlCat.setTemplates(XmlTemplatesFactory.build());
					for(AT template : fSecurity.allForCategory(fbSecurity.getClassTemplate(), fbSecurity.getClassCategory(), category.getCode()))
					{
						Template xTemplate = fTemplate.build(template);
						xmlCat.getTemplates().getTemplate().add(xTemplate);
					}
					xml.getCategory().add(xmlCat);
				}
				catch (JeeslNotFoundException e) {e.printStackTrace();}
			}
		}		
		return xml;
	}

	@Override public Security exportSecurityUsecases()
	{
		Security xml = XmlSecurityFactory.build();

		for(C category : fSecurity.allOrderedPosition(fbSecurity.getClassCategory()))
		{
			if(category.getType().equals(JeeslSecurityCategory.Type.usecase.toString()))
			{
				try
				{
					org.jeesl.model.xml.system.security.Category xmlCat = fCategory.build(category);
					xmlCat.setUsecases(XmlUsecasesFactory.build());
					for(U usecase : fSecurity.allForCategory(fbSecurity.getClassUsecase(), fbSecurity.getClassCategory(), category.getCode()))
					{
						usecase = fSecurity.load(fbSecurity.getClassUsecase(), usecase);
						Collections.sort(usecase.getActions(),comparatorAction);
						Collections.sort(usecase.getViews(),comparatorView);
						xmlCat.getUsecases().getUsecase().add(fUsecase.build(usecase));
					}
					xml.getCategory().add(xmlCat);
				}
				catch (JeeslNotFoundException e) {e.printStackTrace();}
			}
		}		
		return xml;
	}
	
	@Override public Security documentationSecurityViews()
	{
		Security xml = XmlSecurityFactory.build();		
		for(C category : fSecurity.allOrderedPosition(fbSecurity.getClassCategory()))
		{
			if(category.getType().equals(JeeslSecurityCategory.Type.view.toString()))
			{
				try
				{
					org.jeesl.model.xml.system.security.Category xmlCat = fCategory.build(category);
					xmlCat.setViews(XmlViewsFactory.build());
					for(V view : fSecurity.allForCategory(fbSecurity.getClassView(), fbSecurity.getClassCategory(), category.getCode()))
					{
						view = fSecurity.load(fbSecurity.getClassView(),view);
						View xView = xfViewOld.build(view);
						xView.setActions(XmlActionsFactory.build());
						for(A action : view.getActions())
						{
							Action xAction = xfActionDoc.build(action);
							xView.getActions().getAction().add(xAction);
						}
						
						Roles xRoles = XmlRolesFactory.build();
						for(R role : fSecurity.rolesForView(view))
						{
							xRoles.getRole().add(fRoleDescription.build(role));
						}
						xView.setRoles(xRoles);
						
						xmlCat.getViews().getView().add(xView);
					}
					
					xml.getCategory().add(xmlCat);
				}
				catch (JeeslNotFoundException e) {e.printStackTrace();}
			}
		}		
		return xml;
	}

	@Override public Security documentationSecurityPageActions()
	{
		Security xml = XmlSecurityFactory.build();
		for(C category : fSecurity.allOrderedPosition(fbSecurity.getClassCategory()))
		{
			if(category.getType().equals(JeeslSecurityCategory.Type.view.toString()))
			{
				try
				{
					Views views = XmlViewsFactory.build();
					
					for(V view : fSecurity.allForCategory(fbSecurity.getClassView(), fbSecurity.getClassCategory(), category.getCode()))
					{
						view = fSecurity.load(fbSecurity.getClassView(),view);
						View xView = xfViewOld.build(view);
						xView.setActions(XmlActionsFactory.build());
						for(A action : view.getActions())
						{
							Action xAction = xfActionDoc.build(action);
							xView.getActions().getAction().add(xAction);
						}
						
						Roles xRoles = XmlRolesFactory.build();
						for(R role : fSecurity.rolesForView(view))
						{
							xRoles.getRole().add(fRoleDescription.build(role));
						}
						xView.setRoles(xRoles);
						
						views.getView().add(xView);
					}
					
					org.jeesl.model.xml.system.security.Category xCategory = fCategory.build(category);
					xCategory.setViews(views);
					xml.getCategory().add(xCategory);
				}
				catch (JeeslNotFoundException e) {e.printStackTrace();}
			}
		}		
		return xml;
	}
	
	@Override public Security documentationSecurityUsecases()
	{
		Security xml = XmlSecurityFactory.build();

		for(C category : fSecurity.allOrderedPosition(fbSecurity.getClassCategory()))
		{
			if(category.getType().equals(JeeslSecurityCategory.Type.usecase.toString()))
			{
				try
				{
					org.jeesl.model.xml.system.security.Category xmlCat = fCategory.build(category);
					xmlCat.setUsecases(XmlUsecasesFactory.build());
					for(U usecase : fSecurity.allForCategory(fbSecurity.getClassUsecase(), fbSecurity.getClassCategory(), category.getCode()))
					{
						xmlCat.getUsecases().getUsecase().add(fUsecaseDoc.build(usecase));
					}
					xml.getCategory().add(xmlCat);
				}
				catch (JeeslNotFoundException e) {e.printStackTrace();}
			}
		}		
		return xml;
	}
}