package com.koala.linkfilterapp.linkfilterapi.repository;

import com.koala.linkfilterapp.linkfilterapi.api.ipaddress.entity.IpAddress;
import com.koala.linkfilterapp.linkfilterapi.api.ipaddress.entity.IpAddressPk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IpAddressRepository extends JpaRepository<IpAddress, IpAddressPk>, JpaSpecificationExecutor<IpAddress> {
    Page<IpAddress> findByIdIpAddressContains(String projectName, Pageable pageAble);
}
