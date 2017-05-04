/* Start of definition of the adobe analytics variable, "digitalData" .*/
var wcaUser = "false",menuClicked;
var digitalData=digitalData||{};
digitalData={
	page:{
		pageInfo:{},
		category:{},
		attributes:{},
		internalSearch:{}
	},
	event:[{
		eventInfo:{}
	}],
	product:[{
		productInfo:{}
	}],
	cart:{},
	transaction:{
		item:[{
			productInfo:{},
			price:{}
		}],
		profile:{
			address:{}
		},
		total:{},
		attributes:{}
	},
	user:[{
        profile:[{
            profileInfo:{},
            attributes:{
                profileFAN:{}
            }
        }]
    }]
};
/* End of definition of the adobe analytics variable, "digitalData" .*/

/* Self-Invoking Anonymous Function to run on every load of the header content. */

$(function(){
    /* Variables to hold the header data retrieved from the database. */
       var headerData, secMenuData, megaMenuData ;
       var homePageNameInfo;
        var loginId;
        var bdToolsWindow;
        var checkSessionIntervalMins = 29;
        var bdSessionTimeout = 60;
        var  bdSessionId;
       loadJSWithCallBack = function(src, callback) {
          if ($('script[src*="'+src+'"]').length === 0) {
             $.getScript(src)
               .done(function( script, textStatus ) {
                     callback.call();
               })
               .fail(function( jqxhr, settings, exception ) {
                 //console.log("file not loaded " + src);
             });
          }else{
             /* Script loaded already*/
             callback.call();
          }
       }

    var headerFunction = function() {
    this.getHeaderData = function() {
      /*  $.ajax({

            type: "post",
            dataType: 'json',
            url: window.dashboardContext + "/mnm/map/common/dashboardNavigation.jsp",
            success: function(data) {

                if (data) {
                    headerData = data;
                    /* Generating the html for the primary menu using handlebars. */
                   /* var primaryMenuTmpl = Handlebars.compile($("#primaryMenu_tmpl").html()),
                        primaryMenuHtml = primaryMenuTmpl(data);
                    $(".primaryMenuOptionsContainer").removeClass('primaryMenuOption');
                    $('.primaryMenuOption').replaceWith('');
                    $(".primaryMenuOptionsContainer").append(primaryMenuHtml);
                    var dataForNavigation = {};
                    if (window.location.hash) {
                        dataForNavigation.pageUrl = window.location.pathname + "?" + window.location.hash;
                    } else {
                        dataForNavigation.pageUrl = window.location.pathname;
                    }

                    /* Ajax call to get the name of the home page from the DB based on the current url. */
                  /*  $.ajax({
                        type: "post",
                        dataType: 'json',
                        data: dataForNavigation,
                        url: window.dashboardContext + "/mnm/map/common/navigationParent.jsp",
                        headers: {"X-CSRF-Token":window.softToken},
                        success: function(data) {
                            if (data) {
                                homePageNameInfo = data;
                                $(".pageNameContainer .pageName").html(data.name);

                                $.each(headerData.childNavigation, function(dataIndex, primaryMenuData) {

                                    $.each(primaryMenuData.childNavigation, function(dataIndex, secondaryMenuData) {

                                        if (secondaryMenuData.name == homePageNameInfo.name) {

                                            $(".pageName").attr("href", secondaryMenuData.url);
                                        }

                                    });

                                });


                                showSecondaryMenu(homePageNameInfo);
                            }
                        },
                        error: function() {
                            //console.log("Error Response From Service");
                        }
                    });
                }
            },
            error: function() {
                //console.log("Error Response From Service");
            }
        }); */
    }
}
primaryMenuItemClick = function(primaryMenuLinkObj) {

    var $this = $(primaryMenuLinkObj);
    
   /* $('.categoryContainerColumn').remove();*/
    $('.megaMenuContainer').scrollTop(0);
    $('.megaMenuContainer').hide();
    $(".selectionDiv").hide();
    $('.secondaryMenuOptionLink').removeClass('selectedSecondaryMenuOption');
    $('.thirdMenuOptionLink').removeClass('selectedSecondaryMenuOption');

    /* Arrow left position calculation. */

    var elementLeft = $this.position().left,
        elementWidth = $this.width(),
        centerValue = elementLeft + 24 + (elementWidth) / 2;
    $('.selectedOptionIndicator').css('left', centerValue);
    $('.selectedOptionIndicator').show(); //Added By Robert 6/9/15
    // when clicked on Support link
    if ($this.text() === 'Support') {
        return populateSecondaryMenu(primaryMenuLinkObj, getSupportLinkData());    
    }

    /*$.ajax({
         type: "get",
         dataType: 'json',
         async: false,
         contentType :'application/json',
         //url: window.dashboardContext + "/navigation/gadgets/megaMenuNavigation.jsp?megaMenu="+escape($this.text()).toUpperCase(),
         url: window.ebizDashboardContext + "/navigation/"+escape($this.text()),
         //headers: {"X-CSRF-Token":window.softToken},
         success: function(data) {
             populateSecondaryMenu(primaryMenuLinkObj, data);
         },
         complete : function() {
         },
         error: function() {
        	 if (typeof console == 'object') {
        		 console.log("Error Response From Service");
        	 }
         }
     });*/
}

populateSecondaryMenu = function(primaryMenuLinkObj, data) {
	//primary menu link object
	var $this = $(primaryMenuLinkObj);

	if (data) {
		headerData = data;

		if (data.childNavigation.length != 0) {
			$.each(data.childNavigation, function(dataIndex, childNavigation) {
				if(childNavigation == null){
	                childNavigation = {};
	                 secondaryMenuTmpl = Handlebars.compile($("#secondaryMenu_tmpl").html()),
	                 secondaryMenuHtml = secondaryMenuTmpl(childNavigation);
	                $('.secondaryMenuOption').remove();
	                $(".secondaryMenuContentContainer").append(secondaryMenuHtml);
	            }else{
					secMenuData = childNavigation.childNavigation;
					
					var noOfCols = childNavigation.childNavigation.length,
					secondaryMenuTmpl = Handlebars.compile($("#secondaryMenu_tmpl").html()),
					secondaryMenuHtml = secondaryMenuTmpl(childNavigation);
	
					$('.secondaryMenuOption').remove();
					$(".secondaryMenuContentContainer").append(secondaryMenuHtml);
	
					if (noOfCols < 6) {
						$('.secondaryMenuOption').css('margin-left', 40);
					} else if (noOfCols == 6) {
						$('.secondaryMenuOption').css('margin-left', 30);
					} else if (noOfCols > 6 && noOfCols < 9) {
						$('.secondaryMenuOption').css('margin-left', 20);
					}
	            }
				$('.selectedOptionIndicator').show();
				
				if (window.location.pathname == window.ebizDashboardContext + "/index.jsp") {
					$('.secondaryMenuContainerForDashboard').show();
				} else if (window.location.pathname.indexOf('login') > 0) {
                    $('.secondaryMenuContainerForApplication').show();
                } else {
					$(".secondaryMenuContainerForApplication").show();
					/*if(!($('.switch-message-div').is(':visible'))){
						$('.secondaryMenuContainer').css("margin-top", 50);
					}*/
				}
				$('.primaryMenuOptionLink').removeClass('selectedPrimaryMenuOption');
				$this.addClass("selectedPrimaryMenuOption");
				var currentUrl = window.location.href;
				if(currentUrl != null){
					var emaintenanceMatch = currentUrl.search('emaintenance');
					if(emaintenanceMatch >= 0){
						$(".pageNameContainer .pageName").html("Network");
					}
				}
			})
		} else {
			$('.selectedOptionIndicator').hide();
			$('.secondaryMenuContainer').hide();
			$('.secondaryMenuOption').remove();
			$('.primaryMenuOptionLink').removeClass('selectedPrimaryMenuOption');
		}
	}
}


if($(".switch-message-div").is(':visible')){
    		$(".dashboardHeadTitle").css("margin-top","60px");
             $(".dashboardHeadIcons").css("top","127px");
             $("#openWalkThrough").css("margin-top","40px");
             $(".dashboardHeadWhiteOverlay").css("margin-top", "36px");
             $("#dashBoardContainer").css("margin-top", "43px");
}

if(!($('.switch-message-div').is(':visible'))){
		$(".dashboardHeadWhiteOverlay").css("margin-top", 0);
		$(".dashboardHeadTitle").css("margin-top","30px");
		$(".dashboardHeadIcons").css("top","90px");
		$("#openWalkThrough").css("margin-top","0px");
		$("#dashBoardContainer").css("margin-top", "4px");
}

getSupportLinkData = function() {
    return {
          "name": "ESTRATEGY_NAVIGATION",
          "id": "Support",
          "url": null,
          "childNavigation": [
            {
              "name": "Support",
              "url": null,
              "childNavigation": [
                  {
                      "name": "User Information",
                      "url": "/ebiz/ebcsupport/eBCSupport.jsp?module_id=profileInfo",
                      "childNavigation": []
                    },                                  
                    {
                      "name": "Registration and Login",
                      "url": "/ebiz/ebcsupport/eBCSupport.jsp?module_id=registration",
                      "childNavigation": []
                    },
                    {
                      "name": "Orders",
                      "url": "/ebiz/ebcsupport/eBCSupport.jsp?module_id=order",
                      "childNavigation": []
                    },
                    {
                      "name": "Billing",
                      "url": "/ebiz/ebcsupport/eBCSupport.jsp?module_id=billing",
                      "childNavigation": []
                    },
                    {
                        "name": "Network",
                        "url": "/ebiz/ebcsupport/eBCSupport.jsp?module_id=networkInfo",
                        "childNavigation": []
                    },                
                    {
                      "name": "Reports",
                      "url": "/ebiz/ebcsupport/eBCSupport.jsp?module_id=reporting",
                        "childNavigation": []
                    }
            ],
              "urlWithTitle": null,
              "navClass": null
            }
          ],
          "urlWithTitle": null,
          "navClass": null
    };  
}


highlightHomePageNameInSecMenu = function(secondaryMenuItemObj) {

    var $this = $(secondaryMenuItemObj);

    $this.parents(".secondaryMenuOption").find(".selectionDiv").show();
    $this.addClass('homePageNameHighlight');
}

showSecondaryMenu = function(homePageNameInfo) {

    var primaryMenuItem;
    if (headerData && headerData.childNavigation) {
        $.each(headerData.childNavigation, function(dataIndex, primaryMenuData) {

            primaryMenuItemToClick = primaryMenuData.name;
            $.each(primaryMenuData.childNavigation, function(dataIndex, secondaryMenuData) {

                if (secondaryMenuData.name == homePageNameInfo.name) {

                    $.each($('.primaryMenuOptionLink'), function(i, val) {

                        var $this = $(this);

                        if ($this.html() == primaryMenuItemToClick) {
                        			alert("");
                            primaryMenuItemClick($this);
                        }
                    });

                    $.each($('.secondaryMenuOptionLink'), function(i, val) {

                        var $this = $(this);

                        if ($.trim($this.html().split("<")[0]) == secondaryMenuData.name) {

                            highlightHomePageNameInSecMenu($this);
                        }
                    });
                    $.each($('.thirdMenuOptionLink'), function(i, val) {

                        var $this = $(this);

                        if ($.trim($this.html().split("<")[0]) == secondaryMenuData.name) {

                            highlightHomePageNameInSecMenu($this);
                        }
                    });
                    
                    
                }

            });

        });
    }
}

secondaryMenuItemHover = function(secondaryMenuItemObj) {

    var $this = $(secondaryMenuItemObj);

    $(".selectionDiv").hide();
    $('.secondaryMenuOptionLink').removeClass('selectedSecondaryMenuOption');
    $('.thirdMenuOptionLink').removeClass('selectedSecondaryMenuOption');
    
    if ($this.hasClass("pageName")) {
        $.each(headerData.childNavigation, function(dataIndex, childNavigation) {
            var child1 = childNavigation.childNavigation;
            $.each(child1, function(dataIndex, childNavigation) {
                if (childNavigation.name === $.trim($this.html())) {
                    if (childNavigation.childNavigation.length != 0) {
                        secMenuData = child1;
                    }
                }
            });
        });
        $(".megaMenuContainer").addClass("addMargin");
    } else {
        $(".megaMenuContainer").removeClass("addMargin");
    }
    /* 
    $('.categoryContainerColumn').remove();
   $.each(secMenuData, function(dataIndex, childNavigation) {

        if (childNavigation.name == $.trim($this.html().split("<")[0])) {
             Generating the html for the secondary menu. 

            if (childNavigation.childNavigation.length != 0) {

                megaMenuData = childNavigation.childNavigation;
                var megaMenuFirstRowData = {},
                    megaMenuSecondRowData = {},
                    firstRowDataIndex = 0,
                    secondRowDataIndex = 0;

                megaMenuFirstRowData.childNavigation = {};
                megaMenuSecondRowData.childNavigation = {};

                $.each(childNavigation.childNavigation, function(dataIndex, columnInfo) {

                    if (dataIndex < 6) {
                        megaMenuFirstRowData.childNavigation[firstRowDataIndex++] = columnInfo;
                    } else if (dataIndex > 5 && dataIndex < 12) {
                        megaMenuSecondRowData.childNavigation[secondRowDataIndex++] = columnInfo;
                    }

                });

                var megaMenuTmpl = Handlebars.compile($("#megaMenu_tmpl").html()),
                    megaMenuFirstRowHtml = megaMenuTmpl(megaMenuFirstRowData);

                $(".megaMenuFirstRow").append(megaMenuFirstRowHtml);

                if (megaMenuData.length > 6) {

                    var megaMenuSecondRowHtml = megaMenuTmpl(megaMenuSecondRowData);
                    $(".megaMenuSecondRow").append(megaMenuSecondRowHtml);
                    $(".megaMenuSecondRow").show();
                }

                $('.megaMenuContainer').show();
                $this.find(".selectionDiv").show();
                $this.addClass('selectedSecondaryMenuOption');
*/
                /* Width and margin assignments for each column (6 columns max in a row) in the mega menu. */
/*                switch (megaMenuData.length) {

                    case 1:

                        $('.categoryContainer').css('margin-left', '30px');
                        $('.categoryContainer').width(300);
                        break;

                    case 2:

                        $('.categoryContainer').css('margin-left', '20px');
                        $('.categoryContainer').width(300);
                        break;

                    case 3:

                        $('.categoryContainer').css('margin-left', '20px');
                        $('.categoryContainer').width(293);
                        break;

                    case 4:

                        $('.categoryContainer').css('margin-left', '20px');
                        $('.categoryContainer').width(215);
                        break;

                    case 5:

                        $('.categoryContainer').css('margin-left', '10px');
                        $('.categoryContainer').width(175);
                        break;

                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                        $('.categoryContainer').css('margin-left', '10px');
                        $('.categoryContainer').width(145);
                        break;

                    default:
                        $('.categoryContainer').css('margin-left', '10px');
                        $('.categoryContainer').width(145);
                        break;
                }

                $('.megaMenuFirstRow .categoryContainer:first').css('margin-left', '30px');
                $('.megaMenuSecondRow .categoryContainer:first').css('margin-left', '30px');

            } else {

                $('.megaMenuContainer').scrollTop(0);
                $('.megaMenuContainer').hide();
                $(".selectionDiv").hide();
                $('.secondaryMenuOptionLink').removeClass('selectedSecondaryMenuOption');
            }
        }

    });*/
}
/* Loading handlebars.js if it isn't already. */
/*if (userLoggedIn) {
      Generating the html for the primary menu using handlebars. 
    var primaryMenuNames = {"childNavigation": [{"name" : "Manage"},{"name" : "Tools"},{"name" : "Support"}]};

    var primaryMenuTmpl = Handlebars.compile($("#primaryMenu_tmpl").html());
    primaryMenuHtml = primaryMenuTmpl(primaryMenuNames);

    $(".primaryMenuOptionsContainer").removeClass('primaryMenuOption');
    $('.primaryMenuOption').replaceWith('');
    $(".primaryMenuOptionsContainer").append(primaryMenuHtml);
    var dataForNavigation = {};
    if (window.location.hash) {
        dataForNavigation.pageUrl = window.location.pathname + "?" + window.location.hash;
    } else {
        dataForNavigation.pageUrl = window.location.pathname;
    }
}*/

/* On click of any primary menu option, display the corresponding secondary menu. */
$(document).on('click keyup', '.primaryMenuOptionLink', function(ev) {
    if (ev.keyCode==13 || ev.type=='click') {
        ev.stopImmediatePropagation();
        primaryMenuItemClick($(this));

        if (homePageNameInfo) {

            $.each($('.secondaryMenuOptionLink'), function() {

                var $this = $(this);

                if ($.trim($this.html().split("<")[0]) == homePageNameInfo.name) {

                    highlightHomePageNameInSecMenu($this);
                }
            });
            $.each($('.thirdMenuOptionLink'), function() {

                var $this = $(this);

                if ($.trim($this.html().split("<")[0]) == homePageNameInfo.name) {

                    highlightHomePageNameInSecMenu($this);
                }
            });
            
            
        }
    }
});

/* Code related to the CATO compliance for navigation through key board */
// tab-out of a primaryLink in expanded mode
$(document).on('keydown', '.primaryMenuOptionLink', function(e) {
      var keyCode = e.keyCode || e.which;
      // when shift tab pressed retain default behaviour
      if (e.shiftKey && e.keyCode == 9) {
         return;
      }

      e.preventDefault();
      if (keyCode == 40) { //down arrow : focus on the first secondary menu link
          $('.secondaryMenuContentContainer a.secondaryMenuOptionLink:first').focus();
          $('.secondaryMenuContentContainer a.thirdMenuOptionLink:first').focus();
      }

      if (keyCode == 9 || keyCode == 39) { //tab or right arrow
          if ($(this).parent().next().length) { //next primary link
              $(this).parent().next().find("a.primaryMenuOptionLink").focus();
          } else {
              $(".openpopbox").focus(); //traverse to login icon
          }
      }

      if (keyCode == 37) { //left arrow
          if ($(this).parent().prev().length) { //next primary link
              $(this).parent().prev().find("a.primaryMenuOptionLink").focus();
          }
      }
});


//tab-out of a secondary in expanded mode
$(document).on('keydown', '.secondaryMenuOptionLink', function(e) {
      var keyCode = e.keyCode || e.which;
      //enter key
      if(keyCode == 13) {
         window.location = $(this).attr('href');
      }

      e.preventDefault();
      if (keyCode == 40) { //down arrow : focus on the first mega menu link
          $('.megaMenuContainer a.categoryOptionLink:first').focus();
      }

      if (keyCode == 38) {// up arrow: traverse to current primary link
          $(getSelectedPrimaryMenuOption()).find("a.primaryMenuOptionLink").focus();
      }

      if (keyCode == 9 || keyCode == 39) { //tab or right arrow
          if($(this).parent().next().length) { // traverse to next secondary link if exists
              $(this).parent().next().find("a.secondaryMenuOptionLink").focus();
          } else { //traverse to next primary link
              $(getSelectedPrimaryMenuOption()).next().find("a.primaryMenuOptionLink").focus();
          }
      }

      if (keyCode == 37) {// left arrow:
          if ($(this).parent().prev().length) { // traverse to previous link if exists
              $(this).parent().prev().find("a.secondaryMenuOptionLink").focus();
          } else { // traverse to current primary link
              $(getSelectedPrimaryMenuOption()).find("a.primaryMenuOptionLink").focus();
          }
      }
});

$(document).on('keydown', '.thirdMenuOptionLink', function(e) {
    var keyCode = e.keyCode || e.which;
    //enter key
    if(keyCode == 13) {
       window.location = $(this).attr('href');
    }

    e.preventDefault();
    if (keyCode == 40) { //down arrow : focus on the first mega menu link
        $('.megaMenuContainer a.categoryOptionLink:first').focus();
    }

    if (keyCode == 38) {// up arrow: traverse to current primary link
        $(getSelectedPrimaryMenuOption()).find("a.primaryMenuOptionLink").focus();
    }

    if (keyCode == 9 || keyCode == 39) { //tab or right arrow
        if($(this).parent().next().length) { // traverse to next secondary link if exists
            $(this).parent().next().find("a.thirdMenuOptionLink").focus();
        } else { //traverse to next primary link
            $(getSelectedPrimaryMenuOption()).next().find("a.primaryMenuOptionLink").focus();
        }
    }

    if (keyCode == 37) {// left arrow:
        if ($(this).parent().prev().length) { // traverse to previous link if exists
            $(this).parent().prev().find("a.thirdMenuOptionLink").focus();
        } else { // traverse to current primary link
            $(getSelectedPrimaryMenuOption()).find("a.primaryMenuOptionLink").focus();
        }
    }
});
//tab-out of a categoryOptionLink
$(document).on('keydown', '.categoryOptionLink', function(e) {
    var keyCode = e.keyCode || e.which;
    //enter key
    if(keyCode == 13) {
        window.location = $(this).attr('href');
        menuClicked = $(this).attr('href');
    }

    e.preventDefault();

    if (keyCode == 39) {     // right arrow
        $(this).parent().parent().parent().next().find("a.categoryOptionLink:first").focus();
    } else if(keyCode == 37) { //left arrow
        $(this).parent().parent().parent().prev().find("a.categoryOptionLink:first").focus();
    } else if(keyCode == 38) { //up arrow
        if($(this).parent().prev(".categoryOption").length) {
            $(this).parent().prev(".categoryOption").find("a.categoryOptionLink").focus();
        } else {
            if ($(this).parent().parent().parent().prev().length) { //traverse to prev category column if exists
                $(this).parent().parent().parent().prev().find("a.categoryOptionLink:last").focus();
            } else { //traverse to current secondary column
                $(getSelectedSecondaryMenuOption()).find("a.secondaryMenuOptionLink").focus();
                $(getSelectedSecondaryMenuOption()).find("a.thirdMenuOptionLink").focus();
                
            }
        }
    } else if(keyCode == 40) { //down arrow
        if ($(this).parent().next(".categoryOption").length) {
            $(this).parent().next(".categoryOption").find("a.categoryOptionLink").focus();
        } else {
            if ($(this).parent().parent().parent().next().length) { //traverse to next category column if exists
                $(this).parent().parent().parent().next().find("a.categoryOptionLink:first").focus();
            } else { //traverse to next secondary link
                $(getSelectedSecondaryMenuOption()).next().find("a.secondaryMenuOptionLink").focus();
                $(getSelectedSecondaryMenuOption()).next().find("a.thirdMenuOptionLink").focus();
                
            }
        }
    }

});

//get selected primary menu option
function getSelectedPrimaryMenuOption() {
    var retValue;
    $.each($(".primaryMenuContainer .primaryMenuOption"), function(index, value) {
        var primaryLink = $(this).find("a.primaryMenuOptionLink");
        if ($(primaryLink).hasClass("selectedPrimaryMenuOption")) {
            retValue = $(this);
        }
    });
    return retValue;
}

//get selected secondary menu option
function getSelectedSecondaryMenuOption() {
    var retValue;
    $.each($(".secondaryMenuContentContainer .secondaryMenuOption"), function(index, value) {
        if( $(this).find("div.selectionDiv") != null &&  $(this).find("div.selectionDiv").is(':visible')) {
            retValue = $(this);
        }
    });
    return retValue;
}

/* End of Code related to the CATO compliance for navigation through key board */

/* On click of home page name in the primary menu (available on scroll). */
$(document).on('click', '.pageName', function(ev) {

    window.scrollTo(0, 0);
});

$(".headerContentContainer").on("mouseenter", function() {

  /*  $('.categoryContainerColumn').remove();*/
    $('.megaMenuContainer').scrollTop(0);
    $('.megaMenuContainer').hide();
    $(".selectionDiv").hide();
    $('.secondaryMenuOptionLink').removeClass('selectedSecondaryMenuOption');
    $('.thirdMenuOptionLink').removeClass('selectedSecondaryMenuOption');
    
    if (homePageNameInfo) {

        $.each($('.secondaryMenuOptionLink'), function() {

            var $this = $(this);

            if ($this.hasClass("homePageNameHighlight")) {

                highlightHomePageNameInSecMenu($this);
            }
        });
    }
    
    if (homePageNameInfo) {

        $.each($('.thirdMenuOptionLink'), function() {

            var $this = $(this);

            if ($this.hasClass("homePageNameHighlight")) {

                highlightHomePageNameInSecMenu($this);
            }
        });
    }
    
});

/* On click of any secondary menu option. */
$(document).on('click keyup', '.secondaryMenuOptionLink', function(ev) {
    if (ev.keyCode==13 || ev.type=='click') {
           var $this = $(this);
            if (homePageNameInfo) {
                if ($.trim($this.html().split("<")[0]) != homePageNameInfo.name) {
                        $this.parents(".secondaryMenuOption").find(".selectionDiv").hide();
                        $this.removeClass('selectedSecondaryMenuOption');
                }
            }
    }
    }
);
$(document).on('click keyup', '.thirdMenuOptionLink', function(ev) {
    if (ev.keyCode==13 || ev.type=='click') {
           var $this = $(this);
            if (homePageNameInfo) {
                if ($.trim($this.html().split("<")[0]) != homePageNameInfo.name) {
                        $this.parents(".secondaryMenuOption").find(".selectionDiv").hide();
                        $this.removeClass('selectedSecondaryMenuOption');
                }
            }
    }
    }
);


    /* On click of any bdLink create Session. */
    $(document).on('click','.bdLink', function(ev){
        var $this = $(this);
        var attr = $this.attr('href');

        if (typeof attr !== typeof undefined && attr !== false) {
            BdNotify.createSessionForBDLinks(ev, $this.attr("href"));
        }
    });


/*	getProfileData = function(){
		$.ajax({
			type: "post",
			dataType: 'json',
			url: window.dashboardContext + "",
			success: function (data) {
				if(data){
					loginId = data.profileData.repositoryId;
					digitalData.user[0].profile[0].profileInfo.profileID = "";
				    digitalData.user[0].profile[0].attributes.profileRole = data.profileData.userRole;
				}
			},
			error: function (){
				console.log("error");
			}
		 });
	}
*/
    // BD Tools Menu changes


/*    getProfileData = function(){
        $.ajax({
            type: "post",
            dataType: 'json',
            url: window.dashboardContext + "",
            success: function (data) {
                if(data){
                    loginId = data.profileData.repositoryId;
                }
            },
            error: function (){
            	if (typeof console == 'object') {
            		console.log("error");
            	}
            }
         });
    }*/

/*       if(!wcaUser) {
              getProfileData();
       }*/
    BdNotify = {
        createSessionForBDLinks : function(ev, urlOpen) {
            ev.preventDefault();
            $.ajax({
                type:"post",
                dataType: 'json',
                headers: {"authSecurityToken" : window.authSecurityToken},
                url: window.ebizDashboardContext +'/bd/createSession',	
				contentType: "application/json; charset=utf-8",
				Accept: "application/json",
                async: true,
                success : function(response){
                    var sessionResponse = eval(response);
                    var acss = null;
                    var weigs = null;
                    var tid = null;
                    if(sessionResponse.sessionId != null && null != sessionResponse.sessionPwd && null != sessionResponse.bdProfileId){
                    	tid = sessionResponse.sessionId + ":" + sessionResponse.sessionPwd+ ":"+sessionResponse.bdProfileId;
                    }

                    if(null != sessionResponse.acssipAddress && null != sessionResponse.accexpires && null != sessionResponse.acsshash && null !=sessionResponse.acssuser && null != sessionResponse.acsstime ){
                    	acss = "ip&"+sessionResponse.acssipAddress+ "&expires&"+sessionResponse.acssexpires+"&hash&"+sessionResponse.acsshash+"&user&"+sessionResponse.acssuser+"&time&"+sessionResponse.acsstime;
                    }
                    if(null != sessionResponse.weigsIpAddress && null != sessionResponse.weigsExpires && null != sessionResponse.weigsHash && null != sessionResponse.weigsUser && null != sessionResponse.weigsTime){
                    	weigs = "ip&"+sessionResponse.weigsIpAddress+ "&expires&"+sessionResponse.weigsExpires+"&hash&"+sessionResponse.weigsHash+"&user&"+sessionResponse.weigsUser+"&time&"+sessionResponse.weigsTime;
                    }


                    if(null != tid){
                    	 document.cookie = "ECCUser="+tid+"; path=/; domain=.att.com";
                    }
                    if(null != acss){
                    document.cookie = "ACSS_LOGIN="+acss+"; path=/; domain=.att.com";
                    }
                    if(null != weigs){
                    document.cookie = "WEIGS="+weigs+"; path=/; domain=.att.com";
                    }



                    document.cookie = "BDTimeOutEnabled=Y; path=/; domain=.att.com";

                    bdSessionId=sessionResponse.sessionId;
                    bdToolsWindow = window.open(urlOpen,'BDApplication','left=0,top=0,width=900,height=300,status=0,resizable=1,scrollbars=1');
                    
                    // TODO: fix the session timeout and the session check
                    setTimeout(BdNotify.checkBDTimeoutAndExtendSession, 1000*60*checkSessionIntervalMins);
                }
            });
        },

        checkBDTimeoutAndExtendSession : function(){
            if(bdToolsWindow!=null && !bdToolsWindow.closed){
                //console.log("TimeOut***************************"+ bdSessionId);
                $.ajax({
                    type:"post",
                    dataType: 'json',
                    // url: '/rest/model/com/att/b2b/ebcare/rest/actorchain/EBizCareServiceActor/terminateSession?userId='+loginId+'&bdSessionId='+ bdSessionId,
                    url: window.ebizDashboardContext +'/bd/'+bdSessionId+'/checkSession',
                    async: false,
                    success : function(response){
                        var sessionResponse = eval(response);
                        if(sessionResponse>=0){
                        	
                        	if(sessionResponse >= bdSessionTimeout ) {
                        		// call terminate
                        		terminateBDSession();
                        	} else {
                        		// TODO: return timeToExpire from controller
                        		//	var timeToExpire = sessionResponse.BD_TIMEOUT - sessionResponse.LAST_ACCESS_BD_TIME;
                            //Set Timeout to call same function again after 25 mins
                            setTimeout(BdNotify.checkBDTimeoutAndExtendSession, 1000*60*checkSessionIntervalMins);
                        }
                        }
                    },
                    error : function(e){

                    }
                });
            }
        }
    }

    //End of BD Tools menu changes


    /* Click event for the links provided in the mega menu. */
    $(document).on('click','.categoryOptionLink',function(ev){
        $('.megaMenuContainer').scrollTop(0);
        $('.megaMenuContainer').hide();
        window.scrollTo(0, 0);
    });

$(document).on('click', '.secondaryMenuContainer', function(ev) {

    $('.megaMenuContainer').scrollTop(0);
    $('.megaMenuContainer').hide();
});

/* Display Mega menu on hovering over any of the secondary menu links. */
$(document).on('touchstart mouseenter focus', '.secondaryMenuOptionLink, .pageName', function(ev) {
    editHeaderMenuForBilling($(this));
    secondaryMenuItemHover($(this));

    /* Function to handle Menu's on CFD Billing Iframe Pages */
    iframeHover();
});

$(document).on('touchstart mouseenter focus', '.thirdMenuOptionLink, .pageName', function(ev) {
    editHeaderMenuForBilling($(this));
    secondaryMenuItemHover($(this));

    /* Function to handle Menu's on CFD Billing Iframe Pages */
    iframeHover();
});

function editHeaderMenuForBilling(Obj) {
    var url = window.location.pathname;
    var $this = $(Obj);

    //If it is CFD Billing Iframe Page
    if (url == "/ebiz/billing/iframe.jsp" && $.trim($this.html().split("<")[0]) == "Billing") {
        var currentHeaderData = headerData;
        var errorPage = "/ebiz/billing/error.jsp";
        var iframeSrc = $("#CFDIframe").attr('src');
        if (iframeSrc != errorPage) {
            var MenuArea = $("#CFDIframe").contents().find('.hideMenuArea');
            if (MenuArea.length != 0) {
                var MenuHtml = MenuArea.html();
                var payment_array = [];
                var report_array = [];

                var paymentUrls = ["ImplCleanPaymentHistoryAction", "ImplOTPaymentContextSelectAction", "ImplRECPaymentContextSelectAction"];
                var reportUrls = ["ImplPreLoadFFReportListAction", "ImplExternalWFReports", "ImplPreLoadRawDataOutputAction", "ImplDeliveryProfilesContextSelectAction"];
                for (var i = 0; i < paymentUrls.length; i++) {
                    if (MenuHtml.indexOf(paymentUrls[i]) >= 0) {
                        payment_array.push(i + 1);
                    }
                }

                for (var i = 0; i < reportUrls.length; i++) {
                    if (MenuHtml.indexOf(reportUrls[i]) >= 0) {
                        report_array.push(i + 1);
                    }
                }

                $.each(headerData.childNavigation, function(dataIndex, childNavigation) {
                    if (childNavigation.name == "Manage") {
                        var mIndex = dataIndex;
                        var child1 = childNavigation.childNavigation;
                        $.each(child1, function(dataIndex, childNavigation) {
                            if (childNavigation.name == "Billing") {
                                var bIndex = dataIndex;
                                var child2 = childNavigation.childNavigation;
                                $.each(child2, function(dataIndex, childNavigation) {
                                    if (childNavigation.name == "Payments") {
                                        var pIndex = dataIndex;
                                        var child3 = childNavigation.childNavigation;
                                        headerData.childNavigation[mIndex].childNavigation[bIndex].childNavigation[pIndex].childNavigation = [];

                                        $.each(payment_array, function(dataIndex, item) {
                                            if (item == 1) {
                                                headerData.childNavigation[mIndex].childNavigation[bIndex].childNavigation[pIndex].childNavigation.push({
                                                    "childNavigation": [],
                                                    "id": "600153",
                                                    "name": "Payment History",
                                                    "url": "/ebiz/billing/iframe.jsp?destUrl=ImplCleanPaymentHistoryAction.do"
                                                });
                                            } else if (item == 2) {
                                                headerData.childNavigation[mIndex].childNavigation[bIndex].childNavigation[pIndex].childNavigation.push({
                                                    "childNavigation": [],
                                                    "id": "600155",
                                                    "name": "One Time Payment",
                                                    "url": "/ebiz/billing/iframe.jsp?destUrl=ImplOTPaymentContextSelectAction.do"
                                                });
                                            } else if (item == 3) {
                                                headerData.childNavigation[mIndex].childNavigation[bIndex].childNavigation[pIndex].childNavigation.push({
                                                    "childNavigation": [],
                                                    "id": "600157",
                                                    "name": "Recurring Payments",
                                                    "url": "/ebiz/billing/iframe.jsp?destUrl=ImplRECPaymentContextSelectAction.do"
                                                });
                                            }
                                        });
                                    } else if (childNavigation.name == "Reports") {
                                        var pIndex = dataIndex;
                                        var child3 = childNavigation.childNavigation;
                                        headerData.childNavigation[mIndex].childNavigation[bIndex].childNavigation[pIndex].childNavigation = [];

                                        $.each(report_array, function(dataIndex, item) {
                                            if (item == 1) {
                                                headerData.childNavigation[mIndex].childNavigation[bIndex].childNavigation[pIndex].childNavigation.push({
                                                    "childNavigation": [],
                                                    "id": "600159",
                                                    "name": "Create/View Customized Reports",
                                                    "url": "/ebiz/billing/iframe.jsp?destUrl=ImplPreLoadFFReportListAction.do"
                                                });
                                            } else if (item == 2) {
                                                headerData.childNavigation[mIndex].childNavigation[bIndex].childNavigation[pIndex].childNavigation.push({
                                                    "childNavigation": [],
                                                    "id": "600161",
                                                    "name": "Bill Analysis",
                                                    "url": "/ebiz/billing/iframe.jsp?destUrl=ImplExternalWFReports.do"
                                                });
                                            } else if (item == 3) {
                                                headerData.childNavigation[mIndex].childNavigation[bIndex].childNavigation[pIndex].childNavigation.push({
                                                    "childNavigation": [],
                                                    "id": "600163",
                                                    "name": "Raw Data Output",
                                                    "url": "/ebiz/billing/iframe.jsp?destUrl=ImplPreLoadRawDataOutputAction.do"
                                                });
                                            } else if (item == 4) {
                                                headerData.childNavigation[mIndex].childNavigation[bIndex].childNavigation[pIndex].childNavigation.push({
                                                    "childNavigation": [],
                                                    "id": "600165",
                                                    "name": "Delivery Profiles",
                                                    "url": "/ebiz/billing/iframe.jsp?destUrl=ImplDeliveryProfilesContextSelectAction.do"
                                                });
                                            }

                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }
    }
}
/* For all the pages except CFD Iframe Page */
function iframeHover() {
    var url = window.location.pathname;
    //If it is CFD Billing Iframe Page
    if (url == "/ebiz/billing/iframe.jsp") {
        $("a.categoryOptionLink").click(function() {
            var status_url = $(this).attr('href').split('destUrl=')[1].split('&')[0];
            if (status_url != null && status_url != undefined) {
                $('iframe').attr('src', cfd_handoff_url + '/businesscare-ebill/' + status_url);
                if (history.pushState) {
                    history.pushState('', 'AT&amp;T Billing', 'iframe.jsp?destUrl=' + status_url);
                }

                $('.megaMenuContainer').scrollTop(0);
                $('.megaMenuContainer').hide();
                window.scrollTo(0, 0);

                return false;
            } else {
                return true;
            }
        });
    } else {
        return true;
    }

}

$(document).on('mouseleave', '.secondaryMenuOptionLink', function(ev) {

    if ($(".megaMenuContainer").is(":hidden")) {

        if (homePageNameInfo) {

           $.each($('.secondaryMenuOptionLink'), function() {

                var $this = $(this);

                if ($this.hasClass("homePageNameHighlight")) {

                    highlightHomePageNameInSecMenu($this);
                }
            });
        }
    }
});

$(document).on('mouseleave', '.thirdMenuOptionLink', function(ev) {

    if ($(".megaMenuContainer").is(":hidden")) {

        if (homePageNameInfo) {

           $.each($('.thirdMenuOptionLink'), function() {

                var $this = $(this);

                if ($this.hasClass("homePageNameHighlight")) {

                    highlightHomePageNameInSecMenu($this);
                }
            });
        }
    }
});


/* Hide the Mega menu on mouseleave. */
$(document).on('mouseleave', '.megaMenuContainer', function(ev) {
  /*  $('.categoryContainerColumn').remove();*/
    $('.megaMenuContainer').scrollTop(0);
    $('.megaMenuContainer').hide();
    $(".selectionDiv").hide();
    $('.secondaryMenuOptionLink').removeClass('selectedSecondaryMenuOption');
    $('.thirdMenuOptionLink').removeClass('selectedSecondaryMenuOption');

    if (homePageNameInfo) {

        $.each($('.secondaryMenuOptionLink'), function() {

            var $this = $(this);

            if ($this.hasClass("homePageNameHighlight")) {

                highlightHomePageNameInSecMenu($this);
            }
        });
        $.each($('.thirdMenuOptionLink'), function() {

            var $this = $(this);

            if ($this.hasClass("homePageNameHighlight")) {

                highlightHomePageNameInSecMenu($this);
            }
        });
        
        
    }
});

/* On scroll of the page, display the name of the page in the primary menu.
              On scrolling back to the top, display the original primary menu. */
$(window).on('scroll', function() {
    if($('.mapHeader').is(':visible')){
        return false;
    } else {
        $('.secondaryMenuContainer').hide();
        $('.primaryMenuOptionLink').removeClass("selectedPrimaryMenuOption");
        $('.selectedOptionIndicator').hide();
        $('.megaMenuContainer').scrollTop(0);
        $('.megaMenuContainer').hide();
        $('.primaryMenuOptionsContainer').hide();
        $(".pageNameContainer").show();
        if ($(window).scrollTop() === 0) {
        	if(!($('.switch-message-div').is(':visible'))){
                $(".dashboardHeadIcons").css("top","90px");
              }
              else{
                 $(".dashboardHeadIcons").css("top","127px");
              }
            $('.primaryMenuOptionsContainer').show();
            var currentUrl = window.location.href;
            if(currentUrl != null){
                var emaintenanceMatch = currentUrl.search('emaintenance');
                if(emaintenanceMatch >= 0){
                    $(".secondaryMenuContainerForApplication").show();
                    $('.selectedOptionIndicator').show();
                }
            }
            if (homePageNameInfo) {
                showSecondaryMenu(homePageNameInfo);
            }
            $(".pageNameContainer").hide();
        } else {
            if ($(".pageNameContainer .pageName").length && $(".pageNameContainer .pageName").text() !== "") {
                $('.primaryMenuOptionsContainer').hide();
                $(".pageNameContainer").show();
            } else {
                $('.primaryMenuOptionsContainer').show();
                $(".pageNameContainer").hide();
            }
        }
    }
});
      $(".reg-logout-btn").click(function(){
          if(bdToolsWindow!=null && !bdToolsWindow.closed){
          // console.log("log-out with Session Id" + bdSessionId);
            bdToolsWindow.close();
            terminateBDSession();
/*            $.ajax({
                type:"delete",
                dataType: 'json',
                // url: '/rest/model/com/att/b2b/ebcare/rest/actorchain/EBizCareServiceActor/terminateSession?byPassLastAccess=true&userId='+loginId+'&bdSessionId='+ bdSessionId,
                url: window.ebizDashboardContext +'/bd/'+bdSessionId+'/terminateSession',
                async: false,
                success : function(response){
                    // console.log("BD-LOG-OUT-Sucess");
                },
                error : function(e){
                    // console.log("BD-LOG-OUT-Error");
                }
            });*/

         }
    /* Commenting the logout code.As logout functionality is handled by the regular form submition.
         $.ajax({
            type: "get",
            dataType: 'json',
            url: "/rest/model/atg/userprofiling/ProfileActor/logout",
            async: false,
            success: function (response) {
                // console.log("dashboard logout success");
            },
            error: function (){
                window.location.href = "/ebiz/registration/index.jsp?DPSLogout=true";
                // console.log("dashboard logout error");
            }
        } );*/

    });
      
      function terminateBDSession() {
          $.ajax({
              type:"delete",
              dataType: 'json',
              // url: '/rest/model/com/att/b2b/ebcare/rest/actorchain/EBizCareServiceActor/terminateSession?byPassLastAccess=true&userId='+loginId+'&bdSessionId='+ bdSessionId,
              url: window.ebizDashboardContext +'/bd/'+bdSessionId+'/terminateSession',
              async: false,
              success : function(response){
                  // console.log("BD-LOG-OUT-Sucess");
              },
              error : function(e){
                  // console.log("BD-LOG-OUT-Error");
              }
          });    	  
      };
      /*
       * Function Definition to Get Click To Chat Data
       * ========================================== */
          function getClickToChatData() {
             /* $.ajax({
                  type: "get",
                  dataType: 'json',
                  contentType :'application/json',
                  url: window.ebizDashboardContext + "/clicktochat",
                  success: populateClickToChat,
                  error: function() {},
                  complete: clickToChatPopBox
              });*/
          }

          function populateClickToChat(response) {

                  var template = Handlebars.compile($("#clickToChatMultiple_tmpl").html());
                  var clickToChat = template(response);
                  $(".headerIconContainer").append(clickToChat);
             
          };

          function clickToChatPopBox() {
             /* var chatbox = $('.chatBox');

              var methods = {
                open: function(){chatbox.css('display','block');},
                close: function(){chatbox.fadeOut('fast');}
              };
               $(document).on('click keyup ', '.chatIcon', function(ev) {
                     if(ev.keyCode==13 || ev.type=='click'){
                    //$('.chatIcon').click(function()
                if (chatbox.css('display') == 'block'){
                  methods.close();
                  setTimeout(function(){ $(".btn-panel-vertical,#actionsDropDown").css('z-index','999'); },200);
                  if($(window).scrollTop() > 20){
                       setTimeout(function(){ $(".btn-panel-vertical,#actionsDropDown").css('z-index','-999'); },200);
                  };
                } else {
                   $(".btn-panel-vertical,#actionsDropDown").css('z-index','-999');
                  methods.open();
                  $(settings['box']).fadeOut("fast");
                      }   }
              });
*/
              // Close chat-box on clicking cross icon
              $('.circle_close_chat').click(function(){
                setTimeout(function(){ $(".btn-panel-vertical,#actionsDropDown").css('z-index','999'); },200);
                methods.close();
                if($(window).scrollTop() > 20){
                   setTimeout(function(){ $(".btn-panel-vertical,#actionsDropDown").css('z-index','-999'); },200);
                };
              });

              // Close chat-box on Escape key press
              $(document).bind('keyup', function(event){
                if(event.keyCode == 27){ 
                  setTimeout(function(){ $(".btn-panel-vertical,#actionsDropDown").css('z-index','999'); },200);
               /*   methods.close();*/
                  if($(window).scrollTop() > 20){
                       setTimeout(function(){ $(".btn-panel-vertical,#actionsDropDown").css('z-index','-999'); },200);
                  };
                }
              });        
          };

          function JSONLength(json) {
              var length = 0;
              for (var key in json) {
                  length++;
              };
              return length;
          };

          function prepareData(data) {
              var modifiedJSON = {options: []};
              for (var key in data) {
                  var temp = {linkName:key, linkValue:data[key]};
                  modifiedJSON['options'].push(temp);
              };
              return modifiedJSON;
          };

          getClickToChatData();      
});

$('.emailAddress input').focus(function() {
    var dispVal = $(this).val();
    if (dispVal == "Email Address") {
        $(this).val("");
    }
}).blur(function() {
    var dispVal1 = $(this).val();
    if (dispVal1 == "") {
        $(this).val("Email Address");
    }
});

$(document).bind("ajaxSend", function(elm, xhr, s){
       if (s.type == "POST" || s.type == "GET") {
              var token = window.softToken;
              xhr.setRequestHeader('X-CSRF-Token', token);
              }
       });
$(document).ready(function() {
    onClickOfMenu = function(url){
        $('.megaMenuContainer').scrollTop(0);
        $('.megaMenuContainer').hide();
        window.scrollTo(0, 0);
        menuClicked =  url;
        window.location.href = url;
    }
});