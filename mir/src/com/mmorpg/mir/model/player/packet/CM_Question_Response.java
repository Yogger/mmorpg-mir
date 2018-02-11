package com.mmorpg.mir.model.player.packet;

public class CM_Question_Response {
	private int questionid;
	private int response;

	public int getQuestionid() {
		return questionid;
	}

	public void setQuestionid(int questionid) {
		this.questionid = questionid;
	}

	public int getResponse() {
		return response;
	}

	public void setResponse(int response) {
		this.response = response;
	}

}
