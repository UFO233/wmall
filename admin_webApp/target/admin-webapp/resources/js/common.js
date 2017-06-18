var Comm = Comm || {};
Comm.isIE6 = !window.XMLHttpRequest;	//ie6

//重设表格宽高
Comm.resizeGrid = function (grid_id, content_id) {
    $(grid_id).jqGrid('setGridWidth', $(content_id).width());
};

/*获取URL参数值*/
Comm.getRequest = Comm.urlParam = function () {
    var param, url = location.search, theRequest = {};
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for (var i = 0, len = strs.length; i < len; i++) {
            param = strs[i].split("=");
            theRequest[param[0]] = decodeURIComponent(param[1]);
        }
    }
    return theRequest;
};
/*
 通用post请求，返回json
 url:请求地址， params：传递的参数{...}， callback：请求成功回调
 */
Comm.ajaxPost = function (url, params, callback, errCallback) {
    if (url.indexOf('?') != -1) {
        url += "&r=" + new Date().getTime();
    } else {
        url += "?r=" + new Date().getTime();
    }
    $.ajax({
        type: "POST",
        url: url,
        data: params,
        dataType: "json",
        success: function (data, status) {
            callback(data);
        },
        error: function (err) {
            if (err.status != 302 && err.readyState != 0) {
                //parent.ezzalert('操作失败了哦，请检查您的网络链接！',8,'系统提示');
                parent.ezzalert("操作失败了哦，请检查您的网络链接！", 8, '系统提示');
            }
            errCallback && errCallback(err);
        }
    });
};

Comm.ajaxGet = function (url, params, callback, errCallback, showerror) {
    try {
        if (undefined == showerror) {
            showerror = true;
        }

        if (url.indexOf('?') != -1) {
            url += "&r=" + new Date().getTime();
        } else {
            url += "?r=" + new Date().getTime();
        }
        $.ajax({
            type: "GET",
            url: url,
            dataType: "json",
            data: params,
            success: function (data, status) {
                callback(data);
            },
            error: function (err) {
                if (showerror) {
                    if (err.status != 302 && err.readyState != 0) {
                        //alert("操作失败了哦，请检查您的网络链接！" + JSON.stringify(err));
                        parent.ezzalert("操作失败了哦，请检查您的网络链接！", 8, '系统提示');

                    }
                }
                errCallback && errCallback(err);
            }
        });
    } catch (e) {
        alert(e);
    }
};

//数值显示格式转化
Comm.numToCurrency = function (val, dec) {
    val = parseFloat(val);
    dec = dec || 2;	//小数位
    if (val === 0 || isNaN(val)) {
        return '';
    }
    val = val.toFixed(dec).split('.');
    var reg = /(\d{1,3})(?=(\d{3})+(?:$|\D))/g;
    return val[0].replace(reg, "$1,") + '.' + val[1];
};
//数值显示
Comm.currencyToNum = function (val) {
    var val = String(val);
    if ($.trim(val) == '') {
        return 0;
    }
    val = val.replace(/,/g, '');
    val = parseFloat(val);
    return isNaN(val) ? 0 : val;
};
//只允许输入数字
Comm.numerical = function (e) {
    var allowed = '0123456789.-', allowedReg;
    allowed = allowed.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, '\\$&');
    allowedReg = new RegExp('[' + allowed + ']');
    var charCode = typeof e.charCode != 'undefined' ? e.charCode : e.keyCode;
    var keyChar = String.fromCharCode(charCode);
    if (!e.ctrlKey && charCode != 0 && !allowedReg.test(keyChar)) {
        e.preventDefault();
    }
};

//限制输入的字符长度
Comm.limitLength = function (obj, count) {
    obj.on('keyup', function (e) {
        if (count < obj.val().length) {
            e.preventDefault();
            obj.val(obj.val().substr(0, count));
        }
    });
};

//判断:当前元素是否是被筛选元素的子元素
$.fn.isChildOf = function (b) {
    return (this.parents(b).length > 0);
};

//判断:当前元素是否是被筛选元素的子元素或者本身
$.fn.isChildAndSelfOf = function (b) {
    return (this.closest(b).length > 0);
};

//数字输入框
$.fn.digital = function () {
    this.each(function () {
        $(this).keyup(function () {
            this.value = this.value.replace(/\D/g, '');
        })
    });
};

/**
 1. 设置cookie的值，把name变量的值设為value
 example $.cookie(’name’, ‘value’);
 2.新建一个cookie 包括有效期 路径 域名等
 example $.cookie(’name’, ‘value’, {expires: 7, path: ‘/’, domain: ‘jquery.com’, secure: true});
 3.新建cookie
 example $.cookie(’name’, ‘value’);
 4.删除一个cookie
 example $.cookie(’name’, null);
 5.取一个cookie(name)值给myvar
 var account= $.cookie('name');
 **/
$.cookie = function (name, value, options) {
    if (typeof value != 'undefined') { // name and value given, set cookie
        options = options || {};
        if (value === null) {
            value = '';
            options.expires = -1;
        }
        var expires = '';
        if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
            var date;
            if (typeof options.expires == 'number') {
                date = new Date();
                date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
            } else {
                date = options.expires;
            }
            expires = '; expires=' + date.toUTCString(); // use expires attribute, max-age is not supported by IE
        }
        var path = options.path ? '; path=' + options.path : '';
        var domain = options.domain ? '; domain=' + options.domain : '';
        var secure = options.secure ? '; secure' : '';
        document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
    } else { // only name given, get cookie
        var cookieValue = null;
        if (document.cookie && document.cookie != '') {
            var cookies = document.cookie.split(';');
            for (var i = 0; i < cookies.length; i++) {
                var cookie = jQuery.trim(cookies[i]);
                // Does this cookie string begin with the name we want?
                if (cookie.substring(0, name.length + 1) == (name + '=')) {
                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                    break;
                }
            }
        }
        return cookieValue;
    }
};

// 对Date的扩展，将 Date 转化為指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

/**
 * 金额按千位逗号分割
 * @character_set UTF-8
 * @author Jerry.li(hzjerry@gmail.com)
 * @version 1.2014.08.24.2143
 *  Example
 *  <code>
 *      alert($.formatMoney(1234.345, 2)); //=>1,234.35
 *      alert($.formatMoney(-1234.345, 2)); //=>-1,234.35
 *      alert($.unformatMoney(1,234.345)); //=>1234.35
 *      alert($.unformatMoney(-1,234.345)); //=>-1234.35
 *  </code>
 */
;
(function ($) {
    $.extend({
        /**
         * 数字千分位格式化
         * @public
         * @param mixed mVal 数值
         * @param int iAccuracy 小数位精度(默认為2)
         * @return string
         */
        formatMoney: function (mVal, iAccuracy) {
            var fTmp = 0.00;//临时变量
            var iFra = 0;//小数部分
            var iInt = 0;//整数部分
            var aBuf = []; //输出缓存
            var bPositive = true; //保存正负值标记(true:正数)
            /**
             * 输出定长字符串，不够补0
             * <li>闭包函数</li>
             * @param int iVal 值
             * @param int iLen 输出的长度
             */
            function funZero(iVal, iLen) {
                var sTmp = iVal.toString();
                var sBuf = [];
                for (var i = 0, iLoop = iLen - sTmp.length; i < iLoop; i++)
                    sBuf.push('0');
                sBuf.push(sTmp);
                return sBuf.join('');
            }
            if (typeof(iAccuracy) === 'undefined')
                iAccuracy = 2;
            bPositive = (mVal >= 0);//取出正负号
            fTmp = (isNaN(fTmp = parseFloat(mVal))) ? 0 : Math.abs(fTmp);//强制转换為绝对值数浮点
            //所有内容用正数规则处理
            iInt = parseInt(fTmp); //分离整数部分
            iFra = parseInt((fTmp - iInt) * Math.pow(10, iAccuracy) + 0.5); //分离小数部分(四舍五入)

            do {
                aBuf.unshift(funZero(iInt % 1000, 3));
            } while ((iInt = parseInt(iInt / 1000)));
            if (aBuf[0] == "000") {
                aBuf[0] = parseInt(aBuf[0]).toString();//最高段区去掉前导0
            } else {
                aBuf[0] = parseInt(aBuf[0].replace(/\b(0+)/gi, "")).toString();//最高段区去掉前导0
            }
            return ((bPositive) ? '' : '-') + aBuf.join(',') + '.' + ((0 === iFra) ? '00' : funZero(iFra, iAccuracy));
        },
        /**
         * 将千分位格式的数字字符串转换為浮点数
         * @public
         * @param string sVal 数值字符串
         * @return float
         */
        unformatMoney: function (sVal) {
            var fTmp = parseFloat(sVal.replace(/,/g, ''));
            return (isNaN(fTmp) ? 0 : fTmp);
        },
        /**
         * 根据信用类型返回信用描述
         * @param type
         * @returns {string}
         */
        fund_type: function (e) {
            switch ($.trim(e)) {
                case "01":
                    return "系统充值";
                case "02":
                    return "系统扣款";
                case "03":
                    return "韦尔报单";
                case "04":
                    return "报单分报单";
                case "05":
                    return "F持仓报单";
                case "06":
                    return "F持仓复投";
                case "07":
                    return "报单分复投";
                case "08":
                    return "韦尔复投";
                case "09":
                    return "韦尔升级";
                case "10":
                    return "F持仓转账(转出)";
                case "11":
                    return "F持仓转账(转入)";
                case "12":
                    return "韦尔转账(转出)";
                case "13":
                    return "韦尔转账(转入)";
                case "14":
                    return "报单分转账(转出)";
                case "15":
                    return "报单分转账(转入)";
                case "16":
                    return "F持仓提现(转出)";
                case "17":
                    return "报单分转账(转出)";
                case "20":
                    return "账户提现解冻";
                case "21":
                    return "账户提现扣款";
                case "22":
                    return "F持仓提现";
                case "23":
                    return "现金分提现";
                case "24":
                    return "F持仓申请兑换股票";
                case "25":
                    return "报单分升级";
                case "26":
                    return "F持仓升级";
                case "27":
                    return "现金分转出";
                case "28":
                    return "报单币转入";
                case "61":
                    return "直推奖";
                case "62":
                    return "小区奖";
                case "63":
                    return "服务奖";
                case "64":
                    return "小区奖-外事基金";
                case "65":
                    return "福利奖";
                case "66":
                    return "服务奖-外事基金";
                default :
                    return "未知";
            }
        },
        account_type: function (e) {
            switch ($.trim(e)) {
                case '1':
                    return 'W仓位';
                case '2':
                    return 'F持仓';
                case '3':
                    return '韦尔';
                case '4':
                    return '报单分';
                case '5':
                    return '外事基金';
                case '6':
                    return '现金分';
                default :
                    return "未知";
            }
        }, post_cash: function (e) {
            switch ($.trim(e)) {
                case "0":
                    return "待审核";
                case "1":
                    return "通过";
                case "2":
                    return "拒绝";
                case "3":
                    return "已撤销";
                case "4":
                    return "进行中";
                default :
                    return "未知";
            }
        }, post_type: function (e) {
            switch ($.trim(e)) {
                case "1":
                    return "交易平臺钱包";
                case "2":
                    return "BVCoin钱包";
                default :
                    return "未知";
            }
        },
        order_status: function (e) {
            switch (e) {
                case "1":
                    return "成功";
                case "0":
                    return "待支付";
                case "2":
                    return "失败";
                case "4":
                    return "关闭";
                default :
                    return "未知";
            }
        },
        red_txt: function (e) {
            if (!e) {
                return '-';
            }
            return "<font color='red'><strong>" + e + "</strong></font>";
        },
        green_txt: function (e) {
            if (!e) {
                return '-';
            }
            return "<font color='green'><strong>" + e + "</strong></font>";
        }, user_status: function (e) {
            switch (e) { //0 初始化 1 认购 2停用
                case "0":
                    return "初始化";
                case "1":
                    return "认购";
                case "2":
                    return "停用";
                default :
                    return "未知";
            }
        }, user_controlFlag: function (e) {
            switch (e) { //0 初始化 1 白名单 2黑名单
                case "0":
                    return "初始化";
                case "1":
                    return "是";
                case "2":
                    return "否";
                default :
                    return "未知";
            }
        }, user_blacklistFlag: function (e) {
            switch (e) { //0 初始化 1 白名单 2黑名单
                case "0":
                    return "初始化";
                case "1":
                    return "可提现";
                case "2":
                    return "不能提现";
                default :
                    return "未知";
            }
        },

        /**
         * 根据储值状态类型返回储值描述
         * @param type
         * @returns {string}
         */
        getpayStatusByType: function (type) {
            switch (type) {
                case '0':
                    return '待支付';
                case '1':
                    return '成功';
                case '2':
                    return '失败';
            }
        },

        /**
         * 根据提现状态类型返回提现描述
         * @param type
         * @returns {string}
         */
        getwithdrawalsStatusByType: function (type) {
            switch (type) {
                case '0':
                    return '待审核';
                case '1':
                    return '成功';
                case '2':
                    return '失败';
                case '3':
                    return '待处理';
            }
        },

        /**
         * 根据资金类型返回类型描述
         * @param type
         * @returns {string}
         */
        getFundsByType: function (type) {
            switch (type) {
                case "10":
                    return "网银储值";
                case "23":
                    return "系统服务商(加款)";
                case "24":
                    return "系统服务商(扣款)";
                case "25":
                    return "系统服务商(冻结)";
                case "26":
                    return "系统服务商(解冻)";
                case "29":
                    return "钱包转账(转入)";
                case "35":
                    return "钱包支出(游戏充值)";
                case "36":
                    return "信用额还款收入(归还信用额)";
                case "37":
                    return "信用额还款支出(归还信用额)";
            }
        },
        /**
         * 根据资金类型返回类型描述
         * @param type
         * @returns {string}
         */
        getFundNameByType: function (type) {
            switch (type) {
                case "10":
                    return "网银储值";
                case "11":
                    return "供货收入";
                case "12":
                    return "进货支出";
                case "13":
                    return "认购卡收入";
                case "14":
                    return "认购卡支出";
                case "15":
                    return "卡片退货退款";
                case "17":
                    return "返点支出(一卡通)";
                case "18":
                    return "银行提现";
                case "19":
                    return "卡片退货收款";
                case "20":
                    return "返点收入(一卡通)";
                case "23":
                    return "系统服务商(加款)";
                case "24":
                    return "系统服务商(扣款)";
                case "25":
                    return "系统服务商(冻结)";
                case "26":
                    return "系统服务商(解冻)";
                case "27":
                    return "银行提现失败";
                case "28":
                    return "钱包转账(转出)";
                case "29":
                    return "钱包转账(转入)";
                case "30":
                    return "制卡费支出";
                case "32":
                    return "卡押金转出";
                case "33":
                    return "卡押金转入";
                case "38":
                    return "网吧订单提成(转出)";
                case "39":
                    return "网吧订单提成(转入)";
                case "40":
                    return "网吧自助加款(转出)";
                case "41":
                    return "网吧自助加款(转入)";
                case "42":
                    return "网银手续费";
            }
        },
        /**
         * 根据资金类型显示金额
         * @param currency
         * @param options
         * @param rowObject
         * @returns {string}
         */
        formatCurrency: function (currency, options, rowObject) {
            var type = rowObject.credittype;
            if ('10' == type || '13' == type || '14' == type || '15' == type) {
                currency = $.formatMoney(currency, 3);
                return '<font color="#d36161">+' + currency + '</font>';
            } else {
                currency = $.formatMoney(currency, 3);
                return '<font color="#769551">-' + currency + '</font>';
            }
        },
        /**
         * format 中文金额
         * @param currency
         * @returns {string}
         */
        formatlastCurrency: function (currency) {
            currency = $.formatMoney(currency, 3);
            return '￥' + currency;
        },

        formatcredit: function (currency, o, r) {
            if (r.creditflow == 0) {
                //增加
                currency = $.formatMoney(currency + r.money, 3);
            } else {
                //减少
                currency = $.formatMoney(currency - r.money, 3);
            }
            return '￥' + currency;
        },

        formatAreaCurrency: function (el, currency) {
            if (null == currency)currency = 0;
            currency = $.formatMoney(currency, 3);
            currency = '￥<font>' + currency.substr(0, currency.indexOf(".")) + '</font>.' + currency.substr(currency.indexOf(".") + 1);
            return el.html(currency);
        },

        /**
         * 註册用於grid 中获取金额后数量
         * @param cellvalue
         * @param options
         * @param cell
         * @returns {XMLList|*}
         */
        formatUnCurrency: function (cellvalue, options, cell) {
            return $('font', cell).text();
        },

        /**
         * format 信息类型
         * @param currency
         * @returns {string}
         */
        formatmsgType: function (type) {
            switch (type) {
                case '0':
                    return '信息公告';
                case '1':
                    return '消息提醒';
                default :
                    return '未知类型';
            }
        },
        /**
         * format 信息状态
         * @param currency
         * @returns {string}
         */
        formatmsgStatus: function (status) {
            switch (status) {
                case '0':
                    return '未读';
                case '1':
                    return '已读';
                default :
                    return '未知状态';
            }
        },
        /**
         * format 商品类型
         * @param currency
         * @returns {string}
         */
        getrechargeWay: function (e) {
            switch (e) {
                case "10":
                    return "官方卡密";
                case "11":
                    return "卡密直储";
                case "12":
                    return "在线充值";
                case "13":
                    return "人工充值";
                default:
                    return "未知";
            }
        },
        /**
         * format 商品状态
         * @param currency
         * @returns {string}
         */
        fsaleStatus: function (e) {
            switch (e) {
                case "1":
                    return "上架销售";
                case "2":
                    return "供货商下架";
                case "3":
                    return "平臺临时下架";
                case "4":
                    return "平臺永久下架";
                default:
                    return "未知";
            }
        }
    });
})(jQuery);

//是否為空
function isNull(str) {
    str = $.trim(str);
    if (str == null || str.length == 0) {
        return true;
    } else {
        return false;
    }
}

//清除文本框内容
function Closetext(txt, close) {

    var value = txt.val();
    //添加去除 顏色与文字 事件
    close.click(function () {
        txt.val("");
        close.removeClass("red");
    });

    if (value.length != 0) {
        close.addClass("red");
    } else {
        close.removeClass("red");

    }
}


//地区下拉框获取城市信息
function getCitys(obj) {
    if (isNull(obj.value)) {
        var city1 = $("#city");
        city1.empty();
        city1.append("<option value=''>-请选择-</option>");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comm/getCity.html?rcode=" + obj.value,
        async: false,
        dataType: "json",
        success: function (data) {
            //city
            var obj = data.data;
            var city = $("#city");
            city.empty();
            if (obj.length > 0) {
                $.each(obj,
                    function (i) {
                        city.append("<option value='" + obj[i].rcode + "'>" + obj[i].rname + "</option>");
                    });
            }
        }
    });
}

/**
 * placeholder
 */
(function (g, i, d) {
    var a = 'placeholder' in i.createElement('input'),
        e = 'placeholder' in i.createElement('textarea'),
        j = d.fn,
        c = d.valHooks,
        l, k;
    if (a && e) {
        k = j.placeholder = function () {
            return this
        };
        k.input = k.textarea = true
    } else {
        k = j.placeholder = function () {
            var m = this;
            m.filter((a ? 'textarea' : ':input') + '[placeholder]').not('.placeholder').bind({
                'focus.placeholder': b,
                'blur.placeholder': f
            }).data('placeholder-enabled', true).trigger('blur.placeholder');
            return m
        };
        k.input = a;
        k.textarea = e;
        l = {
            get: function (n) {
                var m = d(n);
                return m.data('placeholder-enabled') && m.hasClass('placeholder') ? '' : n.value
            },
            set: function (n, o) {
                var m = d(n);
                if (!m.data('placeholder-enabled')) {
                    return n.value = o
                }
                if (o == '') {
                    n.value = o;
                    if (n != i.activeElement) {
                        f.call(n)
                    }
                } else {
                    if (m.hasClass('placeholder')) {
                        b.call(n, true, o) || (n.value = o)
                    } else {
                        n.value = o
                    }
                }
                return m
            }
        };
        a || (c.input = l);
        e || (c.textarea = l);
        d(function () {
            d(i).delegate('form', 'submit.placeholder', function () {
                var m = d('.placeholder', this).each(b);
                setTimeout(function () {
                    m.each(f)
                }, 10)
            })
        });
        d(g).bind('beforeunload.placeholder', function () {
            d('.placeholder').each(function () {
                this.value = ''
            })
        })
    }

    function h(n) {
        var m = {}, o = /^jQuery\d+$/;
        d.each(n.attributes, function (q, p) {
            if (p.specified && !o.test(p.name)) {
                m[p.name] = p.value
            }
        });
        return m
    }

    function b(o, p) {
        var n = this,
            q = d(n),
            m;
        if (n.value == q.attr('placeholder') && q.hasClass('placeholder')) {
            m = n == i.activeElement;
            if (q.data('placeholder-password')) {
                q = q.hide().next().show().attr('id', q.removeAttr('id').data('placeholder-id'));
                if (o === true) {
                    return q[0].value = p
                }
                q.focus()
            } else {
                n.value = '';
                q.removeClass('placeholder')
            }
            m && n.select()
        }
    }

    function f() {
        var r, m = this,
            q = d(m),
            n = q,
            p = this.id;
        if (m.value == '') {
            if (m.type == 'password') {

                if (!q.data('placeholder-textinput')) {
                    try {
                        r = q.clone().attr({
                            type: 'text'
                        })
                    } catch (o) {
                        r = d('<input>').attr(d.extend(h(this), {
                            type: 'text'
                        }))
                    }
                    r.removeAttr('name').data({
                        'placeholder-password': true,
                        'placeholder-id': p
                    }).bind('focus.placeholder', b);
                    q.data({
                        'placeholder-textinput': r,
                        'placeholder-id': p
                    }).before(r)
                }
                q = q.removeAttr('id').hide().prev().attr('id', p).show()
            }
            q.addClass('placeholder');
            if (m.type == 'password') {
                q[0].value = $(q[0]).attr("id");

            } else {
                q[0].value = q.attr('placeholder');
            }

        } else {
            q.removeClass('placeholder')
        }
    }
}(this, document, jQuery));


$(function () {
    $('input').placeholder();
});


//替换表格控件的翻页样式
function updatePagerIcons(table) {
    var replacement =
    {
        'ui-icon-seek-first': 'ace-icon fa fa-angle-double-left bigger-140',
        'ui-icon-seek-prev': 'ace-icon fa fa-angle-left bigger-140',
        'ui-icon-seek-next': 'ace-icon fa fa-angle-right bigger-140',
        'ui-icon-seek-end': 'ace-icon fa fa-angle-double-right bigger-140'
    };
    $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
        var icon = $(this);
        var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

        if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
    })
}

// 设置快捷日期
function changeDate(days, beginTime, endTime) {
    var now = new Date(); // 获取今天时间
    var day = now.getDate();
    var begin;
    var end;
    end = now.format('yyyy-MM-dd') + ' 23:59:59';
    if (days == 7) {
        now.setDate(day - 7);
        begin = now.format('yyyy-MM-dd' + ' 00:00:00');
    } else if (days == 30) {
        now.setDate(day - 30);
        begin = now.format('yyyy-MM-dd' + ' 00:00:00');
    }
    beginTime.val(begin);
    endTime.val(end);
}

function changeDates(type, beginTime, endTime) {
    var now = new Date(); // 获取今天时间
    var begin;
    var end;
    end = now.format('yyyy-MM-dd') + ' 23:59:59';
    if (type == 'zt') {
        now.setDate(now.getDate() - 1);
        begin = now.format('yyyy-MM-dd' + ' 00:00:00');
        end = now.format('yyyy-MM-dd') + ' 23:59:59';
    } else if (type == 'bz') {
        now.setDate(now.getDate() - now.getDay() + 1);
        begin = now.format('yyyy-MM-dd' + ' 00:00:00');
    } else if (type == 'by') {
        now.setMonth(now.getMonth());
        begin = now.format('yyyy-MM-' + '01 00:00:00');
    } else if (type == 'sy') {
        now.setMonth(now.getMonth() - 1);
        begin = now.format('yyyy-MM-' + '01 00:00:00');
        now.setMonth(now.getMonth() + 1);
        now.setDate(1);
        now.setHours(0);
        now.setMinutes(0);
        now.setSeconds(0);
        now.setTime(now.getTime() - 1000);
        end = now.format('yyyy-MM-dd hh:mm:ss');
    }
    beginTime.val(begin);
    endTime.val(end);
}

/**
 * 时间选择控件 限定时间段三个月内
 * @param startTimeID
 * @param endTimeID
 */
function initRange3Months(startTimeID, endTimeID) {
    $("#" + startTimeID).datetimepicker({
        minDate: moment().add(-3, 'month'),
        maxDate: moment()
    });
    $("#" + endTimeID).datetimepicker({
        minDate: moment().add(-3, 'month'),
        maxDate: $("#" + endTimeID).val()
    });
}

function initRangeMonthsByN(startTimeID, endTimeID, num) {
    $("#" + startTimeID).datetimepicker({
        minDate: moment().add(-num, 'month'),
        maxDate: moment()
    });
    $("#" + endTimeID).datetimepicker({
        minDate: moment().add(-num, 'month'),
        maxDate: moment()
    });
}

/**
 * 时间选择控件 开始时间与结束时间 约束
 * @param startTimeID
 * @param endTimeID
 */
function initRangeDateTimePicker(startTimeID, endTimeID) {
    $("#" + startTimeID).data().DateTimePicker.setMaxDate($("#" + endTimeID).val());
    $("#" + endTimeID).data().DateTimePicker.setMinDate($("#" + startTimeID).val());

    $("#" + startTimeID).on('change', function () {
        $("#" + endTimeID).data().DateTimePicker.setMinDate($(this).val());
    });
    $("#" + endTimeID).on('change', function () {
        $("#" + startTimeID).data().DateTimePicker.setMaxDate($(this).val());
    });
}