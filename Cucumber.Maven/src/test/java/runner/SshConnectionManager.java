
package com.counterparty.automation.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.counterparty.automation.utilities.Functions;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SshConnectionManager {

	
	    private Session session;
	    private ChannelExec channel;
	    private String username = "";
	    private String password = "";
	    private String hostname = "";
	    private OutputStream out;
	    final static Logger logger = Logger.getLogger(SshConnectionManager.class);


	    private Session getSession(){
	        if(session == null || !session.isConnected()){
	            session = connect(hostname,username,password);
	        }
	        return session;
	    }
	    

	    private ChannelExec getChannel(){
	        if(channel == null || !channel.isConnected()){
	            try{
	                channel = (ChannelExec)getSession().openChannel("exec");
	                channel.setPty(false);
	                channel.setErrStream(System.err);
	    			channel.setInputStream(null, true);	
	    			out = channel.getOutputStream();
	    			channel.setOutputStream(System.out, true);
	    			channel.setExtOutputStream(System.err, true);
	    			channel.setPty(true);
					
	            }catch(Exception e){
	                logger.error("Error while opening channel: "+ e.getMessage());
	            }
	        }
	        return channel;
	    }
	    
	    public void sudoUser(String command){
	    	channel = getChannel();
	    	channel.setCommand(command);
	    	try {
				channel.connect();
			} catch (JSchException e) {
				e.printStackTrace();
			}
	    }

	    public Session connect(String hostname, String username, String password){

	        JSch jSch = new JSch();

	        try {

	            session = jSch.getSession(username, hostname, 22);
	            Properties config = new Properties(); 
	            config.put("StrictHostKeyChecking", "no");
	            config.put("PreferredAuthentications", "password");
	            session.setConfig(config);
	            session.setPassword(password);

	            logger.info("Connecting SSH to " + hostname + " - Please wait for few seconds... ");
	            session.connect();
	            logger.info("Connected!");
	        }catch(Exception e){
	        	logger.error("An error occurred while connecting to "+hostname+": "+e.getMessage());
	        }

	        return session;

	    }

	    public void executeCommand(String command, int wait){

	        try{
	          //  System.out.println("Sending command: "+ command);
	            out.write((command + "\n").getBytes());
				out.flush();
				logger.info("waiting...");
				Thread.sleep(wait*1000);

	        }catch(Exception e){
	        	logger.error("An error ocurred during executeCommand: "+e.getMessage());
	        }
	    }

	    

	    private static void readChannelOutput(ChannelExec channel){

	        byte[] buffer = new byte[1024];

	        try{
	            InputStream in = channel.getInputStream();
	            String line = "";
	            while (true){
	                while (in.available() > 0) {
	                    int i = in.read(buffer, 0, 1024);
	                    if (i < 0) {
	                        break;
	                    }
	                    line = new String(buffer, 0, i);
	                    System.out.println(line);
	                }

	                if(line.contains("logout")){
	                    break;
	                }

	                if (channel.isClosed()){
	                    break;
	                }
	                try {
	                    Thread.sleep(1000);
	                } catch (Exception ee){}
	            }
	        }catch(Exception e){
	            System.out.println("Error while reading channel output: "+ e);
	        }

	    }

	    public void close(){
	    	try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        channel.disconnect();
	        session.disconnect();
	        System.out.println("Disconnected channel and session");
	    }
	    
	    
}
