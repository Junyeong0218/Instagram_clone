package apiController.article_api;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.User;
import repository.ArticleDao;
import repository.NewActivityDao;
import service.ArticleService;
import service.ArticleServiceImpl;

@WebServlet("/article/comment/reaction")
public class CommentReactionController extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	private ArticleService articleService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(getServletConfig());
		ServletContext servletContext = config.getServletContext();
		articleService = new ArticleServiceImpl((ArticleDao) servletContext.getAttribute("articleDao"),
																					  (NewActivityDao) servletContext.getAttribute("newActivityDao"));
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User sessionUser = (User) request.getAttribute("sessionUser");
		
		int comment_id = (int) request.getAttribute("comment_id");
		
		int result = articleService.insertCommentLike(comment_id, sessionUser.getId());
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(result);
	}
	
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User sessionUser = (User) request.getAttribute("sessionUser");
		
		int comment_id = (int) request.getAttribute("comment_id");
		
		int result = articleService.deleteCommentLike(comment_id, sessionUser.getId());
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(result);
	}
}
