package de.metux.nanoweb.example;

import de.metux.nanoweb.core.IHandler;
import de.metux.nanoweb.core.IRequest;
import java.io.IOException;
import java.util.Properties;
import java.util.Enumeration;

/**
 * a little dummy handler, which only serves GET requests
 * and replies a dummy text
 */
public class DummyHandler implements IHandler {
	public boolean handle(IRequest request)
	throws IOException {
		request.replyHeader("Server", "nanoweb dummy");

		request.replyStatus(IRequest.status_ok, "OK");
		request.replyHeader("Content-Type", "text/plain");

		request.replyBody("Method="+request.getRequestMethod()+"\n");
		request.replyBody("URI="+request.getURI()+"\n");
		request.replyBody("Root="+request.getRoot()+"\n");
		request.replyBody("Path="+request.getPath()+"\n");

		request.replyBody("\nRequest headers:\n");
		{
			Properties hdrs = request.getHeaders();
			Enumeration<?> e = hdrs.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				request.replyBody(key+": " + hdrs.getProperty(key)+"\n");
			}
		}

		request.replyBody("Anchor: \""+request.getAttribute(IRequest.attribute_anchor)+"\"\n");
		request.replyBody("Query: \""+request.getAttribute(IRequest.attribute_query)+"\"\n");

		{
			Properties q = request.getURLParameters();
			Enumeration<?> e = q.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				request.replyBody(key+"=\""+q.getProperty(key)+"\"\n");
			}
		}

		if (request.getRequestMethod().equals("POST")) {
			request.replyBody("POST PARAMETERS:\n");
			Properties q = request.getBodyParameters();
			Enumeration<?> e = q.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				request.replyBody(key+"=\""+q.getProperty(key)+"\"\n");
			}
		} else if (request.getRequestMethod().equals("PUT")) {
			request.replyBody("PUT PARAMETERS:\n");
			Properties q = request.getBodyParameters();
			Enumeration<?> e = q.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				request.replyBody(key+"=\""+q.getProperty(key)+"\"\n");
			}
		}

		return true;
	}
}
