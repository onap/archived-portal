/*-
 * ================================================================================
 * eCOMP Portal
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */
﻿(function ($) {
	if (!$.fn.tree) {
		throw "Error jqTree is not loaded.";
	}

	$.fn.jqTreeContextMenu = function (menuElement, callbacks) {

		var self = this;
		var $el = this;

		var $menuEl = menuElement;
		
		var nodeToDisabledMenuItems = {};
		
		$menuEl.hide();

		$el.bind("contextmenu", function (e) {
			e.preventDefault();
			return false; 
		});

		$el.bind('tree.contextmenu', function (event) {
			var x = event.click_event.pageX;
			var y = event.click_event.pageY;
			var yPadding = 5;
			var xPadding = 5;
			var menuHeight = $menuEl.height();
			var menuWidth = $menuEl.width();
			var windowHeight = $(window).height();
			var windowWidth = $(window).width();
			
			if (menuHeight + y + yPadding > windowHeight) {
				y = y - menuHeight;
			}
			if (menuWidth + x + xPadding > windowWidth) {
				x = x - menuWidth;
			}

			if (Object.keys(nodeToDisabledMenuItems).length > 0) {
				if (event.node.name in nodeToDisabledMenuItems) {
					var nodeName = event.node.name;
					var items = nodeToDisabledMenuItems[nodeName];
					if (items.length === 0) {
						$menuEl.find('li').addClass('disabled');
						$menuEl.find('li > a').unbind('click');
					} else {
						$menuEl.find('li > a').each(function () {
							$(this).closest('li').removeClass('disabled');
							var hrefValue = $(this).attr('href');
							var value = hrefValue.slice(hrefValue.indexOf("#") + 1, hrefValue.length)
							if ($.inArray(value, items) > -1) {
								$(this).closest('li').addClass('disabled');
								$(this).unbind('click');
							}
						});	
					}
				} else {
					$menuEl.find('li.disabled').removeClass('disabled');
				}
			}

			$menuEl.show();

			$menuEl.offset({ left: x, top: y });

			var dismissContextMenu = function () {
				$(document).unbind('click.jqtreecontextmenu');
				$el.unbind('tree.click.jqtreecontextmenu');
				$menuEl.hide();
			}

			$(document).bind('click.jqtreecontextmenu', function () {
				dismissContextMenu();
			});

			$el.bind('tree.click.jqtreecontextmenu', function (e) {
				dismissContextMenu();
			});

			var selectedNode = $el.tree('getSelectedNode');
			if (selectedNode !== event.node) {
				$el.tree('selectNode', event.node);
			}

			var menuItems = $menuEl.find('li:not(.disabled) a');
			if (menuItems.length !== 0) {
				menuItems.unbind('click');
				menuItems.click(function (e) {
					e.stopImmediatePropagation();
					dismissContextMenu();
					var hrefAnchor = e.currentTarget.attributes.href.nodeValue;
					var funcKey = hrefAnchor.slice(hrefAnchor.indexOf("#") + 1, hrefAnchor.length)
					var callbackFn = callbacks[funcKey];
					if (callbackFn) {
						callbackFn(event.node);
					}
					return false;
				});
			}
		});
		
		this.disable = function () {
			if (arguments.length === 0) {
				$menuEl.find('li:not(.disabled)').addClass('disabled');
				$menuEl.find('li a').unbind('click');
				nodeToDisabledMenuItems = {};
			} else if (arguments.length === 1) {
				var items = arguments[0];
				if (typeof items !== 'object') {
					return;
				}
				$menuEl.find('li > a').each(function () {
					var hrefValue = $(this).attr('href');
					var value = hrefValue.slice(hrefValue.indexOf("#") + 1, hrefValue.length)
					if ($.inArray(value, items) > -1) {
						$(this).closest('li').addClass('disabled');
						$(this).unbind('click');
					}
				});
				nodeToDisabledMenuItems = {};
			} else if (arguments.length === 2) {
				var nodeName = arguments[0];
				var items = arguments[1];
				nodeToDisabledMenuItems[nodeName] = items;
			}
		};

		this.enable = function () {
			if (arguments.length === 0) {
				$menuEl.find('li.disabled').removeClass('disabled');
				nodeToDisabledMenuItems = {};
			} else if (arguments.length === 1) {
				var items = arguments[0];
				if (typeof items !== 'object') {
					return;
				}
				
				$menuEl.find('li > a').each(function () {
					var hrefValue = $(this).attr('href');
					var value = hrefValue.slice(hrefValue.indexOf("#") + 1, hrefValue.length)
					if ($.inArray(value, items) > -1) {
						$(this).closest('li').removeClass('disabled');
					}
				});

				nodeToDisabledMenuItems = {};
			} else if (arguments.length === 2) {
				var nodeName = arguments[0];
				var items = arguments[1];
				if (items.length === 0) {
					delete nodeToDisabledMenuItems[nodeName];
				} else {
					var disabledItems = nodeToDisabledMenuItems[nodeName];
					for (var i = 0; i < items.length; i++) {
						var idx = disabledItems.indexOf(items[i]);
						if (idx > -1) {
							disabledItems.splice(idx, 1);
						}
					}
					if (disabledItems.length === 0) {
						delete nodeToDisabledMenuItems[nodeName];
					} else {
						nodeToDisabledMenuItems[nodeName] = disabledItems;	
					}
				}
				if (Object.keys(nodeToDisabledMenuItems).length === 0) {
					$menuEl.find('li.disabled').removeClass('disabled');
				}
			}
		};
		return this;
	};
} (jQuery));
