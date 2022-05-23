package viewController;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import repository.FollowDao;
import service.FollowService;
import service.FollowServiceImpl;

@WebServlet("/profile")
public class ProfileController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private FollowService followService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(getServletConfig());
		ServletContext servletContext = config.getServletContext();
		followService = new FollowServiceImpl((FollowDao) servletContext.getAttribute("followDao"));
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = (String) request.getAttribute("username");
		System.out.println("진입");
		if(followService.isValidUser(username)) {
			System.out.println("target_username is valid");
			request.getRequestDispatcher("/WEB-INF/views/userinfo/profile.jsp").forward(request, response);
		} else {
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().print("<script>alert(\"존재하지 않는 유저입니다.\"); location.href=\"/main\";</script>");
		}
	}
}
