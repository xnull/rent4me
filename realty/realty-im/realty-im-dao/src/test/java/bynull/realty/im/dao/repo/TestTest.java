package bynull.realty.im.dao.repo;

import org.junit.Test;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

/**
 * @author Vyacheslav Petc (v.pets@oorraa.net)
 * @since 14.12.14.
 */
public class TestTest {


    private static Random rnd = new SecureRandom();

    @Test
    public void finderList(){
        ArrayList<Long> testData = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            testData.add(rnd.nextLong());
        }

        long start = System.currentTimeMillis();

        TreeSet<Long> ints = new TreeSet<>();
        for (Long value : testData) {
            ints.add(value);
        }


        long arrFiled = System.currentTimeMillis();
        System.out.println("Array has filled: " + (arrFiled - start));

        //Collections.sort(ints);
        System.out.println("sorted: " + (System.currentTimeMillis() - arrFiled));

        Iterator<Long> iterator = ints.iterator();
        for (int i = 0; i < 10; i++) {
            System.out.println(iterator.next());
        }
    }
}
