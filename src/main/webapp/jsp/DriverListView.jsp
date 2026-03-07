<%@page import="in.co.rays.project_3.dto.DriverDTO"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.DriverListCtl"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Driver List</title>

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

<form action="<%=ORSView.DRIVER_LIST_CTL%>" method="post">

<jsp:useBean id="dto" class="in.co.rays.project_3.dto.DriverDTO" scope="request"/>

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

	Iterator<DriverDTO> it = list.iterator();
%>

<center>
	<h1 class="text-primary font-weight-bold pt-3">
		<font color="black">Driver List</font>
	</h1>
</center>

<br>

<!-- SEARCH BAR -->
<div class="row">
	<div class="col-sm-2"></div>

	<div class="col-sm-2">
		<input type="text" name="driverCode" class="form-control"
			placeholder="Driver Code"
			value="<%=DataUtility.getStringData(dto.getDriverCode())%>">
	</div>

	<div class="col-sm-2">
		<input type="text" name="driverName" class="form-control"
			placeholder="Driver Name"
			value="<%=DataUtility.getStringData(dto.getDriverName())%>">
	</div>

	<div class="col-sm-2">
		<input type="text" name="licenseNumber" class="form-control"
			placeholder="License Number"
			value="<%=DataUtility.getStringData(dto.getLicenseNumber())%>">
	</div>

	<div class="col-sm-2">
		<input type="submit" class="btn btn-primary"
			name="operation"
			value="<%=DriverListCtl.OP_SEARCH%>">

		<input type="submit" class="btn btn-dark"
			name="operation"
			value="<%=DriverListCtl.OP_RESET%>">
	</div>
</div>

<br>

<!-- TABLE -->
<div class="table-responsive">
	<table class="table table-bordered table-dark table-hover">
		<thead>
			<tr style="background-color: #8C8C8C;">
				<th width="5%">
					<input type="checkbox" id="select_all"> Select All
				</th>
				<th width="2%">S.No</th>
				<th width="5%">Driver Code</th>
				<th width="5%">Driver Name</th>
				<th width="5%">License</th>
				<th width="5%">Contact</th>
				<th width="5%">Edit</th>
			</tr>
		</thead>

		<tbody>
			<%
			while (it.hasNext()) {
				dto = it.next();
			%>
			<tr>
				<td align="center">
					<input type="checkbox" class="checkbox" name="ids"
						value="<%=dto.getId()%>">
				</td>
				<td align="center"><%=index++%></td>
				<td align="center"><%=dto.getDriverCode()%></td>
				<td align="center"><%=dto.getDriverName()%></td>
				<td align="center"><%=dto.getLicenseNumber()%></td>
				<td align="center"><%=dto.getContactNumber()%></td>
				<td align="center">
					<a href="DriverCtl?id=<%=dto.getId()%>">Edit</a>
				</td>
			</tr>
			<%
			}
			%>
		</tbody>
	</table>
</div>

<!-- PAGINATION BUTTONS -->
<table width="100%">
	<tr>
		<td>
			<input type="submit" name="operation"
				class="btn btn-warning"
				value="<%=DriverListCtl.OP_PREVIOUS%>"
				<%=pageNo > 1 ? "" : "disabled"%>>
		</td>

		<td>
			<input type="submit" name="operation"
				class="btn btn-primary"
				value="<%=DriverListCtl.OP_NEW%>">
		</td>

		<td>
			<input type="submit" name="operation"
				class="btn btn-danger"
				value="<%=DriverListCtl.OP_DELETE%>">
		</td>

		<td align="right">
			<input type="submit" name="operation"
				class="btn btn-warning"
				value="<%=DriverListCtl.OP_NEXT%>"
				<%=nextPageSize != 0 ? "" : "disabled"%>>
		</td>
	</tr>
</table>

<input type="hidden" name="pageNo" value="<%=pageNo%>">
<input type="hidden" name="pageSize" value="<%=pageSize%>">

</form>

<%@include file="FooterView.jsp"%>

</body>
</html>