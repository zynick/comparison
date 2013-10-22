package com.zynick.comparison.sites;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zynick.comparison.Item;

public class Zalora implements Website {
    
    @Override
    public List<Item> parse(String query, int size) throws IOException {
        
        // request for a page
        Document doc = Jsoup.connect("http://www.zalora.com.my/catalog/?q=" + query).get();
        
        Elements listS = doc.getElementById("productsCatalog").children();
        
        // grab the list
        size = (size < listS.size()) ? size : listS.size(); // size or listS.size, which ever is smaller (max 4)
        ArrayList<Item> result = new ArrayList<Item> (size);
        for (int i = 0; i < size; i++) {
            Element listE = listS.get(i);
            String title = listE.getElementsByClass("itm-title").get(0).text().trim();
            String price = listE.getElementsByClass("itm-price").not(" .old").text().substring(3).replaceAll(",", "");
            double dPrice = Double.parseDouble(price);
            String img = listE.select("span.itm-imageWrapper img").first().attr("src");
            String url = listE.select("a.itm-link").first().attr("href");
            url = "http://www.zalora.com.my" + url;
            
            result.add(new Item("zalora", title, dPrice, img, url));
        }
        
        return result;
    }

}