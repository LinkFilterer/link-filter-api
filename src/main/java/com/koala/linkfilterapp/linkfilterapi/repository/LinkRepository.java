package com.koala.linkfilterapp.linkfilterapi.repository;

import com.koala.linkfilterapp.linkfilterapi.api.link.enums.LinkStatus;
import com.koala.linkfilterapp.linkfilterapi.api.link.entity.Link;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, String>, JpaSpecificationExecutor<Link> {
    Optional<Link> findByUrlAndIsConnectable(String url, boolean isConnectable);

    List<Link> findByStatus(LinkStatus status);

    List<Link> findByUrl(String url);

    Page<Link> findByUrlContains(String url, Pageable pageAble);
}
