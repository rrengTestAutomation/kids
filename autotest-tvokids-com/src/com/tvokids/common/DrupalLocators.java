package com.tvokids.common;

public class DrupalLocators{

	  /**********common url*******************/
	  public static String userURL = Locators.homeURL + "/user";
	  
	  /**********content type add url*******************/
	  public static String characterBrand = Locators.homeURL + "/node/add/program";
	  public static String customBrand    = Locators.homeURL + "/node/add/custom-brand";
	  public static String playList       = Locators.homeURL + "/node/add/playlist";
	  public static String scheduleItem   = Locators.homeURL + "/node/add/schedule-item";
	  public static String video          = Locators.homeURL + "/node/add/video"; 
	  
	  /**************content admin*******************/
	  public static String adminContentRowFirst    = "/descendant::tr[@class='odd views-row-first views-row-last']";
	  public static String adminContentFieldTitles = "/descendant::td[@class='views-field views-field-title']/a";

	  /************drupal cache*********************/
	  public static String flushAllCachesButton     = "//a[@class='admin-menu-destination'][(.='Flush all caches')]";
	  public static String messageEveryCacheCleared = "//div[@class='messages status'][contains(.,'Every cache cleared.')]";
	  
	  /**********common buttons*******************/
      public static String featureTypeInDropDown     = "//*[@value='feature']";
      public static String deleteOperationInDropDown = "//*[@id='edit-operation']/option[text()='Delete']";
      public static String confirmButton             = "//*[@id='edit-submit']";
      public static String errorConsole              = "//div[@id='console']";
      public static String error                     = "//*[@id='console']/div[@class='messages error']";
      public static String errorMessage              = "//*[@id='console']/div[@class='messages error']/descendant::*[contains(text(),'Error message')]";
      public static String errorAjax                 = "//div[@class='messages error'][contains(text(), 'An AJAX HTTP error occurred')]";
      public static String errorDescription          = "//*[@id='console']/div[@class='messages error'][contains(.,'Description field is required')]";
      public static String logout                    = "//a[text()='Log out']";
	  public static String drupalHomeIcon            = "//*[@id='admin-menu-icon']/descendant::*[text()='Home']";  // used to be: "//*[@id='admin-menu-icon']";		
	  public static String drupalEditButton          = "//*[@id='tabs']/descendant::a[text()='Edit']";
	  public static String statusPerformedDelete  = "//*[@id='console']/descendant::*[contains(text(),'Status message')]/following-sibling::em[contains(text(),'Delete')]";
	  public static String executeButton             = "//*[@id='edit-submit--2']";
      public static String selectAllCheckBox         = "//*[@class='views-table sticky-enabled cols-7 tableheader-processed sticky-table']/descendant::input[@class='vbo-table-select-all form-checkbox']";
      public static String selectAllRowsButton       = "//*[@id='views-form-admin-views-node-system-1']/descendant::*[contains(@value,'Select all') and contains(@value,'rows in this view')]";

      /************common once*********************/
	  public static String title   = "edit-title";
	  public static String titleRemainCharsNumber = "//div[@id='edit-title-counter']/strong";
	  public static int titleMaxCharsNumber       = 26;
	  	  
	  public static String description = "//*[contains(@id,'-description-und-0-value')]";	  
	  
	  public static String ageGroup5   = "edit-field-age-group-und-1";
	  public static String ageGroup6   = "edit-field-age-group-und-2";
	  public static String keywords    = "edit-field-keywords-und";

	  public static String browse = "/descendant::*[contains(@id, '-und-0-upload')][@class='form-file']";
	  public static String upload = "/descendant::*[contains(@id, '-und-0-upload')][contains(@class,'form-submit')]";	  
	  public static String subSet1 = "[1]";
	  public static String subSet2 = "[2]";
	  
	  public static String progress               = "updateprogress";
	  public static String submit                 = "edit-submit";
	  public static String statusPerformedMessage = "//*[@id='console']/descendant::*[contains(.,'on ') and contains(.,' item')]";	  
	  public static String statusPerformedCancel  = "//*[@id='console']/descendant::*[contains(text(),'Status message')]/following-sibling::em[contains(text(),'Cancel')]";
	  public static String longDescription        = "edit-field-description-und-0-value";
	  public static String shortDescription       = "edit-field-summary-und-0-value";
	  
	  /************vertical tab - character banner*********************/
	  public static String characterBannerVerticalTab = "//ul[@class='vertical-tabs-list']/li[1]/a/strong";
	  public static String characterBannerFieldSet = "//fieldset[1]/div/div";
	  public static String characterBannerBrowse = characterBannerFieldSet + browse;
	  public static String characterBannerUpload = characterBannerFieldSet + upload;
	  			
	  /************vertical tab - hero box*********************/
	  public static String heroBoxVerticalTab = "//ul[@class='vertical-tabs-list']/li[2]/a/strong";
	  public static String heroBoxFieldSet    = "//fieldset[2]/div/div";
	  public static String heroBoxBrowse      = heroBoxFieldSet + browse;
	  public static String heroBoxUpload      = heroBoxFieldSet + upload;
	  		    
	  /************vertical tab - tile*********************/
	  public static String tileVerticalTab  = "//ul[@class='vertical-tabs-list']/li[3]/a/strong";
	  public static String tileFieldSet     = "//fieldset[3]/div/div";
	  public static String tileSmallBrowse  = tileFieldSet + subSet1 + browse;
	  public static String tileSmallUpload  = tileFieldSet + subSet1 + upload;
	  public static String tileLargeBrowse = tileFieldSet + subSet2 +  browse;
	  public static String tileLargeUpload  = tileFieldSet + subSet2 +  upload;

	  /************content******************/
	  public static String brandPageDescriptionID                 =     "edit-field-brand-page-description-und-0-value";	  
	  public static String brandPageDescriptionContentLimitMsgCSS = "div#edit-field-brand-page-description-und-0-value-counter.counter";
	  public static String brandPageDescriptionCounterCSS         = "div#edit-field-brand-page-description-und-0-value-counter.counter strong";
	  public static    int descriptionMaxCharsNumber              = 135;
	  
	  /************meta*********************/
	  public static String metaTagsTab            = "//a/strong[text()='Meta tags']";
	  public static String metaTagDescription     = "edit-metatags-und-description-value";
	  	  
}