package pac.http;

import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import pac.context.HttpContext;
import pac.context.ServerContext;

public class HttpRequest {
	
	private InputStream in;
	
	private String method;
	private String url;
	private String protocol;
	
	private String requestURI;
	private String queryString;
	
	private Map<String, String> parameters = new HashMap<String, String>();
	
	private Map<String, String> headers = new HashMap<String, String>();
	
	public HttpRequest(InputStream in) throws EmptyRequestException {
		System.out.println("HttpRequest: 构造方法执行.");
		this.in = in;
		
		parseRequestLine();
		parseHeaders();
		parseContent();
		
		System.out.println("HttpRequest: 构造方法执行完毕.");
	}
	
	private void parseRequestLine() throws EmptyRequestException {
		System.out.println("开始解析请求行.");
		String requestLine = readLine();
		
		if (requestLine.length() == 0) {
			throw new EmptyRequestException("空请求");
		}
		
		String[] array = requestLine.split("\\s");
		method = array[0];
		url = array[1];
		protocol = array[2];
		
		parseUrl();
		
		System.out.println("请求行: " + requestLine);
	}
	
	private void parseUrl() {
		int index = url.indexOf("?");
		if (index == -1) {
			this.requestURI = url;
		} else {
			this.requestURI = this.url.substring(0, index);
			this.requestURI = this.url.substring(index + 1);
			
			parseQueryString(queryString);
		}
		
		System.out.println("requestURI: " + requestURI);
		System.out.println("queryString: " + queryString);
		System.out.println("parameters: " + parameters);
	}
	
	private void parseHeaders() {
		System.out.println("开始解析消息头.");
		
		while (true) {
			String line = readLine();
			if ("".equals(line)) {
				break;
			}
			int index = line.indexOf(":");
			String name = line.substring(0, index).trim();
			String value = line.substring(index + 1).trim();
			headers.put(name, value);
		}
		
		System.out.println("解析消息头完毕.headers: " + headers);
	}
	
	private void parseContent() {
		if (headers.containsKey(HttpContext.HEADER_CONTENT_LENGTH)) {
			try {
				String contentType = headers.get(HttpContext.HEADER_CONTENT_TYPE);
				if ("application/x-www-form-urlencoded".equals(contentType)) {
					System.out.println("开始处理表单数据.");
					int length = Integer.parseInt(headers.get(HttpContext.HEADER_CONTENT_LENGTH));
					byte[] data = new byte[length];
					in.read(data);
					String line = new String(data);
					
					parseQueryString(line);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void parseQueryString(String line) {
		try {
			line = URLDecoder.decode(line, ServerContext.URIEncoding);
			String[] paraArray = line.split("&");
			for (String para : paraArray) {
				String[] array = para.split("=");
				if (array.length == 2) {
					this.parameters.put(array[0], array[1]);
				} else {
					this.parameters.put(array[0], "");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String readLine() {
		StringBuilder builder = new StringBuilder();
		try {
			int d = -1;
			char c1 = 'a', c2 = 'a';
			while ((d = in.read()) != -1) {
				c2 = (char) d;
				if (c1 == 13 && c2 == 10) {
					break;
				}
				builder.append(c2);
				c1 = c2;
			}
			return builder.toString().trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}

	public String getProtocol() {
		return protocol;
	}
	
	public String getHeader(String name) {
		return headers.get(name);
	}
	
	public String getRequestURI() {
		return this.requestURI;
	}
	
	public String getQueryString() {
		return this.queryString;
	}
	
	public String getParameter(String name) {
		return parameters.get(name);
	}
	
	public Map<String, String> getParameterMap() {
		return parameters;
	}
}
