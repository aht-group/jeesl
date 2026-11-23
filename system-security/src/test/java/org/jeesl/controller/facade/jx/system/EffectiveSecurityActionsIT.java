package org.jeesl.controller.facade.jx.system;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.system.security.SecurityCategory;
import org.jeesl.model.ejb.system.security.access.SecurityRole;
import org.jeesl.model.ejb.system.security.access.SecurityUsecase;
import org.jeesl.model.ejb.system.security.context.SecurityContext;
import org.jeesl.model.ejb.system.security.context.SecurityMenu;
import org.jeesl.model.ejb.system.security.page.SecurityAction;
import org.jeesl.model.ejb.system.security.page.SecurityTemplate;
import org.jeesl.model.ejb.system.security.page.SecurityView;
import org.jeesl.model.ejb.system.security.user.SecurityTestUser;
import org.jeesl.util.query.cq.CqLiteral;
import org.jeesl.util.query.ejb.system.EjbSecurityQuery;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Integration check of the effective security actions against an in-memory database. It needs a database and is
 * therefore no test program of ADR-0003; run it explicitly with
 * {@code mvn -o -pl system-security test -Dtest=EffectiveSecurityActionsIT}.
 */
public class EffectiveSecurityActionsIT
{
	private static EntityManagerFactory emf;
	private static EntityManager em;
	private static JeeslSecurityFacade<SecurityCategory,SecurityRole,SecurityView,SecurityUsecase,SecurityAction,SecurityContext,SecurityMenu,SecurityTestUser> fSecurity;
	
	private static SecurityTestUser user;
	private static SecurityAction aDirect,aView,aNone;
	private static SecurityRole r3;
	private static SecurityView v1;
	
	@BeforeAll public static void init()
	{
		emf = Persistence.createEntityManagerFactory("jeesl-security-test");
		em = emf.createEntityManager();
		
		@SuppressWarnings({"rawtypes","unchecked"})
		SecurityFactoryBuilder fb = new SecurityFactoryBuilder(IoLang.class,IoDescription.class,
				SecurityCategory.class,SecurityRole.class,SecurityView.class,SecurityUsecase.class,SecurityAction.class,
				SecurityTemplate.class,null,null,null,null,null,null,SecurityTestUser.class);
		fSecurity = new JeeslSecurityFacadeBean(em,fb);
		
		em.getTransaction().begin();
		
		SecurityCategory category = new SecurityCategory();
		category.setCode("it"); category.setType("test"); category.setVisible(true);
		em.persist(category);
		
		v1 = view(em,category,"v1");
		SecurityView v2 = view(em,category,"v2");
		
		aDirect = action(em,v1,"aDirect");
		SecurityAction aUsecase = action(em,v1,"aUsecase");
		aNone = action(em,v1,"aNone");
		SecurityAction aContext = action(em,v1,"aContext");
		SecurityAction aContextUsecase = action(em,v1,"aContextUsecase");
		aView = action(em,v2,"aView");
		
		SecurityUsecase u1 = usecase(em,category,"u1");
		u1.getActions().add(aUsecase);
		SecurityUsecase u2 = usecase(em,category,"u2");
		u2.getActions().add(aContextUsecase);
		
		SecurityRole r1 = role(em,category,"r1");
		r1.getActions().add(aDirect);
		r1.getUsecases().add(u1);
		
		SecurityRole r2 = role(em,category,"r2");
		r2.getActions().add(aDirect);
		r2.getUsecases().add(u1);
		
		r3 = role(em,category,"r3");
		r3.getActions().add(aContext);
		r3.getUsecases().add(u2);
		
		v2.getRoles().add(r1);
		
		user = new SecurityTestUser();
		user.setEmail("it@example.org");
		user.setFirstName("Integration");
		user.setLastName("Test");
		user.getRoles().add(r1);
		user.getRoles().add(r2);
		em.persist(user);
		
		em.getTransaction().commit();
	}
	
	@BeforeEach public void clear() {em.clear();}
	
	@AfterAll public static void close()
	{
		if(em!=null) {em.close();}
		if(emf!=null) {emf.close();}
	}
	
	@Test public void actionAssignedToUserRole()
	{
		List<SecurityAction> result = actions(queryWithUser());
		
		Assertions.assertTrue(codes(result).contains("aDirect"));
		Assertions.assertFalse(codes(result).contains("aNone"));
		Assertions.assertFalse(codes(result).contains("aContext"));
	}
	
	@Test public void actionAssignedToUsecaseOfUserRole()
	{
		List<SecurityAction> result = actions(queryWithUser());
		
		Assertions.assertTrue(codes(result).contains("aUsecase"));
		Assertions.assertFalse(codes(result).contains("aNone"));
	}
	
	@Test public void actionAssignedToViewOfUserRole()
	{
		List<SecurityAction> result = actions(queryWithUser());
		
		Assertions.assertTrue(codes(result).contains("aView"));
		Assertions.assertFalse(result.isEmpty());
	}
	
	@Test public void actionWithoutSupportedPath()
	{
		List<SecurityAction> result = actions(queryWithUser());
		
		Assertions.assertFalse(codes(result).contains("aNone"));
		Assertions.assertFalse(codes(result).contains("aContextUsecase"));
	}
	
	@Test public void severalPathsGrantActionOnce()
	{
		List<SecurityAction> result = actions(queryWithUser());
		
		Assertions.assertEquals(1,count(result,"aDirect"));
		Assertions.assertEquals(1,count(result,"aUsecase"));
	}
	
	@Test public void rolesOfTheQuery()
	{
		EjbSecurityQuery<SecurityCategory,SecurityRole,SecurityView,SecurityUsecase,SecurityAction,SecurityContext,SecurityTestUser> query = query();
		query.add(r3);
		List<SecurityAction> result = actions(query);
		
		Assertions.assertTrue(codes(result).contains("aContext"));
		Assertions.assertTrue(codes(result).contains("aContextUsecase"));
		Assertions.assertFalse(codes(result).contains("aDirect"));
	}
	
	@Test public void absentRoleListAddsNoPath()
	{
		List<SecurityAction> result = actions(queryWithUser());
		
		Assertions.assertTrue(codes(result).contains("aDirect"));
		Assertions.assertFalse(codes(result).contains("aContext"));
	}
	
	@Test public void viewFilterRemainsEffective()
	{
		EjbSecurityQuery<SecurityCategory,SecurityRole,SecurityView,SecurityUsecase,SecurityAction,SecurityContext,SecurityTestUser> query = queryWithUser();
		query.add(v1);
		List<SecurityAction> result = actions(query);
		
		Assertions.assertTrue(codes(result).contains("aDirect"));
		Assertions.assertTrue(codes(result).contains("aUsecase"));
		Assertions.assertFalse(codes(result).contains("aView"));
	}
	
	@Test public void codeFilterRemainsEffective()
	{
		EjbSecurityQuery<SecurityCategory,SecurityRole,SecurityView,SecurityUsecase,SecurityAction,SecurityContext,SecurityTestUser> query = queryWithUser();
		query.add(CqLiteral.exact("aDirect",CqLiteral.path(org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction.Attributes.code)));
		List<SecurityAction> result = actions(query);
		
		Assertions.assertEquals(1,result.size());
		Assertions.assertEquals("aDirect",result.get(0).getCode());
	}
	
	@Test public void queryWithoutUserAndRoleAppliesFiltersOnly()
	{
		List<SecurityAction> result = actions(query());
		
		Assertions.assertTrue(codes(result).contains("aNone"));
		Assertions.assertTrue(codes(result).contains("aContext"));
	}
	
	@Test public void actionFilterRemainsEffective()
	{
		EjbSecurityQuery<SecurityCategory,SecurityRole,SecurityView,SecurityUsecase,SecurityAction,SecurityContext,SecurityTestUser> query = queryWithUser();
		query.add(aDirect);
		query.maxResults(5);
		List<SecurityAction> result = actions(query);
		
		Assertions.assertEquals(1,result.size());
		Assertions.assertEquals("aDirect",result.get(0).getCode());
	}
	
	private EjbSecurityQuery<SecurityCategory,SecurityRole,SecurityView,SecurityUsecase,SecurityAction,SecurityContext,SecurityTestUser> query()
	{
		return new EjbSecurityQuery<>();
	}
	
	private EjbSecurityQuery<SecurityCategory,SecurityRole,SecurityView,SecurityUsecase,SecurityAction,SecurityContext,SecurityTestUser> queryWithUser()
	{
		EjbSecurityQuery<SecurityCategory,SecurityRole,SecurityView,SecurityUsecase,SecurityAction,SecurityContext,SecurityTestUser> query = query();
		query.add(user);
		return query;
	}
	
	private List<SecurityAction> actions(EjbSecurityQuery<SecurityCategory,SecurityRole,SecurityView,SecurityUsecase,SecurityAction,SecurityContext,SecurityTestUser> query)
	{
		return fSecurity.fSecurityActions(query);
	}
	
	private List<String> codes(List<SecurityAction> list)
	{
		List<String> codes = new ArrayList<String>();
		for(SecurityAction action : list) {codes.add(action.getCode());}
		return codes;
	}
	
	private int count(List<SecurityAction> list, String code)
	{
		int count = 0;
		for(SecurityAction action : list) {if(action.getCode().equals(code)) {count++;}}
		return count;
	}
	
	private static SecurityView view(EntityManager em, SecurityCategory category, String code)
	{
		SecurityView view = new SecurityView();
		view.setCategory(category); view.setCode(code); view.setVisible(true);
		em.persist(view);
		return view;
	}
	
	private static SecurityAction action(EntityManager em, SecurityView view, String code)
	{
		SecurityAction action = new SecurityAction();
		action.setView(view); action.setCode(code); action.setVisible(true);
		em.persist(action);
		return action;
	}
	
	private static SecurityUsecase usecase(EntityManager em, SecurityCategory category, String code)
	{
		SecurityUsecase usecase = new SecurityUsecase();
		usecase.setCategory(category); usecase.setCode(code); usecase.setVisible(true);
		em.persist(usecase);
		return usecase;
	}
	
	private static SecurityRole role(EntityManager em, SecurityCategory category, String code)
	{
		SecurityRole role = new SecurityRole();
		role.setCategory(category); role.setCode(code); role.setVisible(true);
		em.persist(role);
		return role;
	}
}
