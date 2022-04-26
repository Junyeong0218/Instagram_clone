package viewController;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entity.User;
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
		HttpSession session = request.getSession();
		User sessionUser = (User) session.getAttribute("user");
		String username = request.getParameter("username");
		
		if(username == null || username.equals("")) {
			username = sessionUser.getUsername();
		}
		
		System.out.println("username : " + username);
		
		UserProfileResDto resDto = followService.selectUserProfileInfo(username, sessionUser.getId());
		request.setAttribute("userProfile", resDto);
		System.out.println(resDto);
		
		request.getRequestDispatcher("/WEB-INF/views/userinfo/profile.jsp").forward(request, response);
	}
}
