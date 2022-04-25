package com.koala.linkfilterapp.linkfilterapi.controller.maintenance;

import com.koala.linkfilterapp.linkfilterapi.api.common.dto.response.RestResponse;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.CommonException;
import com.koala.linkfilterapp.linkfilterapp.security.dto.UserSearchBean;
import com.koala.linkfilterapp.linkfilterapp.security.entity.Roles;
import com.koala.linkfilterapp.linkfilterapp.security.entity.User;
import com.koala.linkfilterapp.linkfilterapi.service.user.UserManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/userManagement")
public class UserManagementController {
    @Autowired
    UserManagementService service;

    @GetMapping("/getUsers")
    public RestResponse<Page<User>> getUsers(
            @RequestParam(required = false) String email,
            @RequestParam(required = false, defaultValue = "asc") String sortDir,
            @RequestParam(required = false, defaultValue = "modifiedDate") String sortType,
            @RequestParam(required = false, defaultValue = "0") Integer pagoNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize
    ) {
        UserSearchBean searchBean = UserSearchBean.builder()
                .email(email)
                .sortDir(sortDir)
                .sortType(sortType)
                .pageNo(pagoNo)
                .pageSize(pageSize)
                .build();
        Page<User> users = service.getUsers(searchBean);
        return new RestResponse<>(HttpStatus.OK.toString(), "", users, null);
    }

    @PutMapping("/editUser")
    public RestResponse<User> editUser(
            @RequestBody User user
    ) throws CommonException {
        User editedUser = service.editUser(user);
        return new RestResponse<>(HttpStatus.OK.toString(), "", editedUser, null);
    }

    @PutMapping("/addRole")
    public RestResponse<User> addRole(
            @RequestParam String email,
            @RequestParam Roles role) throws CommonException {
        User user = service.addRole(email, role);
        return new RestResponse<>(HttpStatus.OK.toString(), String.format("%s granted %s", email, role), user, null);
    }

    @PutMapping("/removeRole")
    public RestResponse<User> removeRole(
            @RequestParam String email,
            @RequestParam Roles role) throws CommonException {
        User user = service.removeRole(email, role);
        return new RestResponse<>(HttpStatus.OK.toString(), String.format("%s removed from %s", email, role), user, null);
    }
}
