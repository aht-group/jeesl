package org.jeesl.client.model.ejb.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.jeesl.client.model.ejb.system.security.SecurityRole;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@Entity
@Table(name="UtilsMeis", uniqueConstraints={@UniqueConstraint(name="unique-email", columnNames = {"email"})})
@EjbErNode(name="User",category="user",subset="security,ts")
@NamedQueries
({	
	@NamedQuery(name="fUserByEmail",query="SELECT u FROM MeisUser u WHERE u.email = :email")
})
public class User implements JeeslSimpleUser,JeeslUser<SecurityRole>
{
	public static final long serialVersionUID=1;
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
    @Override public long getId() {return id;}
    @Override public void setId(long id) {this.id = id;}

	@NotNull @Column(unique=true)
	protected String email;
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
	
	protected String pwd;
	public String getPwd() {return pwd;}
	public void setPwd(String pwd) {this.pwd = pwd;}
    
	private String salt;
	@Override public String getSalt() {return salt;}
	@Override public void setSalt(String salt) {this.salt = salt;}

	protected String firstName;
    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}

    protected String lastName;
    public String getLastName() {return lastName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    
	private Boolean permitLogin;
	public Boolean getPermitLogin() {return permitLogin;}
	public void setPermitLogin(Boolean permitLogin) {this.permitLogin = permitLogin;}
	
	private String lang;
    public String getLang() {return lang;}
    public void setLang(String lang) {this.lang = lang;}
	
	@ManyToMany(fetch=FetchType.LAZY, mappedBy="users")
	private List<SecurityRole> roles;
    public List<SecurityRole> getRoles() {if(roles==null){roles = new ArrayList<SecurityRole>();};return roles;}
    public void setRoles(List<SecurityRole> roles) {this.roles = roles;}


	public String toString()
	{
		StringBuffer sb = new StringBuffer();
			sb.append(id);
			sb.append(" ").append(firstName).append(" ").append(lastName);
		return sb.toString();
	}
	
	public boolean equals(Object object)
	{
        return (object instanceof User) ? id == ((User) object).getId() : (object == this);
    }
}