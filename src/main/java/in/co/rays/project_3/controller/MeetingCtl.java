package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.MeetingDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.MeetingModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/MeetingCtl" })
public class MeetingCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(MeetingCtl.class);

	// ================= PRELOAD =================
	@Override
	protected void preload(HttpServletRequest request) {

		HashMap<String, String> statusMap = new HashMap<>();
		statusMap.put("Scheduled", "Scheduled");
		statusMap.put("Completed", "Completed");
		statusMap.put("Cancelled", "Cancelled");

		request.setAttribute("statusMap", statusMap);
	}

	// ================= VALIDATE =================
	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("meetingCode"))) {
			request.setAttribute("meetingCode", PropertyReader.getValue("error.require", "Meeting Code"));
			pass = false;

		} else if (!DataValidator.isInteger((request.getParameter("meetingCode")))) {

			request.setAttribute("meetingCode", "Only in numeric");
			pass = false;

		}

		if (DataValidator.isNull(request.getParameter("meetingTitle"))) {
			request.setAttribute("meetingTitle", PropertyReader.getValue("error.require", "Meeting Title"));

			pass = false;

		} else if (!DataValidator.isName((request.getParameter("meetingTitle")))) {

			request.setAttribute("meetingTitle", "Only in alphabetical");
			pass = false;

		}

		if (DataValidator.isNull(request.getParameter("meetingTime"))) {
			request.setAttribute("meetingTime", PropertyReader.getValue("error.require", "Meeting Date"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("meetingLocation"))) {
			request.setAttribute("meetingLocation", PropertyReader.getValue("error.require", "Meeting Location"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("meetingStatus"))) {
			request.setAttribute("meetingStatus", PropertyReader.getValue("error.require", "Meeting Status"));
			pass = false;
		}

		return pass;
	}

	// ================= POPULATE =================
	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		MeetingDTO dto = new MeetingDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setMeetingCode(DataUtility.getString(request.getParameter("meetingCode")));
		dto.setMeetingTitle(DataUtility.getString(request.getParameter("meetingTitle")));
		dto.setMeetingLocation(DataUtility.getString(request.getParameter("meetingLocation")));
		dto.setMeetingStatus(DataUtility.getString(request.getParameter("meetingStatus")));

		// Date me lena hai (as you said earlier)
		dto.setMeetingTime(DataUtility.getDate(request.getParameter("meetingTime")));

		populateBean(dto, request);

		return dto;
	}

	// ================= DO GET =================
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		MeetingModelInt model = ModelFactory.getInstance().getMeetingModel();

		long id = DataUtility.getLong(request.getParameter("id"));
		MeetingDTO dto = null;

		try {
			if (id > 0) {
				dto = model.findByPK(id);
			} else {
				dto = new MeetingDTO();
			}

			request.setAttribute("dto", dto);
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
		}
	}

	// ================= DO POST =================
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		MeetingModelInt model = ModelFactory.getInstance().getMeetingModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			MeetingDTO dto = (MeetingDTO) populateDTO(request);

			try {

				if (id > 0) {
					model.update(dto);
					ServletUtility.setSuccessMessage("Meeting updated successfully", request);
				} else {
					model.add(dto);
					ServletUtility.setSuccessMessage("Meeting scheduled successfully", request);
				}

				ServletUtility.setDto(dto, request);

			} catch (DuplicateRecordException e) {

				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("Meeting code already exists", request);

			} catch (ApplicationException e) {

				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			MeetingDTO dto = (MeetingDTO) populateDTO(request);

			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.MEETING_LIST_CTL, request, response);
				return;

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.MEETING_LIST_CTL, request, response);
			return;

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.MEETING_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		return ORSView.MEETING_VIEW;
	}
}