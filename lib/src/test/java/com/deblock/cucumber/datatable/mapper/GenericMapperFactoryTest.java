package com.deblock.cucumber.datatable.mapper;

import com.deblock.cucumber.datatable.mapper.beans.Bean;
import com.deblock.cucumber.datatable.mapper.beans.Record;
import com.deblock.cucumber.datatable.mapper.datatable.BeanDatatableMapper;
import com.deblock.cucumber.datatable.mapper.datatable.FieldResolver;
import com.deblock.cucumber.datatable.mapper.datatable.RecordDatatableMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

public class GenericMapperFactoryTest {

    @Test
    public void shouldReturnABeanMapperForARegularClass() {
        final var factory = new GenericMapperFactory(Mockito.mock(TypeMetadataFactory.class), Mockito.mock(FieldResolver.class));

        final var result = factory.build(Bean.class);

        assertThat(result).isInstanceOf(BeanDatatableMapper.class);
    }

    @Test
    public void shouldReturnARecordMapperForARecord() {
        final var factory = new GenericMapperFactory(Mockito.mock(TypeMetadataFactory.class), Mockito.mock(FieldResolver.class));

        final var result = factory.build(Record.class);

        assertThat(result).isInstanceOf(RecordDatatableMapper.class);
    }

    public void shouldReturnASimpleColumnDatatableMapperIfMetadataIsFound() {
        final var factory = new GenericMapperFactory(Mockito.mock(TypeMetadataFactory.class), Mockito.mock(FieldResolver.class));

        final var result = factory.build(Record.class);

        assertThat(result).isInstanceOf(RecordDatatableMapper.class);
    }
}
