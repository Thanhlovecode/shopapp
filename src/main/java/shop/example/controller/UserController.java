package shop.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.example.dto.request.user.UserRequest;
import shop.example.dto.request.user.UserUpdateRequest;
import shop.example.dto.response.common.ResponseData;
import shop.example.dto.response.user.UserResponse;
import shop.example.service.UserService;

import java.util.List;


@RestController
@Slf4j
@RequestMapping("${api.prefix}/user")
@Tag(name = "User Controller")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping
    @Operation(summary = "create new user", description = "API for creating new user")
    public ResponseEntity<ResponseData<?>> createUser(@RequestBody @Valid UserRequest userRequest) {
        userService.createUser(userRequest);
        log.info("User '{}' created successfully", userRequest.getFullname());
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseData.builder()
                .status(HttpStatus.CREATED.value())
                .message("User created successfully")
                .build());
    }

    @GetMapping
    @Operation(summary = "get all users", description = "API for getting all users")
    public ResponseEntity<ResponseData<List<UserResponse>>> getAllUser() {
        return ResponseEntity.ok(ResponseData.<List<UserResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .data(userService.getAllUser())
                .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "get user by id",description = "API for get user by id")
    public ResponseEntity<ResponseData<UserResponse>> findUserById(@PathVariable("id") Long id){
        return ResponseEntity.ok(ResponseData.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .data(userService.findById(id))
                .build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete user by id", description = "API for delete user by id")
    public ResponseEntity<ResponseData<?>> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        log.info("User with '{}' is deleted",id);
        return ResponseEntity.ok(ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Delete user successfully")
                .build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "update user", description = "API for update user")
    public ResponseEntity<ResponseData<?>> updateUser(@PathVariable("id") Long id,
                                                      @RequestBody UserUpdateRequest request) {
        userService.updateUser(id, request);
        log.info("User '{}' update successfully", request.getFullname());
        return ResponseEntity.ok(ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Update user successfully")
                .build());

    }

}
