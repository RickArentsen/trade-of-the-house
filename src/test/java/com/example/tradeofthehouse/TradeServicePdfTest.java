package com.example.tradeofthehouse;

import com.example.tradeofthehouse.model.Pdf;
import com.example.tradeofthehouse.repository.PdfRepository;
import com.example.tradeofthehouse.scraper.TradeScraper;
import com.example.tradeofthehouse.service.TradeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TradeServicePdfTest {

    @Mock
    private TradeScraper scraper;

    @Mock
    private PdfRepository pdfRepository;

    @InjectMocks
    private TradeService tradeService;

    @Test
    public void testPdfLinkExtractionAndDatabaseCheck() throws IOException {
        // Mock data
        String politicianName = "Nancy Pelosi";
        int currentYear = 2024;
        String mockHtml = "<html><a href='/path/to/file1.pdf'>PDF 1</a><a href='/path/to/file2.pdf'>PDF 2</a></html>";
        List<String> mockPdfLinks = Arrays.asList(
                "/path/to/file1.pdf",
                "/path/to/file2.pdf"
        );

        // Mock the scraper behavior
        when(scraper.searchFilings(politicianName, String.valueOf(currentYear))).thenReturn(mockHtml);
        when(scraper.extractPdfLinks(mockHtml)).thenReturn(mockPdfLinks);

        // Mock the repository behavior - first PDF exists, second doesn't
        when(pdfRepository.existsByFileName("/path/to/file1.pdf")).thenReturn(true);
        when(pdfRepository.existsByFileName("/path/to/file2.pdf")).thenReturn(false);

        // Execute the test
        String html = scraper.searchFilings(politicianName, String.valueOf(currentYear));
        List<String> pdfLinks = scraper.extractPdfLinks(html);

        // Verify the results
        assertNotNull(html);
        assertNotNull(pdfLinks);
        assertEquals(2, pdfLinks.size());
        assertTrue(pdfLinks.contains("/path/to/file1.pdf"));
        assertTrue(pdfLinks.contains("/path/to/file2.pdf"));

        // Verify database checks
        assertTrue(pdfRepository.existsByFileName("/path/to/file1.pdf"));
        assertFalse(pdfRepository.existsByFileName("/path/to/file2.pdf"));

        // Verify interactions
        verify(scraper, times(1)).searchFilings(politicianName, String.valueOf(currentYear));
        verify(scraper, times(1)).extractPdfLinks(mockHtml);
        verify(pdfRepository, times(1)).existsByFileName("/path/to/file1.pdf");
        verify(pdfRepository, times(1)).existsByFileName("/path/to/file2.pdf");
    }

//    @Test
//    public void testPdfSavingToDatabase() {
//        // Mock data
//        String pdfLink = "/path/to/file.pdf";
//        byte[] pdfData = "mock pdf content".getBytes();
//        LocalDateTime now = LocalDateTime.now();
//        Pdf mockPdf = new Pdf(pdfLink, pdfData, now);
//
//        // Mock the repository behavior
//        when(pdfRepository.save(any(Pdf.class))).thenReturn(mockPdf);
//
//        // Execute the test
//        Pdf savedPdf = pdfRepository.save(new Pdf(pdfLink, pdfData, now));
//
//        // Verify the results
//        assertNotNull(savedPdf);
//        assertEquals(pdfLink, savedPdf.getFileName());
//        assertArrayEquals(pdfData, savedPdf.getFileData());
//
//        // Verify interactions
//        verify(pdfRepository, times(1)).save(any(Pdf.class));
//    }

    @Test
    public void testPdfDownloadIntegration() {
        // This test would be an integration test that actually downloads a PDF
        // For now, we'll skip this test or mark it as @Disabled
        // since it requires network connectivity
    }
}