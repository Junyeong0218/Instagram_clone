package apiController.load_for_main;

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
import repository.UserDao;
import response_dto.RecentStoryResDto;
import service.FollowService;
import service.FollowServiceImpl;

@WebServlet("/load-recent-stories")
public class LoadRecentStories extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
private FollowService followService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(getServletConfig());
		ServletContext servletContext = config.getServletContext();
		followService = new FollowServiceImpl((UserDao) servletContext.getAttribute("userDao"));
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User sessionUser = (User) request.getSession().getAttribute("user");
		
		List<RecentStoryResDto> storyList = followService.selectRecentStories(sessionUser.getId());
		
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		for(RecentStoryResDto user : storyList) {
			sb.append("{ \"id\": \""+ user.getId() + "\"" + 
						", \"username\": \"" + user.getUsername() + "\"" + 
						", \"name\": \"" + user.getName() + "\"" + 
						", \"has_profile_image\": \"" + user.isHas_profile_image() + "\"" + 
						", \"file_name\": \""+ user.getFile_name() + "\" }, ");
		}
		if(sb.length() > 2) {
			sb.replace(sb.lastIndexOf(","), sb.length(), "");
		}
		sb.append(" ]");

		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(sb.toString());
	}
}
