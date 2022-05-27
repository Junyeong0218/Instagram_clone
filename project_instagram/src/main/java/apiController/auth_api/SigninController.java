package apiController.auth_api;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.NonReadActivities;
import entity.User;
import repository.NewActivityDao;
import repository.UserDao;
import service.AuthService;
import service.AuthServiceImpl;
import service.NewActivityService;
import service.NewActivityServiceImpl;

@WebServlet("/auth/signin")
public class SigninController extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	private AuthService authService;
	private NewActivityService newActivityService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(getServletConfig());
		ServletContext servletContext = config.getServletContext();
		authService = new AuthServiceImpl((UserDao) servletContext.getAttribute("userDao"));
		newActivityService = new NewActivityServiceImpl((NewActivityDao) servletContext.getAttribute("newActivityDao"));
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = User.builder().username(request.getParameter("username"))
					  			  						.password(request.getParameter("password")).build();
		User userDetail = authService.signin(user);
		if(userDetail != null) {
			// 로그인 인증 성공 시 NonReadActivities 객체에도 세션 정보 저장
			NonReadActivities.setUser(userDetail.getId(), newActivityService.selectNonReadActivities(userDetail.getId()));
			
			request.setAttribute("user", userDetail);
		}
	}
}
