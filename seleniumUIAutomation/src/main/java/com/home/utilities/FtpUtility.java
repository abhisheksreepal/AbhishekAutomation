package com.home.utilities;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class FtpUtility
{

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(FtpUtility.class);

    private static PropertiesConfiguration envProperties;
    private static Session session;
    private static Channel channel;
    private static ChannelSftp sftp;

    static
    {
        try
        {
            envProperties = new PropertiesConfiguration(
                    "src/test/resources/config/environment.properties");

        }
        catch (ConfigurationException e)
        {
            LoggerUtility
                    .logTraceMessage(
                            log,
                            "[ERROR]Configuration exception -src/test/resources/config/environment.properties"
                                    + e.toString());
        }
    }

    private static boolean connectToRemoteMachine()
    {
        boolean isSuccessfullyConnected = false;
        session = null;
        channel = null;
        sftp = null;

        JSch ssh = new JSch();
        try
        {
            session = ssh.getSession(envProperties.getString("username"),
                    envProperties.getString("HOST"),
                    envProperties.getInt("PORT"));
            session.setPassword(envProperties.getString("password"));
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
        }
        catch (JSchException e1)
        {

            LoggerUtility
                    .logTraceMessage(
                            log,
                            "[ERROR]JSchException exception - Not able to connect to remote Machine and exception -"
                                    + e1.toString());
            return isSuccessfullyConnected;
        }

        sftp = (ChannelSftp) channel;

        return isSuccessfullyConnected = true;
    }

    private static void disconnect()
    {
        if (channel != null)
        {
            channel.disconnect();
        }
        if (session != null)
        {
            session.disconnect();
        }
    }

    /**
     * adds a file to remote location
     * 
     * if file exists, file is overwrittern and return true if file not exists,
     * file is added and return true otherwise file is NOT added and return
     * false
     * 
     * @param fileName
     *            file to be added
     * @return true if file is successfully added, otherwise false
     * 
     */
    public static boolean overWriteRemoteFile(String inputFilePathString,
            String destinationSourceFolder)
    {

        boolean isAdded = false;

        if (connectToRemoteMachine())
        {

            try
            {
                sftp.put(inputFilePathString, destinationSourceFolder);
                return isAdded = true;
            }
            catch (SftpException e)
            {
                return isAdded;
            }
            finally
            {
                disconnect();
            }

        }
        else
        {
            disconnect();
            return isAdded;
        }
    }

    public static boolean deleteRemoteFile(String destinationFilePathString)
    {

        boolean isdeleted = false;

        if (connectToRemoteMachine())
        {

            try
            {
                sftp.rm(destinationFilePathString);
                return isdeleted = true;
            }
            catch (SftpException e)
            {

                if (e.toString().equalsIgnoreCase("2: No Such File"))
                {
                    return isdeleted = true;
                }
                else
                {
                    LoggerUtility.logTraceMessage(log,
                            "[ERROR]Not able to delete file -"
                                    + destinationFilePathString
                                    + " and exception -" + e.toString());
                    return isdeleted;
                }
            }
            finally
            {
                disconnect();
            }

        }
        else
        {
            disconnect();
            return isdeleted;
        }
    }

}
