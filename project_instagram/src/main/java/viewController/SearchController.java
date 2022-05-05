package viewController;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import repository.SearchDao;
import response_dto.SearchResultResDto;
import service.SearchService;
import service.SearchServiceImpl;

@WebServlet("/search")
public class SearchController extends HttpServlet {
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
		String tagName = request.getParameter("tag_name");
		SearchResultResDto dto = searchService.selectAboutHashTag(tagName);
		request.setAttribute("tag_name", tagName);
		request.setAttribute("result", dto);
		
		request.getRequestDispatcher("/WEB-INF/views/search/search.jsp").forward(request, response);
	}
}
