package com.koala.linkfilterapp.linkfilterapi.repository;

import com.koala.linkfilterapp.linkfilterapi.api.common.entity.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, String> {
}
