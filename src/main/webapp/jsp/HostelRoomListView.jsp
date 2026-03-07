<%@page import="in.co.rays.project_3.dto.HostelRoomDTO"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="java.util.Iterator"%>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.controller.HostelRoomListCtl"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Hostel Room List</title>

<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/CheckBox11.js"></script>

<style>
.p4 {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/list.png');
	background-repeat: no-repeat;
	background-attachment: fixed;
	background-size: cover;
	padding-top: 85px;
}

.text { text-align: center; }

.table-hover tbody tr:hover td {
	background-color: #0064ff36;
}
</style>
</head>

<body class="p4">

<%@include file="Header.jsp"%>

<form action="<%=ORSView.HOSTELROOM_LIST_CTL%>" method="post">

<jsp:useBean id="dto" class="in.co.rays.project_3.dto.HostelRoomDTO" scope="request" />

<%
int pageNo = ServletUtility.getPageNo(request);
int pageSize = ServletUtility.getPageSize(request);
int index = ((pageNo - 1) * pageSize) + 1;

int nextPageSize = 0;
if(request.getAttribute("nextListSize") != null){
    nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());
}

List list = ServletUtility.getList(request);
if(list == null){
    list = java.util.Collections.emptyList();
}

Iterator<HostelRoomDTO> it = list.iterator();
HashMap<String, String> status = (HashMap<String, String>) request.getAttribute("status");
%>

<center>
	<h1 class="text-primary font-weight-bold pt-3">
		<font color="black">Hostel Room List</font>
	</h1>
</center>
<br>

<!-- ===== SUCCESS MESSAGE ===== -->
<div class="row">
	<div class="col-md-4"></div>

	<% if (!ServletUtility.getSuccessMessage(request).equals("")) { %>
	<div class="col-md-4 alert alert-success alert-dismissible" style="background-color:#80ff80">
		<button type="button" class="close" data-dismiss="alert">&times;</button>
		<h4><font color="#008000"><%=ServletUtility.getSuccessMessage(request)%></font></h4>
	</div>
	<% } %>

	<div class="col-md-4"></div>
</div>

<!-- ===== ERROR MESSAGE ===== -->
<div class="row">
	<div class="col-md-4"></div>

	<% if (!ServletUtility.getErrorMessage(request).equals("")) { %>
	<div class="col-md-4 alert alert-danger alert-dismissible">
		<button type="button" class="close" data-dismiss="alert">&times;</button>
		<h4><font color="red"><%=ServletUtility.getErrorMessage(request)%></font></h4>
	</div>
	<% } %>

	<div class="col-md-4"></div>
</div>

<% if (list.size() != 0) { %>

<!-- ===== SEARCH BAR ===== -->
<div class="row">
	<div class="col-sm-2"></div>

	<div class="col-sm-2">
		<input type="text" name="roomNumber" class="form-control"
			placeholder="Search by room number"
			value="<%=DataUtility.getStringData(dto.getRoomNumber())%>">
	</div>

	<div class="col-sm-2">
		<%= HTMLUtility.getList("status", dto.getStatus(), status) %>
	</div>

	<div class="col-sm-2">
		<input type="text" name="roomType" class="form-control"
			placeholder="Search by room type"
			value="<%=DataUtility.getStringData(dto.getRoomType())%>">
	</div>

	<div class="col-sm-2">
		<input type="submit" class="btn btn-primary btn-md" name="operation" value="<%=HostelRoomListCtl.OP_SEARCH%>">
		<input type="submit" class="btn btn-dark btn-md" name="operation" value="<%=HostelRoomListCtl.OP_RESET%>">
	</div>
</div>

<br>

<!-- ===== TABLE ===== -->
<div class="table-responsive">
<table class="table table-bordered table-dark table-hover">
<thead>
<tr style="background-color: #8C8C8C;">
	<th width="10%"><input type="checkbox" id="select_all"> Select All</th>
	<th>S.No</th>
	<th>Room No</th>
	<th>Room Type</th>
	<th>Capacity</th>
	<th>Rent</th>
	<th>Status</th>
	<th>Edit</th>
</tr>
</thead>

<tbody>
<%
while(it.hasNext()){
    dto = it.next();
%>
<tr>
	<td align="center"><input type="checkbox" name="ids" value="<%=dto.getId()%>"></td>
	<td align="center"><%=index++%></td>
	<td align="center"><%=dto.getRoomNumber()%></td>
	<td align="center"><%=dto.getRoomType()%></td>
	<td align="center"><%=dto.getCapacity()%></td>
	<td align="center"><%=dto.getRent()%></td>
	<td align="center"><%=dto.getStatus()%></td>
	<td align="center"><a href="HostelRoomCtl?id=<%=dto.getId()%>">Edit</a></td>
</tr>
<% } %>
</tbody>
</table>
</div>

<table width="100%">
<tr>
<td>
<input type="submit" name="operation" class="btn btn-warning" value="<%=HostelRoomListCtl.OP_PREVIOUS%>" <%=pageNo>1?"":"disabled"%>>
</td>

<td>
<input type="submit" name="operation" class="btn btn-primary" value="<%=HostelRoomListCtl.OP_NEW%>">
</td>

<td>
<input type="submit" name="operation" class="btn btn-danger" value="<%=HostelRoomListCtl.OP_DELETE%>">
</td>

<td align="right">
<input type="submit" name="operation" class="btn btn-warning" value="<%=HostelRoomListCtl.OP_NEXT%>" <%=nextPageSize!=0?"":"disabled"%>>
</td>
</tr>
</table>

<% } else { %>

<center>
<input type="submit" name="operation" class="btn btn-primary" value="<%=HostelRoomListCtl.OP_BACK%>">
</center>

<% } %>

<input type="hidden" name="pageNo" value="<%=pageNo%>">
<input type="hidden" name="pageSize" value="<%=pageSize%>">

</form>

<%@include file="FooterView.jsp"%>

</body>
</html>
