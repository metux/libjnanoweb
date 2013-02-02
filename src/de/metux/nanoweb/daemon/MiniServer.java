package de.metux.nanoweb.daemon;

import de.metux.nanoweb.core.IHandler;
import de.metux.nanoweb.core.IRequest;
import de.metux.nanoweb.core.Log;
import de.metux.nanoweb.core.Request;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * minimalistic multithreaded httpd
 *
 * listener loop runs in called thread, while new worker threads
 * are spawned per request
 */
public class MiniServer {

	private static final String logname = "mini-httpd";

	/**
	 * internal worker thread class
	 */
	private class ConnectionHandler extends Thread {
		Socket socket;

		public ConnectionHandler(Socket s) {
			socket = s;
		}

		private boolean bad_request(Request request, String comment)
		throws IOException {
			request.replyStatus(IRequest.status_bad_request, "Bad request");
			request.replyFinish();
			Log.info(logname, "BAD REQUEST: "+comment);
			socket.close();
			return false;
		}

		public void run() {
			try {
				BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				OutputStream output = socket.getOutputStream();
				Request request = new Request(input, output);

				/** we only support single request per connection **/
				request.replyHeader("Connection", "close");

				{
					/* read the first request line */
					String[] req = input.readLine().split(" ");
					if (req.length != 3) {
						bad_request(request, "wrong number of request line parts");
						return;
					}

					if (!request.setMethod(req[0])) {
						bad_request(request, "unsupported request method");
						return;
					}

					request.setLocation(req[1]);
					request.setProtocol(req[2]);
				}

				/* now read the headers */
				{
					boolean run = true;
					while (run) {
						String hdr = input.readLine();
						if ((hdr == null) || (hdr.equals(""))) {
							run = false;
						} else {
							String[] l = hdr.split(":");
							if (l.length == 2)
								request.addHeader(l[0], l[1]);
						}
					}
				}

				handler.handle(request);
				request.replyFinish();
				socket.close();
			} catch (IOException e) {
				Log.error(logname, "IO Error: ", e);
			}
		}
	}

	public MiniServer(int p, IHandler h) {
		port = p;
		handler = h;
	}

	private int port;
	private IHandler handler;

	/**
	 * run the http daemon in caller thread
	 * requests are handled in additional threads
	 */
	public void run() {
		ServerSocket serversocket = null;

		try {
			serversocket = new ServerSocket(port);
		} catch (Exception e) {
			Log.error(logname, "Failed to bind listener sockt to port "+port, e);
			return;
		}

		while (true) {
			try {
				Socket connectionsocket = serversocket.accept();
				InetAddress client = connectionsocket.getInetAddress();
				Log.info(logname, "Connection from: "+client.getHostName());

				ConnectionHandler handler = new ConnectionHandler(connectionsocket);
				handler.start();
			} catch (Exception e) {
				Log.error(logname, "Failed to process connection", e);
			}
		}
	}
}
