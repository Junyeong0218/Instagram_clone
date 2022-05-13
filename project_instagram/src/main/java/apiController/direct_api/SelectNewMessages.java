package apiController.direct_api;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/direct/select-new-messages")
public class SelectNewMessages extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] message_ids = request.getParameterValues("message_list[]");
		List<Integer> message_id_list = Arrays.stream(message_ids).map(e -> Integer.parseInt(e))
																														   .collect(Collectors.toList());
		System.out.println(message_id_list);
	}
}
