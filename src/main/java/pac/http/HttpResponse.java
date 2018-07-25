package pac.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import pac.context.HttpContext;
import pac.context.ServerContext;

public class HttpResponse {
	
	OutputStream out;
	
	private File entity;
	
	private Map<String, String> headers = new HashMap<String, String>();
	
	private int status_code;
	
	public HttpResponse(OutputStream out) {
		this.out = out;
	}
	
	public void flush() {
		sendStatusLine();
		sendHeaders();
		sendContent();
	}
	
	private void sendStatusLine() {
		System.out.println("HttpResponse: 发送状态行");
		try {
			String line = ServerContext.protocol + " " + status_code + " " + HttpContext.getStatusReasonByStatusCode(status_code);
			System.out.println("状态行: " + line);
			println(line);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendHeaders() {
		System.out.println("HttpResponse: 发送响应头.");
		try {
			for (Entry<String, String> e : headers.entrySet()) {
				String line = e.getKey() + ":" + e.getValue();
				System.out.println("header: " + line);
				println(line);
			}
			println("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendContent() {
		System.out.println("HttpResponse: 发送响应正文.");
		if (entity == null) {
			return ;
		}
		try (
				FileInputStream fis = new FileInputStream(entity);
				BufferedInputStream bis = new BufferedInputStream(fis);
		){
			byte[] buf = new byte[1024 * 10];
			int len = -1;
			while ((len = bis.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
			System.out.println("正文响应完毕.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void println(String line) {
		try {
			out.write(line.getBytes("ISO8859-1"));
			out.write(HttpContext.CR);
			out.write(HttpContext.LF);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public File getEntity() {
		return entity;
	}

	public void setEntity(File entity) {
		this.entity = entity;
	}
	
	public void setContentType(String contentType) {
		this.headers.put(HttpContext.HEADER_CONTENT_TYPE, contentType);
	}
	
	public void setContentLength(long length) {
		this.headers.put(HttpContext.HEADER_CONTENT_LENGTH, length + "");
	}
	
	public void setStatusCode(int code) {
		this.status_code = code;
	}
	
	public void sendRedirect(String url) {
		this.setStatusCode(HttpContext.STATUS_CODE_REDIRECT);
		this.headers.put(HttpContext.HEADER_LOCATION, url);
		this.flush();
	}
}
