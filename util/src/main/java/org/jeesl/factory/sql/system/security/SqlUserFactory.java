package org.jeesl.factory.sql.system.security;

import javax.persistence.Table;

import org.jeesl.interfaces.model.system.security.user.JeeslUser;

public class SqlUserFactory <USER extends JeeslUser<?>>
{
	public SqlUserFactory()
	{
		
	}
	
	public String updateSalt(Class<?> c, USER user, String salt)
	{
		if(c.getAnnotation(Table.class)==null) {throw new RuntimeException("Not a @Table)");}
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ").append(c.getAnnotation(Table.class).name());
		sb.append(" SET salt='").append(salt).append("'");
		sb.append(" WHERE id=").append(user.getId());
		sb.append(";");
		
		return sb.toString();
	}
	
	public String updatePwd(Class<?> c, String column, USER user, String pwd)
	{
		if(c.getAnnotation(Table.class)==null) {throw new RuntimeException("Not a @Table)");}
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ").append(c.getAnnotation(Table.class).name());
		sb.append(" SET ").append(column).append("='").append(pwd).append("'");
		sb.append(" WHERE id=").append(user.getId());
		sb.append(";");
		
		return sb.toString();
	}
	
	public String updatePwd(Class<?> c, USER user, String pwd, long rev)
	{
		if(c.getAnnotation(Table.class)==null) {throw new RuntimeException("Not a @Table)");}
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ").append(c.getAnnotation(Table.class).name());
		sb.append(" SET pwd='").append(pwd).append("'");
		sb.append(" WHERE id=").append(user.getId());
		sb.append("   AND rev=").append(rev);
		sb.append(";");
		
		return sb.toString();
	}
}