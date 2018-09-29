<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>企业管理</title>
    <jsp:include page="common/include_css.jsp" />
</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5><small>企业管理</small></h5>
                        <div class="ibox-tools">
                            <a class="collapse-link">
                                <i class="fa fa-chevron-up"></i>
                            </a>
                        </div>
                    </div>
                    <div class="ibox-content">
                    	<div  class="dataTables_wrapper form-inline" role="grid">
                    	
                    		<form method="post" id="searchForm">
				<div class="row">
					<!-- <div class="col-sm-2">
				<div class="dataTables_length">
					<a href="/aihudong-duoping-web/admin/toUpdate" class="btn btn-primary ">新增用户</a>
				</div>
			</div> -->
					<div class="col-sm-10" id="group">
						<div class="input-group">
							<!-- 根据教师、学生、屏幕查询 -->
							<select class="form-control" name="category"
								onchange="selectScreen(this)">
								<option value="">---种类---</option>
								<c:if test="${admin.power==0 || admin.power==1 }">
									<option value="1" selected>教师</option>
									<option value="2">学生</option>
								</c:if>
								<c:if test="${admin.power==0 || admin.power==2 }">
									<option value="4">屏幕</option>
								</c:if>
							</select>
						</div>
						<%-- <div id="facultySelect" class="input-group">
							<!-- 根据院系、专业查询 -->
							<select class="form-control" onchange="changeSubject(this)" name="facultyId">
								<option value="">---院系---</option>
								<c:forEach items="${facultyList }" var="faculty">
									<option value="${faculty.id}">${faculty.facultyName }</option>
								</c:forEach>
							</select>
						</div>
						<div id="subjectSelect" class="input-group">
							<select class="form-control" id="subjectSelected" name="subjectId">
								<option value="">---专业---</option>
							</select>
						</div> --%>
						<div id='time' class='input-group'>
							<select class='form-control' name='interval'>
								<option value=''>---时间---</option>
								<option value='1'>一天</option>
								<option value='7' selected>一周</option>
								<option value='30'>一个月</option>
								<option value='180'>半年</option>
							</select>
						</div>
						&nbsp;&nbsp;日期：&nbsp;&nbsp;
						<input type="text" id="test1" class="form-control" name="time">
						<div class="input-group" style="float: right;">
							<!-- 真实姓名 -->
							<%-- <input type="text" name="username" value="${teacher.username }"
						class="form-control" placeholder="关键字查找"> --%>
							<div class="input-group-btn">
								<button class="btn btn-primary">搜索</button>
							</div>
						</div>
					</div>
				</div>
			</form>
                    		<div id="main" style="height: 400px; width: 900px"></div>
                    		 <table class="table table-striped table-bordered table-hover dataTables-example">
	                            <thead>
	                                <tr>
	                                	<th>企业总数</th>
	                                    <th>教师总数</th>
	                                    <th>学生总数</th>
	                                    <th>屏幕总数</th>
	                                </tr>
	                            </thead>
	                            <tbody>
                            		<tr class="gradeA" id="dataTable">
	                            	</tr>
	                            </tbody>
	                        </table>
                    	
                    	
                    		<!-- 查询条件 -->
	                    	<form method="post" id="searchForm" action="/aihudong-duoping-web/student/showAllStudent">
		                    	<div class="row">
		                    		<div class="col-sm-2">
		                    			<div class="dataTables_length">
		                    				<a href="/aiviews-admin/enterprise/toEditEnterprise" class="btn btn-primary ">新增</a>
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
			                                 	<a href="/aiviews-admin/enterprise/showEnterpriseDetail?id=${enterprise.id }"><i style="margin-left:5px;" class="fa fa-eye"></i></a>
			                                 	<a href="/aiviews-admin/enterprise/toEditEnterprise?id=${enterprise.id }"><i style="margin-left:5px;" class="fa fa-edit"></i></a>
			                                 	<a href="javascript:;" onclick="deleteEnterprise('${enterprise.id }')"><i style="margin-left:5px;" class="fa fa-trash"></i></a>
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
	$.ajax({
		url:"/aiviews-admin/enterprise/getEnterpriseData",
		type:"post",
		success:function(data){
			$("#dataTable").append("<td>"+data.totalNum+"</td><td>"+data.teacherNum+"</td><td>"+data.studentNum+"</td><td>"+data.screenNum+"</td>");
		}
	})

	function deleteEnterprise(){
		var f=window.confirm("你确定要删除该项吗？");
		if(f){
			$.ajax({
				url:"/aiviews-admin/enterprise/deleteEnterprise",
				data:"id="+id,
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
	}
	
	
	
	$(function() {
		getEchartsData();
		$('.btn-primary').on('click',function() {
			getEchartsData();
			return false;
		})
		laydate.render({
			  elem: '#test1', //指定元素
			  istime: false,
			  istoday: true,
			  key: '2018-1-10',
			  issure: true
			});
		$("#test1").value=new Date();
	})

	function changeSubject(object){
	var index=object.value;
	var arrayId = new Array();
	var arrayName = new Array();
	/* <c:forEach items="${facultyList }" var="e">
		<c:forEach items="${e.subjectList }" var="subject"> 
			if(${subject.facultyId}==index){
				arrayName.push("${subject.subjectName}"); //js中可以使用此标签，将EL表达式中的值push到数组中
				arrayId.push(${subject.id});
			}
		</c:forEach>
	</c:forEach> */
	$("#subjectSelected").empty();
	$("#subjectSelected").append("<option value=''>---专业---</option>");

	for(var i=0;i<arrayName.length;i++){
		$("#subjectSelected").append("<option value='"+arrayId[i]+"' >"+arrayName[i]+"</option>");
	}
	}

	//基于准备好的dom，初始化echarts图表
	var myChart = echarts.init(document.getElementById("main"));
	var option = {
		title : {
			text : '产品使用时长统计',
			subtext : '模拟'
		},
		tooltip : {
			trigger : 'axis'
		},
		legend : {
			data : [ '时长']
		},
		//右上角工具条
		toolbox : {
			show : true,
			feature : {
				mark : {
					show : true
				},
				dataView : {
					show : true,
					readOnly : false
				},
				magicType : {
					show : true,
					type : [ 'line', 'bar' ]
				},
				restore : {
					show : true
				},
				saveAsImage : {
					show : true
				}
			}
		},
		calculable : true,
		xAxis : [ {
			type : 'category',
			boundaryGap : false,
			data : [  ]
		} ],
		yAxis : [ {
			type : 'value',
			axisLabel : {
				formatter : '{value} /h'
			}
		} ],
		series : [ {
			name : '时长',
			type : 'line',
			data : [ ],
			markPoint : {
				data : [ {
					type : 'max',
					name : '最大值'
				}, {
					type : 'min',
					name : '最小值'
				} ]
			},
			markLine : {
				data : [ {
					type : 'average',
					name : '平均值'
				} ]
			}
		}]
	};
	myChart.showLoading();

	function getEchartsData(){
		var names=[];    //类别数组（实际用来盛放X轴坐标值）
		var nums=[];    //销量数组（实际用来盛放Y坐标值）
	$.ajax({  
	    type : "post",  
	    async : true, //异步执行  
	    data:$("#searchForm").serialize(),
	    url:"/aiviews-admin/enterprise/getRecordEchartsData",  
	    dataType : "json", //返回数据形式为json  
	    success : function(json) {  
	    	/* var json = eval('(' + json + ')');  */
	           for(var i=0;i<json.xAxisData.length;i++){       
	               names.push(json.xAxisData[i]);    //挨个取出类别并填入类别数组
	             }
	            for(var i=0;i<json.seriesList.length;i++){       
	                nums.push(json.seriesList[i].hour);    //挨个取出销量并填入销量数组
	              }
	           myChart.hideLoading();  
	           myChart.setOption({  
	        	               
	           xAxis:{  
	              data:names  
	           },   
	           series:{
	        	   data:nums
	           },
	           yAxis :[{
	       		type : 'value',
		       		axisLabel : {
		       			formatter : '{value} /'+json.type
		       		}
	       		}],
	           markPoint : {
				data : [ {
					type : 'max',
					name : '最大值'
				}, {
					type : 'min',
					name : '最小值'
				} ]
				},
				markLine : {
					data : [ {
						type : 'average',
						name : '平均值'
					} ]
				}   
	          });
	    },  
	    error : function(errorMsg) {  
	        alert("请求数据失败");  
	    }  
	});  }
	// 为echarts对象加载数据
	myChart.setOption(option);

</script>
</html>
