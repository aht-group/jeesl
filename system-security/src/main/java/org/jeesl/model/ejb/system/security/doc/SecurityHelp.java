package org.jeesl.model.ejb.system.security.doc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.security.doc.JeeslSecurityOnlineHelp;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.cms.structure.IoCms;
import org.jeesl.model.ejb.io.cms.structure.IoCmsSection;
import org.jeesl.model.ejb.system.security.page.SecurityView;

@Entity
@Table(name="SecurityHelp")
@EjbErNode(name="SecurityHelp")
public class SecurityHelp implements JeeslSecurityOnlineHelp<SecurityView,IoCms,IoCmsSection>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@Override public String resolveParentAttribute() {return JeeslSecurityOnlineHelp.Attributes.view.toString();}
	@NotNull @ManyToOne
	private SecurityView view;
	@Override public SecurityView getView() {return view;}
	@Override public void setView(SecurityView view) {this.view = view;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	private boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}

	@NotNull @ManyToOne
	private IoCms document;
	@Override public IoCms getDocument() {return document;}
	@Override public void setDocument(IoCms document) {this.document = document;}

	@NotNull @ManyToOne
	private IoCmsSection section;
	@Override public IoCmsSection getSection() {return section;}
	@Override public void setSection(IoCmsSection section) {this.section = section;}


	@Override public boolean equals(Object object){return (object instanceof SecurityHelp) ? id == ((SecurityHelp) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}