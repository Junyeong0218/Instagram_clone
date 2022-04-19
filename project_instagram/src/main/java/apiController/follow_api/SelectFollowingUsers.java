package apiController.follow_api;

import java.io.IOException;
import java.util.List;

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
import service.FollowService;
import service.FollowServiceImpl;

@WebServlet("/follow/select-following-users")
public class SelectFollowingUsers extends HttpServlet {
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
		
		int count_indicator = Integer.parseInt(request.getParameter("count_indicator"));
		
		List<User> followingUserList = followService.selectFollowingUsers(sessionUser.getId(), count_indicator);
		boolean has_more_users = false;
		if(followingUserList.size() > 10) {
			has_more_users = true;
			followingUserList.remove(10);
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("{ \"following_user_list\": [ ");
		for(User user : followingUserList) {
			sb.append("{ \"id\": \"" + user.getId() + "\", " + 
									"\"username\": \"" + user.getUsername() + "\", " + 
									"\"name\": \"" + user.getName() + "\", " + 
									"\"has_profile_image\": \"" + user.isHas_profile_image() + "\", " + 
									"\"file_name\": \"" + user.getFile_name() + "\" }, ");
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append("], \"has_more_users\": \"" + has_more_users + "\" }");
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(sb.toString());
	}
}
