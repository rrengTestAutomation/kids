package com.tvokids.locator;

public class Drupal{

	  /**********common url*******************/
	  public static String userURL = Common.homeURL + "/user";
	  
	  /**********content type add url*******************/
	  public static String characterBrand = Common.homeURL + "/node/add/program";
	  public static String customBrand    = Common.homeURL + "/node/add/custom-brand";
	  public static String playList       = Common.homeURL + "/node/add/playlist";
	  public static String scheduleItem   = Common.homeURL + "/node/add/schedule-item";
	  public static String video          = Common.homeURL + "/node/add/video"; 
	  
	  /**************content admin*******************/
	  public static String adminContentRowFirst    = "/descendant::tr[@class='odd views-row-first views-row-last']";
	  public static String adminContentFieldTitles = "/descendant::td[@class='views-field views-field-title']/a";

	  /************drupal cache*********************/
	  public static String flushAllCachesButton     = "//a[@class='admin-menu-destination'][(.='Flush all caches')]";
	  public static String messageEveryCacheCleared = "//div[@class='messages status'][contains(.,'Every cache cleared.')]";
	  
	  /**********common buttons*******************/
      public static String featureTypeInDropDown                = "//*[@value='feature']";
      public static String deleteOperationInDropDown            = "//*[@id='edit-operation']/option[text()='Delete']";
      public static String confirmButton                        = "//*[@id='edit-submit']";
      public static String errorConsole                         = "//div[@id='console']";
      public static String error                                = "//*[@id='console']/div[@class='messages error']";
      public static String errorMessage                         = "//*[@id='console']/div[@class='messages error']/descendant::*[contains(text(),'Error message')]";
      
      public static String errorMessageCharacterBannerWrongUploadSize(String image, String size) { return "Error message\nThe specified file " + image + " could not be uploaded. The file is " + size + " KB exceeding the maximum file size of 25 KB."; }
      public static String errorMessageCharacterBannerWrongImageFormat(String image) { return "The selected file " + image + " cannot be uploaded. Only files with the following extensions are allowed: jpg."; }
      public static String errorMessageCharacterBannerWrongImagePixels(String image) { return "Error message\nThe specified file " + image + " could not be uploaded. The image is too small; the minimum dimensions are 200x200 pixels."; }
      
      public static String errorMessageHeroBoxWrongImagePixels(String image) { return "Error message\nThe specified file " + image + " could not be uploaded. The image is too small; the minimum dimensions are 708x398 pixels."; }
      public static String errorMessageHeroBoxWrongUploadSize   (String image, String size) { return "Error message\nThe specified file " + image + " could not be uploaded. The file is " + size + " KB exceeding the maximum file size of 75 KB."; }
      public static String errorMessageHeroBoxWrongImageFormat  (String image) { return "The selected file " + image + " cannot be uploaded. Only files with the following extensions are allowed: png, jpg, jpeg."; }     
      
      public static String errorMessageSmallTileImageRequired   = "Small Tile Image field is required."; 
      public static String errorMessageSmallTileWrongImagePixels(String image) { return "Error message\nThe specified file " + image + " could not be uploaded. The image is too small; the minimum dimensions are 708x398 pixels."; }
      public static String errorMessageSmallTileWrongUploadSize (String image, String size) { return "Error message\nThe specified file " + image + " could not be uploaded. The file is " + size + " KB exceeding the maximum file size of 75 KB."; }
      public static String errorMessageSmallTileWrongImageFormat(String image) { return "The selected file " + image + " cannot be uploaded. Only files with the following extensions are allowed: png, jpg, jpeg."; }
 
      public static String errorMessageLargeTileWrongImagePixels(String image) { return "Error message\nThe specified file " + image + " could not be uploaded. The image is too small; the minimum dimensions are 708x836 pixels."; }
      public static String errorMessageLargeTileWrongUploadSize (String image, String size) { return "Error message\nThe specified file " + image + " could not be uploaded. The file is " + size + " KB exceeding the maximum file size of 100 KB."; }
      public static String errorMessageLargeTileWrongImageFormat(String image) { return "The selected file " + image + " cannot be uploaded. Only files with the following extensions are allowed: png, jpg, jpeg."; }
      
      public static String errorAjax                            = "//div[@class='messages error'][contains(text(), 'An AJAX HTTP error occurred')]";
      public static String errorMessageTitleRequired            = "Brand Page Title field is required.";
      public static String errorTitle                           = "//*[@id='console']/div[@class='messages error'][contains(.,'" + errorMessageTitleRequired + "')]";
      public static String errorMessageDescriptionRequired      = "Brand Page Description field is required.";
      public static String errorDescription                     = "//*[@id='console']/div[@class='messages error'][contains(.,'" + errorMessageDescriptionRequired + "')]";    
      public static String errorBrowse                          = "//div[contains(@class,'messages error')][contains(.,'The') and contains(.,'file')]";
      public static String errorActual                          = "/descendant::em[1]";
      public static String errorExpected                        = "/descendant::em[2]";
      public static String errorSize                            = "/descendant::em[3]";
      public static String errorUpload                          = "//div[contains(@class,'messages error')][contains(.,'could not be uploaded') or contains(.,'cannot be uploaded')]";  // used to be: "//div[@class='messages error'][contains(.,'could not be uploaded')]";
      public static String logout                               = "//a[text()='Log out']";
	  public static String drupalHomeIcon                       = "//*[@id='admin-menu-icon']/descendant::*[text()='Home']";  // used to be: "//*[@id='admin-menu-icon']";		
	  public static String drupalEditButton                     = "//*[@id='tabs']/descendant::a[text()='Edit']";
	  public static String statusPerformedDelete                = "//*[@id='console']/descendant::*[contains(text(),'Status message')]/following-sibling::em[contains(text(),'Delete')]";
	  public static String statusPerformedSend                = "//*[@id='console']/descendant::*[contains(text(),'Status message')]/following-sibling::em[contains(text(),'Send')]";
	  public static String executeButton                        = "//*[@id='edit-submit--2']";
      public static String selectAllCheckBox                    = "(/descendant::input[@class='vbo-table-select-all form-checkbox'])[2]";
                                                                   // used to be:  "//*[@class='views-table sticky-enabled cols-7 tableheader-processed sticky-table']/descendant::input[@class='vbo-table-select-all form-checkbox']";
      public static String selectAllRowsButton                  = "//*[@id='views-form-admin-views-node-system-1']/descendant::*[contains(@value,'Select all') and contains(@value,'rows in this view')]";
      public static String messageNoContentAvailable            = "//*[contains(text(),'No content available.')]";
                                                                   // used to be: "//*[@id='views-form-admin-views-node-system-1']//*[contains(text(),'No content available.')]";
      
      /************common once*********************/
	  public static String title   = "edit-title";
	  public static String titleRemainCharsNumber = "//div[@id='edit-title-counter']/strong";
	  public static int titleMaxCharsNumber       = 26;
	  	  
	  public static String description    = "//*[contains(@id,'-description-und-0-value')]";
	  public static String alternateSmall = "//*[@id='edit-field-small-tile-image-und-0-alt']";
	  public static String alternateLarge = "//*[@id='edit-field-large-tile-image-und-0-alt']";
	  public static String alternateSmallText  = "This is an Alternate Text of Small Tile";
	  public static String alternateLargeText  = "This is an Alternate Text of Large Tile";
	  
	  public static String ageGroup5   = "edit-field-age-group-und-1";
	  public static String ageGroup6   = "edit-field-age-group-und-2";
	  public static String keywords    = "edit-field-keywords-und";

	  public static String browse  = "/descendant::*[contains(@id, '-und-0-upload')][@class='form-file']";
	  public static String upload  = "/descendant::*[contains(@id, '-und-0-upload')][contains(@class,'form-submit')]";
	  public static String remove  = "/descendant::*[contains(@id, '-und-0-remove-button')][contains(@class,'form-submit')]";
	  public static String subSet1 = "[1]";
	  public static String subSet2 = "[2]";
	  
	  public static String progress               = "updateprogress";
	  public static String tilePlacementSelection = "edit-tvokids-content-tile-fieldset-0-landing-page";
	  public static String tilePlacementPublished = "edit-tvokids-content-tile-fieldset-0-published";
	  public static String tilePlacementAdd       = "edit-tvokids-content-tile-fieldset-add"; 
	  public static String submit                 = "edit-submit";
	  public static String statusPerformedMessage = "//*[@id='console']/descendant::*[contains(.,'on ') and contains(.,' item')]";	  
	  public static String statusPerformedCancel  = "//*[@id='console']/descendant::*[contains(text(),'Status message')]/following-sibling::em[contains(text(),'Cancel')]";
	  public static String longDescription        = "edit-field-description-und-0-value";
	  public static String shortDescription       = "edit-field-summary-und-0-value";
	  
	  /************************active tab******************************/
	  public static String verticalTabActive = "/parent::*/span[@id='active-vertical-tab']";
	  
	  /************vertical tab - character banner*********************/
	  public static String characterBannerVerticalTab = "//ul[@class='vertical-tabs-list']/li[1]/a/strong";
	  public static String characterBannerFieldSet = "//fieldset[1]/div/div";
	  public static String characterBannerBrowse = characterBannerFieldSet + browse;
	  public static String characterBannerUpload = characterBannerFieldSet + upload;
	  public static String characterBannerRemove = characterBannerFieldSet + remove;
	  public static String characterBannerDescription = characterBannerFieldSet + "/descendant::div[@class='description']";
	  public static String characterBannerDescriptionOfSize      = "Files must be less than 25 KB.";
	  public static String characterBannerDescriptionOfFormat    = "Allowed file types: jpg.";
	  public static String characterBannerDescriptionOfDimension = "Images must be exactly 200x200 pixels.";
	  			
	  /************vertical tab - hero box*********************/
	  public static String heroBoxVerticalTab = "//ul[@class='vertical-tabs-list']/li[2]/a/strong";
	  public static String heroBoxFieldSet    = "//fieldset[2]/div/div";
	  public static String heroBoxBrowse      = heroBoxFieldSet + browse;
	  public static String heroBoxUpload      = heroBoxFieldSet + upload;
	  public static String heroBoxRemove      = heroBoxFieldSet + remove;
	  
	  /************vertical tab - tile*********************/
	  public static String tileVerticalTab = "//ul[@class='vertical-tabs-list']/li[3]/a/strong";
	  public static String tileFieldSet    = "//fieldset[3]/div/div";
	  public static String tileSmallBrowse = tileFieldSet + subSet1 + browse;
	  public static String tileSmallUpload = tileFieldSet + subSet1 + upload;
	  public static String tileSmallRemove = tileFieldSet + subSet1 + remove;
	  public static String tileSmallDescription            = tileFieldSet + subSet1 + "/descendant::div[@class='description']";
	  public static String tileSmallDescriptionOfSize      = "Files must be less than 75 KB.";
	  public static String tileSmallDescriptionOfFormat    = "Allowed file types: png jpg jpeg.";
	  public static String tileSmallDescriptionOfDimension = "Images must be exactly 708x398 pixels.";
	 
	  public static String tileLargeBrowse = tileFieldSet + subSet2 + browse;
	  public static String tileLargeUpload = tileFieldSet + subSet2 + upload;
	  public static String tileLargeRemove = tileFieldSet + subSet2 + remove;
	  public static String tileLargeDescription            = tileFieldSet + subSet2 + "/descendant::div[@class='description']";
	  public static String tileLargeDescriptionOfSize      = "Files must be less than 100 KB.";
	  public static String tileLargeDescriptionOfFormat    = "\nAllowed file types: png jpg jpeg.";
	  public static String tileLargeDescriptionOfDimension = "\nImages must be exactly 708x836 pixels.";
	  
	  /************content******************/
	  public static String brandPageDescriptionID                 = "edit-field-brand-page-description-und-0-value";	  
	  public static String brandPageDescriptionContentLimitMsgCSS = "div#edit-field-brand-page-description-und-0-value-counter.counter";
	  public static String brandPageDescriptionCounterCSS         = "div#edit-field-brand-page-description-und-0-value-counter.counter strong";
	  public static    int descriptionMaxCharsNumber              = 135;
	  public static String programTelescopeAssetId                = "edit-field-telescope-asset-id-und-0-value";
	  
	  /************url redirect*********************/
	  public static String urlRedirects    = Common.homeURL + "/admin/config/search/redirect";
	  public static String filterRedirects = "edit-filter";
	  public static String filterSubmit    = "edit-submit";
	  public static String filterReset     = "edit-reset";
	  
	  public static String selectAllRedirectsCheckBox = "//table[contains(@class,'redirect-list-tableselect')]/descendant::th[@class='select-all']/input[@class='form-checkbox']";	  
	  public static String redirectUpdateSubmit       = "edit-submit--2";
	  public static String redirectUpdateWarningText  = "Are you sure you want to delete this redirect?";
	  public static String redirectUpdateWarning      = "//h1[@class='page-title']" + Common.TextEntireAddToXpath(redirectUpdateWarningText);
	  
	  public static String urlRedirectsAdd  = urlRedirects + "/add";
	  public static String urlRedirectsFrom = "edit-source";
	  public static String urlRedirectsTo   = "edit-redirect";
	  
	  /************reorder******************/
	  public static String reorderTileHandle(int i) {
		  return "//table[@id='draggableviews-table-content-tiles-reorder-content-tiles']/tbody/tr[" + i + "]/td[1]/a/div";
		  }  	  
	  public static String reorderTileImageSize(int i) {
		  return "//table[@id='draggableviews-table-content-tiles-reorder-content-tiles']/tbody/tr[" + i + "]/descendant::td[@class='views-field views-field-is-big-image']";
		  }
	  
	  /************meta*********************/
	  public static String metaTagsTab            = "//a/strong[text()='Meta tags']";
	  public static String metaTagDescription     = "edit-metatags-und-description-value";
	  	  
}