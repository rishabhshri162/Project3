<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.project_3.dto.CateringDTO"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@page import="in.co.rays.project_3.controller.CateringCtl"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Catering</title>

<style>
.hm {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/catering.jpg');
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

	<form action="<%=ORSView.CATERING_CTL%>" method="post">

		<%
			CateringDTO dto = (CateringDTO) request.getAttribute("dto");
			if (dto == null)
				dto = new CateringDTO();

			long id = dto.getId() != null ? dto.getId() : 0;

			HashMap menuMap = (HashMap) request.getAttribute("menuMap");
			if (menuMap == null)
				menuMap = new HashMap();
		%>

		<div class="container-fluid">
			<div class="row">

				<div class="col-md-4"></div>

				<div class="col-md-4">
					<div class="card">

						<h3 class="text-center text-primary">
							<%=(id > 0) ? "Update Catering" : "Add Catering"%>
						</h3>

						<!-- Success Message -->
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

						<!-- Error Message -->
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

						<input type="hidden" name="id" value="<%=id%>"> <input
							type="hidden" name="createdBy" value="<%=dto.getCreatedBy()%>">
						<input type="hidden" name="modifiedBy"
							value="<%=dto.getModifiedBy()%>"> <input type="hidden"
							name="createdDatetime"
							value="<%=DataUtility.getTimestamp(dto.getCreatedDatetime())%>">
						<input type="hidden" name="modifiedDatetime"
							value="<%=DataUtility.getTimestamp(dto.getModifiedDatetime())%>">

						<!-- ================= FORM FIELDS ================= -->

						<!-- Vendor Name -->
						<b>Vendor Name *</b> <input type="text" class="form-control"
							name="vendorName" placeholder="Enter Vendor Name"
							value="<%=DataUtility.getStringData(dto.getVendorName())%>">

						<font color="red"> <%=ServletUtility.getErrorMessage("vendorName", request)%>
						</font><br>

						<!-- Menu Type -->
						<b>Menu Type *</b>

						<%=HTMLUtility.getList("menuType", dto.getMenuType(), menuMap)%>

						<font color="red"> <%=ServletUtility.getErrorMessage("menuType", request)%>
						</font><br>

						<!-- Cost -->
						<b>Cost *</b> <input type="text" class="form-control" name="cost"
							placeholder="Enter Cost"
							value="<%=DataUtility.getStringData(dto.getCost())%>"> <font
							color="red"> <%=ServletUtility.getErrorMessage("cost", request)%>
						</font><br>
						<br>

						<!-- ================= BUTTONS ================= -->

						<div class="text-center">

							<%
								if (id > 0) {
							%>

							<input type="submit" name="operation"
								value="<%=CateringCtl.OP_UPDATE%>" class="btn btn-success">

							<input type="submit" name="operation"
								value="<%=CateringCtl.OP_CANCEL%>" class="btn btn-warning">

							<%
								} else {
							%>

							<input type="submit" name="operation"
								value="<%=CateringCtl.OP_SAVE%>" class="btn btn-success">

							<input type="submit" name="operation"
								value="<%=CateringCtl.OP_RESET%>" class="btn btn-warning">

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