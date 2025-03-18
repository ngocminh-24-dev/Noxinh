package org.company.noxinh.service;

import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.address.response.DistrictResponseDTO;
import org.company.noxinh.dto.address.response.ProvinceResponseDTO;
import org.company.noxinh.dto.address.response.WardResponseDTO;
import org.company.noxinh.entity.District;
import org.company.noxinh.entity.Province;
import org.company.noxinh.entity.Ward;

import java.util.List;

/**
 * The interface Address service.
 */
public interface AddressService {
    /**
     * Find district response data.
     *
     * @param provinceId the province id
     * @return the response data
     */
    ResponseData<List<DistrictResponseDTO>> findDistrict(Integer provinceId);

    /**
     * Find province response data.
     *
     * @return the response data
     */
    ResponseData<List<ProvinceResponseDTO>> findProvince();

    /**
     * Find ward response data.
     *
     * @param districtId the district id
     * @return the response data
     */
    ResponseData<List<WardResponseDTO>> findWard(Integer districtId);

    /**
     * Gets ward by id.
     *
     * @param id the id
     * @return the ward by id
     */
    WardResponseDTO getWardById(Integer id);

    /**
     * Gets province by id.
     *
     * @param id the id
     * @return the province by id
     */
    ProvinceResponseDTO getProvinceById(Integer id);

    /**
     * Gets district by id.
     *
     * @param id the id
     * @return the district by id
     */
    DistrictResponseDTO getDistrictById(Integer id);
    List<District> getDistrictByIds(List<Integer> id);
    List<Ward> getWardByIds(List<Integer> id);
    List<Province> getProvinceByIds(List<Integer> id);
}
