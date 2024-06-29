package BSEP.KT2.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import BSEP.KT2.model.Advertisement;
import BSEP.KT2.model.AdvertisementRequest;
import BSEP.KT2.service.IAdvertisementRequestService;
import BSEP.KT2.service.IAdvertisementService;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/advertisement")
public class AdvertisementController extends BaseController {

    @Autowired
    private IAdvertisementRequestService advertisementRequestService;
    @Autowired
    private IAdvertisementService advertisementService;

    private static final Logger logger = LoggerFactory.getLogger(AdvertisementController.class);


    @PostMapping("/createRequest")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<Object> createAdvertisementRequest(@RequestBody AdvertisementRequest advertisementRequest, HttpServletRequest httpRequest) {
        String clientIp = httpRequest.getRemoteAddr();
        String clientHost = httpRequest.getRemoteHost();
        int clientPort = httpRequest.getRemotePort();
        logger.info("Entering createAdvertisementRequest() - Advertisement request from IP: {}, HOST: {}, PORT: {}", clientIp, clientHost, clientPort);

        try {
            advertisementRequestService.saveAdvertisementRequest(advertisementRequest);
            logger.info("Exiting createAdvertisementRequest() - Successfully requested advertisement from IP: {}, HOST: {}, PORT: {}", clientIp, clientHost, clientPort);
            return ResponseEntity.status(HttpStatus.CREATED).body(generateSuccessResponse(201, "Successfully requested advertisement."));
        } catch (Exception e) {
            logger.error("createAdvertisementRequest() - Failed to save advertisement request from IP: {}, HOST: {}, PORT: {}", clientIp, clientHost, clientPort, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save advertisement request: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping("/unprocessedRequests")
    public List<AdvertisementRequest> getUnprocessedRequests() {
        return advertisementRequestService.getUnprocessedRequests();
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Object> createAdvertisement(@RequestBody Advertisement advertisement, HttpServletRequest httpRequest) {
        String clientIp = httpRequest.getRemoteAddr();
        String clientHost = httpRequest.getRemoteHost();
        int clientPort = httpRequest.getRemotePort();
        logger.info("Entering createAdvertisement() - Create advertisement request from IP: {}, HOST: {}, PORT: {}", clientIp, clientHost, clientPort);

        try {
            if (advertisementService.saveAdvertisementRequest(advertisement) != null) {
                advertisementRequestService.changeRequestStatus(advertisement.getRequestId());
            }
            logger.info("Exiting createAdvertisement() - Successfully created advertisement from IP: {}, HOST: {}, PORT: {}", clientIp, clientHost, clientPort);
            return ResponseEntity.status(HttpStatus.CREATED).body(generateSuccessResponse(201, "Successfully created advertisement."));
        } catch (Exception e) {
            logger.error("createAdvertisement() - Failed to save advertisement request from IP: {}, HOST: {}, PORT: {}", clientIp, clientHost, clientPort, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save advertisement request: " + e.getMessage());
        }
    }

    @GetMapping("/getAdvertisements")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public List<Advertisement> getAdvertisements() {
        return advertisementService.loadAdvertisements();
    }

    @PostMapping("/visit/{username}/{adId}")
    public ResponseEntity<Object> visit(@PathVariable String username, @PathVariable String adId) {
        try {
            Advertisement advertisement = advertisementService.getAdvertisementById(Integer.parseInt(adId));
            return ResponseEntity.status(HttpStatus.OK).body(advertisement);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(generateErrorResponse(404, ex.getMessage()));
        }
    }

}

