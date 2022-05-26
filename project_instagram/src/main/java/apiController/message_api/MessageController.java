package apiController.message_api;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.FileUploadPathConfig;
import entity.User;
import repository.MessageDao;
import repository.NewActivityDao;
import service.FileService;
import service.MessageService;
import service.MessageServiceImpl;

@WebServlet("/message")
@MultipartConfig(
		fileSizeThreshold = 1024 * 1024,
		maxFileSize = 1024 * 1024 * 3,
		maxRequestSize = 1024 * 1024 * 10
)
public class MessageController extends HttpServlet {
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// message_id 로 검색
		int message_id = (Integer) request.getAttribute("message_id");
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User sessionUser = (User) request.getAttribute("sessionUser");
		
		int room_id = Integer.parseInt(request.getParameter("room_id"));
		boolean result = false;
		
		if(request.getContentType().contains("multipart/form-data")) {
			if(FileService.hasFile(request.getParts())) {
				// insert image type direct message
				String fileName = null;
				
				try {
					fileName = FileService.uploadImageMessage(request.getParts(), FileUploadPathConfig.getFileUploadPath() + "/message_images/");
				} catch (IOException e) {
					System.out.println(this.getClass().getName() + " :::: file upload failed!");
					fileName = null;
				}
				
				if(fileName != null) {
					result = messageService.insertDirectImageMessage(sessionUser.getId(), room_id, fileName);
				}
			}
		} else {
			// insert text type direct message
			String contents = request.getParameter("contents");
			
			result = messageService.insertDirectTextMessage(sessionUser.getId(), room_id, contents);
			
		}
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(result);
	}
	
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// message_id 로 메시지 삭제 ( /message/{message_id}
		int message_id = (Integer) request.getAttribute("message_id");
		
		boolean result = messageService.updateMessageToDelete(message_id);
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(result);
	}
}
