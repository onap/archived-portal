var ARGUMENT1 = (function(window, undefined) {                                                                                      
                                                                                                                                    
	var ARGUMENT1 = ARGUMENT1 || {};                                                                                                
	function extractHostPortApp(src) {	                                                                                            
		
		ARGUMENT1.microserviceId = MICROSERVICE_ID;                                                                                 
		ARGUMENT1.pathArray = src.split( '/' );                                                                                     
			
		ARGUMENT1.widgetName = WIDGET_ID;
		ARGUMENT1.serviceSeperator = ARGUMENT1.pathArray[ARGUMENT1.pathArray.length - 4];                                           
		ARGUMENT1.commonUrl = src.substring(0, src.lastIndexOf("/" + ARGUMENT1.pathArray[ARGUMENT1.pathArray.length - 2]));       
		
		ARGUMENT1.recipientDivDataAttrib = 'data-' + ARGUMENT1.widgetName;                                                          
		ARGUMENT1.controllerName = 'ARGUMENT2';                                                                                     
		ARGUMENT1.readyCssFlag = 'ARGUMENT3';                                                                                       
		ARGUMENT1.readyCssFlagExpectedValue = '#bada55';                                                                            
		ARGUMENT1.serviceURL = src.substring(0, src.lastIndexOf("/" + ARGUMENT1.serviceSeperator)) + '/portalApi/microservice/proxy/parameter/' + WIDGET_ID;  
	}                                  
	
	extractHostPortApp(document.getElementsByTagName('script')[0].src);
	
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
		testElem.id = ARGUMENT1.readyCssFlag;	                                                                                   
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
			if (value && value === 'rgb(186, 218, 85)' || value.toLowerCase() === ARGUMENT1.readyCssFlagExpectedValue) {           
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
	
	function getMarkupContent(markupLocation, callback){                                                                             
		                                                                                                                           
		try{
			jQuery.ajax({                                                                                                              
		        url: markupLocation,
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
	                                                                                                                               
	function renderWidget(data, location, $controllerProvider) {                                                                   
		var div = document.createElement('div');                                                                                   
		getMarkupContent(ARGUMENT1.commonUrl + "/markup/" + ARGUMENT1.widgetName, function(div){
			location.append(div);                                                                                                      
			ARGUMENT1.widgetData = data;
			app.controllerProvider.register(ARGUMENT1.controllerName, ARGUMENT1.controller);                                           
			var mController = angular.element(document.getElementById("widgets"));                                                   
			mController.scope().activateThis(location);   
		});
		
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
		loadStylesheet(ARGUMENT1.commonUrl + '/' + ARGUMENT1.widgetName + '/style.css');                                           
		loadScript(ARGUMENT1.commonUrl + '/' + ARGUMENT1.widgetName + '/controller.js',                                            
			function() {                                                                                                           
				$('['+ ARGUMENT1.recipientDivDataAttrib + ']').each(function() {                                                   
					var location = jQuery(this);                                                                                   
					location.removeAttr(ARGUMENT1.recipientDivDataAttrib);                                                         
					var id = location.attr(ARGUMENT1.recipientDivDataAttrib);                                                      
					getWidgetData(ARGUMENT1.serviceURL, function(data) { 
						isCssReady(function(){                                                                                     
							renderWidget(data, location);                                                                          
						});								                                                                           
					});                                                                                                            
				});                                                                                                                
			}                                                                                                                      
		);                                                                                                                         
	});                                                                                                                            
	                                                                                                                               
	return ARGUMENT1;	                                                                                                           
})(window);                                                                                                                        
				