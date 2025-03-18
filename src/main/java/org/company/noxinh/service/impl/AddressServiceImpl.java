package org.company.noxinh.service.impl;

import org.company.noxinh.constants.ResponseCode;
import org.company.noxinh.constants.ResponseMessage;
import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.address.response.DistrictResponseDTO;
import org.company.noxinh.dto.address.response.ProvinceResponseDTO;
import org.company.noxinh.dto.address.response.WardResponseDTO;
import org.company.noxinh.entity.District;
import org.company.noxinh.entity.Province;
import org.company.noxinh.entity.Ward;
import org.company.noxinh.entity.sub_entity.DistrictWard;
import org.company.noxinh.entity.sub_entity.ProvinceDistrict;
import org.company.noxinh.repository.DistrictRepository;
import org.company.noxinh.repository.ProvinceRepository;
import org.company.noxinh.repository.WardRepository;
import org.company.noxinh.repository.sub_repository.DistrictWardRepository;
import org.company.noxinh.repository.sub_repository.ProvinceDistrictRepository;
import org.company.noxinh.service.AddressService;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {
    private final ProvinceRepository provinceRepository;
    private final ProvinceDistrictRepository provinceDistrictRepository;
    private final ModelMapper modelMapper;
    private final DistrictWardRepository districtWardRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;

    public DistrictResponseDTO getDistrictById(Integer id) {
        District district = districtRepository.findById(id).orElse(null);
        if (district == null) {
            return null;
        }
        return modelMapper.map(district, DistrictResponseDTO.class);
    }

    public List<District> getDistrictByIds(List<Integer> id) {
        return districtRepository.findByIdIn(id);
    }

    public List<Province> getProvinceByIds(List<Integer> id) {
        return provinceRepository.findByIdIn(id);
    }

    public List<Ward> getWardByIds(List<Integer> id) {
        return wardRepository.findByIdIn(id);
    }

    public ProvinceResponseDTO getProvinceById(Integer id) {
        Province province = provinceRepository.findById(id).orElse(null);
        if (province == null) {
            return null;
        }
        return modelMapper.map(province, ProvinceResponseDTO.class);
    }

    public WardResponseDTO getWardById(Integer id) {
        Ward ward = wardRepository.findById(id).orElse(null);
        if (ward == null) {
            return null;
        }
        return modelMapper.map(ward, WardResponseDTO.class);
    }

    @Override
    public ResponseData<List<DistrictResponseDTO>> findDistrict(Integer provinceId) {
        log.info("Start [findDistrict] processing find list district");
        List<ProvinceDistrict> provinceDistricts = provinceDistrictRepository.findByProvinceId(provinceId);
        List<DistrictResponseDTO> responseDTOList = provinceDistricts.stream()
                .map(d -> modelMapper.map(d.getDistrict(), DistrictResponseDTO.class))
                .collect(Collectors.toList());
        log.info("End [findDistrict] processing find list district");
        return new ResponseData<>(ResponseCode.C001.getCode(), ResponseMessage.S003.getMessage(), responseDTOList);
    }

    @Override
    public ResponseData<List<ProvinceResponseDTO>> findProvince() {
        log.info("Start [findProvince] processing find list province");
        List<Province> provinces = provinceRepository.findAll();
        List<ProvinceResponseDTO> responseDTOList = provinces.stream()
                .map(p -> modelMapper.map(p, ProvinceResponseDTO.class))
                .collect(Collectors.toList());
        log.info("End [findProvince] processing find list province");
        return new ResponseData<>(ResponseCode.C001.getCode(), ResponseMessage.S002.getMessage(), responseDTOList);
    }

    @Override
    public ResponseData<List<WardResponseDTO>> findWard(Integer districtId) {
        log.info("Start [findWard] processing find list district");
        List<DistrictWard> districtWards = districtWardRepository.findByDistrictId(districtId);
        List<WardResponseDTO> responseDTOList = districtWards.stream()
                .map(d -> modelMapper.map(d.getWard(), WardResponseDTO.class))
                .collect(Collectors.toList());
        log.info("End [findWard] processing find list district");
        return new ResponseData<>(ResponseCode.C001.getCode(), ResponseMessage.S004.getMessage(), responseDTOList);
    }
}
