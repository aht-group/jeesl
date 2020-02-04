package org.jeesl.web.mbean.prototype.user;

import java.io.Serializable;

import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractAccountBean <USER extends JeeslUser<?>> implements Serializable
{
	final static Logger logger = LoggerFactory.getLogger(AbstractAccountBean.class);
	private static final long serialVersionUID = 1L;
	
	protected String pwd0; public String getPwd0() {return pwd0;} public void setPwd0(String pwd0) {this.pwd0 = pwd0;}
	protected String pwd1; public String getPwd1() {return pwd1;} public void setPwd1(String pwd1) {this.pwd1 = pwd1;}
	protected String pwd2; public String getPwd2() {return pwd2;} public void setPwd2(String pwd2){this.pwd2 = pwd2;}
	
	public AbstractAccountBean()
	{

	}
}