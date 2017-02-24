/* WN: PLAYLIST MODULE */
(function(WN) {
    var functionality = function($, d3) {

        var templates = {
            "playlist-item": "<div class=\"row playlist-item\" data-playlist-item-id=\"{{playlistId}}\">" +
            "<div class=\"col-lg-1\"><i class=\"playlist-state fa fa-circle-o-notch fa-spin\"></i></div>" +
            "<div class=\"col-lg-10\"><a class=\"playlist-info\" href=\"{{url}}\">{{url}}</a></div>" +
            "<div class=\"col-lg-1\"><button type=\"button\" class=\"remove-playlist-item btn btn-warning\"><i class=\"fa fa-times\"></i></button></div>" +
            "</div>"
        }
        var addUrlButton = $("#add-url-button");
        var addUrlInput = $("#add-url-input");
        var addUrlBeforeHereElement = $("#add-url-before-here");
        var playlistIndex = 0;
        addUrlButton.click(function() {
            var val = addUrlInput.val();
            var currentPlaylistIndex = playlistIndex;
            playlistIndex++;
            addUrlInput.val(undefined);
            addPlaylistItem(currentPlaylistIndex, val);
            $.get("/harvest",{url:val})
                .done(function(d) {
                    var playlistItem = $(".playlist-item[data-playlist-item-id='" + currentPlaylistIndex + "']");
                    playlistItem.attr("data-playlist-item-status", d.status);
                    var playlistStatePanel = $(".playlist-item[data-playlist-item-id='" + currentPlaylistIndex + "'] .playlist-state");
                    var playlistInfoPanel = $(".playlist-item[data-playlist-item-id='" + currentPlaylistIndex + "'] .playlist-info");
                    playlistInfoPanel.attr("href", d.sanitizedUrl);
                    playlistStatePanel.removeClass("fa-spin");
                    playlistStatePanel.removeClass("fa-circle-o-notch");
                    if (d.status == 'SUCCESS') {
                        playlistInfoPanel.text(d.title);
                        playlistStatePanel.addClass("fa-check");
                    } else {
                        playlistStatePanel.addClass("fa-times");
                    }
                });
            refreshRemovePlaylistItemClickBinding();
        });
        var refreshRemovePlaylistItemClickBinding = function() {
            var targets = $(".remove-playlist-item");
            targets.unbind("click");
            targets.click(function() {
                $(this).parents(".playlist-item").remove()
            });
        };
        var addPlaylistItem = function(playlistId, url) {
            var html = Mustache.render(templates["playlist-item"],{playlistId:playlistId, url:url});
            addUrlBeforeHereElement.before(html);
        };
    };
    WN.module.load("playlist", functionality);
})(WN);
