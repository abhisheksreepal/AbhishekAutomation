package com.home.utilities;

import java.awt.Rectangle;
import java.awt.Toolkit;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

public class Sikuli extends LoggerUtility
{

    public Screen screen;

    private PropertiesConfiguration envProperties;

    public Sikuli()
    {
        super();
        this.screen = new Screen();
        try
        {
            this.envProperties = new PropertiesConfiguration(
                    "environment.properties");
        }
        catch (ConfigurationException e)
        {
            log.fatal("Environment Property Configuration Error");
            e.printStackTrace();
        }
    }

    /**
     * Method:getStandardWaitTime Description:it returns Default TIME OUT set in
     * env property file
     * 
     * @return
     */
    public String getStandardWaitTime()
    {
        return envProperties.getString("STANDARD_PAGE_LOAD_WAIT_TIME");
    }

    public boolean click(String imageName, String ObjRepoName)
    {
        try
        {
            screen.exists(
                    envProperties.getString("sikuliObjectRepository.PATH")
                            + "/" + ObjRepoName + "/" + imageName + ".JPG",
                    Double.parseDouble(getStandardWaitTime()));
            screen.click(envProperties.getString("sikuliObjectRepository.PATH")
                    + "/" + ObjRepoName + "/" + imageName + ".JPG",
                    Integer.parseInt(getStandardWaitTime()));
        }
        catch (FindFailed ff)
        {
            return false;
        }
        return true;
    }

    public boolean type(String imageName, String ObjRepoName, String string)
    {

        screen.exists(envProperties.getString("sikuliObjectRepository.PATH")
                + "/" + ObjRepoName + "/" + imageName + ".JPG",
                Double.parseDouble(getStandardWaitTime()));
        screen.type(envProperties.getString("sikuliObjectRepository.PATH")
                + "/" + ObjRepoName + "/" + imageName + ".JPG", string);
        return true;

    }

    public boolean clickChildInsideParent(String childImageName,
            String parentImageName, String ObjRepoName)
    {

        Pattern parentImagePattern = new Pattern(
                envProperties.getString("sikuliObjectRepository.PATH") + "/"
                        + ObjRepoName + "/" + parentImageName + ".JPG");
        Pattern childImagePattern = new Pattern(
                envProperties.getString("sikuliObjectRepository.PATH") + "/"
                        + ObjRepoName + "/" + childImageName + ".JPG");

        Rectangle screenResolution = new Rectangle(Toolkit.getDefaultToolkit()
                .getScreenSize());
        Region testRegion = Region.create(screenResolution);
        for (int i = 0; i <= Integer.parseInt(getStandardWaitTime()); i++)
        {
            try
            {
                testRegion = testRegion.find(parentImagePattern
                        .similar((float) (0.9)));
                log.info("Successfully found parent image = " + parentImageName);
                break;
            }
            catch (FindFailed e)
            {
                log.info("Waiting for Image - " + parentImageName);
                if (i <= Integer.parseInt(getStandardWaitTime()))
                {
                    continue;
                }
                else
                {
                    log.error("Image Not found" + parentImageName);
                    return false;
                }
            }
        }
        for (int i = 0; i <= Integer.parseInt(getStandardWaitTime()); i++)
        {
            try
            {
                testRegion.inside().click(
                        childImagePattern.similar((float) 0.9));
                log.info("Successfully Clicked child image = " + childImageName);
                break;
            }
            catch (FindFailed e)
            {
                log.info("Waiting for Image - " + childImageName);
                if (i <= Integer.parseInt(getStandardWaitTime()))
                {
                    continue;
                }
                else
                {
                    log.error("Image Not found" + childImageName);
                    return false;
                }
            }
        }
        return true;

    }

    public boolean clickChildLeftOfParent(String childImageName,
            String parentImageName, String ObjRepoName)
    {

        Pattern parentImagePattern = new Pattern(
                envProperties.getString("sikuliObjectRepository.PATH") + "/"
                        + ObjRepoName + "/" + parentImageName + ".JPG");
        Pattern childImagePattern = new Pattern(
                envProperties.getString("sikuliObjectRepository.PATH") + "/"
                        + ObjRepoName + "/" + childImageName + ".JPG");

        Rectangle screenResolution = new Rectangle(Toolkit.getDefaultToolkit()
                .getScreenSize());
        Region testRegion = Region.create(screenResolution);
        for (int i = 0; i <= Integer.parseInt(getStandardWaitTime()); i++)
        {
            try
            {
                testRegion = testRegion.find(parentImagePattern
                        .similar((float) (0.9)));
                log.info("Successfully found parent image = " + parentImageName);
                break;
            }
            catch (FindFailed e)
            {
                log.info("Waiting for Image - " + parentImageName);
                if (i <= Integer.parseInt(getStandardWaitTime()))
                {
                    continue;
                }
                else
                {
                    log.error("Image Not found" + parentImageName);
                    return false;
                }
            }
        }
        for (int i = 0; i <= Integer.parseInt(getStandardWaitTime()); i++)
        {
            try
            {
                testRegion.left().click(childImagePattern.similar((float) 0.8));
                log.info("Successfully Clicked child image = " + childImageName);
                break;
            }
            catch (FindFailed e)
            {
                log.info("Waiting for Image - " + childImageName);
                if (i <= Integer.parseInt(getStandardWaitTime()))
                {
                    continue;
                }
                else
                {
                    log.error("Image Not found" + childImageName);
                    return false;
                }
            }
        }
        return true;

    }

    public boolean clickChildNearByLeftOfParent(String childImageName,
            String parentImageName, String ObjRepoName, int pixelValueToLeft)
    {

        Pattern parentImagePattern = new Pattern(
                envProperties.getString("sikuliObjectRepository.PATH") + "/"
                        + ObjRepoName + "/" + parentImageName + ".JPG");
        Pattern childImagePattern = new Pattern(
                envProperties.getString("sikuliObjectRepository.PATH") + "/"
                        + ObjRepoName + "/" + childImageName + ".JPG");

        Rectangle screenResolution = new Rectangle(Toolkit.getDefaultToolkit()
                .getScreenSize());
        Region testRegion = Region.create(screenResolution);
        for (int i = 0; i <= Integer.parseInt(getStandardWaitTime()); i++)
        {
            try
            {
                testRegion = testRegion.find(parentImagePattern
                        .similar((float) (0.9)));
                log.info("Successfully found parent image = " + parentImageName);
                break;
            }
            catch (FindFailed e)
            {
                log.info("Waiting for Image - " + parentImageName);
                if (i <= Integer.parseInt(getStandardWaitTime()))
                {
                    continue;
                }
                else
                {
                    log.error("Image Not found" + parentImageName);
                    return false;
                }
            }
        }
        for (int i = 0; i <= Integer.parseInt(getStandardWaitTime()); i++)
        {
            try
            {
                testRegion.nearby(pixelValueToLeft).left()
                        .click(childImagePattern.similar((float) 0.8));
                log.info("Successfully Clicked child image = " + childImageName);
                break;
            }
            catch (FindFailed e)
            {
                log.info("Waiting for Image - " + childImageName);
                if (i <= Integer.parseInt(getStandardWaitTime()))
                {
                    continue;
                }
                else
                {
                    log.error("Image Not found" + childImageName);
                    return false;
                }
            }
        }
        return true;

    }

    public boolean clickImageWithOffset(String ImageName, String ObjRepoName,
            int xOffset, int yOffset)
    {

        boolean imageClicked = false;
        Pattern ImageNamePattern = new Pattern(
                envProperties.getString("sikuliObjectRepository.PATH") + "/"
                        + ObjRepoName + "/" + ImageName + ".JPG");

        Rectangle screenResolution = new Rectangle(Toolkit.getDefaultToolkit()
                .getScreenSize());
        Region testRegion = Region.create(screenResolution);
        for (int i = 0; i <= Integer.parseInt(getStandardWaitTime()); i++)
        {
            try
            {
                testRegion = testRegion.find(ImageNamePattern
                        .similar((float) (0.9)));
                log.info("Successfully found parent image = " + ImageName);
                break;
            }
            catch (FindFailed e)
            {
                log.info("Waiting for Image - " + ImageName);
                if (i <= Integer.parseInt(getStandardWaitTime()))
                {
                    continue;
                }
                else
                {
                    log.error("Image Not found" + ImageName);
                    return false; // Need to return because Parent image not
                                  // found, Hence no use of clicking offset
                }
            }
        }
        for (int i = 0; i <= Integer.parseInt(getStandardWaitTime()); i++)
        {
            try
            {
                screen.click(ImageNamePattern.targetOffset(xOffset, yOffset));
                log.info("Successfully Clicked child image = " + ImageName);
                imageClicked = true;
                break;
            }
            catch (FindFailed e)
            {
                log.error("Image Not found" + ImageName);
            }
        }
        return imageClicked;
    }

    public void waitForImageWithTimeout(String imageName, String ObjRepoName)
    {
        for (int i = 0; i <= Integer.parseInt(getStandardWaitTime()); i++)
        {
            try
            {
                screen.wait(envProperties
                        .getString("sikuliObjectRepository.PATH")
                        + "/"
                        + ObjRepoName + "/" + imageName + ".JPG");
                log.info("Waiting for Image is succesfully done and image found "
                        + imageName);
                return;
            }
            catch (NumberFormatException e)
            {
                log.error("STANDARD_PAGE_LOAD_WAIT_TIME value in environment file is NOT number");
            }
            catch (FindFailed e)
            {
                log.info("Waiting for Image - " + imageName);
                continue;
            }
        }
        log.error("Image -" + imageName + " Not found");
    }

    public void closeBrowser()
    {
        if (clickChildInsideParent("close", "BrowserMaxMinClose",
                "ConfigurationScreenshots"))
        {
            log.info("Successfully closed browser using Sikuli");
        }
        else
        {
            log.error("Not able to close browser using Sikuli");
        }
    }

}
