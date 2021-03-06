package com.bhp.opusb.repository;

import com.bhp.opusb.domain.MBidding;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the MBidding entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MBiddingRepository extends JpaRepository<MBidding, Long>, JpaSpecificationExecutor<MBidding> {
}
