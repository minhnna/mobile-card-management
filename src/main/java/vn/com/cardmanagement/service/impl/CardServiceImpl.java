package vn.com.cardmanagement.service.impl;

import com.google.common.base.Strings;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;

import vn.com.cardmanagement.domain.*;
import vn.com.cardmanagement.repository.UserRepository;
import vn.com.cardmanagement.service.CardService;
import vn.com.cardmanagement.repository.CardRepository;
import vn.com.cardmanagement.service.dto.CardDTO;
import vn.com.cardmanagement.service.mapper.CardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.cardmanagement.web.rest.params.CardQueryCondition;


import java.io.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Card.
 */
@Service
public class CardServiceImpl implements CardService {

    private final Logger log = LoggerFactory.getLogger(CardServiceImpl.class);

    private final CardRepository cardRepository;

    private final UserRepository userRepository;

    private final CardMapper cardMapper;
    private static String sessionID;
    public CardServiceImpl(CardRepository cardRepository, UserRepository userRepository, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.cardMapper = cardMapper;
    }

    /**
     * Save a card.
     *
     * @param cardDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CardDTO save(CardDTO cardDTO) {
        log.debug("Request to save Card : {}", cardDTO);
        Card card = cardMapper.toEntity(cardDTO);
        card = cardRepository.save(card);
        return cardMapper.toDto(card);
    }

    /**
     * Get all the cards.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<CardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cards");
        return cardRepository.findAll(pageable)
            .map(cardMapper::toDto);
    }


    /**
     * Get one card by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<CardDTO> findOne(String id) {
        log.debug("Request to get Card : {}", id);
        return cardRepository.findById(id)
            .map(cardMapper::toDto);
    }

    /**
     * Delete the card by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Card : {}", id);
        cardRepository.deleteById(id);
    }

    @Override
    public List<CardDTO> findNewCards(CardQueryCondition cardQueryCondition) {

        return cardMapper.toDto(cardRepository.findNewCard(cardQueryCondition));
    }


    @Override
    public List<CardDTO> findNewLessThanCards(CardQueryCondition cardQueryCondition) {
        return cardMapper.toDto(cardRepository.findNewLessThanCard(cardQueryCondition));
    }

    @Override
    public Page<CardDTO> findOldCards(Pageable pageable, CardQueryCondition cardQueryCondition) {
        return cardRepository.findOldCard(pageable, cardQueryCondition).map(cardMapper::toDto);
    }

    @Override
    public List<Card> findOldCardsForReport(CardQueryCondition cardQueryCondition) {
        return cardRepository.findOldCard(cardQueryCondition);
    }

    @Override
    public File exportReportForUser(CardQueryCondition cardQueryCondition) {
        Date fromTime = new Date(Long.parseLong(cardQueryCondition.getFromDate()));
        String fromDate = new SimpleDateFormat("yyyy-MM-dd").format(fromTime);
        Date toTime = new Date(Long.parseLong(cardQueryCondition.getToDate()));
        String toDate = new SimpleDateFormat("yyyy-MM-dd").format(toTime);
        String excelFileName = fromDate.concat(toDate).concat(".xlsx");
        File archiver = new File(excelFileName);
        List<Card> cardList = this.findOldCardsForReport(cardQueryCondition);
        log.info("cardList.size==" + cardList.size());
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Hóa đơn");
        sheet.setColumnWidth(0, 18000); //Set column width, you'll probably want to tweak the second int

        // create style for header cells
        CellStyle headerStyle = workbook.createCellStyle();
        CellStyle style = workbook.createCellStyle();
        headerStyle.setWrapText(true);
        style.setWrapText(true);
        Font headerFont = workbook.createFont();
        Font font = workbook.createFont();
        headerFont.setFontName("Arial");
        font.setFontName("Arial");
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerFont.setBold(true);
        headerFont.setColor(HSSFColor.WHITE.index);
//        font.setColor(HSSFColor.BLACK.index);
        headerStyle.setFont(font);
        style.setFont(font);
        DataFormat format = workbook.createDataFormat();
        headerStyle.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
//        style.setFillForegroundColor(HSSFColor.WHITE.index);
        style.setDataFormat(format.getFormat("@"));
        style.setDataFormat(format.getFormat("@"));

        sheet.setDefaultColumnStyle(0, style);
        sheet.setDefaultColumnStyle(1, style);
        sheet.setDefaultColumnStyle(2, style);
        sheet.setDefaultColumnStyle(3, style);
        sheet.setDefaultColumnStyle(4, style);
        sheet.setDefaultColumnStyle(5, style);
        sheet.setDefaultColumnStyle(6, style);
        sheet.setDefaultColumnStyle(7, style);

// create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Stt");
        header.getCell(0).setCellStyle(headerStyle);
        header.createCell(1).setCellValue("Nhà mạng");
        header.getCell(1).setCellStyle(headerStyle);
        header.createCell(2).setCellValue("Số serial");
        header.getCell(2).setCellStyle(headerStyle);
        header.createCell(3).setCellValue("Mã thẻ cào");
        header.getCell(3).setCellStyle(headerStyle);
        header.createCell(4).setCellValue("Mẹnh giá trước khi cào");
        header.getCell(4).setCellStyle(headerStyle);
        header.createCell(5).setCellValue("Mẹnh giá sau khi cào");
        header.getCell(5).setCellStyle(headerStyle);
        header.createCell(6).setCellValue("Trạng thái");
        header.getCell(6).setCellStyle(headerStyle);
        header.createCell(7).setCellValue("Thời gian nạp");
        header.getCell(7).setCellStyle(headerStyle);

        CellRangeAddress region = CellRangeAddress.valueOf("A1:H1");
        for (int i = region.getFirstRow(); i < region.getLastRow(); i++) {
            Row row = sheet.getRow(i);
            for (int j = region.getFirstColumn(); j < region.getLastColumn(); j++) {
                Cell cell = row.getCell(j);
                cell.setCellStyle(headerStyle);
            }
        }
        int rowCount = 1;
        int total = 0;
        for (Card card : cardList) {
            Row userRow = sheet.createRow(rowCount++);
            userRow.createCell(0).setCellValue(rowCount - 1);
            userRow.createCell(1).setCellValue(card.getMobileService());
            userRow.createCell(2).setCellValue(card.getSerialNumber());
            userRow.createCell(3).setCellValue(card.getCode());
            userRow.createCell(4).setCellValue(card.getPrice());
            if (card.getStatus().equalsIgnoreCase("ERROR")) {
                userRow.createCell(5).setCellValue(0);
            } else {
                userRow.createCell(5).setCellValue(card.getRealPrice() == null ? card.getPrice() : card.getRealPrice());
                if (card.getRealPrice() != null && card.getRealPrice() != 0) {
                    total += card.getRealPrice();
                } else {
                    total += card.getPrice();
                }
            }
            userRow.createCell(6).setCellValue(card.getStatus());
            userRow.createCell(7).setCellValue(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date.from(card.getExportedDate())));
        }
        Row userRow = sheet.createRow(rowCount++);
        userRow.createCell(0).setCellValue("");
        userRow.createCell(1).setCellValue("Tổng");
        userRow.createCell(2).setCellValue("");
        userRow.createCell(3).setCellValue("");
        userRow.createCell(4).setCellValue("");
        userRow.createCell(5).setCellValue(total);
        userRow.createCell(6).setCellValue("");
        userRow.createCell(7).setCellValue("");
        userRow.createCell(8).setCellValue("");

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);
        sheet.autoSizeColumn(7);
        try (OutputStream fileOut = new FileOutputStream(archiver)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            log.info("Error export report file");
            e.printStackTrace();
        }
        return archiver;
    }

    @Override
    public Page<CardDTO> findExpiredCards(Pageable pageable, String username) {
        return cardRepository.findExpiredCard(pageable, username).map(cardMapper::toDto);
    }

    @Override
    public CardDTO updateStatus(CardDTO cardDTO) {
        log.debug("Request to update Card : {}", cardDTO.getId());
        Card orgCard = cardRepository.findOne(cardDTO.getId());
        if (cardDTO.getPrice() != 0 && !cardDTO.getStatus().equalsIgnoreCase("ERROR")) {
            orgCard.setRealPrice(cardDTO.getPrice());
        }
        orgCard.setStatus(cardDTO.getStatus());
        orgCard.setUpdatedDate(Instant.now());
        Card result = cardRepository.save(orgCard);
        BeanUtils.copyProperties(result, cardDTO);
        return cardDTO;
    }

    @Override
    public List<User> getAllManagedPendingUsers() {
        List<String> logins = new ArrayList<>();
        List<User> userList = new ArrayList<>();
        logins.addAll(cardRepository.getAllManagedPendingUsers());
        for (String login : logins) {
            userRepository.findOneByLogin(login).isPresent();
            userList.add(userRepository.findOneByLogin(login).orElse(new User()));
        }
        return userList;
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
    public boolean loginByMobifone(){
        String elementId ="";
        FindElementResponse findElementResponse = null;
        while (Strings.isNullOrEmpty(elementId)) {
            findElementResponse = cardRepository.findElement(sessionID, "vn.mobifone.mobifonenext:id/user_id", 5);
            elementId = findElementResponse.getValue().getELEMENT();
        }
        cardRepository.sendKeys(sessionID, elementId, "0908495378");
        findElementResponse = cardRepository.findElement(sessionID, "vn.mobifone.mobifonenext:id/login_button", 5);
        elementId= findElementResponse.getValue().getELEMENT();
        cardRepository.click(sessionID, elementId);
        elementId = "";
        findElementResponse = cardRepository.findElement(sessionID, "vn.mobifone.mobifonenext:id/tv_error_desc", 5);
        if(findElementResponse != null || !Strings.isNullOrEmpty(findElementResponse.getValue().getELEMENT())) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean inputPasscode(String passcode){
        doInputPasscode(passcode);
        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }
        FindElementResponse findElementResponse = cardRepository.findElement(sessionID, "vn.mobifone.mobifonenext:id/button_top_up", 5);
        String elementId= findElementResponse.getValue().getELEMENT();
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
        String elementId= findElementResponse.getValue().getELEMENT();
        return cardRepository.getContentElement(sessionID,elementId);
    }

    private void clickScanNumber() {
        FindElementResponse findElementResponse = cardRepository.findElement(sessionID, "vn.mobifone.mobifonenext:id/button_scan_number", 5);
        String elementId= findElementResponse.getValue().getELEMENT();
        cardRepository.click(sessionID, elementId);
    }
    private void clickTopUp(String elementId) {
//        FindElementResponse findElementResponse = cardRepository.findElement(sessionID, "vn.mobifone.mobifonenext:id/button_top_up", 5);
//        String elementId= findElementResponse.getValue().getELEMENT();
        cardRepository.click(sessionID, elementId);
    }
    private void clickCharge() {
        FindElementResponse findElementResponse = cardRepository.findElement(sessionID, "vn.mobifone.mobifonenext:id/button_topup", 5);
        String elementId= findElementResponse.getValue().getELEMENT();
        cardRepository.click(sessionID, elementId);
    }
    private void clickOK() {
        FindElementResponse findElementResponse = cardRepository.findElement(sessionID, "vn.mobifone.mobifonenext:id/button_cancel", 5);
        String elementId= findElementResponse.getValue().getELEMENT();
        cardRepository.click(sessionID, elementId);
    }

    private void resendPasscode() {
        FindElementResponse findElementResponse = cardRepository.findElement(sessionID, "vn.mobifone.mobifonenext:id/button_resend_sms", 5);
        String elementId= findElementResponse.getValue().getELEMENT();
        cardRepository.click(sessionID, elementId);
    }

    private void doInputPasscode(String passcode) {
        FindElementResponse findElementResponse = cardRepository.findElement(sessionID, "vn.mobifone.mobifonenext:id/input1", 5);
        String elementId= findElementResponse.getValue().getELEMENT();
        cardRepository.sendKeys(sessionID, elementId, passcode);

    }
    private void inputSerial(String passcode) {
        FindElementResponse findElementResponse = cardRepository.findElement(sessionID, "vn.mobifone.mobifonenext:id/edit_pin_code", 5);
        String elementId= findElementResponse.getValue().getELEMENT();
        cardRepository.sendKeys(sessionID, elementId, passcode);
    }
}
