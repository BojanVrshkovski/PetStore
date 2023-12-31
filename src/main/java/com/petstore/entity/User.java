package com.petstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "users")
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "first_name", length = 100, nullable = false)
	private String firstName;

	@Column(name = "last_name", length = 100, nullable = false)
	private String lastName;

	@Column(name = "email_address", length = 100, unique = true, nullable = false)
	private String emailAddress;

	@Column(name = "budget", precision = 10, scale = 2, nullable = false)
	private BigDecimal budget;

	public User() {

	}
	public User(Long userId, String firstName, String lastName, String emailAddress, BigDecimal budget) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.budget = budget;
	}

	public User(String firstName, String lastName, String emailAddress, BigDecimal budget) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.budget = budget;
	}

}
