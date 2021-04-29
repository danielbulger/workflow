package com.danielbulger.workflow.spring.model.graph;

import com.danielbulger.workflow.spring.model.user.User;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Edge {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToOne(optional = false)
	private Port from;

	@OneToOne(optional = false)
	private Port to;

	@CreatedBy
	@OneToOne(optional = false)
	private User createdBy;

	@OneToOne(optional = false)
	private User modifiedBy;

	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime created;

	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime modified;

	@Column(nullable = false)
	private boolean active;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Port getFrom() {
		return from;
	}

	public void setFrom(Port from) {
		this.from = from;
	}

	public Port getTo() {
		return to;
	}

	public void setTo(Port to) {
		this.to = to;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
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
}
