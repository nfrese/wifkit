package net.nfrese.wifkit.core.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import net.nfrese.wifkit.core.model.WftCall;
import net.nfrese.wifkit.core.model.WftPage;

public abstract class AbstractWftService extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 1L;

	public AbstractWftService() {
		super();
	}

	protected abstract WftPage<?> pageInstance();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/json");

		if ("true".equalsIgnoreCase(request.getParameter("initpage")))
		{
			WftPage<?> page = pageInstance(); 
			JsonNode result = page.initPage();

			request.getSession().setAttribute("WFTPAGE_" + page.getWiid(), page);

			printResponse(request, response, result);
		}
		else
		{
			ObjectMapper mapper = new ObjectMapper();

			String jsonStr = getRequestBody(request);

			System.out.println("IN "+ jsonStr + "");

			JsonNode node = mapper.readValue(jsonStr, JsonNode.class);

			String pageWiid = node.get("pagewiid").textValue();
			WftPage<?> page = (WftPage<?>) request.getSession().getAttribute("WFTPAGE_" + pageWiid);
			if (page != null)
			{

				JsonNode events = node.get("events");
				WftCall call = new WftCall();

				for (int i=0; i<events.size(); i++)
				{
					JsonNode eventNode = events.get(i);
					call.addIncomingEvent((ObjectNode)eventNode);
				}
				page.processRequest(call);
				printResponse(request, response, call.getResult());
			}
		}
	}

	private void printResponse(HttpServletRequest request, HttpServletResponse response, JsonNode result) throws IOException
	{
		System.out.println("OUT "+ result + "");

		PrintWriter writer = response.getWriter();

		String jsonp = request.getParameter("jsonp");
		if (jsonp != null)
		{
			writer.write("" + jsonp+ "(\n");
		}

		ObjectMapper mapper = new ObjectMapper();
		writer.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));

		if (jsonp != null)
		{
			writer.write("\n);");
		}
	}

	private String getRequestBody(HttpServletRequest request) throws IOException
	{
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		return buffer.toString();
	}

}
