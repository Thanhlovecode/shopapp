package shop.example.service;

import shop.example.dto.request.user.UserRequest;
import shop.example.dto.request.user.UserUpdateRequest;
import shop.example.dto.response.user.UserResponse;

import java.util.List;

public interface UserService {
    void createUser(UserRequest userRequest);
    List<UserResponse> getAllUser();
    UserResponse findById(Long id);
    void updateUser(Long id, UserUpdateRequest userRequest);
    void deleteUser(Long id);
}
