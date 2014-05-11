package com.home.utilities;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.testng.annotations.DataProvider;

public class DataProviderFromMapUtility extends LoggerUtility
{


    @DataProvider(name = "initializeDataProvider")
    public static Object[][] initializeDataProvider(Method testMethod)
    {

        LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>>> inputTestData = TestDataUtilty.inputTestData;
        String testCaseName = testMethod.getName();
        String className = TestDataUtilty
                .getClassNameByPassingTestMethod(testCaseName);

        List<String> listOfPrimaryKeys = TestDataUtilty
                .getListOfPrimaryKeysForDuplicateTestCase(inputTestData,
                        testCaseName, className);
        Object[][] obj = new Object[listOfPrimaryKeys.size()][];
        List<Object[]> data = new ArrayList<Object[]>();

        for (String string : listOfPrimaryKeys)
        {
            LinkedHashMap<String, LinkedHashMap<String, String>> testdata =new TestDataUtilty()
                    .getTestDataByPassingPrimaryKey(inputTestData, string,
                            className);

            LinkedHashMap<String, String> columnValue = testdata
                    .get(testCaseName);
            int col = 0;
            Object[] o = new Object[columnValue.keySet().size() - 2];
            for (String keys : columnValue.keySet())
            {

                if (!keys.equalsIgnoreCase("TestCaseName")
                        && !keys.equalsIgnoreCase("TestCaseID"))
                {
                    o[col] = columnValue.get(keys);
                    col++;
                }

            }
            data.add(o);

        }

        for (int i = 0; i < obj.length; i++)
        {
            obj[i] = data.get(i);
        }

        return obj;

    }

}
