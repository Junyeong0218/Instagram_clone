package apiController.direct_api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import entity.User;
import repository.MessageDao;
import repository.NewActivityDao;
import service.MessageService;
import service.MessageServiceImpl;

@WebServlet("/direct/insert-direct-image-message")
@MultipartConfig(
		fileSizeThreshold = 1024 * 1024,
		maxFileSize = 1024 * 1024 * 3,
		maxRequestSize = 1024 * 1024 * 10
)
public class InsertDirectImageMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private MessageService messageService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(getServletConfig());
		ServletContext servletContext = config.getServletContext();
		messageService = new MessageServiceImpl((MessageDao) servletContext.getAttribute("messageDao"),
																							  (NewActivityDao) servletContext.getAttribute("newActivityDao"));
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User sessionUser = (User) session.getAttribute("user");
		Collection<Part> parts = request.getParts();
		Iterator<Part> iterator = parts.iterator();
		int room_id = Integer.parseInt(request.getParameter("room_id"));
		String fileName = null;
		boolean uploadFlag = false;
		
		while(iterator.hasNext()) {
			Part part = iterator.next();
			if(part.getName().equals("file")) {
				InputStream fis = part.getInputStream();
				String path = "C:\\Users\\wbfld\\Desktop\\backend 수업_instagram\\project_instagram\\project_instagram\\src\\main\\webapp\\static\\images\\message_images";
				fileName = UUID.randomUUID().toString().replace("-", "") + "-" + part.getSubmittedFileName();
				Path filePath = Paths.get(path + File.separator + fileName);
				
				try {
					Files.write(filePath, fis.readAllBytes());
					uploadFlag = true;
				} catch (Exception e) {
					System.out.println("file upload failed!");
				}
			}
		}
		response.setContentType("text/plain; charset=UTF-8");
		if(uploadFlag) {
			boolean result = messageService.insertDirectImageMessage(sessionUser.getId(), room_id, fileName);
			response.getWriter().print(result);
		} else {
			response.getWriter().print(false);
		}
	}
}
