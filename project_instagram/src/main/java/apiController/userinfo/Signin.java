package apiController.userinfo;

import java.io.IOException;

import javax.servlet.FilterConfig;
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

@WebServlet("/signin")
public class Signin extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	private AuthService authService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(getServletConfig());
		ServletContext servletContext = config.getServletContext();
		authService = new AuthServiceImpl((UserDao) servletContext.getAttribute("userDao"));
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = User.builder().username(request.getParameter("username"))
					  			  .password(request.getParameter("password")).build();
		System.out.println(user);
		User userDetail = authService.signin(user);
		if(userDetail == null) {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print("<script>alert(\"아이디 혹은 비밀번호가 틀렸습니다.\"); location.href = \"/index\";</script>");
		} else {
			request.getSession().setAttribute("user", userDetail);
			response.sendRedirect("/main");
		}
	}
}
