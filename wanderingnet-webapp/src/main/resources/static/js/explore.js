/* WN: EXPLORE MODULE */
(function(WN) {
    var functionality = function($, d3) {
        var playlistContainer = $("#playlist-slide-container");
        var mapCanvasContainer = $("#map-canvas-container");
        var contentSlide = $("#content-slide-container");

        $("#my-profile").click(function() {
            if (playlistContainer.hasClass("wn-slided")) {
                playlistContainer.removeClass("wn-slided");
                if (mapCanvasContainer.hasClass("wn-both-slided")) {
                    mapCanvasContainer.removeClass("wn-both-slided");
                    mapCanvasContainer.addClass("wn-left-slided");
                } else {
                    mapCanvasContainer.removeClass("wn-right-slided");
                }
            } else {
                playlistContainer.addClass("wn-slided");
                if (mapCanvasContainer.hasClass("wn-left-slided")) {
                    mapCanvasContainer.addClass("wn-both-slided");
                    mapCanvasContainer.removeClass("wn-left-slided");
                } else {
                    mapCanvasContainer.addClass("wn-right-slided");
                }
            }
        });

        var contentTitle = $("#content-title");
        var contentText = $("#content-text");
        var contentClose = $("#content-slide-close");
        contentClose.click(function() {
            contentSlide.removeClass("wn-slided");
            if (mapCanvasContainer.hasClass("wn-both-slided")) {
                mapCanvasContainer.removeClass("wn-both-slided");
                mapCanvasContainer.addClass("wn-right-slided");
            } else {
                mapCanvasContainer.removeClass("wn-left-slided");
            }
        });
        $(".content-tag").click(function() {
            var $this = $(this);
            if ($this.hasClass("selected")) {
                $this.removeClass("selected");
            } else {
                $this.addClass("selected");
            }
            var tagId = $this.data("tag-id");
            var docName = $("#content-title").text();
            var docUrl = $("#content-title").attr("href");
            $.post("/explore/wiki/tag", {tagId:tagId,docName:docName,docUrl:docUrl});
        });
        $(document).on("map-category:show", function (e, data) {
            $(".content-tag").removeClass("selected");
            if (!contentSlide.hasClass("wn-slided")) {
                contentSlide.addClass("wn-slided");
                if (mapCanvasContainer.hasClass("wn-right-slided")) {
                    mapCanvasContainer.addClass("wn-both-slided");
                    mapCanvasContainer.removeClass("wn-right-slided");
                } else {
                    mapCanvasContainer.addClass("wn-left-slided");
                }
            }
            contentTitle.attr("href", data.url);
            var t = data.title;
            if (data.type == 'page') {
                t = "Page: " + t;
            } else {
                t = t.replace("Category:", "Category: ");
            }
            contentTitle.text(t);
            var cleanupContent = function() {
                contentText.empty();
            };
            if (data.type == 'page') {
                $.get("/explore/wiki/extract", {title: data.title})
                    .done(function (d) {
                        cleanupContent();
                        contentText.text(d.extract);
                    });
            } else {
                $.get("/explore/wiki/subcategories", {subcategory: data.title})
                    .done(function (d) {
                        cleanupContent();
                        for (var i = 0; i < d.categories.length; i++) {
                            contentText.append("<p>" + d.categories[i] + "</p>");
                        }
                    });
            }
            $.get("/explore/wiki/tags", {docName:t})
                .done(function(tags) {
                    for (var tagId in tags) {
                        var count = tags[tagId];
                        var tag = $("#tag-" + tagId);
                        tag.find(".tag-count").text(count);
                    }
                });

            $.get("/explore/my/wiki/tags", {docName:t})
                .done(function(tags) {
                    for (var i = 0; i < tags.length; i++) {
                        $("#tag-" + tags[i].id).addClass("selected");
                    }
                });


        });
    };
    WN.module.load("explore", functionality);
})(WN);
