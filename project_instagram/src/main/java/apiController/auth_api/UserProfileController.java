package apiController.auth_api;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.ArticleMedia;
import repository.FollowDao;
import response_dto.ArticleResDto;
import response_dto.UserProfileResDto;
import service.FollowService;
import service.FollowServiceImpl;

@WebServlet("/auth/profile")
public class UserProfileController extends HttpServlet {
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
		String targetUsername = (String) request.getAttribute("targetUsername");
		int sessionUserId = (int) request.getAttribute("sessionUserId");

		UserProfileResDto resDto = followService.selectUserProfileInfo(targetUsername, sessionUserId);
		System.out.println(resDto);
		
		StringBuilder sb = new StringBuilder();
		sb.append(" { \"user_id\": \"" + resDto.getUser_id() + "\", " + 
								"\"username\" : \"" + resDto.getUsername() + "\", " +
								"\"name\" : \"" + resDto.getName() + "\", " +
								"\"has_profile_image\" : \"" + resDto.isHas_profile_image() + "\", " +
								"\"file_name\" : \"" + resDto.getFile_name() + "\", " +
								"\"article_count\" : \"" + resDto.getArticle_count() + "\", " +
								"\"follow_flag\" : \"" + resDto.isFollow_flag() + "\", " +
								"\"following\" : \"" + resDto.getFollowing() + "\", " +
								"\"follower\" : \"" + resDto.getFollower() + "\", \"article_list\" : [ ");
		List<ArticleResDto> article_list = resDto.getArticle_list();
		for(ArticleResDto article : article_list) {
			sb.append(" { \"id\" : \"" + article.getId() + "\", \"media_list\" : [ ");
			List<ArticleMedia> media_list = article.getMedia_list();
			for(ArticleMedia media : media_list) {
				sb.append(" { \"media_type\" : \"" + media.getMedia_type() + "\", " + 
										"\"media_name\": \"" + media.getMedia_name() + "\"}, ");
			}
			if(media_list.size() > 0) sb.replace(sb.lastIndexOf(","), sb.length(), "");
			sb.append(" ] }, ");
		}
		if(article_list.size() > 0) sb.replace(sb.lastIndexOf(","), sb.length(), "");
		sb.append(" ] }");
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(sb.toString());
		// json stringify 출력 필요
	}
}
