<%@page import="in.co.rays.project_3.dto.MeetingDTO"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="java.util.Iterator"%>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.controller.MeetingListCtl"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Meeting List</title>

<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/CheckBox11.js"></script>

<style>
.p4 {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/meeting2.jpg');
	background-repeat: no-repeat;
	background-attachment: fixed;
	background-size: cover;
	padding-top: 85px;
}

.table-hover tbody tr:hover td {
	background-color: #0064ff36;
}
</style>
</head>

<body class="p4">

	<%@include file="Header.jsp"%>

	<form action="<%=ORSView.MEETING_LIST_CTL%>" method="post">

		<jsp:useBean id="dto" class="in.co.rays.project_3.dto.MeetingDTO"
			scope="request" />

		<%
			int pageNo = ServletUtility.getPageNo(request);
			int pageSize = ServletUtility.getPageSize(request);
			int index = ((pageNo - 1) * pageSize) + 1;

			int nextPageSize = 0;
			if (request.getAttribute("nextListSize") != null) {
				nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());
			}

			List list = ServletUtility.getList(request);
			if (list == null) {
				list = java.util.Collections.emptyList();
			}

			Iterator<MeetingDTO> it = list.iterator();
			HashMap<String, String> statusMap = (HashMap<String, String>) request.getAttribute("statusMap");
		%>

		<center>
			<h1 class="text-primary font-weight-bold pt-3">
				<font color="black">Meeting List</font>
			</h1>
		</center>
		<br>

		<!-- SUCCESS MESSAGE -->
		<%
			if (!ServletUtility.getSuccessMessage(request).equals("")) {
		%>
		<div class="alert alert-success alert-dismissible"
			style="background-color: #80ff80; width: 40%; margin: auto;">
			<button type="button" class="close" data-dismiss="alert">&times;</button>
			<h4>
				<font color="#008000"> <%=ServletUtility.getSuccessMessage(request)%>
				</font>
			</h4>
		</div>
		<%
			}
		%>

		<!-- ERROR MESSAGE -->
		<%
			if (!ServletUtility.getErrorMessage(request).equals("")) {
		%>
		<div class="alert alert-danger alert-dismissible"
			style="width: 40%; margin: auto;">
			<button type="button" class="close" data-dismiss="alert">&times;</button>
			<h4>
				<font color="red"> <%=ServletUtility.getErrorMessage(request)%>
				</font>
			</h4>
		</div>
		<%
			}
		%>

		<%
			if (list.size() != 0) {
		%>

		<!-- SEARCH BAR -->
		<div class="row">
			<div class="col-sm-2"></div>

			<div class="col-sm-2">
				<input type="text" name="meetingCode" class="form-control"
					placeholder="Meeting Code"
					value="<%=DataUtility.getStringData(dto.getMeetingCode())%>">
			</div>

			<div class="col-sm-2">
				<input type="text" name="meetingTitle" class="form-control"
					placeholder="Meeting Title"
					value="<%=DataUtility.getStringData(dto.getMeetingTitle())%>">
			</div>

			<div class="col-sm-2">
				<%=HTMLUtility.getList("meetingStatus", dto.getMeetingStatus(), statusMap)%>
			</div>

			<div class="col-sm-2">
				<input type="submit" class="btn btn-primary" name="operation"
					value="<%=MeetingListCtl.OP_SEARCH%>"> <input type="submit"
					class="btn btn-dark" name="operation"
					value="<%=MeetingListCtl.OP_RESET%>">
			</div>
		</div>

		<br>

		<!-- TABLE -->
		<div class="table-responsive">
			<table class="table table-bordered table-dark table-hover">
				<thead>
					<tr style="background-color: #8C8C8C;">
						<th width="5%"><input type="checkbox" id="select_all"
							name="Select" class="text"> Select All</th>
						<th width="2%" class="text">S.No</th>
						<th width="5%" class="text">Meeting Code</th>
						<th width="5%" class="text">Title</th>
						<th width="5%" class="text">Date</th>
						<th width="5%" class="text">Location</th>
						<th width="5%" class="text">Status</th>
						<th width="5%" class="text">Edit</th>
					</tr>
				</thead>

				<tbody>
					<%
						while (it.hasNext()) {
								dto = it.next();
					%>
					<tr>
						<td align="center"><input type="checkbox"  class= "checkbox" name="ids"
							value="<%=dto.getId()%>"></td>
						<td align="center"><%=index++%></td>
						<td align="center"><%=dto.getMeetingCode()%></td>
						<td align="center"><%=dto.getMeetingTitle()%></td>
						<td align="center"><%=DataUtility.getStringData(dto.getMeetingTime())%>
						</td>
						<td align="center"><%=dto.getMeetingLocation()%></td>
						<td align="center"><%=dto.getMeetingStatus()%></td>
						<td align="center"><a href="MeetingCtl?id=<%=dto.getId()%>">
								Edit </a></td>
					</tr>
					<%
						}
					%>
				</tbody>
			</table>
		</div>

		<table width="100%">
			<tr>
				<td><input type="submit" name="operation"
					class="btn btn-warning" value="<%=MeetingListCtl.OP_PREVIOUS%>"
					<%=pageNo > 1 ? "" : "disabled"%>></td>

				<td><input type="submit" name="operation"
					class="btn btn-primary" value="<%=MeetingListCtl.OP_NEW%>">
				</td>

				<td><input type="submit" name="operation"
					class="btn btn-danger" value="<%=MeetingListCtl.OP_DELETE%>">
				</td>

				<td align="right"><input type="submit" name="operation"
					class="btn btn-warning" value="<%=MeetingListCtl.OP_NEXT%>"
					<%=nextPageSize != 0 ? "" : "disabled"%>></td>
			</tr>
		</table>

		<%
			} else {
		%>

		<center>
			<input type="submit" name="operation" class="btn btn-primary"
				value="<%=MeetingListCtl.OP_BACK%>">
		</center>

		<%
			}
		%>

		<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
			type="hidden" name="pageSize" value="<%=pageSize%>">

	</form>

	<%@include file="FooterView.jsp"%>

</body>
</html>