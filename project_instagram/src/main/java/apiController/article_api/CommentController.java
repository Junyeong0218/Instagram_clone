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
import javax.xml.stream.events.Comment;

import entity.ArticleComment;
import entity.User;
import repository.ArticleDao;
import repository.NewActivityDao;
import service.ArticleService;
import service.ArticleServiceImpl;

@WebServlet("/article/comment")
public class CommentController extends HttpServlet {
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User sessionUser = (User) request.getAttribute("sessionUser");
		
		int comment_id = (int) request.getAttribute("comment_id");
		System.out.println("/article/integer/comment/integer get : " + comment_id);
		
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
		if(comments.size() > 0) sb.replace(sb.lastIndexOf(","), sb.length(), "");
		sb.append(" ]");
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(sb.toString());
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User sessionUser = (User) request.getAttribute("sessionUser");
		
		int article_id = (Integer) request.getAttribute("article_id");
		String contents = request.getParameter("comment");
		String _related_comment_id = request.getParameter("related_comment_id");
		
		System.out.println("article_id : " + article_id);
		System.out.println("contents : " + contents);
		System.out.println("_related_comment_id : " + _related_comment_id);
		
		int result = 0;
		
		if(_related_comment_id == null) {
			result = articleService.insertComment(article_id, contents, sessionUser.getId());
		} else {
			int related_comment_id = Integer.parseInt(_related_comment_id);
			result = articleService.insertRelatedComment(article_id, contents, sessionUser.getId(), related_comment_id);
		}
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(result);
	}
	
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 댓글 삭제
		int article_id = (int) request.getAttribute("article_id");
		int comment_id = (int) request.getAttribute("comment_id");
		User sessionUser = (User) request.getAttribute("sessionUser");
		
		ArticleComment comment = ArticleComment.builder()
																							 .article_id(article_id)
																							 .id(comment_id)
																							 .user_id(sessionUser.getId())
																							 .build();
		boolean result = articleService.deleteComment(comment);
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(result);
	}
}
