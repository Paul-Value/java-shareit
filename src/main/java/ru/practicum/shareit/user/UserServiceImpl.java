package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto save(UserDto userDto) {
        isExists(userDto.getEmail());
        User user = userMapper.dtoToModel(userDto);
        return userMapper.modelToDto(userRepository.save(user));
    }

    @Override
    public UserDto update(UserDto userDto) {
        isExists(userDto.getId());
        isExists(userDto.getEmail());
        User user = userMapper.dtoToModel(userDto);
        return userMapper.modelToDto(userRepository.update(user));
    }

    @Override
    public UserDto get(Long id) {
        isExists(id);
        return userMapper.modelToDto(userRepository.get(id));
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.getAll().stream()
                .map(userMapper::modelToDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }

    @Override
    public void isExists(Long id) {
        if (!userRepository.isExist(id)) {
            throw new NotFoundException("User with id: " + id + " not found");
        }
    }

    @Override
    public void isExists(String email) {
        if (userRepository.isExist(email)) {
            throw new AlreadyExistException("Email = " + email + " already exists");

        }
    }
}
