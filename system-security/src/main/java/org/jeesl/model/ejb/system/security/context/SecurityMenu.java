package org.jeesl.model.ejb.system.security.context;

import java.util.Map;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityMenu;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.system.security.page.SecurityView;

@Entity
@Table(name="SecurityMenu",uniqueConstraints=@UniqueConstraint(name="UK_SecurityMenu_context_view",columnNames={"context_id","view_id"}))
@EjbErNode(name="SecurityMenu")
public class SecurityMenu implements JeeslSecurityMenu<IoLang,SecurityView,SecurityContext,SecurityMenu>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@NotNull @ManyToOne
	private SecurityContext context;
	@Override public SecurityContext getContext() {return context;}
	@Override public void setContext(SecurityContext context) {this.context = context;}

	@Override public String resolveParentAttribute() {return JeeslSecurityMenu.Attributes.parent.toString();}
	@ManyToOne
	private SecurityMenu parent;
	@Override public SecurityMenu getParent() {return parent;}
	@Override public void setParent(SecurityMenu parent) {this.parent = parent;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	private Boolean visible;
	@Override public Boolean getVisible() {return visible;}
	@Override public void setVisible(Boolean visible) {this.visible = visible;}

	@NotNull @OneToOne
	private SecurityView view;
	@Override public SecurityView getView() {return view;}
	@Override public void setView(SecurityView view) {this.view = view;}

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="SecurityMenuJtLang",joinColumns={@JoinColumn(name="menu_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}


	@Override public boolean equals(Object object){return (object instanceof SecurityMenu) ? id == ((SecurityMenu) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17, 53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}