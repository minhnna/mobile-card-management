package vn.com.cardmanagement.service.mapper;

import vn.com.cardmanagement.domain.*;
import vn.com.cardmanagement.service.dto.TopupRequestDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TopupRequest and its DTO TopupRequestDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TopupRequestMapper extends EntityMapper<TopupRequestDTO, TopupRequest> {



    default TopupRequest fromId(String id) {
        if (id == null) {
            return null;
        }
        TopupRequest topupRequest = new TopupRequest();
        topupRequest.setId(id);
        return topupRequest;
    }
}
