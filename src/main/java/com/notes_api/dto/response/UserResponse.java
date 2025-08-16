package com.notes_api.dto.response;

public class UserResponse {
	public Long id;
    public String email;
    public String role;
    
    public UserResponse() {}
    
	public UserResponse(Long id, String email, String role) {
		super();
		this.id = id;
		this.email = email;
		this.role = role;
	}
}
