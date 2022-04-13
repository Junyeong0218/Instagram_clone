package apiController;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.User;
import service.AuthService;
import service.AuthServiceImpl;

@WebServlet("/signin")
public class Signin extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	private AuthService authService;
	
	public Signin() {
		authService = new AuthServiceImpl();
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = User.builder().username(request.getParameter("username"))
					  			  .password(request.getParameter("password")).build();
		
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
