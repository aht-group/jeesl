package org.jeesl.model.ejb.system.security.user;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.jeesl.interfaces.model.system.security.user.JeeslSecurityUser;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;

@Entity
@Table(name="SecurityUser",uniqueConstraints=@UniqueConstraint(columnNames={"email"}))
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="system",discriminatorType=DiscriminatorType.STRING,length=32)
@DiscriminatorValue("generic")
@SequenceGenerator(name="SequenceUser",sequenceName="SecurityUser_id_seq",allocationSize=1)
public abstract class SecurityUser implements JeeslSimpleUser,JeeslSecurityUser
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SequenceUser")
	protected long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@NotNull @Column(unique=true)
	protected String email;
	@Override public String getEmail() {return email;}
	@Override public void setEmail(String email) {this.email = email;}

	protected String firstName;
	@Override public String getFirstName() {return firstName;}
	@Override public void setFirstName(String firstName) {this.firstName = firstName;}

	protected String lastName;
	@Override public String getLastName() {return lastName;}
	@Override public void setLastName(String lastName) {this.lastName = lastName;}

	protected String pwd;
	public String getPwd() {return pwd;}
	public void setPwd(String pwd) {this.pwd = pwd;}

	private String salt;
	public String getSalt() {return salt;}
	public void setSalt(String salt) {this.salt = salt;}

	private Boolean permitLogin;
	public Boolean getPermitLogin() {return permitLogin;}
	public void setPermitLogin(Boolean permitLogin) {this.permitLogin = permitLogin;}
}