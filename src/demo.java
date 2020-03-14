import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;


public class demo {
    public static void main(String[] args){

        try{
            // Add command to run on ClI of the device
            String command = "";
            // Add the hostname or host IP
            String host = "";
            // Add the username to login with
            String user = "";
            // Add the password to login with
            String password = "";
            // Add the substring to find in the resulting output of the command
            String stringToFind = "";
            // Add the output required to be displayed if the substring is found
            String ifString = "";
            // Add the output required to be displayed if the substring is NOT found
            String ifStringNot = "";
            //Set true or false if you want to see command execution output display
            boolean showCmdOutput = false;

            // Leave this variable
            boolean successFound = false;


            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, 22);
            Properties config = new Properties();
            // Strict Host Key Checking
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);;
            session.setPassword(password);
            session.connect();

            Channel channel = session.openChannel("exec");
            ((ChannelExec)channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec)channel).setErrStream(System.err);

            InputStream input = channel.getInputStream();
            channel.connect();

            System.out.println("\nChannel Connected to machine " + host + " server with command: " + command + "\n");

            try{
                InputStreamReader inputReader = new InputStreamReader(input);
                BufferedReader bufferedReader = new BufferedReader(inputReader);
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    if (showCmdOutput == true) {
                        System.out.println(line);
                    }
                    else {
                        continue;
                    }
                    boolean isFound = line.contains(stringToFind);
                    if (isFound == true) {
                        successFound = true;
                    }
                    else {
                        continue;
                    }
                }
                if (successFound == false) {
                    System.out.println(ifStringNot);
                }
                else {
                    System.out.println(ifString);
                }
                bufferedReader.close();
                inputReader.close();
            }catch(IOException ex){
                ex.printStackTrace();
            }

            channel.disconnect();
            session.disconnect();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}