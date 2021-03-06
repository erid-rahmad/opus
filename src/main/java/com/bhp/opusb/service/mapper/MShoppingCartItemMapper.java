package com.bhp.opusb.service.mapper;


import com.bhp.opusb.domain.MShoppingCartItem;
import com.bhp.opusb.service.dto.MShoppingCartItemDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link MShoppingCartItem} and its DTO {@link MShoppingCartItemDTO}.
 */
@Mapper(componentModel = "spring", uses = {ADOrganizationMapper.class, MProductCatalogMapper.class, MShoppingCartMapper.class})
public interface MShoppingCartItemMapper extends EntityMapper<MShoppingCartItemDTO, MShoppingCartItem> {

    @Mapping(source = "adOrganization.id", target = "adOrganizationId")
    @Mapping(source = "adOrganization.name", target = "adOrganizationName")
    @Mapping(source = "MProductCatalog.id", target = "MProductCatalogId")
    @Mapping(source = "MProductCatalog.name", target = "MProductCatalogName")
    @Mapping(source = "MShoppingCart.id", target = "MShoppingCartId")
    MShoppingCartItemDTO toDto(MShoppingCartItem mShoppingCartItem);

    @Mapping(source = "adOrganizationId", target = "adOrganization")
    @Mapping(source = "MProductCatalogId", target = "MProductCatalog")
    @Mapping(source = "MShoppingCartId", target = "MShoppingCart")
    MShoppingCartItem toEntity(MShoppingCartItemDTO mShoppingCartItemDTO);

    default MShoppingCartItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        MShoppingCartItem mShoppingCartItem = new MShoppingCartItem();
        mShoppingCartItem.setId(id);
        return mShoppingCartItem;
    }
}
