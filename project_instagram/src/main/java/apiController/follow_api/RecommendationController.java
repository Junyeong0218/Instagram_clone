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

import entity.JwtProperties;
import entity.SecurityContext;
import entity.User;
import repository.FollowDao;
import response_dto.UserRecommendResDto;
import service.FollowService;
import service.FollowServiceImpl;

@WebServlet("/follow/recommendation")
public class RecommendationController extends HttpServlet {
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
		User sessionUser = SecurityContext.certificateUser(request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, ""));
		
		List<UserRecommendResDto> recommendList = followService.selectRecommendUsers(sessionUser.getId());
		
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		for(UserRecommendResDto user : recommendList) {
			sb.append("{ \"id\": \""+ user.getId() + "\"" + 
						", \"username\": \"" + user.getUsername() + "\"" + 
						", \"name\": \"" + user.getName() + "\"" + 
						", \"has_profile_image\": \"" + user.isHas_profile_image() + "\"" + 
						", \"file_name\": \""+ user.getFile_name() + "\" }, ");
		}
		if(recommendList.size() > 0) sb.replace(sb.lastIndexOf(","), sb.length(), "");
		sb.append(" ]");
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(sb.toString());
	}
}
