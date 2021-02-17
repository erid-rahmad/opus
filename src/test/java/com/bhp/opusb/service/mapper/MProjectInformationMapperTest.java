package com.bhp.opusb.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MProjectInformationMapperTest {

    private MProjectInformationMapper mProjectInformationMapper;

    @BeforeEach
    public void setUp() {
        mProjectInformationMapper = new MProjectInformationMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(mProjectInformationMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(mProjectInformationMapper.fromId(null)).isNull();
    }
}
