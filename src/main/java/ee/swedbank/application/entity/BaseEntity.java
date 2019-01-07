package ee.swedbank.application.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;

import lombok.Data;

@Data
@MappedSuperclass
public abstract class BaseEntity {

	@Id
	@GeneratedValue(generator = "seqGen")
	@SequenceGenerator(name = "seqGen", sequenceName = "consumption_sequence", allocationSize = 1)
	private Long id;

}
