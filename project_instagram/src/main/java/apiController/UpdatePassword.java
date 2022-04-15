package apiController;

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
import repository.UserDao;
import service.AuthService;
import service.AuthServiceImpl;

@WebServlet("/update-password")
public class UpdatePassword extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	private AuthService authService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(getServletConfig());
		ServletContext servletContext = config.getServletContext();
		authService = new AuthServiceImpl((UserDao) servletContext.getAttribute("userDao"));
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User sessionUser = (User) session.getAttribute("user");
		User requestDto = User.builder()
							  .id(sessionUser.getId())
							  .password(request.getParameter("password"))
							  .build();
		
		int result = authService.updatePassword(requestDto);
		
		response.setContentType("text/html; charset=UTF-8");
		if(result == 1) {
			response.getWriter().print("<script>alert(\"비밀번호 변경에 성공했습니다.\\n다시 로그인해주세요.\"); location.href=\"/logout\";</script>");
		} else {
			response.getWriter().print("<script>alert(\"비밀번호 변경에 실패했습니다.\\n다시 시도해주세요.\"); location.href=\"/userinfo\";</script>");
		}
	}
}
