package apiController;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.AuthService;
import service.AuthServiceImpl;

@WebServlet("/check-username")
public class CheckUsername extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	private AuthService authService;
	
	public CheckUsername() {
		authService = new AuthServiceImpl();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		
		int result = authService.checkUsername(username);
		
		response.getWriter().print(result);
	}
}
