package pac.servlet;

import java.io.File;

import pac.context.HttpContext;
import pac.http.HttpRequest;
import pac.http.HttpResponse;

public class HttpServlet {
	public void service(HttpRequest request, HttpResponse response) {
		
	}
	
	public void forward(String uri, HttpRequest request, HttpResponse response) {
		File page = new File("webapps" + uri);
		response.setStatusCode(HttpContext.STATUS_CODE_OK);
		response.setContentLength(page.length());
		response.setContentType("text/html");
		response.setEntity(page);
		response.flush();
	}
}
