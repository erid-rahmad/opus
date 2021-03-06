package com.bhp.opusb.service.mapper;


import com.bhp.opusb.domain.*;
import com.bhp.opusb.service.dto.CEventTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CEventType} and its DTO {@link CEventTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {ADOrganizationMapper.class, CBiddingTypeMapper.class})
public interface CEventTypeMapper extends EntityMapper<CEventTypeDTO, CEventType> {

    @Mapping(source = "adOrganization.id", target = "adOrganizationId")
    @Mapping(source = "adOrganization.name", target = "adOrganizationName")
    @Mapping(source = "bindingType.id", target = "bindingTypeId")
    @Mapping(source = "bindingType.name", target = "bindingTypeName")
    CEventTypeDTO toDto(CEventType cEventType);

    @Mapping(source = "adOrganizationId", target = "adOrganization")
    @Mapping(source = "bindingTypeId", target = "bindingType")
    CEventType toEntity(CEventTypeDTO cEventTypeDTO);

    default CEventType fromId(Long id) {
        if (id == null) {
            return null;
        }
        CEventType cEventType = new CEventType();
        cEventType.setId(id);
        return cEventType;
    }
}
