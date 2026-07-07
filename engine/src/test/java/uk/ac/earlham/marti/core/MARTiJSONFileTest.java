package uk.ac.earlham.marti.core;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MARTiJSONFileTest {

    @Test
    void writesJsonStructureAndNestedArray() throws Exception {
        Path tempFile = Files.createTempFile("marti-jsonfile-test", ".json");
        try {
            MARTiJSONFile jsonFile = new MARTiJSONFile();
            jsonFile.openFile(tempFile.toString());
            jsonFile.beginJSONSection();
            jsonFile.writeJSONTagString("name", "alice", false);
            jsonFile.beginJSONArray();
            jsonFile.writeJSONTag("nested");
            jsonFile.endJSONArray(true);
            jsonFile.endJSONSection(false);
            jsonFile.closeFile();

            List<String> lines = Files.readAllLines(tempFile);
            assertFalse(lines.isEmpty(), "Expected output file to contain JSON text");
            assertTrue(lines.stream().anyMatch(line -> line.contains("{") || line.contains("}")), "Expected JSON object delimiters");
            assertTrue(lines.stream().anyMatch(line -> line.contains("name") && line.contains("alice")), "Expected a JSON string tag");
            assertTrue(lines.stream().anyMatch(line -> line.contains("nested")), "Expected nested array content to be written");
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    void outputsVersionMetadataWhenRequested() throws Exception {
        Path tempFile = Files.createTempFile("marti-jsonfile-version-test", ".json");
        try {
            MARTiJSONFile jsonFile = new MARTiJSONFile();
            jsonFile.openFile(tempFile.toString());
            jsonFile.outputVersions(true);
            jsonFile.closeFile();

            String contents = Files.readString(tempFile);
            assertTrue(contents.contains("minknow_version"), "Expected minknow_version metadata");
            assertTrue(contents.contains("guppy_version"), "Expected guppy_version metadata");
            assertTrue(contents.contains("blast_version"), "Expected blast_version metadata");
            assertTrue(contents.contains("nt_database_version"), "Expected nt_database_version metadata");
            assertTrue(contents.contains("metamaps_db_version"), "Expected metamaps_db_version metadata");
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }
}
