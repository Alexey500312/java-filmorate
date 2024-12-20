package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExceptionMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    @Override
    public User updateUser(User user) throws NotFoundException {
        if (userStorage.getUser(user.getId()) == null) {
            throw new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, user.getId()));
        }
        return userStorage.updateUser(user);
    }

    @Override
    public List<User> getFriends(Long userId) {
        User user = Optional.ofNullable(userStorage.getUser(userId))
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, userId)));
        return userStorage.getFriends(user);
    }

    public List<User> getFriendsCommonOther(Long userId, Long otherUserId) {
        User user = Optional.ofNullable(userStorage.getUser(userId))
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, userId)));
        User otherUser = Optional.ofNullable(userStorage.getUser(otherUserId))
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, otherUserId)));
        return userStorage.getFriendsCommonOther(user, otherUser);
    }

    @Override
    public List<User> addFriend(Long userId, Long friendId) {
        User user = Optional.ofNullable(userStorage.getUser(userId))
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, userId)));
        User friend = Optional.ofNullable(userStorage.getUser(friendId))
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, friendId)));
        if (user.equals(friend))
            throw new ValidationException("Невозможно добавить в друзья самого себя");
        return userStorage.addFriend(user, friend);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        User user = Optional.ofNullable(userStorage.getUser(userId))
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, userId)));
        User friend = Optional.ofNullable(userStorage.getUser(friendId))
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, friendId)));
        userStorage.removeFriend(user, friend);
    }
}
