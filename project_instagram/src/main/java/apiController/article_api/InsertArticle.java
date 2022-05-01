package apiController.article_api;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;

import entity.User;
import repository.ArticleDao;
import request_dto.InsertArticleReqDto;
import service.ArticleService;
import service.ArticleServiceImpl;

@WebServlet("/article/insert-article")
@MultipartConfig(
		fileSizeThreshold = 1024 * 1024,
		maxFileSize = 1024 * 1024 * 10,
		maxRequestSize = 1024 * 1024 * 50
	)
public class InsertArticle extends HttpServlet {
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
		User sessionUser = (User)session.getAttribute("user");
		
		String dir = "C:\\Users\\wbfld\\Desktop\\backend 수업_instagram\\project_instagram\\project_instagram\\src\\main\\webapp\\static\\images\\article_medias\\";
		File file = new File(dir + sessionUser.getUsername());
		if(!file.exists()) file.mkdirs();
		MultipartRequest multipartRequest = new MultipartRequest(request, dir + sessionUser.getUsername(), 1024 * 1024 * 50, "UTF-8");
		
		InsertArticleReqDto dto = new InsertArticleReqDto();
		dto.setUser_id(sessionUser.getId());
		dto.setFeature(multipartRequest.getParameter("feature"));
		dto.setContents(multipartRequest.getParameter("contents"));
		System.out.println(dto);
		
		String[] types = multipartRequest.getParameterValues("type");
		dto.setMedia_type_list(Arrays.asList(types));
		
		List<String> media_name_list  = new ArrayList<String>();
		Set<String> fileNames = multipartRequest.getFileNameSet();
		Iterator<String> iterator = fileNames.iterator();
		
		while(iterator.hasNext()) {
			media_name_list.add(multipartRequest.getOriginalFileName(iterator.next()));
		}
		
		dto.setMedia_name_list(media_name_list);
		
		System.out.println(dto);
		
		boolean result = articleService.insertArticle(dto);
		
		if(result) {
			File new_folder = new File(dir + dto.getArticle_id());
			new_folder.mkdirs();
			for(int i = 0; i < media_name_list.size(); i++) {
				Path before = Paths.get(dir + sessionUser.getUsername() + File.separator + media_name_list.get(i));
				Path after = Paths.get(dir + dto.getArticle_id() + File.separator + media_name_list.get(i));
				System.out.println(before.toString());
				System.out.println(after.toString());
				Files.move(before, after, StandardCopyOption.REPLACE_EXISTING);				
			}
		}
		file.delete();
		
		System.out.println(result);
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(result);
		
	}
}
