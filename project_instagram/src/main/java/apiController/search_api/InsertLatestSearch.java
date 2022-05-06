package apiController.search_api;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entity.User;
import repository.SearchDao;
import service.SearchService;
import service.SearchServiceImpl;

@WebServlet("/search/insert-latest-search")
public class InsertLatestSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private SearchService searchService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(getServletConfig());
		ServletContext servletContext = config.getServletContext();
		searchService = new SearchServiceImpl((SearchDao) servletContext.getAttribute("searchDao"));
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User sessionUser = (User) session.getAttribute("user");
		
		boolean isUser = Boolean.parseBoolean(request.getParameter("isUser"));
		int id = Integer.parseInt(request.getParameter("id"));
		
		boolean result = searchService.insertLatestSearch(isUser, id, sessionUser.getId());
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(result);
		
	}
}
