package com.mmorpg.mir.model.gameobjects;

public abstract class RequestResponseHandler {
	private Creature requester;

	public RequestResponseHandler(Creature requester) {
		this.requester = requester;
	}

	public Creature getRequester() {
		return requester;
	}
	
	/**
	 * Called when a response is received
	 * 
	 * @param requested
	 *            Player whom requested this response
	 * @param responder
	 *            Player whom responded to this request
	 * @param responseCode
	 *            The response the player gave, usually 0 = no 1 = yes
	 */
	public void handle(Player responder, int response) {
		if (response == 0)
			denyRequest(requester, responder);
		else
			acceptRequest(requester, responder);
	}

	/**
	 * Called when the player accepts a request
	 * 
	 * @param requester
	 *            Creature whom requested this response
	 * @param responder
	 *            Player whom responded to this request
	 */
	public abstract void acceptRequest(Creature requester, Player responder);

	/**
	 * Called when the player denies a request
	 * 
	 * @param requester
	 *            Creature whom requested this response
	 * @param responder
	 *            Player whom responded to this request
	 */
	public abstract void denyRequest(Creature requester, Player responder);

	public abstract boolean deprecated();

}