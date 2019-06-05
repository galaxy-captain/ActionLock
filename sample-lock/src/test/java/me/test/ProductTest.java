package me.test;

import me.galaxy.lock.sample.ProductDTO;
import me.galaxy.lock.sample.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class ProductTest {

    @Autowired
    private ProductService productService;

    @Test
    public void testParallelProductSave() {

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                testProductSave();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                testProductSave();
            }
        });

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                testProductSave();
            }
        });

        Thread t4 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                testProductSave();
            }
        });

        Thread t5 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                testProductSave();
            }
        });

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testProductSave() {

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                if (i < 5)
                    insertProduct("美团");
                else
                    insertProduct("头条");
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                if (i < 5)
                    insertProduct("美团");
                else
                    insertProduct("头条");

            }
        });

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                if (i < 3)
                    insertProduct("头条");
                else if (i < 7)
                    insertProduct("美团");
                else
                    insertProduct("头条");

            }
        });

        Thread t4 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                if (i < 3)
                    insertProduct("美团");
                else if (i < 7)
                    insertProduct("头条");
                else
                    insertProduct("美团");

            }
        });

        Thread t5 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                if (i < 2)
                    insertProduct("美团");
                else if (i < 8)
                    insertProduct("头条");
                else
                    insertProduct("美团");

            }
        });

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void insertProduct(String company) {

        String name = "土豆";
        int price = 10;

        HashMap map = new HashMap();
        map.put("name", name);

        ArrayList list = new ArrayList();
        list.add(company);

        String result = "Insert: " + productService.saveProduct(new ProductDTO(map, list, price), price);

        System.out.println(result);

    }

}
