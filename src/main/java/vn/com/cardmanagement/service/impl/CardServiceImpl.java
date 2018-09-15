package vn.com.cardmanagement.service.impl;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import vn.com.cardmanagement.service.CardService;
import vn.com.cardmanagement.domain.Card;
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

    private final CardMapper cardMapper;

    public CardServiceImpl(CardRepository cardRepository, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
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
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFillForegroundColor(HSSFColor.BLUE.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        font.setBold(true);
        font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);

        // create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Stt");
        header.getCell(0).setCellStyle(style);
        header.createCell(1).setCellValue("Nhà mạng");
        header.getCell(1).setCellStyle(style);
        header.createCell(2).setCellValue("Số serial");
        header.getCell(2).setCellStyle(style);
        header.createCell(3).setCellValue("Mã thẻ cào");
        header.getCell(3).setCellStyle(style);
        header.createCell(4).setCellValue("Mẹnh giá trước khi cào");
        header.getCell(4).setCellStyle(style);
        header.createCell(5).setCellValue("Mẹnh giá sau khi cào");
        header.getCell(5).setCellStyle(style);
        header.createCell(6).setCellValue("Trạng thái");
        header.getCell(6).setCellStyle(style);
        header.createCell(7).setCellValue("Thời gian nạp");
        header.getCell(7).setCellStyle(style);
        DataFormat format = workbook.createDataFormat();
        CellStyle columnStyle = workbook.createCellStyle();
        style.setDataFormat(format.getFormat("@"));
        sheet.setDefaultColumnStyle(0, style);
        sheet.setDefaultColumnStyle(1, style);
        sheet.setDefaultColumnStyle(2, style);
        sheet.setDefaultColumnStyle(3, style);
        sheet.setDefaultColumnStyle(4, style);
        sheet.setDefaultColumnStyle(5, style);
        sheet.setDefaultColumnStyle(6, style);
        sheet.setDefaultColumnStyle(7, style);
        int rowCount = 1;
        for (Card card : cardList) {
            Row userRow = sheet.createRow(rowCount++);
            userRow.createCell(0).setCellValue(rowCount - 1);
            userRow.createCell(1).setCellValue(card.getMobileService());
            userRow.createCell(2).setCellValue(card.getSerialNumber());
            userRow.createCell(3).setCellValue(card.getCode());
            userRow.createCell(4).setCellValue(card.getPrice());
            userRow.createCell(5).setCellValue(card.getRealPrice() == null ? card.getPrice() : card.getRealPrice());
            userRow.createCell(6).setCellValue(card.getStatus());
            userRow.createCell(7).setCellValue(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date.from(card.getExportedDate())));
        }
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
    public Page<CardDTO> findExpiredCards(Pageable pageable) {
        return cardRepository.findExpiredCard(pageable).map(cardMapper::toDto);
    }

    @Override
    public CardDTO updateStatus(CardDTO cardDTO) {
        log.debug("Request to update Card : {}", cardDTO.getId());
        Card orgCard = cardRepository.findOne(cardDTO.getId());
        if (cardDTO.getRealPrice() != 0) {
            orgCard.setRealPrice(cardDTO.getRealPrice());
        }
        orgCard.setStatus(cardDTO.getStatus());
        orgCard.setUpdatedDate(Instant.now());
        Card result = cardRepository.save(orgCard);
        BeanUtils.copyProperties(result, cardDTO);
        return cardDTO;
    }

    @Override
    public Page<String> getAllManagedPendingUsers(Pageable pageable) {
        return cardRepository.getAllManagedPendingUsers(pageable);
    }
}
