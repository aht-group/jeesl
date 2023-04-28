package org.jeesl.model.ejb.module.aom.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEvent;
import org.jeesl.model.ejb.io.cms.markup.IoMarkup;
import org.jeesl.model.ejb.io.fr.IoFileContainer;
import org.jeesl.model.ejb.module.aom.asset.AomAsset;
import org.jeesl.model.ejb.module.aom.company.AomCompany;
import org.jeesl.model.ejb.system.security.user.SecurityUser;

@Entity
@Table(name="AomEvent")
public class AomEvent implements JeeslAomEvent<AomCompany,AomAsset,AomEventType,AomEventStatus,IoMarkup,SecurityUser,IoFileContainer>
{
	public static final long serialVersionUID = 1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="AomEventJtAsset",joinColumns={@JoinColumn(name="event_id")},inverseJoinColumns={@JoinColumn(name="asset_id")},uniqueConstraints=@UniqueConstraint(columnNames={"event_id","asset_id"}))
	private List<AomAsset> assets;
	@Override public List<AomAsset> getAssets() {if(assets==null) {assets = new ArrayList<>();} return assets;}
	@Override public void setAssets(List<AomAsset> assets) {this.assets = assets;}

	@NotNull @ManyToOne
	private AomEventType type;
	@Override public AomEventType getType() {return type;}
	@Override public void setType(AomEventType type) {this.type = type;}

	@NotNull @ManyToOne
	private AomEventStatus status;
	@Override public AomEventStatus getStatus() {return status;}
	@Override public void setStatus(AomEventStatus status) {this.status = status;}

	private Date record;
	@Override public Date getRecord() {return record;}
	@Override public void setRecord(Date record) {this.record = record;}

	@NotNull
	private String name;
	@Override public String getName() {return name;}
	@Override public void setName(String name) {this.name = name;}

	@NotNull @Basic @Column(columnDefinition="text")
	private String remark;
	@Override public String getRemark() {return remark;}
	@Override public void setRemark(String remark) {this.remark = remark;}

	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	private IoMarkup markup;
	@Override public IoMarkup getMarkup() {return markup;}
	@Override public void setMarkup(IoMarkup markup) {this.markup = markup;}

	@ManyToOne
	private AomCompany company;
	@Override public AomCompany getCompany() {return company;}
	@Override public void setCompany(AomCompany company) {this.company = company;}

	private Double amount;
	@Override public Double getAmount() {return amount;}
	@Override public void setAmount(Double amount) {this.amount = amount;}

	@OneToOne
	private IoFileContainer frContainer;
	@Override public IoFileContainer getFrContainer() {return frContainer;}
	@Override public void setFrContainer(IoFileContainer frContainer) {this.frContainer = frContainer;}


	@Override public boolean equals(Object o) {return (o instanceof AomEvent) ? id == ((AomEvent)o).getId() : (o == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,51).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}