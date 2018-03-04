package pac.servlet;

import pac.dao.UserInfoDAO;
import pac.http.HttpRequest;
import pac.http.HttpResponse;
import pac.vo.UserInfo;

public class LoginServlet extends HttpServlet {
	@Override
	public void service(HttpRequest request, HttpResponse response) {
		try {
			UserInfoDAO dao = new UserInfoDAO();
			
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			UserInfo userInfo = dao.findByUsername(username);
			
			if (userInfo != null && userInfo.getPassword().equals(password)) {
				response.sendRedirect("login_suc.html");
			} else {
				response.sendRedirect("login_fail.html");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
