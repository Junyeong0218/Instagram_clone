package apiController.auth_api;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

import entity.User;
import repository.UserDao;
import service.AuthService;
import service.AuthServiceImpl;

@WebServlet("/auth/password")
public class PasswordController extends HttpServlet{
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
		User sessionUser = (User) request.getAttribute("sessionUser");
		String targetPassword = request.getParameter("password");
		
		User user = User.builder()
										.id(sessionUser.getId())
										.password(targetPassword)
										.build();
		
		boolean result = authService.checkOriginPassword(user);
		
		response.getWriter().print(result);
		System.out.println(result);
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User sessionUser = (User) request.getAttribute("sessionUser");
		String requestBody = new String(request.getInputStream().readAllBytes(), "UTF-8");
		String passwordParam = requestBody.replaceAll("[\\{\\}]", "").split(":")[1].replaceAll("\"", "");
		System.out.println(passwordParam);
		User requestDto = User.builder()
													  .id(sessionUser.getId())
													  .password(BCrypt.hashpw(passwordParam, BCrypt.gensalt()))
													  .build();
		System.out.println(requestDto);
		
		int result = authService.updatePassword(requestDto);
		
		response.setContentType("text/html; charset=UTF-8");
		if(result == 1) {
			response.getWriter().print(true);
		} else {
			response.getWriter().print(false);
		}
	}
}
