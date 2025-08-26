package com.example.tradeofthehouse.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pdfs")
public class Pdf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pdf_id")
    private Long id;

    @Column(name = "file_name", nullable = false, length = 255, columnDefinition = "BYTEA")
    private String fileName;

//    @Lob
//    @Column(name = "file_data", nullable = false)
//    private byte[] fileData;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    public Pdf() {

    }

    public Pdf(String fileName, LocalDateTime uploadedAt) {
        //byte[] fileData

        this.fileName = fileName;
        //this.fileData = fileData;
        this.uploadedAt = uploadedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

//    public byte[] getFileData() { return fileData; }
//    public void setFileData(byte[] fileData) { this.fileData = fileData; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }

    @Override
    public String toString() {
        return String.format("Pdf{id=%d, fileName='%s', uploadedAt=%s}", id, fileName, uploadedAt);
    }
}
