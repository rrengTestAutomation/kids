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
	  public static String sixAndOverURL   = homeURL + "/6-and-over";

	  public static String fiveAndUnderSearchURL = fiveAndUnderURL + "/search";
	  public static String sixAndOverSearchURL   = sixAndOverURL   + "/search";
	  
	  public static String localResourceDir  = System.getProperty("user.dir") + File.separator + "resources";
	  public static String localDriversDir   = localResourceDir + File.separator + "drivers" + File.separator;                                                 
	  public static String localImageDir     = localResourceDir + File.separator + "images";                                                                                     
	  public static String localFileDir      = localResourceDir + File.separator + "files";                                                                                      
	  public static String outputFileDir     = System.getProperty("user.dir") + File.separator + "output" + File.separator;
	  public static String testOutputFileDir = System.getProperty("user.dir") + File.separator + "test-output" + File.separator;
	  public static String testIconFileDir   = System.getProperty("user.dir") + File.separator + "icons" + File.separator;
	  
	  public static String notFound          = "//*[contains(text(),'Not Found')]";
	  public static String notFoundError     = "//*[text()='Not Found']";
	  
	  /**********home page elements*******************/
	  public static String homePageLogo              = "//div[@class='splash-logo']/img[@alt='TVOKids']";
	  public static String kidsPageLogo              = "//a[@id='logo-link']";
	  public static String kidsPageLogoCanvas        = kidsPageLogo + "/canvas[@id='canvas']";
	  public static String kidsPageLogoImage         = kidsPageLogo + "/img";
	  public static String kidsPageLogoStatic        = kidsPageLogoCanvas + "[@class='animated-logo']";
	  public static String kidsPageLogoAnimated      = kidsPageLogoCanvas + "[@class='animated-logo animating']";
      public static String homePageFiveAndUnderBlock = "//a[@class='splash-under-5']";
	  public static String homePageFiveAndUnderTitle = homePageFiveAndUnderBlock + "/div[@class='splash-text']";
	  public static String homePageFiveAndUnderImage = homePageFiveAndUnderBlock + "/picture/img";
	  public static String homePageSixAndOverBlock   = "//a[@class='splash-over-6']";
	  public static String homePageSixAndOverTitle   = homePageSixAndOverBlock + "/div[@class='splash-text']";
	  public static String homePageSixAndOverImage   = homePageSixAndOverBlock + "/picture/img";
	  
	  /**********age pages elements*******************/
	  public static String charBannerButtonLeft  = "//button[@class='character-banner-button-prev']";
	  public static String charBannerButtonRight = "//button[@class='character-banner-button-next']";
	  public static String charFiveAndUnderBase  = "//a[@href='/5-and-under/";
	  public static String charSixAndOverBase    = "//a[@href='/6-and-over/";
	  public static String searchIcon            = "//a[@class='search-link']";
	  
	  /**********character page elements*********/
	  public static String characterTitle = "//div[@class='tile-bubble']/following-sibling::*/h1";
	  
	  /**********footer last*********************/
	  public static String logout = "//a[text()='Log out']";

	  /**********common web elements*******************/	  
	  public static String title = "//div[@class='tile-title']/h1"; // USED TO BE: "//div[contains(@id,'tile-node')]/h1;"
	  public static String ajaxProgressThrobber = "/descendant::div[@class='ajax-progress ajax-progress-throbber']/div[@class='throbber']";	  
	  public static String ajaxThrobber         = "//*[contains(.,'ajax')]/descendant::div[@class='throbber']";
	  public static String throbber             = "/descendant::div[@class='throbber']";
	  public static String autocomplete         = "//*[@id='autocomplete']/ul/li/div/div";
	  public static String autoComplete         = "//*[@id='autocomplete']/ul/li/div";
	  
	  public static String thumbnail        = "/descendant::a[@href]";
	  public static String image            = "/descendant::img[@typeof='foaf:Image']";
	  public static String filterSelected   = "/descendant::option[@selected]";
	  
	  public static String fiveAndUnderLinkBase = "//a[@href='/5-and-under/";
	  public static String sixAndOverLinkBase = "//a[@href='/6-and-over/";
	  
	  
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
