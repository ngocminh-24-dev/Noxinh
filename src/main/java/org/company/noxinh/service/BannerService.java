package org.company.noxinh.service;

import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.banner.request.CreateBannerRequestDTO;
import org.company.noxinh.dto.banner.request.DisplayBannerRequestDTO;
import org.company.noxinh.dto.banner.request.UpdateBannerRequestDTO;
import org.company.noxinh.dto.banner.response.BannerResponseDTO;
import org.company.noxinh.dto.banner.response.BannerResponseDTOV2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * The interface Banner service.
 */
public interface BannerService {
    /**
     * Create banner response data.
     *
     * @param request the request
     * @return the response data
     */
    ResponseData<String> createBanner(CreateBannerRequestDTO request);

    /**
     * Change display response data.
     *
     * @param bannerID the banner id
     * @return the response data
     */
    ResponseData<String> changeDisplay(DisplayBannerRequestDTO bannerID);

    /**
     * Gets banner display.
     *
     * @return the banner display
     */
    ResponseData<List<BannerResponseDTO>> getBannerDisplay();

    /**
     * Delete banner response data.
     *
     * @param bannerID the banner id
     * @return the response data
     */
    ResponseData<String> deleteBanner(Integer bannerID);

    /**
     * Update banner response data.
     *
     * @param bannerID the banner id
     * @param request  the request
     * @return the response data
     */
    ResponseData<String> updateBanner(Integer bannerID, UpdateBannerRequestDTO request);

    /**
     * Gets banner list.
     *
     * @param pageable the pageable
     * @return the banner list
     */
    ResponseData<Page<BannerResponseDTOV2>> getBannerList(Pageable pageable);
}
