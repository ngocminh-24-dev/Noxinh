package org.company.noxinh.repository;

import io.micrometer.common.lang.NonNullApi;
import org.company.noxinh.dto.banner.response.BannerResponseDTOV2;
import org.company.noxinh.entity.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Banner repository.
 */
@Repository
@NonNullApi
public interface BannerRepository extends JpaRepository<Banner, Integer> {
    /**
     * Find byis display boolean.
     *
     * @return the boolean
     */
    int countByIsDisplayTrue();

    /**
     * Find by is display true optional.
     *
     * @return the optional
     */
    List<Banner> findByIsDisplayTrue();

    @Query("SELECT new org.company.noxinh.dto.banner.response.BannerResponseDTOV2(b.id,b.title, b.image, b.createTime, b.isDisplay) " +
            "FROM Banner b")
    Page<BannerResponseDTOV2> findAllBanners(Pageable pageable);
}
