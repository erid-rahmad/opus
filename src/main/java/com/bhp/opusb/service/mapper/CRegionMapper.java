package com.bhp.opusb.service.mapper;


import com.bhp.opusb.domain.*;
import com.bhp.opusb.service.dto.CRegionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CRegion} and its DTO {@link CRegionDTO}.
 */
@Mapper(componentModel = "spring", uses = {CCountryMapper.class})
public interface CRegionMapper extends EntityMapper<CRegionDTO, CRegion> {

    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "country.name", target = "countryName")
    CRegionDTO toDto(CRegion cRegion);

    @Mapping(target = "cCities", ignore = true)
    @Mapping(target = "removeCCity", ignore = true)
    @Mapping(source = "countryId", target = "country")
    CRegion toEntity(CRegionDTO cRegionDTO);

    default CRegion fromId(Long id) {
        if (id == null) {
            return null;
        }
        CRegion cRegion = new CRegion();
        cRegion.setId(id);
        return cRegion;
    }
}
