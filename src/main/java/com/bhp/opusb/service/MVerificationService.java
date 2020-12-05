package com.bhp.opusb.service;

import java.util.List;
import java.util.Optional;

import com.bhp.opusb.domain.ADOrganization;
import com.bhp.opusb.domain.MVerification;
import com.bhp.opusb.repository.MVerificationRepository;
import com.bhp.opusb.service.dto.MVerificationDTO;
import com.bhp.opusb.service.dto.MVerificationLineCriteria;
import com.bhp.opusb.service.dto.MVerificationLineDTO;
import com.bhp.opusb.service.dto.VerificationDTO;
import com.bhp.opusb.service.mapper.MVerificationMapper;
import com.bhp.opusb.service.trigger.ProcessTrigger;
import com.bhp.opusb.service.trigger.process.integration.MVerificationOutbound;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.filter.LongFilter;

/**
 * Service Implementation for managing {@link MVerification}.
 */
@Service
@Transactional
public class MVerificationService {

    private final Logger log = LoggerFactory.getLogger(MVerificationService.class);

    private final ADOrganization organization;
    private final MVerificationRepository mVerificationRepository;
    private final MVerificationMapper mVerificationMapper;
    private final MVerificationLineService mVerificationLineService;
    private final MVerificationLineQueryService mVerificationLineQueryService;
    private final ProcessTrigger mVerificationOutbound;

    public MVerificationService(MVerificationRepository mVerificationRepository,
            MVerificationMapper mVerificationMapper, MVerificationLineService mVerificationLineService,
            MVerificationLineQueryService mVerificationLineQueryService, ADOrganizationService adOrganizationService,
            @Qualifier("mVerificationOutbound") ProcessTrigger mVerificationOutbound) {
        this.mVerificationRepository = mVerificationRepository;
        this.mVerificationMapper = mVerificationMapper;
        this.mVerificationLineService = mVerificationLineService;
        this.mVerificationLineQueryService = mVerificationLineQueryService;
        this.mVerificationOutbound = mVerificationOutbound;

        organization = adOrganizationService.getDefaultOrganization();
    }

    public MVerification submitEVerification(VerificationDTO verificationDTO) {
        // Ensure verification has generated ID.
        MVerification verification = mVerificationMapper.toEntity(verificationDTO.getForm());
        verification.active(true)
            .adOrganization(organization);

        mVerificationRepository.save(verification);

        // Batch save verification line.
        mVerificationLineService.saveAll(verificationDTO.getLine(), verification, organization);

        return verification;
    }

    /**
     * TODO Use a generic method to update the document status for every entities.
     * TODO Use the workflow engine for maintaining the flow state.
     */
    public void updateDocumentStatus(VerificationDTO verificationDTO) {
        MVerificationDTO mVerificationDTO = verificationDTO.getForm();
        List<MVerificationLineDTO> mVerificationLineDTOs = verificationDTO.getLine();
        log.debug("Request to update MVerificationDTO's document status : {}", mVerificationDTO);
        MVerification mVerification = mVerificationMapper.toEntity(mVerificationDTO);

        mVerificationRepository.save(mVerification);
        mVerificationLineService.saveAll(mVerificationLineDTOs, mVerification, organization);

        if (!verificationDTO.getRemove().isEmpty()) {
            mVerificationLineService.removeAll(verificationDTO.getRemove());
        }

        if (mVerification.getVerificationStatus().equals("APV")) {
            findOne(mVerification.getId())
                .ifPresent(header -> {
                    MVerificationLineCriteria lineCriteria = new MVerificationLineCriteria();
                    LongFilter idFilter = new LongFilter();
                    idFilter.setEquals(header.getId());
                    lineCriteria.setVerificationId(idFilter);
                    List<MVerificationLineDTO> lines = mVerificationLineQueryService.findByCriteria(lineCriteria);

                    if (!lines.isEmpty()) {
                        ((MVerificationOutbound) mVerificationOutbound).sendPayload(header, lines);
                    }
                });

        }
    }

    /**
     * Save a mVerification.
     *
     * @param mVerificationDTO the entity to save.
     * @return the persisted entity.
     */
    public MVerificationDTO save(MVerificationDTO mVerificationDTO) {
        log.debug("Request to save MVerification : {}", mVerificationDTO);
        MVerification mVerification = mVerificationMapper.toEntity(mVerificationDTO);
        mVerification = mVerificationRepository.save(mVerification);
        return mVerificationMapper.toDto(mVerification);
    }

    /**
     * Get all the mVerifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MVerificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MVerifications");
        return mVerificationRepository.findAll(pageable)
            .map(mVerificationMapper::toDto);
    }

    /**
     * Get one mVerification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MVerificationDTO> findOne(Long id) {
        log.debug("Request to get MVerification : {}", id);
        return mVerificationRepository.findById(id)
            .map(mVerificationMapper::toDto);
    }

    /**
     * Delete the mVerification by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MVerification : {}", id);
        mVerificationRepository.deleteById(id);
    }
}
