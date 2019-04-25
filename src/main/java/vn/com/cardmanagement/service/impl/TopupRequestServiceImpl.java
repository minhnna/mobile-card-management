package vn.com.cardmanagement.service.impl;

import com.google.common.base.Strings;
import org.springframework.scheduling.annotation.Scheduled;
import vn.com.cardmanagement.domain.*;
import vn.com.cardmanagement.repository.CardRepository;
import vn.com.cardmanagement.service.TopupRequestService;
import vn.com.cardmanagement.repository.TopupRequestRepository;
import vn.com.cardmanagement.service.dto.TopupRequestDTO;
import vn.com.cardmanagement.service.mapper.TopupRequestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Service Implementation for managing TopupRequest.
 */
@Service
public class TopupRequestServiceImpl implements TopupRequestService {

    private final Logger log = LoggerFactory.getLogger(TopupRequestServiceImpl.class);

    private final TopupRequestRepository topupRequestRepository;
    private final CardRepository cardRepository;

    private final TopupRequestMapper topupRequestMapper;

    private static String sessionID;

    private Lock topupResourceLocker = new ReentrantLock();
    BlockingQueue<Runnable> topupTaskQueue = new ArrayBlockingQueue<Runnable>(50);
    ThreadPoolExecutor executorTopupService = new ThreadPoolExecutor(2, 8, 5, TimeUnit.SECONDS, topupTaskQueue);

    public TopupRequestServiceImpl(TopupRequestRepository topupRequestRepository, CardRepository cardRepository, TopupRequestMapper topupRequestMapper) {
        this.topupRequestRepository = topupRequestRepository;
        this.cardRepository = cardRepository;
        this.topupRequestMapper = topupRequestMapper;
    }

    /**
     * Save a topupRequest.
     *
     * @param topupRequestDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TopupRequestDTO save(TopupRequestDTO topupRequestDTO) {
        log.debug("Request to save TopupRequest : {}", topupRequestDTO);
        TopupRequest topupRequest = topupRequestMapper.toEntity(topupRequestDTO);
        topupRequest = topupRequestRepository.save(topupRequest);
        return topupRequestMapper.toDto(topupRequest);
    }

    /**
     * Get all the topupRequests.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<TopupRequestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TopupRequests");
        return topupRequestRepository.findAll(pageable)
            .map(topupRequestMapper::toDto);
    }


    /**
     * Get one topupRequest by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<TopupRequestDTO> findOne(String id) {
        log.debug("Request to get TopupRequest : {}", id);
        return topupRequestRepository.findById(id)
            .map(topupRequestMapper::toDto);
    }

    /**
     * Delete the topupRequest by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete TopupRequest : {}", id);
        topupRequestRepository.deleteById(id);
    }

    @Override
    public void createDevice() {
        CreateDeviceRequest createDeviceRequest = new CreateDeviceRequest();
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setDeviceName("127.0.0.1:62115");
        createDeviceRequest.setDesiredCapabilities(desiredCapabilities);
        CreateDeviceResponse result = cardRepository.createDevice(createDeviceRequest);
        sessionID = result.getSessionId();
    }

    @Override
    public boolean loginByMobifone() {
        String elementId = "";
        FindElementResponse findElementResponse = null;
        while (Strings.isNullOrEmpty(elementId)) {
            findElementResponse = cardRepository.findElement(sessionID, "vn.mobifone.mobifonenext:id/user_id", 5);
            elementId = findElementResponse.getValue().getELEMENT();
        }
        cardRepository.sendKeys(sessionID, elementId, "0908495378");
        findElementResponse = cardRepository.findElement(sessionID, "vn.mobifone.mobifonenext:id/login_button", 5);
        elementId = findElementResponse.getValue().getELEMENT();
        cardRepository.click(sessionID, elementId);
        elementId = "";
        findElementResponse = cardRepository.findElement(sessionID, "vn.mobifone.mobifonenext:id/tv_error_desc", 5);
        if (findElementResponse != null || !Strings.isNullOrEmpty(findElementResponse.getValue().getELEMENT())) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean inputPasscode(String passcode) {
        doInputPasscode(passcode);
        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }
        FindElementResponse findElementResponse = cardRepository.findElement(sessionID, "vn.mobifone.mobifonenext:id/button_top_up", 5);
        String elementId = findElementResponse.getValue().getELEMENT();
        if (findElementResponse == null) {
            resendPasscode();
            return false;
        }
        clickTopUp(elementId);
        clickScanNumber();
        inputSerial("261609762852");
        clickCharge();
        clickOK();
        String result = getResultContent();
        log.info("result==", result);
        return true;
    }

    private String getResultContent() {
        FindElementResponse findElementResponse = cardRepository.findElement(sessionID, "vn.mobifone.mobifonenext:id/txt_description", 5);
        String elementId = findElementResponse.getValue().getELEMENT();
        return cardRepository.getContentElement(sessionID, elementId);
    }

    private void clickScanNumber() {
        FindElementResponse findElementResponse = cardRepository.findElement(sessionID, "vn.mobifone.mobifonenext:id/button_scan_number", 5);
        String elementId = findElementResponse.getValue().getELEMENT();
        cardRepository.click(sessionID, elementId);
    }

    private void clickTopUp(String elementId) {
//        FindElementResponse findElementResponse = cardRepository.findElement(sessionID, "vn.mobifone.mobifonenext:id/button_top_up", 5);
//        String elementId= findElementResponse.getValue().getELEMENT();
        cardRepository.click(sessionID, elementId);
    }

    private void clickCharge() {
        FindElementResponse findElementResponse = cardRepository.findElement(sessionID, "vn.mobifone.mobifonenext:id/button_topup", 5);
        String elementId = findElementResponse.getValue().getELEMENT();
        cardRepository.click(sessionID, elementId);
    }

    private void clickOK() {
        FindElementResponse findElementResponse = cardRepository.findElement(sessionID, "vn.mobifone.mobifonenext:id/button_cancel", 5);
        String elementId = findElementResponse.getValue().getELEMENT();
        cardRepository.click(sessionID, elementId);
    }

    private void resendPasscode() {
        FindElementResponse findElementResponse = cardRepository.findElement(sessionID, "vn.mobifone.mobifonenext:id/button_resend_sms", 5);
        String elementId = findElementResponse.getValue().getELEMENT();
        cardRepository.click(sessionID, elementId);
    }

    private void doInputPasscode(String passcode) {
        FindElementResponse findElementResponse = cardRepository.findElement(sessionID, "vn.mobifone.mobifonenext:id/input1", 5);
        String elementId = findElementResponse.getValue().getELEMENT();
        cardRepository.sendKeys(sessionID, elementId, passcode);

    }

    private void inputSerial(String passcode) {
        FindElementResponse findElementResponse = cardRepository.findElement(sessionID, "vn.mobifone.mobifonenext:id/edit_pin_code", 5);
        String elementId = findElementResponse.getValue().getELEMENT();
        cardRepository.sendKeys(sessionID, elementId, passcode);
    }

    @Scheduled(fixedDelay = 10000)
    @Override
    public void runAutoTopup() {
        try {
            this.topupResourceLocker.lock();//ensure this method is not called in parallel
            log.info("Start run Topup");
            // get list image infos
                TopupRequest topupRequest = topupRequestRepository.findByStatusAAndOrderByCreatedDateAsc("");
                if (topupRequest == null) {
                    log.error("uncheckedVideo is NULL");
                }
                enqueueTopup(topupRequest);
        } catch (RejectedExecutionException ie) {
            log.warn("Queue is full");
        } catch (Exception e) {
            log.error("Run topup get error {}", e.getMessage());
        } finally {
            log.info("Finished run topup");
            this.topupResourceLocker.unlock();
        }
    }

    private void enqueueTopup(TopupRequest topupRequest) {
        log.info("enqueueVideoForCheck:" + topupRequest.getId());
        executorTopupService.execute(new Runnable() {
            @Override
            public void run() {
                    log.info("multi thread topup:" + topupRequest.getId());
                    topup(topupRequest);
            }
        });
    }

    private void topup(TopupRequest topupRequest) {

    }
}
