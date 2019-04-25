package vn.com.cardmanagement.web.rest;

import vn.com.cardmanagement.MobileCardManagementApp;

import vn.com.cardmanagement.domain.Card;
import vn.com.cardmanagement.repository.CardRepository;
import vn.com.cardmanagement.service.CardService;
import vn.com.cardmanagement.service.dto.CardDTO;
import vn.com.cardmanagement.service.mapper.CardMapper;
import vn.com.cardmanagement.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static vn.com.cardmanagement.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CardResource REST controller.
 *
 * @see CardResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MobileCardManagementApp.class)
public class CardResourceIntTest {

    private static final String DEFAULT_MOBILE_SERVICE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_SERVICE = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRICE = 1;
    private static final Integer UPDATED_PRICE = 2;

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPORTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPORTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Integer DEFAULT_REAL_PRICE = 1;
    private static final Integer UPDATED_REAL_PRICE = 2;

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private CardService cardService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restCardMockMvc;

    private Card card;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CardResource cardResource = new CardResource(cardService);
        this.restCardMockMvc = MockMvcBuilders.standaloneSetup(cardResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Card createEntity() {
        Card card = new Card()
            .mobileService(DEFAULT_MOBILE_SERVICE)
            .price(DEFAULT_PRICE)
            .serialNumber(DEFAULT_SERIAL_NUMBER)
            .code(DEFAULT_CODE)
            .createdDate(DEFAULT_CREATED_DATE)
            .exportedDate(DEFAULT_EXPORTED_DATE)
            .updatedDate(DEFAULT_UPDATED_DATE)
            .userId(DEFAULT_USER_ID)
            .status(DEFAULT_STATUS)
            .realPrice(DEFAULT_REAL_PRICE)
            .deleted(DEFAULT_DELETED);
        return card;
    }

    @Before
    public void initTest() {
        cardRepository.deleteAll();
        card = createEntity();
    }

    @Test
    public void createCard() throws Exception {
        int databaseSizeBeforeCreate = cardRepository.findAll().size();

        // Create the Card
        CardDTO cardDTO = cardMapper.toDto(card);
        restCardMockMvc.perform(post("/api/cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardDTO)))
            .andExpect(status().isCreated());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeCreate + 1);
        Card testCard = cardList.get(cardList.size() - 1);
        assertThat(testCard.getMobileService()).isEqualTo(DEFAULT_MOBILE_SERVICE);
        assertThat(testCard.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCard.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testCard.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCard.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCard.getExportedDate()).isEqualTo(DEFAULT_EXPORTED_DATE);
        assertThat(testCard.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
        assertThat(testCard.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testCard.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCard.getRealPrice()).isEqualTo(DEFAULT_REAL_PRICE);
        assertThat(testCard.isDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    public void createCardWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cardRepository.findAll().size();

        // Create the Card with an existing ID
        card.setId("existing_id");
        CardDTO cardDTO = cardMapper.toDto(card);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardMockMvc.perform(post("/api/cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllCards() throws Exception {
        // Initialize the database
        cardRepository.save(card);

        // Get all the cardList
        restCardMockMvc.perform(get("/api/cards?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(card.getId())))
            .andExpect(jsonPath("$.[*].mobileService").value(hasItem(DEFAULT_MOBILE_SERVICE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].exportedDate").value(hasItem(DEFAULT_EXPORTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].realPrice").value(hasItem(DEFAULT_REAL_PRICE)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }
    
    @Test
    public void getCard() throws Exception {
        // Initialize the database
        cardRepository.save(card);

        // Get the card
        restCardMockMvc.perform(get("/api/cards/{id}", card.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(card.getId()))
            .andExpect(jsonPath("$.mobileService").value(DEFAULT_MOBILE_SERVICE.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.exportedDate").value(DEFAULT_EXPORTED_DATE.toString()))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.realPrice").value(DEFAULT_REAL_PRICE))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    public void getNonExistingCard() throws Exception {
        // Get the card
        restCardMockMvc.perform(get("/api/cards/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateCard() throws Exception {
        // Initialize the database
        cardRepository.save(card);

        int databaseSizeBeforeUpdate = cardRepository.findAll().size();

        // Update the card
        Card updatedCard = cardRepository.findById(card.getId()).get();
        updatedCard
            .mobileService(UPDATED_MOBILE_SERVICE)
            .price(UPDATED_PRICE)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .code(UPDATED_CODE)
            .createdDate(UPDATED_CREATED_DATE)
            .exportedDate(UPDATED_EXPORTED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE)
            .userId(UPDATED_USER_ID)
            .status(UPDATED_STATUS)
            .realPrice(UPDATED_REAL_PRICE)
            .deleted(UPDATED_DELETED);
        CardDTO cardDTO = cardMapper.toDto(updatedCard);

        restCardMockMvc.perform(put("/api/cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardDTO)))
            .andExpect(status().isOk());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
        Card testCard = cardList.get(cardList.size() - 1);
        assertThat(testCard.getMobileService()).isEqualTo(UPDATED_MOBILE_SERVICE);
        assertThat(testCard.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCard.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testCard.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCard.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCard.getExportedDate()).isEqualTo(UPDATED_EXPORTED_DATE);
        assertThat(testCard.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
        assertThat(testCard.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testCard.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCard.getRealPrice()).isEqualTo(UPDATED_REAL_PRICE);
        assertThat(testCard.isDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    public void updateNonExistingCard() throws Exception {
        int databaseSizeBeforeUpdate = cardRepository.findAll().size();

        // Create the Card
        CardDTO cardDTO = cardMapper.toDto(card);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardMockMvc.perform(put("/api/cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteCard() throws Exception {
        // Initialize the database
        cardRepository.save(card);

        int databaseSizeBeforeDelete = cardRepository.findAll().size();

        // Delete the card
        restCardMockMvc.perform(delete("/api/cards/{id}", card.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Card.class);
        Card card1 = new Card();
        card1.setId("id1");
        Card card2 = new Card();
        card2.setId(card1.getId());
        assertThat(card1).isEqualTo(card2);
        card2.setId("id2");
        assertThat(card1).isNotEqualTo(card2);
        card1.setId(null);
        assertThat(card1).isNotEqualTo(card2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CardDTO.class);
        CardDTO cardDTO1 = new CardDTO();
        cardDTO1.setId("id1");
        CardDTO cardDTO2 = new CardDTO();
        assertThat(cardDTO1).isNotEqualTo(cardDTO2);
        cardDTO2.setId(cardDTO1.getId());
        assertThat(cardDTO1).isEqualTo(cardDTO2);
        cardDTO2.setId("id2");
        assertThat(cardDTO1).isNotEqualTo(cardDTO2);
        cardDTO1.setId(null);
        assertThat(cardDTO1).isNotEqualTo(cardDTO2);
    }
}
