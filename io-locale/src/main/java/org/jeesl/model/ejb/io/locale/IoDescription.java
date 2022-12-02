package org.jeesl.model.ejb.io.locale;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;

@Entity
@Table(name="IoDescription")
public class IoDescription implements JeeslDescription
{
	public static final long serialVersionUID=1l;
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	@NotNull
	private String lkey;
	public String getLkey() {return lkey;}
	public void setLkey(String lkey) {this.lkey = lkey;}
	
	@NotNull @Basic @Column(columnDefinition="text")
	private String lang;
	public String getLang() {return lang;}
	public void setLang(String name) {this.lang = name;}
	
	private Boolean styled;
	@Override public Boolean getStyled() {return styled;}
	@Override public void setStyled(Boolean styled) {this.styled = styled;}

	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(id);
		sb.append(" ["+lkey+"]");
		sb.append(" "+lang);
		return sb.toString();
	}
}