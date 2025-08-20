package ir.bankingSystem.repository;

import ir.bankingSystem.model.entity.CompressedExceptionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CompressedExceptionLogRepository extends JpaRepository<CompressedExceptionLog , Long> {
}
