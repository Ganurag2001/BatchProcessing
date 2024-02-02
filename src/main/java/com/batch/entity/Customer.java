package com.batch.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CUSTOMERS_INFO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

	@Id
	private Integer id;

	private String firstName;

	private String lastName;

	private String email;

	private String contact;

	private String jobTitle;

}
