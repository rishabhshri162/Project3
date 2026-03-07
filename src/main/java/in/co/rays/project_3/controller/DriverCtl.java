package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.DriverDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.DriverModelHibImp;
import in.co.rays.project_3.model.DriverModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/DriverCtl" })
public class DriverCtl extends BaseCtl {

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		DriverDTO dto = new DriverDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setDriverCode(DataUtility.getString(request.getParameter("driverCode")));
		dto.setDriverName(DataUtility.getString(request.getParameter("driverName")));
		dto.setLicenseNumber(DataUtility.getString(request.getParameter("licenseNumber")));
		dto.setContactNumber(DataUtility.getString(request.getParameter("contactNumber")));

		populateBean(dto, request);

		return dto;
	}

	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("driverCode"))) {
			request.setAttribute("driverCode", PropertyReader.getValue("error.require", "Driver Code"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("driverName"))) {
			request.setAttribute("driverName", PropertyReader.getValue("error.require", "Driver Name"));
			pass = false;
		} else if (!DataValidator.isName((request.getParameter("driverName")))) {

			request.setAttribute("driverName", "Only in Alphabet");
			pass = false;

		}

		if (DataValidator.isNull(request.getParameter("licenseNumber"))) {
			request.setAttribute("licenseNumber", PropertyReader.getValue("error.require", "License Number"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("contactNumber"))) {
			request.setAttribute("contactNumber", PropertyReader.getValue("error.require", "Contact Number"));
			pass = false;
		}

		return pass;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		long id = DataUtility.getLong(request.getParameter("id"));

		DriverModelInt model = new DriverModelHibImp();

		if (id > 0) {
			try {
				DriverDTO dto = model.findByPK(id);
				ServletUtility.setDto(dto, request);
			} catch (ApplicationException e) {
				e.printStackTrace();
			}
		}

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String op = DataUtility.getString(request.getParameter("operation"));

		DriverModelInt model = new DriverModelHibImp();
		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			DriverDTO dto = (DriverDTO) populateDTO(request);

			try {

				if (id > 0) {

					model.update(dto);
					ServletUtility.setSuccessMessage("Driver updated successfully", request);

				} else {

					model.add(dto);
					ServletUtility.setSuccessMessage("Driver added successfully", request);
				}

				ServletUtility.setDto(dto, request);
				ServletUtility.forward(getView(), request, response);
				return;

			} catch (Exception e) {

				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage(e.getMessage(), request);
				ServletUtility.forward(getView(), request, response);
				return;
			}
		}

		else if (OP_DELETE.equalsIgnoreCase(op)) {

			DriverDTO dto = (DriverDTO) populateDTO(request);

			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.DRIVER_LIST_CTL, request, response);
				return;
			} catch (Exception e) {
				ServletUtility.setErrorMessage(e.getMessage(), request);
				ServletUtility.forward(getView(), request, response);
				return;
			}
		}

		else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.DRIVER_LIST_CTL, request, response);
			return;
		}

		else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.DRIVER_CTL, request, response);
			return;
		}
	}

	@Override
	protected String getView() {
		return ORSView.DRIVER_VIEW;
	}
}