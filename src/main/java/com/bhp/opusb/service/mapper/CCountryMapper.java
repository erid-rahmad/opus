package com.bhp.opusb.service.mapper;


import com.bhp.opusb.domain.*;
import com.bhp.opusb.service.dto.CCountryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CCountry} and its DTO {@link CCountryDTO}.
 */
@Mapper(componentModel = "spring", uses = {CCurrencyMapper.class})
public interface CCountryMapper extends EntityMapper<CCountryDTO, CCountry> {

    @Mapping(source = "currency.id", target = "currencyId")
    @Mapping(source = "currency.name", target = "currencyName")
    CCountryDTO toDto(CCountry cCountry);

    @Mapping(source = "currencyId", target = "currency")
    @Mapping(target = "cRegions", ignore = true)
    @Mapping(target = "removeCRegion", ignore = true)
    @Mapping(target = "cCities", ignore = true)
    @Mapping(target = "removeCCity", ignore = true)
    CCountry toEntity(CCountryDTO cCountryDTO);

    default CCountry fromId(Long id) {
        if (id == null) {
            return null;
        }
        CCountry cCountry = new CCountry();
        cCountry.setId(id);
        return cCountry;
    }
}
