package apiController.article_api;

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

import entity.ArticleMedia;
import entity.JwtProperties;
import entity.SecurityContext;
import entity.User;
import repository.ArticleDao;
import response_dto.ArticleResDto;
import service.ArticleService;
import service.ArticleServiceImpl;

@WebServlet("/article/list")
public class ArticleListController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private ArticleService articleService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(getServletConfig());
		ServletContext servletContext = config.getServletContext();
		articleService = new ArticleServiceImpl((ArticleDao) servletContext.getAttribute("articleDao"));
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User sessionUser = SecurityContext.certificateUser(request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, ""));
		
		int page_indicator = (Integer) request.getAttribute("page_indicator");
		List<ArticleResDto> articleResDtoList = articleService.selectArticles(sessionUser.getId());
		
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		for(ArticleResDto dto :articleResDtoList) {
			sb.append("{ \"id\": \""+ dto.getId() + "\"" + 
					", \"user_id\": \"" + dto.getUser_id() + "\"" + 
					", \"has_profile_image\": \"" + dto.isHas_profile_image() + "\"" + 
					", \"file_name\": \"" + dto.getFile_name() + "\"" + 
					", \"feature\": \"" + dto.getFeature() + "\"" + 
					", \"username\": \"" + dto.getUsername() + "\"" + 
					", \"contents\": \"" + dto.getContents() + "\"" + 
					", \"create_date\": \"" + dto.getArticle_create_date() + "\"" + 
					", \"like_flag\": \"" + dto.isLike_flag() + "\"" + 
					", \"media_list\": [ ");
			List<ArticleMedia> mediaList = dto.getMedia_list();
			for(ArticleMedia media : mediaList) {
				sb.append(" { \"media_type\": \"" + media.getMedia_type() + "\", "
									+ "\"media_name\": \"" + media.getMedia_name() + "\" }, ");
			}
			sb.replace(sb.lastIndexOf(","), sb.length(), "");
			
			sb.append("], \"total_like_count\": \"" + dto.getTotal_like_count() + "\"" + 
						", \"total_commented_user_count\": \"" + dto.getTotal_commented_user_count() + "\" }, ");
			
		}
		sb.replace(sb.lastIndexOf(","), sb.length(), "");
		sb.append(" ]");
		
		System.out.println(sb.toString());
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(sb.toString());
		
	}
}
