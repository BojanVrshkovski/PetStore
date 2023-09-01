package com.petstore.entity;

import com.petstore.entity.enums.PetType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pets")
@Data
public class Pet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pet_id")
	private Long petId;

	@OneToOne
	@JoinColumn(name = "owner_id", referencedColumnName = "user_id")
	private User owner;

	@Column(name = "name", length = 50, nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "pet_type", length = 10, nullable = false)
	private PetType petType;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "date_of_birth", nullable = false)
	private LocalDate dateOfBirth;

	@Column(name = "price", precision = 10, scale = 2, nullable = false)
	private BigDecimal price;

	@Column(name = "rating")
	private Integer rating;
}
