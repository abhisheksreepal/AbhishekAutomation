package com.home.utilities;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

public class ObjectRepoUtility extends LoggerUtility
{

    private static Logger log = Logger.getLogger(ObjectRepoUtility.class);

    public static HashMap<String, HashMap<String, String>> loginObjRepo;
    public static HashMap<String, HashMap<String, String>> homePageObjRepo;
    public static HashMap<String, HashMap<String, String>> logoutPageObjRepo;

    public static void loadObjectRepoForAllPages()
    {
        loginObjRepo = ObjectRepoUtility.fetchObjectFromFile("loginPage");
        homePageObjRepo = ObjectRepoUtility.fetchObjectFromFile("homePage");
        logoutPageObjRepo = ObjectRepoUtility.fetchObjectFromFile("logoutPage");
    }



    public enum ObjectType
    {

        XPATH, ID, NAME, CLASS, CSSPATH, LINKTEXT, PARTIALLINKTEXT, TAGNAME;

        static public boolean isDefined(String objTypeElement)
        {
            boolean isDefined = false;
            for (ObjectType objType : ObjectType.values())
            {
                if (objTypeElement != null)
                {
                    if (objTypeElement.equalsIgnoreCase(objType.toString()))
                    {
                        isDefined = true;
                        return isDefined;
                    }
                }
            }
            return isDefined;
        }
    }

    /**
     * Method:fetchObjectFromFile Description:it fetch objects from external
     * file to Hash Map
     * 
     * Object File: Description Object Name, Object Type, Object Value => will
     * be converted to a Hashmap for intenal use
     * 
     * example: {ObjectName:(ObjectType:ObjectValue) and LAST keyValue
     * :("fileDetails":(fileName:filePath))
     * 
     * @return
     */
    private static HashMap<String, HashMap<String, String>> fetchObjectFromFile(
            String fileName)
    {

        String applicationName = envProperties.getString("application");

        HashMap<String, HashMap<String, String>> objRepo = new HashMap<String, HashMap<String, String>>();
        if (fileName != null)
        {
            CSVReader reader;
            try
            {
                reader = new CSVReader(new FileReader(
                        envProperties.getString("objRepoLocation") + "/"
                                + applicationName + "/" + fileName + ".csv"));
                String[] nextLine;
                try
                {
                    while ((nextLine = reader.readNext()) != null)
                    {
                        if (nextLine.length != 3)
                        {
                            log.error("Invalid Object row,Few fields are Missing in this row = ObjectName ="
                                    + nextLine[0]
                                    + ", Object type = "
                                    + nextLine[1]
                                    + ", Object Value ="
                                    + nextLine[2]);
                            continue;
                        }
                        else
                        {
                            if (nextLine[1] != null && nextLine[0] != null
                                    && nextLine[2] != null)
                            {
                                if (ObjectType.isDefined(nextLine[1]))
                                {
                                    HashMap<String, String> objMeta = new HashMap<String, String>();
                                    objMeta.put(nextLine[1], nextLine[2]);
                                    objRepo.put(nextLine[0], objMeta);
                                    log.debug("Successfully added Objects - ObjectName ="
                                            + nextLine[0]
                                            + ", Object type = "
                                            + nextLine[1]
                                            + ", Object Value ="
                                            + nextLine[2]);
                                }
                                else if (nextLine[1]
                                        .equalsIgnoreCase("ObjectType")
                                        && nextLine[0]
                                                .equalsIgnoreCase("ObjectName")
                                        && nextLine[2]
                                                .equalsIgnoreCase("ObjectValue"))
                                {
                                    log.debug("IGNORING following row since It is a COLUMN HEADER - ObjectName ="
                                            + nextLine[0]
                                            + ", Object type = "
                                            + nextLine[1]
                                            + ", Object Value ="
                                            + nextLine[2]);
                                }
                                else
                                {
                                    log.error("Invalid Object Type = "
                                            + nextLine[1]
                                            + " ,Hence Rejecting this object row with Object Name ="
                                            + nextLine[0]
                                            + " and Object Value ="
                                            + nextLine[2]);
                                }
                            }
                        }
                    }
                }
                catch (IOException e)
                {
                    log.error("IO exception - Object File in the followig path = "
                            + envProperties.getString("objRepoLocation")
                            + "/"
                            + applicationName + "/"

                            + fileName + ".csv");
                    log.error(e.toString());
                }
            }
            catch (FileNotFoundException e)
            {
                log.error("Object File Not found in the followig path = "
                        + envProperties.getString("objRepoLocation") + "/"
                        + applicationName + "/" + fileName + ".csv");
                log.error(e.toString());
            }

            // Add last key value pair -> FileDetails: (FileName:FilePath)
            HashMap<String, String> objMeta = new HashMap<String, String>();
            objMeta.put(fileName, envProperties.getString("objRepoLocation")
                    + "/" + applicationName + "/" + fileName + ".csv");
            objRepo.put("fileDetails", objMeta);

            return objRepo;
        }
        else
        {
            log.error("INVALID File Name passed as argument "
                    + envProperties.getString("objRepoLocation") + "/"
                    + applicationName + "/" + fileName + ".csv");
            return objRepo;
        }
    }

    /**
     * Method:getObjectValue Description:get Object value from objRepo hashMap
     * 
     * 
     * @param objRepo
     * @param objectName
     * @return
     */
    public static String getObjectValue(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName)
    {
        String objType = null;
        String objValue = null;
        String fileNameNull = null;
        if (objectRepo != null)
        {
            if (objectName != null)
            {
                try
                {
                    Set<String> keys = objectRepo.get("fileDetails").keySet();
                    for (String key : keys)
                    {
                        fileNameNull = key;
                    }
                    objectRepo.get(objectName).isEmpty();
                }
                catch (NullPointerException e)
                {
                    log.error("Object Name "
                            + objectName
                            + " NOT found OR Configuration failure happened in the repo = "
                            + fileNameNull);
                    throw new RuntimeException(
                            "[ERROR] Object Name "
                                    + objectName
                                    + " NOT found OR Configuration failure happened in the repo = "
                                    + fileNameNull);
                }
                if (objectRepo.get(objectName).isEmpty())
                {
                    Set<String> keys = objectRepo.get("fileDetails").keySet();
                    String fileName = null;
                    String filePath = null;
                    for (String key : keys)
                    {
                        fileName = key;
                        filePath = objectRepo.get("fileDetails").get(fileName);
                    }
                    log.error("Object Name "
                            + objectName
                            + " NOT found OR Configuration failure happened in the repo = "
                            + fileName + " present in following path - "
                            + filePath);
                    throw new RuntimeException(
                            "[ERROR] Object Name "
                                    + objectName
                                    + " NOT found OR Configuration failure happened in the repo = "
                                    + fileName
                                    + " present in following path - "
                                    + filePath);
                }
                else
                {
                    Set<String> keys = objectRepo.get(objectName).keySet();
                    for (String key : keys)
                    {
                        objType = key;
                        objValue = objectRepo.get(objectName).get(objType);
                        log.debug("Returning Object Value = " + objValue);
                    }
                    return objValue;
                }
            }
            else
            {
                log.error("Object Name =" + objectName + " is null");
                throw new RuntimeException("[ERROR] Object Name =" + objectName
                        + " is null");
            }
        }
        else
        {
            log.error("Object Repo =" + objectRepo + " is NULL");
            throw new RuntimeException("[ERROR] Object Repo =" + objectRepo
                    + " is NULL");
        }
    }

    /**
     * getModifiedObjectValue Description:get Modified Object value from objRepo
     * hashMap
     * 
     * "$" will be replaced 1. If noOfOccurancesToBeReplaced <= 0 -> replaces
     * ObjValue to valuesToBeReplaced 2. If noOfOccurancesToBeReplaced > 0 ->
     * replaces $ with valuesToBeReplaced
     * 
     * $ -> value which will be replaced & -> this is used as splitter in
     * valuesToBeReplacedString
     * 
     * @param objRepo
     * @param objectName
     * @return
     */
    public static String getModifiedObjectValue(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, int noOfOccurancesToBeReplaced,
            String valuesToBeReplaced)
    {
        String ObjectValue = getObjectValue(objectRepo, objectName);

        String modifiedObjectValue = ObjectValue;

        if (modifiedObjectValue != null)
        {
            if (valuesToBeReplaced != null)
            {
                String[] values = valuesToBeReplaced.split("&", -1);
                if (noOfOccurancesToBeReplaced <= 0)
                {
                    return valuesToBeReplaced;
                }
                else
                {
                    if (values.length == noOfOccurancesToBeReplaced)
                    {
                        for (int i = 0; i < noOfOccurancesToBeReplaced; i++)
                        {
                            modifiedObjectValue = modifiedObjectValue
                                    .replaceFirst("\\$", values[i]);
                        }
                        log.debug("Successfuly returning modified Object Value = "
                                + modifiedObjectValue);
                        return modifiedObjectValue;
                    }
                    else
                    {
                        log.error("getModifiedObjectValue lenght of valuesToBeReplaced ["
                                + values.length
                                + "] is NOT equal to noOfOccurancesToBeReplaced ["
                                + noOfOccurancesToBeReplaced + "]");
                        throw new RuntimeException(
                                "[ERROR] getModifiedObjectValue lenght of valuesToBeReplaced ["
                                        + values.length
                                        + "] is NOT equal to noOfOccurancesToBeReplaced ["
                                        + noOfOccurancesToBeReplaced + "]");

                    }
                }
            }
            else
            {
                log.error("ValuesToBeReplaced Argument =" + ObjectValue
                        + " is null");
                throw new RuntimeException(
                        "[ERROR] ValuesToBeReplaced Argument =" + ObjectValue
                                + " is null");
            }
        }
        else
        {
            log.error("Object value =" + ObjectValue + " is null");
            throw new RuntimeException("[ERROR] Object value =" + ObjectValue
                    + " is null");
        }
    }

    /**
     * Method:getObjectType Description:get Object type from objRepo hashMap
     * 
     * 
     * @param objRepo
     * @param objectName
     * @return
     */
    public static String getObjectType(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName)
    {
        String objType = null;
        String fileNameNull = null;
        if (objectRepo != null)
        {
            if (objectName != null)
            {
                try
                {
                    Set<String> keys = objectRepo.get("fileDetails").keySet();
                    for (String key : keys)
                    {
                        fileNameNull = key;
                    }
                    objectRepo.get(objectName).isEmpty();
                }
                catch (NullPointerException e)
                {
                    log.error("Object Name "
                            + objectName
                            + " NOT found OR Configuration failure happened in the repo = "
                            + fileNameNull);
                    throw new RuntimeException(
                            "[ERROR] Object Name "
                                    + objectName
                                    + " NOT found OR Configuration failure happened in the repo = "
                                    + fileNameNull);
                }
                if (objectRepo.get(objectName).isEmpty())
                {
                    Set<String> keys = objectRepo.get("fileDetails").keySet();
                    String fileName = null;
                    String filePath = null;
                    for (String key : keys)
                    {
                        fileName = key;
                        filePath = objectRepo.get("fileDetails").get(fileName);
                    }
                    log.error("Object Name "
                            + objectName
                            + " NOT found OR Configuration failure happened in the repo = "
                            + fileName + " present in following path - "
                            + filePath);
                    throw new RuntimeException(
                            "[ERROR] Object Name "
                                    + objectName
                                    + " NOT found OR Configuration failure happened in the repo = "
                                    + fileName
                                    + " present in following path - "
                                    + filePath);
                }
                else
                {
                    Set<String> keys = objectRepo.get(objectName).keySet();
                    for (String key : keys)
                    {
                        objType = key;
                        log.debug("Returning Object Type = " + objType);
                    }
                    return objType;
                }
            }
            else
            {
                log.error("Object Name =" + objectName + " is null");
                throw new RuntimeException("[ERROR] Object Name =" + objectName
                        + " is null");
            }
        }
        else
        {
            log.error("Object Repo =" + objectRepo + " is NULL");
            throw new RuntimeException("[ERROR] Object Repo =" + objectRepo
                    + " is NULL");
        }
    }

}
