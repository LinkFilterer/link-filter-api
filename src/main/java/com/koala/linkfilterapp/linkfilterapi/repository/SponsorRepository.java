package com.koala.linkfilterapp.linkfilterapi.repository;

import com.koala.linkfilterapp.linkfilterapi.api.sponsor.entity.Sponsor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, String>, JpaSpecificationExecutor<Sponsor> {
    Page<Sponsor> findByIdContains(String projectName, Pageable pageAble);

    List<Sponsor> findByEndDateAfter(Date currentDate);

    List<Sponsor> findByEndDateBefore(Date currentDate);
}
