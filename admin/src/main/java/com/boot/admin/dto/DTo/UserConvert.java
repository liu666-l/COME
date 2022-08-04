package com.boot.admin.dto.DTo;

import com.boot.admin.dto.UserDTO;
import com.boot.admin.pojo.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserConvert {
    UserConvert MAPPER = Mappers.getMapper(UserConvert.class);

    public UserDTO do2dto(User person);

    public List<UserDTO> dos2dtos(List<User> list);
}
