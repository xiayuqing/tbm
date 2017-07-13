<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html class="zh_CN">
<head>
    <%@include file="/view/common/pageHead.jsp" %>
</head>
<body>
<%@include file="/view/common/header.jsp" %>

<div class="am-cf admin-main">
    <%@include file="/view/common/leftNav.jsp" %>

    <!-- content start -->
    <div class="admin-content">
        <div class="am-cf am-padding">
            <div class="am-fl am-cf"><strong class="am-text-primary am-text-lg">Sign Up</strong> /
                <small>system info register</small>
            </div>
        </div>
        <hr/>
        <div class="am-tabs am-margin" data-am-tabs>
            <ul class="am-tabs-nav am-nav am-nav-tabs">
                <li class="am-active"><a id="service-detail-tab" href="#tab1">System Register</a></li>
            </ul>

            <div class="am-tabs-bd">
                <div class="am-tab-panel am-fade am-in am-active mt-default" id="tab1">
                    <div class="am-g">
                        <div class="am-u-sm-12 am-u-md-8">
                            <form class="am-form am-form-horizontal" enctype="multipart/form-data"
                                  id="upload-sys-form">
                                <div class="am-form-group">
                                    <label for="sys-name" class="am-u-sm-3 am-form-label">Name:</label>
                                    <div class="am-u-sm-9">
                                        <input id="sys-name" name="sys-name" type="text" placeholder="eg:user-jhd">
                                        <span class="check-tip none">Can Not be empty and less than 100 characters</span>
                                    </div>
                                </div>

                                <div class="am-form-group">
                                    <label for="sys-group" class="am-u-sm-3 am-form-label">Group:</label>
                                    <div class="am-u-sm-9">
                                        <input id="sys-group" name="sys-group" type="text"
                                               placeholder="eg:Project-Name">
                                        <span class="check-tip none">Can Not be empty and less than 100 characters</span>
                                    </div>
                                </div>
                                <div class="am-form-group">
                                    <label for="env" class="am-u-sm-3 am-form-label">Environment:</label>
                                    <div class="am-u-sm-9">
                                        <select id="env" data-am-selected="{btnSize: 'sm'}">
                                            <option value="1">BETA</option>
                                            <option value="2">PROD</option>
                                            <option value="3">ALPHA</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="am-form-group">
                                    <div class="am-u-sm-9 am-u-sm-push-3">
                                        <button id="confirm-sys-signup" type="button" class="am-btn am-btn-primary">
                                            Sign Up
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div class="am-u-sm-12 am-u-md-4">
                            <div class="am-panel am-panel-default">
                                <div class="am-panel-bd">
                                    <div class="user-info am-text-danger">
                                        <p><span class="am-icon-warning icon"></span>Tip:</p>
                                        <p>系统注册后请留存 SystemId 值,该值用于tbm-client配置中.</p>
                                        <p>系统信息可在[注册信息]中查询</p>
                                    </div>
                                </div>
                            </div>

                        </div>

                    </div>

                </div>
            </div>
        </div>


    </div>
    <!-- content end -->

    <%--等待模态框--%>
    <div class="am-modal am-modal-loading am-modal-no-btn" tabindex="-1" id="my-modal-loading">
        <div class="am-modal-dialog">
            <div class="am-modal-hd">Waiting....</div>
            <div class="am-modal-bd">
                <span class="am-icon-spinner am-icon-spin"></span>
            </div>
        </div>
    </div>

    <%--解析系统jar包之后的模态框--%>
    <div class="am-modal am-modal-alert" tabindex="-1" id="sys-response-alert">
        <div class="am-modal-dialog">
            <div class="am-modal-hd">提交成功,Themis已解析完成您的服务,您可以在"已解析系统"中查看.</div>
            <div class="am-modal-footer">
                <span class="am-modal-btn">确定</span>
            </div>
        </div>
    </div>

    <%--解析依赖包之后的模态框--%>
    <div class="am-modal am-modal-alert" tabindex="-1" id="dep-response-alert">
        <div class="am-modal-dialog">
            <div class="am-modal-hd">依赖JAR包提交成功.</div>
            <div class="am-modal-footer">
                <span class="am-modal-btn">确定</span>
            </div>
        </div>
    </div>

</div>

<%@include file="/view/common/footer.jsp" %>
<script>
    $(function () {
        $('#sys-name').on('blur', function () {
            checkNull('sys-name');
        });
        $('#sys-group').on('blur', function () {
            checkNull('sys-group');
        });

        $('#confirm-sys-signup').on('click', function () {
            if (!(checkNull('sys-name') && checkNull('sys-group'))) {
                return false;
            }

            uploadJar('upload-sys-form', 'confirm-sys-upload', '/api/system-upload', 'sys-response-alert', '服务注册申请');
        });
    });

</script>
</body>
</html>
