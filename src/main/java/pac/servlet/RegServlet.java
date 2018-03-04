package pac.servlet;

import pac.dao.UserInfoDAO;
import pac.http.HttpRequest;
import pac.http.HttpResponse;
import pac.vo.UserInfo;

public class RegServlet extends HttpServlet {
	@Override
	public void service(HttpRequest request, HttpResponse response) {
		try {
			UserInfoDAO dao = new UserInfoDAO();
			System.out.println("开始注册.");
			
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String nickname = request.getParameter("nickname");
			String phonenumber = request.getParameter("phonenumber");
			
			System.out.println(username + ", " + password + ", " + nickname + ", " + phonenumber);;
			
			UserInfo userInfo = dao.findByUsername(username);
			
			if (userInfo != null) {
				response.sendRedirect("reg_haveUser.html");
			} else {
				UserInfo user = new UserInfo(username, password, nickname, phonenumber);
				dao.save(user);
				
				System.out.println("注册完毕!");
				
				response.sendRedirect("reg_success.html");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
