package apiController.search_api;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.ArticleDetail;
import repository.SearchDao;
import response_dto.SearchResultResDto;
import service.SearchService;
import service.SearchServiceImpl;

@WebServlet("/search/hashtag")
public class SelectHashTagContoller extends HttpServlet {
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
		String tagName = (String) request.getAttribute("tag_name");
		SearchResultResDto dto = searchService.selectAboutHashTag(tagName);
		
		StringBuilder sb = new StringBuilder();
		sb.append(" { \"articles\": [ ");
		for(ArticleDetail article : dto.getArticle_list()) {
			sb.append(" { \"id\": \"" + article.getArticle_id() + "\", " + 
									"\"media_type\": \"" + article.getMedia_type() + "\", " + 
									"\"media_name\": \"" + article.getMedia_name() + "\", " + 
									"\"like_count\": \"" + article.getLike_user_count() + "\", " + 
									"\"comment_count\": \"" + article.getComment_count() + "\"}, ");
		}
		if(dto.getArticle_list().size() > 0) sb.replace(sb.lastIndexOf(","), sb.length(), "");
		sb.append("], \"related_article_count\": \"" + dto.getRelated_article_count() + "\"}");
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(sb.toString());
	}
}
