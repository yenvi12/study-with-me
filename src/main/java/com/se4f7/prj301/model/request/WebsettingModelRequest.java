package com.se4f7.prj301.model.request;

import com.se4f7.prj301.enums.PostsStatusEnum;

public class WebsettingModelRequest {
	private String type;
	private String image;
	private PostsStatusEnum status;
	private String content;

	public WebsettingModelRequest() {
		super();
	}

	public WebsettingModelRequest(String type, String image, PostsStatusEnum status, String content) {
		super();
		this.type = type;
		this.image = image;
		this.status = status;
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public PostsStatusEnum getStatus() {
		return status;
	}

	public void setStatus(PostsStatusEnum status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
