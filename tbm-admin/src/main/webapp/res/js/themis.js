(function ($) {
    'use strict';

    $(function () {
        var $fullText = $('.admin-fullText');
        $('#admin-fullscreen').on('click', function () {
            $.AMUI.fullscreen.toggle();
        });

        $(document).on($.AMUI.fullscreen.raw.fullscreenchange, function () {
            $fullText.text($.AMUI.fullscreen.isFullscreen ? '退出全屏' : '开启全屏');
        });

        $('.select-dropdown').on('mouseover', function (e) {
            //setTimeout($dropdown.dropdown('open'),1000);
            $(this).addClass('am-active');
            return false;
        }).on('mouseout', function () {
            $(this).removeClass('am-active');
        })

    });
})(jQuery);

var needSessionJson = ['N','Y'];


function uploadJar(formId, submitBtnId, url, resModalId, resText) {
    //等待页
    $('#my-modal-loading').modal('open');
    var formData = new FormData($("#" + formId)[0]);
    var loadingModalOpen = false;//用来解决模态框多次调用的问题
    $("#" + submitBtnId).addClass("am-disabled").off('click');
    $('#my-modal-loading').on('opened.modal.amui', function () {
        if (!loadingModalOpen) {
            $.ajax({
                url: url,
                type: 'POST',
                data: formData,
                async: false,
                cache: false,
                contentType: false,
                processData: false,
                timeout: 3600000,
                success: function (data) {
                    $('#my-modal-loading').modal('close');
                    var result = JSON.parse(data);
                    if ("200" == result.code) {
                        $('#my-modal-loading').on('closed.modal.amui', function () {
                            $("#" + formId)[0].reset();
                            $('#' + resModalId).modal('open');
                        });
                    } else if ("300" == result.code) {
                        $('#my-modal-loading').on('closed.modal.amui', function () {
                            $('#' + resModalId).find('.am-modal-hd').html(resText + '失败,原因:' + result.msg);
                            $('#' + resModalId).modal('open');
                        });
                    } else {
                        $('#my-modal-loading').on('closed.modal.amui', function () {
                            $('#' + resModalId).find('.am-modal-hd').html(resText + '失败,请联系管理员.原因:' + result.msg);
                            $('#' + resModalId).modal('open');
                        });
                    }
                },
                error: function (data) {
                    $('#my-modal-loading').modal('close');
                    if (data.status == '404') {
                        alertTip('fail', '页面丢失，稍后再试');
                    } else if (data.status == '500') {
                        alertTip('fail', '系统忙，稍后再试');
                    } else {
                        alertTip('fail', '出错了,响应码是' + data.status + ',请联系管理员');
                    }
                }
            });
            $("#" + submitBtnId).removeClass("am-disabled").off('click').on('click', function () {
                uploadJar(formId, submitBtnId, url, resModalId, resText);
            });
            loadingModalOpen = true;
        }
    });
}

//确认系统
function confirmSystem() {
    var serviceList = $("#service-list tbody tr").not('.no-record');
    for (var i = 0; i < serviceList.length; i++) {
        if (serviceList.eq(i).attr('data-status') != 1) {
            alertTip('fail', '该系统的服务未全部确认,请确认系统的所有服务后再确认系统');
            return false;
        }
    }

    $('#confirm-system-modal').modal({
        relatedTarget: this,
        onConfirm: function (options) {
            var systemId = $("#system-id").val();
            var url = "/api/confirm/system";
            $("#confirm-system").addClass("am-disabled").off('click');
            sendAjax({
                url: url,
                method: 'POST',
                param: {systemId: systemId},
                tipText: '确认系统',
                callback: function (result) {
                    //弹框提示成功,再跳转
                    alertTip('success', '确认系统成功', function () {
                        window.location.reload();
                    });
                }
                // ,
                // errorFun: function () {
                //     $("#confirm-system").removeClass("am-disabled").off('click').on('click', confirmSystem);
                // }
            });
        }
    });
}

//确认服务
function confirmService() {
    var apiList = $("#apiList tbody tr").not('.no-record');
    for (var i = 0; i < apiList.length; i++) {
        if (apiList.eq(i).attr('data-status') != 1) {
            alertTip('fail', '该服务存在API示例未提交,请完善所有API');
            return false;
        }
    }

    $('#confirm-service-modal').modal({
        relatedTarget: this,
        onConfirm: function (options) {
            var serviceId = $("#service-id").val();
            var url = "/api/confirm/service";
            $("#confirm-service").addClass("am-disabled").off('click');
            sendAjax({
                url: url,
                method: 'POST',
                param: {serviceId: serviceId},
                tipText: '确认服务',
                callback: function (result) {
                    //弹框提示成功,再跳转
                    alertTip('success', '确认服务成功', function () {
                        window.location.reload();
                    });
                },
                errorFun: function () {
                    $("#confirm-service").removeClass("am-disabled").off('click').on('click', confirmService);
                }
            });
        }
    });
}

function confirmApi() {
    //验证是否为空
    if (!checkNull('reqEg')) {
        return false;
    }
    if (!checkNull('resEg')) {
        return false;
    }

    $('#confirm-api-modal').modal({
        relatedTarget: this,
        onConfirm: function (options) {
            var param = $("#reqResEgForm").serialize();
            var apiId = currentTr.attr("data-id");
            var statusCode = "{\"200\":\"success\",\"300\":\"fail\",\"999\":\"exception\"}";
            var others = "{}";
            var state = 1;
            param += '&apiId=' + apiId + '&statusCode=' + statusCode + '&others=' + others + '&state=' + state;
            var id = $("#api-demo-id").val();//demoId
            if(id){
                param += '&id='+id;
            }

            $("#save-example").addClass("am-disabled").off('click');
            sendAjax({
                url: '/api/confirm/api',
                method: 'POST',
                param: param,
                tipText: '提交示例',
                callback: function (result) {
                    //请求示例和响应示例提交成功后,把按钮和textArea都置灰,修改按钮出现
                    $("#reqEg").attr("disabled", "true");
                    $("#resEg").attr("disabled", "true");
                    $("#reqEgTip").hide();
                    $("#resEgTip").hide();
                    $("#save-example").off('click').addClass("am-disabled").html("示例已提交");
                    //show update button and assign value to apiDemoId
                    $("#update-eg-style").removeClass("none");
                    $("#api-demo-id").val(result.data);
                    //改变当前API的status
                    currentTr.attr('data-status', '1');
                    currentTr.find(".am-btn-group").html('<a onclick="getApiDetail(this)" class="am-btn am-btn-default am-btn-xs am-text-success"><span class="am-icon-check-square-o"></span>查看</a>');

                    //弹框提示成功,再跳转到service-detail
                    alertTip('success', '提交示例成功', function () {
                        $("#service-detail-tab").trigger('click');
                    });
                },
                errorFun: function () {
                    $("#save-example").removeClass("am-disabled").off('click').on('click', confirmApi);
                }
            });
        },
        onCancel: function () {

        }
    });

}

//请求示例和响应示例改成可修改状态
function updateDemoStyle() {
    $("#reqEg").removeAttr("disabled");
    $("#resEg").removeAttr("disabled");
    $("#reqEgTip").show();
    $("#resEgTip").show();
    $("#update-eg-style").addClass('none');
    $("#commit-update-eg").removeClass('none');
}

function updateDemo() {
    //验证是否为空
    if (!checkNull('reqEg')) {
        return false;
    }
    if (!checkNull('resEg')) {
        return false;
    }
    $('#update-demo-modal').modal({
        relatedTarget: this,
        onConfirm: function (options) {
            var param = $("#reqResEgForm").serialize();
            var url = '/api/update/api-demo';
            var apiId = currentTr.attr("data-id");
            var id = $("#api-demo-id").val();//demoId
            var statusCode = "{\"200\":\"success\",\"300\":\"fail\",\"999\":\"exception\"}";
            var others = "{}";
            var state = 1;
            param += '&id=' + id + '&apiId=' + apiId + '&statusCode=' + statusCode + '&others=' + others + '&state=' + state;
            if(!id){
                url = '/api/create/api-demo';
            }
            $("#commit-update-eg").addClass("am-disabled").off('click');
            sendAjax({
                url: url,
                method: 'POST',
                param: param,
                tipText: '修改示例',
                callback: function (result) {
                    //修改后,把按钮和textArea都置灰
                    $("#reqEg").attr("disabled", "true");
                    $("#resEg").attr("disabled", "true");
                    $("#reqEgTip").hide();
                    $("#resEgTip").hide();
                    $("#update-eg-style").removeClass("none");
                    $("#commit-update-eg").removeClass("am-disabled").off('click').on('click', updateDemo).addClass("none");

                    //弹框提示成功,再跳转到service-detail
                    alertTip('success', '修改示例成功', function () {
                        $("#service-detail-tab").trigger('click');
                    });
                },
                errorFun: function () {
                    $("#commit-update-eg").removeClass("am-disabled").off('click').on('click', updateDemo);
                }
            });
        },
        onCancel: function () {

        }
    });
}


function getApiDetail(obj, origin) {
    currentTr = $(obj).closest('tr');
    var id = currentTr.attr('data-id');
    $(obj).addClass("am-disabled").removeAttr('onclick');
    sendAjax({
        url: '/api/api-detail',
        param: {apiId: id},
        tipText: '获取API详情',
        callback: function (result) {
            $("#api-detail-tab").closest('li').removeClass('none');
            $("#api-detail-tab").trigger('click');
            if (origin == 'doc') {
                $(obj).removeClass("am-disabled").removeAttr('onclick').attr('onclick', 'getApiDetail(this,"' + origin + '")');
                initApiDetailDoc(result);
            } else {
                $(obj).removeClass("am-disabled").removeAttr('onclick').attr('onclick', 'getApiDetail(this)');
                initApiDetail(result);
            }
        },
        errorFun: function () {
            if (origin == 'doc') {
                $(obj).removeClass("am-disabled").removeAttr('onclick').attr('onclick', 'getApiDetail(this,"' + origin + '")');
            } else {
                $(obj).removeClass("am-disabled").removeAttr('onclick').attr('onclick', 'getApiDetail(this)');
            }
        }
    });
}

//初始化API详情
function initApiDetail(result) {
    // 业务参数
    var bizParams = result.data.argumentDTOs;
    var biz_param_table = $("#biz_param_table");
    showParamTable(bizParams, biz_param_table);

    // 响应参数
    var resParams = result.data.responseDTOs;
    var res_param_table = $("#res_param_table");
    showParamTable(resParams, res_param_table);

    //API基本信息
    $("#api-name").html(result.data.apiName);
    $("#api-description").html(result.data.description);
    $("#api-method").html(result.data.httpMethod);
    $("#api-need-session").html(needSessionJson[result.data.needSession]);

    //根据status是否为1来渲染响应实例和请求实例页面
    if(result.data.apiDemoDTO){
        $("#reqEg").val(result.data.apiDemoDTO.requestDemo).removeClass('null-tip-bd');
        $("#resEg").val(result.data.apiDemoDTO.responseDemo).removeClass('null-tip-bd');
        $("#api-demo-id").val(result.data.apiDemoDTO.id);
    }else{
        $("#reqEg").val('').removeClass('null-tip-bd');
        $("#resEg").val('').removeClass('null-tip-bd');
        $("#api-demo-id").val('');
    }

    $("#reqEg").next('span').addClass('none');
    $("#resEg").next('span').addClass('none');
    $("#commit-update-eg").addClass('none');

    if (result.data.status == 1) {
        $("#reqEg").attr("disabled", "true");
        $("#resEg").attr("disabled", "true");
        $("#reqEgTip").hide();
        $("#resEgTip").hide();
        $("#update-eg-style").removeClass('none');
        $("#save-example").addClass("am-disabled").off('click').html("示例已提交");
    } else {
        $("#reqEg").removeAttr("disabled");
        $("#resEg").removeAttr("disabled");
        $("#reqEgTip").show();
        $("#resEgTip").show();
        //绑定点击事件前先off,保证只绑定一个点击事件
        $("#update-eg-style").addClass('none');
        $("#save-example").removeClass("am-disabled").off('click').on('click', confirmApi).html("提交示例");

        $("#reqEg").off('blur').on("blur", function () {
            checkNull("reqEg");
        });
        $("#resEg").off('blur').on("blur", function () {
            checkNull("resEg");
        });
    }
}


//文档中心:初始化API详情
function initApiDetailDoc(result) {
    // 业务参数
    var bizParams = result.data.argumentDTOs;
    var biz_param_table = $("#biz_param_table");
    showParamTable(bizParams, biz_param_table);

    // 响应参数
    var resParams = result.data.responseDTOs;
    var res_param_table = $("#res_param_table");
    showParamTable(resParams, res_param_table);

    //API基本信息
    $("#api-name").html(result.data.apiName);
    $("#api-description").html(result.data.description);
    $("#api-method").html(result.data.httpMethod);
    $("#api-need-session").html(needSessionJson[result.data.needSession]);

    $("#reqEg").html(result.data.apiDemoDTO.requestDemo);
    $("#resEg").html(result.data.apiDemoDTO.responseDemo);
}

// 得到当前位置的目录
function getContentPos(id, currentLiId, url, param, name, redirectUrl) {
    sendAjax({
        url: url,
        param: param,
        tipText: '获取目录',
        callback: function (result) {
            var htmlArr = [];
            if (result.data.length > 0) {
                for (var i = 0; i < result.data.length; i++) {
                    var href = redirectUrl + result.data[i].id;
                    if (result.data[i].id == currentLiId) {
                        $("#" + id).prev().html(result.data[i][name]);//显示当前目录
                        htmlArr.push('<li class="am-active"><a href="' + href + '">' + result.data[i][name] + '</a></li>');
                    } else {
                        htmlArr.push('<li><a href="' + href + '">' + result.data[i][name] + '</a></li>');
                    }
                }
            } else {
                htmlArr.push('<li class="am-text-center">暂无记录</li>');
            }
            $('#' + id).html(htmlArr.join(''));
        }
    });
}


//下线系统或依赖包
function offline(obj,url, id, tipText) {
    $('#offline-modal').find('.am-modal-bd').html('确定下线该' + tipText + '吗?');
    $('#offline-modal').modal({
        relatedTarget: this,
        onConfirm: function (options) {
            $(obj).addClass("am-disabled").off('click');
            sendAjax({
                url: url,
                method: 'POST',
                param: {systemId: id},
                tipText: '下线' + tipText,
                callback: function (result) {
                    alertTip('success', '下线' + tipText + '成功', function () {
                        window.location.reload();
                    });
                },
                errorFun: function () {
                    $(obj).removeClass("am-disabled").off('click').on('click', function () {
                        offline(obj, id, tipText);
                    });
                }
            });
        }
    });
}

function upgradeSysView(obj) {
    var tr = $(obj).closest('tr');
    $('#sysName').val(tr.attr('data-sys-name'));
    $('#sysDesc').val(tr.attr('data-sys-description'));
    $('#sysOwner').val(tr.attr('data-sys-owner'));
    $('#sysId').val(tr.attr('data-sys-id'));
    $('#sys-detail-tab').closest('li').removeClass('none');
    $('#sys-detail-tab').trigger('click');
}

function upgradeDepView(obj) {
    var tr = $(obj).closest('tr');
    $('#depName').val(tr.attr('data-dep-name'));
    $('#depDesc').val(tr.attr('data-dep-description'));
    $('#depOwner').val(tr.attr('data-dep-owner'));
    $('#depId').val(tr.attr('data-dep-id'));
    $('#dep-detail-tab').closest('li').removeClass('none');
    $('#dep-detail-tab').trigger('click');
}

function getDepList() {
    $('#dep-list-li').addClass("am-disabled").off('click');
    var param = {
        offset: 0,
        len: 999,
        dataLevel: 1,
        status: 1,
        type: 2
    };
    sendAjax({
        url: '/api/list/system-info',
        method: 'GET',
        param: param,
        tipText: '获取依赖包列表',
        callback: function (result) {
            var htmlArr = [];
            var list = result.data;
            var tipText = '依赖包';
            if (list.length > 0) {
                for (var i = 0; i < list.length; i++) {
                    htmlArr.push('<tr data-dep-id="' + list[i].id + '"  data-dep-name="' + list[i].name + '"  data-dep-owner="' + list[i].owner + '"  data-dep-description="' + list[i].description + '" >');
                    htmlArr.push('<td>' + list[i].name + '</td>');
                    htmlArr.push('<td>' + list[i].owner + '</td>');
                    htmlArr.push('<td>' + list[i].description + '</td>');

                    var upgradeClick = "upgradeDepView(this)";
                    var offlineClick = "offline(this,'/api/remove-dependency'," + list[i].id + ",'" + tipText + "')";

                    htmlArr.push('<td><div class="am-btn-toolbar"><div class="am-btn-group am-btn-group-xs">' +
                        '<a onclick="' + upgradeClick + '"  class="am-btn am-btn-xs am-text-secondary"><span class="am-icon-arrow-circle-o-up"></span>升级</a>' +
                        '<a onclick="' + offlineClick + '" class="am-btn am-btn-xs am-text-danger"><span class="am-icon-arrow-circle-down"></span>下线</a> </div></td>'
                    );
                }

            } else {
                htmlArr.push('<tr><td colspan="4" class="am-text-center">暂无记录</td></tr>');
            }
            $('#dep-table').find('tbody').html(htmlArr.join(''));
            $('#dep-list-li').removeClass("am-disabled").off('click')
            $("#dep-list-tab").trigger('click');
        },
        errorFun: function () {
            $('#dep-list-li').removeClass("am-disabled").off('click').on('click', getDepList);
        }
    });
}

function newServiceView(obj) {
    var tr = $(obj).closest('tr');
    $('#serviceName').val('');
    $('#serviceDesc').val('');
    $('#serviceOwner').val('');

    var tab = $($('#upgrade-service-tab').attr('href'));
    tab.find('form #serviceId').remove();

    $('#new-service-tab').closest('li').removeClass('none');
    $('#new-service-tab').trigger('click');
}

function upgradeServiceView(obj) {
    var tr = $(obj).closest('tr');
    $('#serviceName').val(tr.attr('data-service-name'));
    $('#serviceDesc').val(tr.attr('data-service-description'));
    $('#serviceOwner').val(tr.attr('data-service-owner'));

    var tab = $($('#upgrade-service-tab').attr('href'));
    tab.find('form').append($('<input id="serviceId" name="serviceId" type="hidden" value="'+tr.attr('data-service-id')+'"/>'));

    $('#upgrade-service-tab').closest('li').removeClass('none');
    $('#upgrade-service-tab').trigger('click');
}

function upgradeService(formId, submitBtnId, url, resModalId, resText) {
    //等待页
    $('#my-modal-loading').modal('open');
    var formData = new FormData($("#" + formId)[0]);
    var loadingModalOpen = false;//用来解决模态框多次调用的问题
    $("#" + submitBtnId).addClass("am-disabled").off('click');
    $('#my-modal-loading').on('opened.modal.amui', function () {
        if (!loadingModalOpen) {
            $.ajax({
                url: url,
                type: 'POST',
                data: formData,
                async: false,
                cache: false,
                contentType: false,
                processData: false,
                timeout: 3600000,
                success: function (data) {
                    $('#my-modal-loading').modal('close');
                    var result = JSON.parse(data);
                    if ("200" == result.code) {
                        $('#my-modal-loading').on('closed.modal.amui', function () {
                            $("#" + formId)[0].reset();
                            $('#' + resModalId).modal('open');
                        });
                    } else if ("300" == result.code) {
                        $('#my-modal-loading').on('closed.modal.amui', function () {
                            $('#' + resModalId).find('.am-modal-hd').html(resText + '失败,原因:' + result.msg);
                            $('#' + resModalId).modal('open');
                        });
                    } else {
                        $('#my-modal-loading').on('closed.modal.amui', function () {
                            $('#' + resModalId).find('.am-modal-hd').html(resText + '失败,请联系管理员.原因:' + result.msg);
                            $('#' + resModalId).modal('open');
                        });
                    }
                },
                error: function (data) {
                    $('#my-modal-loading').modal('close');
                    if (data.status == '404') {
                        alertTip('fail', '页面丢失，稍后再试');
                    } else if (data.status == '500') {
                        alertTip('fail', '系统忙，稍后再试');
                    } else {
                        alertTip('fail', '出错了,响应码是' + data.status + ',请联系管理员');
                    }
                }
            });
            $("#" + submitBtnId).removeClass("am-disabled").off('click').on('click', function () {
                upgradeService(formId, submitBtnId, url, resModalId, resText);
            });
            loadingModalOpen = true;
        }
    });
}

//下线系统或依赖包
function offlineService(obj,url, id, tipText) {
    $('#offline-modal').find('.am-modal-bd').html('确定下线该' + tipText + '吗?');
    $('#offline-modal').modal({
        relatedTarget: this,
        onConfirm: function (options) {
            $(obj).addClass("am-disabled").off('click');
            sendAjax({
                url: url,
                method: 'POST',
                param: {systemId: id},
                tipText: '下线' + tipText,
                callback: function (result) {
                    alertTip('success', '下线' + tipText + '成功', function () {
                        window.location.reload();
                    });
                },
                errorFun: function () {
                    $(obj).removeClass("am-disabled").off('click').on('click', function () {
                        offline(obj, id, tipText);
                    });
                }
            });
        }
    });
}


function sendAjax(options) {
    $.ajax({
        url: options.url,
        type: options.method || 'GET',
        data: options.param,
        contentType: options.contentType || 'application/x-www-form-urlencoded;charset=UTF-8;',
        success: function (data) {
            var result = JSON.parse(data);
            if ("200" == result.code) {
                options.callback && options.callback(result);
            } else if ("300" == result.code) {
                //弹框提示失败
                alertTip('fail', options.tipText + '失败,原因是:' + result.msg);
                options.errorFun && options.errorFun();
            } else {
                alertTip('fail', options.tipText + '失败,请联系管理员.');
                options.errorFun && options.errorFun();
            }
        },
        error: function (data) {
            if (data.status == '404') {
                alertTip('fail', '页面丢失，稍后再试');
            } else if (data.status == '500') {
                alertTip('fail', '系统忙，稍后再试');
            } else {
                alertTip('fail', '出错了,响应码是' + data.status + ',请联系管理员');
            }
            options.errorFun && options.errorFun();
        }
    });
}


//把数组排序成树形结构的遍历结果
function getTree(newItems, parents, originItems, level) {

    for (var i = 0; i < parents.length; i++) {

        //判断是否存在于新数组中,如果存在就不放进去.如果不存在,就判断一下parentId是否为-1,是则放入新数组中,否则不放
        if ($.inArray(parents[i], newItems) < 0 && parents[i].parentId == -1) {
            newItems.push(parents[i]);
        }

        var index = $.inArray(parents[i], newItems);//得到当前节点在newItems中的位置
        if (index >= 0 && !newItems[index].level) {
            var children = [];
            //找到所有孩子节点,插入新的数组中
            for (var j = 0; j < originItems.length; j++) {
                if (originItems[j].parentId == parents[i].id) {
                    newItems.splice(index + 1 + children.length, 0, originItems[j]);
                    children.push(originItems[j]);
                }
            }

            newItems[index].level = level;
            newItems[index].childrenLength = children.length;

            if (children.length > 0) {
                getTree(newItems, children, originItems, level + 1);
            }

        }

    }
}

//折叠、打开子元素
function toggleFolder(obj) {
    var currentTr = $(obj).closest('tr');
    var level = currentTr.attr('level');
    var next = currentTr.next();
    while (next.length > 0) {
        if (level < parseInt(next.attr('level'))) {
            if ($(obj).hasClass('open')) {
                next.hide();
                next.find('.has-children').removeClass('am-icon-minus-square-o').addClass('am-icon-plus-square-o');
            } else {
                next.show();
                next.find('.has-children').addClass('am-icon-minus-square-o').removeClass('am-icon-plus-square-o');
            }
            next = next.next();
        } else {
            break;
        }
    }
    if ($(obj).hasClass('open')) {
        $(obj).removeClass('open').removeClass('am-icon-minus-square-o').addClass('am-icon-plus-square-o');
    } else {
        $(obj).addClass('open').addClass('am-icon-minus-square-o').removeClass('am-icon-plus-square-o');
    }
}


function showParamTable(params, table) {
    var newItems = [];
    if (params && Array.isArray(params)) {
        getTree(newItems, params, params, 1);//得到父子排序后的数组
    }

    //构建请求参数table
    var tableHtml = [];
    if (newItems.length > 0) {
        for (var i = 0; i < newItems.length; i++) {
            var rowHtml = [];
            var level = newItems[i].level;
            rowHtml.push('<tr id=' + newItems[i].id + ' level=' + level + ' parentId=' + newItems[i].parentId + ' class="relative-tr">');
            if (newItems[i].childrenLength <= 0) {
                rowHtml.push('<td  style="padding-left:' + ((level - 1) * 25 + 8) + 'px;"><span class="icon-tree">└</span><span class="paramName">' + newItems[i].argName + '</span></td>');//参数名
            } else {
                rowHtml.push('<td  style="padding-left:' + ((level - 1) * 25 + 8) + 'px;"><span class="icon-tree has-children am-icon-minus-square-o open" onclick="toggleFolder(this)"></span><span class="paramName">' + newItems[i].argName + '</span></td>');//参数名
            }

            rowHtml.push('<td>' + newItems[i].defaultValue + '</td>');//示例值
            if (newItems[i].isRequired == 1) {//是否必须
                rowHtml.push('<td>Y</td>');
            } else {
                rowHtml.push('<td>N</td>');
            }

            rowHtml.push('<td>' + newItems[i].description + '</td>');//描述
            rowHtml.push('</tr>');
            tableHtml.push(rowHtml.join(''));
        }
    } else {
        tableHtml.push('<tr><td colspan="4" class="am-text-center">暂无记录</td></tr>');
    }

    table.find('tbody').html(tableHtml.join(''));
}


jQuery.fn.center = function () {
    this.css('position', 'absolute');
    this.css('top', ( $(window).height() - this.height() ) / 2 + $(window).scrollTop() + 'px');
    this.css('left', ( $(window).width() - this.width() ) / 2 + $(window).scrollLeft() + 'px');
    return this;
};

function alertTip(success, tipText, fun) {
    if (success == 'success') {
        var html = '<div id="alertTip" class="alert-tip alert-success "><span class="am-icon-check-circle-o icon"></span>' + tipText + '</div>';
    } else {
        var html = '<div id="alertTip" class="alert-tip alert-fail"><span class="am-icon-warning icon"></span>' + tipText + '</div>';
    }
    $(html).appendTo($("body").eq(0));
    $('#alertTip').center();
    $('#alertTip').show(500);
    setTimeout(function () {
        $('#alertTip').hide(500);
        $('#alertTip').remove();
        fun && fun();
    }, 2000);
}

function checkNull(id) {
    var value = $("#" + id).val().trim();
    if (!value) {
        $("#" + id).addClass('null-tip-bd');
        $("#" + id).next('span').removeClass('none');
        return false;
    }
    $("#" + id).removeClass('null-tip-bd');
    $("#" + id).next('span').addClass('none');
    return true;
}

function onlyEnglish(id) {
    var value = $("#" + id).val().trim();
    var reg = /^[a-zA-Z]+$/;
    if (!reg.test(value)) {
        $("#" + id).addClass('null-tip-bd');
        $("#" + id).next('span').removeClass('none');
        return false;
    }
    $("#" + id).removeClass('null-tip-bd');
    $("#" + id).next('span').addClass('none');
    return true;
}