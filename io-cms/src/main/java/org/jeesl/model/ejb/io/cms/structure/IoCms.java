package org.jeesl.model.ejb.io.cms.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.cms.JeeslIoCms;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;

@Entity
@Table(name="IoCms")
@EjbErNode(name="Mail",category="system",subset="handbook")
public class IoCms implements JeeslIoCms<IoLang,IoDescription,IoLocale,IoCmsCategory,IoCmsSection>
{
	public static final long serialVersionUID=1;	


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@ManyToOne
	private IoCmsCategory category;
	@Override public IoCmsCategory getCategory() {return category;}
	@Override public void setCategory(IoCmsCategory category) {this.category = category;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="IoCmsJtLang",joinColumns={@JoinColumn(name="cms_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}

	@NotNull @OneToOne(cascade=CascadeType.ALL)
	private IoCmsSection root;
	@Override public IoCmsSection getRoot() {return root;}
	@Override public void setRoot(IoCmsSection root) {this.root = root;}

	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="IoCmsJtLanguage",joinColumns={@JoinColumn(name="cms_id")},inverseJoinColumns={@JoinColumn(name="locale_id")})
	private List<IoLocale> locales;
	@Override public List<IoLocale> getLocales() {if(Objects.isNull(locales)) {locales = new ArrayList<>();} return locales;}
	@Override public void setLocales(List<IoLocale> locales) {this.locales = locales;}

	private boolean toc;
	@Override public boolean getToc() {return toc;}
	@Override public void setToc(boolean toc) {this.toc = toc;}


	@Override public boolean equals(Object object){return (object instanceof IoCms) ? id == ((IoCms) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}