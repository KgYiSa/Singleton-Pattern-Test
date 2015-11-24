(function() {
    if (window.tcsEditor) {
        return;
    }

    window.tcsEditor = (function($) {
        var editor = {};

        editor.init = function() {
            var workarea = $('#workarea'),
                rulers = $('#tcs-rulers'),
                ruler_corner = $('');
        };

        return editor;
    }(jQuery));

    // Run init once DOM is loaded
    $(tcsEditor.init);
}());