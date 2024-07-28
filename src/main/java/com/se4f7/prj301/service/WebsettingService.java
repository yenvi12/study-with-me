package com.se4f7.prj301.service;

import javax.servlet.http.Part;

import com.se4f7.prj301.model.PaginationModel;
import com.se4f7.prj301.model.request.WebsettingModelRequest;
import com.se4f7.prj301.model.response.WebsettingModelResponse;

public interface WebsettingService {

	public boolean create(WebsettingModelRequest request, Part image, String username);

	public boolean update(String id, WebsettingModelRequest request, Part image, String username);

	public boolean deleteById(String id);

	public WebsettingModelResponse getById(String id);

	public PaginationModel filter(String page, String size, String name);

}
