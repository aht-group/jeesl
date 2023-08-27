package org.jeesl.interfaces.controller.db;

import java.io.File;

import org.exlp.model.xml.config.Parameters;

import net.sf.exlp.exception.ExlpUnsupportedOsException;
import net.sf.exlp.interfaces.util.TextWriter;

public interface UtilsDbShell
{
	public static enum Operation {dump,restore,drop}
	public static enum Scope {all,data,structure}
	
	public static final String cfgBinPsql = "db.bin.psql";
	public static final String cfgBinDump = "db.bin.dump";
	public static final String cfgBinRestore = "db.bin.restore";
	public static final String cfgBinDrop = "db.bin.drop";
	
	public static final String hostRestore = "db.restore.host";
	public static final String restoreUser = "db.restore.user.name";
	public static final String restorePwd = "db.restore.user.password";
	
	
	public static final String cfgDbDrop = "db.tables.drop";
	public static final String cfgDbDropTable = "db.tables.drop.table";
	public static final String cfgDbDropCascade = "db.tables.drop.cascade";
	public static final String cfgDbDropSequence = "db.tables.drop.sequence";
	
	public static final String restoreTable = "db.tables.restore.table";
	public static final String restoreTrigger = "db.tables.restore.ttable";
	public static final String restroreSequence = "db.tables.restore.sequence";
	public static final String cfgDbTablesKey = "db.tables.key.table";
	
	public static final String dirRestore = "db.dir.restore";
	public static final String dirDump = "db.dir.dump";
	public static final String cfgDirShell = "db.dir.shell";
	
	TextWriter getWriter();
	File getShellFile();
	
	public void buildCommands(boolean withStructure) throws ExlpUnsupportedOsException;
	
	Parameters getConfigurationParameter();
}
