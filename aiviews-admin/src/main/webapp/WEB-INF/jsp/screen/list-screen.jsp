<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>

<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>屏幕管理</title>
<jsp:include page="../common/include_css.jsp" />
</head>

<body class="gray-bg">
	<div class="wrapper wrapper-content animated fadeInRight">
		<div class="row">
			<div class="col-sm-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>
							<small>屏幕管理</small>
						</h5>
						<div class="ibox-tools">
							<a class="collapse-link"> <i class="fa fa-chevron-up"></i>
							</a>
						</div>
					</div>
					<div class="ibox-content">
						<div class="dataTables_wrapper form-inline" role="grid">
							<!-- 查询条件 -->
							<!-- <form method="post" id="searchForm"
								action="/aihudong-duoping-web/screen/showAllScreen">
								<div class="row">
									<div class="col-sm-2">
										<div class="dataTables_length">
											<a href="/aihudong-duoping-web/screen/toUpdateScreen"
												class="btn btn-primary ">新增屏幕</a>
										</div>
									</div>
								</div>
							</form> -->
							<!-- 查询条件结束 -->
							<table
								class="table table-striped table-bordered table-hover dataTables-example">
								<thead>
									<tr>
										<th>校区</th>
										<th>教学楼</th>
										<th>教室</th>
										<th>屏幕账号</th>
										<th>屏幕分配者</th>
										<th>使用总时长</th>
										<th>使用总人次</th>
										<th>屏幕类型</th>
										<c:if test="${admin.power==0 || admin.power==2}">
											<th>操作</th>
										</c:if>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${screenList }" var="screen">
										<tr class="gradeA">
											<td>${screen.room.building.zone.zoneName }</td>
											<td>${screen.room.building.buildingName }</td>
											<td>${screen.room.num }</td>
											<td>${screen.username }</td>
											<td>${screen.admin.truename }</td>
											<td>${screen.duration }</td>
											<td>${screen.number }</td>
											<td>
												<c:if test="${screen.type=='1' }">触摸屏</c:if>
												<c:if test="${screen.type=='2' }">文档屏</c:if>
												<c:if test="${screen.type=='3' }">投影</c:if>
												<c:if test="${screen.type=='4' }">电视</c:if>
												<c:if test="${screen.type=='5' }">临时屏幕</c:if>
											</td>
											<c:if test="${admin.power==0 || admin.power==2}">
												<td><a
													href="/aihudong-duoping-web/screen/toUpdateScreen?id=${screen.id }"><i
														style="margin-left: 5px;" class="fa fa-edit"></i></a> <a
													href="javascript:;" onclick="deleteScreen('${screen.id }')"><i
														style="margin-left: 5px;" class="fa fa-trash"></i></a> <a
													href="/aihudong-duoping-web/screen/screenTransfer?id=${screen.id }"><i
														style="margin-left: 5px;" class="fa fa-arrow-right"></i></a></td>
											</c:if>
										</tr>
									</c:forEach>
								</tbody>
							</table>
							<!-- 分页 -->
							<jsp:include page="../common/include_page.jsp">
								<jsp:param value="/aiviews-admin/enterprise/selectScreen"
									name="pageTitle" />
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
//JS校验form表单信息  
function checkData(){  
   var fileDir = $("#upfile").val();  
   var suffix = fileDir.substr(fileDir.lastIndexOf("."));  
   if("" == fileDir){  
       alert("选择需要导入的Excel文件！");  
       return false;  
   }  
   if(".xls" != suffix && ".xlsx" != suffix ){  
       alert("选择Excel格式的文件导入！");  
       return false;  
   }  
   return true;  
} 


	function deleteScreen(id){
		var f=window.confirm("你确定要删除这项吗？");
		if(f){
			$.ajax({
				url:"/aihudong-duoping-web/screen/deleteScreen",
				data:"id="+id,
				type:"post",
				success:function(data){
					if(data=='success'){
						alert("操作成功！");
						window.location="/aihudong-duoping-web/screen/showAllScreen";
					}else{
						alert("操作失败");
					}
				}
			})
		}
		
	}
</script>
</html>
