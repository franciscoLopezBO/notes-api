package com.notes_api.dto.request;

import java.util.List;

public class NoteRequest {
	public Long id;
	public Long userId;
	public String title;
	public String content;
	public List<String> tags;
}
