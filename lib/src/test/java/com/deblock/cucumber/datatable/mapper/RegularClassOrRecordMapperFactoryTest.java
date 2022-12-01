package com.deblock.cucumber.datatable.mapper;

import com.deblock.cucumber.datatable.mapper.beans.Bean;
import com.deblock.cucumber.datatable.mapper.beans.Record;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

public class RegularClassOrRecordMapperFactoryTest {

    @Test
    public void shouldReturnABeanMapperForARegularClass() {
        final var factory = new RegularClassOrRecordMapperFactory();

        final var result = factory.build(Bean.class, Mockito.mock(TypeMetadataFactory.class));

        assertThat(result).isInstanceOf(BeanMapper.class);
    }

    @Test
    public void shouldReturnARecordMapperForARecord() {
        final var factory = new RegularClassOrRecordMapperFactory();

        final var result = factory.build(Record.class, Mockito.mock(TypeMetadataFactory.class));

        assertThat(result).isInstanceOf(RecordMapper.class);
    }
}
