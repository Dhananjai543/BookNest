package com.booknest.loanservice.repository;

import com.booknest.loanservice.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
