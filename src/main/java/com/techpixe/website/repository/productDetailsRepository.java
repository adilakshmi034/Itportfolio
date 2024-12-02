package com.techpixe.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.techpixe.website.entity.ProductDetails;

public interface productDetailsRepository extends JpaRepository<ProductDetails, Long> {

	@Query("SELECT pd FROM ProductDetails pd WHERE pd.product.id = :productId")
	ProductDetails findByProductId(Long productId);

	@Transactional
	@Modifying
	@Query("DELETE FROM ProductFeature pf WHERE pf.productDetails.productDetaiId = :productDetaiId")
	void deleteProductFeaturesByProductDetailId(Long productDetaiId);

	@Transactional
	@Modifying
	@Query("DELETE FROM ProductDetails pd WHERE pd.productDetaiId = :productDetaiId")
	void deleteProductDetailsById(Long productDetaiId);

	@Transactional
	@Modifying
	@Query("DELETE FROM ProductImage pi WHERE pi.productDetails.productDetaiId = :productDetaiId")
	void deleteProductImagesByProductDetailId(Long productDetaiId);

}
