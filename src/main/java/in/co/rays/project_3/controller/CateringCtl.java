package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.CateringDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.CateringModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/CateringCtl" })
public class CateringCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(CateringCtl.class);

	// ================= PRELOAD =================
	@Override
	protected void preload(HttpServletRequest request) {

		HashMap<String, String> menuMap = new HashMap<>();

		menuMap.put("Veg", "Veg");
		menuMap.put("Non-Veg", "Non-Veg");
		menuMap.put("Buffet", "Buffet");

		request.setAttribute("menuMap", menuMap);
	}

	// ================= VALIDATE =================
	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("vendorName"))) {

			request.setAttribute("vendorName", PropertyReader.getValue("error.require", "Vendor Name"));
			pass = false;

		} else if (!DataValidator.isName(request.getParameter("vendorName"))) {

			request.setAttribute("vendorName", "Only alphabetical allowed");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("menuType"))) {

			request.setAttribute("menuType", PropertyReader.getValue("error.require", "Menu Type"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("cost"))) {

			request.setAttribute("cost", PropertyReader.getValue("error.require", "Cost"));
			pass = false;
		} else if (!DataValidator.isInteger(request.getParameter("cost"))) {

			request.setAttribute("cost", "Only numeric value allowed");
			pass = false;
		}

		return pass;
	}

	// ================= POPULATE =================
	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		CateringDTO dto = new CateringDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setVendorName(DataUtility.getString(request.getParameter("vendorName")));
		dto.setMenuType(DataUtility.getString(request.getParameter("menuType")));
		dto.setCost(DataUtility.getString(request.getParameter("cost")));

		populateBean(dto, request);

		return dto;
	}

	// ================= DO GET =================
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		CateringModelInt model = ModelFactory.getInstance().getCateringModel();

		long id = DataUtility.getLong(request.getParameter("id"));
		CateringDTO dto = null;

		try {

			if (id > 0) {
				dto = model.findByPK(id);
			} else {
				dto = new CateringDTO();
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

		CateringModelInt model = ModelFactory.getInstance().getCateringModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			CateringDTO dto = (CateringDTO) populateDTO(request);

			try {

				if (id > 0) {
					model.update(dto);
					ServletUtility.setSuccessMessage("Catering updated successfully", request);
				} else {
					model.add(dto);
					ServletUtility.setSuccessMessage("Catering added successfully", request);
				}

				ServletUtility.setDto(dto, request);

			} catch (DuplicateRecordException e) {

				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("Vendor already exists", request);

			} catch (ApplicationException e) {

				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			CateringDTO dto = (CateringDTO) populateDTO(request);

			try {

				model.delete(dto);
				ServletUtility.redirect(ORSView.CATERING_LIST_CTL, request, response);
				return;

			} catch (ApplicationException e) {

				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.CATERING_LIST_CTL, request, response);
			return;

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.CATERING_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		return ORSView.CATERING_VIEW;
	}
}