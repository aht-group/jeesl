package net.sf.ahtutils.doc.loc.counter;

import java.io.FileNotFoundException;
import java.io.IOException;

import net.sf.ahtutils.doc.loc.BasicFileInfo;

/*
 * @author Thorsten Kisner
 */
public interface LineCounter
{
	BasicFileInfo countlines() throws FileNotFoundException, IOException;
}
