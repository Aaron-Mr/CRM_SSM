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

		//日历插件
		$(".time").datetimepicker({
			language:'zh-CN', //语言
			format:'yyyy-mm-dd',//日期的格式
			minView:'month', //可以选择的最小视图
			initialDate:new Date(),//初始化显示的日期
			autoclose:true,//设置选择完日期或者时间之后，日否自动关闭日历
			pickerPosition:"top-left"
		});

		pageList(1,2);

		//设置全选
		$("#qx").click(function () {
			$("input[name=xz]").prop("checked",this.checked);
		});

		//取消全选
		$("#clueBody").on("click","input[name=xz]",function () {
			$("#qx").prop("checked",$("input[name=xz]:checked").size() == $("input[name=xz]").size())
		});

		//给删除按钮绑定事件
		$("#deleteClueBtn").click(function () {
			//获取参数
			var $clueList = $("input[name=xz]:checked");

			if ($clueList.length == 0){
				alert("请选择要删除的记录！")
			}else {
				if (confirm("确认要删除选中的记录吗？")){
					var param = "";

					for (var i=0; i<$clueList.length; ++i){
						param += "id="+$clueList[i].value;
						if (i<$clueList.length-1){
							param += "&";
						}
					}

				};

				$.ajax({
					url:"workbench/clue/deleteClueByIds.do",
					type:"post",
					dataType:"json",
					data:param,
					success:function (data) {
						if (data.code==1){
							alert("删除成功！");
							pageList(1,2);
						}else {
							alert("删除失败！")
						}
					}
				})

			}

		});

		//给修改线索的更新按钮绑定事件
		$("#updateClueBtn").click(function () {
			//获取参数
			var id = $("input[name=xz]:checked").val();
			var owner = $("#edit-clueOwner").val();
			var company = $("#edit-company").val();
			var appellation = $("#edit-appellation").val();
			var phone = $("#edit-phone").val();
			var mphone = $("#edit-mphone").val();
			var state = $("#edit-status").val();
			var source = $("#edit-source").val();
			var email = $("#edit-email").val();
			var website = $("#edit-website").val();
			var fullname = $("#edit-fullname").val();
			var contactSummary = $("#edit-contactSummary").val();
			var nextContactTime = $("#edit-nextContactTime").val();
			var describe = $("#edit-describe").val();
			var job = $("#edit-job").val();
			var address = $("#edit-address").val();


			if (company==""){
				alert("公司不能为空！");
				return;
			}
			if (fullname==""){
				alert("姓名不能为空！");
				return;
			}

			$.ajax({
				url:"workbench/clue/editClue.do",
				type:"post",
				dataType:"json",
				data:{
					id:id,
					owner:owner,
					company:company,
					appellation:appellation,
					phone:phone,
					mphone:mphone,
					state:state,
					source:source,
					email:email,
					website:website,
					fullname:fullname,
					contactSummary:contactSummary,
					nextContactTime:nextContactTime,
					describe:describe,
					job:job,
					address:address
				},
				success:function (data) {
					if(data.code==1){
						alert("更新成功！");
						$("#editClueModal").modal("hide");
						pageList(1,2);
					}else {
						alert("更新失败！")
					}
				}
			})

		});

		//给修改按钮绑定事件
		$("#editClueBtn").click(function () {
			var $xz = $("input[name=xz]:checked");
			if($xz.length==0){
				alert("请选择要修改的记录！");
				return;
			}
			if ($xz.length>1){
				alert("暂不支持批量修改，请选择单条记录！");
				return;
			}
			var id = $xz.val();
			//发送请求，给修改模态窗口铺值
			$.ajax({
				url:"workbench/clue/getClueById.do",
				type:"get",
				dataType:"json",
				data:{
					id:id
				},
				success:function (data) {
					$("#edit-clueOwner").val(data.owner);
					$("#edit-company").val(data.company);
					$("#edit-appellation").val(data.appellation);
					$("#edit-phone").val(data.phone);
					$("#edit-mphone").val(data.mphone);
					$("#edit-status").val(data.state);
					$("#edit-source").val(data.source);
					$("#edit-email").val(data.email);
					$("#edit-website").val(data.website);
					$("#edit-fullname").val(data.fullname);
					$("#edit-contactSummary").val(data.contactSummary);
					$("#edit-nextContactTime").val(data.nextContactTime);
					$("#edit-describe").val(data.describe);
					$("#edit-job").val(data.job);
					$("#edit-address").val(data.address);
					//打开修改的模态窗口
					$("#editClueModal").modal("show");
				}
			})

		});


		//给查询按钮绑定事件
		$("#selectClueByConditionBtn").click(function () {
			$("#hidden_fullname").val($("#select_fullname").val());
			$("#hidden_company").val($("#select_company").val());
			$("#hidden_phone").val($("#select_phone").val());
			$("#hidden_owner").val($("#select_owner").val());
			$("#hidden_mphone").val($("#select_mphone").val());
			$("#hidden_source").val($("#select_source").val());
			$("#hidden_state").val($("#select_state").val());
			pageList(1,$("#cluePage").bs_pagination('getOption', 'rowsPerPage'));
		});

		//将当前用户设置为默认所有者
		var id = "${sessionScope.sessionUser.id}";
		$("#create-clueOwner").val(id);

		//给创建线索的保存按钮绑定事件
		$("#saveClueBtn").click(function () {
			//获取参数
			var owner = $("#create-clueOwner").val();
			var company = $("#create-company").val();
			var appellation = $("#create-appellation").val();
			var fullname = $("#create-fullname").val();
			var job = $("#create-job").val();
			var email = $("#create-email").val();
			var phone = $("#create-phone").val();
			var website = $("#create-website").val();
			var mphone = $("#create-mphone").val();
			var state = $("#create-status").val();
			var source = $("#create-source").val();
			var describe = $("#create-describe").val();
			var contactSummary = $("#create-contactSummary").val();
			var nextContactTime = $("#create-nextContactTime").val();
			var address = $("#create-address").val();

			if (company==""){
				alert("公司不能为空！");
				return;
			}
			if (fullname==""){
				alert("姓名不能为空！");
				return;
			}
			alert("state = "+state);

			$.ajax({
				url:"workbench/clue/saveClue.do",
				type:"post",
				dataType:"json",
				data:{
					owner:owner,
					company:company,
					appellation:appellation,
					fullname:fullname,
					job:job,
					email:email,
					phone:phone,
					website:website,
					mphone:mphone,
					state:state,
					source:source,
					describe:describe,
					contactSummary:contactSummary,
					nextContactTime:nextContactTime,
					address:address
				},
				success:function (data) {
					if (data.code==1){
						//关闭模态窗口
						alert("创建成功！");
						$("#createClueModal").modal("hide");
						//刷新线索列表
						pageList(1,2);
					}else {
						alert("创建失败！")
					}
				}
			})

		})

	});

	//分页查询结合条件查询
	function pageList(pageNo,pageSize) {
		//获取参数
		var fullname = $("#hidden_fullname").val();
		var company = $("#hidden_company").val();
		var phone = $("#hidden_phone").val();
		var owner = $("#hidden_owner").val();
		var mphone = $("#hidden_mphone").val();
		var source = $("#hidden_source").val();
		var state = $("#hidden_state").val();

		/*var fullname = $("#select_fullname").val();
		var company = $("#select_company").val();
		var phone = $("#select_phone").val();
		var owner = $("#select_owner").val();
		var mphone = $("#select_mphone").val();
		var source = $("#select_source").val();
		var state = $("#select_state").val();*/

		$.ajax({
			url: "workbench/clue/selectClueByConditionForPage.do",
			type: "get",
			dataType: "json",
			data:{
				fullname:fullname,
				company:company,
				phone:phone,
				owner:owner,
				mphone:mphone,
				source:source,
				state:state,
				pageSize:pageSize,
				pageNo:pageNo
			},
			success:function (data) {
				var html = "";

				//显示总条数
				$("#totalRowsB").text(data.totalRows);

				$.each(data.clueList,function (i,n) {
					html += "<tr>";
					html += "<td><input type='checkbox' name='xz' value='"+n.id+"'/></td>";
					html += "<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/clue/toDetail.do?id="+n.id+"';\">"+n.fullname+n.appellation+"</a></td>";
					html += "<td>"+n.company+"</td>";
					html += "<td>"+n.phone+"</td>";
					html += "<td>"+n.mphone+"</td>";
					html += "<td>"+n.source+"</td>";
					html += "<td>"+n.owner+"</td>";
					html += "<td>"+n.state+"</td>";
					html += "</tr>";
				});

				//显示线索列表
				$("#clueList").html(html);

				//计算总页数
				var totalPages = data.totalRows % pageSize == 0 ? data.totalRows/pageSize : parseInt(data.totalRows/pageSize)+1;

				//对容器调用bs_pagination工具函数，显示翻页信息
				$("#cluePage").bs_pagination({

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
<body id="clueBody">

	<input type="hidden" id="hidden_fullname">
	<input type="hidden" id="hidden_company">
	<input type="hidden" id="hidden_phone">
	<input type="hidden" id="hidden_mphone">
	<input type="hidden" id="hidden_source">
	<input type="hidden" id="hidden_state">
	<input type="hidden" id="hidden_owner">

	<!-- 创建线索的模态窗口 -->
	<div class="modal fade" id="createClueModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">创建线索</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-clueOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-clueOwner">
								  <%--<option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
									<c:forEach items="${userList}" var="user">
										<option value="${user.id}">${user.name}</option>
									</c:forEach>
								</select>
							</div>
							<label for="create-company" class="col-sm-2 control-label">公司<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-company">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-appellation" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-appellation">
								  <option></option>
								  <%--<option>先生</option>
								  <option>夫人</option>
								  <option>女士</option>
								  <option>博士</option>
								  <option>教授</option>--%>
									<c:forEach items="${appellationList}" var="appellation">
										<option value="${appellation.value}">${appellation.value}</option>
									</c:forEach>
								</select>
							</div>
							<label for="create-fullname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-fullname">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-job">
							</div>
							<label for="create-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-email">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-phone">
							</div>
							<label for="create-website" class="col-sm-2 control-label">公司网站</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-website">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-mphone">
							</div>
							<label for="create-status" class="col-sm-2 control-label">线索状态</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-status">
								  <option></option>
									<c:forEach items="${clueStateList}" var="clueState">
										<option value="${clueState.value}">${clueState.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-source" class="col-sm-2 control-label">线索来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-source">
								  <option></option>
									<c:forEach items="${sourceList}" var="source">
										<option value="${source.value}">${source.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						

						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">线索描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>
						
						<div style="position: relative;top: 15px;">
							<div class="form-group">
								<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
								<div class="col-sm-10" style="width: 81%;">
									<textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
								<div class="col-sm-10" style="width: 300px;">
									<input type="text" class="form-control time" id="create-nextContactTime">
								</div>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>
						
						<div style="position: relative;top: 20px;">
							<div class="form-group">
                                <label for="create-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="create-address"></textarea>
                                </div>
							</div>
						</div>
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveClueBtn" >保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改线索的模态窗口 -->
	<div class="modal fade" id="editClueModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">修改线索</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-clueOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-clueOwner">
								  <%--<option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
									  <c:forEach items="${userList}" var="user">
										  <option value="${user.id}">${user.name}</option>
									  </c:forEach>
								</select>
							</div>
							<label for="edit-company" class="col-sm-2 control-label">公司<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-company" value="动力节点">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-appellation" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-appellation">
								  <option></option>
								  <%--<option selected>先生</option>
								  <option>夫人</option>
								  <option>女士</option>
								  <option>博士</option>
								  <option>教授</option>--%>
									<c:forEach items="${appellationList}" var="appellation">
										<option value="${appellation.value}">${appellation.value}</option>
									</c:forEach>
								</select>
							</div>
							<label for="edit-fullname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-fullname" value="李四">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-job" value="CTO">
							</div>
							<label for="edit-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-email" value="lisi@bjpowernode.com">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-phone" value="010-84846003">
							</div>
							<label for="edit-website" class="col-sm-2 control-label">公司网站</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-website" value="http://www.bjpowernode.com">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-mphone" value="12345678901">
							</div>
							<label for="edit-status" class="col-sm-2 control-label">线索状态</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-status">
								  <option></option>
								  <%--<option>试图联系</option>
								  <option>将来联系</option>
								  <option selected>已联系</option>
								  <option>虚假线索</option>
								  <option>丢失线索</option>
								  <option>未联系</option>
								  <option>需要条件</option>--%>
									<c:forEach items="${clueStateList}" var="clueState">
										<option value="${clueState.value}">${clueState.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-source" class="col-sm-2 control-label">线索来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-source">
								  <option></option>
								  <%--<option selected>广告</option>
								  <option>推销电话</option>
								  <option>员工介绍</option>
								  <option>外部介绍</option>
								  <option>在线商场</option>
								  <option>合作伙伴</option>
								  <option>公开媒介</option>
								  <option>销售邮件</option>
								  <option>合作伙伴研讨会</option>
								  <option>内部研讨会</option>
								  <option>交易会</option>
								  <option>web下载</option>
								  <option>web调研</option>
								  <option>聊天</option>--%>
									<c:forEach items="${sourceList}" var="source">
										<option value="${source.value}">${source.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">这是一条线索的描述信息</textarea>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>
						
						<div style="position: relative;top: 15px;">
							<div class="form-group">
								<label for="edit-contactSummary" class="col-sm-2 control-label">联系纪要</label>
								<div class="col-sm-10" style="width: 81%;">
									<textarea class="form-control" rows="3" id="edit-contactSummary">这个线索即将被转换</textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="edit-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
								<div class="col-sm-10" style="width: 300px;">
									<input type="text" class="form-control time" id="edit-nextContactTime" value="2017-05-01">
								</div>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="edit-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="edit-address">北京大兴区大族企业湾</textarea>
                                </div>
                            </div>
                        </div>
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateClueBtn" >更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>线索列表</h3>
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
				      <input class="form-control" type="text" id="select_fullname">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司</div>
				      <input class="form-control" type="text" id="select_company">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司座机</div>
				      <input class="form-control" type="text" id="select_phone">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">线索来源</div>
					  <select class="form-control" id="select_source">
					  	  <option></option>
					  	  <%--<option>广告</option>
						  <option>推销电话</option>
						  <option>员工介绍</option>
						  <option>外部介绍</option>
						  <option>在线商场</option>
						  <option>合作伙伴</option>
						  <option>公开媒介</option>
						  <option>销售邮件</option>
						  <option>合作伙伴研讨会</option>
						  <option>内部研讨会</option>
						  <option>交易会</option>
						  <option>web下载</option>
						  <option>web调研</option>
						  <option>聊天</option>--%>
						  <c:forEach items="${sourceList}" var="source">
							  <option value="${source.value}">${source.value}</option>
						  </c:forEach>
					  </select>
				    </div>
				  </div>
				  
				  <br>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="select_owner">
				    </div>
				  </div>
				  
				  
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">手机</div>
				      <input class="form-control" type="text" id="select_mphone">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">线索状态</div>
					  <select class="form-control" id="select_state">
					  	<option></option>
					  	<%--<option>试图联系</option>
					  	<option>将来联系</option>
					  	<option>已联系</option>
					  	<option>虚假线索</option>
					  	<option>丢失线索</option>
					  	<option>未联系</option>
					  	<option>需要条件</option>--%>
						  <c:forEach items="${clueStateList}" var="clueState">
							  <option value="${clueState.value}">${clueState.value}</option>
						  </c:forEach>
					  </select>
				    </div>
				  </div>

				  <button type="button" class="btn btn-default" id="selectClueByConditionBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 40px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#createClueModal"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editClueBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteClueBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
				
			</div>
			<div style="position: relative;top: 50px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx" /></td>
							<td>名称</td>
							<td>公司</td>
							<td>公司座机</td>
							<td>手机</td>
							<td>线索来源</td>
							<td>所有者</td>
							<td>线索状态</td>
						</tr>
					</thead>
					<tbody id="clueList">
						<%--<tr>
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">李四先生</a></td>
							<td>动力节点</td>
							<td>010-84846003</td>
							<td>12345678901</td>
							<td>广告</td>
							<td>zhangsan</td>
							<td>已联系</td>
						</tr>--%>
                        <%--<tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">李四先生</a></td>
                            <td>动力节点</td>
                            <td>010-84846003</td>
                            <td>12345678901</td>
                            <td>广告</td>
                            <td>zhangsan</td>
                            <td>已联系</td>
                        </tr>--%>
					</tbody>
				</table>
				<div id="cluePage"></div>
			</div>
			
			<%--<div style="height: 50px; position: relative;top: 60px;">
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