<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.project_3.dto.HostelRoomDTO"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@page import="in.co.rays.project_3.controller.HostelRoomCtl"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Hostel Room</title>

<style>
.hm {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/50913.jpg');
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

	<form action="<%=ORSView.HOSTELROOM_CTL%>" method="post">

		<%
			/* ================= DTO (ABSOLUTELY SAFE) ================= */
			HostelRoomDTO dto = (HostelRoomDTO) request.getAttribute("dto");
			if (dto == null) {
				dto = new HostelRoomDTO();
			}

			Long idObj = dto.getId(); // 👈 wrapper safe
			long id = (idObj != null) ? idObj : 0; // 👈 NEVER NULL

			/* ================= DROPDOWN MAPS ================= */
			HashMap roomTypeMap = (HashMap) request.getAttribute("roomTypeMap");
			if (roomTypeMap == null) {
				roomTypeMap = new HashMap();
			}

			HashMap statusMap = (HashMap) request.getAttribute("statusMap");
			if (statusMap == null) {
				statusMap = new HashMap();
			}
		%>

		<div class="container">
			<div class="row">
				<div class="col-md-4"></div>

				<div class="col-md-4">
					<div class="card">

						<!-- ===== Heading ===== -->
						<h3 class="text-center text-primary">
							<%=(id > 0) ? "Update Hostel Room" : "Add Hostel Room"%>
						</h3>

						<!-- ===== SUCCESS MESSAGE ===== -->
						<div class="row">
							<div class="col-md-12 text-center">

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

							</div>
						</div>

						<!-- ===== ERROR MESSAGE ===== -->
						<div class="row">
							<div class="col-md-12 text-center">

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

							</div>
						</div>
						<!-- ===== Hidden Fields ===== -->
						<input type="hidden" name="id" value="<%=id%>"> <input
							type="hidden" name="createdBy" value="<%=dto.getCreatedBy()%>">
						<input type="hidden" name="modifiedBy"
							value="<%=dto.getModifiedBy()%>"> <input type="hidden"
							name="createdDatetime"
							value="<%=DataUtility.getTimestamp(dto.getCreatedDatetime())%>">
						<input type="hidden" name="modifiedDatetime"
							value="<%=DataUtility.getTimestamp(dto.getModifiedDatetime())%>">

						<!-- ===== Room Number ===== -->
						<label>Room Number <span style="color: red">*</span></label> <input
							type="text" name="roomNumber" class="form-control" placeholder="Enter room number"
							value="<%=DataUtility.getStringData(dto.getRoomNumber())%>">
						<font color="red"> <%=ServletUtility.getErrorMessage("roomNumber", request)%>
						</font> <br>

						<!-- ===== Room Type ===== -->
						<label>Room Type <span style="color: red">*</span></label>
						<%=HTMLUtility.getList("roomType", dto.getRoomType(), roomTypeMap)%>
						<font color="red"> <%=ServletUtility.getErrorMessage("roomType", request)%>
						</font> <br> <br>

						<!-- ===== Capacity ===== -->
						<label>Capacity <span style="color: red">*</span></label> <input
							type="text" name="capacity" class="form-control" placeholder="Enter capacity"
							value="<%=(dto.getCapacity() != null) ? dto.getCapacity() : ""%>">

						<font color="red"> <%=ServletUtility.getErrorMessage("capacity", request)%>
						</font> <br>

						<!-- ===== Rent ===== -->
						<label>Rent <span style="color: red">*</span></label><input
							type="text" name="rent" class="form-control" placeholder="Enter rent"
							value="<%=(dto.getRent() != null) ? dto.getRent() : ""%>">

						<font color="red"> <%=ServletUtility.getErrorMessage("rent", request)%>
						</font> <br>

						<!-- ===== Status ===== -->
						<label>Status <span style="color: red">*</span></label>
						<%=HTMLUtility.getList("status", dto.getStatus(), statusMap)%>
						<font color="red"> <%=ServletUtility.getErrorMessage("status", request)%>
						</font> <br> <br>

						<!-- ===== Buttons ===== -->
						<div class="text-center">
							<%
								if (id > 0) {
							%>
							<input type="submit" name="operation"
								value="<%=HostelRoomCtl.OP_UPDATE%>" class="btn btn-success">
							<input type="submit" name="operation"
								value="<%=HostelRoomCtl.OP_CANCEL%>" class="btn btn-warning">
							<%
								} else {
							%>
							<input type="submit" name="operation"
								value="<%=HostelRoomCtl.OP_SAVE%>" class="btn btn-success">
							<input type="submit" name="operation"
								value="<%=HostelRoomCtl.OP_RESET%>" class="btn btn-warning">
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
