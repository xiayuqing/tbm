<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- sidebar start -->
<div class="admin-sidebar am-offcanvas" id="admin-offcanvas">
    <div class="am-offcanvas-bar admin-offcanvas-bar">
        <ul class="am-list admin-sidebar-list">
            <li><a href="/page/index"><span class="am-icon-home"></span> 首页</a></li>
            <li class="admin-parent">
                <a class="am-cf" data-am-collapse="{target: '#collapse-nav'}"><span class="am-icon-file"></span>
                    服务管理 <span class="am-icon-angle-right am-fr am-margin-right"></span></a>
                <ul class="am-list am-collapse admin-sidebar-sub am-in" id="collapse-nav">
                    <li><a href="/edit/upload" class="am-cf"><span class="am-icon-upload"></span> 服务注册</a>
                    </li>
                    <li><a href="/edit/resolved?offset=0&len=999&status=0" class="am-cf"><span
                            class="am-icon-check"></span> 已解析系统<span
                            class="am-badge am-badge-secondary am-margin-right am-fr"></span></a></li>
                    <li><a href="#" class="disabled"><span class="am-icon-puzzle-piece"></span> 审核中系统<span
                            class="am-badge am-badge-secondary am-margin-right am-fr"></span></a></li>
                    <li><a href="/verified/sysList?offset=0&len=999&status=1"><span class="am-icon-th"></span> 已审核系统<span
                            class="am-badge am-badge-secondary am-margin-right am-fr"></span></a></li>
                    <li><a href="/online/query-system"><span class="am-icon-calendar"></span> 已上线系统<span
                            class="am-badge am-badge-secondary am-margin-right am-fr"></span></a></li>
                </ul>
            </li>
            <li><a href="/page/doc/sysList"><span class="am-icon-table"></span> 文档中心</a></li>
            <li><a href="/tools"><span class="am-icon-wrench"></span> 工具</a></li>
            <li><a href="/servers/list"><span class="am-icon-server"></span> 服务器</a></li>
            <li><a href="/website/list"><span class="am-icon-internet-explorer"></span> 网站</a></li>
            <li><a href="#"><span class="am-icon-pencil-square-o"></span> 权限管理</a></li>
            <li><a href="#"><span class="am-icon-sign-out"></span> 注销</a></li>
        </ul>

        <div class="am-panel am-panel-default admin-sidebar-panel">
            <div class="am-panel-bd">
                <p><span class="am-icon-bookmark"></span> 公告</p>
                <p>BalaBalaBalaBalaBala。—— Themis AI</p>
            </div>
        </div>

        <div class="am-panel am-panel-default admin-sidebar-panel">
            <div class="am-panel-bd">
                <p><span class="am-icon-tag"></span> wiki</p>
                <p>Welcome to the Themis wiki!</p>
            </div>
        </div>
    </div>
</div>
<!-- sidebar end -->
















