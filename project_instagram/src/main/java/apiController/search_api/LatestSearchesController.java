package apiController.search_api;

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
import entity.LatestSearchRecord;
import entity.User;
import repository.SearchDao;
import response_dto.LatestSearchResDto;
import service.SearchService;
import service.SearchServiceImpl;

@WebServlet("/search/log")
public class LatestSearchesController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private SearchService searchService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(getServletConfig());
		ServletContext servletContext = config.getServletContext();
		searchService = new SearchServiceImpl((SearchDao) servletContext.getAttribute("searchDao"));
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User sessionUser = (User) request.getAttribute("sessionUser");
		
		LatestSearchResDto dto = searchService.selectLatestSearches(sessionUser.getId());
		StringBuilder sb = new StringBuilder();
		
		List<LatestSearchRecord> latestSearchList = dto.getLatestSearchList();
		List<User> userInfos = dto.getUserInfos();
		List<HashTag> hashTags = dto.getHashTags();
		
		sb.append("[ ");
		for(int i=0; i<userInfos.size(); i++) {
			sb.append(" { \"id\": \"" + latestSearchList.get(i).getId() + "\", " + 
									"\"searched_user_id\": \"" + userInfos.get(i).getId() + "\", " + 
									"\"username\": \"" + userInfos.get(i).getUsername() + "\", " + 
									"\"name\": \"" + userInfos.get(i).getName() + "\", " + 
									"\"has_profile_image\": \"" + userInfos.get(i).isHas_profile_image() + "\", " + 
									"\"file_name\": \"" + userInfos.get(i).getFile_name() + "\", " + 
									"\"user_follow_flag\": \"" + userInfos.get(i).isUser_follow_flag() + "\", " + 
									"\"hash_tag_id\": \"" + hashTags.get(i).getId() + "\", " + 
									"\"tag_name\": \"" + hashTags.get(i).getTag_name() + "\", " + 
									"\"hash_tag_follow_flag\": \"" + hashTags.get(i).isHash_tag_follow_flag() + "\", " + 
									"\"create_date\": \"" + latestSearchList.get(i).getCreate_date() + "\", " + 
									"\"update_date\": \"" + latestSearchList.get(i).getUpdate_date() + "\"}, ");
			
		}
		if(userInfos.size() > 0) sb.replace(sb.lastIndexOf(","), sb.length(), "");
		sb.append(" ]");
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(sb.toString());
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User sessionUser = (User) request.getAttribute("sessionUser");
		
		boolean isUser = Boolean.parseBoolean(request.getParameter("isUser"));
		int id = Integer.parseInt(request.getParameter("id"));
		
		boolean result = searchService.insertLatestSearch(isUser, id, sessionUser.getId());
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(result);
	}
}
