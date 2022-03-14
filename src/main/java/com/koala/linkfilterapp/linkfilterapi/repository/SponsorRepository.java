package com.koala.linkfilterapp.linkfilterapi.repository;

import com.koala.linkfilterapp.linkfilterapi.api.sponsor.entity.Sponsor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, String> {
    Page<Sponsor> findByIdContains(String projectName, Pageable pageAble);
}
