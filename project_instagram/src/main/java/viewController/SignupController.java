package viewController;

import java.io.IOException;

import javax.servlet.FilterConfig;
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

@WebServlet("/signup")
public class SignupController extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	private AuthService authService;
	
	public void init(FilterConfig filterConfig) throws ServletException {
		ServletContext servletContext = filterConfig.getServletContext();
		authService = new AuthServiceImpl((UserDao) servletContext.getAttribute("userDao"));
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/signup.jsp").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = new User();
		user.setUsername(request.getParameter("username"));
		user.setPassword(request.getParameter("password"));
		user.setName(request.getParameter("name"));
		user.setEmail(request.getParameter("email"));
		
		int result = authService.signup(user);
		
		response.setCharacterEncoding("UTF-8");
		
		if(result == 1) {
			response.getWriter().write("<script>alert(\"가입이 완료되었습니다.\"); location.href=\"/index\";</script>");
		} else {
			response.getWriter().write("<script>alert(\"가입이 실패했습니다.\"); location.href=\"/index\";</script>");
		}
	}
}
