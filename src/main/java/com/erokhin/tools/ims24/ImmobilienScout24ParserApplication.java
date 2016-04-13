package com.erokhin.tools.ims24;

import com.erokhin.tools.ims24.appartement.Apartement;
import com.erokhin.tools.ims24.appartement.ApartementPageParser;
import com.erokhin.tools.ims24.appartement.ApartementsRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Optional;

@SpringBootApplication
public class ImmobilienScout24ParserApplication implements CommandLineRunner {
    private static Logger log = LoggerFactory.getLogger(ImmobilienScout24ParserApplication.class);

    @Autowired
    private ResultPageParser resultPageParser;

    @Autowired
    private ApartementPageParser apartementPageParser;

    @Autowired
    private ApartementsRepo apartementsRepo;


    public static void main(String[] args) throws IOException {
//        String s = " Scout-ID: 87655203 ";
//        Pattern p = Pattern.compile("^ Scout-ID: \\d+ $");
//        Matcher m = p.matcher(s);
//        System.out.println(m.find());
//        System.out.println(m.group());
        SpringApplication.run(ImmobilienScout24ParserApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
//        String url = "https://www.immobilienscout24.de/Suche/S-T/WAZ/Berlin/Berlin/Tiergarten-Tiergarten_Grunewald-Wilmersdorf_Prenzlauer-Berg-Prenzlauer-Berg_Friedrichshain-Friedrichshain_Kreuzberg-Kreuzberg_Schoeneberg-Schoeneberg_Wilmersdorf-Wilmersdorf_Mitte-Mitte_Charlottenburg-Charlottenburg/EURO-800,00-1300,00/-/-/true/-/-/-/-/-/-/-/3,00-?enteredFrom=saved_search";
//        Collection<String> links = resultPageParser.parse(url);
//        Files.write(Paths.get("./links.txt"), links);
////
        Optional<Apartement> a = apartementPageParser.parse("/expose/87655203");

        if (a.isPresent()) {
            apartementsRepo.save(a.get());
        }

        System.out.println(apartementsRepo.findAll());

    }
}
