package com.zynick.comparison.sites;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zynick.comparison.Item;

//parsing code works as of 2013-07-06
public class Rakuten implements Website {

    @Override
    public List<Item> parse(String query, int size) throws IOException {

        // request for a page
        Document doc = Jsoup.connect("http://www.rakuten.com.my/search/" + query)
                .userAgent("zynick-bot (me@zynick.com http://zynick.com)")  // be an ethical crawler :) 
                .timeout(10*1000).get();

        Elements rowS = doc.select("div.b-layout-right div.b-container").get(2).children();

        ArrayList<Item> result = new ArrayList<Item>(size);
        int count = 0;
        loop: for (Element rowE : rowS) {
            if (count >= size)
                break;

            Elements colS = rowE.children();

            for (Element colE : colS) {
                if (count >= size)
                    break loop;

                Element aE = colE.getElementsByTag("a").first();
                String img = aE.child(0).attr("src");
                String title = colE.select("div.b-fix-2lines a").first().text();
                String price = colE.select("span.b-text-prime").first().text().replaceAll(",", "");
                double dPrice = Double.parseDouble(price);
                String url = aE.attr("href");
                url = "http://www.rakuten.com.my" + url;

                result.add(new Item("rakuten", title, dPrice, img, url));

                count++;
            }
        }

        return result;
    }
}
