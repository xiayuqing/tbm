<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
            <div class="am-fl am-cf"><strong class="am-text-primary am-text-lg">首页</strong> /
                <small>概览</small>
            </div>
        </div>

        <ul class="am-avg-sm-1 am-avg-md-4 am-margin am-padding am-text-center admin-content-list ">
            <li><a href="#" class="am-text-warning"><span
                    class="am-icon-btn am-icon-angellist"></span><br/>已解析系统<br/>-</a>
            </li>
            <li><a href="#" class="am-text-danger"><span
                    class="am-icon-btn am-icon-briefcase"></span><br/>审核中系统<br/>-</a>
            </li>
            <li><a href="#" class="am-text-secondary"><span class="am-icon-btn am-icon-recycle"></span><br/>已审核系统<br/>-</a>
            </li>
            <li><a href="#" class="am-text-success"><span
                    class="am-icon-btn am-icon-user-md"></span><br/>已上线系统<br/>-</a>
            </li>
        </ul>
    </div>
    <!-- content end -->
</div>
<%@include file="/view/common/footer.jsp" %>
</body>
</html>
