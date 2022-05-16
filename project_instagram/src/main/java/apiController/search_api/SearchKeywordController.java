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
import javax.servlet.http.HttpSession;

import entity.SearchKeyword;
import entity.User;
import repository.SearchDao;
import service.SearchService;
import service.SearchServiceImpl;

@WebServlet("/search/keyword")
public class SearchKeywordController extends HttpServlet {
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
		HttpSession session = request.getSession();
		User sessionUser = (User) session.getAttribute("user");
		
		String keyword = (String) request.getAttribute("keyword");
		
		List<SearchKeyword> searchResult = searchService.selectKeyword(keyword, sessionUser.getId());
		StringBuilder sb = new StringBuilder();
		
		sb.append(" [ ");
		for(SearchKeyword result : searchResult) {
			sb.append(" { \"user_id\": \"" + result.getSearched_user_id() +  "\", " + 
									"\"username\" : \"" + result.getUsername() + "\", " + 
									"\"name\" : \"" + result.getName() + "\", " + 
									"\"has_profile_image\" : \"" + result.isUser_follow_flag() + "\", " + 
									"\"file_name\" : \"" + result.getFile_name() + "\", " + 
									"\"user_follow_flag\" : \"" + result.isUser_follow_flag() + "\", " + 
									"\"hash_tag_id\" : \"" + result.getHash_tag_id() + "\", " + 
									"\"tag_name\" : \"" + result.getTag_name() + "\", " + 
									"\"hash_tag_follow_flag\" : \"" + result.isHash_tag_follow_flag() + "\" }, ");
		}
		if(searchResult.size() > 0) sb.replace(sb.lastIndexOf(","), sb.length(), "");
		sb.append(" ]");
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(sb.toString());
	}
}
