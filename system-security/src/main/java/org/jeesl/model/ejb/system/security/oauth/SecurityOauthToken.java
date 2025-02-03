package org.jeesl.model.ejb.system.security.oauth;

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
import org.jeesl.interfaces.model.system.security.oauth.JeeslSecurityOauthToken;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.system.security.user.SecurityUser;

@Entity
@Table(name="SecurityOauthToken")
@EjbErNode(name="SecurityOauthToken")
public class SecurityOauthToken implements JeeslSecurityOauthToken
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@NotNull
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}

	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_SecurityOauthToken_user"))
	private SecurityUser user;
	public SecurityUser getUser() {return user;}
	public void setUser(SecurityUser user) {this.user = user;}

	private String nonce;
	public String getNonce() {return nonce;}
	public void setNonce(String nonce) {this.nonce = nonce;}
	
//	private String token;
//	public String getToken() {return token;}
//	public void setToken(String token) {this.token = token;}
	
	@Override public boolean equals(Object object){return (object instanceof SecurityOauthToken) ? id == ((SecurityOauthToken) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17, 53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}