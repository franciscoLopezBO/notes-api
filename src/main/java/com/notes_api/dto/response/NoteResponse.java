package com.notes_api.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class NoteResponse {
	public Long id;
	public Long userId;
	public String title;
	public String content;
	public List<TagResponse> tags;
	public Boolean archived = false;
	public LocalDateTime updatedAt;
	
	public NoteResponse(Long id, Long userId, String title, String content, List<TagResponse> tags, LocalDateTime updatedAt, boolean archived) {
		super();
		this.id = id;
		this.userId = userId;
		this.title = title;
		this.content = content;
		this.tags = tags;
		this.updatedAt = updatedAt;
		this.archived = archived;
	}
}
