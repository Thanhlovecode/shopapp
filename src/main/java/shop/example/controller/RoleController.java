package shop.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.example.dto.request.role.RoleRequest;
import shop.example.dto.response.common.ResponseData;
import shop.example.dto.response.role.RoleResponse;
import shop.example.service.RoleService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Role Controller")
@RequestMapping("${api.prefix}/role")
@Slf4j
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    @Operation(summary = "create new role",description = "API for create new role")
    public ResponseEntity<String> createRole(@RequestBody RoleRequest request) {
        roleService.createRole(request);
        log.info("Role {} create successfully", request.getRoleName());
        return ResponseEntity.status(HttpStatus.CREATED).body("Create successfully");
    }

    @GetMapping
    @Operation(summary = "get all role",description = "API for get all role")
    private ResponseEntity<ResponseData<List<RoleResponse>>> getALlRole() {
        return ResponseEntity.ok(ResponseData.<List<RoleResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .data(roleService.getAllRole())
                .build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete role by id",description = "API for delete role by id")
    public ResponseEntity<String> deleteRole(@PathVariable("id") Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok("delete successfully");
    }
}

