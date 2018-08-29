<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>记录管理</title>
    <jsp:include page="../common/include_css.jsp" />
</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5><small>记录管理</small></h5>
                        <div class="ibox-tools">
                            <a class="collapse-link">
                                <i class="fa fa-chevron-up"></i>
                            </a>
                        </div>
                    </div>
                    <div class="ibox-content">
                    	<div  class="dataTables_wrapper form-inline" role="grid">
                    		<!-- 查询条件 -->
	                    	<form method="post" id="searchForm" action="/aihudong-duoping-web/admin/showAllAdmin">
		                    	<div class="row">
		                    		<div class="col-sm-10">
		                    			<div class="input-group" style="float:right;">
		                    				<!-- 真实姓名 -->
				                            <input type="text" name="truename" value="${admin.truename }" class="form-control" placeholder="关键字查找">
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
	                                	<th>用户名</th>
	                                    <th>登录时间</th>
	                                    <th>退出时间</th>
	                                    <th>屏幕ID</th>
	                                    <th>操作</th>
	                                </tr>
	                            </thead>
	                            <tbody>
	                            	<c:forEach items="${recordList }" var="record">
	                            		<tr class="gradeA">
	                            			<td>${record.username }</td>
	                            			<td>
	                            				${record.startTime }
		                            		 </td>
		                            		 <td>
		                            		 	${record.endTime }
		                           			 </td>
		                            		 <td>${record.screenId }</td>
			                                 <td>
			                                 	<c:if test="${admin.power== 1}"><a href="javascript:;"
												onclick="addFaculty(${admin.id});"><i
													style="margin-left: 5px;" class="fa fa-diamond"></i></a></c:if>
			                                 	 <c:if test="${admin.power== 2}"><a href="javascript:;"
												onclick="addBuilding(${admin.id});"><i
													style="margin-left: 5px;" class="fa fa-diamond"></i></a></c:if>
			                                 	<a href="/aihudong-duoping-web/admin/toUpdateAdmin?id=${admin.id }"><i style="margin-left:5px;" class="fa fa-edit"></i></a>
			                                 	<a href="javascript:;" onclick="deleteAdmin(${admin.id })"><i style="margin-left:5px;" class="fa fa-trash"></i></a>
			                                 </td>
		                            	</tr>
		                            </c:forEach>
	                            </tbody>
	                        </table>
							<!-- 分页 -->
							<jsp:include page="../common/include_page.jsp">
 								<jsp:param value="/aiviews-admin/enterprise/selectRecord" name="pageTitle"/>
							</jsp:include>
							<!-- 分页结束 -->
                    </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="../common/include_js.jsp" />
	
</body>
<script type="text/javascript">




</script>
</html>
