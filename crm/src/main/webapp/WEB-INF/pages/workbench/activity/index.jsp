<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>


<script type="text/javascript">

	$(function(){

		//将创建市场活动模态窗口中的所有者默认为当前用户
		var id = "${sessionScope.sessionUser.id}";
		$("#create-marketActivityOwner").val(id);

		//日历插件
		$(".time").datetimepicker({
			language:'zh-CN', //语言
			format:'yyyy-mm-dd',//日期的格式
			minView:'month', //可以选择的最小视图
			initialDate:new Date(),//初始化显示的日期
			autoclose:true//设置选择完日期或者时间之后，日否自动关闭日历
		});

		$("#saveBtn").click(function () {
			//获取参数
			var owner = $.trim($("#create-marketActivityOwner").val())
			var name = $.trim($("#create-marketActivityName").val())
			var startDate = $("#create-startTime").val()
			var endDate = $("#create-endTime").val()
			var cost = $.trim($("#create-cost").val())
			var description = $.trim($("#create-describe").val())

			//验证参数
			if (owner==""){
				alert("所有者不能为空！");
				return;
			}
			if (name==""){
				alert("市场活动名称不能为空！");
				return;
			}
			if (endDate<startDate){
				alert("结束时间不能早于开始时间！");
				return;
			}

			var regExp = /^(([1-9]\d*)|0)$/;
			if (!regExp.test(cost)){
				alert("成本只能是非负正数！");
				return;
			}

			//发送ajax请求
			$.ajax({
				url:"workbench/activity/saveActivity.do",
				type:"post",
				dataType:"json",
				data:{
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					description:description
				},
				success:function (data) {
					if (data.code == "0"){
						alert(data.message)
					}else {
						alert("创建成功!");
						$("#createActivityForm")[0].reset();
						$("#createActivityModal").modal("hide")
						pageList(1,2);
					}
				}
			})
		});

		pageList(1,2);

		//给查询按钮绑定事件
		$("#queryBtn").click(function () {
			$("#hidden-name").val($.trim($("#query-name").val()));
			$("#hidden-owner").val($.trim($("#query-owner").val()));
			$("#hidden-startDate").val($.trim($("#query-startDate").val()));
			$("#hidden-endDate").val($.trim($("#query-endDate").val()));
			pageList(1,2);
		});

		//给市场活动列表全选框绑定全选
		$("#qx").click(function () {
			$("input[name=xz]").prop("checked",this.checked);
		});

		//取消全选操作

		//当复选框不是全部选择状态的话，取消全选框的选中状态
		$("#activityBody").on("click","input[name=xz]",function () {
			$("#qx").prop("checked",$("input[name=xz]:checked").size() == $("input[name=xz]").size())
		});

		//给删除按钮绑定事件
		$("#deleteActivityBtn").click(function () {
			//获取选中的市场活动id列表
			var $activityList = $("input[name=xz]:checked");

			if ($activityList.length == 0){
				alert("请选择要删除的市场活动");
				return;
			}else {
				if (confirm("确定要删除选中的市场活动吗？")){
					var param="";

					for (var i=0; i<$activityList.length; ++i){
						param += "id="+$activityList[i].value;
						if (i<$activityList.length-1){
							param += "&";
						}
					}

				}

				$.ajax({
					url:"workbench/activity/deleteActivityByIds.do",
					type:"post",
					dataType:"json",
					data:param,
					success:function (data) {
						if (data.code == 1){
							alert("删除成功！");
							pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
							$("#qx").prop("checked",false);
						}else {
							alert("删除失败！")
						}
					}
				})

			}

		});

		//给修改按钮绑定事件
		$("#ditActivityBtn").click(function () {
			//获取选中的市场活动
			var $activity = $("input[name=xz]:checked");
			if ($activity.length == 0){
				alert("请选择要修改的市场活动！");
				return;
			}else if($activity.length > 1) {
				alert("不支持批量修改操作！");
				return;
			}else {

				//获取id
				var id = $($activity).val();

				//发起Ajax请求
				$.ajax({
					url:"workbench/activity/selectActivityById.do",
					type:"get",
					dataType:"json",
					data:{id:id},
					success:function (data) {
						var html = "";
						$.each(data.userList,function (i,n) {
							html += "<option value='"+n.id+"' >"+n.name+"</option>";
						});

						$("#edit-marketActivityOwner").html(html);

						$("#edit-marketActivityOwner").val(data.activity.owner);
						$("#edit-marketActivityName").val(data.activity.name);
						$("#edit-startTime").val(data.activity.startDate);
						$("#edit-endTime").val(data.activity.endDate);
						$("#edit-cost").val(data.activity.cost);
						$("#edit-describe").val(data.activity.description);
					}
				});

				//打开修改的模态窗口
				$("#editActivityModal").modal("show");

			}
		});

		//给市场活动修改模态窗口中的更新按钮绑定事件
		$("#updateActivityBtn").click(function () {
			//收集参数
			var owner = $("#edit-marketActivityOwner").val();
			var name = $("#edit-marketActivityName").val();
			var startDate = $("#edit-startTime").val();
			var endDate = $("#edit-endTime").val();
			var cost = $("#edit-cost").val();
			var describe = $("#edit-describe").val();
			var id = $($("input[name=xz]:checked")).val();

			//发起请求
			$.ajax({
				url:"workbench/activity/updateActivity.do",
				type:"post",
				dataType:"json",
				data:{
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					describe:describe,
					id:id
				},
				success:function (data) {
					if (data.code == 1){
						alert("更新成功！");
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						$("#editActivityModal").modal("hide");
					}else {
						alert("更新失败！");
					}
				}

			});

		})

		//给下载市场活动按钮绑定事件
		$("#exportActivityAllBtn").click(function () {
			window.location.href="workbench/activity/exportActivity.do"
		});

		//给选择下载市场活动按钮绑定事件
		$("#exportActivityXzBtn").click(function () {
			//获取到所有选中的市场活动id
			var $activityList = $("input[name=xz]:checked");
			var param = "";
			if ($activityList.length==0){
				alert("请选择要导出的记录！");
				return;
			}else {
				for (var i=0; i<$activityList.length; ++i){
					param += "id="+$activityList[i].value;
					if (i<$activityList.length-1){
						param += "&";
					}
				}
			}

			window.location.href="workbench/activity/exportActivityByIds.do?"+param;
		});

		//给导入市场活动按钮绑定事件
		$("#importActivityBtn").click(function () {
			//获取参数
			var activityFileName = $("#activityFile").val();
			//获取后缀名
			var suffix = activityFileName.substr(activityFileName.lastIndexOf(".")+1).toLocaleLowerCase();

			if (suffix!=="xls"){
				alert("仅支持xls文件！");
				return;
			}

			//获取文件内容本身
			var activityFile = $("#activityFile")[0].files[0];

			//判断文件大小
			if (activityFile.size>5*1024*1024){
				alert("文件大小不能超过5MB！");
				return;
			}

			//FormDate是Ajax提供的接口，可以通过键值对的方式向后台传递参数，优点是不仅可以以字符格式传递，而且以二进制传递，所以参数类型没有限制
			var formData = new FormData();
			formData.append("activityFile",activityFile);

			//发起请求
			$.ajax({
				//是否把参数转化成字符，默认是true
				processData:false,
				//是否把参数转化成urlencoded格式，默认是true
				contentType:false,
				url:"workbench/activity/importActivity.do",
				dataType:"json",
				type:"post",
				data:formData,
				success:function (data) {
					if (data.code==1){
						alert("成功导入"+data.retDate+"条记录！");
						$("#importActivityModal").modal("hide");
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
					}else if (data.code==0){
						alert(data.message);
					}
				}
			})

		})

	});

	function pageList(pageNo,pageSize) {
		//分页查询结合条件查询
		//获取参数
		var name = $("#hidden-name").val();
		var owner = $("#hidden-owner").val();
		var startDate = $("#hidden-startDate").val();
		var endDate = $("#hidden-endDate").val();

		$.ajax({
			url:"workbench/activity/queryActivityByConditionForPage.do",
			type:"get",
			dataType:"json",
			data:{
				name:name,
				owner:owner,
				startDate:startDate,
				endDate:endDate,
				pageNo:pageNo,
				pageSize:pageSize
			},
			success:function (data) {
				var html = "";

				//显示总条数
				$("#totalRowsB").text(data.totalRows);

				$.each(data.activityList,function (index,object) {
					html += "<tr class=\"active\">";
					html += "<td><input type=\"checkbox\" name='xz' value='"+object.id+"' /></td>";
					html += "<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/activity/detail.do?id="+object.id+"';\">"+object.name+"</a></td>";
					//html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+object.id+'\';">'+object.name+'</a></td>';
					html += "<td>"+object.owner+"</td>";
					html += "<td>"+object.startDate+"</td>";
					html += "<td>"+object.endDate+"</td>";
					html += "</tr>";
				});

				//显示市场活动列表
				$("#activityListBody").html(html);

				//计算总页数
				var totalPages = data.totalRows % pageSize == 0 ? data.totalRows/pageSize : parseInt(data.totalRows/pageSize)+1;


				//对容器调用bs_pagination工具函数，显示翻页信息
				$("#activityPage").bs_pagination({

					currentPage:pageNo,//当前页号,相当于pageNo
					rowsPerPage:pageSize,//每页显示条数,相当于pageSize
					totalRows:data.totalRows,//总条数
					totalPages: totalPages,  //总页数,必填参数.

					visiblePageLinks:3,//最多可以显示的卡片数

					showGoToPage:true,//是否显示"跳转到"部分,默认true--显示
					showRowsPerPage:true,//是否显示"每页显示条数"部分。默认true--显示
					showRowsInfo:true,//是否显示记录的信息，默认true--显示

					//用户每次切换页号，都自动触发本函数;
					//每次返回切换页号之后的pageNo和pageSize
					onChangePage: function(event,pageObj) { // returns page_num and rows_per_page after a link has clicked
						pageList(pageObj.currentPage,pageObj.rowsPerPage);
					}

				});

			}
		})
	}

</script>
</head>
<body id="activityBody">


	<%--用来存放查询条件的隐藏域--%>
	<input type="hidden" id="hidden-name" />
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-startDate"/>
	<input type="hidden" id="hidden-endDate"/>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="createActivityForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
								  <%--<option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
									<c:forEach items="${userList}" var="u">
										<option value="${u.id}">${u.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startTime">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endTime">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
									<%--<option>zhangsan</option>
                                    <option>lisi</option>
                                    <option>wangwu</option>--%>
									<%--<c:forEach items="${userList}" var="u">
										<option id="${u.id}">${u.name}</option>
									</c:forEach>--%>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-startTime" value="">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-endTime" value="">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateActivityBtn" >更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" id="query-name" type="text">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" id="query-owner" type="text">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="query-startTime" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="query-endTime">
				    </div>
				  </div>
				  
				  <button type="button" id="queryBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#createActivityModal"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="ditActivityBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteActivityBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityListBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>--%>
                        <%--<tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
				<div id="activityPage"></div>
			</div>
			
			<%--<div style="height: 50px; position: relative;top: 30px;">
				<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b id="totalRowsB"></b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>
			</div>--%>
			
		</div>
		
	</div>
</body>
</html>