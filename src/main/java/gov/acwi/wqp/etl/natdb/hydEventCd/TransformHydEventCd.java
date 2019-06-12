package gov.acwi.wqp.etl.natdb.hydEventCd;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import gov.acwi.wqp.etl.Application;

@Component
public class TransformHydEventCd {
	
	@Autowired
	@Qualifier(Application.DATASOURCE_NWIS_QUALIFIER)
	private DataSource dataSourceNwis;

	@Autowired
	@Qualifier(Application.DATASOURCE_NATDB_QUALIFIER)
	private DataSource dataSourceNatdb;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	
	@Bean
	public JdbcCursorItemReader<HydEventCd> hydEventCdReader() {
		return new JdbcCursorItemReaderBuilder<HydEventCd>()
				.dataSource(dataSourceNatdb)
				.name("natdbHydEventCd")
				.sql("select hyd_event_cd, hyd_event_srt_nu, hyd_event_vld_fg, hyd_event_nm, hyd_event_ds from hyd_event_cd")
				.rowMapper(new HydEventCdRowMapper())
				.build();
	}

	@Bean
	public HydEventCdProcessor hydEventCdProcessor() {
		return new HydEventCdProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<HydEventCd> hydEventCdWriter() {
		return new JdbcBatchItemWriterBuilder<HydEventCd>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("INSERT INTO hyd_event_cd" + 
						" (hyd_event_cd, hyd_event_srt_nu, hyd_event_vld_fg, hyd_event_nm, hyd_event_ds) VALUES (:hydEventCd, :hydEventSrtNu, :hydEventVldFg, :hydEventNm, :hydEventDs)")
				.dataSource(dataSourceNwis)
				.build();
	}
}
