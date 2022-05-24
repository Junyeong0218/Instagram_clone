package apiController.auth_api;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.oreilly.servlet.MultipartRequest;

import entity.JwtProperties;
import entity.SecurityContext;
import entity.User;
import repository.UserDao;
import request_dto.CheckInputReqDto;
import service.AuthService;
import service.AuthServiceImpl;

@WebServlet("/auth/userinfo")
@MultipartConfig(
	fileSizeThreshold = 1024 * 1024,
	maxFileSize = 1024 * 1024 * 3,
	maxRequestSize = 1024 * 1024 * 10
)
public class UserinfoController extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	private AuthService authService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(getServletConfig());
		ServletContext servletContext = config.getServletContext();
		authService = new AuthServiceImpl((UserDao) servletContext.getAttribute("userDao"));
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String paramName = request.getParameterNames().nextElement();
		String value = request.getParameter(paramName);
		CheckInputReqDto checkInputReqDto = CheckInputReqDto.of(paramName, value);
		
		int result = authService.checkInput(checkInputReqDto);

		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(result);
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 중간에 한글 경로 포함되어 있어도 알아서 파일 넣어줌.
		// form-data 로 받았을 때의 fileName으로 저장되기 때문에 js로 파일명 변경을 시도했으나, 보안 문제로 input type=file 의 value를 임의로 변경할 수 없었음.
		// 파일명을 username.extension 으로 변경한 새로운 File 객체를 생성하고 DataTransfer 객체의 items에 add 한 후 input type=file에 덮어쓰기로 해결함.
		User sessionUser = SecurityContext.certificateUser(request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, ""));
		
		String formDataString = request.getParameter("formData");
		System.out.println(formDataString);
		
//		Iterator<Part> parts = request.getParts().iterator();
//		while(parts.hasNext()) {
//			Part part = parts.next();
//			if(part.getName().equals("file")) continue;
//			String value = new String(part.getInputStream().readAllBytes(), "UTF-8");
//			System.out.println(part.getName() + " : " + value);
//		}
		
//		Iterator<String> paramNames = request.getParameterNames().asIterator();
//		
//		while(paramNames.hasNext()) {
//			String paramName = paramNames.next();
//			System.out.println(paramName + " : " + request.getParameter(paramName));
//		}
//		String dir = FileUploadPathConfig.getFileUploadPath() + "/user_profile_images/";
//		MultipartRequest multipartRequest = new MultipartRequest(request, dir, 1024 * 1024 * 3, "UTF-8");
//		Enumeration<String> params = multipartRequest.getParameterNames();
//		
//		User user = new User();
//		user.setId(sessionUser.getId());
//		while(params.hasMoreElements()) {
//			user = setParameters(multipartRequest, params.nextElement(), user);
//		}
//		user.setFile_name(multipartRequest.getOriginalFileName("file"));
//		
//		int result = authService.updateUserinfo(sessionUser, user);
//		
//		if(result > 0) {
////			User updatedUser = authService.getUser(sessionUser.getUsername());
//			response.sendRedirect("/main");
//		} else {
//			response.sendError(416, "update userinfo failed");
//		}
	}
	
	private User setParameters(MultipartRequest multipartRequest, String parameterName, User user) {
		switch(parameterName) {
			case "username":
				user.setUsername(multipartRequest.getParameterValues(parameterName)[0]);
				break;
			case "name":
				user.setName(multipartRequest.getParameterValues(parameterName)[0]);
				break;
			case "website":
				user.setWebsite(multipartRequest.getParameterValues(parameterName)[0]);
				break;
			case "description":
				user.setDescription(multipartRequest.getParameterValues(parameterName)[0]);
				break;
			case "email":
				user.setEmail(multipartRequest.getParameterValues(parameterName)[0]);
				break;
			case "phone":
				user.setPhone(multipartRequest.getParameterValues(parameterName)[0]);
				break;
			case "gender":
				String gender = multipartRequest.getParameterValues(parameterName)[0];
				user.setGender(gender.equals("남성") ? 0 : gender.equals("여성") ? 1 : gender.equals("맞춤 성별") ? 2 : 3);
				break;
		}
		return user;
	}
}
