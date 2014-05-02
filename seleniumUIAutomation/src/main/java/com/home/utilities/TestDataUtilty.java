package com.home.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

public class TestDataUtilty
{

    private static Logger log = Logger.getLogger(TestDataUtilty.class);
    private static PropertiesConfiguration envProperties;
    public static HashMap<String, HashMap<String, HashMap<String, String>>> testCaseToTestDataMap = new HashMap<String, HashMap<String, HashMap<String, String>>>();

    public static HashMap<String, HashMap<String, String>> loginTestData;

    static
    {
        try
        {
            envProperties = new PropertiesConfiguration(
                    "config/environment.properties");

        }
        catch (ConfigurationException e)
        {
            LoggerUtility.logTraceMessage(log, e.toString());
        }
    }

    private static HashMap<String, HashMap<String, String>> loadTestDataFromFile(
            String dataFileName) throws IOException
    {
        HashMap<String, HashMap<String, String>> testData = new HashMap<String, HashMap<String, String>>();

        String filePath = "src/test/resources/resources/data/"
                + envProperties.getString("default") + "/" + dataFileName
                + ".csv";
        File file = new File(filePath);
        BufferedReader bufRdr = new BufferedReader(new FileReader(file));
        String line = null;
        String[] colNames = null;
        int row = 0;
        while ((line = bufRdr.readLine()) != null)
        {
            if (row == 0)
            {
                colNames = line.split(",", -1);
            }
            else
            {
                HashMap<String, String> m2 = new HashMap<String, String>();
                String[] params = line.split(",", -1);
                for (int i = 1; i < params.length; i++)
                {
                    m2.put(colNames[i], params[i]);
                }
                testData.put(params[0], m2);
            }
            row++;
        }
        return testData;
    }

    public static void loadTestDataForAllPagesAndAssignToTestCase()
            throws Exception
    {
        loginTestData = TestDataUtilty.loadTestDataFromFile("loginPageData");

        testCaseToTestDataMap.put("LoginPageTest", loginTestData);
    }

    public static String getTestDataValueForGivenTestCase(
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

    public List<String> fetchTCNamesFromDataFile(String dataFileName)
            throws Exception
    {
        List<String> tcNames = new ArrayList<String>();
        String filePath = "src/test/resources/resources/data/" + dataFileName;

        File file = new File(filePath);
        BufferedReader bufRdr = new BufferedReader(new FileReader(file));
        String line = null;
        int row = 0;
        while ((line = bufRdr.readLine()) != null)
        {
            if (row > 0)
            {
                String[] params = line.split(",", -1);
                tcNames.add(params[0]);
            }

            row++;
        }
        return tcNames;
    }

}
