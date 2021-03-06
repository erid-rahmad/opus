package com.bhp.opusb.service.mapper;


import com.bhp.opusb.domain.MShoppingCart;
import com.bhp.opusb.service.dto.MShoppingCartDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link MShoppingCart} and its DTO {@link MShoppingCartDTO}.
 */
@Mapper(componentModel = "spring", uses = {ADOrganizationMapper.class, AdUserMapper.class})
public interface MShoppingCartMapper extends EntityMapper<MShoppingCartDTO, MShoppingCart> {

    @Mapping(source = "adOrganization.id", target = "adOrganizationId")
    @Mapping(source = "adOrganization.name", target = "adOrganizationName")
    @Mapping(source = "adUser.user.id", target = "adUserId")
    @Mapping(source = "adUser.user.login", target = "adUserName")
    MShoppingCartDTO toDto(MShoppingCart mShoppingCart);

    @Mapping(target = "mShoppingCartItems", ignore = true)
    @Mapping(target = "removeMShoppingCartItem", ignore = true)
    @Mapping(source = "adOrganizationId", target = "adOrganization")
    @Mapping(source = "adUserId", target = "adUser")
    MShoppingCart toEntity(MShoppingCartDTO mShoppingCartDTO);

    default MShoppingCart fromId(Long id) {
        if (id == null) {
            return null;
        }
        MShoppingCart mShoppingCart = new MShoppingCart();
        mShoppingCart.setId(id);
        return mShoppingCart;
    }
}
