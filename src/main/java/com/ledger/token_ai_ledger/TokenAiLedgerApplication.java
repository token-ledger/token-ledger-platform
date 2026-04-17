package com.ledger.token_ai_ledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.ledger.springailedger.domain.member.Member;
import com.ledger.springailedger.domain.member.MemberRepository;

@SpringBootApplication(scanBasePackages = "com.ledger")
@EntityScan(basePackageClasses = Member.class)
@EnableJpaRepositories(basePackageClasses = MemberRepository.class)
public class TokenAiLedgerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TokenAiLedgerApplication.class, args);
	}

}
