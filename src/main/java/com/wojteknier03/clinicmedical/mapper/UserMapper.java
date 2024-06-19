package com.wojteknier03.clinicmedical.mapper;

import com.wojteknier03.clinicmedical.dto.UserDto;
import com.wojteknier03.clinicmedical.model.AppUser;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToUserDto(AppUser user);
    AppUser userDtoToUser(UserDto userDto);
    List<UserDto> toDtoList(List<AppUser> user);
}
