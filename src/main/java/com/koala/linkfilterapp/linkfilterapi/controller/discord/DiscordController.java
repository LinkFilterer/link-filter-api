package com.koala.linkfilterapp.linkfilterapi.controller.discord;

import com.koala.linkfilterapp.linkfilterapi.api.common.dto.response.RestResponse;
import com.koala.linkfilterapp.linkfilterapi.api.common.enums.AddressType;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.CommonException;
import com.koala.linkfilterapp.linkfilterapi.api.discord.dto.DiscordSettingsRequest;
import com.koala.linkfilterapp.linkfilterapi.api.discord.entity.DiscordSettings;
import com.koala.linkfilterapp.linkfilterapi.api.link.dto.response.LinkBean;
import com.koala.linkfilterapp.linkfilterapi.api.report.enums.ReportType;
import com.koala.linkfilterapp.linkfilterapi.api.requesthistory.entity.RequestHistory;
import com.koala.linkfilterapp.linkfilterapi.api.requesthistory.enums.RequestType;
import com.koala.linkfilterapp.linkfilterapi.api.sponsor.dto.response.SponsorBean;
import com.koala.linkfilterapp.linkfilterapi.service.discord.DiscordService;
import com.koala.linkfilterapp.linkfilterapi.service.ipaddress.impl.IpAddressServiceImpl;
import com.koala.linkfilterapp.linkfilterapi.service.ipaddress.impl.RequestHistoryServiceImpl;
import com.koala.linkfilterapp.linkfilterapi.service.link.impl.LinkServiceImpl;
import com.koala.linkfilterapp.linkfilterapi.service.sponsor.impl.SponsorServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.koala.linkfilterapp.linkfilterapi.controller.common.ControllerConstants.BROWSER_EXTENSION_ORIGIN;
import static com.koala.linkfilterapp.linkfilterapi.controller.common.ControllerConstants.UI_SERVER_ORIGIN;

@CrossOrigin(origins = {UI_SERVER_ORIGIN, BROWSER_EXTENSION_ORIGIN})
@RestController
@Slf4j
@RequestMapping("/discord")
public class DiscordController {

    @Autowired
    LinkServiceImpl linkService;

    @Autowired
    SponsorServiceImpl sponsorService;

    @Autowired
    IpAddressServiceImpl ipAddressService;

    @Autowired
    RequestHistoryServiceImpl requestHistoryService;

    @Autowired
    DiscordService discordService;

    // Will simply check database for a result and return it (INVALID/VALID/UNKNOWN) is public
    @PostMapping(value = "/checkLink")
    public ResponseEntity<RestResponse<LinkBean>> checkLink(@RequestParam String url,
                                                            @RequestParam AddressType source,
                                                            @RequestParam String userId,
                                                            HttpServletRequest request) throws CommonException {
        String ipAddress = request.getRemoteAddr();
        if (ipAddressService.checkIfBanned(ipAddress, source, userId)) {
            return null;
        }
        RequestHistory requestHistory = requestHistoryService.saveRequestHistory(url, ipAddress, userId, RequestType.CHECK, source);

        LinkBean response = linkService.checkLink(url, request.getRemoteAddr(), requestHistory);
        SponsorBean sponsor = sponsorService.getSponsorInfo();
        response.setSponsor(sponsor);
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Link Checked", response, null), HttpStatus.OK);
    }

    @PostMapping(value = "/reportLink")
    public ResponseEntity<RestResponse<LinkBean>> reportLink(@RequestParam String url,
                                                             @RequestParam AddressType source,
                                                             @RequestParam String userId,
                                                             @RequestParam ReportType reportType,
                                                             HttpServletRequest request) throws CommonException {
        String ipAddress = request.getRemoteAddr();
        if (ipAddressService.checkIfBanned(ipAddress, source, userId)) {
            return null;
        }
        RequestHistory requestHistory = requestHistoryService.saveRequestHistory(url, ipAddress, userId, RequestType.CHECK, source);

        LinkBean response = linkService.reportLink(url, request.getRemoteAddr(), userId, reportType, requestHistory);
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Link Reported", response, null), HttpStatus.OK);
    }

    @PostMapping(value = "/checkLinks")
    public ResponseEntity<RestResponse<List<LinkBean>>> checkLinks(@RequestBody List<String> urls,
                                                                   @RequestParam String userId,
                                                                   HttpServletRequest request) throws CommonException {
        String ipAddress = request.getRemoteAddr();
        if (ipAddressService.checkIfBanned(ipAddress, AddressType.WEB, Strings.EMPTY)) {
            return null;
        }
        List<String> distinctUrls = urls.stream().distinct().collect(Collectors.toList());
        List<RequestHistory> requestHistories = requestHistoryService.saveRequestHistories(distinctUrls, ipAddress, userId, RequestType.CHECK, AddressType.DISCORD);
        requestHistoryService.ipCheck(request.getRemoteAddr(), userId);

        List<LinkBean> response = linkService.checkLinks(distinctUrls, request.getRemoteAddr(), requestHistories);

        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Successfully checked links", response, null), HttpStatus.OK);
    }

    @GetMapping(value = "getSettings/{serverId}")
    public ResponseEntity<RestResponse<DiscordSettings>> getSettings(@PathVariable("serverId") String serverId,
                                                                     HttpServletRequest request) throws CommonException {
        String ipAddress = request.getRemoteAddr();
        // Todo spam check?
        if (ipAddressService.checkIfBanned(ipAddress, AddressType.WEB, Strings.EMPTY)) {
            return null;
        }
        DiscordSettings settings = discordService.getSettings(serverId);
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Successfully received settings", settings, null), HttpStatus.OK);
    }

    @PostMapping(value = "postSettings")
    public ResponseEntity<RestResponse<DiscordSettings>> postSettings(@RequestBody DiscordSettingsRequest settingsRequest,
                                                                      HttpServletRequest request) throws CommonException {
        String ipAddress = request.getRemoteAddr();
        // Todo spam check?
        if (ipAddressService.checkIfBanned(ipAddress, AddressType.WEB, Strings.EMPTY)) {
            return null;
        }
        DiscordSettings settings = discordService.updateSettings(settingsRequest);
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Successfully updated settings", settings, null), HttpStatus.OK);
    }
}
