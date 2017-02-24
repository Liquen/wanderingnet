/* WN: EXPLORE MODULE */
(function(WN) {
    var functionality = function($, d3) {
        var typingTimer;                //timer identifier
        var doneTypingInterval = 2000;  //time in ms, 5 second for example
        var $input = $('#search-box').find('input');

        var doneTyping = function() {
            var text = $input.val().toLowerCase();
            if (!text || text.length == 0) {
                d3.selectAll("#map-canvas svg g g.node")
                    .classed("search-found", false);
            } else {
                d3.selectAll("#map-canvas svg g g.node")
                    .classed("search-found", function (d, i) {
                        return (d.title && d.title.toLowerCase().indexOf(text) > -1) ||
                            (d.id && d.id.toLowerCase().indexOf(text) > -1);
                    });
            }
        };
//on keyup, start the countdown
        $input.on('keyup', function () {
            clearTimeout(typingTimer);
            typingTimer = setTimeout(doneTyping, doneTypingInterval);
        });

//on keydown, clear the countdown
        $input.on('keydown', function () {
            clearTimeout(typingTimer);
        });
        
        var loadDataset = function(fileName) {
            var canvas = $("#map-canvas");
            canvas.find("svg").remove();

            var file = "/static/data/" + fileName;
            var enable_text = true;

            var w = WN.common.getActualWidth();
            var h = 800;

            var focus_node = null, highlight_node = null;

            var text_center = false;
            var outline = false;

            var min_score = 0;
            var max_score = 1000;

            var color = d3.scale.linear()
                .domain([min_score, max_score])
                .range(["gray", "red"]);

            var highlight_color = "purple";
            var highlight_trans = 0.1;

            var size = d3.scale.pow().exponent(1)
                .domain([1,100])
                .range([8,24]);

            var force = d3.layout.force()
                .linkDistance(60)
                .charge(-300)
                .size([w,h]);
            var drag = force.drag()
                .on("dragstart", dragstart);;

            var default_node_color = "#ccc";
//var default_node_color = "rgb(3,190,100)";
            var default_link_color = "#888";
            var nominal_base_node_size = 8;
            var nominal_text_size = 12;
            var max_text_size = 24;
            var nominal_stroke = 1.5;
            var max_stroke = 4.5;
            var max_base_node_size = 36;
            var min_zoom = 0.1;
            var max_zoom = 7;
            var svg = d3.select("#map-canvas").append("svg");
            var zoom = d3.behavior.zoom().scaleExtent([min_zoom,max_zoom]);
            svg.on("dblclick.zoom", null);
            var g = svg.append("g");
            svg.style("cursor","move");

            d3.json(file, function(error, graph) {

                var linkedByIndex = {};
                graph.links.forEach(function(d) {
                    linkedByIndex[d.source + "," + d.target] = true;
                });

                function isConnected(a, b) {
                    return linkedByIndex[a.index + "," + b.index] || linkedByIndex[b.index + "," + a.index] || a.index == b.index;
                }

                force
                    .nodes(graph.nodes)
                    .links(graph.links)
                    .start();

                var link = g.selectAll(".link")
                    .data(graph.links)
                    .enter().append("line")
                    .attr("class", "link")
                    .style("stroke-width",nominal_stroke)
                    .style("stroke", function(d) {
                        if (WN.common.isNumber(d.history_size) && d.history_size>=0) return color(d.history_size);
                        else return default_link_color; });


                var node = g.selectAll(".node")
                    .data(graph.nodes)
                    .enter().append("g")
                    .attr("class", "node")
                    .call(drag);

                node.on("dblclick", dblclick);

                /*
                 node.on("dblclick.zoom", function(d) { d3.event.stopPropagation();
                 var dcx = (window.innerWidth/2-d.x*zoom.scale());
                 var dcy = (window.innerHeight/2-d.y*zoom.scale());
                 zoom.translate([dcx,dcy]);
                 g.attr("transform", "translate("+ dcx + "," + dcy  + ")scale(" + zoom.scale() + ")");
                 });
                 */
                var tocolor = "fill";
                var towhite = "stroke";
                if (outline) {
                    tocolor = "stroke";
                    towhite = "fill";
                }

                var circle = node.append("path")
                    .attr("d", d3.svg.symbol()
                        .size(function(d) { return Math.PI*Math.pow(size(d.size)||nominal_base_node_size,2); })
                        .type(function(d) { return d.type; }))

                    .style(tocolor, function(d) {
                        if (WN.common.isNumber(d.history_size) && d.history_size>=0) return color(d.history_size);
                        else return default_node_color; })
                    //.attr("r", function(d) { return size(d.size)||nominal_base_node_size; })
                    .style("stroke-width", nominal_stroke)
                    .style(towhite, "white");


                if (enable_text) {
                    var text = g.selectAll(".text")
                        .data(graph.nodes)
                        .enter().append("text")
                        .attr("dy", ".35em")
                        .style("font-size", nominal_text_size + "px")

                    if (text_center) {
                        text.text(function (d) {
                            return d.title;
                        }).style("text-anchor", "middle");
                    } else {
                        text.attr("dx", function (d) {
                            return (size(d.size) || nominal_base_node_size);
                        }).text(function (d) {
                            return '\u2002' + d.title;
                        });
                    }

                }
                node.on("mouseover", function(d) {
                    set_highlight(d);
                })
                    .on("mousedown", function(d) { d3.event.stopPropagation();
                        focus_node = d;
                        set_focus(d);
                        $(document).trigger("map-category:show", [{title: d.title, type: d.type, url: d.url, id: d.id}]);
                        if (highlight_node === null) set_highlight(d)

                    }	).on("mouseout", function(d) {
                        exit_highlight();

                    }	);

                d3.select(window).on("mouseup",
                    function() {
                        if (focus_node!==null)
                        {
                            focus_node = null;
                            if (highlight_trans<1)
                            {

                                circle.style("opacity", 1);
                                if (enable_text) text.style("opacity", 1);
                                link.style("opacity", 1);
                            }
                        }

                        if (highlight_node === null) exit_highlight();
                    });

                function exit_highlight()
                {
                    highlight_node = null;
                    if (focus_node===null)
                    {
                        svg.style("cursor","move");
                        if (highlight_color!="white")
                        {
                            circle.style(towhite, "white");
                            if (enable_text) text.style("visibility", "hidden");
                            link.style("stroke", function(o) {return (WN.common.isNumber(o.history_size) && o.history_size>=0)?color(o.history_size):default_link_color});
                        }

                    }
                }

                function set_focus(d)
                {
                    if (highlight_trans<1)  {
                        circle.style("opacity", function(o) {
                            return isConnected(d, o) ? 1 : highlight_trans;
                        });

                        if (enable_text) text.style("opacity", function(o) {
                            return isConnected(d, o) ? 1 : highlight_trans;
                        });

                        link.style("opacity", function(o) {
                            return o.source.index == d.index || o.target.index == d.index ? 1 : highlight_trans;
                        });
                    }
                }


                function set_highlight(d)
                {
                    svg.style("cursor","pointer");
                    if (focus_node!==null) d = focus_node;
                    highlight_node = d;

                    if (highlight_color!="white")
                    {
                        circle.style(towhite, function(o) {
                            return isConnected(d, o) ? highlight_color : "white";});
                        if (enable_text) text.style("visibility", function(o) {
                            return isConnected(d, o) ? "visible" : "hidden";});
                        link.style("stroke", function(o) {
                            return o.source.index == d.index || o.target.index == d.index ? highlight_color : ((WN.common.isNumber(o.history_size) && o.history_size>=0)?color(o.history_size):default_link_color);

                        });
                    }
                }


                zoom.on("zoom", function() {
                    console.log("zoom");

                    var stroke = nominal_stroke;
                    if (nominal_stroke*zoom.scale()>max_stroke) stroke = max_stroke/zoom.scale();
                    link.style("stroke-width",stroke);
                    circle.style("stroke-width",stroke);

                    var base_radius = nominal_base_node_size;
                    if (nominal_base_node_size*zoom.scale()>max_base_node_size) base_radius = max_base_node_size/zoom.scale();
                    circle.attr("d", d3.svg.symbol()
                        .size(function(d) { return Math.PI*Math.pow(size(d.size)*base_radius/nominal_base_node_size||base_radius,2); })
                        .type(function(d) { return d.type; }));

                    if (enable_text) {
                        //circle.attr("r", function(d) { return (size(d.size)*base_radius/nominal_base_node_size||base_radius); })
                        if (!text_center) text.attr("dx", function (d) {
                            return (size(d.size) * base_radius / nominal_base_node_size || base_radius);
                        });

                        var text_size = nominal_text_size;
                        if (nominal_text_size * zoom.scale() > max_text_size) text_size = max_text_size / zoom.scale();
                        text.style("font-size", text_size + "px");
                    }

                    g.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
                });

                svg.call(zoom);
                svg.on("dblclick.zoom", null);

                resize();

                force.on("tick", function() {

                    node.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
                    if (enable_text) text.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });

                    link.attr("x1", function(d) { return d.source.x; })
                        .attr("y1", function(d) { return d.source.y; })
                        .attr("x2", function(d) { return d.target.x; })
                        .attr("y2", function(d) { return d.target.y; });

                    node.attr("cx", function(d) { return d.x; })
                        .attr("cy", function(d) { return d.y; });
                });

                function resize() {
                    var width = window.innerWidth, height = window.innerHeight;
                    svg.attr("width", width).attr("height", height);

                    force.size([force.size()[0]+(width-w)/zoom.scale(),force.size()[1]+(height-h)/zoom.scale()]).resume();
                    w = width;
                    h = height;
                }

            });
            return svg;
        };
        $(document).ready(function() {
            loadDataset($("body").data("map-file-name"));
        });

        function dblclick(d) {
            d3.select(this).classed("fixed", d.fixed = false);
        }

        function dragstart(d) {
            d3.select(this).classed("fixed", d.fixed = true);
        }
    };
    WN.module.load("explore-maps", functionality);
})(WN);
