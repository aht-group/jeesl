package net.sf.ahtutils.factory.word;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.exlp.util.jx.JaxbUtil;
import org.jeesl.factory.xml.io.locale.status.XmlStatusFactory;
import org.jeesl.factory.xml.system.security.XmlRoleFactory;
import org.jeesl.factory.xml.system.security.XmlUserFactory;
import org.jeesl.model.xml.io.locale.status.Status;
import org.jeesl.model.xml.system.security.Role;
import org.jeesl.model.xml.system.security.Roles;
import org.jeesl.model.xml.system.security.Staff;
import org.jeesl.model.xml.system.security.Staffs;
import org.jeesl.model.xml.system.security.User;
import org.jeesl.test.JeeslDocBootstrap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.word.WordResponsibilityMatrixFactory;
import net.sf.ahtutils.test.AbstractUtilsDocTest;
import net.sf.ahtutils.xml.project.Responsibilities;

public class TestResponsibilityMatrixFactory extends AbstractUtilsDocTest
{
	final static Logger logger = LoggerFactory.getLogger(TestResponsibilityMatrixFactory.class);
	
	private static File fDst;
	
	private List<Role> roles;
	private List<Status> statuses;
	
	private Responsibilities responsibilites;
	
	@BeforeAll
	public static void initFiles() throws FileNotFoundException
	{
		fDst = new File("target","matrix.docx");
		logger.info("Using target File: "+fDst.getAbsolutePath());
	}
	
	@BeforeEach
	public void init()
	{	
		initLists();
		responsibilites = new Responsibilities();
		responsibilites.setRoles(new Roles());
		responsibilites.getRoles().getRole().addAll(roles);
		
		User u1 = XmlUserFactory.create("User1", "Lastname1");
		addStaff(u1, 1, 1);
		responsibilites.getUser().add(u1);

		User u2 = XmlUserFactory.create("User2", "Lastname2");
		addStaff(u2, 2, 1);
		addStaff(u2, 3, 2);
		responsibilites.getUser().add(u2);
		
		User u3 = XmlUserFactory.create("User3", "Lastname3");
		addStaff(u3, 3, 1);
		addStaff(u3, 2, 2);
		addStaff(u3, 1, 2);
		responsibilites.getUser().add(u3);
	}
	
	private void initLists()
	{
		roles = new ArrayList<Role>();
		roles.add(XmlRoleFactory.create("tl", "Team Leader"));
		roles.add(XmlRoleFactory.create("dev", "Application Developer"));
		roles.add(XmlRoleFactory.create("sec", "Security Expert"));
		
		statuses = new ArrayList<Status>();
		statuses.add(XmlStatusFactory.create(WordResponsibilityMatrixFactory.Status.primary.toString()));
		statuses.add(XmlStatusFactory.create(WordResponsibilityMatrixFactory.Status.secondary.toString()));
	}
	
	private void addStaff(User user, int roleIndex, int statusIndex)
	{
		if(Objects.nonNull(user.getStaffs())) {user.setStaffs(new Staffs());}
		
		Staff staff = new Staff();
		staff.setRole(roles.get(roleIndex-1));
		staff.setStatus(statuses.get(statusIndex-1));
		user.getStaffs().getStaff().add(staff);
	}
	
	@Test public void debug()
	{
		JaxbUtil.info(responsibilites);
	}
	
	public void test()
	{
		logger.info("Starting test");
		WordResponsibilityMatrixFactory wrmf = new WordResponsibilityMatrixFactory();
		wrmf.buildWord(fDst, responsibilites);
	}
	
	public static void main(String[] args) throws Exception
    {
		JeeslDocBootstrap.init();
		
		TestResponsibilityMatrixFactory.initFiles();
		TestResponsibilityMatrixFactory test = new TestResponsibilityMatrixFactory();
		test.init();
		test.debug();
		test.test();
    }
}