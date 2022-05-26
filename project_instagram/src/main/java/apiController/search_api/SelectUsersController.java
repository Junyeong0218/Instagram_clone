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

import entity.User;
import repository.SearchDao;
import service.SearchService;
import service.SearchServiceImpl;

@WebServlet("/search/users")
public class SelectUsersController extends HttpServlet {
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
		System.out.println("진입");
		String keyword = (String) request.getAttribute("keyword");
		System.out.println(keyword);
		List<User> users = searchService.selectUsers(keyword, sessionUser.getId());
		StringBuilder sb = new StringBuilder();
		
		sb.append("[ ");
		for(User user: users) {
			sb.append(" { \"id\": \"" + user.getId() + "\", " + 
									"\"username\": \"" + user.getUsername() + "\", " + 
									"\"name\": \"" + user.getName() + "\", " + 
									"\"has_profile_image\": \"" + user.isHas_profile_image() + "\", " + 
									"\"file_name\": \"" + user.getFile_name() + "\" }, ");
		}
		if(users.size() > 0) sb.replace(sb.lastIndexOf(","), sb.length(), "");
		sb.append(" ]");
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(sb.toString());
	}
}
