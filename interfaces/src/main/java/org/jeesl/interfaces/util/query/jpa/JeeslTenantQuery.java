package org.jeesl.interfaces.util.query.jpa;

import java.io.Serializable;

import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslTenantQuery <REALM extends JeeslTenantRealm<?,?,REALM,?>> extends Serializable
{
	TenantIdentifier<REALM> getTenantIdentifier();
}