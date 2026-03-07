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
import in.co.rays.project_3.dto.MeetingDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.MeetingModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "MeetingListCtl", urlPatterns = { "/ctl/MeetingListCtl" })
public class MeetingListCtl extends BaseCtl {

    private static Logger log = Logger.getLogger(MeetingListCtl.class);

    // ================= PRELOAD =================
    @Override
    protected void preload(HttpServletRequest request) {

        HashMap<String, String> statusMap = new HashMap<>();
        statusMap.put("Scheduled", "Scheduled");
        statusMap.put("Completed", "Completed");
        statusMap.put("Cancelled", "Cancelled");

        request.setAttribute("statusMap", statusMap);
    }

    // ================= POPULATE =================
    @Override
    protected BaseDTO populateDTO(HttpServletRequest request) {

        MeetingDTO dto = new MeetingDTO();

        dto.setId(DataUtility.getLong(request.getParameter("id")));
        dto.setMeetingCode(DataUtility.getString(request.getParameter("meetingCode")));
        dto.setMeetingTitle(DataUtility.getString(request.getParameter("meetingTitle")));
        dto.setMeetingStatus(DataUtility.getString(request.getParameter("meetingStatus")));
        dto.setMeetingLocation(DataUtility.getString(request.getParameter("meetingLocation")));

        return dto;
    }

    // ================= DO GET =================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("MeetingListCtl doGet Start");

        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

        MeetingDTO dto = (MeetingDTO) populateDTO(request);
        MeetingModelInt model = ModelFactory.getInstance().getMeetingModel();

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

        log.debug("MeetingListCtl doGet End");
    }

    // ================= DO POST =================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("MeetingListCtl doPost Start");

        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0)
                ? DataUtility.getInt(PropertyReader.getValue("page.size"))
                : pageSize;

        String op = DataUtility.getString(request.getParameter("operation"));
        String[] ids = request.getParameterValues("ids");

        MeetingDTO dto = (MeetingDTO) populateDTO(request);
        MeetingModelInt model = ModelFactory.getInstance().getMeetingModel();

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

                ServletUtility.redirect(ORSView.MEETING_CTL, request, response);
                return;

            } else if (OP_RESET.equalsIgnoreCase(op) || OP_BACK.equalsIgnoreCase(op)) {

                ServletUtility.redirect(ORSView.MEETING_LIST_CTL, request, response);
                return;

            } else if (OP_DELETE.equalsIgnoreCase(op)) {

                pageNo = 1;

                if (ids != null && ids.length > 0) {

                    for (String id : ids) {
                        MeetingDTO deleteDto = new MeetingDTO();
                        deleteDto.setId(DataUtility.getLong(id));
                        model.delete(deleteDto);
                    }

                    ServletUtility.setSuccessMessage("Meeting deleted successfully", request);

                } else {

                    ServletUtility.setErrorMessage("Select at least one record", request);
                }
            }

            dto = (MeetingDTO) populateDTO(request);

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

        log.debug("MeetingListCtl doPost End");
    }

    @Override
    protected String getView() {
        return ORSView.MEETING_LIST_VIEW;
    }
}