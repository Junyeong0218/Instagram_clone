package filter.controller_dispatcher;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.RequestMethod;

@WebServlet("/message/*")
public class MessageControllerParamFilter implements Filter {
	
	private final String MESSAGE = "/message";
	private final String MESSAGE_ROOMS = "/message/rooms";
	private final String MESSAGE_ROOM = "/message/room";
	private final String MESSAGE_ROOM_FLAG = "/message/room/flag";
	private final String MESSAGE_REACTION = "/message/reaction";
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		String uri = req.getRequestURI();
		System.out.println("uri : " + uri);
		
		String method = req.getMethod();
		System.out.println("method : " + method);
		
		if(uri.equals(MESSAGE_ROOMS) || uri.equals(MESSAGE_ROOM) || uri.equals(MESSAGE_ROOM_FLAG) || uri.equals(MESSAGE_REACTION)) {
			chain.doFilter(request, response);
			return;
		} else if(uri.equals(MESSAGE) && method.equals(RequestMethod.POST)) {
			chain.doFilter(request, response);
			return;
		}
		
		uri = uri.replace("/message", "");
		String[] uris = uri.split("/");
		System.out.println("after replace : " + uri);
		System.out.println("uris.length : " + uris.length);
		for(String s : uris) {
			System.out.println(s);
		}
		
		if(uris.length == 2) {
			try {
				// /message allows GET PUT DELETE
				int message_id = Integer.parseInt(uris[1]);
				request.setAttribute("message_id", message_id);
				
				if(method.equals(RequestMethod.GET) ||
					method.equals(RequestMethod.PUT) ||
					method.equals(RequestMethod.DELETE)) {
					request.getRequestDispatcher(MESSAGE).forward(request, response);
				} else {
					resp.sendError(404, "invalid Method");
				}
			} catch (NumberFormatException e) {
				if(uris[1].equals("rooms")) {
					// /message/rooms allows only GET method
					if(method.equals(RequestMethod.GET)) {
						request.getRequestDispatcher(MESSAGE_ROOMS).forward(request, response);
					} else {
						resp.sendError(404, "invalid Method");
					}
				} else if(uris[1].equals("room")) {
					// /message/room allow only POST method
					if(method.equals(RequestMethod.POST)) {
						request.getRequestDispatcher(MESSAGE_ROOM).forward(request, response);
					} else {
						resp.sendError(404, "invalid Method");
					}
				} else {
					resp.sendError(404, "bad request");
				}
			}
		} else if(uris.length == 3) {
			if(uris[1].equals("room")) {
				try {
					int room_id = Integer.parseInt(uris[2]);
					request.setAttribute("room_id", room_id);
					// /message/room/(int) allows only GET method
					if(method.equals(RequestMethod.GET)) {
						request.getRequestDispatcher(MESSAGE_ROOM).forward(request, response);
					} else {
						resp.sendError(404, "invalid Method");
					}
				} catch (NumberFormatException e) {
					if(uris[2].equals("flag")) {
						// /message/reaction/flag allows only GET method
						if(method.equals(RequestMethod.GET)) {
							request.getRequestDispatcher(MESSAGE_ROOM_FLAG).forward(request, response);
						} else {
							resp.sendError(404, "invalid Method");
						}
					} else {
						resp.sendError(404, "bad request");
					}
				}
			} else if(uris[1].equals("reaction")) {
				try {
					int message_id = Integer.parseInt(uris[2]);
					request.setAttribute("message_id", message_id);
					// /message/reaction/(int) allows POST DELETE method
					if(method.equals(RequestMethod.POST) ||
						method.equals(RequestMethod.DELETE)) {
						request.getRequestDispatcher(MESSAGE_REACTION).forward(request, response);
					} else {
						resp.sendError(404, "invalid Method");
					}
				} catch (Exception e) {
					System.out.println(this.getClass().getName() + " :::: Integer parseInt Error occured!");
					resp.sendError(409, "invalid params");
				}
			} else {
				resp.sendError(404, "bad request");
			}
		}
	}
}
