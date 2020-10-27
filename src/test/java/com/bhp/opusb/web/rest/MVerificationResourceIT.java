package com.bhp.opusb.web.rest;

import com.bhp.opusb.OpusWebApp;
import com.bhp.opusb.domain.MVerification;
import com.bhp.opusb.domain.ADOrganization;
import com.bhp.opusb.domain.CCurrency;
import com.bhp.opusb.repository.MVerificationRepository;
import com.bhp.opusb.service.MVerificationService;
import com.bhp.opusb.service.dto.MVerificationDTO;
import com.bhp.opusb.service.mapper.MVerificationMapper;
import com.bhp.opusb.service.dto.MVerificationCriteria;
import com.bhp.opusb.service.MVerificationQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link MVerificationResource} REST controller.
 */
@SpringBootTest(classes = OpusWebApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class MVerificationResourceIT {

    private static final String DEFAULT_VERIFICATION_NO = "AAAAAAAAAA";
    private static final String UPDATED_VERIFICATION_NO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_VERIFICATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VERIFICATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_VERIFICATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_INVOICE_NO = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_INVOICE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INVOICE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_INVOICE_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_TAX_INVOICE = "AAAAAAAAAA";
    private static final String UPDATED_TAX_INVOICE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_TAX_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TAX_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TAX_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_TOTAL_LINES = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_LINES = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_LINES = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TAX_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAX_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TAX_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_GRAND_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_GRAND_TOTAL = new BigDecimal(2);
    private static final BigDecimal SMALLER_GRAND_TOTAL = new BigDecimal(1 - 1);

    private static final String DEFAULT_VERIFICATION_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_VERIFICATION_STATUS = "BBBBBBBBBB";

    private static final UUID DEFAULT_UID = UUID.randomUUID();
    private static final UUID UPDATED_UID = UUID.randomUUID();

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private MVerificationRepository mVerificationRepository;

    @Autowired
    private MVerificationMapper mVerificationMapper;

    @Autowired
    private MVerificationService mVerificationService;

    @Autowired
    private MVerificationQueryService mVerificationQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMVerificationMockMvc;

    private MVerification mVerification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MVerification createEntity(EntityManager em) {
        MVerification mVerification = new MVerification()
            .verificationNo(DEFAULT_VERIFICATION_NO)
            .verificationDate(DEFAULT_VERIFICATION_DATE)
            .description(DEFAULT_DESCRIPTION)
            .invoiceNo(DEFAULT_INVOICE_NO)
            .invoiceDate(DEFAULT_INVOICE_DATE)
            .taxInvoice(DEFAULT_TAX_INVOICE)
            .taxDate(DEFAULT_TAX_DATE)
            .totalLines(DEFAULT_TOTAL_LINES)
            .taxAmount(DEFAULT_TAX_AMOUNT)
            .grandTotal(DEFAULT_GRAND_TOTAL)
            .verificationStatus(DEFAULT_VERIFICATION_STATUS)
            .uid(DEFAULT_UID)
            .active(DEFAULT_ACTIVE);
        // Add required entity
        ADOrganization aDOrganization;
        if (TestUtil.findAll(em, ADOrganization.class).isEmpty()) {
            aDOrganization = ADOrganizationResourceIT.createEntity(em);
            em.persist(aDOrganization);
            em.flush();
        } else {
            aDOrganization = TestUtil.findAll(em, ADOrganization.class).get(0);
        }
        mVerification.setAdOrganization(aDOrganization);
        // Add required entity
        CCurrency cCurrency;
        if (TestUtil.findAll(em, CCurrency.class).isEmpty()) {
            cCurrency = CCurrencyResourceIT.createEntity(em);
            em.persist(cCurrency);
            em.flush();
        } else {
            cCurrency = TestUtil.findAll(em, CCurrency.class).get(0);
        }
        mVerification.setCurrency(cCurrency);
        return mVerification;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MVerification createUpdatedEntity(EntityManager em) {
        MVerification mVerification = new MVerification()
            .verificationNo(UPDATED_VERIFICATION_NO)
            .verificationDate(UPDATED_VERIFICATION_DATE)
            .description(UPDATED_DESCRIPTION)
            .invoiceNo(UPDATED_INVOICE_NO)
            .invoiceDate(UPDATED_INVOICE_DATE)
            .taxInvoice(UPDATED_TAX_INVOICE)
            .taxDate(UPDATED_TAX_DATE)
            .totalLines(UPDATED_TOTAL_LINES)
            .taxAmount(UPDATED_TAX_AMOUNT)
            .grandTotal(UPDATED_GRAND_TOTAL)
            .verificationStatus(UPDATED_VERIFICATION_STATUS)
            .uid(UPDATED_UID)
            .active(UPDATED_ACTIVE);
        // Add required entity
        ADOrganization aDOrganization;
        if (TestUtil.findAll(em, ADOrganization.class).isEmpty()) {
            aDOrganization = ADOrganizationResourceIT.createUpdatedEntity(em);
            em.persist(aDOrganization);
            em.flush();
        } else {
            aDOrganization = TestUtil.findAll(em, ADOrganization.class).get(0);
        }
        mVerification.setAdOrganization(aDOrganization);
        // Add required entity
        CCurrency cCurrency;
        if (TestUtil.findAll(em, CCurrency.class).isEmpty()) {
            cCurrency = CCurrencyResourceIT.createUpdatedEntity(em);
            em.persist(cCurrency);
            em.flush();
        } else {
            cCurrency = TestUtil.findAll(em, CCurrency.class).get(0);
        }
        mVerification.setCurrency(cCurrency);
        return mVerification;
    }

    @BeforeEach
    public void initTest() {
        mVerification = createEntity(em);
    }

    @Test
    @Transactional
    public void createMVerification() throws Exception {
        int databaseSizeBeforeCreate = mVerificationRepository.findAll().size();

        // Create the MVerification
        MVerificationDTO mVerificationDTO = mVerificationMapper.toDto(mVerification);
        restMVerificationMockMvc.perform(post("/api/m-verifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mVerificationDTO)))
            .andExpect(status().isCreated());

        // Validate the MVerification in the database
        List<MVerification> mVerificationList = mVerificationRepository.findAll();
        assertThat(mVerificationList).hasSize(databaseSizeBeforeCreate + 1);
        MVerification testMVerification = mVerificationList.get(mVerificationList.size() - 1);
        assertThat(testMVerification.getVerificationNo()).isEqualTo(DEFAULT_VERIFICATION_NO);
        assertThat(testMVerification.getVerificationDate()).isEqualTo(DEFAULT_VERIFICATION_DATE);
        assertThat(testMVerification.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMVerification.getInvoiceNo()).isEqualTo(DEFAULT_INVOICE_NO);
        assertThat(testMVerification.getInvoiceDate()).isEqualTo(DEFAULT_INVOICE_DATE);
        assertThat(testMVerification.getTaxInvoice()).isEqualTo(DEFAULT_TAX_INVOICE);
        assertThat(testMVerification.getTaxDate()).isEqualTo(DEFAULT_TAX_DATE);
        assertThat(testMVerification.getTotalLines()).isEqualTo(DEFAULT_TOTAL_LINES);
        assertThat(testMVerification.getTaxAmount()).isEqualTo(DEFAULT_TAX_AMOUNT);
        assertThat(testMVerification.getGrandTotal()).isEqualTo(DEFAULT_GRAND_TOTAL);
        assertThat(testMVerification.getVerificationStatus()).isEqualTo(DEFAULT_VERIFICATION_STATUS);
        assertThat(testMVerification.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testMVerification.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createMVerificationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mVerificationRepository.findAll().size();

        // Create the MVerification with an existing ID
        mVerification.setId(1L);
        MVerificationDTO mVerificationDTO = mVerificationMapper.toDto(mVerification);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMVerificationMockMvc.perform(post("/api/m-verifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mVerificationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MVerification in the database
        List<MVerification> mVerificationList = mVerificationRepository.findAll();
        assertThat(mVerificationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkVerificationNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = mVerificationRepository.findAll().size();
        // set the field null
        mVerification.setVerificationNo(null);

        // Create the MVerification, which fails.
        MVerificationDTO mVerificationDTO = mVerificationMapper.toDto(mVerification);

        restMVerificationMockMvc.perform(post("/api/m-verifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mVerificationDTO)))
            .andExpect(status().isBadRequest());

        List<MVerification> mVerificationList = mVerificationRepository.findAll();
        assertThat(mVerificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVerificationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = mVerificationRepository.findAll().size();
        // set the field null
        mVerification.setVerificationDate(null);

        // Create the MVerification, which fails.
        MVerificationDTO mVerificationDTO = mVerificationMapper.toDto(mVerification);

        restMVerificationMockMvc.perform(post("/api/m-verifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mVerificationDTO)))
            .andExpect(status().isBadRequest());

        List<MVerification> mVerificationList = mVerificationRepository.findAll();
        assertThat(mVerificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInvoiceNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = mVerificationRepository.findAll().size();
        // set the field null
        mVerification.setInvoiceNo(null);

        // Create the MVerification, which fails.
        MVerificationDTO mVerificationDTO = mVerificationMapper.toDto(mVerification);

        restMVerificationMockMvc.perform(post("/api/m-verifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mVerificationDTO)))
            .andExpect(status().isBadRequest());

        List<MVerification> mVerificationList = mVerificationRepository.findAll();
        assertThat(mVerificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInvoiceDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = mVerificationRepository.findAll().size();
        // set the field null
        mVerification.setInvoiceDate(null);

        // Create the MVerification, which fails.
        MVerificationDTO mVerificationDTO = mVerificationMapper.toDto(mVerification);

        restMVerificationMockMvc.perform(post("/api/m-verifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mVerificationDTO)))
            .andExpect(status().isBadRequest());

        List<MVerification> mVerificationList = mVerificationRepository.findAll();
        assertThat(mVerificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalLinesIsRequired() throws Exception {
        int databaseSizeBeforeTest = mVerificationRepository.findAll().size();
        // set the field null
        mVerification.setTotalLines(null);

        // Create the MVerification, which fails.
        MVerificationDTO mVerificationDTO = mVerificationMapper.toDto(mVerification);

        restMVerificationMockMvc.perform(post("/api/m-verifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mVerificationDTO)))
            .andExpect(status().isBadRequest());

        List<MVerification> mVerificationList = mVerificationRepository.findAll();
        assertThat(mVerificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTaxAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = mVerificationRepository.findAll().size();
        // set the field null
        mVerification.setTaxAmount(null);

        // Create the MVerification, which fails.
        MVerificationDTO mVerificationDTO = mVerificationMapper.toDto(mVerification);

        restMVerificationMockMvc.perform(post("/api/m-verifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mVerificationDTO)))
            .andExpect(status().isBadRequest());

        List<MVerification> mVerificationList = mVerificationRepository.findAll();
        assertThat(mVerificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGrandTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = mVerificationRepository.findAll().size();
        // set the field null
        mVerification.setGrandTotal(null);

        // Create the MVerification, which fails.
        MVerificationDTO mVerificationDTO = mVerificationMapper.toDto(mVerification);

        restMVerificationMockMvc.perform(post("/api/m-verifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mVerificationDTO)))
            .andExpect(status().isBadRequest());

        List<MVerification> mVerificationList = mVerificationRepository.findAll();
        assertThat(mVerificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVerificationStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = mVerificationRepository.findAll().size();
        // set the field null
        mVerification.setVerificationStatus(null);

        // Create the MVerification, which fails.
        MVerificationDTO mVerificationDTO = mVerificationMapper.toDto(mVerification);

        restMVerificationMockMvc.perform(post("/api/m-verifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mVerificationDTO)))
            .andExpect(status().isBadRequest());

        List<MVerification> mVerificationList = mVerificationRepository.findAll();
        assertThat(mVerificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMVerifications() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList
        restMVerificationMockMvc.perform(get("/api/m-verifications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mVerification.getId().intValue())))
            .andExpect(jsonPath("$.[*].verificationNo").value(hasItem(DEFAULT_VERIFICATION_NO)))
            .andExpect(jsonPath("$.[*].verificationDate").value(hasItem(DEFAULT_VERIFICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].invoiceNo").value(hasItem(DEFAULT_INVOICE_NO)))
            .andExpect(jsonPath("$.[*].invoiceDate").value(hasItem(DEFAULT_INVOICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].taxInvoice").value(hasItem(DEFAULT_TAX_INVOICE)))
            .andExpect(jsonPath("$.[*].taxDate").value(hasItem(DEFAULT_TAX_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalLines").value(hasItem(DEFAULT_TOTAL_LINES.intValue())))
            .andExpect(jsonPath("$.[*].taxAmount").value(hasItem(DEFAULT_TAX_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].grandTotal").value(hasItem(DEFAULT_GRAND_TOTAL.intValue())))
            .andExpect(jsonPath("$.[*].verificationStatus").value(hasItem(DEFAULT_VERIFICATION_STATUS)))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getMVerification() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get the mVerification
        restMVerificationMockMvc.perform(get("/api/m-verifications/{id}", mVerification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mVerification.getId().intValue()))
            .andExpect(jsonPath("$.verificationNo").value(DEFAULT_VERIFICATION_NO))
            .andExpect(jsonPath("$.verificationDate").value(DEFAULT_VERIFICATION_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.invoiceNo").value(DEFAULT_INVOICE_NO))
            .andExpect(jsonPath("$.invoiceDate").value(DEFAULT_INVOICE_DATE.toString()))
            .andExpect(jsonPath("$.taxInvoice").value(DEFAULT_TAX_INVOICE))
            .andExpect(jsonPath("$.taxDate").value(DEFAULT_TAX_DATE.toString()))
            .andExpect(jsonPath("$.totalLines").value(DEFAULT_TOTAL_LINES.intValue()))
            .andExpect(jsonPath("$.taxAmount").value(DEFAULT_TAX_AMOUNT.intValue()))
            .andExpect(jsonPath("$.grandTotal").value(DEFAULT_GRAND_TOTAL.intValue()))
            .andExpect(jsonPath("$.verificationStatus").value(DEFAULT_VERIFICATION_STATUS))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }


    @Test
    @Transactional
    public void getMVerificationsByIdFiltering() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        Long id = mVerification.getId();

        defaultMVerificationShouldBeFound("id.equals=" + id);
        defaultMVerificationShouldNotBeFound("id.notEquals=" + id);

        defaultMVerificationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMVerificationShouldNotBeFound("id.greaterThan=" + id);

        defaultMVerificationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMVerificationShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllMVerificationsByVerificationNoIsEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where verificationNo equals to DEFAULT_VERIFICATION_NO
        defaultMVerificationShouldBeFound("verificationNo.equals=" + DEFAULT_VERIFICATION_NO);

        // Get all the mVerificationList where verificationNo equals to UPDATED_VERIFICATION_NO
        defaultMVerificationShouldNotBeFound("verificationNo.equals=" + UPDATED_VERIFICATION_NO);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByVerificationNoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where verificationNo not equals to DEFAULT_VERIFICATION_NO
        defaultMVerificationShouldNotBeFound("verificationNo.notEquals=" + DEFAULT_VERIFICATION_NO);

        // Get all the mVerificationList where verificationNo not equals to UPDATED_VERIFICATION_NO
        defaultMVerificationShouldBeFound("verificationNo.notEquals=" + UPDATED_VERIFICATION_NO);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByVerificationNoIsInShouldWork() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where verificationNo in DEFAULT_VERIFICATION_NO or UPDATED_VERIFICATION_NO
        defaultMVerificationShouldBeFound("verificationNo.in=" + DEFAULT_VERIFICATION_NO + "," + UPDATED_VERIFICATION_NO);

        // Get all the mVerificationList where verificationNo equals to UPDATED_VERIFICATION_NO
        defaultMVerificationShouldNotBeFound("verificationNo.in=" + UPDATED_VERIFICATION_NO);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByVerificationNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where verificationNo is not null
        defaultMVerificationShouldBeFound("verificationNo.specified=true");

        // Get all the mVerificationList where verificationNo is null
        defaultMVerificationShouldNotBeFound("verificationNo.specified=false");
    }
                @Test
    @Transactional
    public void getAllMVerificationsByVerificationNoContainsSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where verificationNo contains DEFAULT_VERIFICATION_NO
        defaultMVerificationShouldBeFound("verificationNo.contains=" + DEFAULT_VERIFICATION_NO);

        // Get all the mVerificationList where verificationNo contains UPDATED_VERIFICATION_NO
        defaultMVerificationShouldNotBeFound("verificationNo.contains=" + UPDATED_VERIFICATION_NO);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByVerificationNoNotContainsSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where verificationNo does not contain DEFAULT_VERIFICATION_NO
        defaultMVerificationShouldNotBeFound("verificationNo.doesNotContain=" + DEFAULT_VERIFICATION_NO);

        // Get all the mVerificationList where verificationNo does not contain UPDATED_VERIFICATION_NO
        defaultMVerificationShouldBeFound("verificationNo.doesNotContain=" + UPDATED_VERIFICATION_NO);
    }


    @Test
    @Transactional
    public void getAllMVerificationsByVerificationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where verificationDate equals to DEFAULT_VERIFICATION_DATE
        defaultMVerificationShouldBeFound("verificationDate.equals=" + DEFAULT_VERIFICATION_DATE);

        // Get all the mVerificationList where verificationDate equals to UPDATED_VERIFICATION_DATE
        defaultMVerificationShouldNotBeFound("verificationDate.equals=" + UPDATED_VERIFICATION_DATE);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByVerificationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where verificationDate not equals to DEFAULT_VERIFICATION_DATE
        defaultMVerificationShouldNotBeFound("verificationDate.notEquals=" + DEFAULT_VERIFICATION_DATE);

        // Get all the mVerificationList where verificationDate not equals to UPDATED_VERIFICATION_DATE
        defaultMVerificationShouldBeFound("verificationDate.notEquals=" + UPDATED_VERIFICATION_DATE);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByVerificationDateIsInShouldWork() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where verificationDate in DEFAULT_VERIFICATION_DATE or UPDATED_VERIFICATION_DATE
        defaultMVerificationShouldBeFound("verificationDate.in=" + DEFAULT_VERIFICATION_DATE + "," + UPDATED_VERIFICATION_DATE);

        // Get all the mVerificationList where verificationDate equals to UPDATED_VERIFICATION_DATE
        defaultMVerificationShouldNotBeFound("verificationDate.in=" + UPDATED_VERIFICATION_DATE);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByVerificationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where verificationDate is not null
        defaultMVerificationShouldBeFound("verificationDate.specified=true");

        // Get all the mVerificationList where verificationDate is null
        defaultMVerificationShouldNotBeFound("verificationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllMVerificationsByVerificationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where verificationDate is greater than or equal to DEFAULT_VERIFICATION_DATE
        defaultMVerificationShouldBeFound("verificationDate.greaterThanOrEqual=" + DEFAULT_VERIFICATION_DATE);

        // Get all the mVerificationList where verificationDate is greater than or equal to UPDATED_VERIFICATION_DATE
        defaultMVerificationShouldNotBeFound("verificationDate.greaterThanOrEqual=" + UPDATED_VERIFICATION_DATE);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByVerificationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where verificationDate is less than or equal to DEFAULT_VERIFICATION_DATE
        defaultMVerificationShouldBeFound("verificationDate.lessThanOrEqual=" + DEFAULT_VERIFICATION_DATE);

        // Get all the mVerificationList where verificationDate is less than or equal to SMALLER_VERIFICATION_DATE
        defaultMVerificationShouldNotBeFound("verificationDate.lessThanOrEqual=" + SMALLER_VERIFICATION_DATE);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByVerificationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where verificationDate is less than DEFAULT_VERIFICATION_DATE
        defaultMVerificationShouldNotBeFound("verificationDate.lessThan=" + DEFAULT_VERIFICATION_DATE);

        // Get all the mVerificationList where verificationDate is less than UPDATED_VERIFICATION_DATE
        defaultMVerificationShouldBeFound("verificationDate.lessThan=" + UPDATED_VERIFICATION_DATE);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByVerificationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where verificationDate is greater than DEFAULT_VERIFICATION_DATE
        defaultMVerificationShouldNotBeFound("verificationDate.greaterThan=" + DEFAULT_VERIFICATION_DATE);

        // Get all the mVerificationList where verificationDate is greater than SMALLER_VERIFICATION_DATE
        defaultMVerificationShouldBeFound("verificationDate.greaterThan=" + SMALLER_VERIFICATION_DATE);
    }


    @Test
    @Transactional
    public void getAllMVerificationsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where description equals to DEFAULT_DESCRIPTION
        defaultMVerificationShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the mVerificationList where description equals to UPDATED_DESCRIPTION
        defaultMVerificationShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where description not equals to DEFAULT_DESCRIPTION
        defaultMVerificationShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the mVerificationList where description not equals to UPDATED_DESCRIPTION
        defaultMVerificationShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMVerificationShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the mVerificationList where description equals to UPDATED_DESCRIPTION
        defaultMVerificationShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where description is not null
        defaultMVerificationShouldBeFound("description.specified=true");

        // Get all the mVerificationList where description is null
        defaultMVerificationShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllMVerificationsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where description contains DEFAULT_DESCRIPTION
        defaultMVerificationShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the mVerificationList where description contains UPDATED_DESCRIPTION
        defaultMVerificationShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where description does not contain DEFAULT_DESCRIPTION
        defaultMVerificationShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the mVerificationList where description does not contain UPDATED_DESCRIPTION
        defaultMVerificationShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllMVerificationsByInvoiceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where invoiceNo equals to DEFAULT_INVOICE_NO
        defaultMVerificationShouldBeFound("invoiceNo.equals=" + DEFAULT_INVOICE_NO);

        // Get all the mVerificationList where invoiceNo equals to UPDATED_INVOICE_NO
        defaultMVerificationShouldNotBeFound("invoiceNo.equals=" + UPDATED_INVOICE_NO);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByInvoiceNoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where invoiceNo not equals to DEFAULT_INVOICE_NO
        defaultMVerificationShouldNotBeFound("invoiceNo.notEquals=" + DEFAULT_INVOICE_NO);

        // Get all the mVerificationList where invoiceNo not equals to UPDATED_INVOICE_NO
        defaultMVerificationShouldBeFound("invoiceNo.notEquals=" + UPDATED_INVOICE_NO);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByInvoiceNoIsInShouldWork() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where invoiceNo in DEFAULT_INVOICE_NO or UPDATED_INVOICE_NO
        defaultMVerificationShouldBeFound("invoiceNo.in=" + DEFAULT_INVOICE_NO + "," + UPDATED_INVOICE_NO);

        // Get all the mVerificationList where invoiceNo equals to UPDATED_INVOICE_NO
        defaultMVerificationShouldNotBeFound("invoiceNo.in=" + UPDATED_INVOICE_NO);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByInvoiceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where invoiceNo is not null
        defaultMVerificationShouldBeFound("invoiceNo.specified=true");

        // Get all the mVerificationList where invoiceNo is null
        defaultMVerificationShouldNotBeFound("invoiceNo.specified=false");
    }
                @Test
    @Transactional
    public void getAllMVerificationsByInvoiceNoContainsSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where invoiceNo contains DEFAULT_INVOICE_NO
        defaultMVerificationShouldBeFound("invoiceNo.contains=" + DEFAULT_INVOICE_NO);

        // Get all the mVerificationList where invoiceNo contains UPDATED_INVOICE_NO
        defaultMVerificationShouldNotBeFound("invoiceNo.contains=" + UPDATED_INVOICE_NO);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByInvoiceNoNotContainsSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where invoiceNo does not contain DEFAULT_INVOICE_NO
        defaultMVerificationShouldNotBeFound("invoiceNo.doesNotContain=" + DEFAULT_INVOICE_NO);

        // Get all the mVerificationList where invoiceNo does not contain UPDATED_INVOICE_NO
        defaultMVerificationShouldBeFound("invoiceNo.doesNotContain=" + UPDATED_INVOICE_NO);
    }


    @Test
    @Transactional
    public void getAllMVerificationsByInvoiceDateIsEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where invoiceDate equals to DEFAULT_INVOICE_DATE
        defaultMVerificationShouldBeFound("invoiceDate.equals=" + DEFAULT_INVOICE_DATE);

        // Get all the mVerificationList where invoiceDate equals to UPDATED_INVOICE_DATE
        defaultMVerificationShouldNotBeFound("invoiceDate.equals=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByInvoiceDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where invoiceDate not equals to DEFAULT_INVOICE_DATE
        defaultMVerificationShouldNotBeFound("invoiceDate.notEquals=" + DEFAULT_INVOICE_DATE);

        // Get all the mVerificationList where invoiceDate not equals to UPDATED_INVOICE_DATE
        defaultMVerificationShouldBeFound("invoiceDate.notEquals=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByInvoiceDateIsInShouldWork() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where invoiceDate in DEFAULT_INVOICE_DATE or UPDATED_INVOICE_DATE
        defaultMVerificationShouldBeFound("invoiceDate.in=" + DEFAULT_INVOICE_DATE + "," + UPDATED_INVOICE_DATE);

        // Get all the mVerificationList where invoiceDate equals to UPDATED_INVOICE_DATE
        defaultMVerificationShouldNotBeFound("invoiceDate.in=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByInvoiceDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where invoiceDate is not null
        defaultMVerificationShouldBeFound("invoiceDate.specified=true");

        // Get all the mVerificationList where invoiceDate is null
        defaultMVerificationShouldNotBeFound("invoiceDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllMVerificationsByInvoiceDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where invoiceDate is greater than or equal to DEFAULT_INVOICE_DATE
        defaultMVerificationShouldBeFound("invoiceDate.greaterThanOrEqual=" + DEFAULT_INVOICE_DATE);

        // Get all the mVerificationList where invoiceDate is greater than or equal to UPDATED_INVOICE_DATE
        defaultMVerificationShouldNotBeFound("invoiceDate.greaterThanOrEqual=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByInvoiceDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where invoiceDate is less than or equal to DEFAULT_INVOICE_DATE
        defaultMVerificationShouldBeFound("invoiceDate.lessThanOrEqual=" + DEFAULT_INVOICE_DATE);

        // Get all the mVerificationList where invoiceDate is less than or equal to SMALLER_INVOICE_DATE
        defaultMVerificationShouldNotBeFound("invoiceDate.lessThanOrEqual=" + SMALLER_INVOICE_DATE);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByInvoiceDateIsLessThanSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where invoiceDate is less than DEFAULT_INVOICE_DATE
        defaultMVerificationShouldNotBeFound("invoiceDate.lessThan=" + DEFAULT_INVOICE_DATE);

        // Get all the mVerificationList where invoiceDate is less than UPDATED_INVOICE_DATE
        defaultMVerificationShouldBeFound("invoiceDate.lessThan=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByInvoiceDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where invoiceDate is greater than DEFAULT_INVOICE_DATE
        defaultMVerificationShouldNotBeFound("invoiceDate.greaterThan=" + DEFAULT_INVOICE_DATE);

        // Get all the mVerificationList where invoiceDate is greater than SMALLER_INVOICE_DATE
        defaultMVerificationShouldBeFound("invoiceDate.greaterThan=" + SMALLER_INVOICE_DATE);
    }


    @Test
    @Transactional
    public void getAllMVerificationsByTaxInvoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where taxInvoice equals to DEFAULT_TAX_INVOICE
        defaultMVerificationShouldBeFound("taxInvoice.equals=" + DEFAULT_TAX_INVOICE);

        // Get all the mVerificationList where taxInvoice equals to UPDATED_TAX_INVOICE
        defaultMVerificationShouldNotBeFound("taxInvoice.equals=" + UPDATED_TAX_INVOICE);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByTaxInvoiceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where taxInvoice not equals to DEFAULT_TAX_INVOICE
        defaultMVerificationShouldNotBeFound("taxInvoice.notEquals=" + DEFAULT_TAX_INVOICE);

        // Get all the mVerificationList where taxInvoice not equals to UPDATED_TAX_INVOICE
        defaultMVerificationShouldBeFound("taxInvoice.notEquals=" + UPDATED_TAX_INVOICE);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByTaxInvoiceIsInShouldWork() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where taxInvoice in DEFAULT_TAX_INVOICE or UPDATED_TAX_INVOICE
        defaultMVerificationShouldBeFound("taxInvoice.in=" + DEFAULT_TAX_INVOICE + "," + UPDATED_TAX_INVOICE);

        // Get all the mVerificationList where taxInvoice equals to UPDATED_TAX_INVOICE
        defaultMVerificationShouldNotBeFound("taxInvoice.in=" + UPDATED_TAX_INVOICE);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByTaxInvoiceIsNullOrNotNull() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where taxInvoice is not null
        defaultMVerificationShouldBeFound("taxInvoice.specified=true");

        // Get all the mVerificationList where taxInvoice is null
        defaultMVerificationShouldNotBeFound("taxInvoice.specified=false");
    }
                @Test
    @Transactional
    public void getAllMVerificationsByTaxInvoiceContainsSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where taxInvoice contains DEFAULT_TAX_INVOICE
        defaultMVerificationShouldBeFound("taxInvoice.contains=" + DEFAULT_TAX_INVOICE);

        // Get all the mVerificationList where taxInvoice contains UPDATED_TAX_INVOICE
        defaultMVerificationShouldNotBeFound("taxInvoice.contains=" + UPDATED_TAX_INVOICE);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByTaxInvoiceNotContainsSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where taxInvoice does not contain DEFAULT_TAX_INVOICE
        defaultMVerificationShouldNotBeFound("taxInvoice.doesNotContain=" + DEFAULT_TAX_INVOICE);

        // Get all the mVerificationList where taxInvoice does not contain UPDATED_TAX_INVOICE
        defaultMVerificationShouldBeFound("taxInvoice.doesNotContain=" + UPDATED_TAX_INVOICE);
    }


    @Test
    @Transactional
    public void getAllMVerificationsByTaxDateIsEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where taxDate equals to DEFAULT_TAX_DATE
        defaultMVerificationShouldBeFound("taxDate.equals=" + DEFAULT_TAX_DATE);

        // Get all the mVerificationList where taxDate equals to UPDATED_TAX_DATE
        defaultMVerificationShouldNotBeFound("taxDate.equals=" + UPDATED_TAX_DATE);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByTaxDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where taxDate not equals to DEFAULT_TAX_DATE
        defaultMVerificationShouldNotBeFound("taxDate.notEquals=" + DEFAULT_TAX_DATE);

        // Get all the mVerificationList where taxDate not equals to UPDATED_TAX_DATE
        defaultMVerificationShouldBeFound("taxDate.notEquals=" + UPDATED_TAX_DATE);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByTaxDateIsInShouldWork() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where taxDate in DEFAULT_TAX_DATE or UPDATED_TAX_DATE
        defaultMVerificationShouldBeFound("taxDate.in=" + DEFAULT_TAX_DATE + "," + UPDATED_TAX_DATE);

        // Get all the mVerificationList where taxDate equals to UPDATED_TAX_DATE
        defaultMVerificationShouldNotBeFound("taxDate.in=" + UPDATED_TAX_DATE);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByTaxDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where taxDate is not null
        defaultMVerificationShouldBeFound("taxDate.specified=true");

        // Get all the mVerificationList where taxDate is null
        defaultMVerificationShouldNotBeFound("taxDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllMVerificationsByTaxDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where taxDate is greater than or equal to DEFAULT_TAX_DATE
        defaultMVerificationShouldBeFound("taxDate.greaterThanOrEqual=" + DEFAULT_TAX_DATE);

        // Get all the mVerificationList where taxDate is greater than or equal to UPDATED_TAX_DATE
        defaultMVerificationShouldNotBeFound("taxDate.greaterThanOrEqual=" + UPDATED_TAX_DATE);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByTaxDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where taxDate is less than or equal to DEFAULT_TAX_DATE
        defaultMVerificationShouldBeFound("taxDate.lessThanOrEqual=" + DEFAULT_TAX_DATE);

        // Get all the mVerificationList where taxDate is less than or equal to SMALLER_TAX_DATE
        defaultMVerificationShouldNotBeFound("taxDate.lessThanOrEqual=" + SMALLER_TAX_DATE);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByTaxDateIsLessThanSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where taxDate is less than DEFAULT_TAX_DATE
        defaultMVerificationShouldNotBeFound("taxDate.lessThan=" + DEFAULT_TAX_DATE);

        // Get all the mVerificationList where taxDate is less than UPDATED_TAX_DATE
        defaultMVerificationShouldBeFound("taxDate.lessThan=" + UPDATED_TAX_DATE);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByTaxDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where taxDate is greater than DEFAULT_TAX_DATE
        defaultMVerificationShouldNotBeFound("taxDate.greaterThan=" + DEFAULT_TAX_DATE);

        // Get all the mVerificationList where taxDate is greater than SMALLER_TAX_DATE
        defaultMVerificationShouldBeFound("taxDate.greaterThan=" + SMALLER_TAX_DATE);
    }


    @Test
    @Transactional
    public void getAllMVerificationsByTotalLinesIsEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where totalLines equals to DEFAULT_TOTAL_LINES
        defaultMVerificationShouldBeFound("totalLines.equals=" + DEFAULT_TOTAL_LINES);

        // Get all the mVerificationList where totalLines equals to UPDATED_TOTAL_LINES
        defaultMVerificationShouldNotBeFound("totalLines.equals=" + UPDATED_TOTAL_LINES);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByTotalLinesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where totalLines not equals to DEFAULT_TOTAL_LINES
        defaultMVerificationShouldNotBeFound("totalLines.notEquals=" + DEFAULT_TOTAL_LINES);

        // Get all the mVerificationList where totalLines not equals to UPDATED_TOTAL_LINES
        defaultMVerificationShouldBeFound("totalLines.notEquals=" + UPDATED_TOTAL_LINES);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByTotalLinesIsInShouldWork() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where totalLines in DEFAULT_TOTAL_LINES or UPDATED_TOTAL_LINES
        defaultMVerificationShouldBeFound("totalLines.in=" + DEFAULT_TOTAL_LINES + "," + UPDATED_TOTAL_LINES);

        // Get all the mVerificationList where totalLines equals to UPDATED_TOTAL_LINES
        defaultMVerificationShouldNotBeFound("totalLines.in=" + UPDATED_TOTAL_LINES);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByTotalLinesIsNullOrNotNull() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where totalLines is not null
        defaultMVerificationShouldBeFound("totalLines.specified=true");

        // Get all the mVerificationList where totalLines is null
        defaultMVerificationShouldNotBeFound("totalLines.specified=false");
    }

    @Test
    @Transactional
    public void getAllMVerificationsByTotalLinesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where totalLines is greater than or equal to DEFAULT_TOTAL_LINES
        defaultMVerificationShouldBeFound("totalLines.greaterThanOrEqual=" + DEFAULT_TOTAL_LINES);

        // Get all the mVerificationList where totalLines is greater than or equal to UPDATED_TOTAL_LINES
        defaultMVerificationShouldNotBeFound("totalLines.greaterThanOrEqual=" + UPDATED_TOTAL_LINES);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByTotalLinesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where totalLines is less than or equal to DEFAULT_TOTAL_LINES
        defaultMVerificationShouldBeFound("totalLines.lessThanOrEqual=" + DEFAULT_TOTAL_LINES);

        // Get all the mVerificationList where totalLines is less than or equal to SMALLER_TOTAL_LINES
        defaultMVerificationShouldNotBeFound("totalLines.lessThanOrEqual=" + SMALLER_TOTAL_LINES);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByTotalLinesIsLessThanSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where totalLines is less than DEFAULT_TOTAL_LINES
        defaultMVerificationShouldNotBeFound("totalLines.lessThan=" + DEFAULT_TOTAL_LINES);

        // Get all the mVerificationList where totalLines is less than UPDATED_TOTAL_LINES
        defaultMVerificationShouldBeFound("totalLines.lessThan=" + UPDATED_TOTAL_LINES);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByTotalLinesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where totalLines is greater than DEFAULT_TOTAL_LINES
        defaultMVerificationShouldNotBeFound("totalLines.greaterThan=" + DEFAULT_TOTAL_LINES);

        // Get all the mVerificationList where totalLines is greater than SMALLER_TOTAL_LINES
        defaultMVerificationShouldBeFound("totalLines.greaterThan=" + SMALLER_TOTAL_LINES);
    }


    @Test
    @Transactional
    public void getAllMVerificationsByTaxAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where taxAmount equals to DEFAULT_TAX_AMOUNT
        defaultMVerificationShouldBeFound("taxAmount.equals=" + DEFAULT_TAX_AMOUNT);

        // Get all the mVerificationList where taxAmount equals to UPDATED_TAX_AMOUNT
        defaultMVerificationShouldNotBeFound("taxAmount.equals=" + UPDATED_TAX_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByTaxAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where taxAmount not equals to DEFAULT_TAX_AMOUNT
        defaultMVerificationShouldNotBeFound("taxAmount.notEquals=" + DEFAULT_TAX_AMOUNT);

        // Get all the mVerificationList where taxAmount not equals to UPDATED_TAX_AMOUNT
        defaultMVerificationShouldBeFound("taxAmount.notEquals=" + UPDATED_TAX_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByTaxAmountIsInShouldWork() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where taxAmount in DEFAULT_TAX_AMOUNT or UPDATED_TAX_AMOUNT
        defaultMVerificationShouldBeFound("taxAmount.in=" + DEFAULT_TAX_AMOUNT + "," + UPDATED_TAX_AMOUNT);

        // Get all the mVerificationList where taxAmount equals to UPDATED_TAX_AMOUNT
        defaultMVerificationShouldNotBeFound("taxAmount.in=" + UPDATED_TAX_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByTaxAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where taxAmount is not null
        defaultMVerificationShouldBeFound("taxAmount.specified=true");

        // Get all the mVerificationList where taxAmount is null
        defaultMVerificationShouldNotBeFound("taxAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllMVerificationsByTaxAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where taxAmount is greater than or equal to DEFAULT_TAX_AMOUNT
        defaultMVerificationShouldBeFound("taxAmount.greaterThanOrEqual=" + DEFAULT_TAX_AMOUNT);

        // Get all the mVerificationList where taxAmount is greater than or equal to UPDATED_TAX_AMOUNT
        defaultMVerificationShouldNotBeFound("taxAmount.greaterThanOrEqual=" + UPDATED_TAX_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByTaxAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where taxAmount is less than or equal to DEFAULT_TAX_AMOUNT
        defaultMVerificationShouldBeFound("taxAmount.lessThanOrEqual=" + DEFAULT_TAX_AMOUNT);

        // Get all the mVerificationList where taxAmount is less than or equal to SMALLER_TAX_AMOUNT
        defaultMVerificationShouldNotBeFound("taxAmount.lessThanOrEqual=" + SMALLER_TAX_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByTaxAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where taxAmount is less than DEFAULT_TAX_AMOUNT
        defaultMVerificationShouldNotBeFound("taxAmount.lessThan=" + DEFAULT_TAX_AMOUNT);

        // Get all the mVerificationList where taxAmount is less than UPDATED_TAX_AMOUNT
        defaultMVerificationShouldBeFound("taxAmount.lessThan=" + UPDATED_TAX_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByTaxAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where taxAmount is greater than DEFAULT_TAX_AMOUNT
        defaultMVerificationShouldNotBeFound("taxAmount.greaterThan=" + DEFAULT_TAX_AMOUNT);

        // Get all the mVerificationList where taxAmount is greater than SMALLER_TAX_AMOUNT
        defaultMVerificationShouldBeFound("taxAmount.greaterThan=" + SMALLER_TAX_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllMVerificationsByGrandTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where grandTotal equals to DEFAULT_GRAND_TOTAL
        defaultMVerificationShouldBeFound("grandTotal.equals=" + DEFAULT_GRAND_TOTAL);

        // Get all the mVerificationList where grandTotal equals to UPDATED_GRAND_TOTAL
        defaultMVerificationShouldNotBeFound("grandTotal.equals=" + UPDATED_GRAND_TOTAL);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByGrandTotalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where grandTotal not equals to DEFAULT_GRAND_TOTAL
        defaultMVerificationShouldNotBeFound("grandTotal.notEquals=" + DEFAULT_GRAND_TOTAL);

        // Get all the mVerificationList where grandTotal not equals to UPDATED_GRAND_TOTAL
        defaultMVerificationShouldBeFound("grandTotal.notEquals=" + UPDATED_GRAND_TOTAL);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByGrandTotalIsInShouldWork() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where grandTotal in DEFAULT_GRAND_TOTAL or UPDATED_GRAND_TOTAL
        defaultMVerificationShouldBeFound("grandTotal.in=" + DEFAULT_GRAND_TOTAL + "," + UPDATED_GRAND_TOTAL);

        // Get all the mVerificationList where grandTotal equals to UPDATED_GRAND_TOTAL
        defaultMVerificationShouldNotBeFound("grandTotal.in=" + UPDATED_GRAND_TOTAL);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByGrandTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where grandTotal is not null
        defaultMVerificationShouldBeFound("grandTotal.specified=true");

        // Get all the mVerificationList where grandTotal is null
        defaultMVerificationShouldNotBeFound("grandTotal.specified=false");
    }

    @Test
    @Transactional
    public void getAllMVerificationsByGrandTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where grandTotal is greater than or equal to DEFAULT_GRAND_TOTAL
        defaultMVerificationShouldBeFound("grandTotal.greaterThanOrEqual=" + DEFAULT_GRAND_TOTAL);

        // Get all the mVerificationList where grandTotal is greater than or equal to UPDATED_GRAND_TOTAL
        defaultMVerificationShouldNotBeFound("grandTotal.greaterThanOrEqual=" + UPDATED_GRAND_TOTAL);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByGrandTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where grandTotal is less than or equal to DEFAULT_GRAND_TOTAL
        defaultMVerificationShouldBeFound("grandTotal.lessThanOrEqual=" + DEFAULT_GRAND_TOTAL);

        // Get all the mVerificationList where grandTotal is less than or equal to SMALLER_GRAND_TOTAL
        defaultMVerificationShouldNotBeFound("grandTotal.lessThanOrEqual=" + SMALLER_GRAND_TOTAL);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByGrandTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where grandTotal is less than DEFAULT_GRAND_TOTAL
        defaultMVerificationShouldNotBeFound("grandTotal.lessThan=" + DEFAULT_GRAND_TOTAL);

        // Get all the mVerificationList where grandTotal is less than UPDATED_GRAND_TOTAL
        defaultMVerificationShouldBeFound("grandTotal.lessThan=" + UPDATED_GRAND_TOTAL);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByGrandTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where grandTotal is greater than DEFAULT_GRAND_TOTAL
        defaultMVerificationShouldNotBeFound("grandTotal.greaterThan=" + DEFAULT_GRAND_TOTAL);

        // Get all the mVerificationList where grandTotal is greater than SMALLER_GRAND_TOTAL
        defaultMVerificationShouldBeFound("grandTotal.greaterThan=" + SMALLER_GRAND_TOTAL);
    }


    @Test
    @Transactional
    public void getAllMVerificationsByVerificationStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where verificationStatus equals to DEFAULT_VERIFICATION_STATUS
        defaultMVerificationShouldBeFound("verificationStatus.equals=" + DEFAULT_VERIFICATION_STATUS);

        // Get all the mVerificationList where verificationStatus equals to UPDATED_VERIFICATION_STATUS
        defaultMVerificationShouldNotBeFound("verificationStatus.equals=" + UPDATED_VERIFICATION_STATUS);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByVerificationStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where verificationStatus not equals to DEFAULT_VERIFICATION_STATUS
        defaultMVerificationShouldNotBeFound("verificationStatus.notEquals=" + DEFAULT_VERIFICATION_STATUS);

        // Get all the mVerificationList where verificationStatus not equals to UPDATED_VERIFICATION_STATUS
        defaultMVerificationShouldBeFound("verificationStatus.notEquals=" + UPDATED_VERIFICATION_STATUS);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByVerificationStatusIsInShouldWork() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where verificationStatus in DEFAULT_VERIFICATION_STATUS or UPDATED_VERIFICATION_STATUS
        defaultMVerificationShouldBeFound("verificationStatus.in=" + DEFAULT_VERIFICATION_STATUS + "," + UPDATED_VERIFICATION_STATUS);

        // Get all the mVerificationList where verificationStatus equals to UPDATED_VERIFICATION_STATUS
        defaultMVerificationShouldNotBeFound("verificationStatus.in=" + UPDATED_VERIFICATION_STATUS);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByVerificationStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where verificationStatus is not null
        defaultMVerificationShouldBeFound("verificationStatus.specified=true");

        // Get all the mVerificationList where verificationStatus is null
        defaultMVerificationShouldNotBeFound("verificationStatus.specified=false");
    }
                @Test
    @Transactional
    public void getAllMVerificationsByVerificationStatusContainsSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where verificationStatus contains DEFAULT_VERIFICATION_STATUS
        defaultMVerificationShouldBeFound("verificationStatus.contains=" + DEFAULT_VERIFICATION_STATUS);

        // Get all the mVerificationList where verificationStatus contains UPDATED_VERIFICATION_STATUS
        defaultMVerificationShouldNotBeFound("verificationStatus.contains=" + UPDATED_VERIFICATION_STATUS);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByVerificationStatusNotContainsSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where verificationStatus does not contain DEFAULT_VERIFICATION_STATUS
        defaultMVerificationShouldNotBeFound("verificationStatus.doesNotContain=" + DEFAULT_VERIFICATION_STATUS);

        // Get all the mVerificationList where verificationStatus does not contain UPDATED_VERIFICATION_STATUS
        defaultMVerificationShouldBeFound("verificationStatus.doesNotContain=" + UPDATED_VERIFICATION_STATUS);
    }


    @Test
    @Transactional
    public void getAllMVerificationsByUidIsEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where uid equals to DEFAULT_UID
        defaultMVerificationShouldBeFound("uid.equals=" + DEFAULT_UID);

        // Get all the mVerificationList where uid equals to UPDATED_UID
        defaultMVerificationShouldNotBeFound("uid.equals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByUidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where uid not equals to DEFAULT_UID
        defaultMVerificationShouldNotBeFound("uid.notEquals=" + DEFAULT_UID);

        // Get all the mVerificationList where uid not equals to UPDATED_UID
        defaultMVerificationShouldBeFound("uid.notEquals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByUidIsInShouldWork() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where uid in DEFAULT_UID or UPDATED_UID
        defaultMVerificationShouldBeFound("uid.in=" + DEFAULT_UID + "," + UPDATED_UID);

        // Get all the mVerificationList where uid equals to UPDATED_UID
        defaultMVerificationShouldNotBeFound("uid.in=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByUidIsNullOrNotNull() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where uid is not null
        defaultMVerificationShouldBeFound("uid.specified=true");

        // Get all the mVerificationList where uid is null
        defaultMVerificationShouldNotBeFound("uid.specified=false");
    }

    @Test
    @Transactional
    public void getAllMVerificationsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where active equals to DEFAULT_ACTIVE
        defaultMVerificationShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the mVerificationList where active equals to UPDATED_ACTIVE
        defaultMVerificationShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where active not equals to DEFAULT_ACTIVE
        defaultMVerificationShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the mVerificationList where active not equals to UPDATED_ACTIVE
        defaultMVerificationShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultMVerificationShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the mVerificationList where active equals to UPDATED_ACTIVE
        defaultMVerificationShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllMVerificationsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        // Get all the mVerificationList where active is not null
        defaultMVerificationShouldBeFound("active.specified=true");

        // Get all the mVerificationList where active is null
        defaultMVerificationShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllMVerificationsByAdOrganizationIsEqualToSomething() throws Exception {
        // Get already existing entity
        ADOrganization adOrganization = mVerification.getAdOrganization();
        mVerificationRepository.saveAndFlush(mVerification);
        Long adOrganizationId = adOrganization.getId();

        // Get all the mVerificationList where adOrganization equals to adOrganizationId
        defaultMVerificationShouldBeFound("adOrganizationId.equals=" + adOrganizationId);

        // Get all the mVerificationList where adOrganization equals to adOrganizationId + 1
        defaultMVerificationShouldNotBeFound("adOrganizationId.equals=" + (adOrganizationId + 1));
    }


    @Test
    @Transactional
    public void getAllMVerificationsByCurrencyIsEqualToSomething() throws Exception {
        // Get already existing entity
        CCurrency currency = mVerification.getCurrency();
        mVerificationRepository.saveAndFlush(mVerification);
        Long currencyId = currency.getId();

        // Get all the mVerificationList where currency equals to currencyId
        defaultMVerificationShouldBeFound("currencyId.equals=" + currencyId);

        // Get all the mVerificationList where currency equals to currencyId + 1
        defaultMVerificationShouldNotBeFound("currencyId.equals=" + (currencyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMVerificationShouldBeFound(String filter) throws Exception {
        restMVerificationMockMvc.perform(get("/api/m-verifications?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mVerification.getId().intValue())))
            .andExpect(jsonPath("$.[*].verificationNo").value(hasItem(DEFAULT_VERIFICATION_NO)))
            .andExpect(jsonPath("$.[*].verificationDate").value(hasItem(DEFAULT_VERIFICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].invoiceNo").value(hasItem(DEFAULT_INVOICE_NO)))
            .andExpect(jsonPath("$.[*].invoiceDate").value(hasItem(DEFAULT_INVOICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].taxInvoice").value(hasItem(DEFAULT_TAX_INVOICE)))
            .andExpect(jsonPath("$.[*].taxDate").value(hasItem(DEFAULT_TAX_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalLines").value(hasItem(DEFAULT_TOTAL_LINES.intValue())))
            .andExpect(jsonPath("$.[*].taxAmount").value(hasItem(DEFAULT_TAX_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].grandTotal").value(hasItem(DEFAULT_GRAND_TOTAL.intValue())))
            .andExpect(jsonPath("$.[*].verificationStatus").value(hasItem(DEFAULT_VERIFICATION_STATUS)))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restMVerificationMockMvc.perform(get("/api/m-verifications/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMVerificationShouldNotBeFound(String filter) throws Exception {
        restMVerificationMockMvc.perform(get("/api/m-verifications?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMVerificationMockMvc.perform(get("/api/m-verifications/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMVerification() throws Exception {
        // Get the mVerification
        restMVerificationMockMvc.perform(get("/api/m-verifications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMVerification() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        int databaseSizeBeforeUpdate = mVerificationRepository.findAll().size();

        // Update the mVerification
        MVerification updatedMVerification = mVerificationRepository.findById(mVerification.getId()).get();
        // Disconnect from session so that the updates on updatedMVerification are not directly saved in db
        em.detach(updatedMVerification);
        updatedMVerification
            .verificationNo(UPDATED_VERIFICATION_NO)
            .verificationDate(UPDATED_VERIFICATION_DATE)
            .description(UPDATED_DESCRIPTION)
            .invoiceNo(UPDATED_INVOICE_NO)
            .invoiceDate(UPDATED_INVOICE_DATE)
            .taxInvoice(UPDATED_TAX_INVOICE)
            .taxDate(UPDATED_TAX_DATE)
            .totalLines(UPDATED_TOTAL_LINES)
            .taxAmount(UPDATED_TAX_AMOUNT)
            .grandTotal(UPDATED_GRAND_TOTAL)
            .verificationStatus(UPDATED_VERIFICATION_STATUS)
            .uid(UPDATED_UID)
            .active(UPDATED_ACTIVE);
        MVerificationDTO mVerificationDTO = mVerificationMapper.toDto(updatedMVerification);

        restMVerificationMockMvc.perform(put("/api/m-verifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mVerificationDTO)))
            .andExpect(status().isOk());

        // Validate the MVerification in the database
        List<MVerification> mVerificationList = mVerificationRepository.findAll();
        assertThat(mVerificationList).hasSize(databaseSizeBeforeUpdate);
        MVerification testMVerification = mVerificationList.get(mVerificationList.size() - 1);
        assertThat(testMVerification.getVerificationNo()).isEqualTo(UPDATED_VERIFICATION_NO);
        assertThat(testMVerification.getVerificationDate()).isEqualTo(UPDATED_VERIFICATION_DATE);
        assertThat(testMVerification.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMVerification.getInvoiceNo()).isEqualTo(UPDATED_INVOICE_NO);
        assertThat(testMVerification.getInvoiceDate()).isEqualTo(UPDATED_INVOICE_DATE);
        assertThat(testMVerification.getTaxInvoice()).isEqualTo(UPDATED_TAX_INVOICE);
        assertThat(testMVerification.getTaxDate()).isEqualTo(UPDATED_TAX_DATE);
        assertThat(testMVerification.getTotalLines()).isEqualTo(UPDATED_TOTAL_LINES);
        assertThat(testMVerification.getTaxAmount()).isEqualTo(UPDATED_TAX_AMOUNT);
        assertThat(testMVerification.getGrandTotal()).isEqualTo(UPDATED_GRAND_TOTAL);
        assertThat(testMVerification.getVerificationStatus()).isEqualTo(UPDATED_VERIFICATION_STATUS);
        assertThat(testMVerification.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testMVerification.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingMVerification() throws Exception {
        int databaseSizeBeforeUpdate = mVerificationRepository.findAll().size();

        // Create the MVerification
        MVerificationDTO mVerificationDTO = mVerificationMapper.toDto(mVerification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMVerificationMockMvc.perform(put("/api/m-verifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mVerificationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MVerification in the database
        List<MVerification> mVerificationList = mVerificationRepository.findAll();
        assertThat(mVerificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMVerification() throws Exception {
        // Initialize the database
        mVerificationRepository.saveAndFlush(mVerification);

        int databaseSizeBeforeDelete = mVerificationRepository.findAll().size();

        // Delete the mVerification
        restMVerificationMockMvc.perform(delete("/api/m-verifications/{id}", mVerification.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MVerification> mVerificationList = mVerificationRepository.findAll();
        assertThat(mVerificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}