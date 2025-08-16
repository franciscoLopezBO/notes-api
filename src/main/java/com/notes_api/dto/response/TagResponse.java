package com.notes_api.dto.response;

public class TagResponse {
	public Long id;
    public String name;

    public TagResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
