<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>学生管理</title>
    <jsp:include page="common/include_css.jsp" />
</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5><small>学生管理</small></h5>
                        <div class="ibox-tools">
                            <a class="collapse-link">
                                <i class="fa fa-chevron-up"></i>
                            </a>
                        </div>
                    </div>
                    <div class="ibox-content">
                    	<div  class="dataTables_wrapper form-inline" role="grid">
                    		<!-- 查询条件 -->
	                    	<form method="post" id="searchForm" action="/aihudong-duoping-web/student/showAllStudent">
		                    	<div class="row">
		                    		<div class="col-sm-2">
		                    			<div class="dataTables_length">
		                    				<a href="/aihudong-duoping-web/student/toUpdate" class="btn btn-primary ">新增</a>
		                    			</div>
		                    		</div>
		                    		<div class="col-sm-10">
		                    			<div class="input-group" style="float:right;">
		                    				<!-- 真实姓名 -->
				                            <input type="text" name="username" value="${student.username }" class="form-control" placeholder="关键字查找">
				                            <div class="input-group-btn">
				                                <input type="submit"  class="btn btn-primary" value="搜索">
				                            </div>
				                        </div>
		                    		</div>
		                    	</div>
	                    	</form>
                    		<!-- 查询条件结束 -->
	                        <table class="table table-striped table-bordered table-hover dataTables-example">
	                            <thead>
	                                <tr>
	                                	<th>企业id</th>
	                                    <th>企业名称</th>
	                                    <th>企业域名</th>
	                                    <th>操作</th>
	                                </tr>
	                            </thead>
	                            <tbody>
	                            	<c:forEach items="${enterpriseList }" var="enterprise">
	                            		<tr class="gradeA">
	                            			 <td>${enterprise.id }</td>
		                            		 <td>${enterprise.enterpriseName }</td>
		                            		 <td>${enterprise.realmName }</td>
			                                 <td>
			                                 	<a href="/aiviews-admin/enterprise/showEnterpriseDetail?id=${enterprise.id }"><i style="margin-left:5px;" class="fa fa-edit"></i></a>
			                                 	<a href="javascript:;" onclick="deleteStudent('${student.id }')"><i style="margin-left:5px;" class="fa fa-trash"></i></a>
			                                 </td>
		                            	</tr>
		                            </c:forEach>
	                            </tbody>
	                        </table>
							<!-- 分页 -->
							<jsp:include page="common/include_page.jsp">
 								<jsp:param value="/aiviews-admin/enterprise/selectAllEnterprise" name="pageTitle"/>
							</jsp:include>
							<!-- 分页结束 -->
                    </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="common/include_js.jsp" />

</body>
<script type="text/javascript">


</script>
</html>
