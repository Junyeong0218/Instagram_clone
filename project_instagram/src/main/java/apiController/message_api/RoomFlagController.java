package apiController.message_api;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import repository.MessageDao;
import service.MessageService;
import service.MessageServiceImpl;

@WebServlet("/message/room/flag")
public class RoomFlagController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private MessageService messageService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(getServletConfig());
		ServletContext servletContext = config.getServletContext();
		messageService = new MessageServiceImpl((MessageDao) servletContext.getAttribute("messageDao"));
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int current_room_id = Integer.parseInt(request.getParameter("current_room_id"));
		String[] ids = request.getParameterValues("new_message_ids[]");
		System.out.println(ids);
		
		if(ids != null && ids.length > 0) {
			List<Integer> new_message_ids = Arrays.stream(ids).map(e -> Integer.parseInt(e))
					.collect(Collectors.toList());
			System.out.println(new_message_ids);
			System.out.println(current_room_id);
			
			List<Boolean> flags = messageService.checkRoomIdByMessageId(current_room_id, new_message_ids);
			StringBuilder sb = new StringBuilder();
			
			sb.append("[ ");
			for(boolean flag : flags) {
				sb.append(flag + ", ");
			}
			if(flags.size() > 0) sb.replace(sb.lastIndexOf(","), sb.length(), "");
			sb.append(" ]");
			
			response.setContentType("text/plain; charset=UTF-8");
			response.getWriter().print(sb.toString());			
		}
	}
}
