package com.erokhin.tools.ims24.appartement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

import static com.erokhin.tools.ims24.appartement.Apartment.Conclusion.*;

/**
 * Created by Dmitry Erokhin (dmitry.erokhin@gmail.com)
 * 10/04/16
 */
@Component
public class ApartmentPageParser {
    private static Logger log = LoggerFactory.getLogger(ApartmentPageParser.class);

    @Value("${fetch.delay:1000}")
    private int queryDelay;

    @Value("${fetch.base-url}")
    private String baseUrl;

    public ApartmentPageParser() {
    }

    public Optional<Apartment> parse(String url) {
        Apartment apartment = new Apartment();

        Optional<String> id = Arrays.stream(url.split("/"))
                .filter(e -> e.matches("\\d+"))
                .findAny();

        if (id.isPresent()) {
            apartment.setId(Long.parseLong(id.get()));
        } else {
            log.warn("Can not process url {}", url);
            return Optional.empty();
        }

        apartment.setUrl(baseUrl + url);

        log.debug("Requesting url: {}", baseUrl + url);

        Document doc;
        try {
            doc = Jsoup.connect(baseUrl + url).get();
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }

        apartment.setVacantFrom(parseVacantFrom(doc));
        apartment.setMinMonthsRental(parseMinMonthsRental(doc));
        apartment.setMaxMonthsRental(parseMaxMonthsRental(doc));
        Double rooms = parseRooms(doc);
        apartment.setRooms(rooms);
        apartment.setLivingSpace(parseLivingSpace(doc));
        apartment.setMaxCapacity(parseMaxCapacity(doc));
        apartment.setFloor(parseFloor(doc));
        apartment.setPetsProhibited(parsePetsProhibited(doc));
        apartment.setFinalRentCost(parseFinalRentCost(doc));
        apartment.setDeposit(parseDeposit(doc));
        apartment.setLocation(parseLocation(doc));
        apartment.setConclusion(
                apartment.isPetsProhibited() ? NO : MAYBE
        );

        log.debug("Parsed {}", apartment);
        return Optional.of(apartment);
    }

    private LocalDate parseVacantFrom(Document doc) {
        return parse(
                doc,
                "Vacant from",
                d -> d.getElementsByClass("is24qa-bezugsfrei-ab").first(),
                e -> LocalDate.parse(e.text(), DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                null
        );
    }

    private Integer parseMinMonthsRental(Document doc) {
        return parse(
                doc,
                "Minimum rental period",
                d -> d.getElementsByClass("is24qa-mindestmietdauer").first(),
                e -> new Scanner(e.text()).nextInt(),
                null
        );
    }

    private Integer parseMaxMonthsRental(Document doc) {
        return parse(
                doc,
                "Minimum rental period",
                d -> d.getElementsByClass("is24qa-maximale-mietdauer").first(),
                e -> new Scanner(e.text()).nextInt(),
                null
        );
    }

    private Double parseRooms(Document doc) {
        return parse(
                doc,
                "Rooms amount",
                d -> d.getElementsByClass("is24qa-zimmer").first(),
                e -> new Scanner(e.text()).nextDouble(),
                null
        );
    }

    private Integer parseLivingSpace(Document doc) {
        return parse(
                doc,
                "LivingSpace",
                d -> d.getElementsByClass("is24qa-wohnflaeche-ca").first(),
                e -> new Scanner(e.text()).nextInt(),
                null
        );
    }

    private Integer parseMaxCapacity(Document doc) {
        //TODO: implement
        return null;
    }

    private String parseFloor(Document doc) {
        return parse(
                doc,
                "Floor",
                d -> d.getElementsByClass("is24qa-etage").first(),
                e -> e.text().trim(),
                null
        );
    }

    private boolean parsePetsProhibited(Document doc) { //is24qa-haustiere
        return parse(
                doc,
                "Pets allowance",
                d -> d.getElementsByClass("is24qa-haustiere").first(),
                e -> "Nein".equalsIgnoreCase(e.text().trim()),
                false
        );
    }

    private Integer parseFinalRentCost(Document doc) {
        return parse(
                doc,
                "Final cost",
                d -> d.getElementsByClass("is24qa-pauschalmiete").first(),
                e -> Integer.valueOf(e.text().replaceAll("[^\\d+]", "")),
                null
        );
    }

    private Integer parseDeposit(Document doc) { //is24qa-kaution
        return parse(
                doc,
                "Deposit",
                d -> d.getElementsByClass("is24qa-kaution").first(),
                e -> new Scanner(e.text()).nextInt(),
                null
        );
    }

    private String parseLocation(Document doc) {
        //FIXME:
//        return parse(
//                doc,
//                "Location",
//                d -> d.getElementsByClass("is24-expose-address").first().getElementsByClass("data-ng-non-bindable").first(),
//                e -> e.text().trim(),
//                null
//        );
        return null;
    }

    private <T> T parse(Document doc,
                        String name,
                        Function<Document, Element> elementSupplier,
                        Function<Element, T> parser,
                        T errorResultValue) {
        Element element = elementSupplier.apply(doc);
        if (element == null) {
            log.warn("Can't find \"{}\" field", name);
            return errorResultValue;
        }

        try {
            return parser.apply(element);
        } catch (Exception e) {
            log.warn("Could not parse \"{}\"", name);
            return errorResultValue;
        }
    }

}

