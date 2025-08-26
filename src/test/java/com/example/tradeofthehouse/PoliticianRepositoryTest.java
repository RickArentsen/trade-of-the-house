package com.example.tradeofthehouse;

import com.example.tradeofthehouse.repository.PoliticianRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PoliticianRepositoryTest {

    @Autowired
    private PoliticianRepository politicianRepository;

    @Test
    public void testFindAllNames() {
        List<String> names = politicianRepository.findAllNames();
        assertNotNull(names);
        assertFalse(names.isEmpty());
        System.out.println("Found names: " + names);
    }
}