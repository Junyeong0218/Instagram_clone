package apiController.article_api;

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
import repository.ArticleDao;
import service.ArticleService;
import service.ArticleServiceImpl;

@WebServlet("/article/insert-comment")
public class InsertComment extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ArticleService articleService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(getServletConfig());
		ServletContext servletContext = config.getServletContext();
		articleService = new ArticleServiceImpl((ArticleDao) servletContext.getAttribute("articleDao"));
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User sessionUser = (User) session.getAttribute("user");
		
		int article_id = Integer.parseInt(request.getParameter("article_id"));
		String contents = request.getParameter("comment");
		System.out.println(article_id);
		System.out.println(contents);
		int result = articleService.insertComment(article_id, contents, sessionUser.getId());
		System.out.println(result);
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(result);
	}
}
