package pac.context;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class HttpContext {
	public static final int CR = 13;
	public static final int LF = 10;
	
	public static final int STATUS_CODE_OK = 200;
	public static final int STATUS_CODE_NOT_FOUND = 404;
	public static final int STATUS_CODE_ERROR = 500;
	public static final int STATUS_CODE_REDIRECT = 302;
	
	private static Map<Integer, String> codeReasonMap = new HashMap<Integer, String>();
	
	public static final String HEADER_CONTENT_TYPE = "Content-Type";
	public static final String HEADER_CONTENT_LENGTH = "Content-Length";
	public static final String HEADER_LOCATION = "Location";
	
	public static Map<String, String> mimeTypeMapping = new HashMap<String, String>();
	
	static {
		init();
	}
	
	private static void init() {
		initMimeTypeMapping();
		initCodeReason();
	}
	
	private static void initMimeTypeMapping() {
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new FileInputStream("conf/web.xml"));
			Element root = doc.getRootElement();
			List<Element> mimeList = root.elements("mime-mapping");
			for (Element mimeEle : mimeList) {
				String extension = mimeEle.elementText("extendion");
				String mimeType = mimeEle.elementText("mime-type");
				mimeTypeMapping.put(extension, mimeType);
			}
			
			System.out.println("mimeTypeMapping.size(): " + mimeTypeMapping.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void initCodeReason() {
		codeReasonMap.put(200, "OK");
		codeReasonMap.put(404, "NOT FOUND");
		codeReasonMap.put(302, "MOVED PERMANENTLY");
		codeReasonMap.put(500, "INTERNAL SERVER ERROR");
	}
	
	public static String getMimeType(String extension) {
		return mimeTypeMapping.get(extension);
	}
	
	public static String getStatusReasonByStatusCode(int code) {
		return codeReasonMap.get(code);
	}
}
