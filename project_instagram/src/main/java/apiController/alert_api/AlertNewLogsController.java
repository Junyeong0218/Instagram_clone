package apiController.alert_api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.NonReadActivities;
import entity.User;

@WebServlet("/alert/new-logs")
public class AlertNewLogsController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User sessionUser = (User) request.getAttribute("sessionUser");
		response.setContentType("text/plain; charset=UTF-8");
		if(NonReadActivities.isChanged(sessionUser.getId()) != null) {
			String array = NonReadActivities.getNonReadMessage(sessionUser.getId());
			response.getWriter().print(array);
		}
	}
}
