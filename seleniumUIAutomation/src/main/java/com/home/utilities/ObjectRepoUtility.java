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

    public ObjectRepoUtility(Logger log)
    {
        super(log);

    }

    public ObjectRepoUtility()
    {
        super();

    }

    public static HashMap<String, HashMap<String, String>> loginObjRepo;
    public static HashMap<String, HashMap<String, String>> homePageObjRepo;
    public static HashMap<String, HashMap<String, String>> logoutPageObjRepo;

    public void loadObjectRepoForAllPages()
    {
        loginObjRepo = fetchObjectFromNewFileFormat("loginPage");
        homePageObjRepo = fetchObjectFromNewFileFormat("homePage");
        logoutPageObjRepo = fetchObjectFromNewFileFormat("logoutPage");
    }

    public enum ObjectType
    {

        XPATH, ID, NAME, CLASS, CSS, LINK_TEXT, PARTIAL_LINK_TEXT, TAG_NAME;

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
     * /** Method:getObjectValue Description:get Object value from objRepo
     * hashMap
     * 
     * 
     * @param objRepo
     * @param objectName
     * @return
     */
    public String getObjectValue(
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
                    new LoggerUtility().log
                            .error("Object Name "
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
                    new LoggerUtility().log
                            .error("Object Name "
                                    + objectName
                                    + " NOT found OR Configuration failure happened in the repo = "
                                    + fileName
                                    + " present in following path - "
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
                        new LoggerUtility().log
                                .debug("Returning Object Value = " + objValue);
                    }
                    return objValue;
                }
            }
            else
            {
                new LoggerUtility().log.error("Object Name =" + objectName
                        + " is null");
                throw new RuntimeException("[ERROR] Object Name =" + objectName
                        + " is null");
            }
        }
        else
        {
            new LoggerUtility().log.error("Object Repo =" + objectRepo
                    + " is NULL");
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
    public String getModifiedObjectValue(
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
                        new LoggerUtility().log
                                .debug("Successfuly returning modified Object Value = "
                                        + modifiedObjectValue);
                        return modifiedObjectValue;
                    }
                    else
                    {
                        new LoggerUtility().log
                                .error("getModifiedObjectValue lenght of valuesToBeReplaced ["
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
                new LoggerUtility().log.error("ValuesToBeReplaced Argument ="
                        + ObjectValue + " is null");
                throw new RuntimeException(
                        "[ERROR] ValuesToBeReplaced Argument =" + ObjectValue
                                + " is null");
            }
        }
        else
        {
            new LoggerUtility().log.error("Object value =" + ObjectValue
                    + " is null");
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
    public String getObjectType(
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
                    new LoggerUtility().log
                            .error("Object Name "
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
                    new LoggerUtility().log
                            .error("Object Name "
                                    + objectName
                                    + " NOT found OR Configuration failure happened in the repo = "
                                    + fileName
                                    + " present in following path - "
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
                        new LoggerUtility().log
                                .debug("Returning Object Type = " + objType);
                    }
                    return objType;
                }
            }
            else
            {
                new LoggerUtility().log.error("Object Name =" + objectName
                        + " is null");
                throw new RuntimeException("[ERROR] Object Name =" + objectName
                        + " is null");
            }
        }
        else
        {
            new LoggerUtility().log.error("Object Repo =" + objectRepo
                    + " is NULL");
            throw new RuntimeException("[ERROR] Object Repo =" + objectRepo
                    + " is NULL");
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
    private HashMap<String, HashMap<String, String>> fetchObjectFromNewFileFormat(
            String fileName)
    {

        String applicationName = envProperties.getString("application");
        String objRepoLocation = envProperties.getString("objRepoLocation");
        String objFilePath = objRepoLocation + "/" + applicationName + "/"
                + "seleniumObjectRepo" + "/" + fileName + ".csv";

        HashMap<String, HashMap<String, String>> objRepo = new HashMap<String, HashMap<String, String>>();
        if (fileName != null)
        {
            CSVReader reader;
            try
            {
                reader = new CSVReader(new FileReader(objFilePath));
                String[] nextLine;
                int row = 0;
                String[] columns =
                { "ObjectName", "ID", "NAME", "CLASS", "LINK_TEXT",
                        "PARTIAL_LINK_TEXT", "TAG_NAME", "CSS", "XPATH" };
                try
                {
                    while ((nextLine = reader.readNext()) != null)
                    {
                        if (row == 0)
                        {

                            for (int i = 1; i < nextLine.length; i++)
                            {
                                if (!ObjectType.isDefined(nextLine[i]))
                                {
                                    new LoggerUtility().log
                                            .error("Invalid Object Type defined-["
                                                    + nextLine[i] + "]");
                                }

                            }

                            ++row;
                        }
                        else
                        {
                            int position = getObjectPosition(nextLine);
                            if (position != 0)
                            {
                                HashMap<String, String> objMeta = new HashMap<String, String>();
                                objMeta.put(columns[position],
                                        getObjectValue(position, nextLine));
                                objRepo.put(nextLine[0], objMeta);
                                new LoggerUtility().log
                                        .debug("Successfully added Objects - ObjectName ="
                                                + nextLine[0]
                                                + ", Object type = "
                                                + columns[position]
                                                + ", Object Value ="
                                                + getObjectValue(position,
                                                        nextLine));
                            }
                            else
                            {
                                new LoggerUtility().log
                                        .debug("[Not Added to ObjRepo]No Value defined for Objects - ObjectName ="
                                                + nextLine[0]
                                                + ", Object type = "
                                                + columns[position]
                                                + ", Object Value ="
                                                + getObjectValue(position,
                                                        nextLine));
                            }

                        }
                    }

                    reader.close();
                }
                catch (IOException e)
                {
                    new LoggerUtility().log
                            .error("IO exception - Object File in the followig path = "
                                    + objFilePath);
                    new LoggerUtility().log.error(e.toString());
                }
            }
            catch (FileNotFoundException e)
            {
                new LoggerUtility().log
                        .error("Object File Not found in the followig path = "
                                + objFilePath);
                new LoggerUtility().log.error(e.toString());
            }

            // Add last key value pair -> FileDetails: (FileName:FilePath)
            HashMap<String, String> objMeta = new HashMap<String, String>();
            objMeta.put("fileName", objFilePath);
            objRepo.put("fileDetails", objMeta);

            return objRepo;
        }
        else
        {
            new LoggerUtility().log
                    .error("INVALID File Name passed as argument "
                            + objFilePath);
            return objRepo;
        }
    }

    private int getObjectPosition(String[] nextLine)
    {
        int position = 0;

        for (int i = 1; i < nextLine.length; i++)
        {
            if (!nextLine[i].trim().equalsIgnoreCase(""))
            {
                return i;
            }
        }

        return position;
    }

    private String getObjectValue(int position, String[] nextLine)
    {

        return nextLine[position];
    }

}
