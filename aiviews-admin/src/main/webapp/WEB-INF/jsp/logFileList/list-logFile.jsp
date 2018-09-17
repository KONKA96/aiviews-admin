<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>日志管理</title>
    <jsp:include page="../common/include_css.jsp" />
</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5><small>日志管理</small></h5>
                        <div class="ibox-tools">
                            <a class="collapse-link">
                                <i class="fa fa-chevron-up"></i>
                            </a>
                        </div>
                    </div>
                    <div class="ibox-content">
                    	<div  class="dataTables_wrapper form-inline" role="grid">
                    		<!-- 查询条件 -->
	                    	<%-- <form method="post" id="searchForm" action="/aiviews-admin/enterprise/selectRecord">
		                    	<div class="row">
		                    		<div class="col-sm-10">
		                    		
		                    			<div class="input-group" style="float:right;">
		                    				<!-- 用户名 -->
				                            <input type="text" name="username" value="${record.username }" class="form-control" placeholder="关键字查找">
				                            <div class="input-group-btn">
				                                <input type="submit"  class="btn btn-primary" value="搜索">
				                            </div>
				                        </div>
		                    		</div>
		                    	</div>
	                    	</form> --%>
                    		<!-- 查询条件结束 -->
	                        <table class="table table-striped table-bordered table-hover dataTables-example">
	                            <thead>
	                                <tr>
	                                	<th>文件名</th>
	                                    <th>操作</th>
	                                </tr>
	                            </thead>
	                            <tbody>
	                            	<c:forEach items="${fileList }" var="file">
	                            		<tr class="gradeA">
	                            			<td>${file }</td>
			                                 <td>
			                                 	<a href="javascript:;" onclick="downFile('${file}')"><i style="margin-left:5px;" class="fa fa-download"></i></a>
			                                 </td>
		                            	</tr>
		                            </c:forEach>
	                            </tbody>
	                        </table>
                    </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="../common/include_js.jsp" />
	
</body>
<script type="text/javascript">
	function downFile(fileName){
		$.ajax({
			url:"/aiviews-admin/enterprise/testHttpsFile",
			data:"fileName="+fileName,
			type:"post",
			success:function(data){
				if(data=='success'){
					alert("下载成功！");
				}else{
					alert("操作失败");
				}
			}
		})
	}



</script>
</html>
