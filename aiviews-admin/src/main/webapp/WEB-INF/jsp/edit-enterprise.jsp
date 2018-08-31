<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>企业</title>
    <jsp:include page="common/include_css.jsp" />
</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5><small>企业</small></h5>
                        <div class="ibox-tools">
                        	<button class="btn btn-primary" onclick="goback()">返回</button>
                            <a class="collapse-link">
                                <i class="fa fa-chevron-up"></i>
                            </a>
                        </div>
                    </div>
                    <div class="ibox-content">
                        <form class="form-horizontal" id="editForm">
                        	<input type="hidden" name="id" value="${enterprise.id }">
                        	<div class="form-group">
                                <label class="col-sm-2 control-label">企业名称</label>

                                <div class="col-sm-10">
                                    <input id="username" name="enterpriseName" value="${enterprise.enterpriseName }" type="text" class="form-control" placeholder="企业名称">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">企业网址</label>
                                <div class="col-sm-10">
                                    <input id="truename" name="realmName" value="${enterprise.realmName }" type="text" class="form-control" placeholder="企业网址">
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <div class="col-sm-4 col-sm-offset-2">
                                    <button class="btn btn-primary" type="button" onclick="updateInfo()">保存</button>
                                    <button class="btn btn-white" type="reset">取消</button>
                                </div>
                            </div>
                        </form> 
                    </div>
                </div>
            </div>
        </div>
    </div>
	
	<jsp:include page="common/include_js.jsp" />

</body>
<script type="text/javascript">

	$(document).ready(function () {
	    $('.i-checks').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green',
	    });
	});
	
	function goback(){
		window.history.back();
	}
	
	function updateInfo(){
		$.ajax({
			url:"/aiviews-admin/enterprise/updateEnterprise",
			data:$("#editForm").serialize(),
			type:"post",
			success:function(data){
				if(data=='success'){
					alert("操作成功！");
					window.location="/aiviews-admin/enterprise/selectAllEnterprise";
				}else{
					alert("操作失败");
				}
			}
		})
	}
</script>
</html>
