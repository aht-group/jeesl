package org.jeesl.model.ejb.io.cms.content;

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
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsContent;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.cms.authoring.IoCmsVisibility;
import org.jeesl.model.ejb.io.cms.markup.IoMarkupType;

@Entity
@Table(name="IoCmsContent")
@EjbErNode(name="Content",category="system",subset="cms")
public class IoCmsContent implements JeeslIoCmsContent<IoCmsVisibility,IoCmsElement,IoMarkupType>
{
	public static final long serialVersionUID=1;	


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@Override public String resolveParentAttribute() {return JeeslIoCmsContent.Attributes.element.toString();}
	@NotNull @ManyToOne
	private IoCmsElement element;
	@Override public IoCmsElement getElement() {return element;}
	@Override public void setElement(IoCmsElement element) {this.element = element;}

	@NotNull
	private String lkey;
	@Override public String getLkey() {return lkey;}
	@Override public void setLkey(String lkey) {this.lkey = lkey;}

	@NotNull
	@Basic @Column(columnDefinition="text")
	private String lang;
	@Override public String getLang() {return lang;}
	@Override public void setLang(String name) {this.lang = name;}

	@NotNull @ManyToOne
	private IoMarkupType markup;
	@Override public IoMarkupType getMarkup() {return markup;}
	@Override public void setMarkup(IoMarkupType markup) {this.markup = markup;}

	private boolean fallback;
	@Override public boolean isFallback() {return fallback;}
	@Override public void setFallback(boolean fallback) {this.fallback = fallback;}


	@Override public boolean equals(Object object){return (object instanceof IoCmsContent) ? id == ((IoCmsContent) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}