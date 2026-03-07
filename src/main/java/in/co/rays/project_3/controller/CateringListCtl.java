package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.CateringDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.CateringModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "CateringListCtl", urlPatterns = { "/ctl/CateringListCtl" })
public class CateringListCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(CateringListCtl.class);

	// ================= PRELOAD =================
	@Override
	protected void preload(HttpServletRequest request) {

		HashMap<String, String> menuMap = new HashMap<>();

		menuMap.put("Veg", "Veg");
		menuMap.put("Non-Veg", "Non-Veg");
		menuMap.put("Buffet", "Buffet");

		request.setAttribute("menuMap", menuMap);
	}

	// ================= POPULATE =================
	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		CateringDTO dto = new CateringDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setVendorName(DataUtility.getString(request.getParameter("vendorName")));
		dto.setMenuType(DataUtility.getString(request.getParameter("menuType")));
		dto.setCost(DataUtility.getString(request.getParameter("cost")));

		return dto;
	}

	// ================= DO GET =================
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("CateringListCtl doGet Start");

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		CateringDTO dto = (CateringDTO) populateDTO(request);
		CateringModelInt model = ModelFactory.getInstance().getCateringModel();

		try {

			List list = model.search(dto, pageNo, pageSize);
			List next = model.search(dto, pageNo + 1, pageSize);

			ServletUtility.setList(list, request);

			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found", request);
			}

			if (next == null || next.size() == 0) {
				request.setAttribute("nextListSize", 0);
			} else {
				request.setAttribute("nextListSize", next.size());
			}

			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {

			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}

		log.debug("CateringListCtl doGet End");
	}

	// ================= DO POST =================
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("CateringListCtl doPost Start");

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		String op = DataUtility.getString(request.getParameter("operation"));
		String[] ids = request.getParameterValues("ids");

		CateringDTO dto = (CateringDTO) populateDTO(request);
		CateringModelInt model = ModelFactory.getInstance().getCateringModel();

		try {

			if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {

				if (OP_SEARCH.equalsIgnoreCase(op)) {
					pageNo = 1;
				} else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;
				} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
					pageNo--;
				}

			} else if (OP_NEW.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.CATERING_CTL, request, response);
				return;

			} else if (OP_RESET.equalsIgnoreCase(op) || OP_BACK.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.CATERING_LIST_CTL, request, response);
				return;

			} else if (OP_DELETE.equalsIgnoreCase(op)) {

				pageNo = 1;

				if (ids != null && ids.length > 0) {

					for (String id : ids) {

						CateringDTO deleteDto = new CateringDTO();
						deleteDto.setId(DataUtility.getLong(id));

						model.delete(deleteDto);
					}

					ServletUtility.setSuccessMessage("Catering deleted successfully", request);

				} else {

					ServletUtility.setErrorMessage("Select at least one record", request);
				}
			}

			dto = (CateringDTO) populateDTO(request);

			List list = model.search(dto, pageNo, pageSize);
			List next = model.search(dto, pageNo + 1, pageSize);

			ServletUtility.setDto(dto, request);
			ServletUtility.setList(list, request);

			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found", request);
			}

			if (next == null || next.size() == 0 && !OP_DELETE.equalsIgnoreCase(op)) {
				request.setAttribute("nextListSize", 0);
			} else {
				request.setAttribute("nextListSize", next.size());
			}

			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {

			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}

		log.debug("CateringListCtl doPost End");
	}

	@Override
	protected String getView() {
		return ORSView.CATERING_LIST_VIEW;
	}
}