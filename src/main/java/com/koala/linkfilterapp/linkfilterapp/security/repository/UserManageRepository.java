package com.koala.linkfilterapp.linkfilterapp.security.repository;

import com.koala.linkfilterapp.linkfilterapp.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserManageRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
}
