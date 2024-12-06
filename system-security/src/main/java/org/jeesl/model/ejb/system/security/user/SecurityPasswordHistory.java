package org.jeesl.model.ejb.system.security.user;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.security.user.pwd.JeeslPasswordHistory;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@Entity
@Table(name="SecurityPasswordHistory")
@EjbErNode(name="UserPasswordHistory",category="user",subset="systemUser")
public class SecurityPasswordHistory  implements JeeslPasswordHistory<SecurityUser>
{
	public static final long serialVersionUID=1;

	public enum Attributes {id,user,pwd,record}
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id){this.id = id;}
	
	@Override public String resolveParentAttribute() {return JeeslPasswordHistory.Attributes.user.toString();}
	@NotNull @ManyToOne
	private SecurityUser user;
	@Override public SecurityUser getUser() {return user;}
	@Override public void setUser(SecurityUser user) {this.user = user;}
	
	@NotNull
	private String pwd;
	@Override public String getPwd() {return pwd;}
	@Override public void setPwd(String pwd) {this.pwd = pwd;}
	
	@NotNull
	private Date record;
	@Override public Date getRecord() {return record;}
	@Override public void setRecord(Date record) {this.record = record;}


	@Override public boolean equals(Object object) {return (object instanceof SecurityPasswordHistory) ? id == ((SecurityPasswordHistory) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(27,43).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		sb.append(" ").append(record);
		sb.append(" pwd:").append(pwd);
		return sb.toString();
	}
}