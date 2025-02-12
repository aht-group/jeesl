package org.jeesl.connectors.tools;  

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.swing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerTrayIcon {
	
	final static Logger logger = LoggerFactory.getLogger(ServerTrayIcon.class);
	
    public static void renderTrayControl() {
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
    private static void createAndShowGUI() {
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon =
                new TrayIcon(createImage("/images/mono-kdeprint-report-20px.png", "tray icon"));
        final SystemTray tray = SystemTray.getSystemTray();
        
        // Create a popup menu components
        MenuItem aboutItem = new MenuItem("Show Server Address");
        MenuItem exitItem = new MenuItem("Quit");
        
        //Add components to popup menu
        popup.add(aboutItem);
        popup.add(exitItem);
        
        trayIcon.setPopupMenu(popup);
        
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            logger.warn("TrayIcon could not be added.");
            return;
        }
        
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "This IP address of this server is " +getIp());
            }
        });
        
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                System.exit(0);
            }
        });
    }
    
    protected static Image createImage(String path, String description) {
        URL imageURL = ServerTrayIcon.class.getResource(path);
        
        if (imageURL == null) {
            logger.error("Tray icon could not be found in: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
	
	public static String getIp() {
		String address = "prolem requesting IP";
        BufferedReader in = null;
        try {
			URL awsRequest = new URL("http://checkip.amazonaws.com");
            in = new BufferedReader(new InputStreamReader(
            awsRequest.openStream()));
            address = in.readLine();
        } catch (IOException ex) {
			logger.warn("Could not request IP from AWS: " +ex.getMessage());
		} finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("Could not close connection to AWS " +e.getMessage());
                }
            }
        }
		return address;
	}
}
