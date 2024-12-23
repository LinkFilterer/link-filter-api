package com.koala.linkfilterapp.linkfilterapi.service.user;

import com.koala.linkfilterapp.linkfilterapi.api.common.exception.CommonException;
import com.koala.linkfilterapp.linkfilterapi.repository.RoleRepository;
import com.koala.linkfilterapp.linkfilterapp.security.dto.UserSearchBean;
import com.koala.linkfilterapp.linkfilterapp.security.entity.Role;
import com.koala.linkfilterapp.linkfilterapp.security.entity.Roles;
import com.koala.linkfilterapp.linkfilterapp.security.entity.User;
import com.koala.linkfilterapp.linkfilterapp.security.repository.UserManageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Slf4j
@Service
public class UserManagementService {
    @Autowired
    UserManageRepository repository;

    @Autowired
    RoleRepository roleRepository;

    public Page<User> getUsers(UserSearchBean searchBean) {
        Specification<User> querySpec = getQuerySpec(searchBean);
        Pageable pageRequest = getPageableRequest(searchBean);
        return repository.findAll(querySpec, pageRequest);
    }

    public User createUser(User user) throws CommonException {
        List<User> result = repository.findByEmail(user.getEmail());
        if (result.size() > 0) {
            log.error("User already exists");
            throw new CommonException();
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setCreatedDate(new Date());
        user.setModifiedDate(new Date());
        user.setEnabled(true);
        return repository.save(user);
    }

    public User editUser(User user) throws CommonException {
        Optional<User> foundUser = repository.findById(user.getId());
        if (!foundUser.isPresent()) {
            log.error("User not found");
            throw new CommonException();
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!foundUser.get().getPassword().equals(user.getPassword())) {
            user.setPassword(encoder.encode(user.getPassword()));
        }
        user.setModifiedDate(new Date());
        return repository.save(user);
    }

    public User deleteUser(String email) throws CommonException {
        List<User> result = repository.findByEmail(email);
        if (result.size() == 0) {
            log.error("User not found");
            throw new CommonException();
        } else if (result.size() > 1) {
            log.error("More than one user with that email! : " + result);
            throw new CommonException();
        } else {
            repository.delete(result.get(0));
        }
        return result.get(0);
    }

    public User deleteUser(Long id) throws CommonException {
        Optional<User> result = repository.findById(id);
        if (!result.isPresent()) {
            log.error("User not found");
            throw new CommonException();
        }
        repository.delete(result.get());
        return result.get();
    }

    public User addRole(String email, Roles role) throws CommonException {
        List<User> result = repository.findByEmail(email);
        validateEmail(email, result);
        User user = result.get(0);
        Role foundRole = roleRepository.findByName(role.toString());
        if (isNull(foundRole)) {
            log.error("Role not found");
            throw new CommonException();
        }
        user.getRoles().add(foundRole);
        log.info(String.format("Role %s added to %s", role, email));
        repository.save(user);
        return user;
    }

    public User removeRole(String email, Roles role) throws CommonException {
        List<User> result = repository.findByEmail(email);
        validateEmail(email, result);
        User user = result.get(0);
        Role foundRole = roleRepository.findByName(role.toString());
        if (isNull(foundRole)) {
            log.error("Role not found");
            throw new CommonException();
        }
        user.getRoles().remove(foundRole);
        log.info(String.format("Role %s removed from %s", role, email));
        repository.save(user);
        return user;
    }

    private void validateEmail(String email, List<User> result) throws CommonException {
        if (result.size() == 0) {
            CommonException exception = new CommonException(HttpStatus.NOT_FOUND, "No User found with email: " + email, null, "Update Role Request", null);
            log.error(exception.toString());
            throw exception;
        }
        if (result.size() > 1) {
            CommonException exception = new CommonException(HttpStatus.NOT_FOUND, "More than one user found with email: " + email, null, "Update Role Request", null);
            log.error(exception.toString());
            throw exception;
        }
    }

    private Specification<User> getQuerySpec(UserSearchBean searchBean) {
        return ((root, query, criteriaBuilder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(searchBean.getEmail())) {
                predicates.add(criteriaBuilder.equal(root.get("email"), searchBean.getEmail()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    private Pageable getPageableRequest(UserSearchBean searchBean) {
        Sort sortOrder = Sort.by(searchBean.getSortType());
        Optional<Sort.Direction> sortDirection = Sort.Direction.fromOptionalString(searchBean.getSortDir());
        if (sortDirection.isPresent()) {
            sortOrder = Sort.by(sortDirection.get(), searchBean.getSortType());
        }
        return PageRequest.of(searchBean.getPageNo(), searchBean.getPageSize(), sortOrder);
    }
}
