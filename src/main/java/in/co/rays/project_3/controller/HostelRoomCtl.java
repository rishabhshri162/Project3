package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.HostelRoomDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.HostelRoomModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * HostelRoom Controller (Add / Update / Delete)
 * 
 * @author Rishabh Shrivastava
 */
@WebServlet(urlPatterns = { "/ctl/HostelRoomCtl" })
public class HostelRoomCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(HostelRoomCtl.class);

	/* ================= PRELOAD ================= */

	@Override
	protected void preload(HttpServletRequest request) {

		HashMap<String, String> roomTypeMap = new HashMap<>();
		roomTypeMap.put("Single", "Single");
		roomTypeMap.put("Double", "Double");
		roomTypeMap.put("Triple", "Triple");

		HashMap<String, String> statusMap = new HashMap<>();
		statusMap.put("Available", "Available");
		statusMap.put("Occupied", "Occupied");

		request.setAttribute("roomTypeMap", roomTypeMap);
		request.setAttribute("statusMap", statusMap);
	}



	/* ================= VALIDATE ================= */

	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("roomNumber"))) {
			request.setAttribute("roomNumber", PropertyReader.getValue("error.require", "Room Number"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("roomType"))) {
			request.setAttribute("roomType", PropertyReader.getValue("error.require", "Room Type"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("capacity"))) {
			request.setAttribute("capacity", PropertyReader.getValue("error.require", "Capacity"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("rent"))) {
			request.setAttribute("rent", PropertyReader.getValue("error.require", "Rent"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("status"))) {
			request.setAttribute("status", PropertyReader.getValue("error.require", "Status"));
			pass = false;
		}

		return pass;
	}

	/* ================= POPULATE DTO ================= */

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		HostelRoomDTO dto = new HostelRoomDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setRoomNumber(DataUtility.getString(request.getParameter("roomNumber")));
		dto.setRoomType(DataUtility.getString(request.getParameter("roomType")));
		dto.setCapacity(DataUtility.getString(request.getParameter("capacity")));
		dto.setRent(DataUtility.getString(request.getParameter("rent")));
		dto.setStatus(DataUtility.getString(request.getParameter("status")));

		populateBean(dto, request);

		return dto;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		HostelRoomModelInt model = ModelFactory.getInstance().getHostelRoomModel();

		long id = DataUtility.getLong(request.getParameter("id"));
		HostelRoomDTO dto = null;

		try {
			if (id > 0) {
				dto = model.findByPK(id);  
			} else {
				dto = new HostelRoomDTO();  
			}

			request.setAttribute("dto", dto);
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
		}
	}



	/* ================= DO POST ================= */

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		HostelRoomModelInt model = ModelFactory.getInstance().getHostelRoomModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			HostelRoomDTO dto = (HostelRoomDTO) populateDTO(request);

			try {
				if (id > 0) {
					model.update(dto);
					ServletUtility.setSuccessMessage("Hostel Room updated successfully", request);
				} else {
					model.add(dto);
					ServletUtility.setSuccessMessage("Hostel Room added successfully", request);
				}
				ServletUtility.setDto(dto, request);

			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("Room number already exists", request);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			HostelRoomDTO dto = (HostelRoomDTO) populateDTO(request);
			try {
				model.delete(dto);
			ServletUtility.redirect(ORSView.HOSTELROOM_LIST_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.HOSTELROOM_LIST_CTL, request, response);
			return;

		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.HOSTELROOM_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		return ORSView.HOSTELROOM_VIEW;
	}
}
