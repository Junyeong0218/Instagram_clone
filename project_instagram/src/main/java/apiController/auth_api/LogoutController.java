package apiController.auth_api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.NonReadActivities;
import entity.SecurityContext;
import entity.User;

@WebServlet("/auth/logout")
public class LogoutController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uuid = (String) request.getSession().getAttribute("UUID");
		User sessionUser = (User) request.getAttribute("sessionUser");
		try {
			SecurityContext.getInstance().invalidateUser(sessionUser, uuid);
		} catch (NullPointerException e) {
			System.out.println("security context is not created!!");
		}
		if(NonReadActivities.eraseUser(sessionUser.getId())) {
			request.getSession().invalidate();
			response.sendRedirect("/index");
		}
	}
}