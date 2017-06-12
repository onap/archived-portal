var ARUGMENT1 = (function(window, undefined) {                                                                                      
                                                                                                                                    
	var ARUGMENT1 = ARUGMENT1 || {};                                                                                                
	function extractHostPortApp(src) {	                                                                                            
		ARUGMENT1.microserviceId = MICROSERVICE_ID;                                                                                 
		ARUGMENT1.pathArray = src.split( '/' );                                                                                     
		ARUGMENT1.widgetName = ARUGMENT1.pathArray[ARUGMENT1.pathArray.length - 2];                                                 
		ARUGMENT1.serviceSeperator = ARUGMENT1.pathArray[ARUGMENT1.pathArray.length - 4];                                           
		ARUGMENT1.commonUrl = src.substring(0, src.lastIndexOf("/" + ARUGMENT1.widgetName));                                      
		ARUGMENT1.recipientDivDataAttrib = 'data-' + ARUGMENT1.widgetName;                                                          
		ARUGMENT1.controllerName = 'ARUGMENT2';                                                                                     
		ARUGMENT1.readyCssFlag = 'ARUGMENT3';                                                                                       
		ARUGMENT1.readyCssFlagExpectedValue = '#bada55';                                                                            
		ARUGMENT1.serviceURL = src.substring(0, src.lastIndexOf("/" + ARUGMENT1.serviceSeperator)) + '/portalApi/microservice/proxy/parameter/' + WIDGET_ID;  
	}                                                                                                                              
	                                                                                                                               
	extractHostPortApp(document.currentScript.src);                                                                                
	                                                                                                                               
	function loadStylesheet(url) {                                                                                                 
		var link = document.createElement('link');                                                                                 
		link.rel = 'stylesheet';                                                                                                   
		link.type = 'text/css';                                                                                                    
		link.href = url;                                                                                                           
		var entry = document.getElementsByTagName('script')[0];                                                                    
		entry.parentNode.insertBefore(link, entry);                                                                                
	}                                                                                                                              
                                                                                                                                   
	function isCssReady(callback) {                                                                                                
		var testElem = document.createElement('span');                                                                             
		testElem.id = ARUGMENT1.readyCssFlag;	                                                                                   
		testElem.style = 'CSS_ARG1';                                                                       
		var entry = document.getElementsByTagName('script')[0];                                                                    
		entry.parentNode.insertBefore(testElem, entry);                                                                            
	                                                                                                                               
		(function poll() {                                                                                                         
			var node = document.getElementById('css-ready');                                                                       
			var value;                                                                                                             
			if (window.getComputedStyle) {                                                                                         
				value = document.defaultView.getComputedStyle(testElem, null)                                                      
						.getPropertyValue('color');                                                                                
			}                                                                                                                      
			else if (node.currentStyle) {                                                                                          
				value = node.currentStyle.color;                                                                                   
			}                                                                                                                      
			if (value && value === 'rgb(186, 218, 85)' || value.toLowerCase() === ARUGMENT1.readyCssFlagExpectedValue) {           
				callback();                                                                                                        
			} else {                                                                                                               
				setTimeout(poll, 500);                                                                                             
			}                                                                                                                      
		})();                                                                                                                      
	}                                                                                                                              
	                                                                                                                               
	function injectCss(css) {                                                                                                      
		var style = document.createElement('style');                                                                               
		style.type = 'text/css';                                                                                                   
 		css = css.replace(/\}/g, "}\n");                                                                                           
	                                                                                                                               
		if (style.styleSheet) {                                                                                                    
			style.styleSheet.cssText = css;                                                                                        
		} else {                                                                                                                   
			style.appendChild(document.createTextNode(css));                                                                       
		}                                                                                                                          
		var entry = document.getElementsByTagName('script')[0];                                                                    
		entry.parentNode.insertBefore(style, entry);                                                                               
	}                                                                                                                              
	                                                                                                                               
	function loadScript(url, callback) {                                                                                           
		var script = document.createElement('script');                                                                             
		script.src = url;                                                                                                          
		var entry = document.getElementsByTagName('script')[0];                                                                    
		entry.parentNode.insertBefore(script, entry);                                                                              
		script.onload = script.onreadystatechange = function() {                                                                   
			var rdyState = script.readyState;                                                                                      
			if (!rdyState || /complete|loaded/.test(script.readyState)) {                                                          
				callback();                                                                                                        
				script.onload = null;                                                                                              
				script.onreadystatechange = null;                                                                                  
			}                                                                                                                      
		};                                                                                                                         
	}                                                                                                                              
                                                                                                                                   
	function loadSupportingFiles(callback) {                                                                                       
		callback();                                                                                                                
	}                                                                                                                              
	                                                                                                                               
	function getWidgetParams() {                                                                                                   
		                                                                                                                           
	}                                                                                                                              
	                                                                                                                               
	function getWidgetData(widgetUrl, callback) {   	                                                                           
	var responseData;                      																						   
	try{                                   																						   
		jQuery.ajax({                       																					   
			url: widgetUrl,               																						   
			method: "GET",           																							   
			xhrFields: {              																							   
				withCredentials: true       																					   
			},    
			crossDomain: true,			
			success: function (result) {    																				 	   
				if (result.isOk == false){  																					   
			                     																							   
				}else{                      																					   
					callback(result);		  																					   
				}                           																					   
			}                             																					   
		});                                 																				   
	}                                      																						   
	catch(e){                              																						   
	
	}                                      																						   
																							   
	}                                                                                                   
	                                                                                                                               
	function getMarkupContent(markupLocation, target){                                                                             
		                                                                                                                           
		jQuery.ajax({                                                                                                              
	        url: markupLocation,                                                                                                   
	        success: function (result) {                                                                                           
	            if (result.isOk == false){                                                                                         
	            	                                                                                                               
	            }else{                                                                                                             
	            	target.innerHTML = result;                                                                                     
				}                                                                                                                  
	        },                                                                                                                     
	        async: false                                                                                                           
	    });                                                                                                                        
	}                                                                                                                              
	                                                                                                                               
	function renderWidget(data, location, $controllerProvider) {                                                                   
		var div = document.createElement('div');                                                                                   
		getMarkupContent(ARUGMENT1.commonUrl + "/markup/" + ARUGMENT1.widgetName, div);                                          
		location.append(div);                                                                                                      
		 ARUGMENT1.widgetData = data;                                                                                              
		app.controllerProvider.register(ARUGMENT1.controllerName, ARUGMENT1.controller);                                           
		var mController = angular.element(document.getElementById("widgets"));                                                   
		mController.scope().activateThis(location);                                                                                
	}                                                                                                                              
	                                                                                                                               
	function printAllArtifacts(moduleName, controllerName) {                                                                       
	    var queue = angular.module(moduleName)._invokeQueue;                                                                       
	    for(var i=0;i<queue.length;i++) {                                                                                          
	        var call = queue[i];                                                                                                   
	        console.log(i + '. ' + call[0] + ' - ' + call[1] + ' - ' + call[2][0]);                                                
	    }                                                                                                                          
	}                                                                                                                              
	                                                                                                                               
	function get(name){                                                                                                            
	   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))                                     
	      return decodeURIComponent(name[1]);                                                                                      
	}                                                                                                                              
                                                                                                                                   
	loadSupportingFiles(function() {                                                                                               
		loadStylesheet(ARUGMENT1.commonUrl + '/' + ARUGMENT1.widgetName + '/style.css');                                           
		loadScript(ARUGMENT1.commonUrl + '/' + ARUGMENT1.widgetName + '/controller.js',                                            
			function() {                                                                                                           
				$('['+ ARUGMENT1.recipientDivDataAttrib + ']').each(function() {                                                   
					var location = jQuery(this);                                                                                   
					location.removeAttr(ARUGMENT1.recipientDivDataAttrib);                                                         
					var id = location.attr(ARUGMENT1.recipientDivDataAttrib);                                                      
					getWidgetData(ARUGMENT1.serviceURL, function(data) {                                                           
						isCssReady(function(){                                                                                     
							renderWidget(data, location);                                                                          
						});								                                                                           
					});                                                                                                            
				});                                                                                                                
			}                                                                                                                      
		);                                                                                                                         
	});                                                                                                                            
	                                                                                                                               
	return ARUGMENT1;	                                                                                                           
})(window);                                                                                                                        
				