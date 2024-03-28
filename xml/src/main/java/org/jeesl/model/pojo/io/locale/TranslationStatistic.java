package org.jeesl.model.pojo.io.locale;


public class TranslationStatistic
{

	// >>>>>>>>>>>>>>>>>>>>>>>>>>Fields<<<<<<<<<<<<<<<<<<<<<<<<<<<
		
	private String file;

	private int allTranslations,versionOutdated,missing;
	
	
	// >>>>>>>>>>>>>>>>>>>>>>>>>>Getters and Setters<<<<<<<<<<<<<<<<<
	
	public String getFile() {return file;}
	public void setFile(String file) {this.file = file;}
	
	public int getAllTranslations() {return allTranslations;}
	public void setAllTranslations(int allTranslations) {this.allTranslations = allTranslations;}

	public int getVersionOutdated() {return versionOutdated;}
	public void setVersionOutdated(int versionOutdated) {this.versionOutdated = versionOutdated;}

	public int getMissing() {return missing;}
	public void setMissing(int missing) {this.missing = missing;}
	
	// >>>>>>>>>>>>>>>>>>>>>>>>>>Methods<<<<<<<<<<<<<<<<<<<<<<<<<<<	
	
}