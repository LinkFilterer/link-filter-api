package com.koala.linkfilterapp.linkfilterapi.service.sponsor.impl;

import com.koala.linkfilterapp.linkfilterapi.api.common.exception.CommonException;
import com.koala.linkfilterapp.linkfilterapi.api.sponsor.dto.request.SponsorRequestBean;
import com.koala.linkfilterapp.linkfilterapi.api.sponsor.dto.request.SponsorSearchBean;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
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

    public Page<Sponsor> searchSponsors(SponsorSearchBean searchBean) {
        Specification<Sponsor> querySpec = createQuery(searchBean);
        Pageable pageRequest = getPageableRequest(searchBean);
        return repository.findAll(querySpec, pageRequest);
    }

    private Pageable getPageableRequest(SponsorSearchBean searchBean) {
        Sort sortOrder = Sort.by(searchBean.getSortType().toString());
        Optional<Sort.Direction> sortDirection = Sort.Direction.fromOptionalString(searchBean.getSortDir());
        if (sortDirection.isPresent()) {
            sortOrder = Sort.by(sortDirection.get(), searchBean.getSortType().toString());
        }
        return PageRequest.of(searchBean.getPageNo(), searchBean.getPageSize(), sortOrder);
    }

    private Specification<Sponsor> createQuery(SponsorSearchBean searchBean) {
        return (root, query, builder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.hasText(searchBean.getId())) { predicates.add(builder.equal(root.<String>get("id"), searchBean.getId())); }
            if(StringUtils.hasText(searchBean.getDescription())) { predicates.add(builder.equal(root.<String>get("description"), searchBean.getDescription())); }
            if(StringUtils.hasText(searchBean.getBanner())) { predicates.add(builder.equal(root.<String>get("banner"), searchBean.getBanner())); }
            if(StringUtils.hasText(searchBean.getUrl())) { predicates.add(builder.equal(root.<String>get("url"), searchBean.getUrl())); }
            if(StringUtils.hasText(searchBean.getCreationDate())) { predicates.add(builder.equal(root.<String>get("creationDate"), searchBean.getCreationDate())); }
            if(StringUtils.hasText(searchBean.getEndDate())) { predicates.add(builder.equal(root.<String>get("endDate"), searchBean.getEndDate())); }
            if(nonNull(searchBean.getIsExpired())) { predicates.add(builder.equal(root.<String>get("isExpired"), searchBean.getIsExpired())); }
            if(nonNull(searchBean.getWeight())) { predicates.add(builder.equal(root.<String>get("weight"), searchBean.getWeight())); }
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public Sponsor createSponsor(SponsorRequestBean bean) throws CommonException {
        Optional<Sponsor> foundSponsor = repository.findById(bean.getId());

        if (foundSponsor.isPresent()) {
            throw new CommonException(HttpStatus.CONFLICT, "Sponsor already exists, use update api", null, null, null);
        }

        repository.save(converter.convert(bean));
        return bean;
    }

    public Sponsor updateSponsor(SponsorRequestBean bean) throws CommonException {
        Optional<Sponsor> foundSponsor = repository.findById(bean.getId());

        if (!foundSponsor.isPresent()) {
            throw new CommonException(HttpStatus.NOT_FOUND, "Sponsor not found!", null, null, null);
        }

        repository.delete(foundSponsor.get());
        repository.save(converter.convert(bean));
        return bean;
    }

    public void deleteSponsor(String projectName) throws CommonException {
        Optional<Sponsor> foundSponsor = repository.findById(projectName);

        if (!foundSponsor.isPresent()) {
            throw new CommonException(HttpStatus.NOT_FOUND, "Sponsor not found!", null, null, null);
        }

        repository.delete(foundSponsor.get());
    }

    public SponsorBean getSponsorInfo(String projectName, String ipAddress) throws CommonException {
        Optional<Sponsor> retrievedSponsor = repository.findById(projectName);
        if (retrievedSponsor.isPresent()) {
            return converter.convert(retrievedSponsor.get());
        } else {
            CommonException exception = new CommonException(HttpStatus.BAD_REQUEST, "Unable to find sponsor: " + projectName, null, ipAddress, null);
            throw exception;
        }
    }

    public SponsorBean getSponsorInfo() throws CommonException {
        Random rand = new Random();

        List<Sponsor> retrievedSponsors = repository.findByEndDateAfter(new Date(System.currentTimeMillis()));
        log.info("Retrieved Sponsors " + retrievedSponsors);
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
            try {
                return nonNull(sponserMap.higherEntry(randomDouble)) ? converter.convert(sponserMap.higherEntry(randomDouble).getValue()) : null;
            } catch (Exception e) {
                log.warning(e.toString());
            }
            return null;
        }
    }
}
