package si.fri.rsoteam.services.mappers;

import si.fri.rsoteam.lib.dtos.UserDto;
import si.fri.rsoteam.models.entities.UserEntity;

public class UserMapper {
    public static UserDto entityToDto(UserEntity ue) {
        UserDto userDto = new UserDto();
        userDto.id = ue.getId();
        userDto.birthDay = ue.getBirthDay();
        userDto.email = ue.getEmail();
        userDto.surname = ue.getSurname();
        userDto.name = ue.getName();
        userDto.gsm = ue.getGsm();

        return userDto;
    }

    public static UserEntity dtoToEntity(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userDto.email);
        userEntity.setName(userDto.name);
        userEntity.setSurname(userDto.surname);
        userEntity.setBirthDay(userDto.birthDay);
        userEntity.setGsm(userDto.gsm);

        return userEntity;
    }
}
