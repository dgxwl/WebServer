package pac;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pac.context.ServerContext;

public class WebServer {
	
	private ServerSocket server;
	
	private ExecutorService threadPool;
	
	public WebServer() throws IOException {
		server = new ServerSocket(ServerContext.port);
		threadPool = Executors.newFixedThreadPool(ServerContext.threadPoolSum);
	}
	
	public void start() {
		try {
			while (true) {
				Socket socket = server.accept();
				ClientHandler handler = new ClientHandler(socket);
				threadPool.execute(handler);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			WebServer server = new WebServer();
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
