package com.project.prepnester.service.mapper;

import com.project.prepnester.dto.response.UserDetailsResponse;
import com.project.prepnester.model.PrepNesterUserDeatils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserToUserDetailsResponseMapper {

  UserToUserDetailsResponseMapper INSTANCE = Mappers.getMapper(
      UserToUserDetailsResponseMapper.class);

  @Mapping(source = "email", target = "email")
  UserDetailsResponse prepNesterUserToUserDetailsResponse(PrepNesterUserDeatils user);
}
