package com.koala.linkfilterapp.linkfilterapi.service.common.impl;

import com.koala.linkfilterapp.linkfilterapi.api.common.dto.response.RequestHistoryData;
import com.koala.linkfilterapp.linkfilterapi.api.common.dto.response.RequestHistoryStatResponse;
import com.koala.linkfilterapp.linkfilterapi.api.common.dto.response.RequestHistoryStatistic;
import com.koala.linkfilterapp.linkfilterapi.api.common.entity.RequestHistory;
import com.koala.linkfilterapp.linkfilterapi.api.common.enums.RequestType;
import com.koala.linkfilterapp.linkfilterapi.api.common.enums.TimeInterval;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.LinkException;
import com.koala.linkfilterapp.linkfilterapi.api.link.entity.Link;
import com.koala.linkfilterapp.linkfilterapi.repository.RequestHistoryRepository;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

import static com.koala.linkfilterapp.linkfilterapi.service.common.CommonApiConstants.CONNECTION_THRESHOLD;
import static com.koala.linkfilterapp.linkfilterapi.service.common.CommonApiConstants.REQUEST_DATE_FORMAT;
import static java.util.Objects.nonNull;

@Service
public class RequestHistoryServiceImpl {
    Logger log = Logger.getLogger("RequestHistoryServiceImpl");

    @Autowired
    RequestHistoryRepository requestHistoryRepository;

    public void ipCheck(String ipAddress) throws LinkException {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime.getTime());
        calendar.add(Calendar.MINUTE, -1);
        Timestamp timeInterval = new Timestamp(calendar.getTime().getTime());
        long timesConnected = requestHistoryRepository.countByRequestTimeAfterAndIpAddress(timeInterval,ipAddress);
        if (timesConnected > CONNECTION_THRESHOLD) {
            LinkException exception = new LinkException(HttpStatus.TOO_MANY_REQUESTS, "Connection threshold reached please try again in a minute", null, null, null);
            throw exception;
        }
    }

    public RequestHistory saveRequestHistory(String url, String ipAddress, RequestType requestType) {
        RequestHistory requestHistory = new RequestHistory();
        requestHistory.setRequestType(requestType);
        requestHistory.setIpAddress(ipAddress);
        requestHistory.setRequestedUrl(url);
        requestHistory.setRequestTime(new Timestamp(System.currentTimeMillis()));
        requestHistoryRepository.save(requestHistory);
        return requestHistory;
    }

    public void processRequestHistory(RequestHistory requestHistory, Link link) {
        requestHistory.setUrl(link.getUrl());
        requestHistoryRepository.save(requestHistory);
    }

    public Page<RequestHistoryData> getAllRequestHistory(Integer pageNbr, Integer pageSize, String sortType) {
        DozerBeanMapper mapper = new DozerBeanMapper();
        Pageable page = PageRequest.of(pageNbr, pageSize).withSort(Sort.Direction.ASC, sortType);
        return requestHistoryRepository.findAll(page).map(entity -> mapper.map(entity, RequestHistoryData.class));
    }

    public RequestHistoryStatResponse getRequestHistoryStatistics(String url, TimeInterval timeInterval, String startingDate,
                                                                  String endDate) throws LinkException {
        log.info("Get request statistics service");
        RequestHistoryStatResponse response = new RequestHistoryStatResponse();
        response.setInterval(timeInterval);
        List<RequestHistoryStatistic> responseData = new ArrayList<>();
        Date start = convertStringToDate(startingDate);
        Date end = convertStringToDate(endDate);
        if (start.before(end)) {
            long interval = getIntervalValue(timeInterval);
            long startTime = start.getTime();
            for (long i = startTime; i < end.getTime(); i+=interval) {
                RequestHistoryStatistic stat = new RequestHistoryStatistic();
                Date before = new Date();
                Date after = new Date();
                before.setTime(i);
                after.setTime(i+interval);
                long requestCount = 0;
                if (!StringUtils.isEmpty(url)) {
                    requestCount = requestHistoryRepository.countByLinkRequestedUrlAndRequestTimeBetween(url, before, after);
                } else {
                    requestCount = requestHistoryRepository.countByRequestTimeBetween(before,after);
                }
                stat.setCount(requestCount);
                stat.setTime(convertDateToString(before, timeInterval));
                responseData.add(stat);
            }
        } else {
            LinkException exception = new LinkException();
            exception.setDescription(String.format("INVALID START/END DATES: %s, %s", startingDate, endDate));
            throw exception;
        }
        response.setRequestHistoryStatistics(responseData);
        return response;
    }

    private Date convertStringToDate(String date) {
        Date parsedDate = null;
        if (StringUtils.hasText(date)) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(REQUEST_DATE_FORMAT);
            LocalDateTime localDateTime = LocalDateTime.parse(date, dateTimeFormatter);
            parsedDate = Date.from(ZonedDateTime.of(localDateTime, ZoneId.systemDefault()).toInstant());
        }
        return parsedDate;
    }

    private String convertDateToString(Date date, TimeInterval interval) {
        String dateFormat = getDateFormat(interval);
        String formattedDate = null;
        if(nonNull(date)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat).withZone(ZoneId.systemDefault());
            formattedDate = formatter.format(date.toInstant());
        }
        return formattedDate;
    }

    private Long getIntervalValue(TimeInterval interval) {
        Map<TimeInterval, Long> timeIntMap = new HashMap<>();
        timeIntMap.put(TimeInterval.SECOND, (long) 1000);
        timeIntMap.put(TimeInterval.MINUTE, (long) 1000 * 60);
        timeIntMap.put(TimeInterval.HOUR, (long) 1000 * 60 * 60);
        timeIntMap.put(TimeInterval.DAY, (long) 1000 * 60 * 60 * 24);
        timeIntMap.put(TimeInterval.MONTH, (long) 1000 * 60 * 60 * 24 * 30);
        timeIntMap.put(TimeInterval.YEAR, (long) 1000 * 60 * 60 * 24 * 30 * 12);
        return timeIntMap.getOrDefault(interval, 1000 * 60 * 60 * 24L);
    }

    private String getDateFormat(TimeInterval interval) {
        Map<TimeInterval, String> timeIntMap = new HashMap<>();
        timeIntMap.put(TimeInterval.SECOND, "mm:ss");
        timeIntMap.put(TimeInterval.MINUTE, "HH:mm");
        timeIntMap.put(TimeInterval.HOUR, "HH:mm:ss");
        timeIntMap.put(TimeInterval.DAY, "MM/dd");
        timeIntMap.put(TimeInterval.MONTH, "MM");
        timeIntMap.put(TimeInterval.YEAR, "yyyy");
        return timeIntMap.getOrDefault(interval, null);
    }
}
