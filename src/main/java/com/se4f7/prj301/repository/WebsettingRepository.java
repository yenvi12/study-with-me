package com.se4f7.prj301.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.se4f7.prj301.constants.ErrorMessage;
import com.se4f7.prj301.enums.PostsStatusEnum;
import com.se4f7.prj301.model.PaginationModel;
import com.se4f7.prj301.model.request.WebsettingModelRequest;
import com.se4f7.prj301.model.response.WebsettingModelResponse;
import com.se4f7.prj301.utils.DBUtil;

public class WebsettingRepository {
	private static final String INSERT_SQL = "INSERT INTO web_setting (type, content, status, image, createdBy, updatedBy) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_SQL = "UPDATE web_setting SET type = ?, content = ?, status = ?, image = ?, updatedBy = ? WHERE id = ?";
	private static final String GET_BY_ID_SQL = "SELECT id, type, content, image, status, createdBy, updatedBy, createdDate, updatedDate FROM web_setting WHERE id = ?";
	private static final String GET_BY_TYPE_SQL = "SELECT * FROM web_setting WHERE type = ?";
	private static final String DELETE_BY_ID_SQL = "DELETE FROM web_setting WHERE id = ?";
	private static final String SEARCH_LIST_SQL = "SELECT w.id, w.type, w.content, w.image, w.status, w.createdBy, w.updatedBy, w.createdDate, w.updatedDate FROM web_setting w WHERE w.type LIKE ? LIMIT ? OFFSET ?";
	private static final String COUNT_BY_NAME_SQL = "SELECT COUNT(w.id) AS totalRecord FROM web_setting AS w WHERE w.type LIKE ?";

	public boolean create(WebsettingModelRequest request, String username) {
		// Open connection and set SQL query into PreparedStatement.
		try (Connection connection = DBUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL)) {
			// Set parameters.
			preparedStatement.setString(1, request.getType());
			preparedStatement.setString(2, request.getContent());
			if (request.getStatus() != null) {
				preparedStatement.setString(3, request.getStatus().toString());
			} else {
				preparedStatement.setString(3, PostsStatusEnum.ACTIVE.toString());
			}
			preparedStatement.setString(4, request.getImage());
			preparedStatement.setString(5, username);
			preparedStatement.setString(6, username);
			// Show SQL query.
			System.out.println(preparedStatement);
			// Execute query.
			preparedStatement.executeUpdate();
			return true;
		} catch (Exception e) {
			throw new RuntimeException(ErrorMessage.SQL_ERROR + e.getMessage());
		}
	}

	public boolean update(Long id, WebsettingModelRequest request, String username) {
		// Open connection and set SQL query into PreparedStatement.
		try (Connection connection = DBUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
			// Set parameters.
			preparedStatement.setString(1, request.getType());
			preparedStatement.setString(2, request.getContent());
			if (request.getStatus() != null) {
				preparedStatement.setString(3, request.getStatus().toString());
			} else {
				preparedStatement.setString(3, PostsStatusEnum.ACTIVE.toString());
			}
			preparedStatement.setString(4, request.getImage());
			preparedStatement.setString(5, username);
			preparedStatement.setLong(6, id);
			// Show SQL query.
			System.out.println(preparedStatement);
			// Execute query.
			preparedStatement.executeUpdate();
			return true;
		} catch (Exception e) {
			throw new RuntimeException(ErrorMessage.SQL_ERROR + e.getMessage());
		}
	}

	public WebsettingModelResponse getById(Long id) {
		// Open connection and set SQL query into PreparedStatement.
		try (Connection connection = DBUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID_SQL)) {
			// Set parameters.
			preparedStatement.setLong(1, id);
			// Show SQL query.
			System.out.println(preparedStatement);
			// Execute query.
			ResultSet rs = preparedStatement.executeQuery();
			if (!rs.isBeforeFirst()) {
				return null;
			}
			WebsettingModelResponse response = new WebsettingModelResponse();
			while (rs.next()) {
				response.setId(rs.getLong("id"));
				response.setContent(rs.getString("content"));
				response.setStatus(PostsStatusEnum.valueOf(rs.getString("status")));
				response.setType(rs.getString("type"));
				response.setImage(rs.getString("image"));
				response.setCreatedDate(rs.getString("createdDate"));
				response.setUpdatedDate(rs.getString("updatedDate"));
				response.setCreatedBy(rs.getString("createdBy"));
				response.setUpdatedBy(rs.getString("updatedBy"));
			}
			return response;
		} catch (Exception e) {
			throw new RuntimeException(ErrorMessage.SQL_ERROR + e.getMessage());
		}
	}

	public WebsettingModelResponse getByType(String type) {
		// Open connection and set SQL query into PreparedStatement.
		try (Connection connection = DBUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_TYPE_SQL)) {
			// Set parameters.
			preparedStatement.setString(1, type);
			// Show SQL query.
			System.out.println(preparedStatement);
			// Execute query.
			ResultSet rs = preparedStatement.executeQuery();
			if (!rs.isBeforeFirst()) {
				return null;
			}
			WebsettingModelResponse response = new WebsettingModelResponse();
			while (rs.next()) {
				response.setId(rs.getLong("id"));
				response.setContent(rs.getString("content"));
				response.setStatus(PostsStatusEnum.valueOf(rs.getString("status")));
				response.setType(rs.getString("type"));
				response.setImage(rs.getString("image"));
				response.setCreatedDate(rs.getString("createdDate"));
				response.setUpdatedDate(rs.getString("updatedDate"));
				response.setCreatedBy(rs.getString("createdBy"));
				response.setUpdatedBy(rs.getString("updatedBy"));			}
			return response;
		} catch (Exception e) {
			throw new RuntimeException(ErrorMessage.SQL_ERROR + e.getMessage());
		}
	}

	public boolean deleteById(Long id) {
		// Open connection and set SQL query into PreparedStatement.
		try (Connection connection = DBUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID_SQL)) {
			// Set parameters.
			preparedStatement.setLong(1, id);
			// Show SQL query.
			System.out.println(preparedStatement);
			// Execute query.
			preparedStatement.executeUpdate();
			return true;
		} catch (Exception e) {
			throw new RuntimeException(ErrorMessage.SQL_ERROR + e.getMessage());
		}
	}

	public PaginationModel filterByName(int page, int size, String name) {
		// Open connection and set SQL query into PreparedStatement.
		try (Connection connection = DBUtil.getConnection();
				PreparedStatement stmtSelect = connection.prepareStatement(SEARCH_LIST_SQL);
				PreparedStatement stmtCount = connection.prepareStatement(COUNT_BY_NAME_SQL)) {
			// Set parameters.
			stmtSelect.setString(1, name != null ? "%" + name + "%" : "%%");
			stmtSelect.setInt(2, size);
			stmtSelect.setInt(3, page * size);
			// Show SQL query.
			System.out.println(stmtSelect);
			// Execute query.
			// Select records.
			ResultSet rs = stmtSelect.executeQuery();
			List<WebsettingModelResponse> results = new ArrayList<WebsettingModelResponse>();
			while (rs.next()) {
				WebsettingModelResponse response = new WebsettingModelResponse();
				response.setId(rs.getLong("id"));
				response.setContent(rs.getString("content"));
				response.setStatus(PostsStatusEnum.valueOf(rs.getString("status")));
				response.setType(rs.getString("type"));
				response.setImage(rs.getString("image"));
				response.setCreatedDate(rs.getString("createdDate"));
				response.setUpdatedDate(rs.getString("updatedDate"));
				response.setCreatedBy(rs.getString("createdBy"));
				response.setUpdatedBy(rs.getString("updatedBy"));
				results.add(response);
			}

			// Count records;
			stmtCount.setString(1, name != null ? "%" + name + "%" : "%%");
			ResultSet rsCount = stmtCount.executeQuery();
			int totalRecord = 0;
			while (rsCount.next()) {
				totalRecord = rsCount.getInt("totalRecord");
			}
			return new PaginationModel(page, size, totalRecord, results);
		} catch (Exception e) {
			throw new RuntimeException(ErrorMessage.SQL_ERROR + e.getMessage());
		}
	}
}
