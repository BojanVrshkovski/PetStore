package com.petstore.repository;

import com.petstore.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet,Long> {
	@Query("SELECT p FROM Pet p WHERE p.owner IS NULL")
	List<Pet> findAvailablePets();
}
