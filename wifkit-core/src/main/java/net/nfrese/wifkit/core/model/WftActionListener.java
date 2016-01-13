package net.nfrese.wifkit.core.model;

import com.fasterxml.jackson.databind.JsonNode;

public interface WftActionListener extends java.util.EventListener{
	
	void onAction(JsonNode args);
	
}
