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
import response_dto.UserProfileResDto;
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
		int sessionUserId = (int) request.getAttribute("sessionUserId");
		String username = (String) request.getAttribute("username");
		
		UserProfileResDto resDto = followService.selectUserProfileInfo(username, sessionUserId);
		request.setAttribute("userProfile", resDto);
		System.out.println(resDto);
		
		request.getRequestDispatcher("/WEB-INF/views/userinfo/profile.jsp").forward(request, response);
	}
}
