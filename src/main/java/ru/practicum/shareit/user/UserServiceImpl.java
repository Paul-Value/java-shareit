package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto create(UserCreateDto userDto) {
        isExists(userDto.getEmail());
        User user = UserMapper.dtoToModel(userDto);
        return UserMapper.modelToDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto update(UserUpdateDto userUpdateDto, Long userId) {
        isExists(userUpdateDto.getId());
        isExists(userUpdateDto.getEmail());
        User user = userRepository.findById(userId).get();
        if (userUpdateDto.getEmail() != null) {
            user.setEmail(userUpdateDto.getEmail());
        }
        if (userUpdateDto.getName() != null) {
            user.setName(userUpdateDto.getName());
        }
        return UserMapper.modelToDto(userRepository.save(user));
    }

    @Override
    public UserDto get(Long id) {
        isExists(id);
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        return UserMapper.modelToDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        return userRepository.findAll().stream()
                .map(UserMapper::modelToDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void isExists(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User with id: " + id + " not found");
        }
    }

    @Override
    public void isExists(String email) {
        if (userRepository.existsByUserEmail(email)) {
            throw new AlreadyExistException("Email = " + email + " already exists");

        }
    }
}
