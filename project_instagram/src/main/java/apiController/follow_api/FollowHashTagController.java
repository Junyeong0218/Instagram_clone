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

import entity.HashTag;
import entity.JwtProperties;
import entity.SecurityContext;
import entity.User;
import repository.FollowDao;
import repository.NewActivityDao;
import service.FollowService;
import service.FollowServiceImpl;

@WebServlet("/follow/hashtag")
public class FollowHashTagController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private FollowService followService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(getServletConfig());
		ServletContext servletContext = config.getServletContext();
		followService = new FollowServiceImpl((FollowDao) servletContext.getAttribute("followDao"),
																					(NewActivityDao) servletContext.getAttribute("newActivityDao"));
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User sessionUser = SecurityContext.certificateUser(request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, ""));
		int page_indicator = (int) request.getAttribute("page_indicator");
		// select following hashtags
		
		List<HashTag> hashTags = followService.selectFollowingHashTags(sessionUser.getId(), page_indicator);
		System.out.println(hashTags);
		
		StringBuilder sb = new StringBuilder();
		sb.append(" [ ");
		for(HashTag hashTag : hashTags) {
			sb.append(" { \"id\": \"" + hashTag.getId() + "\", " + 
									"\"tag_name\": \"" + hashTag.getTag_name() + "\", " +
									"\"create_date\": \"" + hashTag.getCreate_date() + "\"}, ");
		}
		if(hashTags.size() > 0) sb.replace(sb.lastIndexOf(","), sb.length(), "");
		sb.append(" ]");
		
		System.out.println(sb.toString());
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(sb.toString());
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User sessionUser = SecurityContext.certificateUser(request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, ""));
		int hash_tag_id = (int) request.getAttribute("hash_tag_id");
		// insert follow hashtag
		int result = followService.insertFollowHashTag(hash_tag_id, sessionUser.getId());
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(result);
	}
	
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User sessionUser = SecurityContext.certificateUser(request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, ""));
		int hash_tag_id = (int) request.getAttribute("hash_tag_id");
		// delete follow hashtag
		int result = followService.deleteFollowHashTag(hash_tag_id, sessionUser.getId());
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(result);
	}
}
