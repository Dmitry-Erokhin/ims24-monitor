package com.erokhin.tools.ims24;

import com.erokhin.tools.ims24.appartement.Apartment;
import com.erokhin.tools.ims24.appartement.ApartmentPageParser;
import com.erokhin.tools.ims24.appartement.ApartmentsRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@SpringBootApplication
public class IMS24 implements CommandLineRunner {
    private static Logger log = LoggerFactory.getLogger(IMS24.class);

    @Autowired
    private ResultPageParser resultPageParser;

    @Autowired
    private ApartmentPageParser apartmentPageParser;

    @Autowired
    private ApartmentsRepo apartmentsRepo;


    public static void main(String[] args) throws IOException {
        SpringApplication.run(IMS24.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
//        String url = "https://www.immobilienscout24.de/Suche/S-T/WAZ/Berlin/Berlin/Tiergarten-Tiergarten_Grunewald-Wilmersdorf_Prenzlauer-Berg-Prenzlauer-Berg_Friedrichshain-Friedrichshain_Kreuzberg-Kreuzberg_Schoeneberg-Schoeneberg_Wilmersdorf-Wilmersdorf_Mitte-Mitte_Charlottenburg-Charlottenburg/EURO-800,00-1300,00/-/-/true/-/-/-/-/-/-/-/3,00-?enteredFrom=saved_search";
//        Collection<String> links = resultPageParser.parse(url);
//        Files.write(Paths.get("./links.txt"), links);



////
        Files.lines(Paths.get("./doc/links.txt"))
                .limit(5)
                .map(apartmentPageParser::parse)
                .forEach(a -> a.ifPresent(apartmentsRepo::save));

        System.out.println(apartmentsRepo.findAll());

    }
}
