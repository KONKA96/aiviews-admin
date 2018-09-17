<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>教师管理</title>
    <jsp:include page="../common/include_css.jsp" />
</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5><small>教师管理</small></h5>
                        <div class="ibox-tools">
                            <a class="collapse-link">
                                <i class="fa fa-chevron-up"></i>
                            </a>
                        </div>
                    </div>
                    <div class="ibox-content">
                    	<div  class="dataTables_wrapper form-inline" role="grid">
                    		<!-- 查询条件 -->
	                    	<form method="post" id="searchForm" action="/aiviews-admin/enterprise/selectTeacher">
		                    	<div class="row">
		                    		<div class="col-sm-2">
		                    			<div class="dataTables_length">
		                    				<a href="${enterprise.realmName }/aihudong-duoping-web/teacher/toUpdate" class="btn btn-primary ">新增用户</a>
		                    			</div>
		                    		</div>
		                    		<div class="col-sm-10">

										<div class="input-group" style="float:right;">
		                    				<!-- 真实姓名 -->
				                            <input type="text" name="username" value="${teacher.username }" class="form-control" placeholder="关键字查找">
				                            <div class="input-group-btn">
				                                <input type="submit"  class="btn btn-primary" value="搜索">
				                            </div>
				                        </div>
		                    		</div>
		                    	</div>
	                    	</form>
	                        <table class="table table-striped table-bordered table-hover dataTables-example">
	                            <thead>
	                                <tr>
	                                	<th>用户名</th>
	                                    <th>真实姓名</th>
	                                    <th>职务</th>
	                                    <th>院系</th>
	                                    <th>科目</th>
	                                    <th>联系方式</th>
	                                    <th>性别</th>
	                                    <th>备注</th>
	                                    <th>操作</th>
	                                </tr>
	                            </thead>
	                            <tbody>
	                            	<c:forEach items="${teacherList }" var="teacher">
	                            		<tr class="gradeA">
	                            			 <td>${teacher.username }</td>
		                            		 <td>${teacher.truename }</td>
		                            		 <td>${teacher.job }</td>
		                            		 <td>${teacher.subject.faculty.facultyName }</td>
		                            		 <td>${teacher.subject.subjectName }</td>
		                            		 <td>${teacher.telephone }</td>
		                            		 <td>
		                            		 	<c:if test="${teacher.sex==0 }">男</c:if>
		                            		 	<c:if test="${teacher.sex==1 }">女</c:if>
		                            		 </td>
		                            		 <td>${teacher.remake }</td>
			                                 <td>
			                                 	<a href="${enterprise.realmName }/aihudong-duoping-web/teacher/toUpdate?id=${teacher.id }"><i style="margin-left:5px;" class="fa fa-edit"></i></a>
			                                 	<a href="javascript:;" onclick="deleteTeacher('${teacher.id }')"><i style="margin-left:5px;" class="fa fa-trash"></i></a>
			                                 </td>
		                            	</tr>
		                            </c:forEach>
	                            </tbody>
	                        </table>
							<!-- 分页 -->
							<jsp:include page="../common/include_page.jsp">
 								<jsp:param value="/aiviews-admin/enterprise/selectTeacher" name="pageTitle"/>
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

	function deleteTeacher(id){
		var f=window.confirm("你确定要删除这项吗？");
		var realm = "${enterprise.realmName }";
		if(f){
			$.ajax({
				url:realm+"/aihudong-duoping-web/teacher/deleteTeacher",
				data:"id="+id,
				type:"post",
				success:function(data){
					if(data=='success'){
						alert("操作成功！");
						window.location="/aiviews-admin/enterprise/selectTeacher";
					}else{
						alert("操作失败");
					}
				}
			})
		}
		
	}
</script>
</html>
