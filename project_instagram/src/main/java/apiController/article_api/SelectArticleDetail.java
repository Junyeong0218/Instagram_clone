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

import entity.ArticleComment;
import entity.ArticleMedia;
import entity.User;
import repository.ArticleDao;
import response_dto.ArticleDetailResDto;
import service.ArticleService;
import service.ArticleServiceImpl;

@WebServlet("/article/select-article-detail")
public class SelectArticleDetail extends HttpServlet {
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
		
		int article_id = Integer.parseInt(request.getParameter("article_id"));
		
		ArticleDetailResDto articleDetailResDto = articleService.selectArticleDetail(article_id, sessionUser.getId());
		List<ArticleMedia> media_list = articleDetailResDto.getMedia_list();
		List<ArticleComment> article_comment_list = articleDetailResDto.getArticle_comment_list();
		System.out.println(articleDetailResDto);
		
		StringBuilder sb = new StringBuilder();
		sb.append("{ \"id\": \"" + articleDetailResDto.getId() + "\", " + 
							  " \"user_id\": \"" + articleDetailResDto.getUser_id() + "\", " + 
							  " \"username\": \"" + articleDetailResDto.getUsername() + "\", " + 
							  " \"has_profile_image\": \"" + articleDetailResDto.isHas_profile_image() + "\", " +
							  " \"file_name\": \"" + articleDetailResDto.getFile_name() + "\", " +
							  " \"feature\": \"" + articleDetailResDto.getFeature() + "\", " +
							  " \"contents\": \"" + articleDetailResDto.getContents() + "\", " +
							  " \"article_create_date\": \"" + articleDetailResDto.getArticle_create_date() + "\", " +
							  " \"like_flag\": \"" + articleDetailResDto.isLike_flag() + "\", " + 
							  " \"total_like_count\": \"" + articleDetailResDto.getLike_user_count() + "\", " + 
							  " \"media_list\": [ ");
		for(int i = 0; i < media_list.size(); i++) {
			sb.append(" { \"media_type\": \"" + media_list.get(i).getMedia_type() + "\", "
								+ " \"media_name\": \"" + media_list.get(i).getMedia_name() + "\" }, ");
		}
		sb.replace(sb.lastIndexOf(","), sb.length(), "");
		sb.append(" ], \"article_comment_list\": [ ");
		
		for(ArticleComment comment : article_comment_list) {
			sb.append("{ \"id\": \"" + comment.getId() + "\", " + 
								  " \"article_id\": \"" + comment.getArticle_id() + "\", " + 
								  " \"user_id\": \"" + comment.getUser_id() + "\", " +
								  " \"username\": \"" + comment.getUsername() + "\", " +
								  " \"has_profile_image\": \"" + comment.isHas_profile_image() + "\", " +
								  " \"file_name\": \"" + comment.getFile_name() + "\", " +
								  " \"contents\": \"" + comment.getContents() + "\", " +
								  " \"create_date\": \"" + comment.getCreate_date() + "\", " +
								  " \"comment_like_flag\": \"" + comment.isLike_flag() + "\", " +
								  " \"related_comment_count\": \"" + comment.getRelated_comment_count() + "\", " +
								  " \"comment_like_user_count\": \"" + comment.getComment_like_user_count() + "\"},  ");
		}
		sb.replace(sb.lastIndexOf(","), sb.length(), "");
		sb.append(" ] }");
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(sb.toString());
	}
}


















