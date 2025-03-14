package org.jeesl.model.ejb.system.security.oauth;

import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Column;
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
import org.jeesl.interfaces.model.system.security.oauth.JeeslSecurityOauthKey;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@Entity
@Table(name="SecurityOauthKey")
@EjbErNode(name="SecurityOauthKey")
public class SecurityOauthKey implements JeeslSecurityOauthKey<SecurityOauthKeyType>
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
	@JoinColumn(foreignKey=@ForeignKey(name="fk_SecurityOauthKey_type"))
	private SecurityOauthKeyType type;
	public SecurityOauthKeyType getType() {return type;}
	public void setType(SecurityOauthKeyType type) {this.type = type;}
		
	private boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}

	private LocalDateTime record;
	@Override public LocalDateTime getRecord() {return record;}
	@Override public void setRecord(LocalDateTime record) {this.record = record;}

	private String name;
	@Override public String getName() {return name;}
	@Override public void setName(String name) {this.name = name;}
	
	@Basic @Column(columnDefinition="text")
	private String json;
	@Override public String getJson() {return json;}
	@Override public void setJson(String json) {this.json = json;}


	@Override public boolean equals(Object object){return (object instanceof SecurityOauthKey) ? id == ((SecurityOauthKey) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17, 53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}