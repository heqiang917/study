
import md5 from "md5";

var filePreloadUrl = "/file/chunk/info";
var fileUploadUrl = "/file/chunk/upload";
var maxUploadTasks = 5;

var FileUploader = function (options) {
	this.options = options || {};
	
	this.file = this.options.file;
	this.fileSize = this.file.size;
	this.chunks = [];
	this.currentChunks = 0;
	this.loadedSize = 0;

	this.chunkSize = this.options.chunkSize || 2 * 1024 * 1024;
	this.chunkCount = this.options.chunkCount || Math.ceil(this.fileSize / this.chunkSize);

	this.startUploading = false;
	this.errorMessage = "";
};
var _FileUploader = FileUploader.prototype;

_FileUploader.start = function () {
	this.startUploading = true;
	doUpload.call(this);
};

_FileUploader.stop = function () {
	this.startUploading = false;
	for (var i = 1; i <= this.chunks.length; i++) {
		var chunk = this.chunks[i];
		if (chunk && chunk.state == 1) {
			if (chunk.xhr)
				chunk.xhr.abort();
			chunk.xhr = null;
			this.chunks[i] = null;
		}
	}
	this.currentChunks = 0;
};

_FileUploader.isComplete = function () {
	return this.fileSize <= this.loadedSize;
};

///////////////////////////////////////////////////////////
var doUpload = function () {
	console.log("doUpload");
	if (!this.startUploading)
		return ;
	while (true) {
		if (this.currentChunks >= maxUploadTasks)
			break;
		var chunkIndex = getNextChunkIndex.call(this);
		if (chunkIndex < 0)
			break;
		this.currentChunks += 1;
		uploadChunk.call(this, chunkIndex);
	}
};

var uploadChunk = function (chunkIndex) {
	var chunk = this.chunks[chunkIndex] = {index: chunkIndex, state: 1};
	initChunk.call(this, chunk).then(() => {
		return validChunk.call(this, chunk);
	}).then(() => {
		var xhr = chunk.xhr = createHttpRequest.call(this);
		if (xhr) {
			xhr.open("POST", fileUploadUrl, true);

			var self = this;
			xhr.onload = function (e) { onLoadedHandler.call(self, e, chunk); };
			xhr.onerror = function (e) { onErrorHandler.call(self, e, chunk); };
			xhr.onreadystatechange = function (e) { onUploadStateChange.call(self, e, xhr, chunk); };
			xhr.upload.onprogress = function (e) { onProgressHandler.call(self, e, chunk); };

			var form = new FormData();
			form.append("file", chunk.data);
			form.append("fileId", this.options.params.fileId);
			form.append("chunkIndex", chunkIndex);
			form.append("chunkSize", chunk.total);
			form.append("chunkNumber", this.chunkCount);
			form.append("md5", chunk.md5);

			xhr.send(form);
		}
	}).catch(() => {
		onProgressHandler.call(this, {loaded: chunk.total}, chunk);
		onLoadedHandler.call(this, {}, chunk);
	});
};

var initChunk = function (chunk) {
	var chunkStart = this.chunkSize * (chunk.index - 1);
	chunk.data = this.file.slice(chunkStart, chunkStart + this.chunkSize);
	chunk.total = chunk.data.size;
	return new Promise((resolve, reject) => {
		var reader = new FileReader();
		reader.onload = function () {
			chunk.md5 = md5(this.result);
			resolve(chunk);
		};
		reader.readAsBinaryString(chunk.data);
	});
};

var validChunk = function (chunk) {
	var params = {};
	params.fileId = this.options.params.fileId;
	params.chunkIndex = chunk.index;
	params.md5 = chunk.md5;
	return new Promise((resolve, reject) => {
		this.options.context._$get(filePreloadUrl, params, (data) => {
			resolve(chunk);
		}, () => {
			reject();
			return false;
		});
	});
};

// 从1开始
var getNextChunkIndex = function () {
	for (var i = 1; i <= this.chunkCount; i++) {
		if (!this.chunks[i])
			return i;
	}
	return -1;
};

var onUploadStateChange = function (e, xhr) {
	if (xhr.readyState == 4) {
		if (xhr.status != 200) { // 出错了
			console.log(xhr.responseText);
			var response = JSON.parse(xhr.responseText);
			this.errorMessage = response.message || "文件上传失败！";
		}
	}
};

var onLoadedHandler = function (e, chunk) {
	if (this.errorMessage) {
		chunk.state = 3; // error
		if (this.options.errorHandler) {
			var errorChunks = [];
			for (var i = 1; i <= this.chunks.length; i++) {
				if (this.chunks[i] && this.chunks[i].state == 3)
					errorChunks.push(this.chunks[i]);
			}
			if (errorChunks.length < 2) // 错误提示一次就可以了
				this.options.errorHandler(this.options.fileId, this.errorMessage);
		}
	}
	else {
		chunk.state = 2; // success
		if (this.isComplete()) {
			if (this.options.loadedHandler)
				this.options.loadedHandler(this.options.fileId);
		}
		else {
			this.currentChunks -= 1;
			doUpload.call(this);
		}
	}
	chunk.xhr = null;
};

var onProgressHandler = function (e, chunk) {
	chunk.loaded = Math.min(chunk.total, e.loaded);

	this.loadedSize = 0;
	for (var i = 1; i <= this.chunks.length; i++) {
		if (this.chunks[i])
			this.loadedSize += this.chunks[i].loaded || 0;
	}

	if (this.options.progressHandler)
		this.options.progressHandler(this.options.fileId, this.loadedSize);
};

var onErrorHandler = function (e, chunk) {
	console.log(e);
};

var createHttpRequest = function () {
	if (window.XMLHttpRequest)
		return new XMLHttpRequest();

	if (window.ActiveXObject)
		return new ActiveXObject("Microsoft.XMLHTTP");

	this._$showMsg("当前浏览器版本较低，不支持该上传功能。或者使用其他浏览器（如：chrome）。");
	return false;
};

///////////////////////////////////////////////////////////
export default FileUploader;
