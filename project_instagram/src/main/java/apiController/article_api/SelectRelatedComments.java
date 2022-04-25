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
import entity.User;
import repository.ArticleDao;
import service.ArticleService;
import service.ArticleServiceImpl;

@WebServlet("/article/select-related-comments")
public class SelectRelatedComments extends HttpServlet {
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
		
		int comment_id = Integer.parseInt(request.getParameter("comment_id"));
		
		List<ArticleComment> comments = articleService.selectRelatedComments(comment_id, sessionUser.getId());
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		for(ArticleComment comment : comments) {
			sb.append("{ \"id\": \"" + comment.getId() + "\", " + 
									"\"article_id\": \"" + comment.getArticle_id() + "\", " + 
									"\"user_id\": \"" + comment.getUser_id() + "\", " + 
									"\"username\": \"" + comment.getUsername() + "\", " + 
									"\"has_profile_image\": \"" + comment.isHas_profile_image() + "\", " + 
									"\"file_name\": \"" + comment.getFile_name() + "\", " + 
									"\"contents\": \"" + comment.getContents() + "\", " + 
									"\"create_date\": \"" + comment.getCreate_date() + "\", " + 
									"\"comment_like_user_count\": \"" + comment.getComment_like_user_count() + "\", " + 
									"\"like_flag\": \"" + comment.isLike_flag() + "\" }, ");
		}
		sb.replace(sb.lastIndexOf(","), sb.length(), "");
		sb.append(" ]");
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(sb.toString());
	}
}
