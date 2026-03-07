<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.project_3.dto.TrainTicketDTO"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@page import="in.co.rays.project_3.controller.TrainTicketCtl"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Train Ticket</title>

<style>
.hm {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/train.jpg');
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

	<form action="<%=ORSView.TRAINTICKET_CTL%>" method="post">

		<%
			TrainTicketDTO dto = (TrainTicketDTO) request.getAttribute("dto");
			if (dto == null)
				dto = new TrainTicketDTO();

			long id = dto.getId() != null ? dto.getId() : 0;

			HashMap classMap = (HashMap) request.getAttribute("classMap");
			if (classMap == null)
				classMap = new HashMap();
		%>

		<div class="container-fluid">
			<div class="row">

				<div class="col-md-4"></div>

				<div class="col-md-4">
					<div class="card">

						<h3 class="text-center text-primary">
							<%=(id > 0) ? "Update Train Ticket" : "Book Train Ticket"%>
						</h3>

						<!-- Success -->
						<%
							if (!ServletUtility.getSuccessMessage(request).equals("")) {
						%>
						<div class="alert alert-success alert-dismissible"
							style="background-color: #80ff80;">
							<button type="button" class="close" data-dismiss="alert">&times;</button>
							<h4>
								<font color="#008000"><%=ServletUtility.getSuccessMessage(request)%></font>
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
								<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
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

						<div class="md-form">

							<!-- Passenger Name -->
							<span class="pl-sm-5"><b>Passenger Name</b> *</span><br>
							<div class="col-sm-12">
								<div class="input-group">
									<div class="input-group-prepend">
										<div class="input-group-text">
											<i class="fa fa-user"></i>
										</div>
									</div>
									<input type="text" class="form-control" name="passengerName"
										placeholder="Enter Passenger name"
										value="<%=DataUtility.getStringData(dto.getPassengerName())%>">
								</div>
							</div>
							<font color="red" class="pl-sm-5"><%=ServletUtility.getErrorMessage("passengerName", request)%></font><br>

							<!-- Train Number -->
							<span class="pl-sm-5"><b>Train Number</b> *</span><br>
							<div class="col-sm-12">
								<div class="input-group">
									<div class="input-group-prepend">
										<div class="input-group-text">
											<i class="fa fa-subway"></i>
										</div>
									</div>
									<input type="text" class="form-control" name="trainNumber"
										placeholder="Enter train number"
										value="<%=DataUtility.getStringData(dto.getTrainNumber())%>">
								</div>
							</div>
							<font color="red" class="pl-sm-5"><%=ServletUtility.getErrorMessage("trainNumber", request)%></font><br>

							<!-- Train Name -->
							<span class="pl-sm-5"><b>Train Name</b></span><br>
							<div class="col-sm-12">
								<div class="input-group">
									<div class="input-group-prepend">
										<div class="input-group-text">
											<i class="fa fa-train"></i>
										</div>
									</div>
									<input type="text" class="form-control" name="trainName"
										placeholder="Enter train name"
										value="<%=DataUtility.getStringData(dto.getTrainName())%>">
								</div>
							</div>
							<font color="red" class="pl-sm-5"><%=ServletUtility.getErrorMessage("trainName", request)%></font><br>


							<!-- Source Station -->
							<span class="pl-sm-5"><b>Source Station</b> *</span><br>
							<div class="col-sm-12">
								<div class="input-group">
									<div class="input-group-prepend">
										<div class="input-group-text">
											<i class="fa fa-map-marker"></i>
										</div>
									</div>
									<input type="text" class="form-control" name="sourceStation"
										placeholder="Enter source station"
										value="<%=DataUtility.getStringData(dto.getSourceStation())%>">
								</div>
							</div>
							<font color="red" class="pl-sm-5"><%=ServletUtility.getErrorMessage("sourceStation", request)%></font><br>

							<!-- Destination Station -->
							<span class="pl-sm-5"><b>Destination Station</b> *</span><br>
							<div class="col-sm-12">
								<div class="input-group">
									<div class="input-group-prepend">
										<div class="input-group-text">
											<i class="fa fa-map-marker"></i>
										</div>
									</div>
									<input type="text" class="form-control"
										name="destinationStation"
										placeholder="Enter destination station"
										value="<%=DataUtility.getStringData(dto.getDestinationStation())%>">
								</div>
							</div>
							<font color="red" class="pl-sm-5"><%=ServletUtility.getErrorMessage("destinationStation", request)%></font><br>

							<!-- Journey Date -->
							<span class="pl-sm-5"><b>Journey Date</b> <span
								style="color: red;">*</span></span></br>

							<div class="col-sm-12">
								<div class="input-group">
									<div class="input-group-prepend">
										<div class="input-group-text">
											<i class="fa fa-calendar grey-text" style="font-size: 1rem;"></i>
										</div>
									</div>

									<input type="text" id="udate5" name="journeyDate"
										class="form-control" placeholder="Journey Date"
										readonly="readonly"
										value="<%=DataUtility.getStringData(dto.getJourneyDate())%>">
								</div>
							</div>

							<font color="red" class="pl-sm-5"> <%=ServletUtility.getErrorMessage("journeyDate", request)%>
							</font> <br>


							<!-- Seat Number -->
							<span class="pl-sm-5"><b>Seat Number</b></span><br>
							<div class="col-sm-12">
								<div class="input-group">
									<div class="input-group-prepend">
										<div class="input-group-text">
											<i class="fa fa-chair"></i>
										</div>
									</div>
									<input type="text" class="form-control" name="seatNumber"
										placeholder="Enter Seat Number"
										value="<%=DataUtility.getStringData(dto.getSeatNumber())%>">
								</div>

							</div>
							<font color="red" class="pl-sm-5"><%=ServletUtility.getErrorMessage("seatNumber", request)%></font><br>

							<!-- Ticket Class -->
							<span class="pl-sm-5"><b>Ticket Class</b> <span
								style="color: red">*</span></span><br>
							<div class="col-sm-12">
								<div class="input-group">
									<div class="input-group-prepend">
										<div class="input-group-text">
											<i class="fa fa-toggle-on grey-text"></i>
										</div>
									</div>
									<%=HTMLUtility.getList("ticketClass", dto.getTicketClass(), classMap)%>
								</div>
								<font color="red" class="pl-sm-5"><%=ServletUtility.getErrorMessage("ticketClass", request)%></font><br>

							</div>
						</div>
						<!-- md-form properly closed here -->


						<div class="text-center">
							<%
								if (id > 0) {
							%>
							<input type="submit" name="operation"
								value="<%=TrainTicketCtl.OP_UPDATE%>" class="btn btn-success">
							<input type="submit" name="operation"
								value="<%=TrainTicketCtl.OP_CANCEL%>" class="btn btn-warning">
							<%
								} else {
							%>
							<input type="submit" name="operation"
								value="<%=TrainTicketCtl.OP_SAVE%>" class="btn btn-success">
							<input type="submit" name="operation"
								value="<%=TrainTicketCtl.OP_RESET%>" class="btn btn-warning">
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
