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

import entity.User;
import repository.FollowDao;
import service.FollowService;
import service.FollowServiceImpl;

@WebServlet("/follow/followers")
public class FollowersController extends HttpServlet {
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
		User sessionUser = (User) request.getAttribute("sessionUser");
		int page_indicator = (int) request.getAttribute("page_indicator");

		System.out.println(sessionUser);
		System.out.println(page_indicator);
		
		List<User> followerList = followService.selectFollowers(sessionUser.getId(), page_indicator);
		System.out.println(followerList);
		boolean has_more_followers = false;
		if(followerList.size() > 10) {
			has_more_followers = true;
			followerList.remove(10);
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(" { \"follower_list\": [ ");
		for(User user : followerList) {
			sb.append("{ \"id\": \"" + user.getId() + "\", " + 
									"\"username\": \"" + user.getUsername() + "\", " + 
									"\"name\": \"" + user.getName() + "\", " + 
									"\"has_profile_image\": \"" + user.isHas_profile_image() + "\", " + 
									"\"file_name\": \"" + user.getFile_name() + "\" }, ");
		}
		if(sb.indexOf(",") != -1) sb.delete(sb.length() - 2, sb.length());
		sb.append(" ], \"has_more_followers\": \"" + has_more_followers + "\" }");
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(sb.toString());
	}
}
