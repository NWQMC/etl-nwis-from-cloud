package gov.acwi.wqp.etl.natdb.gw.aquiferType;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import gov.acwi.wqp.etl.BaseTruncateNwisTable;
import gov.acwi.wqp.etl.natdb.gw.BasicLookup;

@Component
@StepScope
public class DeleteAquiferType extends BaseTruncateNwisTable {

	@Autowired
	public DeleteAquiferType(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, BasicLookup.AQUIFER_TYPE);
	}

}
