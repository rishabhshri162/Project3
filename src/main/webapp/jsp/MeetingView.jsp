<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.project_3.dto.MeetingDTO"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@page import="in.co.rays.project_3.controller.MeetingCtl"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Meeting</title>

<style>
.hm {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/meeting.jpg');
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
	<%@include file="calendar.jsp"%>

	<form action="<%=ORSView.MEETING_CTL%>" method="post">

		<%
			MeetingDTO dto = (MeetingDTO) request.getAttribute("dto");
			if (dto == null)
				dto = new MeetingDTO();

			long id = dto.getId() != null ? dto.getId() : 0;

			HashMap statusMap = (HashMap) request.getAttribute("statusMap");
			if (statusMap == null)
				statusMap = new HashMap();
		%>

		<div class="container-fluid">
			<div class="row">

				<div class="col-md-4"></div>

				<div class="col-md-4">
					<div class="card">

						<h3 class="text-center text-primary">
							<%=(id > 0) ? "Update Meeting" : "Schedule Meeting"%>
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

						<input type="hidden" name="id" value="<%=id%>"> <input
							type="hidden" name="createdBy" value="<%=dto.getCreatedBy()%>">
						<input type="hidden" name="modifiedBy"
							value="<%=dto.getModifiedBy()%>"> <input type="hidden"
							name="createdDatetime"
							value="<%=DataUtility.getTimestamp(dto.getCreatedDatetime())%>">
						<input type="hidden" name="modifiedDatetime"
							value="<%=DataUtility.getTimestamp(dto.getModifiedDatetime())%>">

						<!-- ================= FORM FIELDS ================= -->

						<!-- Meeting Code -->
						<b>Meeting Code *</b> <input type="text" class="form-control"
							name="meetingCode" placeholder="Enter Meeting Code"
							value="<%=DataUtility.getStringData(dto.getMeetingCode())%>">
						<font color="red"> <%=ServletUtility.getErrorMessage("meetingCode", request)%>
						</font><br>

						<!-- Meeting Title -->
						<b>Meeting Title *</b> <input type="text" class="form-control"
							name="meetingTitle" placeholder="Enter Meeting Title"
							value="<%=DataUtility.getStringData(dto.getMeetingTitle())%>">
						<font color="red"> <%=ServletUtility.getErrorMessage("meetingTitle", request)%>
						</font><br>

						<!-- Meeting Date (Meeting Time as Date) -->
						<b>Meeting Date *</b> <input type="text" id="udate5"
							name="meetingTime" class="form-control"
							placeholder="Select Meeting Date" readonly="readonly"
							value="<%=DataUtility.getStringData(dto.getMeetingTime())%>">
						<font color="red"> <%=ServletUtility.getErrorMessage("meetingTime", request)%>
						</font><br>

						<!-- Meeting Location -->
						<b>Meeting Location *</b> <input type="text" class="form-control"
							name="meetingLocation" placeholder="Enter Meeting Location"
							value="<%=DataUtility.getStringData(dto.getMeetingLocation())%>">
						<font color="red"> <%=ServletUtility.getErrorMessage("meetingLocation", request)%>
						</font><br>

						<!-- Meeting Status -->
						<b>Meeting Status *</b>
						<%=HTMLUtility.getList("meetingStatus", dto.getMeetingStatus(), statusMap)%>
						<font color="red"> <%=ServletUtility.getErrorMessage("meetingStatus", request)%>
						</font><br>
						<br>

						<!-- ================= BUTTONS ================= -->

						<div class="text-center">
							<%
								if (id > 0) {
							%>
							<input type="submit" name="operation"
								value="<%=MeetingCtl.OP_UPDATE%>" class="btn btn-success">
							<input type="submit" name="operation"
								value="<%=MeetingCtl.OP_CANCEL%>" class="btn btn-warning">
							<%
								} else {
							%>
							<input type="submit" name="operation"
								value="<%=MeetingCtl.OP_SAVE%>" class="btn btn-success">
							<input type="submit" name="operation"
								value="<%=MeetingCtl.OP_RESET%>" class="btn btn-warning">
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