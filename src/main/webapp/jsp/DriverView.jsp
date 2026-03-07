<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.dto.DriverDTO"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@page import="in.co.rays.project_3.controller.DriverCtl"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Driver</title>

<style>
.hm {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/driver.jpg');
	background-repeat: no-repeat;
	background-size: cover;
	padding-top: 80px;
}

.card {
	padding: 20px;
}
</style>
</head>

<body class="hm">

	<%@ include file="Header.jsp"%>

	<form action="<%=ORSView.DRIVER_CTL%>" method="post">

		<%
			DriverDTO dto = (DriverDTO) request.getAttribute("dto");
			if (dto == null)
				dto = new DriverDTO();

			long id = dto.getId() != null ? dto.getId() : 0;
		%>

		<div class="container-fluid">
			<div class="row">
				<div class="col-md-4"></div>

				<div class="col-md-4">
					<div class="card">

						<h3 class="text-center text-primary">
							<%=(id > 0) ? "Update Driver" : "Add Driver"%>
						</h3>

						<!-- Success -->
						<%
							if (!ServletUtility.getSuccessMessage(request).equals("")) {
						%>
						<div class="alert alert-success alert-dismissible"
							style="background-color: #80ff80;">
							<button type="button" class="close" data-dismiss="alert">&times;</button>
							<h4>
								<font color="#008000"> <%=ServletUtility.getSuccessMessage(request)%>
								</font>
							</h4>
						</div>
						<%
							}
						%>

						<!-- Error -->
						<%
							if (!ServletUtility.getErrorMessage(request).equals("")) {
						%>
						<div class="alert alert-danger alert-dismissible">
							<button type="button" class="close" data-dismiss="alert">&times;</button>
							<h4>
								<font color="red"> <%=ServletUtility.getErrorMessage(request)%>
								</font>
							</h4>
						</div>
						<%
							}
						%>

						<input type="hidden" name="id" value="<%=id%>">

						<!-- Driver Code -->
						<b>Driver Code</b> <input type="text" class="form-control"
							name="driverCode" placeholder="Enter Drive Code"
							value="<%=DataUtility.getStringData(dto.getDriverCode())%>">
						<font color="red"> <%=ServletUtility.getErrorMessage("driverCode", request)%>
						</font><br>

						<!-- Driver Name -->
						<b>Driver Name</b> <input type="text" class="form-control"
							name="driverName" placeholder="Enter Driver Name"
							value="<%=DataUtility.getStringData(dto.getDriverName())%>">
						<font color="red"> <%=ServletUtility.getErrorMessage("driverName", request)%>
						</font><br>

						<!-- License Number -->
						<b>License Number</b> <input type="text" class="form-control"
							name="licenseNumber" placeholder="Enter Licence"
							value="<%=DataUtility.getStringData(dto.getLicenseNumber())%>">
						<font color="red"> <%=ServletUtility.getErrorMessage("licenseNumber", request)%>
						</font><br>

						<!-- Contact Number -->
						<b>Contact Number</b> <input type="text" class="form-control"
							name="contactNumber" placeholder="Enter Contact Number"
							value="<%=DataUtility.getStringData(dto.getContactNumber())%>">
						<font color="red"> <%=ServletUtility.getErrorMessage("contactNumber", request)%>
						</font>
						<br>

						<div class="text-center">
							<%
								if (id > 0) {
							%>
							<input type="submit" name="operation"
								value="<%=DriverCtl.OP_UPDATE%>" class="btn btn-success">
							<input type="submit" name="operation"
								value="<%=DriverCtl.OP_CANCEL%>" class="btn btn-warning">
							<%
								} else {
							%>
							<input type="submit" name="operation"
								value="<%=DriverCtl.OP_SAVE%>" class="btn btn-success">
							<input type="submit" name="operation"
								value="<%=DriverCtl.OP_RESET%>" class="btn btn-warning">
							<%
								}
							%>
						</div>

					</div>
				</div>

				<div class="col-md-4"></div>
			</div>
		</div>

	</form>

	<%@ include file="FooterView.jsp"%>

</body>
</html>