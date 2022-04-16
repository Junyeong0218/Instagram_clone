package apiController.load_for_main;

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

import entity.ArticleComment;
import entity.User;
import repository.ArticleDao;
import response_dto.ArticleResDto;
import service.ArticleService;
import service.ArticleServiceImpl;

@WebServlet("/load-articles")
public class LoadArticles extends HttpServlet {
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
		HttpSession session = request.getSession();
		User sessionUser = (User) session.getAttribute("user");
		
		List<ArticleResDto> articleResDtoList = articleService.selectArticles(sessionUser.getId());
		
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		for(ArticleResDto dto :articleResDtoList) {
			sb.append("{ \"id\": \""+ dto.getId() + "\"" + 
					", \"user_id\": \"" + dto.getUser_id() + "\"" + 
					", \"has_profile_image\": \"" + dto.isHas_profile_image() + "\"" + 
					", \"file_name\": \"" + dto.getFile_name() + "\"" + 
					", \"feature\": \"" + dto.getFeature() + "\"" + 
					", \"media_type\": \"" + dto.getMedia_type() + "\"" + 
					", \"username\": \"" + dto.getUsername() + "\"" + 
					", \"contents\": \"" + dto.getContents() + "\"" + 
					", \"create_date\": \"" + dto.getArticle_create_date() + "\"" + 
					", \"like_flag\": \"" + dto.isLike_flag() + "\"" + 
					", \"media_name_list\": [ ");
			List<String> mediaNameList = dto.getMedia_name_list();
			for(String mediaName : mediaNameList) {
				sb.append("\"" + mediaName + "\", ");
			}
			sb.replace(sb.lastIndexOf(","), sb.length(), "");
			
			sb.append("], \"total_like_count\": \"" + dto.getTotal_like_count() + "\"" + 
						", \"total_commented_user_count\": \"" + dto.getTotal_commented_user_count() + "\", \"article_comment_list\": [ ");
			
			List<ArticleComment> commentList = dto.getArticle_comment_list();
			for(ArticleComment comment : commentList) {
				sb.append(" { \"id\": \"" + comment.getId() + "\"" + 
							", \"article_id\": \"" + comment.getArticle_id() + "\"" + 
							", \"commented_user_id\": \"" + comment.getCommented_user_id() + "\"" + 
							", \"commented_username\": \"" + comment.getCommented_username() + "\"" + 
							", \"contents\": \"" + comment.getContents() + "\"" + 
							", \"comment_like_user_count\": \"" + comment.getComment_like_user_count() + "\" }, ");
			}
			sb.replace(sb.lastIndexOf(","), sb.length(), "");
			sb.append("] }, ");
			
		}
		sb.replace(sb.lastIndexOf(","), sb.length(), "");
		sb.append(" ]");
		
		System.out.println(sb.toString());
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(sb.toString());
		
	}
}
