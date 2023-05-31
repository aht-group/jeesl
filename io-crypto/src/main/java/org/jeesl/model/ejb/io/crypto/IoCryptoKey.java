package org.jeesl.model.ejb.io.crypto;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKey;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.system.security.user.SecurityUser;

@Table(name="IoCryptoKey")
@EjbErNode(name="CryptoKey",category="ioCrypto",subset="ioCrypto")
@Entity
public class IoCryptoKey implements JeeslIoCryptoKey<SecurityUser,IoCryptoKeyLifetime>
{
	public static final long serialVersionUID=1;
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	@Override public String resolveParentAttribute() {return JeeslIoCryptoKey.Attributes.user.toString();}
	@ManyToOne
	private SecurityUser user;
	@Override public SecurityUser getUser() {return user;}
	@Override public void setUser(SecurityUser user) {this.user = user;}
	
	private LocalDateTime record;
	@Override public LocalDateTime getRecord() {return record;}
	@Override public void setRecord(LocalDateTime record) {this.record = record;}
	
	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	private String name;
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	
	private IoCryptoKeyLifetime status;
	@Override public IoCryptoKeyLifetime getStatus() {return status;}
	@Override public void setStatus(IoCryptoKeyLifetime status) {this.status = status;}
	
	@NotNull
	private String salt;
	@Override public String getSalt() {return salt;}
	@Override public void setSalt(String salt) {this.salt = salt;}

	@NotNull
	private String iv;
	@Override public String getIv() {return iv;}
	@Override public void setIv(String iv) {this.iv = iv;}
	
	@NotNull
	private String verification;
	@Override public String getVerification() {return verification;}
	@Override public void setVerification(String verification) {this.verification = verification;}


	@Override public boolean equals(Object object) {return (object instanceof IoCryptoKey) ? id == ((IoCryptoKey) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(23,13).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}