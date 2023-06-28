package org.jeesl.model.ejb.module.cl;

import java.time.LocalDate;
import java.util.HashMap;
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
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.cl.JeeslClCheckItem;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.cms.markup.IoMarkup;
import org.jeesl.model.ejb.io.locale.IoLang;

@Table(name="ClChecklistItem")
@EjbErNode(name="Checklist",category="tafu",subset="moduleCl")
@Entity
public class ClCheckItem implements JeeslClCheckItem<IoLang,ClCheckList,IoMarkup>
{
	public static final long serialVersionUID=1;

	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	@Override public String resolveParentAttribute() {return JeeslClCheckItem.Attributes.checklist.toString();}
	@NotNull @ManyToOne
	private ClCheckList checklist;
	@Override public ClCheckList getChecklist() {return checklist;}
	@Override public void setChecklist(ClCheckList checklist) {this.checklist = checklist;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	private boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}
	
	private LocalDate validFrom;
	@Override public LocalDate getValidFrom() {return validFrom;}
	@Override public void setValidFrom(LocalDate validFrom) {this.validFrom = validFrom;}
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="ClChecklistItemJtLang",joinColumns={@JoinColumn(name="item_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	@MapKey(name="lkey")
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {if(Objects.isNull(name)) {name=new HashMap<>();} return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="ClChecklistItemJtMarkup",joinColumns={@JoinColumn(name="item_id")},inverseJoinColumns={@JoinColumn(name="markup_id")})
	protected Map<String,IoMarkup> markup;
	@Override public Map<String,IoMarkup> getMarkup() {return markup;}
	@Override public void setMarkup(Map<String,IoMarkup> markup) {this.markup = markup;}


	@Override public boolean equals(Object object) {return (object instanceof ClCheckItem) ? id == ((ClCheckItem) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(23,7).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		
		return sb.toString();
	}
}