package org.jeesl.model.ejb.io.ssi.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiCredential;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@Entity
@Table(name="IoSsiCredential",uniqueConstraints={@UniqueConstraint(name="uk_IoSsiCredential_system_code",columnNames={"system_id","code"})})
@EjbErNode(name="Credential")
public class IoSsiCredential implements JeeslIoSsiCredential<IoSsiSystem>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@Override public String resolveParentAttribute() {return JeeslIoSsiCredential.Attributes.system.toString();}
	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoSsiCredential_system"))
	private IoSsiSystem system;
	@Override public IoSsiSystem getSystem(){return system;}
	@Override public void setSystem(IoSsiSystem system){this.system = system;}

	@NotNull
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	private Boolean visible;
	@Override public Boolean getVisible() {return visible;}
	@Override public void setVisible(Boolean visible) {this.visible = visible;}

	@Column(name="username")
	private String user;
	@Override public String getUser() {return user;}
	@Override public void setUser(String user) {this.user = user;}

	private String pwd;
	@Override public String getPwd() {return pwd;}
	@Override public void setPwd(String pwd) {this.pwd = pwd;}

	private String token;
	@Override public String getToken() {return token;}
	@Override public void setToken(String token) {this.token = token;}

	private String url;
	@Override public String getUrl() {return url;}
	@Override public void setUrl(String url) {this.url = url;}

	private String host;
	@Override public String getHost() {return host;}
	@Override public void setHost(String host) {this.host = host;}

	private Integer port;
	@Override public Integer getPort() {return port;}
	@Override public void setPort(Integer port) {this.port = port;}

	private boolean encrypted;
	@Override public boolean isEncrypted() {return encrypted;}
	@Override public void setEncrypted(boolean encrypted) {this.encrypted = encrypted;}
	
//	@Basic @Column(columnDefinition="text")
//	private String keyPrivate;
//	public String getKeyPrivate() {return keyPrivate;}
//	public void setKeyPrivate(String keyPrivate) {this.keyPrivate = keyPrivate;}
//	
//	@Basic @Column(columnDefinition="text")
//	private String keyPublic;
//	public String getKeyPublic() {return keyPublic;}
//	public void setKeyPublic(String keyPublic) {this.keyPublic = keyPublic;}


	@Override public boolean equals(Object object){return (object instanceof IoSsiCredential) ? id == ((IoSsiCredential) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		sb.append(" (").append(system.getCode()).append(")");
		sb.append(" ").append(code);
		return sb.toString();
	}
}