package me.galaxy.lock.sample;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductDTO {

    private HashMap name;
    private ArrayList company;
    private int price;

    public ProductDTO(HashMap name, ArrayList company, int price) {
        this.name = name;
        this.company = company;
        this.price = price;
    }

    public HashMap getName() {
        return name;
    }

    public void setName(HashMap name) {
        this.name = name;
    }

    public ArrayList getCompany() {
        return company;
    }

    public void setCompany(ArrayList company) {
        this.company = company;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
