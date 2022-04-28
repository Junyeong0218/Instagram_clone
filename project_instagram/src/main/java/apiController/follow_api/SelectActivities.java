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

import entity.Activity;
import entity.User;
import repository.FollowDao;
import service.FollowService;
import service.FollowServiceImpl;

@WebServlet("/follow/select-activities")
public class SelectActivities extends HttpServlet {
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
		User user = (User) session.getAttribute("user");
		
		List<Activity> activities = followService.selectActivities(user.getId());
		
		System.out.println(activities);
		
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		for(Activity activity : activities) {
			System.out.println(activity);
			sb.append(" { \"id\": \"" + activity.getId() + "\", " + 
									"\"user_id\": \"" + activity.getUser_id() + "\", " + 
									"\"username\": \"" + activity.getUsername() + "\", " + 
									"\"has_profile_image\": \"" + activity.isHas_profile_image() + "\", " + 
									"\"file_name\": \"" + activity.getFile_name() + "\", " + 
									"\"related_user_id\": \"" + activity.getRelated_user_id() + "\", " + 
									"\"related_username\": \"" + activity.getRelated_username() + "\", " + 
									"\"related_user_has_profile_image\": \"" + activity.isRelated_user_has_profile_image() + "\", " + 
									"\"related_user_file_name\": \"" + activity.getRelated_user_file_name() + "\", " + 
									"\"activity_flag\": \"" + activity.getActivity_flag() + "\", " + 
									"\"activity_message\": \"" + activity.getActivity_message() + "\", " + 
									"\"article_id\": \"" + activity.getArticleDetail().getArticle_id() + "\", " + 
									"\"media_name\": \"" + activity.getArticleDetail().getMedia_name() + "\", " + 
									"\"comment_id\": \"" + activity.getArticleComment().getId() + "\", " + 
									"\"contents\": \"" + activity.getArticleComment().getContents() + "\", " + 
									"\"follow_id\": \"" + activity.getFollow().getId() + "\", " + 
									"\"follower_group\": \"" + activity.getFollow().getFollower_group() + "\", " + 
									"\"create_date\": \"" + activity.getCreate_date() + "\", " + 
									"\"update_date\": \"" + activity.getUpdate_date() + "\" }, ");
		}
		if(activities.size() > 0) sb.replace(sb.lastIndexOf(","), sb.length(), "");
		sb.append(" ]");
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(sb.toString());
	}
}
