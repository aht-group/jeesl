package org.jeesl.controller.web.system.security;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import org.exlp.util.system.DateUtil;
import org.jeesl.controller.util.comparator.ejb.module.calendar.EjbWithRecordDateComparator;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.security.pwd.JeeslSecurityPasswordRating;
import org.jeesl.interfaces.model.system.security.pwd.JeeslSecurityPasswordRule;
import org.jeesl.interfaces.model.system.security.user.pwd.JeeslPasswordHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;

public class JeeslPasswordGwc <RATING extends JeeslSecurityPasswordRating<?,?,?,?>,
										RULE extends JeeslSecurityPasswordRule<?,?,?,?>,
										HISTORY extends JeeslPasswordHistory<?>>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslPasswordGwc.class);
	
	private final String regexUpper = "(.*?[A-Z]){%d,}.*?";
	private final String regexSymbols = "(.*?[_.*+:#!?%%{}\\|@\\[\\];=\"&$\\\\/,()-]){%d,}.*?";
	
	private final Class<RATING> cRating;
	private RATING rating;

	private final Map<RULE,Boolean> mapResult; public Map<RULE,Boolean> getMapResult() {return mapResult;}

	private JeeslFacade facade;
	
	private final EjbWithRecordDateComparator<HISTORY> cpHistory;
	private boolean debugOnInfo; public void setDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo = debugOnInfo;}

	public JeeslPasswordGwc(Class<RATING> cRating)
	{		
		this.cRating=cRating;
		
		mapResult = new HashMap<>();
		
		cpHistory = new EjbWithRecordDateComparator<>();
		debugOnInfo = false;
	}
	
	public void genericPostConstruct(JeeslFacade facade)
	{
		this.facade=facade;
	}
	
	private void clear()
	{
		mapResult.clear();
	}
	
	public void bypassAgeCheck(List<RULE> rules)
	{		
		for(RULE r : rules)
		{
			if(r.getCode().equals(JeeslSecurityPasswordRule.Code.age.toString())) {mapResult.put(r,true);}
		}
	}
	
	public boolean passwordCompliant()
	{
		boolean result = true;
		for(Boolean b : mapResult.values())
		{
			if(b==false) {result=false;}
		}
		return result;
	}
	
	public void analyseComplexity1(List<RULE> rules, String pwd) throws JeeslNotFoundException
	{
		
		Zxcvbn zxcvbn = new Zxcvbn();
		Strength strength = zxcvbn.measure(pwd);
		
		if(debugOnInfo) {logger.info("fJeesl:"+(facade==null));}
		if(debugOnInfo) {logger.info("strength:"+(strength==null));}
		rating = facade.fByCode(cRating,JeeslSecurityPasswordRating.codePrefix+""+strength.getScore());
		
		analyseComplexity0(rules,pwd,rating);
	}
	
	public void analyseComplexity0(List<RULE> rules, String pwd, RATING rating)
	{
		this.clear();
		for(RULE r : rules)
		{
			Integer min = Integer.valueOf(r.getSymbol());
			if(r.getCode().equals(JeeslSecurityPasswordRule.Code.length.toString())) {mapResult.put(r, validLength(pwd,min));}
			else if(r.getCode().equals(JeeslSecurityPasswordRule.Code.digit.toString())) {mapResult.put(r, validDigits(pwd,min));}
			else if(r.getCode().equals(JeeslSecurityPasswordRule.Code.lower.toString())) {mapResult.put(r, validLower(pwd,min));}
			else if(r.getCode().equals(JeeslSecurityPasswordRule.Code.upper.toString())) {mapResult.put(r, validUpper(pwd,min));}
			else if(r.getCode().equals(JeeslSecurityPasswordRule.Code.symbol.toString())) {mapResult.put(r, validSymbols(pwd,min));}
			else if(r.getCode().equals(JeeslSecurityPasswordRule.Code.rating.toString()))
			{
				if(rating==null) {mapResult.put(r, false);}
				else {mapResult.put(r, validRating(rating,min));}
			}
		}
	}
	
	public void analyseHistory(List<RULE> rules, String hash, List<HISTORY> histories)
	{
		Collections.sort(histories,cpHistory);
		Collections.reverse(histories);
		
		if(debugOnInfo) {for(HISTORY h : histories) {logger.info(h.toString());}}
		
		for(RULE r : rules)
		{
			Integer value = Integer.valueOf(r.getSymbol());
			if(r.getCode().equals(JeeslSecurityPasswordRule.Code.history.toString()))
			{
				boolean isValidHistory = validHistory(value,hash,histories);
				if(debugOnInfo) {logger.info("Valid History: "+isValidHistory);}
				mapResult.put(r,isValidHistory);
			}
			else if(r.getCode().equals(JeeslSecurityPasswordRule.Code.age.toString()))
			{
				boolean isValidAge = validAge(value,histories);
				if(debugOnInfo) {logger.info("Valid Age: "+isValidAge);}
				mapResult.put(r, isValidAge);
			}
		}
	}
	
	public String value(RULE r, String pwd)
	{
		if(r.getCode().equals(JeeslSecurityPasswordRule.Code.length.toString())) {return Integer.valueOf(this.actualLenght(pwd)).toString();}
		else if(r.getCode().equals(JeeslSecurityPasswordRule.Code.digit.toString())) {return Integer.valueOf(this.actualDigits(pwd)).toString();}
		else if(r.getCode().equals(JeeslSecurityPasswordRule.Code.lower.toString())) {return Integer.valueOf(this.actualLower(pwd)).toString();}
		else if(r.getCode().equals(JeeslSecurityPasswordRule.Code.upper.toString())) {return Integer.valueOf(this.actualUpper(pwd)).toString();}
		else if(r.getCode().equals(JeeslSecurityPasswordRule.Code.symbol.toString())) {return Integer.valueOf(this.actualSymbols(pwd)).toString();}
		else if(r.getCode().equals(JeeslSecurityPasswordRule.Code.rating.toString())) {return Objects.nonNull(rating) ? rating.getCode() : "--";}
		else {return "nyi";}
	}
	
	public boolean validLength(String pwd, int min) {return this.actualLenght(pwd)>=min;}
	private int actualLenght(String pwd) {return pwd.length();}
	
	public boolean validDigits(String pwd, int min) {return this.actualDigits(pwd)>=min;}
	private int actualDigits(String pwd) {return Long.valueOf(pwd.chars().filter(Character::isDigit).count()).intValue();}
	
	public boolean validLower(String pwd, int min) {return this.actualLower(pwd)>=min;}
	private int actualLower(String pwd) {return Long.valueOf(pwd.chars().filter(Character::isLowerCase).count()).intValue();}
	
	protected boolean validUpper(String pwd, int min) {return this.actualUpper(pwd)>=min;}
	private int actualUpper(String pwd) {return Long.valueOf(pwd.chars().filter(Character::isUpperCase).count()).intValue();}
	
	protected boolean validSymbols(String pwd, int min)
	{
		return Pattern.matches(String.format(regexSymbols, min), pwd);
	}
	private int actualSymbols(String pwd) {return pwd.length() - this.actualDigits(pwd) - this.actualLower(pwd) - this.actualUpper(pwd);}
	
	protected boolean validRating(RATING rating, int min)
	{
		return rating.getPosition()>=min;
	}
	
	protected boolean validHistory(int maxMonths, String hash, List<HISTORY> histories)
	{
		LocalDate ldNow = LocalDate.now();
		
		if(histories==null || histories.isEmpty() || histories.size()>=1) {return true;}
		
		for(HISTORY h : histories.subList(1,histories.size()))
		{
			LocalDate ld = DateUtil.toLocalDate(h.getRecord());
			long months = ChronoUnit.MONTHS.between(ld, ldNow);
			if(h.getPwd().contentEquals(hash) && months<=maxMonths)
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean validAge(int maxDays, List<HISTORY> histories)
	{
		if(histories.isEmpty()) {return true;}
		LocalDate ld = DateUtil.toLocalDate(histories.get(0).getRecord());
		LocalDate ldNow = LocalDate.now();

		long days = ChronoUnit.DAYS.between(ld, ldNow);
		
		boolean ageIsValid = false;
		if(days<maxDays) {ageIsValid = true;}
		
		return ageIsValid;
	}
}