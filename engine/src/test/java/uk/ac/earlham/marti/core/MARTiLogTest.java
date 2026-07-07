package uk.ac.earlham.marti.core;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class MARTiLogTest {

    @Test
    void writesTimestampedLogEntriesToFile() throws Exception {
        Path tempFile = Files.createTempFile("marti-log-test", ".log");
        try {
            MARTiLog log = new MARTiLog();
            log.open(tempFile.toString());
            log.setLogLevel(1);
            log.print(1, "hello");
            log.println(1, "world");
            log.printlnLogAndScreen("screened");
            log.close();

            String content = Files.readString(tempFile);
            assertTrue(content.contains("hello"), "Expected printed text to appear in the log file");
            assertTrue(content.contains("world"), "Expected printed line to appear in the log file");
            assertTrue(content.contains("screened"), "Expected printlnLogAndScreen output to appear in the log file");
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    void calendarConversionProducesExpectedFormattedString() {
        GregorianCalendar calendar = new GregorianCalendar(2025, 0, 2, 3, 4, 5);
        MARTiLog log = new MARTiLog();

        String formatted = log.calendarToString(calendar);
        assertEquals("2/1/2025 03:04:05", formatted);

        assertNotNull(log.stringToCalendar(formatted));
        assertTrue(Pattern.compile("^\\d{1,2}/\\d{1,2}/\\d{4} \\d{2}:\\d{2}:\\d{2}$")
                .matcher(log.getTime()).matches(), "Expected getTime() to return a timestamp string");
    }
}
