package com.example.pcswebserver.web.payload;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface JwtTokenMapper {

    JwtTokenMapper INSTANCE = Mappers.getMapper(JwtTokenMapper.class);

    @Mapping(target = "expiresAt", dateFormat = "dd-MM-yyyy HH:mm:ss")
    JwtToken toPayload(DecodedJWT jwt);
}
