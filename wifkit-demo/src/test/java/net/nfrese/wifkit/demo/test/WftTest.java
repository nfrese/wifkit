package net.nfrese.wifkit.demo.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import net.nfrese.wifkit.core.model.WftCall;
import net.nfrese.wifkit.core.model.WftPage;
import net.nfrese.wifkit.demo.widgets.MyWftPage;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class WftTest {
	
	@Test
	public void test()
	{
		JsonNodeFactory nf = new JsonNodeFactory(true);
		ObjectNode node = new ObjectNode(nf);
		node.put("etype", "init");
		
		WftCall call = new WftCall();
		call.addIncomingEvent(node);
		
		MyWftPage page = new MyWftPage("RootPanel");
		
		page.processRequest(call);
		
		JsonNode result = call.getResult();
		
		System.out.println(result + "");
		
		
		
	}
	
	@Test
	public void testDirectInit() throws JsonProcessingException
	{
		
		MyWftPage page = new MyWftPage("RootPanel");
		JsonNode result = page.initPage();
		
		System.out.println(result + "");
		
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
	}
	
	@Test
	public void testParseJSON() throws JsonParseException, JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		
		String jsonStr = "{\"a\":5, \"b\" : { \"c\": 7 }}";
		JsonNode node = mapper.readValue(jsonStr, JsonNode.class);
		
		Assert.assertEquals(5, node.get("a").asInt());
		Assert.assertEquals(7, node.get("b").get("c").asInt());
	}
	
	
	@Test
	@Ignore
	public void testJS() throws ScriptException, MalformedURLException, IOException
	{
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        
        InputStream input = new URL("https://ajax.googleapis.com/ajax/libs/prototype/1.7.3.0/prototype.js").openStream();
        InputStreamReader isr = new InputStreamReader(input);
        
        engine.eval(isr);

        engine.eval("print('Hello, World')");
        
	}
	

}
