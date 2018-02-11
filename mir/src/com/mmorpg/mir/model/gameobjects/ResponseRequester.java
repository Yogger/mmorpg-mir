package com.mmorpg.mir.model.gameobjects;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.h2.util.New;

public class ResponseRequester {
	private static final Logger log = Logger.getLogger(ResponseRequester.class);
	private Player player;
	private HashMap<RequestHandlerType, RequestResponseHandler> map = new HashMap<RequestHandlerType, RequestResponseHandler>();

	public ResponseRequester(Player player) {
		this.player = player;
	}

	private void clearDeprecated() {
		List<RequestHandlerType> removes = New.arrayList();
		for (Entry<RequestHandlerType, RequestResponseHandler> entry : map.entrySet()) {
			if (entry.getValue().deprecated()) {
				removes.add(entry.getKey());
			}
		}
		for (RequestHandlerType type : removes) {
			map.remove(type);
		}
	}

	/**
	 * Adds this handler to this messageID, returns false if there already
	 * exists one
	 * 
	 * @param messageId
	 *            ID of the request message
	 * @return true or false
	 */
	public synchronized boolean putRequest(RequestHandlerType messageId, RequestResponseHandler handler) {
		clearDeprecated();
		map.put(messageId, handler);
		return true;
	}

	/**
	 * Responds to the given message ID with the given response Returns success
	 * 
	 * @param messageId
	 * @param response
	 * @return Success
	 */
	public synchronized boolean respond(RequestHandlerType messageId, int response) {
		clearDeprecated();
		RequestResponseHandler handler = map.get(messageId);
		if (handler != null) {
			map.remove(messageId);
			log.debug("RequestResponseHandler triggered for response code " + messageId + " from " + player.getName());
			handler.handle(player, response);
			return true;
		}
		return false;
	}

	public synchronized boolean hadRequest(RequestHandlerType messageId) {
		clearDeprecated();
		return map.containsKey(messageId);
	}

	/**
	 * Automatically responds 0 to all requests, passing the given player as the
	 * responder
	 */
	public synchronized void denyAll() {
		clearDeprecated();
		for (RequestResponseHandler handler : map.values()) {
			handler.handle(player, 0);
		}

		map.clear();
	}
}