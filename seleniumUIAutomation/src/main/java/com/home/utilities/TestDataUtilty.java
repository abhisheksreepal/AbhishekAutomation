package com.home.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;

public class TestDataUtilty
{

    private static Logger log = Logger.getLogger(TestDataUtilty.class);

    public static LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>>> inputTestData;

    private static LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>>> loadTestDataFromFile(
            String dataFileName, String environment, String applicationName)
            throws IOException
    {
        LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>>> testData = new  LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>>>();
        LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>> primaryKeyMap = new LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>>();
        LinkedHashMap<String, LinkedHashMap<String, String>> testCaseMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();

        String filePath = "src/test/resources/resources/data/"
                + applicationName + "/" + environment + "/" + dataFileName
                + ".csv";
        File file = new File(filePath);
        BufferedReader bufRdr = new BufferedReader(new FileReader(file));
        String line = null;
        String[] colNames = null;

        while ((line = bufRdr.readLine()) != null)
        {
            String[] string = line.split(",", -1);

            if (string[1].trim().equalsIgnoreCase("PrimaryKey"))
            {
                colNames = new String[string.length];
                for (int i = 0; i < string.length; i++)
                {
                    colNames[i] = string[i].trim();
                }
                testCaseMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();
                primaryKeyMap = new LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>>();
                testData.put(
                        string[0],
                        new LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>>());

            }
            else if (!string[1].trim().equalsIgnoreCase("")
                    && !string[1].trim().equalsIgnoreCase("PrimaryKey")
                    && string[0].trim().equalsIgnoreCase(""))
            {
                LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
                testCaseMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();
                for (int i = 2; i < string.length; i++)
                {
                    data.put(colNames[i], string[i]);
                }
                testCaseMap.put(string[2], data);
                primaryKeyMap.put(string[1], testCaseMap);
                testData.put(colNames[0], primaryKeyMap);
            }

        }
        return testData;
    }

    public static void loadTestDataToMemory(String environment,
            String application) throws Exception
    {
        inputTestData = TestDataUtilty.loadTestDataFromFile("inputfileData",
                environment, application);

    }

    public static String getTestDataValueForGivenTestCaseForGiven(
            HashMap<String, HashMap<String, String>> testData, String tcName,
            String colName)
    {
        if (!testData.containsKey(tcName))
        {
            log.error("Test Data-" + testData
                    + " does not contain Test case with Name - " + tcName);
            throw new RuntimeException("Test Data-" + testData
                    + " does not contain Test case with Name - " + tcName);
        }
        else
        {
            if (!testData.get(tcName).containsKey(colName))
            {
                log.error("Test Data-" + testData
                        + " contains Test case with Name - " + tcName
                        + " but NOT contains following column -" + colName);
                throw new RuntimeException("Test Data-" + testData
                        + " contains Test case with Name - " + tcName
                        + " but NOT contains following column -" + colName);
            }
            else
            {
                return testData.get(tcName).get(colName);
            }
        }

    }

    public static List<String> getListOfPrimaryKeysForDuplicateTestCase(
            LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>>> testDatas,
            String testCaseName, String moduleName)
    {

        LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>> testData = testDatas
                .get(moduleName);
        List<String> listOfPrimaryKeys = new ArrayList<String>();
        for (String primaryKeys : testData.keySet())
        {
            if (testData.get(primaryKeys).containsKey(testCaseName))
            {

                listOfPrimaryKeys.add(primaryKeys);
            }
        }
        return listOfPrimaryKeys;
    }

    public static LinkedHashMap<String, LinkedHashMap<String, String>> getTestDataByPassingPrimaryKey(
            LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>>> testDatas,
            String primaryKey, String moduleName)
    {
        LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>> testData = testDatas
                .get(moduleName);
        if (!testData.containsKey(primaryKey))
        {
            log.error("Test Data-" + testData
                    + " does not contain Test case with Name - " + primaryKey);
            throw new RuntimeException("Test Data-" + testData
                    + " does not contain Test case with Name - " + primaryKey);
        }
        else
        {
            return testData.get(primaryKey);

        }
    }

    public static String getClassNameByPassingTestMethod(String testCaseName)
    {
        String className = null;
        for (String cName : TestDataUtilty.inputTestData
                .keySet())
        {
            className = cName;
            for (String  pKeys : inputTestData.get(cName).keySet())
            {
                if(inputTestData.get(cName).get(pKeys).containsKey(testCaseName)){
                    return className;
                }
            }
        }
        return className;
    }
}
