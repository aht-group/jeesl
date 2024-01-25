package org.jeesl.util.scripting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.sf.exlp.util.io.resourceloader.MultiResourceLoader;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScriptExecutor
{   
    final static Logger logger = LoggerFactory.getLogger(ScriptExecutor.class);
    
    List<String> commands = new ArrayList<String>();
    
    public ScriptExecutor(Interpreter.scripts interpreter)
    {
         if (interpreter.equals(Interpreter.scripts.VBSCRIPT)){commands.add( "cscript");}
    }
    
    public String execute(String scriptFile, List<String> cmdLineParameters) throws IOException
    {
         // Prepare the command line

        MultiResourceLoader mrl = MultiResourceLoader.instance();
        File tempScriptFile = createScriptFile(mrl.searchIs(scriptFile));

        logger.trace("Loaded script " +scriptFile + " from Resources and created temporary script " +tempScriptFile.getAbsolutePath() +" for execution.");
        commands.add(tempScriptFile.getAbsolutePath());

        commands.addAll(cmdLineParameters);
        showOptions(cmdLineParameters);

        // Create a process builder in temporary directory
        ProcessBuilder builder = new ProcessBuilder(commands);  
        builder.directory(new File(System.getenv("temp")));  

        // Run the process and read the stream
        final Process process   = builder.start();  
        InputStream is              = process.getInputStream();  
        InputStreamReader isr = new InputStreamReader(is);  
        BufferedReader br       = new BufferedReader(isr);  

        // Output data from script written to console as String
        // And return the last line for further processing
        String line;  
        String fullText = "";
        while ((line = br.readLine()) != null) {  
          logger.trace(line);
          fullText = line;
        }  
        logger.trace("Script completed.");
        return fullText;
    }
    
    public List<String> getList(String scriptFile, List<String> cmdLineParameters) throws IOException
    {
         // Prepare the command line

        MultiResourceLoader mrl = MultiResourceLoader.instance();
        File tempScriptFile = createScriptFile(mrl.searchIs(scriptFile));

        logger.trace("Loaded script " +scriptFile + " from Resources and created temporary script " +tempScriptFile.getAbsolutePath() +" for execution.");
        commands.add(tempScriptFile.getAbsolutePath());

        commands.addAll(cmdLineParameters);
        showOptions(cmdLineParameters);

        // Create a process builder in temporary directory
        ProcessBuilder builder = new ProcessBuilder(commands);  
        builder.directory(new File(System.getenv("temp")));  

        // Run the process and read the stream
        final Process process   = builder.start();  
        InputStream is              = process.getInputStream();  
        InputStreamReader isr = new InputStreamReader(is);  
        BufferedReader br       = new BufferedReader(isr);  

        // Output data from script written to console as String
        // And return the list of lines for further processing
        String line;  
        Boolean isRelevant = false;
        List<String> list = new ArrayList<String>();
        while ((line = br.readLine()) != null) {  
          logger.trace(line);
          // We need to wait for the line that says @@@Begin Content@@@
          // to avoid getting infos like the Scripting Host Version
          if (isRelevant) 
            { list.add(line);}
          if (line.equals("@@@Begin Content@@@"))
            { isRelevant = true; }
        }  
        logger.trace("Script completed.");
        return list;
    }

    public static File createScriptFile (InputStream in) throws IOException
    {
        final File tempFile = File.createTempFile(UUID.randomUUID().toString(), ".vbs");
 //       tempFile.deleteOnExit();
        FileOutputStream out = new FileOutputStream(tempFile);
        IOUtils.copy(in, out);
        out.close();
        return tempFile;
    }

    public static void showOptions(List<String> cmdLineParameters)
    {
        int i = 0;
        for (String option : cmdLineParameters)
        {
            logger.trace("Argument " +i +": " +option);
            i++;
        }
    }
}
