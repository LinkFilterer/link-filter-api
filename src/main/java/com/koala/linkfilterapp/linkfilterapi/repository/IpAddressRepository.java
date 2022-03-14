package com.koala.linkfilterapp.linkfilterapi.repository;

import com.koala.linkfilterapp.linkfilterapi.api.common.entity.IpAddress;
import com.koala.linkfilterapp.linkfilterapi.api.sponsor.entity.Sponsor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpAddressRepository extends JpaRepository<IpAddress, String> {
    Page<IpAddress> findByIpAddressContains(String projectName, Pageable pageAble);
}
