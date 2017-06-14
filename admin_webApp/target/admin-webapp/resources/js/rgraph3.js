/**
 *    RGraph Version 2
 *    graph implementation by Raphael
 *    @author: yanghengfeng
 */
!function (global) {
    //Raphael的arrow扩展
    (!Raphael.fn.rgrapharrow ) && (Raphael.fn.rgrapharrow = function (edge, rgraph) {
        var paper = this, getConnection = function (obj1, obj2) {
                /* get bounding boxes of target and source */
                var bb1 = obj1.getBBox();
                var bb2 = null;
                if (obj2.x) {
                    bb2 = obj2;
                } else {
                    bb2 = obj2.getBBox();
                }

                //adjustx:在IE下getBBox获取的坐标没有算上_viewBox中的部分。
                //直接访问_viewBox不是好的做法，但是Raphael并没有提供接口访问该属性
                //对象getBBox()在IE下xy坐标有问题
                /*var adjustx=(document.all?(paper._viewBox||[0,0])[0]:0),
                 adjusty=(document.all?(paper._viewBox||[0,0])[1]:0);
                 bb1.x = bb1.x+adjustx;
                 bb1.y = bb1.y+adjusty;
                 bb2.x = bb2.x+adjustx;
                 bb2.y = bb2.y+adjusty;*/
                var off1 = 0;
                var off2 = 0;
                /* coordinates for potential connection coordinates from/to the objects */
                var p = [
                    /* NORTH 1 */
                    {x: bb1.x + bb1.width / 2, y: bb1.y - off1},
                    /* SOUTH 1 */
                    {x: bb1.x + bb1.width / 2, y: bb1.y + bb1.height + off1},
                    /* WEST */
                    {x: bb1.x - off1, y: bb1.y + bb1.height / 2},
                    /* EAST  1 */
                    {x: bb1.x + bb1.width + off1, y: bb1.y + bb1.height / 2},
                    /* NORTH 2 */
                    {x: bb2.x + bb2.width / 2, y: bb2.y - off2},
                    /* SOUTH 2 */
                    {x: bb2.x + bb2.width / 2, y: bb2.y + bb2.height + off2},
                    /* WEST  2 */
                    {x: bb2.x - off2, y: bb2.y + bb2.height / 2},
                    /* EAST  2 */
                    {x: bb2.x + bb2.width + off2, y: bb2.y + bb2.height / 2}
                ];

                /* distances between objects and according coordinates connection */
                var d = {}, dis = [];

                /*
                 * find out the best connection coordinates by trying all possible ways
                 */
                /* loop the first object's connection coordinates */
                for (var i = 0; i < 4; i++) {
                    /* loop the seond object's connection coordinates */
                    for (var j = 4; j < 8; j++) {
                        var dx = Math.abs(p[i].x - p[j].x);
                        var dy = Math.abs(p[i].y - p[j].y);
                        if ((i == j - 4) || (((i != 3 && j != 6) || p[i].x < p[j].x)
                            && ((i != 2 && j != 7) || p[i].x > p[j].x)
                            && ((i != 0 && j != 5) || p[i].y > p[j].y)
                            && ((i != 1 && j != 4) || p[i].y < p[j].y))) {
                            dis.push(dx + dy);
                            d[dis[dis.length - 1].toFixed(3)] = [i, j];
                        }
                    }
                }
                var res = dis.length == 0 ? [0, 4] : d[Math.min.apply(Math, dis).toFixed(3)];

                return {x1: p[res[0]].x, y1: p[res[0]].y, x2: p[res[1]].x, y2: p[res[1]].y};
            },
            getPos = function (obj1, obj2) {
                var conn = getConnection(obj1, obj2), fromx = conn.x1, fromy = conn.y1, tox = conn.x2, toy = conn.y2,
                    sign = (tox == fromx) ? 1 : ((tox - fromx) / Math.abs(tox - fromx)),
                    c = (tox == fromx) ? Math.PI / 2 : Math.atan((toy - fromy) / (tox - fromx)),
                    triangleX1 = (tox - Math.cos(c - Math.PI / 12) * 10 * sign).toFixed(1),
                    triangleY1 = (toy - Math.sin(c - Math.PI / 12) * 10 * sign).toFixed(1),
                    triangleX2 = (tox - Math.cos(c + Math.PI / 12) * 10 * sign).toFixed(1),
                    triangleY2 = (toy - Math.sin(c + Math.PI / 12) * 10 * sign).toFixed(1);
                return {
                    linestr: "M" + fromx + "," + fromy + " L" + tox + "," + toy,
                    trianglestr: "M" + tox + "," + toy + " L" + triangleX1 + "," + triangleY1 + " L" + triangleX2 + "," + triangleY2 + " L" + tox + "," + toy,
                    arrowpos: {
                        x: (fromx + tox) / 2,
                        y: (fromy + toy) / 2,
                        transform: ((fromx + tox) / 2) + "," + ((fromy + toy) / 2) + "r" + (c / Math.PI) * 180 + (c > Math.PI / 2 ? ("t8,8") : ("t-8,-8"))
                    }
                }
            },
            pos = getPos(edge.source._shapes, edge.target._shapes),
            line = this.path(pos.linestr).attr(edge.arrowStyle || {}).toBack(),
            triangle = this.path(pos.trianglestr)
                .attr({stroke: "#000", fill: "#000", "stroke-width": 1})
                .attr(edge.arrowStyle || {})
                .toBack(),
            label = this.text(pos.arrowpos.x, pos.arrowpos.y, edge.label || "")
                .attr({fill: "#000", "font-size": "12px"})
                .transform(pos.arrowpos.transform)
                .attr(edge.arrowStyle || {});
        return {
            redraw: function () {
                var pos = getPos(edge.source._shapes, edge.target._shapes);
                line.attr("path", pos.linestr).attr(edge.arrowStyle || {});
                triangle.attr("path", pos.trianglestr).attr(edge.arrowStyle || {});
                label.attr(pos.arrowpos).attr(edge.arrowStyle || {}).transform(pos.arrowpos.transform);
                label.attr({x: pos.arrowpos.x, y: pos.arrowpos.y});
            },
            //高亮箭头时会调用该方法
            highlight: function () {
                triangle.data("_normal", {
                    stroke: triangle.attr("stroke"),
                    fill: triangle.attr("fill")
                });
                triangle.attr({"stroke": "#FF9900", "fill": "#FF9900"});

                line.data("_normal", {
                    stroke: line.attr("stroke"),
                    fill: line.attr("fill")
                });
                line.attr({"stroke": "#FF9900", "fill": "#FF9900"});

                label.data("_normal", {
                    stroke: label.attr("stroke"),
                    fill: label.attr("fill")
                });
                label.attr({"stroke": "#FF9900", "fill": "#FF9900"});
            },
            //清除高亮
            unhighlight: function () {
                triangle.attr(triangle.data("_normal"));
                label.attr(label.data("_normal"));
                line.attr(line.data("_normal"));
            }
        }
    });

    (!Raphael.fn.rgraphnode ) && (Raphael.fn.rgraphnode = function (node) {
        var paper = this,
            text = Raphael.fullfill(node.label, node) || "",
            getSize = function (text) {
                /*var rows = text.split("\n"),h = rows.length*14,w=0;
                 for (var i = rows.length - 1; i >= 0; i--) {
                 var row = rows[i];
                 if(row.length*12 > w ){
                 w = row.length*12;
                 }
                 };
                 return {w:w,h:h};*/

                var s = document.createElement("div"), size = {};
                s.id = "_rgraph_span";
                s.setAttribute("class", "link_btn");
                s.style.position = "absolute";
                s.style.top = "-9999px";
                document.body.appendChild(s);
                s.innerHTML = text.replace(/\n/gm, "<br/>");
                size = {w: s.offsetWidth, h: s.offsetHeight};
                document.body.removeChild(s);
                return size;

            },
            size = getSize(text);
        node.point[0] = node.point[0] || 0;
        node.point[1] = node.point[1] || 0;
        node._shape = paper.rect(node.point[0] - size.w / 2, node.point[1] - size.h / 2, size.w, size.h)
            .attr({stroke: "#ccc", fill: "#ccc", "stroke-width": 1, "cursor": "pointer"})
            .attr(node.rectStyle || {});
        //node._text = paper.text(node.point[0],node.point[1],'1').attr({"cursor":"pointer","font-size":12}).attr(node.textStyle||{});
        return {
            //移动节点时会调用该方法
            move: function (dx, dy) {
                node._shape.transform("...t" + dx + "," + dy);
                //node._text.transform("...t"+dx+","+dy);
            },
            //高亮节点时会调用该方法
            highlight: function () {
                node._shape.data("_normal", {
                    stroke: node._shape.attr("stroke"),
                    fill: node._shape.attr("fill")
                });
                //node._text.data("_normal",{
                //    fill: node._text.attr("fill")
                //});
                node._shape.attr({"stroke": "#FF9900", "fill": "#FFFF66"});
                //node._text.attr({"fill":"#000"});
            },
            //清除高亮
            unhighlight: function () {
                node._shape.attr(node._shape.data("_normal"));
                //node._text.attr(node._text.data("_normal"));
            },
            //更新节点文本
            update: function () {
                var text = Raphael.fullfill(node.label, node) || "", size = getSize(text);
                node._shape.attr({
                    x: node.point[0] - size.w / 2,
                    y: node.point[1] - size.h / 2,
                    width: size.w,
                    height: size.h
                });
                // node._text.attr({text:text});
            }
        }
    });
    //util methods
    var addEventListener = document.all ? (function (ele, eventname, fn) {
            ele.attachEvent("on" + eventname, fn);
        }) : (function (ele, eventname, fn) {
            ele.addEventListener(eventname, fn, false)
        }),
        getEvent = function (e) {
            var evt = window.event ? window.event : e;
            (!evt.stopPropagation) && (evt.stopPropagation = function () {
                evt.cancelBubble = false;
            });
            (!evt.preventDefault) && (evt.preventDefault = function () {
                evt.returnValue = false;
            });
            (!evt.target) && (evt.target = evt.srcElement);
            (!evt.which) && (e.which = e.button);
            return evt;
        },
    //拖拽过程容易产生选中，清除选中对象
        clearSelection = function () {
            if (window.getSelection) {
                if (window.getSelection().empty) {  // Chrome
                    window.getSelection().empty();
                } else if (window.getSelection().removeAllRanges) {  // Firefox
                    window.getSelection().removeAllRanges();
                }
            } else if (document.selection) {  // IE?
                document.selection.empty();
            }
        };

    // RGraph definition
    var RGraph = global.RGraph = function (domId, opts) {
        var me = this;
        me.domId = domId;
        me.canvas = document.getElementById(domId);
        me.width = me.canvas.clientWidth;
        me.height = me.canvas.clientHeight;
        me.paper = Raphael(me.canvas, me.width, me.height);

        me.nodes = [];
        me.runNum = 9;
        me.edges = [];
        me._tempNode = null;
        me.isDrow = 0;
        me.isEdit = false;
        me.last_zoom=1;
        me._bind();
        for (var opt in opts || {}) {
            me[opt] = opts[opt];
        }
        ;

        var rect = me.paper.rect(150, 150, 150, 150);

    };
    RGraph.prototype = {
        // 绑定事件
        _bind: function () {
            var me = this, c = me.canvas, wheel = function (e) {//wheel event
                var evt = getEvent(e), viewbox = me.getViewBox(), delta = evt.wheelDelta ? (evt.wheelDelta / 120) : (-evt.detail / 3),
                    basePoint = {x: viewbox.x + evt.clientX, y: viewbox.y + evt.clientY};
                me.setZoom(me.getZoom() * (1 + delta * 0.1), basePoint);
                evt.stopPropagation();
                evt.preventDefault();
                return false;
            }
            try {
                addEventListener(c, "mousewheel", wheel);//IE+webkit
                addEventListener(c, "DOMMouseScroll", wheel);//firefox
            } catch (e) {
            }

            //drag event
            me.canvasdragging = false;
            me.nodedragging = false;
            addEventListener(c, "mousedown", function (e) {
                var evt = getEvent(e), drag = false, viewbox = me.getViewBox(),
                    node = me.paper.getElementByPoint(evt.clientX, evt.clientY);
                if (e.which != 1) {//左键结束
                    me.isDrow = 3;
                    me.reset();
                    me.draw();
                    return;
                }
                if (evt.target.parentNode == me.canvas) {
                    if (!me.isEdit) {
                        // 拖拽图片
                        me.canvasdragging = {x: evt.clientX, y: evt.clientY, timestamp: (new Date()).getTime()};
                        me.canvas.style.cursor = "move";
                        return;
                    }
                    // 生成节点并绘制连线
                    me.runNum++;

                    var offset = $("#cc").css('left').replace('px', '');
                    var offsettop = $("#cc").css('top').replace('px', '');
                    var evt = getEvent(e);

                    //计算偏移量
                    var wi=$("#cc").width();
                    var h=$("#cc").height();
                    var cw=$('#canvas').width();
                    var ch=$('#canvas').height();
                    var center_left=(-(wi-cw)/2);  // 居中时候的偏移量
                    var center_top=(-(h-ch)/2);

                    var top = parseInt($("#cc").css("top").replace('px', '') - center_top);
                    var left = parseInt($("#cc").css("left").replace('px', '') - center_left);
                    // 移动后偏移量计算
                    var mouseDownX = evt.clientX-left;
                    var mouseDownY = evt.clientY-top;

                    var _aNode = {nodeId: me.runNum, label: "节点3", layoutPosX: mouseDownX, layoutPosY: mouseDownY};
                    me.addNode(_aNode);
                    me._tempNode = _aNode;
                    me.isDrow = 1;

                    // 将前一个node 和当前node 进行连接
                    if (me.nodes.length > 1) {
                        var _edge = {};
                        _edge.source = me.nodes[me.nodes.length - 2];
                        _edge.target = _aNode;
                        me.addEdge(_edge);
                    }
                } else {
                    me.isDrow = 0;
                }

                if (node && node.data("nodeId")) {//拖动节点
                    var nodeId = node.data("nodeId"), draggingnode = me.getNode(nodeId);
                    me.nodedragging = {
                        x: evt.clientX,
                        y: evt.clientY,
                        node: draggingnode,
                        _x: evt.clientX,
                        _y: evt.clientY,
                        timestamp: (new Date()).getTime()
                    };
                    draggingnode._shapes.attr({"cursor": "move"}).toFront();
                    (me.onnodestartdrag || function () {
                    }).call(me, evt, draggingnode);
                }
            });
            addEventListener(c, "mousemove", function (e) {
                var evt = getEvent(e), viewbox = me.getViewBox(), now = (new Date()).getTime(),
                    hovernode = me.paper.getElementByPoint(evt.clientX, evt.clientY);
                clearSelection();

                //记录上次canvasdragging触发的时间戳，减速canvasdragging触发的频率
                if (me.canvasdragging && now - me.canvasdragging.timestamp > 1) {
                    var x = parseInt((me.canvasdragging.x - evt.clientX) / me.getZoom()),
                        y = parseInt((me.canvasdragging.y - evt.clientY) / me.getZoom()),
                        viewbox = me.getViewBox();
                    me.canvasdragging = {x: evt.clientX, y: evt.clientY};
                    me.setViewBox(viewbox.x + x, viewbox.y + y, viewbox.w, viewbox.h);
                    me.canvasdragging.timestamp = now;

                    // 移动图片
                    var top = parseInt($("#cc").css("top").replace('px', '')) - y;
                    var left = parseInt($("#cc").css("left").replace('px', '')) - x;
                    $("#cc").css('top', top + 'px');
                    $("#cc").css('left', left + 'px');
                }
                if (me.nodedragging && now - me.nodedragging.timestamp > 1) {

                    var nodedragging = me.nodedragging,
                        dx = ((evt.clientX - nodedragging.x) / me.getZoom()).toFixed(3),
                        dy = ((evt.clientY - nodedragging.y) / me.getZoom()).toFixed(3),
                        node = nodedragging.node,
                        edges = me.getNodeEdges(node.nodeId);
                    console.log(evt.clientX+","+evt.clientY+" "+dx+","+dy);
                    node.point=[evt.clientX,evt.clientY];
                    node._node.move(dx, dy);
                    for (var i = edges.length - 1; i >= 0; i--) {
                        edges[i].arrow.redraw();
                    }
                    ;
                    me.nodedragging.timestamp = now;
                    me.nodedragging.x = evt.clientX;
                    me.nodedragging.y = evt.clientY;
                    (me.onnodedragging || function () {
                    }).call(me, evt, node);
                }
                //鼠标放在节点上，高亮节点和相关的箭头
                //排除现有行为
                if (me.canvasdragging || me.nodedragging) {
                    return;
                }
                if (hovernode && hovernode.data("nodeId")) {
                    me.hovernode = me.getNode(hovernode.data("nodeId"));
                    if (me._lastHoverNode != me.hovernode) {//进入node
                        if (me._lastHoverNode) { //触发之前节点的unhighlight
                            var edges = me.getNodeEdges(me._lastHoverNode.nodeId);
                            me._lastHoverNode._node.unhighlight();
                            for (var i = edges.length - 1; i >= 0; i--) {
                                edges[i].arrow.unhighlight();
                            }
                            ;
                            (me.onnodemouseout || function () {
                            }).call(me, evt, me._lastHoverNode);
                        }
                        me.hovernode._node.highlight();
                        var edges = me.getNodeEdges(me.hovernode.nodeId);
                        for (var i = edges.length - 1; i >= 0; i--) {
                            edges[i].arrow.highlight();
                        }
                        ;
                        (me.onnodemouseover || function () {
                        }).call(me, evt, me.hovernode);
                    }
                    me._lastHoverNode = me.hovernode;
                } else {
                    if (me._lastHoverNode) {
                        var edges = me.getNodeEdges(me._lastHoverNode.nodeId);
                        me._lastHoverNode._node.unhighlight();
                        for (var i = edges.length - 1; i >= 0; i--) {
                            edges[i].arrow.unhighlight();
                        }
                        ;
                        (me.onnodemouseout || function () {
                        }).call(me, evt, me._lastHoverNode);
                        me._lastHoverNode = null;
                    }
                }
            });
            addEventListener(c, "mouseup", function (e) {
                var evt = getEvent(e);
                clearSelection();

                if (me.canvasdragging) {
                    me.canvasdragging = false;
                    me.canvas.style.cursor = "auto";
                }
                if (me.nodedragging) {
                    var nodedragging = me.nodedragging,
                        node = nodedragging.node;
                    node._shapes.attr({"cursor": "pointer"});
                    if (evt.clientX == nodedragging._x && evt.clientY == nodedragging._y) {//如果没有拖动位置，说明是点击
                        if (typeof(me.onnodeclick) == "function") me.onnodeclick.call(me, evt, node);
                    }
                    (me.onnodeenddrag || function () {
                    }).call(me, evt, node);
                    me.nodedragging = false;

                }
            });
        },
        layout: function () {
            var me = this;
            //计算每个节点的坐标
            for (var i = me.nodes.length - 1; i >= 0; i--) {
                var node = me.nodes[i];
                node.point = [node.layoutPosX, node.layoutPosY];
            }
            ;
        },
        _drawNode: function (node) {
            var me = this,
                drawNodeFn = typeof me.drawNodeFn == "function" ? me.drawNodeFn : me.paper[me.drawNodeFn || "rgraphnode"];
            me.paper.setStart();
            node._node = drawNodeFn.call(me.paper, node);
            node._shapes = me.paper.setFinish();
            node._shapes.data("nodeId", node.nodeId);//节点绘制的所有元素都标记上nodeId
        },
        _drawEdge: function (edge) {
            var me = this;
            me.paper.setStart();
            edge.arrow = me.paper.rgrapharrow(edge, me);
            //edge._shapes = me.paper.setFinish();
        },
        draw: function () {
            var me = this;
            me.paper.clear();
            //绘制节点
            for (var i = me.nodes.length - 1; i >= 0; i--) {
                me._drawNode(me.nodes[i]);
            }
            ;
            //画箭头
            for (var i = me.edges.length - 1; i >= 0; i--) {
                me._drawEdge(me.edges[i]);

            }
            ;
        },
        removeNode: function (nodeId) {
            var me = this, node = me.getNode(nodeId), edges = me.getNodeEdges(nodeId);
            if (!node && !edges) return false;
            for (var i = edges.length - 1; i >= 0; i--) {
                edges[i]._shapes && (edges[i]._shapes.remove());
            }
            ;
            node._shapes && (node._shapes.remove());
            for (var i = me.edges.length - 1; i >= 0; i--) {
                var edge = me.edges[i];
                if (edge.source == node || edge.target == node) {
                    me.edges.splice(i, 1);
                }
            }
            for (var i = me.nodes.length - 1; i >= 0; i--) {
                if (me.nodes[i].nodeId == nodeId) {
                    me.nodes.splice(i, 1);
                    break;
                }
            }
            return true;
        },
        updateNode: function (nodeId) {
            var me = this, node = me.getNode(nodeId), edges = me.getNodeEdges(nodeId);
            if (!node && !edges) return false;
            node._node.update();
            for (var i = edges.length - 1; i >= 0; i--) {
                edges[i].arrow.redraw();
            }
            ;
        },
        addNode: function (node) {
            var me = this, vb = me.getViewBox();
            if (!node.nodeId || me.getNode(node.nodeId)) return false;
            node.point = [node.layoutPosX, node.layoutPosY];
            me.nodes.push(node);
            me._drawNode(node);
            return true;
        },
        existEdge: function (sourceNodeId, targetNodeId) {
            for (var i = this.edges.length - 1; i >= 0; i--) {
                var edge = this.edges[i];
                if (edge.source.nodeId == sourceNodeId && edge.target.nodeId == targetNodeId) {
                    return edge;
                }
            }
            return false;
        },
        addEdge: function (edge) {
            var me = this;
            edge.source = edge.source.nodeId ? edge.source : me.getNode(edge.source);
            edge.target = edge.target.nodeId ? edge.target : me.getNode(edge.target);
            if (!edge.target || !edge.source || !edge.source.nodeId || !edge.target.nodeId) {
                return false;
            }
            var existedEdge = me.existEdge(edge.source.nodeId, edge.target.nodeId);
            if (existedEdge) {
                existedEdge.arrowStyle = edge.arrowStyle || {};
                existedEdge.arrow.unhighlight();
                existedEdge.arrow.redraw();
            } else {
                me._drawEdge(edge);
                me.edges.push(edge);
            }
            return true;
        },
        loadData: function (data) {
            var me = this;
            //prepare layout
            me.nodes = [];
            me.edges = [];
            for (var nodeid in data.nodes) {
                data.nodes[nodeid].nodeId = nodeid;
                me.nodes.push(data.nodes[nodeid]);
            }
            for (var i = data.edges.length - 1; i >= 0; i--) {
                var edge = data.edges[i];
                edge.source = data.nodes[edge.source] || edge.source;
                edge.target = data.nodes[edge.target] || edge.target;
                me.edges.push(edge);
            }
            ;
            me.reset();
            me.layout();
            me.draw();
        },
        relayout: function () {
            var me = this, graphData = {nodes: [], edges: []};
            for (var i = me.edges.length - 1; i >= 0; i--) {
                var edge = me.edges[i], newEdge = {};
                for (var k in edge) {
                    newEdge[k] = edge[k];
                }
                newEdge.source = edge.source.nodeId;
                newEdge.target = edge.target.nodeId;
                graphData.edges.push(edge);
            }
            ;

            for (var i = me.nodes.length - 1; i >= 0; i--) {
                var node = me.nodes[i];
                graphData.nodes[node.nodeId] = node;
            }
            ;
            me.loadData(graphData);
        },
        getNode: function (nodeId) {
            var me = this, node = null;
            for (var i = me.nodes.length - 1; i >= 0; i--) {
                if (me.nodes[i].nodeId == nodeId) {
                    node = me.nodes[i];
                    break;
                }
            }
            ;
            return node;
        },
        //根据nodeId获取Edges
        getNodeEdges: function (nodeId) {
            var me = this, edges = [], node = me.getNode(nodeId);
            if (!node) return edges;
            for (var i = me.edges.length - 1; i >= 0; i--) {
                var edge = me.edges[i];
                if (edge.source == node || edge.target == node) {
                    edges.push(edge);
                }
            }
            ;
            return edges;
        },
        setViewBox: function (x, y, w, h, fit) {
            var me = this;
            me.x = x;
            me.y = y;
            me.w = w;
            me.h = h;
            return me.paper.setViewBox(x, y, w, h, fit);
        },
        getViewBox: function () {
            return {x: this.x, y: this.y, w: this.w, h: this.h};
        },
        getZoom: function () {
            return this.zoom;
        },
        reset: function () {
            this.zoom = 1;
            this.setViewBox(0, 0, this.width, this.height);
        },
        setZoom: function (zoom, basePoint) {
            // var me = this, viewbox = me.getViewBox();
            // if (!basePoint) {
            //     basePoint = {x: viewbox.x + viewbox.w / 2, y: viewbox.y + viewbox.h / 2};
            // }
            // me.last_zoom=me.zoom;  // 设置上一次的
            // me.zoom = zoom;
            //
            // viewbox.x += parseInt((basePoint.x - viewbox.x) * (1 / me.zoom - 1 / zoom));
            // viewbox.y += parseInt((basePoint.y - viewbox.y) * (1 / me.zoom - 1 / zoom));
            // viewbox.w = parseInt(me.paper.width / me.zoom);
            // viewbox.h = parseInt(me.paper.height / me.zoom);
            // me.setViewBox(viewbox.x, viewbox.y, viewbox.w, viewbox.h);


            // var cw=$('#canvas').width();
            // var ch=$('#canvas').height();
            //
            // for(var i=0;i<this.nodes.length;i++){
            //     var _tempNode=this.nodes[i];
            //
            //     // 先换算到中心坐标系
            //     var x_=_tempNode.point[0]-cw/2;
            //     var y_= ch/2-_tempNode.point[1];
            //
            //     // 计算比例
            //     // 需要根据之前的缩放比例换算到  不缩放情况下的位置
            //     x_=(x_/me.last_zoom)*zoom;
            //     y_=(y_/me.last_zoom)*zoom;
            //
            //     // 换算到css 坐标系
            //     x_=parseInt(cw/2+x_);
            //     y_=parseInt(ch/2-y_);
            //
            //     _tempNode.point[0]=x_;
            //     _tempNode.point[1]=y_;
            //     console.log(x_+','+y_);
            //     this.updateNode(_tempNode.nodeId);
            // }

            //$("#cc").css('-webkit-transform','scale('+zoom+')');
        }, zoomImg: function (o, vz) {
            var zoom = parseInt(vz * 100);
            if (zoom > 0) {
                o.style.zoom = zoom + '%';
                // 缩微图片 同时让图像居中

                var wi=parseInt($("#cc").width()*(vz*100/100));
                var h=parseInt($("#cc").height()*(vz*100/100));

                var cw=$('#canvas').width();
                var ch=$('#canvas').height();

                // 居中对齐图片
                $("#cc").css('left',(-(wi-cw)/2)+'px');
                $("#cc").css('top',(-(h-ch)/2)+'px');
            }
        }, beginEdit: function (isEdit) {
            this.isEdit = isEdit;
            console.log('isEdit:' + this.isEdit);
        },getNodes:function(){
            return this.nodes;
        }
    };
}(window)