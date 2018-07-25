package pac;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import pac.context.HttpContext;
import pac.context.ServerContext;
import pac.http.EmptyRequestException;
import pac.http.HttpRequest;
import pac.http.HttpResponse;
import pac.servlet.HttpServlet;

public class ClientHandler implements Runnable {

	private Socket socket;
	
	public ClientHandler(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		try {
			HttpRequest request = new HttpRequest(socket.getInputStream());
			
			HttpResponse response = new HttpResponse(socket.getOutputStream());
			
			String requestURI = request.getRequestURI();
			
			String serlvetName = ServerContext.getServletNameByURI(requestURI);
			
			if (serlvetName != null) {
				Class<?> cls = Class.forName(serlvetName);
				
				HttpServlet servlet = (HttpServlet) cls.newInstance();
				servlet.service(request, response);
			} else {
				File file = new File("webapps" + request.getRequestURI());
				if (file.exists()) {
					String name = file.getName();
					System.out.println("fileName: " + name);
					
					String extension = name.substring(name.lastIndexOf(".") + 1);
					System.out.println("extension:" + extension);
					String contentType = HttpContext.getMimeType(extension);
					
					System.out.println("content-Type:" + contentType);
					response.setStatusCode(HttpContext.STATUS_CODE_OK);
					
					response.setContentType(contentType);
					response.setContentLength(file.length());
					
					response.setEntity(file);
					response.flush();
				} else {
					System.out.println("文件不存在.");
				}
			}
		} catch (EmptyRequestException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
