package org.jeesl.api.rest.extern;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.json.ssi.deepl.JsonDeeplTranslations;
import org.jeesl.model.json.ssi.deepl.JsonDeeplUsage;

public interface DeeplRest
{				
    @GET @Path("/v2/usage")
	@Produces(MediaType.APPLICATION_JSON)
    JsonDeeplUsage usage(@QueryParam("auth_key") String apiKey);
    
    @POST @Path("/v2/translate")
    @Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.TEXT_PLAIN)
    String debug(@QueryParam("auth_key") String apiKeyH,
    					@FormParam("text") String test,
    					@FormParam("source_lang") String sourceLang,
    					@FormParam("target_lang") String targetLang);
    
    @POST @Path("/v2/translate")
    @Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
    JsonDeeplTranslations translate(@QueryParam("auth_key") String apiKeyH,
    					@FormParam("text") String test,
    					@FormParam("source_lang") String sourceLang,
    					@FormParam("target_lang") String targetLang);
}