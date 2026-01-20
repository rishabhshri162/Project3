package in.co.rays.project_3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import in.co.rays.project_3.util.ServletUtility;

/**
 * Error functionality controller.perform error page operation
 * 
 * @author Rishabh Shrivastava
 *
 */
@WebServlet(name = "ErrorCtl", urlPatterns = { "/ErrorCtl" })
public class ErrorCtl extends BaseCtl {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		HttpSession session = request.getSession();
		session = request.getSession();
		session.invalidate();
		response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE); //503
		ServletUtility.setErrorMessage("Database server is down", request);
		ServletUtility.forward(getView(), request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		return ORSView.LOGIN_VIEW;
	}

}
