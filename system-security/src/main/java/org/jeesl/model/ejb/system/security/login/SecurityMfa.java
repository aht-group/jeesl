package org.jeesl.model.ejb.system.security.login;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.security.login.JeeslSecurityMfa;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.system.security.user.SecurityUser;

@Entity
@Table(name="SecurityMfa")
@EjbErNode(name="SecurityMfa")
public class SecurityMfa implements JeeslSecurityMfa<SecurityUser,SecurityMfaType>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@Override public String resolveParentAttribute() {return JeeslSecurityMfa.Attributes.user.toString();}
	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_SecurityMfa_user"))
	private SecurityUser user;
	@Override public SecurityUser getUser() {return user;}
	@Override public void setUser(SecurityUser user) {this.user = user;}
	
	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_SecurityMfa_type"))
	private SecurityMfaType type;
	public SecurityMfaType getType() {return type;}
	public void setType(SecurityMfaType type) {this.type = type;}
	
	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}
	
	private boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}

	private LocalDateTime record;
	@Override public LocalDateTime getRecord() {return record;}
	@Override public void setRecord(LocalDateTime record) {this.record = record;}
	
	private String json;
	@Override public String getJson() {return json;}
	@Override public void setJson(String json) {this.json = json;}


	@Override public boolean equals(Object object){return (object instanceof SecurityMfa) ? id == ((SecurityMfa) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17, 53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}