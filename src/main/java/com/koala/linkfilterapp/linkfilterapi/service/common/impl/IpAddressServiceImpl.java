package com.koala.linkfilterapp.linkfilterapi.service.common.impl;

import com.koala.linkfilterapp.linkfilterapi.api.common.dto.request.BanAction;
import com.koala.linkfilterapp.linkfilterapi.api.common.entity.IpAddress;
import com.koala.linkfilterapp.linkfilterapi.api.common.enums.BanStatus;
import com.koala.linkfilterapp.linkfilterapi.api.common.enums.IpSortType;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.LinkException;
import com.koala.linkfilterapp.linkfilterapi.repository.IpAddressRepository;
import com.koala.linkfilterapp.linkfilterapi.service.common.IpAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class IpAddressServiceImpl implements IpAddressService {
    Logger log = Logger.getLogger("LinkMaintenanceService");


    @Autowired
    IpAddressRepository repository;

    public List<IpAddress> getAllIps() {
        return repository.findAll();
    }

    public Page<IpAddress> searchIps(int pageNum, int size, IpSortType sortType, String projectName) {
        Pageable page = PageRequest.of(pageNum, size).withSort(Sort.by(sortType.toString()).ascending());
        Page<IpAddress> found = repository.findByIpAddressContains(projectName, page);
        return found;
    }

    @Override
    public boolean checkIfBanned(String ipAddress) {
        Optional<IpAddress> ipAddressEntity = repository.findById(ipAddress.trim());
        if(ipAddressEntity.isPresent()) {
            return BanStatus.BAN.equals(ipAddressEntity.get().getIsBanned());
        } else {
            IpAddress newIp = new IpAddress(ipAddress, BanStatus.NOT_BANNED, null);
            repository.save(newIp);
            return false;
        }
    }

    @Override
    public List<IpAddress> manageIpBan(List<BanAction> request) throws LinkException {
        if(!CollectionUtils.isEmpty(request)) {
            return request.stream().map(action -> {
                IpAddress entity;
                Optional<IpAddress> ipAddressEntity = repository.findById(action.getIpAddress());

                if (!ipAddressEntity.isPresent()) {
                    entity = new IpAddress(action.getIpAddress(), action.getIsBanned(), null);
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
