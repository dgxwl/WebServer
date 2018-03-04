package pac.context;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ServerContext {
	public static int port;
	
	public static String protocol;
	
	public static int threadPoolSum;
	
	public static String URIEncoding;
	
	private static Map<String, String> servletMappings = new HashMap<String, String>();
	
	static {
		init();
	}
	
	public static void init() {
		initServerConfig();
		initServletMapping();
	}
	
	private static void initServerConfig() {
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new FileInputStream("conf/config.xml"));
			Element root = doc.getRootElement();
			Element connector = root.element("Connector");
			
			port = Integer.parseInt(connector.attributeValue("port"));
			protocol = connector.attributeValue("protocol");
			threadPoolSum = Integer.parseInt(connector.attributeValue("threadPool"));
			URIEncoding = connector.attributeValue("URIEncoding");
			System.out.println(port + ", " + protocol + ", " + threadPoolSum + ", " + URIEncoding);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void initServletMapping() {
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new FileInputStream("conf/servlets.xml"));
			Element root = doc.getRootElement();
			List<Element> list = root.elements();
			for (Element servletEle : list) {
				String uri = servletEle.attributeValue("uri");
				String className = servletEle.attributeValue("class");
				servletMappings.put(uri, className);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getServletNameByURI(String uri) {
		return servletMappings.get(uri);
	}
}
