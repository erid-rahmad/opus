package com.bhp.opusb.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.bhp.opusb.domain.MVendorScoring;
import com.bhp.opusb.domain.*; // for static metamodels
import com.bhp.opusb.repository.MVendorScoringRepository;
import com.bhp.opusb.service.dto.MVendorScoringCriteria;
import com.bhp.opusb.service.dto.MVendorScoringDTO;
import com.bhp.opusb.service.mapper.MVendorScoringMapper;

/**
 * Service for executing complex queries for {@link MVendorScoring} entities in the database.
 * The main input is a {@link MVendorScoringCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MVendorScoringDTO} or a {@link Page} of {@link MVendorScoringDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MVendorScoringQueryService extends QueryService<MVendorScoring> {

    private final Logger log = LoggerFactory.getLogger(MVendorScoringQueryService.class);

    private final MVendorScoringRepository mVendorScoringRepository;

    private final MVendorScoringMapper mVendorScoringMapper;

    public MVendorScoringQueryService(MVendorScoringRepository mVendorScoringRepository, MVendorScoringMapper mVendorScoringMapper) {
        this.mVendorScoringRepository = mVendorScoringRepository;
        this.mVendorScoringMapper = mVendorScoringMapper;
    }

    /**
     * Return a {@link List} of {@link MVendorScoringDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MVendorScoringDTO> findByCriteria(MVendorScoringCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MVendorScoring> specification = createSpecification(criteria);
        return mVendorScoringMapper.toDto(mVendorScoringRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MVendorScoringDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MVendorScoringDTO> findByCriteria(MVendorScoringCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MVendorScoring> specification = createSpecification(criteria);
        return mVendorScoringRepository.findAll(specification, page)
            .map(mVendorScoringMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MVendorScoringCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MVendorScoring> specification = createSpecification(criteria);
        return mVendorScoringRepository.count(specification);
    }

    /**
     * Function to convert {@link MVendorScoringCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MVendorScoring> createSpecification(MVendorScoringCriteria criteria) {
        Specification<MVendorScoring> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MVendorScoring_.id));
            }
            if (criteria.getPercentage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPercentage(), MVendorScoring_.percentage));
            }
            if (criteria.getUid() != null) {
                specification = specification.and(buildSpecification(criteria.getUid(), MVendorScoring_.uid));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), MVendorScoring_.active));
            }
            if (criteria.getBiddingId() != null) {
                specification = specification.and(buildSpecification(criteria.getBiddingId(),
                    root -> root.join(MVendorScoring_.bidding, JoinType.LEFT).get(MBidding_.id)));
            }
            if (criteria.getAdOrganizationId() != null) {
                specification = specification.and(buildSpecification(criteria.getAdOrganizationId(),
                    root -> root.join(MVendorScoring_.adOrganization, JoinType.LEFT).get(ADOrganization_.id)));
            }
            if (criteria.getBiddingCriteriaId() != null) {
                specification = specification.and(buildSpecification(criteria.getBiddingCriteriaId(),
                    root -> root.join(MVendorScoring_.biddingCriteria, JoinType.LEFT).get(CBiddingCriteria_.id)));
            }
            if (criteria.getBiddingSubCriteriaId() != null) {
                specification = specification.and(buildSpecification(criteria.getBiddingSubCriteriaId(),
                    root -> root.join(MVendorScoring_.biddingSubCriteria, JoinType.LEFT).get(CBiddingSubCriteria_.id)));
            }
            if (criteria.getAdUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getAdUserId(),
                    root -> root.join(MVendorScoring_.adUser, JoinType.LEFT).get(AdUser_.id)));
            }
        }
        return specification;
    }
}
