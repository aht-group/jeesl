package org.jeesl.model.ejb.io.cms.markup;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@Audited
@Entity
@Table(name="IoMarkup")
@EjbErNode(name="Markup",category="ioCms",subset="ioCms")
public class IoMarkup implements JeeslIoMarkup<IoMarkupType>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	@NotNull
	private String lkey;
	@Override public String getLkey() {return lkey;}
	@Override public void setLkey(String lkey) {this.lkey = lkey;}
	
	@NotNull @Basic @Column(columnDefinition="text")
	private String content;
	@Override public String getContent() {return content;}
	@Override public void setContent(String content) {this.content = content;}

	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@NotNull @ManyToOne
	private IoMarkupType type;
	@Override public IoMarkupType getType() {return type;}
	@Override public void setType(IoMarkupType type) {this.type = type;}


	@Override public boolean equals(Object object) {return (object instanceof IoMarkup) ? id == ((IoMarkup) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(17,37).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		sb.append(" ").append(lkey);
		sb.append("-").append(type.getCode());
		sb.append(": ").append(content);
		return sb.toString();
	}
}