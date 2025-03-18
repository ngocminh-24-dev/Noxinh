package org.company.noxinh.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.company.noxinh.constants.ResponseCode;
import org.company.noxinh.constants.ResponseMessage;
import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.banner.request.CreateBannerRequestDTO;
import org.company.noxinh.dto.banner.request.DisplayBannerRequestDTO;
import org.company.noxinh.dto.banner.request.UpdateBannerRequestDTO;
import org.company.noxinh.dto.banner.response.BannerResponseDTO;
import org.company.noxinh.dto.banner.response.BannerResponseDTOV2;
import org.company.noxinh.entity.Banner;
import org.company.noxinh.exception.exceptions.NotFoundException;
import org.company.noxinh.repository.BannerRepository;
import org.company.noxinh.service.BannerService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

/**
 * The type Banner service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BannerServiceImpl implements BannerService {
    private final BannerRepository bannerRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseData<String> createBanner(CreateBannerRequestDTO request) {
        try {
            log.info("Start [createBanner] processing create banner");
            if (request == null) {
                return new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage());
            }
            if (Boolean.TRUE.equals(request.getIsDisplay())) {
                request.setIsDisplay(true);
                if (countBannerDisplay() >= 3) {
                    return new ResponseData<>(ResponseCode.C007.getCode(), ResponseMessage.E031.getMessage());
                }
            }
            bannerRepository.save(new Banner(request.getTitle(), request.getImage(), request.getIsDisplay(), Instant.now()));
            return new ResponseData<>(ResponseCode.C001.getCode(), ResponseMessage.S015.getMessage());
        } catch (Exception ex) {
            log.info("Error while create banner: {}", ex.getMessage());
        } finally {
            log.info("End [createBanner] processing create banner");
        }
        return new ResponseData<>(ResponseCode.C007.getCode(), ResponseMessage.E025.getMessage());
    }

    @Override
    public ResponseData<String> changeDisplay(DisplayBannerRequestDTO request) {
        if (request == null) {
            return new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage());
        }
        try {
            log.info("Start [changeDisplay] processing change display banner");
            List<Banner> bannerList = bannerRepository.findByIsDisplayTrue();
            bannerList.forEach(banner -> {
                banner.setIsDisplay(false);
                bannerRepository.save(banner);
            });
            request.getBannerIds().forEach(id -> {
                Banner banner = bannerRepository.findById(id).orElseThrow(() -> new NotFoundException(ResponseMessage.E026.getMessage() + id));
                banner.setIsDisplay(true);
                bannerRepository.save(banner);
            });
            return new ResponseData<>(ResponseCode.C001.getCode(), ResponseMessage.S020.getMessage());
        } catch (Exception ex) {
            log.info("Error change display banner: {}", ex.getMessage());
        } finally {
            log.info("End [changeDisplay] processing change display banner");
        }
        return new ResponseData<>(ResponseCode.C007.getCode(), ResponseMessage.E027.getMessage());

    }

    @Override
    public ResponseData<List<BannerResponseDTO>> getBannerDisplay() {
        log.info("Start [getBannerDisplay] processing get display banner");
        List<BannerResponseDTO> banners = bannerRepository.findByIsDisplayTrue()
                .stream()
                .map(banner -> modelMapper.map(banner, BannerResponseDTO.class))
                .sorted(Comparator.comparing(BannerResponseDTO::getCreateTime).reversed())
                .limit(3)
                .toList();
        log.info("End [getBannerDisplay] processing getdisplay banner");
        return new ResponseData<>(ResponseCode.C001.getCode(), ResponseMessage.S017.getMessage(), banners);
    }

    @Override
    public ResponseData<String> deleteBanner(Integer bannerID) {
        if (bannerID == null) {
            return new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage());
        }
        try {
            log.info("Start [deleteBanner] processing delete banner");
            Banner banner = bannerRepository.findById(bannerID)
                    .orElseThrow(() -> new NotFoundException(ResponseMessage.E026.getMessage() + bannerID));
            bannerRepository.delete(banner);
            return new ResponseData<>(ResponseCode.C001.getCode(), ResponseMessage.S018.getMessage());
        } catch (Exception ex) {
            log.info("Error while delete banner: {}", ex.getMessage());
        } finally {
            log.info("End [deleteBanner] processing delete banner");
        }
        return new ResponseData<>(ResponseCode.C007.getCode(), ResponseMessage.E029.getMessage());
    }

    @Override
    public ResponseData<String> updateBanner(Integer bannerID, UpdateBannerRequestDTO request) {
        try {
            log.info("Start [updateBanner] processing update banner");
            if (request == null || bannerID == null) {
                return new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage());
            }
            Banner banner = bannerRepository.findById(bannerID)
                    .orElseThrow(() -> new NotFoundException(ResponseMessage.E026.getMessage() + bannerID));
            banner.setTitle(request.getTitle());
            banner.setImage(request.getImage());
            bannerRepository.save(banner);
            return new ResponseData<>(ResponseCode.C001.getCode(), ResponseMessage.S019.getMessage());
        } catch (Exception ex) {
            log.info("Error while update banner: {}", ex.getMessage());
        } finally {
            log.info("End [updateBanner] processing update banner");
        }
        return new ResponseData<>(ResponseCode.C007.getCode(), ResponseMessage.E025.getMessage());
    }

    @Override
    public ResponseData<Page<BannerResponseDTOV2>> getBannerList(Pageable pageable) {
        try {
            log.info("Start [getBannerList] processing get banner list");
            Page<BannerResponseDTOV2> banners = bannerRepository.findAllBanners(pageable);
            return new ResponseData<>(ResponseCode.C001.getCode(), ResponseMessage.S017.getMessage(), banners);
        } catch (Exception ex) {
            log.info("Error while banner list : {}", ex.getMessage());
        } finally {
            log.info("End [getBannerList] processing get banner list");
        }
        return new ResponseData<>(ResponseCode.C007.getCode(), ResponseMessage.E028.getMessage());
    }

    /**
     * Change banner display.
     */
    public int countBannerDisplay() {
        return bannerRepository.countByIsDisplayTrue();
    }
}
