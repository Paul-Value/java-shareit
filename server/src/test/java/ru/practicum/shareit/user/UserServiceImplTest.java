package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;


    @Test
    public void createTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");
        user.setId(1L);

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto userDto = new UserDto();
        userDto.setEmail("Test@test.com");
        userDto.setName("Test User");
        userDto.setId(1L);

        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setEmail("Test@test.com");
        userCreateDto.setName("Test User");
        //userDto.setId(1L);

        userDto = userService.create(userCreateDto);
        assertEquals(1L, userDto.getId());

        verify(userRepository).save(any(User.class));
    }

    @Test
    public void findAllTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");
        user.setId(1L);

        when(userRepository.findAll()).thenReturn(List.of(user));

        Collection<UserDto> users = userService.getAll();
        assertEquals(1, users.size());

        verify(userRepository).findAll();
    }

    @Test
    public void deleteUserTest() {

        userService.delete(1L);

        verify(userRepository).deleteById(anyLong());
    }

    @Test
    public void findByIdTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");
        user.setId(1L);

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserDto userDto = userService.get(1L);
        assertEquals(1L, userDto.getId());

        verify(userRepository).findById(anyLong());
    }

    @Test
    public void updateTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");
        user.setId(1L);

        when(userRepository.existsById(any())).thenReturn(true);
        when(userRepository.existsByUserEmail(anyString())).thenReturn(false);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setEmail("Test@test.com");
        updateDto.setName("Test User");

        UserDto userDto = userService.update(updateDto, 1L);
        assertEquals(1L, userDto.getId());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldByIdNotExists() {
        long id = 2L;
        when(userRepository.existsById(id))
                .thenReturn(false);

        final NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> userService.isExists(id));

        assertEquals("User with id: " + id + " not found", exception.getMessage());
    }

    @Test
    void shouldByEmailNotExists() {
        String email = "test@test.com";
        when(userRepository.existsByUserEmail(email))
                .thenReturn(true);

        final AlreadyExistException exception = Assertions.assertThrows(AlreadyExistException.class, () -> userService.isExists(email));

        assertEquals("Email = " + email + " already exists", exception.getMessage());
    }
}