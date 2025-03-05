/******/ (() => { // webpackBootstrap
/******/ 	"use strict";
/******/ 	var __webpack_modules__ = ({

/***/ "./src/jeesl.responsive.ts":
/*!*********************************!*\
  !*** ./src/jeesl.responsive.ts ***!
  \*********************************/
/***/ ((__unused_webpack_module, __unused_webpack_exports, __webpack_require__) => {

/* provided dependency */ var $ = __webpack_require__(/*! jquery */ "./node_modules/jquery/dist/jquery.js");
/* provided dependency */ var jQuery = __webpack_require__(/*! jquery */ "./node_modules/jquery/dist/jquery.js");

var menuHeightStyle;
var treeHeightStyle;
var listItemHeight;
window.calculateMenuHeight = function () {
    var cssRules = '@media (max-width: 768px) {' + cssRulesMenuHeightCalculate() + '}';
    menuHeightStyle.append(cssRules);
};
function cssRulesMenuHeightCalculate() {
    var cssRules = '';
    $('.jeesl-dropdown-list').each(function (index, element) {
        cssRules += '.jeesl-dropdown-list[dropdown-id=' +
            ($(element).attr('dropdown-id') || '').replace(/:/g, '\\:') +
            '].jeesl-active { height: ' +
            ($.merge($(element).children('.jeesl-dropdown-item').toArray(), $(element).find('.jeesl-datatable tr').toArray())
                .map(function (child) { var _a; return (_a = $(child).outerHeight()) !== null && _a !== void 0 ? _a : 0; })
                .reduce(function (previous, current) { return previous + current; }, 0) + 15 + (!$(element).hasClass('jeesl-dropdown-list-multi') ? 15 : 0)) +
            'px !important; }';
    });
    return cssRules;
}
function toggleTreeItem() {
    $(this).parent().next('.ui-treenode-children').toggleClass('jeesl-active');
}
function findTreeRoot(treeNode) {
    return treeNode.parent().hasClass('ui-treenode-children') ? findTreeRoot(treeNode.parent().parent()) : treeNode;
}
function setTreeHeight(node) {
    var height = 0;
    var childList = node.children('.ui-treenode-children');
    if (node.hasClass('jeesl-active')) {
        height += childList.first()
            .children('.ui-treenode')
            .map(function (index, item) { return setTreeHeight($(item)); })
            .toArray()
            .reduce(function (accumulator, currentValue) { return accumulator + currentValue; }, 0)
            + childList.first().children('.ui-tree-droppoint:last-child').length * 4;
    }
    childList.height(height);
    return height + 50;
}
window.toggleMenu = function() {
    var _this = this;
    $('.jeesl-menu-bar-dropdown').filter(function (index, button) { return button !== _this; }).removeClass('jeesl-active').siblings('.jeesl-dropdown-list').removeClass('jeesl-active');
    $(this).filter(function (index, current) { return $(current).find('.jeesl-greyscale'); })
        .toggleClass('jeesl-active')
        .siblings('.jeesl-dropdown-list')
        .removeAttr('style')
        .toggleClass('jeesl-active')
        .find('.jeesl-open')
        .delay(200)
        .queue(function () { $(this).removeClass('jeesl-open').removeAttr('style').dequeue(); });
}

window.reloadStatusBar = function() {
	reloadContent($('.jeesl-status-bar'));
}
window.jsfToJQuery = function(jsfSelector) {
	return $(jsfSelector.replace(' ', ',').replace(/^\:+/, '#').replaceAll(/,\:+/g, ',#').replaceAll(':', '\\:'));
}
window.reloadContent = function(context) {
    var newButtons = context.find('.jeesl-menu-bar-dropdown');
    var newDropdowns = context.find('.jeesl-dropdown-list').attr('dropdown-id', function (index, oldValue) { return 'jeesl-dropdown-' + ($('.jeesl-dropdown-list').length + index); });
    calculateMenuHeight(newDropdowns);
    newButtons.click(toggleMenu);
}
function toggleSubmenu(eventArgs) {
    var currentSubmenu = $(this).parent();
    var currentListItem = currentSubmenu.parent();
    var closeRequested;
    var submenuHeight = currentListItem.children('.jeesl-submenu').eq(0).height();
    if (listItemHeight !== undefined && submenuHeight !== undefined && submenuHeight > listItemHeight) {
        closeRequested = true;
    }
    else {
        closeRequested = false;
    }
    var overlay = currentListItem.parent();
    overlay.height('auto');
    var submenus = overlay.find('.jeesl-submenu');
    if (!closeRequested) {
        currentSubmenu.addClass('jeesl-open');
    }
    submenus.each(function () {
        var $this = $(this);
        var dropDownSubOuterHeight = $this.children('.jeesl-dropdown-sub').eq(0).outerHeight();
        $this.stop().animate({
            height: $this.parent().is(currentListItem) && !closeRequested ? (dropDownSubOuterHeight !== null && dropDownSubOuterHeight !== void 0 ? dropDownSubOuterHeight : 0) + listItemHeight : listItemHeight
        }, 400, function () {
            if ($this.height() === listItemHeight) {
                $this.removeAttr('style');
            }
        });
    });
    $.when(submenus).then(function () {
        submenus.each(function () { $(this).toggleClass('jeesl-open', $(this).is(currentSubmenu) && !closeRequested); });
        overlay.height(overlay.children().map(function (index, child) { return $(child).outerHeight(); }).toArray().reduce(function (previous, current) { return previous + current; }, 0) + 30);
        if (overlay.find('.jeesl-open').length === 0) {
            overlay.removeAttr('style');
        }
    });
}
function toggleDatatable(eventArgs) {
    var panel = $(this).parent();
    if (panel.hasClass('jeesl-active')) {
        panel.removeClass('jeesl-active');
        panel.find('.ui-datatable-tablewrapper').animate({ height: 0 }, 400);
    }
    else {
        var tablewrapper = panel.find('.ui-datatable-tablewrapper');
        tablewrapper.css('height', 'auto');
        var height = tablewrapper.height();
        tablewrapper.height(0);
        panel.addClass('jeesl-active');
        tablewrapper.animate({ height: height }, 400);
    }
}
function initCollapsibleDatatable(parent) {
    var datatable = !!parent ? $(parent).find('.jeesl-datatable-collapsible') : $('.jeesl-datatable-collapsible');
    datatable.not('.jeesl-active').find('.ui-datatable-tablewrapper').height(0);
    datatable.find('.ui-datatable-header').click(toggleDatatable);
}
(function ($) {
    $(function () {
        menuHeightStyle = $('<style>').prop('type', 'text/css').appendTo('head');
        treeHeightStyle = $('<style>').prop('type', 'text/css').appendTo('head');
        var menuButtons = $('.jeesl-menu-bar-dropdown');
        var dropdowns = $('.jeesl-dropdown-list').attr('dropdown-id', function (index, oldValue) { return oldValue || ('jeesl-dropdown-' + index); });
        calculateMenuHeight(dropdowns);
        menuButtons.click(toggleMenu);
        $('.ui-tree-toggler').click(toggleTreeItem);
        var overlay = $('.jeesl-header .jeesl-dropdown-list');
        overlay.find('.jeesl-submenu-icon').click({ overlay: overlay }, toggleSubmenu);
        initCollapsibleDatatable();
    });
})(jQuery.noConflict(true));


/***/ })

/******/ 	});
/************************************************************************/
/******/ 	// The module cache
/******/ 	var __webpack_module_cache__ = {};
/******/ 	
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/ 		// Check if module is in cache
/******/ 		var cachedModule = __webpack_module_cache__[moduleId];
/******/ 		if (cachedModule !== undefined) {
/******/ 			return cachedModule.exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = __webpack_module_cache__[moduleId] = {
/******/ 			// no module.id needed
/******/ 			// no module.loaded needed
/******/ 			exports: {}
/******/ 		};
/******/ 	
/******/ 		// Execute the module function
/******/ 		__webpack_modules__[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/ 	
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/ 	
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = __webpack_modules__;
/******/ 	
/************************************************************************/
/******/ 	/* webpack/runtime/amd define */
/******/ 	(() => {
/******/ 		__webpack_require__.amdD = function () {
/******/ 			throw new Error('define cannot be used indirect');
/******/ 		};
/******/ 	})();
/******/ 	
/******/ 	/* webpack/runtime/amd options */
/******/ 	(() => {
/******/ 		__webpack_require__.amdO = {};
/******/ 	})();
/******/ 	
/******/ 	/* webpack/runtime/chunk loaded */
/******/ 	(() => {
/******/ 		var deferred = [];
/******/ 		__webpack_require__.O = (result, chunkIds, fn, priority) => {
/******/ 			if(chunkIds) {
/******/ 				priority = priority || 0;
/******/ 				for(var i = deferred.length; i > 0 && deferred[i - 1][2] > priority; i--) deferred[i] = deferred[i - 1];
/******/ 				deferred[i] = [chunkIds, fn, priority];
/******/ 				return;
/******/ 			}
/******/ 			var notFulfilled = Infinity;
/******/ 			for (var i = 0; i < deferred.length; i++) {
/******/ 				var [chunkIds, fn, priority] = deferred[i];
/******/ 				var fulfilled = true;
/******/ 				for (var j = 0; j < chunkIds.length; j++) {
/******/ 					if ((priority & 1 === 0 || notFulfilled >= priority) && Object.keys(__webpack_require__.O).every((key) => (__webpack_require__.O[key](chunkIds[j])))) {
/******/ 						chunkIds.splice(j--, 1);
/******/ 					} else {
/******/ 						fulfilled = false;
/******/ 						if(priority < notFulfilled) notFulfilled = priority;
/******/ 					}
/******/ 				}
/******/ 				if(fulfilled) {
/******/ 					deferred.splice(i--, 1)
/******/ 					var r = fn();
/******/ 					if (r !== undefined) result = r;
/******/ 				}
/******/ 			}
/******/ 			return result;
/******/ 		};
/******/ 	})();
/******/ 	
/******/ 	/* webpack/runtime/define property getters */
/******/ 	(() => {
/******/ 		// define getter functions for harmony exports
/******/ 		__webpack_require__.d = (exports, definition) => {
/******/ 			for(var key in definition) {
/******/ 				if(__webpack_require__.o(definition, key) && !__webpack_require__.o(exports, key)) {
/******/ 					Object.defineProperty(exports, key, { enumerable: true, get: definition[key] });
/******/ 				}
/******/ 			}
/******/ 		};
/******/ 	})();
/******/ 	
/******/ 	/* webpack/runtime/global */
/******/ 	(() => {
/******/ 		__webpack_require__.g = (function() {
/******/ 			if (typeof globalThis === 'object') return globalThis;
/******/ 			try {
/******/ 				return this || new Function('return this')();
/******/ 			} catch (e) {
/******/ 				if (typeof window === 'object') return window;
/******/ 			}
/******/ 		})();
/******/ 	})();
/******/ 	
/******/ 	/* webpack/runtime/hasOwnProperty shorthand */
/******/ 	(() => {
/******/ 		__webpack_require__.o = (obj, prop) => (Object.prototype.hasOwnProperty.call(obj, prop))
/******/ 	})();
/******/ 	
/******/ 	/* webpack/runtime/make namespace object */
/******/ 	(() => {
/******/ 		// define __esModule on exports
/******/ 		__webpack_require__.r = (exports) => {
/******/ 			if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 				Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 			}
/******/ 			Object.defineProperty(exports, '__esModule', { value: true });
/******/ 		};
/******/ 	})();
/******/ 	
/******/ 	/* webpack/runtime/jsonp chunk loading */
/******/ 	(() => {
/******/ 		// no baseURI
/******/ 		
/******/ 		// object to store loaded and loading chunks
/******/ 		// undefined = chunk not loaded, null = chunk preloaded/prefetched
/******/ 		// [resolve, reject, Promise] = chunk loading, 0 = chunk loaded
/******/ 		var installedChunks = {
/******/ 			"jeesl.responsive": 0
/******/ 		};
/******/ 		
/******/ 		// no chunk on demand loading
/******/ 		
/******/ 		// no prefetching
/******/ 		
/******/ 		// no preloaded
/******/ 		
/******/ 		// no HMR
/******/ 		
/******/ 		// no HMR manifest
/******/ 		
/******/ 		__webpack_require__.O.j = (chunkId) => (installedChunks[chunkId] === 0);
/******/ 		
/******/ 		// install a JSONP callback for chunk loading
/******/ 		var webpackJsonpCallback = (parentChunkLoadingFunction, data) => {
/******/ 			var [chunkIds, moreModules, runtime] = data;
/******/ 			// add "moreModules" to the modules object,
/******/ 			// then flag all "chunkIds" as loaded and fire callback
/******/ 			var moduleId, chunkId, i = 0;
/******/ 			if(chunkIds.some((id) => (installedChunks[id] !== 0))) {
/******/ 				for(moduleId in moreModules) {
/******/ 					if(__webpack_require__.o(moreModules, moduleId)) {
/******/ 						__webpack_require__.m[moduleId] = moreModules[moduleId];
/******/ 					}
/******/ 				}
/******/ 				if(runtime) var result = runtime(__webpack_require__);
/******/ 			}
/******/ 			if(parentChunkLoadingFunction) parentChunkLoadingFunction(data);
/******/ 			for(;i < chunkIds.length; i++) {
/******/ 				chunkId = chunkIds[i];
/******/ 				if(__webpack_require__.o(installedChunks, chunkId) && installedChunks[chunkId]) {
/******/ 					installedChunks[chunkId][0]();
/******/ 				}
/******/ 				installedChunks[chunkId] = 0;
/******/ 			}
/******/ 			return __webpack_require__.O(result);
/******/ 		}
/******/ 		
/******/ 		var chunkLoadingGlobal = self["webpackChunkjeesl"] = self["webpackChunkjeesl"] || [];
/******/ 		chunkLoadingGlobal.forEach(webpackJsonpCallback.bind(null, 0));
/******/ 		chunkLoadingGlobal.push = webpackJsonpCallback.bind(null, chunkLoadingGlobal.push.bind(chunkLoadingGlobal));
/******/ 	})();
/******/ 	
/************************************************************************/
/******/ 	
/******/ 	// startup
/******/ 	// Load entry module and return exports
/******/ 	// This entry module depends on other loaded chunks and execution need to be delayed
/******/ 	var __webpack_exports__ = __webpack_require__.O(undefined, ["vendors"], () => (__webpack_require__("./src/jeesl.responsive.ts")))
/******/ 	__webpack_exports__ = __webpack_require__.O(__webpack_exports__);
/******/ 	
/******/ })()
;
//# sourceMappingURL=jeesl.responsive.bundle.js.map