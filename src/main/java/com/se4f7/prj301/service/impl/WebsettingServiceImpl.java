package com.se4f7.prj301.service.impl;

import javax.servlet.http.Part;

import com.se4f7.prj301.constants.ErrorMessage;
import com.se4f7.prj301.model.PaginationModel;
import com.se4f7.prj301.model.request.WebsettingModelRequest;
import com.se4f7.prj301.model.response.WebsettingModelResponse;
import com.se4f7.prj301.repository.WebsettingRepository;
import com.se4f7.prj301.service.WebsettingService;
import com.se4f7.prj301.utils.FileUtil;
import com.se4f7.prj301.utils.StringUtil;

public class WebsettingServiceImpl implements WebsettingService {

	private WebsettingRepository WebsettingRepository = new WebsettingRepository();

	@Override
	public boolean create(WebsettingModelRequest request, Part image, String username) {
		// Validate title is exists.
		WebsettingModelResponse oldWebsetting = WebsettingRepository.getByType(request.getType());
		if (oldWebsetting != null) {
			throw new RuntimeException(ErrorMessage.NAME_IS_EXISTS);
		}
		// Saving file from request.
		if (image != null && image.getSubmittedFileName() != null) {
			// Call function save file and return file name.
			String fileName = FileUtil.saveFile(image);
			// Set filename saved to Model.
			request.setImage(fileName);
		}
		// Call repository saving file.
		return WebsettingRepository.create(request, username);
	}

	@Override
	public boolean update(String id, WebsettingModelRequest request, Part image, String username) {
		// Parse String to Long.
		Long idNumber = StringUtil.parseLong("Id", id);
		System.out.println(idNumber);
		// Get old Websetting.
		WebsettingModelResponse oldWebsetting = WebsettingRepository.getById(idNumber);
		// If Websetting is not exists cannot update so will throw Error.
		if (oldWebsetting == null) {
			throw new RuntimeException(ErrorMessage.RECORD_NOT_FOUND);
		}
		// Compare is type change.
		if (!request.getType().equalsIgnoreCase(oldWebsetting.getType())) {
			// Compare new title with other name in database.
			WebsettingModelResponse otherWebsetting = WebsettingRepository.getByType(request.getType());
			if (otherWebsetting != null) {
				throw new RuntimeException(ErrorMessage.NAME_IS_EXISTS);
			}
		}
		// Saving file from request.
		if (image != null && image.getSubmittedFileName() != null) {
			// Delete old image -> saving memory.
			FileUtil.removeFile(oldWebsetting.getImage());
			// Call function save file and return file name.
			String fileName = FileUtil.saveFile(image);
			// Set filename saved to Model.
			request.setImage(fileName);
		} else {
			// If image not change we don't need replace it.
			// Re-use old name.
			request.setImage(oldWebsetting.getImage());
		}
		// Call repository saving file.
		return WebsettingRepository.update(idNumber, request, username);
	}

	@Override
	public boolean deleteById(String id) {
		Long idNumber = StringUtil.parseLong("Id", id);
		System.out.println(idNumber);
		WebsettingModelResponse oldWebsetting = WebsettingRepository.getById(idNumber);
		if (oldWebsetting == null) {
			throw new RuntimeException(ErrorMessage.RECORD_NOT_FOUND);
		}
		if (oldWebsetting.getImage() != null) {
			// Delete old image -> saving memory.
			FileUtil.removeFile(oldWebsetting.getImage());
		}
		return WebsettingRepository.deleteById(idNumber);
	}

	@Override
	public WebsettingModelResponse getById(String id) {
		Long idNumber = StringUtil.parseLong("Id", id);
		WebsettingModelResponse oldWebsetting = WebsettingRepository.getById(idNumber);
		if (oldWebsetting == null) {
			throw new RuntimeException(ErrorMessage.RECORD_NOT_FOUND);
		}
		return oldWebsetting;
	}

	@Override
	public PaginationModel filter(String page, String size, String name) {
		int pageNumber = StringUtil.parseInt("Page", page);
		int sizeNumber = StringUtil.parseInt("Size", size);
		return WebsettingRepository.filterByName(pageNumber, sizeNumber, name);
	}

}
