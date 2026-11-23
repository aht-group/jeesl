package org.jeesl.model.ejb.system.security.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.model.ejb.system.security.access.SecurityRole;

/**
 * Test-only user entity: the JEESL model declares the user as an abstract class, the roles of a user are
 * supplied by the consuming application.
 */
@Entity
@DiscriminatorValue("test")
public class SecurityTestUser extends SecurityUser implements JeeslUser<SecurityRole>
{
	public static final long serialVersionUID=1;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="SecurityUserJtRole",joinColumns={@JoinColumn(name="user_id")},inverseJoinColumns={@JoinColumn(name="role_id")})
	private List<SecurityRole> roles;
	@Override public List<SecurityRole> getRoles() {if(Objects.isNull(roles)) {roles = new ArrayList<>();} return roles;}
	@Override public void setRoles(List<SecurityRole> roles) {this.roles = roles;}
}
