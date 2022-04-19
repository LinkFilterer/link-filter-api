package com.koala.linkfilterapp.linkfilterapi.controller.maintenance;

import com.koala.linkfilterapp.linkfilterapi.api.common.dto.response.RestResponse;
import com.koala.linkfilterapp.linkfilterapp.security.dto.UserSearchBean;
import com.koala.linkfilterapp.linkfilterapp.security.entity.User;
import com.koala.linkfilterapp.linkfilterapp.security.service.impl.UserManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        return new RestResponse<>(HttpStatus.OK.toString(), "", users,null);
    }
}
