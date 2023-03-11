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

import org.jeesl.interfaces.model.system.locale.JeeslMarkup;

@Entity
@Table(name="IoMarkup")
public class IoMarkup implements JeeslMarkup<IoMarkupType>
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

	@NotNull @ManyToOne
	private IoMarkupType type;
	@Override public IoMarkupType getType() {return type;}
	@Override public void setType(IoMarkupType type) {this.type = type;}
	
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