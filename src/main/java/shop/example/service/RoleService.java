package shop.example.service;

import shop.example.dto.request.role.RoleRequest;
import shop.example.dto.response.role.RoleResponse;

import java.util.List;

public interface RoleService {
    void createRole(RoleRequest request);
    List<RoleResponse> getAllRole();
    void deleteRole(Long role);
}
