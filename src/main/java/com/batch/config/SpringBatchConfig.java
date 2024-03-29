package com.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;

import com.batch.entity.Customer;
import com.batch.repository.CustomerRepository;
import lombok.AllArgsConstructor;

@Configuration
class BatchInfrastructureConfig {

    @Bean
    public JobBuilderFactory jobBuilderFactory(JobRepository jobRepository) {
        return new JobBuilderFactory(jobRepository);
    }
}


@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class SpringBatchConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

//	@Autowired
	private StepBuilderFactory stepBuilderFactory;

//	@Autowired
	private CustomerRepository repository;

//	public SpringBatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
//			CustomerRepository repository) {
//		this.jobBuilderFactory = jobBuilderFactory;
//		this.stepBuilderFactory = stepBuilderFactory;
//		this.repository = repository;
//	}

	@Bean
	public FlatFileItemReader<Customer> reader() {
		FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<Customer>();
		itemReader.setResource(new FileSystemResource("src/main/resources/Customers.csv"));
		itemReader.setName("csvReader");
		itemReader.setLinesToSkip(1);
		itemReader.setLineMapper(lineMapper());
		return itemReader;
	}

	private LineMapper<Customer> lineMapper() {
		DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();

		delimitedLineTokenizer.setDelimiter(",");
		delimitedLineTokenizer.setStrict(false);
		delimitedLineTokenizer.setNames("id", "firstName", "lastName", "email", "contact", "jobTitle");

		BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Customer.class);

		lineMapper.setLineTokenizer(delimitedLineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);

		return lineMapper;
	}

	@Bean
	public CustomerProcessor processor() {
		return new CustomerProcessor();
	}

	@Bean
	public RepositoryItemWriter<Customer> writer() {
		RepositoryItemWriter<Customer> writer = new RepositoryItemWriter<>();
		writer.setRepository(repository);
		writer.setMethodName("save");
		return writer;
	}

	@Bean
	public Step step1() {

		return stepBuilderFactory.get("csv-step").<Customer, Customer>chunk(10).reader(reader()).processor(processor())
				.writer(writer()).build();
	}

	@Bean
	public Job runJob() {
		return jobBuilderFactory.get("importCustomers").flow(step1()).end().build();
	}
	

}
