package com.example.tradeofthehouse.repository;

import com.example.tradeofthehouse.model.Pdf;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PdfRepository extends JpaRepository<Pdf, Long> {

    boolean existsByFileName(String fileName);

    // Custom query to fetch a PDF by its file name
    @Query("SELECT p FROM Pdf p WHERE p.fileName = :fileName")
    Optional<Pdf> findByFileName(String fileName);
}
