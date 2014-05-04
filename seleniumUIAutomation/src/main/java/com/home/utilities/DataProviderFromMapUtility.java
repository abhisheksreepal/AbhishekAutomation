package com.home.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.DataProvider;

public class DataProviderFromMapUtility
{

    private static Logger log = Logger
            .getLogger(DataProviderFromMapUtility.class);

    @DataProvider(name = "getDataFromFile")
    public static Iterator<Object[]> initializeDataProvider(String testCaseName,
            String className) 
    {
        
        
        HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> inputTestData = TestDataUtilty.inputTestData;
        List<String> listOfPrimaryKeys = TestDataUtilty.getListOfPrimaryKeysForDuplicateTestCase(inputTestData, testCaseName, className);
        List<Object[]> data = new ArrayList<Object[]>();
        for (String string : listOfPrimaryKeys)
        {
            HashMap<String, HashMap<String, String>> testdata = TestDataUtilty.getTestDataByPassingPrimaryKey(inputTestData, string, className);
            String line =null;
            HashMap<String, String >columnValue  = testdata.get(testCaseName);
            for (String keys : columnValue.keySet())
            {
                if(!keys.equalsIgnoreCase("TestCaseName") && !keys.equalsIgnoreCase("TestCaseID")){
                    line =columnValue.get(keys);
                }
                line+=",";
            }
            data.add(new Object[]{});
        }
        
        return data.iterator();
        
    }


}
