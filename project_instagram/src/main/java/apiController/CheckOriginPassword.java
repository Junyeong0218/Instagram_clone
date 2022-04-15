package apiController;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.User;
import repository.UserDao;
import service.AuthService;
import service.AuthServiceImpl;

@WebServlet("/check-origin-password")
public class CheckOriginPassword extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	private AuthService authService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(getServletConfig());
		ServletContext servletContext = config.getServletContext();
		authService = new AuthServiceImpl((UserDao) servletContext.getAttribute("userDao"));
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User sessionUser = (User) request.getSession().getAttribute("user");
		String targetPassword = request.getParameter("password");
		
		User user = User.builder()
						.id(sessionUser.getId())
						.password(targetPassword)
						.build();
		
		int result = authService.checkOriginPassword(user);
		
		response.getWriter().print(result);
		System.out.println(result);
	}
}
