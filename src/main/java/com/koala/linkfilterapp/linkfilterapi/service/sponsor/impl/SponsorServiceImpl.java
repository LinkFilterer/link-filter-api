package com.koala.linkfilterapp.linkfilterapi.service.sponsor.impl;

import com.koala.linkfilterapp.linkfilterapi.api.common.enums.SponsorSortType;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.LinkException;
import com.koala.linkfilterapp.linkfilterapi.api.sponsor.dto.request.SponsorRequestBean;
import com.koala.linkfilterapp.linkfilterapi.api.sponsor.dto.response.SponsorBean;
import com.koala.linkfilterapp.linkfilterapi.api.sponsor.entity.Sponsor;
import com.koala.linkfilterapp.linkfilterapi.repository.SponsorRepository;
import com.koala.linkfilterapp.linkfilterapi.service.sponsor.SponsorService;
import com.koala.linkfilterapp.linkfilterapi.service.sponsor.converter.SponsorConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.logging.Logger;

import static java.util.Objects.nonNull;


@Service
public class SponsorServiceImpl implements SponsorService {
    Logger log = Logger.getLogger("SponsorServiceImpl");

    @Autowired
    SponsorRepository repository;

    @Autowired
    SponsorConverter converter;

    public List<Sponsor> getAllSponsors() {
        return repository.findAll();
    }

    public Page<Sponsor> searchSponsors(int pageNum, int size, SponsorSortType sortType, String projectName) {
        Pageable page = PageRequest.of(pageNum, size).withSort(Sort.by(sortType.toString()).ascending());
        Page<Sponsor> found = repository.findByIdContains(projectName, page);
        return found;
    }

    public Sponsor createSponsor(SponsorRequestBean bean) throws LinkException {
        Optional<Sponsor> foundSponsor = repository.findById(bean.getId());

        if (foundSponsor.isPresent()) {
            throw new LinkException(HttpStatus.CONFLICT, "Sponsor already exists, use update api", null, null, null);
        }

        repository.save(converter.convert(bean));
        return bean;
    }

    public Sponsor updateSponsor(SponsorRequestBean bean) throws LinkException {
        Optional<Sponsor> foundSponsor = repository.findById(bean.getId());

        if (!foundSponsor.isPresent()) {
            throw new LinkException(HttpStatus.NOT_FOUND, "Sponsor not found!", null, null, null);
        }

        repository.delete(foundSponsor.get());
        repository.save(converter.convert(bean));
        return bean;
    }

    public void deleteSponsor(String projectName) throws LinkException {
        Optional<Sponsor> foundSponsor = repository.findById(projectName);

        if (!foundSponsor.isPresent()) {
            throw new LinkException(HttpStatus.NOT_FOUND, "Sponsor not found!", null, null, null);
        }

        repository.delete(foundSponsor.get());
    }

    public SponsorBean getSponsorInfo(String projectName, String ipAddress) throws LinkException {
        Optional<Sponsor> retrievedSponsor = repository.findById(projectName);
        if (retrievedSponsor.isPresent()) {
            return converter.convert(retrievedSponsor.get());
        } else {
            LinkException exception = new LinkException(HttpStatus.BAD_REQUEST, "Unable to find sponsor: " + projectName, null, ipAddress, null);
            throw exception;
        }
    }

    public SponsorBean getSponsorInfo() throws LinkException {
        Random rand = new Random();

        List<Sponsor> retrievedSponsors = repository.findAll();
        if (CollectionUtils.isEmpty(retrievedSponsors)) {
            log.warning("No Sponsors found");
            return null;
        } else {
            NavigableMap<Double, Sponsor> sponserMap = new TreeMap<>();
            Double weightTotal = 0.0;
            for (Sponsor sponsor : retrievedSponsors) {
                weightTotal += sponsor.getWeight();
                sponserMap.put(weightTotal, sponsor);
            }

            Double randomDouble = rand.nextDouble() * weightTotal;
            Sponsor randomSponsor = null;
            try {
                return nonNull(sponserMap.higherEntry(randomDouble)) ? converter.convert(sponserMap.higherEntry(randomDouble).getValue()) : null;
            } catch (Exception e) {
                log.warning(e.toString());
            }
            return null;
        }
    }
}
