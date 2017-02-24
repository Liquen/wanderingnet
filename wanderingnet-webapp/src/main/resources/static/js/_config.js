/* WN: CONFIG MODULE */

WN = (function($, d3) {
    $(function () {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        if (token && header) {
            $(document).ajaxSend(function (e, xhr, options) {
                xhr.setRequestHeader(header, token);
            });
        }
    });
    var unloaded = [];
    var loadNotReady = function(moduleName, moduleFunctionality) {
        unloaded.push({moduleName: moduleName, moduleFunctionality: moduleFunctionality});
    };

    var wn = {
        module : {
            load : loadNotReady
        }
    };

    $(document).ready(function() {

        var html = $("html");
        var modules = html.data("req-modules").split(",");

        wn.module.load = function (moduleName, moduleFunctionality, requiresReady) {
            if (modules.indexOf(moduleName) >= 0) {
                console.log("Loading module " + moduleName);
                moduleFunctionality($, d3);
            }
        };
        for (var i = 0; i < unloaded.length; i++)  {
            var unloadedModule = unloaded[i];
            wn.module.load(unloadedModule.moduleName, unloadedModule.moduleFunctionality);
        }
        unloaded = [];
    });

    return wn;

})(jQuery, d3);