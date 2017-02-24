(function() {
    WN.common = {

    };

    WN.common.isNumber = function(n) {
        return !isNaN(parseFloat(n)) && isFinite(n);
    };
    WN.common.getActualWidth = function() {
        return window.innerWidth ||
            document.documentElement.clientWidth ||
            document.body.clientWidth ||
            document.body.offsetWidth;
    };
})(WN, jQuery, d3);