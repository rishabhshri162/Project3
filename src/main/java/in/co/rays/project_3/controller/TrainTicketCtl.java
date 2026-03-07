package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.TrainTicketDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.TrainTicketModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * TrainTicket Controller (Add / Update / Delete)
 * 
 * @author Rishabh
 */
@WebServlet(urlPatterns = { "/ctl/TrainTicketCtl" })
public class TrainTicketCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(TrainTicketCtl.class);

	@Override
	protected void preload(HttpServletRequest request) {

		HashMap<String, String> classMap = new HashMap<>();
		classMap.put("Sleeper", "Sleeper");
		classMap.put("3A", "3A");
		classMap.put("2A", "2A");
		classMap.put("1A", "1A");

		request.setAttribute("classMap", classMap);
	}

	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("passengerName"))) {
			request.setAttribute("passengerName", PropertyReader.getValue("error.require", "Passenger Name"));
			pass = false;

		} else if (!DataValidator.isName(request.getParameter("passengerName"))) {

			request.setAttribute("passengerName", "Only in alphabetical");

			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("trainNumber"))) {
			request.setAttribute("trainNumber", PropertyReader.getValue("error.require", "Train Number"));
			pass = false;
		} else if (!DataValidator.isInteger(request.getParameter("trainNumber"))) {

			request.setAttribute("trainNumber", "please only in numeric");

			pass = false;

		} else if (!DataValidator.isTrainNumber(request.getParameter("trainNumber"))) {

			request.setAttribute("trainNumber", "Train number should be only 5 digit");

			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("trainName"))) {
			request.setAttribute("trainName", PropertyReader.getValue("error.require", "Train Name"));
			pass = false;

		} else if (!DataValidator.isName(request.getParameter("trainName"))) {

			request.setAttribute("trainName", "Only in alphabetical");

			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("sourceStation"))) {
			request.setAttribute("sourceStation", PropertyReader.getValue("error.require", "Source Station"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("destinationStation"))) {
			request.setAttribute("destinationStation", PropertyReader.getValue("error.require", "Destination Station"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("journeyDate"))) {
			request.setAttribute("journeyDate", PropertyReader.getValue("error.require", "Journey Date"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("seatNumber"))) {
			request.setAttribute("seatNumber", PropertyReader.getValue("error.require", "Seat Number"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("ticketClass"))) {
			request.setAttribute("ticketClass", PropertyReader.getValue("error.require", "Ticket Class"));
			pass = false;
		}

		return pass;
	}

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		TrainTicketDTO dto = new TrainTicketDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setPassengerName(DataUtility.getString(request.getParameter("passengerName")));
		dto.setTrainNumber(DataUtility.getString(request.getParameter("trainNumber")));
		dto.setTrainName(DataUtility.getString(request.getParameter("trainName")));
		dto.setSourceStation(DataUtility.getString(request.getParameter("sourceStation")));
		dto.setDestinationStation(DataUtility.getString(request.getParameter("destinationStation")));
		dto.setJourneyDate(DataUtility.getString(request.getParameter("journeyDate")));
		dto.setSeatNumber(DataUtility.getString(request.getParameter("seatNumber")));
		dto.setTicketClass(DataUtility.getString(request.getParameter("ticketClass")));

		populateBean(dto, request);

		return dto;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		TrainTicketModelInt model = ModelFactory.getInstance().getTrainTicketModel();

		long id = DataUtility.getLong(request.getParameter("id"));
		TrainTicketDTO dto = null;

		try {
			if (id > 0) {
				dto = model.findByPK(id);
			} else {
				dto = new TrainTicketDTO();
			}

			request.setAttribute("dto", dto);
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		TrainTicketModelInt model = ModelFactory.getInstance().getTrainTicketModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			TrainTicketDTO dto = (TrainTicketDTO) populateDTO(request);

			try {
				if (id > 0) {
					model.update(dto);
					ServletUtility.setSuccessMessage("Train ticket updated successfully", request);
				} else {
					model.add(dto);
					ServletUtility.setSuccessMessage("Train ticket booked successfully", request);
				}

				ServletUtility.setDto(dto, request);

			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("Train number already exists", request);

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			TrainTicketDTO dto = (TrainTicketDTO) populateDTO(request);

			try {
				model.delete(dto);
                ServletUtility.redirect(ORSView.TRAINTICKET_LIST_CTL, request, response);
                return;

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

            ServletUtility.redirect(ORSView.TRAINTICKET_LIST_CTL, request, response);
            return;

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.TRAINTICKET_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		return ORSView.TRAINTICKET_VIEW;
	}
}
