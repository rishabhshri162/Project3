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
import in.co.rays.project_3.dto.DriverDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.DriverModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "DriverListCtl", urlPatterns = { "/ctl/DriverListCtl" })
public class DriverListCtl extends BaseCtl {

    private static Logger log = Logger.getLogger(DriverListCtl.class);

    @Override
    protected void preload(HttpServletRequest request) {

        try {
            DriverModelInt model = ModelFactory.getInstance().getDriverModel();
            List list = model.list();   // saare drivers

            request.setAttribute("driverCodeList", list);

        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected BaseDTO populateDTO(HttpServletRequest request) {

        DriverDTO dto = new DriverDTO();

        dto.setId(DataUtility.getLong(request.getParameter("id")));
        dto.setDriverCode(DataUtility.getString(request.getParameter("driverCode")));
        dto.setDriverName(DataUtility.getString(request.getParameter("driverName")));
        dto.setLicenseNumber(DataUtility.getString(request.getParameter("licenseNumber")));
        dto.setContactNumber(DataUtility.getString(request.getParameter("contactNumber")));

        return dto;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("DriverListCtl doGet Start");

        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

        DriverDTO dto = (DriverDTO) populateDTO(request);
        DriverModelInt model = ModelFactory.getInstance().getDriverModel();

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

        log.debug("DriverListCtl doGet End");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("DriverListCtl doPost Start");

        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0)
                ? DataUtility.getInt(PropertyReader.getValue("page.size"))
                : pageSize;

        String op = DataUtility.getString(request.getParameter("operation"));
        String[] ids = request.getParameterValues("ids");

        DriverDTO dto = (DriverDTO) populateDTO(request);
        DriverModelInt model = ModelFactory.getInstance().getDriverModel();

        try {

            if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op)
                    || OP_PREVIOUS.equalsIgnoreCase(op)) {

                if (OP_SEARCH.equalsIgnoreCase(op)) {
                    pageNo = 1;
                } else if (OP_NEXT.equalsIgnoreCase(op)) {
                    pageNo++;
                } else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
                    pageNo--;
                }

            } else if (OP_NEW.equalsIgnoreCase(op)) {

                ServletUtility.redirect(ORSView.DRIVER_CTL, request, response);
                return;

            } else if (OP_RESET.equalsIgnoreCase(op) || OP_BACK.equalsIgnoreCase(op)) {

                ServletUtility.redirect(ORSView.DRIVER_LIST_CTL, request, response);
                return;

            } else if (OP_DELETE.equalsIgnoreCase(op)) {

                pageNo = 1;

                if (ids != null && ids.length > 0) {

                    for (String id : ids) {
                        DriverDTO deleteDto = new DriverDTO();
                        deleteDto.setId(DataUtility.getLong(id));
                        model.delete(deleteDto);
                    }

                    ServletUtility.setSuccessMessage("Driver deleted successfully", request);

                } else {

                    ServletUtility.setErrorMessage("Select at least one record", request);
                }
            }

            dto = (DriverDTO) populateDTO(request);

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

        log.debug("DriverListCtl doPost End");
    }

    @Override
    protected String getView() {
        return ORSView.DRIVER_LIST_VIEW;
    }
}