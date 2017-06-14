var labelType, useGradients, nativeTextSupport, animate;

(function () {
    var ua = navigator.userAgent,
        iStuff = ua.match(/iPhone/i) || ua.match(/iPad/i),
        typeOfCanvas = typeof HTMLCanvasElement,
        nativeCanvasSupport = (typeOfCanvas == 'object' || typeOfCanvas == 'function'),
        textSupport = nativeCanvasSupport
            && (typeof document.createElement('canvas').getContext('2d').fillText == 'function');
    //I'm setting this based on the fact that ExCanvas provides text support for IE
    //and that as of today iPhone/iPad current text support is lame
    labelType = (!nativeCanvasSupport || (textSupport && !iStuff)) ? 'Native' : 'HTML';
    nativeTextSupport = labelType == 'Native';
    useGradients = nativeCanvasSupport;
    animate = !(iStuff || !nativeCanvasSupport);
})();

var Log = {
    Elem: false,
    write: function (text) {
        if (!this.elem) {
            this.elem = document.getElementById('log');
        }
        if (this.elem) {
            this.elem.innerHTML = text;
            this.elem.style.left = (500 - this.elem.offsetWidth / 2) + 'px';
        }
    }
};


function init(data) {
    //init data
    var json =data;
    //end
    //init Spacetree
    //Create a new ST instance
    var st = new $jit.ST({
        //id of viz container element
        injectInto: 'infovis',
        //set duration for the animation
        duration: 200,
        //set animation transition type
        transition: $jit.Trans.Quart.easeInOut,
        //set distance between node and its children
        levelDistance: 60,
        siblingOffset: 80,
        //subtreeOffset: 80,
        //enable panning
        Navigation: {
            enable: true,
            panning: true
        },
        orientation: 'top',
        //set node and edge styles
        //set overridable=true for styling individual
        //nodes or edges
        Node: {
            height: 78,
            width: 163,
            type: 'rectangle',
            //color: '#aaa',
            overridable: true
        },

        Edge: {
            type: 'bezier',
            overridable: true
        },

        onBeforeCompute: function (node) {
            Log.write("loading " + node.name);
        },

        onAfterCompute: function () {
            Log.write("done");
        },

        //This method is called on DOM label creation.
        //Use this method to add event handlers and styles to
        //your node.
        onCreateLabel: function (label, node) {
            label.id = node.id;
            var d=node.data;
            var cc = '<div class="tt"><ul class="tree_m radio">' +
                '<li class="tree_a"><span>(' + d.userId + ')<i class="c_hide">用户ID</i></span></li>' +
                '<li class="tree_b"><span>级别：' + d.proName +'<i class="c_hide">产品级别</i></span></li>' +
                '<li class="tree_c"><span>' + d.resultsLeft + '(左)<i class="c_hide">左区业绩</i></span><span>' + d.resultsRight + '(右)<i class="c_hide">右区业绩</i></span></li>' +
                '<li class="tree_b"><span>' + d.refereeId + '(推荐人)<i class="c_hide">推荐人</i></span></li>' +
                '</ul></div>';
            label.innerHTML = cc;//node.name;
            label.onclick = function () {
                if (node._depth == 2 && node.data.leftId != null) {
                    $("#userId").val(node.data.userId);
                    getData();
                }
                st.onClick(node.id);
            };
            //set label styles
            var style = label.style;
            //style.width = 60 + 'px';
            //style.height = 17 + 'px';
            style.cursor = 'pointer';
            style.color = '#333';
            style.fontSize = '0.8em';
            style.textAlign = 'center';
            //style.paddingTop = '3px';
            style.border = '1px solid #ddd';
        },

        //This method is called right before plotting
        //a node. It's useful for changing an individual node
        //style properties before plotting it.
        //The data properties prefixed with a dollar
        //sign will override the global node style properties.
        onBeforePlotNode: function (node) {
            //add some color to the nodes in the path between the
            //root node and the selected node.
            if (node.selected) {
               // node.data.$color = "#ff7";
            }
            else {
                delete node.data.$color;
                //if the node belongs to the last plotted level
                if (!node.anySubnode("exist")) {
                    //count children number
                    var count = 0;
                    node.eachSubnode(function (n) {
                        count++;
                    });
                    //assign a node color based on
                    //how many children it has
                   // node.data.$color = ['#aaa', '#baa', '#caa', '#daa', '#eaa', '#faa'][count];
                }
            }
        },

        //This method is called right before plotting
        //an edge. It's useful for changing an individual edge
        //style properties before plotting it.
        //Edge data proprties prefixed with a dollar sign will
        //override the Edge global style properties.
        onBeforePlotLine: function (adj) {
            if (adj.nodeFrom.selected && adj.nodeTo.selected) {
                adj.data.$color = "#eed";
                adj.data.$lineWidth = 3;
            }
            else {
                delete adj.data.$color;
                delete adj.data.$lineWidth;
            }
        }
    });
    //load json data
    st.loadJSON(json);
    //compute node positions and layout
    st.compute();
    //optional: make a translation of the tree
    st.geom.translate(new $jit.Complex(-200, 0), "current");
    //emulate a click on the root node.
    st.onClick(st.root);
    //end
}

