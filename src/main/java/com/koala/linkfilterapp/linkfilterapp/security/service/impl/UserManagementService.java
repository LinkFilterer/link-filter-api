package com.koala.linkfilterapp.linkfilterapp.security.service.impl;

import com.koala.linkfilterapp.linkfilterapp.security.dto.UserSearchBean;
import com.koala.linkfilterapp.linkfilterapp.security.entity.User;
import com.koala.linkfilterapp.linkfilterapp.security.repository.UserManageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserManagementService {
    @Autowired
    UserManageRepository repository;

    public Page<User> getUsers(UserSearchBean searchBean) {
        Specification<User> querySpec = getQuerySpec(searchBean);
        Pageable pageRequest = getPageableRequest(searchBean);
        return repository.findAll(querySpec, pageRequest);
    }

    private Specification<User> getQuerySpec(UserSearchBean searchBean) {
        return ((root, query, criteriaBuilder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.hasText(searchBean.getEmail())) {
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
