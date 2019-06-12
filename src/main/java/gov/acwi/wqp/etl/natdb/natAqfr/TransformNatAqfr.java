package gov.acwi.wqp.etl.natdb.natAqfr;

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
public class TransformNatAqfr {
	
	@Autowired
	@Qualifier(Application.DATASOURCE_NWIS_QUALIFIER)
	private DataSource dataSourceNwis;

	@Autowired
	@Qualifier(Application.DATASOURCE_NATDB_QUALIFIER)
	private DataSource dataSourceNatdb;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	
	@Bean
	public JdbcCursorItemReader<NatAqfr> natAqfrReader() {
		return new JdbcCursorItemReaderBuilder<NatAqfr>()
				.dataSource(dataSourceNatdb)
				.name("natdbNatAqfr")
				.sql("select nat_aqfr_state.country_cd, nat_aqfr_state.state_cd, nat_aqfr.nat_aqfr_cd, nat_aqfr.nat_aqfr_nm from nat_aqfr join nat_aqfr_state on nat_aqfr.nat_aqfr_cd = nat_aqfr_state.nat_aqfr_cd")
				.rowMapper(new NatAqfrRowMapper())
				.build();
	}

	@Bean
	public NatAqfrProcessor natAqfrProcessor() {
		return new NatAqfrProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<NatAqfr> natAqfrWriter() {
		return new JdbcBatchItemWriterBuilder<NatAqfr>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("INSERT INTO nat_aqfr" + 
						" (country_cd, state_cd, nat_aqfr_cd, nat_aqfr_nm) VALUES (:countryCd, :stateCd, :natAqfrCd, :natAqfrNm)")
				.dataSource(dataSourceNwis)
				.build();
	}
}
