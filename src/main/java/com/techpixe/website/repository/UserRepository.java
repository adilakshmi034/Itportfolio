package com.techpixe.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techpixe.website.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByMobileNumber(Long mobileNumber);

	User findByEmail(String email);



}
