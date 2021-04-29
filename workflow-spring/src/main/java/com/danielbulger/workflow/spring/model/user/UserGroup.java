package com.danielbulger.workflow.spring.model.user;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
public class UserGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, unique = true)
	private String name;

	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime created;

	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime modified;

	@Column(nullable = false)
	private boolean active;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "userGroups")
	private Set<User> users;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		joinColumns = @JoinColumn(name = "user_group_id"),
		inverseJoinColumns = @JoinColumn(name="permission_id")
	)
	private Set<Permission> permissions;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getModified() {
		return modified;
	}

	public void setModified(LocalDateTime modified) {
		this.modified = modified;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		UserGroup userGroup = (UserGroup) o;
		return id == userGroup.id && active == userGroup.active && name.equals(userGroup.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, active);
	}

	@Override
	public String toString() {
		return "UserGroup{" +
			"id=" + id +
			", name='" + name + '\'' +
			", active=" + active +
			'}';
	}
}
