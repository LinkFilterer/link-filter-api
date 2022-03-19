package com.koala.linkfilterapp.linkfilterapi.repository;

import com.koala.linkfilterapp.linkfilterapi.api.requesthistory.entity.RequestHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RequestHistoryRepository extends JpaRepository<RequestHistory, String>, JpaSpecificationExecutor<RequestHistory> {
    List<RequestHistory> findByRequestTime(Date requestTime);

    List<RequestHistory> findByRequestTimeBetween(Date time1, Date time2);

    List<RequestHistory> findByRequestTimeBefore(Date beforeDate);

    List<RequestHistory> findByRequestTimeAfter(Date afterDate);

    long countByRequestTime(Date requestTime);

    long countByLinkRequestedUrlAndRequestTimeBetween(String url, Date time1, Date time2);
    long countByRequestTimeBetween(Date time1, Date time2);

    long countByRequestTimeBefore(Date beforeDate);

    long countByRequestTimeAfterAndIpAddress(Date afterDate, String ip);
}
