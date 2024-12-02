package com.techpixe.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techpixe.website.constants.SalesStatus;
import com.techpixe.website.entity.Leads;

public interface LeadsRepository extends JpaRepository<Leads, Long> {

	@Query(value = "SELECT * FROM leads l WHERE l.sales_Id = :sales_Id", nativeQuery = true)
	List<Leads> findLeadsBySalesIdNative(@Param("sales_Id") Long sales_Id);

//	@Query("SELECT l FROM Leads l WHERE l.superAdmin.id = :id")
//    List<Leads> findLeadsBySuperAdminId(@Param("id") Long superAdminId);

	List<Leads> findByStatusNot(SalesStatus status);

//	List<Leads> findBySalesSalesid(Long sales_Id);

}
