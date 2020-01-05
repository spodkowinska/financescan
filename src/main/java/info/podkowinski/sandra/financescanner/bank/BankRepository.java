package info.podkowinski.sandra.financescanner.bank;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankRepository extends JpaRepository<Bank, Long> {
    List<Bank>findByUserId(Long id);
}
