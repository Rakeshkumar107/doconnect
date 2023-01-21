package com.wipro.cp.doconnect.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name="users")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@NotEmpty
	@Size(max = 512)
	@Column(unique = true)
	private String username;
	
	@NotNull
	@NotEmpty
	@Size(min = 8, max = 128)
	private String password;
	
	@NotNull
	@NotEmpty
	@Size(max = 512)
	private String name;
	
	@NotNull
	@NotEmpty
	@Pattern(regexp="[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")
	@Size(max = 512)
	@Column(unique = true)
	private String email;
	
	@NotNull
	private boolean isAdmin;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Long getId() {
		return id;
	}

	public User(@NotNull @NotEmpty @Size(max = 512) String username,
			@NotNull @NotEmpty @Size(max = 512) String name,
			@NotNull @NotEmpty @Size(min = 8, max = 128) String password,
			@NotNull @NotEmpty @Pattern(regexp = "[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}") @Size(max = 512) String email,
			@NotNull boolean isAdmin) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
		this.isAdmin = isAdmin;
	}

	public User() {
		super();
	}

}
