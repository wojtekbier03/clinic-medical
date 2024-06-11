package com.wojteknier03.clinic_medical.mapper;

import com.wojteknier03.clinic_medical.dto.UserDto;
import com.wojteknier03.clinic_medical.model.AppUser;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToUserDto(AppUser user);
    AppUser userDtoToUser(UserDto userDto);

    default List<UserDto> toDtoList(List<AppUser> user){
        return user.stream()
                .map(this::userToUserDto)
                .toList();
    }
}
