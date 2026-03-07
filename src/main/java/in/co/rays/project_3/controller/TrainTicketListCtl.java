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
import in.co.rays.project_3.dto.TrainTicketDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.TrainTicketModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * TrainTicket List Controller
 * 
 * @author Rishabh
 */
@WebServlet(name = "TrainTicketListCtl", urlPatterns = { "/ctl/TrainTicketListCtl" })
public class TrainTicketListCtl extends BaseCtl {

    private static Logger log = Logger.getLogger(TrainTicketListCtl.class);

    @Override
    protected void preload(HttpServletRequest request) {

        HashMap<String, String> classMap = new HashMap<>();
        classMap.put("Sleeper", "Sleeper");
        classMap.put("3A", "3A");
        classMap.put("2A", "2A");

        request.setAttribute("classMap", classMap);
    }

    @Override
    protected BaseDTO populateDTO(HttpServletRequest request) {

        TrainTicketDTO dto = new TrainTicketDTO();

        dto.setId(DataUtility.getLong(request.getParameter("id")));
        dto.setPassengerName(DataUtility.getString(request.getParameter("passengerName")));
        dto.setTrainNumber(DataUtility.getString(request.getParameter("trainNumber")));
        dto.setSourceStation(DataUtility.getString(request.getParameter("sourceStation")));
        dto.setDestinationStation(DataUtility.getString(request.getParameter("destinationStation")));
        dto.setTicketClass(DataUtility.getString(request.getParameter("ticketClass")));

        populateBean(dto, request);

        return dto;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("TrainTicketListCtl doGet Start");

        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

        TrainTicketDTO dto = (TrainTicketDTO) populateDTO(request);

        TrainTicketModelInt model = ModelFactory.getInstance().getTrainTicketModel();

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

        log.debug("TrainTicketListCtl doGet End");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("TrainTicketListCtl doPost Start");

        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0)
                ? DataUtility.getInt(PropertyReader.getValue("page.size"))
                : pageSize;

        String op = DataUtility.getString(request.getParameter("operation"));
        String[] ids = request.getParameterValues("ids");

        TrainTicketDTO dto = (TrainTicketDTO) populateDTO(request);
        TrainTicketModelInt model = ModelFactory.getInstance().getTrainTicketModel();

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
                ServletUtility.redirect(ORSView.TRAINTICKET_CTL, request, response);
                return;

            } else if (OP_RESET.equalsIgnoreCase(op) || OP_BACK.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.TRAINTICKET_LIST_CTL, request, response);
                return;

            } else if (OP_DELETE.equalsIgnoreCase(op)) {

                pageNo = 1;
                if (ids != null && ids.length > 0) {
                    for (String id : ids) {
                        TrainTicketDTO deleteDto = new TrainTicketDTO();
                        deleteDto.setId(DataUtility.getLong(id));
                        model.delete(deleteDto);
                    }
                    ServletUtility.setSuccessMessage("Data deleted successfully", request);
                } else {
                    ServletUtility.setErrorMessage("Select at least one record", request);
                }
            }

            dto = (TrainTicketDTO) populateDTO(request);

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

        log.debug("TrainTicketListCtl doPost End");
    }

    @Override
    protected String getView() {
        return ORSView.TRAINTICKET_LIST_VIEW;
    }
}
