package com.itsmoarigato.mvc.rest;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadCommand {
	
	int arigatoId;

	public int getArigatoId() {
		return arigatoId;
	}

	public void setArigatoId(int arigatoId) {
		this.arigatoId = arigatoId;
	}
	
	@NotNull
	MultipartFile file;

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	
	
	
}
