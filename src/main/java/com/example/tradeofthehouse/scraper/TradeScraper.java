package com.example.tradeofthehouse.scraper;

import com.example.tradeofthehouse.model.Trade;
import com.example.tradeofthehouse.repository.PoliticianRepository;
import com.example.tradeofthehouse.repository.TradeRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.*;

/**
 * Scraper component that fetches U.S. House financial disclosure filings,
 * downloads PDF documents, and extracts structured trade information.
 * <p>
 * This class uses:
 *   - Jsoup to submit search queries and extract PDF links</li>
 *   - Apache PDFBox to read PDF text</li>
 *   - Regular expressions to parse trade details (ticker, dates, amounts)</li>
 * <p>
 * The extracted trades are stored in a static list of {@link Trade} objects,
 * accessible through {@link #getTrades()}.
 */
@Component
public class TradeScraper {

    private static final String BASE_URL = "https://disclosures-clerk.house.gov/";
    private static final String SEARCH_PATH = "/FinancialDisclosure/ViewSearch";
    private static final String SEARCH_URL = BASE_URL + SEARCH_PATH;

    static final List<String> uniqueTickers = new ArrayList<>();

    private static final Pattern pattern = Pattern.compile("\\(([A-Z]{2,5})\\)");
    private static final Pattern datePattern = Pattern.compile("\\b\\d{1,2}[-/]\\d{1,2}[-/]\\d{2,4}\\b");

    private static final List<Path> downloadedFiles = new ArrayList<>();

    static final List<Trade> trades = new ArrayList<>();

    /**
     * Runs a scrape for filings from a specific politician and year.
     * Downloads PDFs, extracts trades, and prints them to the console
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            String politicianName = "Greene";
            String filingYear = "2025";

            String html = searchFilings(politicianName, filingYear);
//            System.out.println(html);
            List<String> pdfLinks = extractPdfLinks(html);

            System.out.println("Found PDF links:");
            for (String link : pdfLinks) {
                System.out.println(" - " + link);
            }

            for (String pdf : pdfLinks) {
                System.out.println(" - " + pdf);
                byte[] pdfData = downloadPdfBytes(pdf);
//                System.out.println(Arrays.toString(pdfData));
                List<Trade> trades = extractTradesFromPdf(pdfData, politicianName);
                for (Trade trade : trades) {
                    System.out.println(trade);
                }
            }

//            for (String link : pdfLinks) {
//                Path filePath = Path.of(downloadPdf(link), "pdfs");
//                downloadedFiles.add(filePath);
//            }
//
//            for (Path pdfPath : downloadedFiles) {
//                extractTradesFromPdf(String.valueOf(pdfPath), politicianName);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Submits a search query for filings on the disclosure site.
     *
     * @param lastName   the last name of the politician
     * @param filingYear the filing year to search
     * @return the raw HTML response from the search
     * @throws IOException if the request fails
     */
    public static String searchFilings(String lastName, String filingYear) throws IOException {
        Connection.Response response =
                Jsoup.connect(SEARCH_URL).userAgent("Mozilla/5.0 (compatible; PTR-scraper/1.0)")
                        .timeout(20000).method(Connection.Method.GET).execute();

        Document doc = response.parse();

        Element form = doc.selectFirst("form[id~=form], form");
        if (form == null) {
            throw new RuntimeException("Search form not found");
        }

        Map<String, String> payload = new HashMap<>();
        for (Element tag : form.select("input, select, textarea")) {
            String name = tag.attr("name");
            if (name.isEmpty()) {
                continue;
            }

            if (tag.tagName().equals("select")) {
                if (name.equalsIgnoreCase("FilingYear")) {
                    payload.put(name, filingYear);
                } else {
                    Element selected = tag.selectFirst("option[selected]");
                    payload.put(name, selected != null ? selected.attr("value") : "");
                }
            } else if (tag.tagName().equals("input")) {
                String type = tag.attr("type").toLowerCase();
                if (type.equals("text") || type.equals("search") || type.equals("hidden")) {
                    if (name.equalsIgnoreCase("LastName")) {
                        payload.put(name, lastName);
                    } else {
                        payload.put(name, tag.attr("value"));
                    }
                } else {
                    payload.put(name, tag.attr("value"));
                }
            } else {
                payload.put(name, tag.text().isEmpty() ? tag.attr("value") : tag.text());
            }
        }

        String action = form.attr("action").isEmpty() ? SEARCH_PATH : form.attr("action");
        String submitUrl = BASE_URL + action;

        Connection.Method method =
                form.attr("method").equalsIgnoreCase("get") ? Connection.Method.GET :
                        Connection.Method.POST;

        Connection.Response submitResponse =
                Jsoup.connect(submitUrl).userAgent("Mozilla/5.0 (compatible; PTR-scraper/1.0)")
                        .timeout(30000).method(method).data(payload).execute();

        return submitResponse.body();
    }

    /**
     * Extracts PDF links from the HTML search results.
     *
     * @param html the raw HTML returned by {@link #searchFilings}
     * @return list of PDF URLs (relative or absolute)
     */
    public static List<String> extractPdfLinks(String html) {
        Document doc = Jsoup.parse(html);
        List<String> links = new ArrayList<>();
        for (Element a : doc.select("a[href$=.pdf]")) {
            links.add(a.attr("href"));
        }
        return links;
    }

    /**
     * Downloads a PDF from the disclosure site to a local directory.
     *
     * @param link    the PDF link (relative or absolute)
     //* @param saveDir the local directory to save to
     * @return the path to the downloaded file
     * @throws IOException if download fails
     */

    // At first there were two arguments, String link, String saveDir
    // This was because I was saving the files locally
    // But during the database implementation, this become unnecessary
    // It returns a String now, but first this was a Path as you can see in the code

    public static String downloadPdf(String link) throws IOException {
        //Files.createDirectories(Paths.get(saveDir));
        String pdfUrl = link.startsWith("http") ? link : BASE_URL + link;

        System.out.println(pdfUrl);

        String fileName = Paths.get(new URL(pdfUrl).getPath()).getFileName().toString();
        //Path filePath = Paths.get(saveDir, fileName);

//        try (InputStream in = new URL(pdfUrl).openStream()) {
//            Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
//        }

        //System.out.println("Downloaded: " + filePath);
        //return filePath;
        return pdfUrl;
    }

    public static byte[] downloadPdfBytes(String pdfLink) throws IOException {
        String pdfUrl = pdfLink.startsWith("http") ? pdfLink : BASE_URL + pdfLink;
        try (InputStream in = new URL(pdfUrl).openStream()) {
            return in.readAllBytes();
        }
    }

    public static List<Trade> extractTradesFromPdf(byte[] pdfData, String politician) throws IOException {
        try (PDDocument document = PDDocument.load(pdfData)) {
            String text = new PDFTextStripper().getText(document);
            System.out.println(text);
            return parseTrades(text, politician);
        }
    }

    /**
     * Opens a PDF and extracts trades by parsing its text content.
     *
     * @param pdfPath    path to the downloaded PDF
     * @param politician the politician name for context
     */
    public static void extractTradesFromPdf(String pdfPath, String politician) {
        try (PDDocument document = PDDocument.load(pdfPath.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            System.out.println(text);

            // Parse and collect trades
            parseTradesPrev(text, politician);

            System.out.println("\nFound " + trades.size() + " trades:");
            for (Trade trade : trades) {
                System.out.println(trade);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses raw text from a PDF into structured {@link Trade} objects.
     *
     * @param text       PDF text content
     * @param politician politician name for attribution
     */
    public static void parseTradesPrev(String text, String politician) {
        String[] lines = text.split("\n");
        List<Trade> currentTrades = new ArrayList<>();
        Trade currentTrade = null;
        boolean inTrade = false;
        String pendingAmount = null;

        for (String line : lines) {
            line = line.trim();

            // Handle pending amount across lines
            if (pendingAmount != null) {
                if (line.startsWith("$") && !containsLetters(line) && ! containsQuestionmark(line)) {
                    currentTrade.amount = pendingAmount + " " + line;
                    pendingAmount = null;
                    continue;
                } else {
                    currentTrade.amount = pendingAmount;
                    pendingAmount = null;
                }
            }

            // Start a new trade if ticker found
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String ticker = matcher.group(1);
                uniqueTickers.add(ticker);
                inTrade = true;

                if (currentTrade != null) {
                    currentTrades.add(currentTrade);
                }

                currentTrade = new Trade();
                currentTrade.politician = politician;
                currentTrade.ticker = ticker;
                currentTrade.action = "TBD";
                currentTrade.amount = "$?";
                currentTrade.filingStatus = "X";
                currentTrade.description = "X";
            }

            // Process trade details
            if (inTrade && currentTrade != null) {
                // Action
                if (line.startsWith("P ") || line.startsWith("S ")) {
                    String action;
                    if (line.startsWith("P")) {
                        action = "Buy";
                    } else if (line.startsWith("S")) {
                        action = "Sell";
                    } else {
                        action = "(TBD)";
                    }
                    currentTrade.action =
                            line.contains("(partial)") ? action + " (partial)" : action;
                }

                // Dates
                Matcher dateMatcher = datePattern.matcher(line);
                List<LocalDate> dates = new ArrayList<>();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
                while (dateMatcher.find()) {
                    String dateStr = dateMatcher.group().replace('-', '/');
                    try {
                        dates.add(LocalDate.parse(dateStr, formatter));
                    } catch (Exception e) {
                        // skip invalid date
                    }
                }
                if (!dates.isEmpty()) {
                    currentTrade.date = dates.get(0).atStartOfDay();
                }

                if (line.contains("$")) {

                }

                // Amount (with multi-line handling)
                if (line.contains("$") && !containsLetters(line) && !containsQuestionmark(line)) {
                    int amountStart = line.indexOf('$');
                    String amountPart = line.substring(amountStart);

                    if (amountPart.trim().endsWith("-")) {
                        pendingAmount = amountPart.trim();
                        currentTrade.amount = pendingAmount;
                    } else {
                        currentTrade.amount = amountPart.replaceAll("\\s+", " ").replace("- -", "-")
                                .replace("$ ", "$").trim();
                    }
                }

                // Filing status
                if (line.startsWith("F") && !line.contains("ID")) {
                    int statusStart = line.indexOf(':');
                    currentTrade.filingStatus =
                            (statusStart != -1) ? line.substring(statusStart + 1).trim() :
                                    line.replaceFirst("^F\\w*", "").trim();
                }

                // Description
                if (line.startsWith("D") && !line.contains(politician)) {
                    int descStart = line.indexOf(':');
                    currentTrade.description =
                            (descStart != -1) ? line.substring(descStart + 1).trim() :
                                    line.replaceFirst("^D\\w*", "").trim();
                }
            }
        }

        if (currentTrade != null) {
            currentTrades.add(currentTrade);
        }
        trades.addAll(currentTrades);
    }

    public static List<Trade> parseTrades(String text, String politician) {
        List<Trade> tradesFromPdf = new ArrayList<>();
        String[] lines = text.split("\n");
        List<Trade> currentTrades = new ArrayList<>();
        Trade currentTrade = null;
        boolean inTrade = false;
        String pendingAmount = null;

        for (String line : lines) {
            line = line.trim();

            // Handle pending amount across lines
            if (pendingAmount != null) {
                if (line.startsWith("$") && !containsLetters(line) && !containsQuestionmark(line)) {
                    currentTrade.amount = pendingAmount + " " + line;
                    pendingAmount = null;
                    continue;
                } else {
                    currentTrade.amount = pendingAmount;
                    pendingAmount = null;
                }
            }

            // Start a new trade if ticker found
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String ticker = matcher.group(1);
                uniqueTickers.add(ticker);
                inTrade = true;

                if (currentTrade != null) {
                    currentTrades.add(currentTrade);
                }

                currentTrade = new Trade();
                currentTrade.politician = politician;
                currentTrade.ticker = ticker;
                currentTrade.action = "X";
                currentTrade.amount = "X";
                currentTrade.description = "X";
                currentTrade.filingStatus = "X";
            }

            // Process trade details
            if (inTrade && currentTrade != null) {
                // Action
                if (line.startsWith("P ") || line.startsWith("S ") || line.contains(" P ") || line.contains(" S ")) {
                    String action;
                    if (line.startsWith("P")) {
                        action = "Buy";
                    } else if (line.startsWith("S")) {
                        action = "Sell";
                    } else if (line.contains(" P ")) {
                        action = "Buy";
                    } else if (line.contains(" S ")) {
                        action = "Sell";
                    }else {
                        action = "TBD";
                    }
                    currentTrade.action = line.contains("(partial)") ? action + " (partial)" : action;
                }

                // Dates
                Matcher dateMatcher = datePattern.matcher(line);
                List<LocalDate> dates = new ArrayList<>();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
                while (dateMatcher.find()) {
                    String dateStr = dateMatcher.group().replace('-', '/');
                    try {
                        dates.add(LocalDate.parse(dateStr, formatter));
                    } catch (Exception e) {
                        // skip invalid date
                    }
                }
                if (!dates.isEmpty()) {
                    currentTrade.date = dates.get(0).atStartOfDay();
                }

                // Amount (with multi-line handling)
                if (line.contains("$") && !containsLetters(line) && !containsQuestionmark(line)) {
                    int amountStart = line.indexOf('$');
                    String amountPart = line.substring(amountStart);

                    if (amountPart.trim().endsWith("-")) {
                        pendingAmount = amountPart.trim();
                        currentTrade.amount = pendingAmount;
                    } else {
                        currentTrade.amount = amountPart.replaceAll("\\s+", " ").replace("- -", "-")
                                .replace("$ ", "$").trim();
                    }
                }

                // Filing status
                if (line.startsWith("F") && !line.contains("ID")) {
                    int statusStart = line.indexOf(':');
                    currentTrade.filingStatus =
                            (statusStart != -1) ? line.substring(statusStart + 1).trim() :
                                    line.replaceFirst("^F\\w*", "").trim();
                }

                // Description
                if (line.startsWith("D") && !line.contains(politician)) {
                    int descStart = line.indexOf(':');
                    currentTrade.description =
                            (descStart != -1) ? line.substring(descStart + 1).trim() :
                                    line.replaceFirst("^D\\w*", "").trim();
                }
            }
        }

        if (currentTrade != null) {
            currentTrades.add(currentTrade);
        }
        trades.addAll(currentTrades);
        tradesFromPdf.addAll(currentTrades); // from your existing logic
        return tradesFromPdf;
    }

    /**
     * Checks if a string contains any alphabetic letters.
     *
     * @param line the string to check
     * @return true if letters are found, false otherwise
     */
    private static boolean containsLetters(String line) {
        return Pattern.compile("[a-zA-Z]").matcher(line).find();
    }

    private static boolean containsQuestionmark(String line) {
        return Pattern.compile("/?").matcher(line).find();
    }

    /**
     * Returns all trades collected.
     *
     * @return list of trades
     */
    public List<Trade> getTrades() {
        return trades;
    }
}