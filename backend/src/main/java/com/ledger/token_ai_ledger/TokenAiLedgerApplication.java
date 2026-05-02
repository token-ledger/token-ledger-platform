package com.ledger.token_ai_ledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.ledger")
@EntityScan(basePackages = "com.ledger.springailedger.domain")
@EnableJpaRepositories(basePackages = "com.ledger.springailedger.domain")
public class TokenAiLedgerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TokenAiLedgerApplication.class, args);
	}
}