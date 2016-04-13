package com.erokhin.tools.ims24;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Dmitry Erokhin (dmitry.erokhin@gmail.com)
 * 10/04/16
 */
@Component
public class ResultPageParser {
    private static Logger log = LoggerFactory.getLogger(ResultPageParser.class);

    @Value("${fetch.delay:1000}")
    private int queryDelay;

    @Value("${fetch.base-url}")
    private String baseUrl;

    public ResultPageParser() {
    }

    public Set<String> parse(String url) {
        return parseDirty(url).stream()
                .filter(s -> s.matches("/expose/\\d+$"))
                .collect(Collectors.toSet());
    }

    private List<String> parseDirty(String url) {
        url = baseUrl + url;
        log.debug("Requesting url: {}", url);

        List<String> result = new LinkedList<>();

        Document doc;

        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            log.warn("Can not retrieve data from url", e);
            return Collections.emptyList();
        }

        Element list = doc.getElementById("resultListItems");
        Elements allLinks = list.getElementsByTag("a");

        for (Element link : allLinks) {
            result.add(link.attr("href"));
        }

        log.debug("Collected {} links", result.size());

        Optional<Element> nextPageLink = doc.getElementById("pager")
                .getElementsByTag("a")
                .stream()
                .filter(e -> e.attr("data-is24-qa").equalsIgnoreCase("paging_bottom_next"))
                .findFirst();


        if (nextPageLink.isPresent()) {
            String newSearchUrl = nextPageLink.get().attr("href");
            log.debug("Next search url will be requested after {} seconds: {}.", queryDelay / 1000, newSearchUrl);
            try {
                Thread.sleep(queryDelay);
            } catch (InterruptedException e) {
                log.warn("Interrupted while next query awaiting", e);
            }
            result.addAll(parseDirty(newSearchUrl));
        }

        return result;
    }
}

