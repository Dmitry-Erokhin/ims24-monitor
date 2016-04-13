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

/**
 * Created by Dmitry Erokhin (dmitry.erokhin@gmail.com)
 * 10/04/16
 */
@Component
public class ApartementPageParser {
    private static Logger log = LoggerFactory.getLogger(ApartementPageParser.class);

    @Value("${fetch.delay:1000}")
    private int queryDelay;

    @Value("${fetch.base-url}")
    private String baseUrl;

    public ApartementPageParser() {
    }

    public Optional<Apartement> parse(String url) {
        Apartement apartement = new Apartement();

        Optional<String> id = Arrays.stream(url.split("/"))
                .filter(e -> e.matches("\\d+"))
                .findAny();

        if (id.isPresent()) {
            apartement.id = id.get();
        } else {
            log.warn("Can not process url {}", url);
            return Optional.empty();
        }

        apartement.url = baseUrl + url;

        log.debug("Requesting url: {}", baseUrl + url);

        Document doc;
        try {
            doc = Jsoup.connect(baseUrl + url).get();
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }

        apartement.vacantFrom = parseVacantFrom(doc);
        apartement.minMonthsRental = parseMinMonthsRental(doc);
        apartement.maxMonthsRental = parseMaxMonthsRental(doc);
        Double rooms = parseRooms(doc);
        apartement.rooms = rooms == null ? null : rooms.intValue();
        apartement.hasHalfroom = rooms != null && rooms > apartement.rooms;
        apartement.livingSpace = parseLivingSpace(doc);
        apartement.maxCapacity = parseMaxCapacity(doc);
        apartement.floor = parseFloor(doc);
        apartement.petsProhibited = parsePetsProhibited(doc);
        apartement.finalRentCost = parseFinalRentCost(doc);
        apartement.deposit = parseDeposit(doc);
        apartement.location = parseLocation(doc);
        apartement.additionalInfo = parseInfo(doc);

        log.debug("Parsed {}", apartement);
        return Optional.of(apartement);
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
                "Rooms amount",
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
                "Rooms amount",
                d -> d.getElementsByClass("is24qa-etage").first(),
                e -> e.text().trim(),
                null
        );
    }

    private boolean parsePetsProhibited(Document doc) { //is24qa-haustiere
        return parse(
                doc,
                "Rooms amount",
                d -> d.getElementsByClass("is24qa-haustiere").first(),
                e -> "Nein".equalsIgnoreCase(e.text().trim()),
                null
        );
    }

    private Integer parseFinalRentCost(Document doc) {
        return parse(
                doc,
                "Final cost",
                d -> d.getElementsByClass("is24qa-pauschalmiete").first(),
                e -> new Scanner(e.text()).nextInt(),
                null
        );
    }

    private int parseDeposit(Document doc) { //is24qa-kaution
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

    private String parseInfo(Document doc) {
        return parse(
                doc,
                "Deposit",
                d -> d.getElementsByClass("is24qa-objektbeschreibung").first(),
                e -> e.text().trim(),
                null
        );
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

