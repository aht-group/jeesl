package org.jeesl.factory.builder.system;

import java.util.Comparator;

import org.jeesl.controller.util.comparator.ejb.system.security.SecurityActionComparator;
import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.system.security.EjbSecurityActionFactory;
import org.jeesl.factory.ejb.system.security.EjbSecurityActionTemplateFactory;
import org.jeesl.factory.ejb.system.security.EjbSecurityAreaFactory;
import org.jeesl.factory.ejb.system.security.EjbSecurityCategoryFactory;
import org.jeesl.factory.ejb.system.security.EjbSecurityContextFactory;
import org.jeesl.factory.ejb.system.security.EjbSecurityHelpFactory;
import org.jeesl.factory.ejb.system.security.EjbSecurityMenuFactory;
import org.jeesl.factory.ejb.system.security.EjbSecurityRoleFactory;
import org.jeesl.factory.ejb.system.security.EjbSecurityUsecaseFactory;
import org.jeesl.factory.ejb.system.security.EjbSecurityViewFactory;
import org.jeesl.factory.ejb.system.security.EjbStaffFactory;
import org.jeesl.factory.json.system.security.JsonPageFactory;
import org.jeesl.factory.json.system.security.JsonPagesFactory;
import org.jeesl.factory.sql.system.security.SqlUserFactory;
import org.jeesl.factory.txt.system.security.TxtSecurityViewFactory;
import org.jeesl.factory.txt.system.security.TxtStaffFactory;
import org.jeesl.interfaces.model.io.cms.JeeslIoCms;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsSection;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.access.JeeslStaff;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.doc.JeeslSecurityOnlineHelp;
import org.jeesl.interfaces.model.system.security.doc.JeeslSecurityOnlineTutorial;
import org.jeesl.interfaces.model.system.security.login.JeeslSecurityMfaType;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityArea;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslSecurityUser;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityFactoryBuilder<L extends JeeslLang, D extends JeeslDescription,
									C extends JeeslSecurityCategory<L,D>,
									R extends JeeslSecurityRole<L,D,C,V,U,A>,
									V extends JeeslSecurityView<L,D,C,R,U,A>,
									U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
									A extends JeeslSecurityAction<L,D,R,V,U,AT>,
									AT extends JeeslSecurityTemplate<L,D,C>,
									CTX extends JeeslSecurityContext<L,D>,
									M extends JeeslSecurityMenu<L,V,CTX,M>,
									AR extends JeeslSecurityArea<L,D,V>,
//									MFA extends JeeslSecurityMfa<?,?>,
//									MFT extends JeeslSecurityMfaType<L,D,MFT,?>,
									OT extends JeeslSecurityOnlineTutorial<L,D,V>,
									OH extends JeeslSecurityOnlineHelp<V,DC,DS>,
									DC extends JeeslIoCms<L,D,?,?,DS>,
									DS extends JeeslIoCmsSection<L,DS>,
									UJ extends JeeslSecurityUser,
									UP extends JeeslUser<R>
>
				extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(SecurityFactoryBuilder.class);
        
	private final Class<C> cCategory; public Class<C> getClassCategory(){return cCategory;}
	private final Class<R> cRole; public Class<R> getClassRole(){return cRole;}
	private final Class<V> cView; public Class<V> getClassView(){return cView;}
	private final Class<U> cUsecase; public Class<U> getClassUsecase(){return cUsecase;}
    private final Class<A> cAction; public Class<A> getClassAction(){return cAction;}
    private final Class<AT> cTemplate; public Class<AT> getClassTemplate(){return cTemplate;}
    private final Class<CTX> cContext; public Class<CTX> getClassContext(){return cContext;}
    private final Class<M> cMenu; public Class<M> getClassMenu(){return cMenu;}
    private final Class<AR> cArea; public Class<AR> getClassArea(){return cArea;}
    
    private final Class<OH> cOnlineHelp; public Class<OH> getClassOnlineHelp() {return cOnlineHelp;}
	private final Class<UP> cUser; public Class<UP> getClassUserProject() {return cUser;}
	
	public SecurityFactoryBuilder(final Class<L> cL, final Class<D> cD,
									final Class<C> cCategory,
									final Class<R> cRole,
									final Class<V> cView,
									final Class<U> cUsecase,
									final Class<A> cAction,
									final Class<AT> cTemplate,
									final Class<CTX> cContext,
									final Class<M> cMenu,
									final Class<AR> cArea,
									final Class<OH> cOnlineHelp,
									final Class<UP> cUser)
	{		
		super(cL,cD);
		this.cCategory=cCategory;
		this.cRole=cRole;
		this.cView=cView;
		this.cUsecase=cUsecase;
		this.cAction=cAction;
		this.cTemplate=cTemplate;
		this.cContext=cContext;
		this.cMenu=cMenu;
		this.cArea=cArea;
		this.cOnlineHelp=cOnlineHelp;
		this.cUser=cUser;
	}
	
	public EjbSecurityContextFactory<CTX> ejbContext(){return new EjbSecurityContextFactory<>(cContext);}
	public EjbSecurityCategoryFactory<C> ejbCategory(){return new EjbSecurityCategoryFactory<>(cCategory);}
	public EjbSecurityRoleFactory<C,R> ejbRole(){return new EjbSecurityRoleFactory<C,R>(cRole);}
	public EjbSecurityViewFactory<C,V> ejbView(){return new EjbSecurityViewFactory<C,V>(cView);}
	public EjbSecurityUsecaseFactory<C,U> ejbUsecase() {return new EjbSecurityUsecaseFactory<C,U>(cUsecase);}
	public EjbSecurityActionFactory<V,A> ejbAction() {return new EjbSecurityActionFactory<V,A>(cAction);}
	public EjbSecurityActionTemplateFactory<C,AT> ejbTemplate(){return new EjbSecurityActionTemplateFactory<C,AT>(cTemplate);}
	public EjbSecurityMenuFactory<V,CTX,M> ejbMenu(){return new EjbSecurityMenuFactory<>(cMenu);}
	public EjbSecurityAreaFactory<V,AR> ejbArea() {return new EjbSecurityAreaFactory<V,AR>(cArea);}
	public EjbSecurityHelpFactory<V,OH,DC,DS> ejbHelp() {return new EjbSecurityHelpFactory<>(cOnlineHelp);}
	
//	public EjbSecurityUserFactory<UP> ejbUser() {return new EjbSecurityUserFactory<>(cUser);}
	
	public <STAFF extends JeeslStaff<R,UP,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> EjbStaffFactory<R,UP,STAFF,D1,D2> ejbStaff(final Class<STAFF> cStaff) {return new EjbStaffFactory<>(cStaff);}
	
	public <STAFF extends JeeslStaff<R,UP,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> TxtStaffFactory<L,D,R,UP,STAFF,D1,D2> txtStaff(String localeCode) {return new TxtStaffFactory<>(localeCode);}
	public TxtSecurityViewFactory<L,D,C,V> txtView(String localeCode){return new TxtSecurityViewFactory<>(localeCode);}
	
	public JsonPageFactory<L,D,C,V,CTX,M> jsonPage() {return new JsonPageFactory<>();}
	public JsonPagesFactory<L,D,C,R,V,U,A,AT,CTX,M,AR,UJ,UP> jsonPages() {return new JsonPagesFactory<>(this);}
	
	public SqlUserFactory<UP> sqlUser() {return new SqlUserFactory<>();}
	
	public Comparator<A> comparatorAction(SecurityActionComparator.Type type) {return (new SecurityActionComparator<C,V,A,AT>()).factory(type);}
}