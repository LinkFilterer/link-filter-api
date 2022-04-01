package com.koala.linkfilterapp.linkfilterapi.service.ipaddress.impl;

import com.koala.linkfilterapp.linkfilterapi.api.common.enums.AddressType;
import com.koala.linkfilterapp.linkfilterapi.api.ipaddress.dto.BanAction;
import com.koala.linkfilterapp.linkfilterapi.api.ipaddress.entity.IpAddress;
import com.koala.linkfilterapp.linkfilterapi.api.common.enums.BanStatus;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.CommonException;
import com.koala.linkfilterapp.linkfilterapi.api.ipaddress.dto.IpSearchBean;
import com.koala.linkfilterapp.linkfilterapi.api.ipaddress.entity.IpAddressPk;
import com.koala.linkfilterapp.linkfilterapi.repository.IpAddressRepository;
import com.koala.linkfilterapp.linkfilterapi.service.ipaddress.IpAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service
public class IpAddressServiceImpl implements IpAddressService {
    Logger log = Logger.getLogger("LinkMaintenanceService");

    @Autowired
    IpAddressRepository repository;

    public List<IpAddress> getAllIps() {
        return repository.findAll();
    }

    public Page<IpAddress> searchIps(IpSearchBean searchBean) {
        Specification<IpAddress> querySpec = createQuery(searchBean);
        Pageable pageRequest = getPageableRequest(searchBean);
        return repository.findAll(querySpec, pageRequest);
    }

    private Pageable getPageableRequest(IpSearchBean searchBean) {
        Sort sortOrder = Sort.by(searchBean.getSortType().toString());
        Optional<Sort.Direction> sortDirection = Sort.Direction.fromOptionalString(searchBean.getSortDirection());
        if (sortDirection.isPresent()) {
            sortOrder = Sort.by(sortDirection.get(), searchBean.getSortType().toString());
        }
        return PageRequest.of(searchBean.getPageNo(), searchBean.getPageSize(), sortOrder);
    }

    private Specification<IpAddress> createQuery(IpSearchBean searchBean) {
        return (root, query, builder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.hasText(searchBean.getIpAddress())) { predicates.add(builder.equal(root.<String>get("ipAddress"), searchBean.getIpAddress())); }
            if(nonNull(searchBean.getIpAddressType())) { predicates.add(builder.equal(root.<String>get("ipAddressType"), searchBean.getIpAddressType())); }
            if(nonNull(searchBean.getIsBanned())) { predicates.add(builder.equal(root.<String>get("isBanned"), searchBean.getIsBanned())); }
            if(nonNull(searchBean.getLastAccessed())) { predicates.add(builder.equal(root.<String>get("lastAccessed"), searchBean.getLastAccessed())); }
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public boolean checkIfBanned(String ipAddress) {
        IpAddressPk id = new IpAddressPk(ipAddress, "N/A");
        Optional<IpAddress> ipAddressEntity = repository.findById(id);
        if(ipAddressEntity.isPresent()) {
            ipAddressEntity.get().setLastAccessed(new Date());
            repository.save(ipAddressEntity.get());
            return BanStatus.BAN.equals(ipAddressEntity.get().getIsBanned());
        } else {
            IpAddress newIp = new IpAddress(id, BanStatus.NOT_BANNED, AddressType.ADMIN, new Date());
            repository.save(newIp);
            return false;
        }
    }


    @Override
    public boolean checkIfBanned(String ipAddress, AddressType addressType) {
        IpAddressPk id = new IpAddressPk(ipAddress, "N/A");
        Optional<IpAddress> ipAddressEntity = repository.findById(id);
        if(ipAddressEntity.isPresent()) {
            ipAddressEntity.get().setLastAccessed(new Date());
            repository.save(ipAddressEntity.get());
            return BanStatus.BAN.equals(ipAddressEntity.get().getIsBanned());
        } else {
            IpAddress newIp = new IpAddress(id, BanStatus.NOT_BANNED, addressType, new Date());
            repository.save(newIp);
            return false;
        }
    }

    public boolean checkIfBanned(String ipAddress, AddressType addressType, String userId) {
        IpAddressPk id = new IpAddressPk(ipAddress, userId);
        Optional<IpAddress> ipAddressEntity = repository.findById(id);
        if(ipAddressEntity.isPresent()) {
            ipAddressEntity.get().setLastAccessed(new Date());
            repository.save(ipAddressEntity.get());
            return BanStatus.BAN.equals(ipAddressEntity.get().getIsBanned());
        } else {
            IpAddress newIp = new IpAddress(id, BanStatus.NOT_BANNED, addressType, new Date());
            repository.save(newIp);
            return false;
        }
    }

    @Override
    public List<IpAddress> manageIpBan(List<BanAction> request) throws CommonException {
        IpAddressPk id = new IpAddressPk();
        if(!CollectionUtils.isEmpty(request)) {
            return request.stream().map(action -> {
                IpAddress entity;
                Optional<IpAddress> ipAddressEntity = repository.findById(id);

                if (!ipAddressEntity.isPresent()) {
                    entity = new IpAddress(id, action.getIsBanned(), null, new Date());
                    repository.save(entity);
                } else {
                    entity = ipAddressEntity.get();
                    entity.setIsBanned(action.getIsBanned());
                    repository.save(entity);
                }
                return entity;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
