package com.tvokids.locator;

import java.io.File;

public class Common {

	  /**********common credentiald***********/
	  public static String adminUsername = "dev";
	  public static String adminPassword = "dev";
	  public static String contentEditorUsername = "content_editor";
	  public static String contentEditorPassword = "changeme";
	
	  /**********common url*******************/
	  public static String homeURL         = System.getProperty("URL");
	  public static String userLoginPage   = homeURL + "/user"; 
	  public static String adminContentURL = homeURL + "/admin/content";
	  public static String fiveAndUnderURL = homeURL + "/5-and-under";
	  public static String sixAndOvererURL = homeURL + "/6-and-over";
	  
	  public static String localResourceDir  = System.getProperty("user.dir") + File.separator + "resources";
	  public static String localDriversDir   = localResourceDir + File.separator + "drivers" + File.separator;                                                 
	  public static String localImageDir     = localResourceDir + File.separator + "images";                                                                                     
	  public static String localFileDir      = localResourceDir + File.separator + "files";                                                                                      
	  public static String outputFileDir     = System.getProperty("user.dir") + File.separator + "output" + File.separator;
	  public static String testOutputFileDir = System.getProperty("user.dir") + File.separator + "test-output" + File.separator;
	  public static String testIconFileDir   = System.getProperty("user.dir") + File.separator + "icons" + File.separator;
	  
	  public static String notFound          = "//*[contains(text(),'Not Found')]";
	  public static String notFoundError     = "//*[text()='Not Found']";
	  
	  /**********footer last*********************/
	  public static String logout = "//a[text()='Log out']";

	  /**********common web elements*******************/
	  public static String title = "//div[contains(@id,'tile-node')]/h1";
	  public static String ajaxProgressThrobber = "/descendant::div[@class='ajax-progress ajax-progress-throbber']/div[@class='throbber']";	  
	  public static String ajaxThrobber         = "//*[contains(.,'ajax')]/descendant::div[@class='throbber']";
	  public static String throbber             = "/descendant::div[@class='throbber']";
	  public static String autocomplete         = "//*[@id='autocomplete']/ul/li/div/div";
	  public static String autoComplete         = "//*[@id='autocomplete']/ul/li/div";
	  
	  public static String thumbnail        = "/descendant::a[@href]";
	  public static String image            = "/descendant::img[@typeof='foaf:Image']";
	  public static String filterSelected   = "/descendant::option[@selected]";
	  
	  public static String fiveAndUnderLinkBase = "//a[@href='/5-and-under/";
	  public static String sixAndOvererLinkBase = "//a[@href='/6-and-over/";
	  
	  
	  public static String metaTag            = "//meta[@name]";
	  public static String metaTagDescription = "//meta[@name='description']";	  
	  public static String XpathEqualsStart       = "//*[.='";
	  public static String XpathEqualsStartQuot   = "//*[.=\"";	  
	  public static String XpathEqualsTextStart       = "//*[text()='";
	  public static String XpathEqualsTextStartQuot   = "//*[text()=\"";	  
	  public static String XpathContainsStart         = "/descendant::*[contains(.,'";
	  public static String XpathContainsStartQuot     = "/descendant::*[contains(.,\"";	  
	  public static String XpathContainsTextStart     = "/descendant::*[contains(text(),'";
	  public static String XpathContainsTextStartQuot = "/descendant::*[contains(text(),\"";
	  public static String XpathContainsAnyStart      = "/descendant::*[@*[contains(.,'";
	  public static String XpathContainsAnyStartQuot  = "/descendant::*[@*[contains(.,\"";
	  public static String XpathEnd                   = "]";
	  public static String XpathEqualsEnd             = "']";
	  public static String XpathEqualsEndQuot         = "\"]";
	  public static String XpathContainsEnd           = "')]";
	  public static String XpathContainsEndQuot       = "\")]";
	  public static String XpathContainsAnyEnd        = "')]]";
	  public static String XpathContainsAnyEndQuot    = "\")]]";
	  public static String XpathParent                = "/parent::*";
	  public static String XpathGrandParent           = "/parent::*/parent::*";
	  public static String imageParticle = "/img";
	  
	  public static String[] imageArray  = { "1_Vimeo.jpg","2_Layouts.jpg","2_Louis.jpg","2_Metacafe.jpg","2_Phil.png","2_qlcteam.jpg","3_qlcteam.jpg" };
	  public static int[] imageSizeArray = { 580879, 438462, 380586, 482688, 205666, 663898, 663898  };
	
}
