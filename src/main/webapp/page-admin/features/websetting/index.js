$(document).ready(function() {

	var $pagination = $('#WebSettingPagination');
	var inpSearchWebSettingName = '';
	// Init twbsPagination with defaultOpts /assets/global.settings.js)
	$pagination.twbsPagination(defaultOpts);

	// Listener event onChange when user typing on input search.
	this.onSearchByName = function() {
		// Get value from input search.
		inpSearchWebSettingName = $('#inpSearchWebSettingType').val();
		// Call function search filter value from input.
		this.getWebSetting(0, defaultPageSize, inpSearchWebSettingName);
	}

	// Function search and pagination WebSetting. 
	this.getWebSetting = function(page = 0, size = defaultPageSize, name = '') {
		// Use Ajax call API search WebSetting (/assets/http.js).
		Http.get(`${domain}/admin/api/websetting?type=filter&page=${page}&size=${size}&name=${name}`)
			.then(res => {
				let appendHTML = '';
				// Clear all elements in table content.
				$('#tblWebSetting').empty();
				// Reset pagination.
				$pagination.twbsPagination('destroy');
				// Check api error or no data response.
				if (!res.success || res.data.totalRecord === 0) {
					// Append text No Data when records empty;
					$('#tblWebSetting').append(`<tr><td colspan='10' style='text-align: center;'>No Data</td></tr>`);
					// End function.
					return;
				}

				// Build table content from data responses.
				for (const record of res.data.records) {
					appendHTML += '<tr>';
					appendHTML += `<td>${record.id}</td>`;
					appendHTML += `<td>${record.type}</td>`;
					appendHTML += `<td>${record.content}</td>`;
					appendHTML += `<td>${record.image}</td>`;
					appendHTML +=
						`<td>
                            <span class='badge ${record.status.toLocaleLowerCase() === 'active' ? 'bg-success' : 'bg-danger'}'>
                                ${record.status}
                            </span>
                        </td>`;
					appendHTML += `<td>${record.createdBy}</td>`;
					appendHTML += `<td>${record.createdDate}</td>`;
					appendHTML += `<td>${record.updatedBy}</td>`;
					appendHTML += `<td>${record.updatedDate}</td>`;

					// Append action button Edit & Delete.
					appendHTML +=
						`<td class='text-right'>
                            <a class='btn btn-info btn-sm' onclick='swicthViewWebSetting(false, ${record.id})'>
                                <i class='fas fa-pencil-alt'></i>
                            </a>
                            <a class='btn btn-danger btn-sm' onclick='deleteWebSetting(${record.id})'>
                                <i class='fas fa-trash'></i>
                            </a>
                        </td>`;
					appendHTML += '</tr>';
				}

				// Build pagination with twbsPagination.
				// More detail: https://josecebe.github.io/twbs-pagination/
				$pagination.twbsPagination($.extend({}, defaultOpts, {
					startPage: res.data.page + 1,
					totalPages: Math.ceil(res.data.totalRecord / res.data.size)
				}));
				// Add event listener when page change.
				$pagination
					.on('page', (event, num) => {
						this.getWebSetting(num - 1, defaultPageSize, inpSearchWebSettingName);
					});

				// Append html table into tBody.
				$('#tblWebSetting').append(appendHTML);
			})
			.catch(err => {
				toastr.error(err.errMsg);
			})
	}

	// Function delete WebSetting by id.
	this.deleteWebSetting = function(id) {
		// Use Ajax call API get WebSetting by id (/assets/http.js).
		if (confirm('Are you sure you want to delete this websetting?')) {

			Http.delete(`${domain}/admin/api/websetting?id=${id}`)
				.then(res => {
					if (res.success) {
						this.swicthViewWebSetting(true);
						toastr.success('Delete WebSetting success !')
					} else {
						toastr.error(res.errMsg);
					}
				})
				.catch(err => {
					toastr.error(err.errMsg);
				})
		}
	}
	
	// Call API get WebSetting by id.
	this.getWebSettingById = function(id) {
		// Use Ajax call API get WebSetting by id (/assets/http.js).
		Http.get(`${domain}/admin/api/websetting?type=getOne&id=${id}`)
			.then(res => {
				if (res.success) {
					// Set value from response on update form.
					$('#inpWebSettingId').val(id);
					$('#inpWebSettingImage').val(null);
					$('#inpWebSettingType').val(res.data.type);
					// Set value for textarea Content.
					$('#inpWebSettingContent').summernote('code', res.data.content);

				} else {
					toastr.error(res.errMsg);
				}
			})
			.catch(err => {
				toastr.error(err.errMsg);
			})
	}

	// Function create/edit WebSetting.
	this.saveWebSetting = function() {
		const currentId = $('#inpWebSettingId').val();
		// Get value from input and build a JSON Payload.
		const payload = {
			'type': $('#inpWebSettingType').val(),
			'content': $('#inpWebSettingContent').summernote('code')
		}
		// Create FormData and append files & JSON stringify.
		var formData = new FormData();
		// Append file selected from input.
		if ($('#inpWebSettingImage')[0]) {
			formData.append('image', $('#inpWebSettingImage')[0].files[0]);
		}
		// Append payload WebSetting info.
		formData.append('payload', JSON.stringify(payload));
		if (currentId) {
			// Read detail additional function putFormData in file: /assets/http.js
			Http.putFormData(`${domain}/admin/api/websetting?id=${currentId}`, formData)
				.then(res => {
					if (res.success) {
						this.swicthViewWebSetting(true);
						toastr.success(`Update WebSetting success !`)
					} else {
						toastr.error(res.errMsg);
					}
				})
				.catch(err => {
					toastr.error(err.errMsg);
				});
		} else {
			// Read detail additional function postFormData in file: /assets/http.js
			Http.postFormData(`${domain}/admin/api/websetting`, formData)
				.then(res => {
					if (res.success) {
						this.swicthViewWebSetting(true);
						toastr.success(`Create WebSetting success !`)
					} else {
						toastr.error(res.errMsg);
					}
				})
				.catch(err => {
					toastr.error(err.errMsg);
				});
		}
	};

	// TODO: Handle after.
	this.draftWebSetting = function() {
		alert("Làm biếng chưa có code");
	}

	this.initSelect2Category = function() {
		// Init value for select2 on id #selPostsCategory.
		$('#selPostsCategory').select2({
			theme: 'bootstrap4',
			// Call api search category with select2.
			ajax: {
				url: `${domain}/admin/api/category`,
				headers: {
					// Get token from localStore and append on API.
					// Read more function: /assets/http.js
					'Authorization': 'Bearer ' + Http.getToken(),
					'Content-Type': 'application/json',
				},
				data: function(params) {
					var query = {
						type: 'filter',
						page: 0,
						size: 10,
						// params.term is value input on select2.
						name: params.term
					}
					// Query parameters will be ?type=[type]&page=[page]&size=[size]&name=[params.term]
					return query;
				},
				// Transform the data returned by your API into the format expected by Select2
				// Default format when use select2 is [{id: [id], text: [text]}]
				// So we need convert data from response to format of select2.
				processResults: function(res) {
					return {
						// Why we need using function [map] ?
						// Read more: https://viblo.asia/p/su-dung-map-filter-va-reduce-trong-javascript-YWOZrxm75Q0 
						results: res.data.records.map(elm => {
							return {
								id: elm.id,
								text: elm.name
							}
						})
					};
				}
			}
		});
	}

	// Action change display screen between Table and Form Create/Edit.
	this.swicthViewWebSetting = function(isViewTable, id = null) {
		if (isViewTable) {
			$('#WebSetting-table').css('display', 'block');
			$('#WebSetting-form').css('display', 'none');
			this.getWebSetting(0, defaultPageSize);
		} else {
			// Init summernote (Text Editor).
			$('#inpWebSettingContent').summernote({ height: 150 });
			// Init select2 (Support select & search value).
			this.initSelect2Category();
			$('#WebSetting-table').css('display', 'none');
			$('#WebSetting-form').css('display', 'block');
			if (id == null) {
				$('#inpWebSettingType').val(null);
				$('#inpWebSettingImage').val(null);
				$('#inpWebSettingContent').val('').summernote('code', '');
			} else {
				this.getWebSettingById(id);
			}
		}
	};

	// Fix issues Bootstrap 4 not show file name.
	// More detail: https://stackoverflow.com/questions/48613992/bootstrap-4-file-input-doesnt-show-the-file-name
	$('#inpWebSettingImage').change(function(e) {
		if (e.target.files.length) {
			// Replace the "Choose a file" label
			$(this).next('.custom-file-label').html(e.target.files[0].name);
		}
	});

	// Set default view mode is table.
	this.swicthViewWebSetting(true);

});
