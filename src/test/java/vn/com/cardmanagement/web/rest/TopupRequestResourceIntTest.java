package vn.com.cardmanagement.web.rest;

import vn.com.cardmanagement.MobileCardManagementApp;

import vn.com.cardmanagement.domain.TopupRequest;
import vn.com.cardmanagement.repository.TopupRequestRepository;
import vn.com.cardmanagement.service.TopupRequestService;
import vn.com.cardmanagement.service.dto.TopupRequestDTO;
import vn.com.cardmanagement.service.mapper.TopupRequestMapper;
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
 * Test class for the TopupRequestResource REST controller.
 *
 * @see TopupRequestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MobileCardManagementApp.class)
public class TopupRequestResourceIntTest {

    private static final String DEFAULT_MOBILE_SERVICE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_SERVICE = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_TOPUP_VALUE = 1;
    private static final Integer UPDATED_TOPUP_VALUE = 2;

    private static final Integer DEFAULT_REAL_VALUE = 1;
    private static final Integer UPDATED_REAL_VALUE = 2;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    @Autowired
    private TopupRequestRepository topupRequestRepository;

    @Autowired
    private TopupRequestMapper topupRequestMapper;

    @Autowired
    private TopupRequestService topupRequestService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restTopupRequestMockMvc;

    private TopupRequest topupRequest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TopupRequestResource topupRequestResource = new TopupRequestResource(topupRequestService);
        this.restTopupRequestMockMvc = MockMvcBuilders.standaloneSetup(topupRequestResource)
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
    public static TopupRequest createEntity() {
        TopupRequest topupRequest = new TopupRequest()
            .mobileService(DEFAULT_MOBILE_SERVICE)
            .mobileNumber(DEFAULT_MOBILE_NUMBER)
            .topupValue(DEFAULT_TOPUP_VALUE)
            .realValue(DEFAULT_REAL_VALUE)
            .createdDate(DEFAULT_CREATED_DATE)
            .updatedDate(DEFAULT_UPDATED_DATE)
            .userId(DEFAULT_USER_ID)
            .status(DEFAULT_STATUS)
            .deleted(DEFAULT_DELETED);
        return topupRequest;
    }

    @Before
    public void initTest() {
        topupRequestRepository.deleteAll();
        topupRequest = createEntity();
    }

    @Test
    public void createTopupRequest() throws Exception {
        int databaseSizeBeforeCreate = topupRequestRepository.findAll().size();

        // Create the TopupRequest
        TopupRequestDTO topupRequestDTO = topupRequestMapper.toDto(topupRequest);
        restTopupRequestMockMvc.perform(post("/api/topup-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(topupRequestDTO)))
            .andExpect(status().isCreated());

        // Validate the TopupRequest in the database
        List<TopupRequest> topupRequestList = topupRequestRepository.findAll();
        assertThat(topupRequestList).hasSize(databaseSizeBeforeCreate + 1);
        TopupRequest testTopupRequest = topupRequestList.get(topupRequestList.size() - 1);
        assertThat(testTopupRequest.getMobileService()).isEqualTo(DEFAULT_MOBILE_SERVICE);
        assertThat(testTopupRequest.getMobileNumber()).isEqualTo(DEFAULT_MOBILE_NUMBER);
        assertThat(testTopupRequest.getTopupValue()).isEqualTo(DEFAULT_TOPUP_VALUE);
        assertThat(testTopupRequest.getRealValue()).isEqualTo(DEFAULT_REAL_VALUE);
        assertThat(testTopupRequest.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testTopupRequest.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
        assertThat(testTopupRequest.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testTopupRequest.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTopupRequest.isDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    public void createTopupRequestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = topupRequestRepository.findAll().size();

        // Create the TopupRequest with an existing ID
        topupRequest.setId("existing_id");
        TopupRequestDTO topupRequestDTO = topupRequestMapper.toDto(topupRequest);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTopupRequestMockMvc.perform(post("/api/topup-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(topupRequestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TopupRequest in the database
        List<TopupRequest> topupRequestList = topupRequestRepository.findAll();
        assertThat(topupRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllTopupRequests() throws Exception {
        // Initialize the database
        topupRequestRepository.save(topupRequest);

        // Get all the topupRequestList
        restTopupRequestMockMvc.perform(get("/api/topup-requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(topupRequest.getId())))
            .andExpect(jsonPath("$.[*].mobileService").value(hasItem(DEFAULT_MOBILE_SERVICE.toString())))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].topupValue").value(hasItem(DEFAULT_TOPUP_VALUE)))
            .andExpect(jsonPath("$.[*].realValue").value(hasItem(DEFAULT_REAL_VALUE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }
    
    @Test
    public void getTopupRequest() throws Exception {
        // Initialize the database
        topupRequestRepository.save(topupRequest);

        // Get the topupRequest
        restTopupRequestMockMvc.perform(get("/api/topup-requests/{id}", topupRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(topupRequest.getId()))
            .andExpect(jsonPath("$.mobileService").value(DEFAULT_MOBILE_SERVICE.toString()))
            .andExpect(jsonPath("$.mobileNumber").value(DEFAULT_MOBILE_NUMBER.toString()))
            .andExpect(jsonPath("$.topupValue").value(DEFAULT_TOPUP_VALUE))
            .andExpect(jsonPath("$.realValue").value(DEFAULT_REAL_VALUE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    public void getNonExistingTopupRequest() throws Exception {
        // Get the topupRequest
        restTopupRequestMockMvc.perform(get("/api/topup-requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateTopupRequest() throws Exception {
        // Initialize the database
        topupRequestRepository.save(topupRequest);

        int databaseSizeBeforeUpdate = topupRequestRepository.findAll().size();

        // Update the topupRequest
        TopupRequest updatedTopupRequest = topupRequestRepository.findById(topupRequest.getId()).get();
        updatedTopupRequest
            .mobileService(UPDATED_MOBILE_SERVICE)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .topupValue(UPDATED_TOPUP_VALUE)
            .realValue(UPDATED_REAL_VALUE)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE)
            .userId(UPDATED_USER_ID)
            .status(UPDATED_STATUS)
            .deleted(UPDATED_DELETED);
        TopupRequestDTO topupRequestDTO = topupRequestMapper.toDto(updatedTopupRequest);

        restTopupRequestMockMvc.perform(put("/api/topup-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(topupRequestDTO)))
            .andExpect(status().isOk());

        // Validate the TopupRequest in the database
        List<TopupRequest> topupRequestList = topupRequestRepository.findAll();
        assertThat(topupRequestList).hasSize(databaseSizeBeforeUpdate);
        TopupRequest testTopupRequest = topupRequestList.get(topupRequestList.size() - 1);
        assertThat(testTopupRequest.getMobileService()).isEqualTo(UPDATED_MOBILE_SERVICE);
        assertThat(testTopupRequest.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
        assertThat(testTopupRequest.getTopupValue()).isEqualTo(UPDATED_TOPUP_VALUE);
        assertThat(testTopupRequest.getRealValue()).isEqualTo(UPDATED_REAL_VALUE);
        assertThat(testTopupRequest.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTopupRequest.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
        assertThat(testTopupRequest.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testTopupRequest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTopupRequest.isDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    public void updateNonExistingTopupRequest() throws Exception {
        int databaseSizeBeforeUpdate = topupRequestRepository.findAll().size();

        // Create the TopupRequest
        TopupRequestDTO topupRequestDTO = topupRequestMapper.toDto(topupRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTopupRequestMockMvc.perform(put("/api/topup-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(topupRequestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TopupRequest in the database
        List<TopupRequest> topupRequestList = topupRequestRepository.findAll();
        assertThat(topupRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteTopupRequest() throws Exception {
        // Initialize the database
        topupRequestRepository.save(topupRequest);

        int databaseSizeBeforeDelete = topupRequestRepository.findAll().size();

        // Delete the topupRequest
        restTopupRequestMockMvc.perform(delete("/api/topup-requests/{id}", topupRequest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TopupRequest> topupRequestList = topupRequestRepository.findAll();
        assertThat(topupRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TopupRequest.class);
        TopupRequest topupRequest1 = new TopupRequest();
        topupRequest1.setId("id1");
        TopupRequest topupRequest2 = new TopupRequest();
        topupRequest2.setId(topupRequest1.getId());
        assertThat(topupRequest1).isEqualTo(topupRequest2);
        topupRequest2.setId("id2");
        assertThat(topupRequest1).isNotEqualTo(topupRequest2);
        topupRequest1.setId(null);
        assertThat(topupRequest1).isNotEqualTo(topupRequest2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TopupRequestDTO.class);
        TopupRequestDTO topupRequestDTO1 = new TopupRequestDTO();
        topupRequestDTO1.setId("id1");
        TopupRequestDTO topupRequestDTO2 = new TopupRequestDTO();
        assertThat(topupRequestDTO1).isNotEqualTo(topupRequestDTO2);
        topupRequestDTO2.setId(topupRequestDTO1.getId());
        assertThat(topupRequestDTO1).isEqualTo(topupRequestDTO2);
        topupRequestDTO2.setId("id2");
        assertThat(topupRequestDTO1).isNotEqualTo(topupRequestDTO2);
        topupRequestDTO1.setId(null);
        assertThat(topupRequestDTO1).isNotEqualTo(topupRequestDTO2);
    }
}
