package ar.com.profebot.resolutor;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

import ar.com.profebot.resolutor.service.ResolutorService;

public class ResolutorServiceTest extends ResolutorService {

    @Test
    public void getFactorPairs_ok1() {
        List<Integer[]> factors = super.getFactorPairs(1,true);
        List<Integer[]> expected = new ArrayList<>();
        expected.add(new Integer[]{-1,-1});
        expected.add(new Integer[] {1,1});

        Assert.assertEquals(expected.size(), factors.size());
        for (int i=0; i <factors.size(); i++){
            Assert.assertArrayEquals(factors.get(i), expected.get(i));
        }
    }

    @Test
    public void getFactorPairs_ok2() {
        List<Integer[]> factors = super.getFactorPairs(5,true);
        List<Integer[]> expected = new ArrayList<Integer[]>();
        expected.add(new Integer[]{-1,-5});
        expected.add(new Integer[] {1,5});

        Assert.assertEquals(expected.size(), factors.size());
        for (int i=0; i <factors.size(); i++){
            Assert.assertArrayEquals(factors.get(i), expected.get(i));
        }

    }

    @Test
    public void getFactorPairs_ok3() {
        List<Integer[]> factors = super.getFactorPairs(12,true);
        List<Integer[]> expected = new ArrayList<Integer[]>();
        expected.add(new Integer[]{-3,-4});
        expected.add(new Integer[] {-2,-6});
        expected.add(new Integer[]{-1,-12});
        expected.add(new Integer[] {1,12});
        expected.add(new Integer[] {2,6});
        expected.add(new Integer[] {3,4});

        Assert.assertEquals(expected.size(), factors.size());
        for (int i=0; i <factors.size(); i++){
            Assert.assertArrayEquals(factors.get(i), expected.get(i));
        }
    }

    @Test
    public void getFactorPairs_ok4() {
        List<Integer[]> factors = super.getFactorPairs(-12,true);
        List<Integer[]> expected = new ArrayList<Integer[]>();
        expected.add(new Integer[]{-3,4});
        expected.add(new Integer[] {-2,6});
        expected.add(new Integer[]{-1,12});
        expected.add(new Integer[] {1,-12});
        expected.add(new Integer[] {2,-6});
        expected.add(new Integer[] {3,-4});

        Assert.assertEquals(expected.size(), factors.size());
        for (int i=0; i <factors.size(); i++){
            Assert.assertArrayEquals(factors.get(i), expected.get(i));
        }
    }

    @Test
    public void getFactorPairs_ok5() {
        List<Integer[]> factors = super.getFactorPairs(15,true);
        List<Integer[]> expected = new ArrayList<Integer[]>();
        expected.add(new Integer[]{-3,-5});
        expected.add(new Integer[] {-1,-15});
        expected.add(new Integer[]{1,15});
        expected.add(new Integer[] {3,5});

        Assert.assertEquals(expected.size(), factors.size());
        for (int i=0; i <factors.size(); i++){
            Assert.assertArrayEquals(factors.get(i), expected.get(i));
        }
    }

    @Test
    public void getFactorPairs_ok6() {
        List<Integer[]> factors = super.getFactorPairs(36,true);
        List<Integer[]> expected = new ArrayList<Integer[]>();
        expected.add(new Integer[]{-6,-6});
        expected.add(new Integer[] {-4,-9});
        expected.add(new Integer[]{-3,-12});
        expected.add(new Integer[] {-2,-18});
        expected.add(new Integer[] {-1,-36});
        expected.add(new Integer[] {1,36});
        expected.add(new Integer[] {2,18});
        expected.add(new Integer[]{3,12});
        expected.add(new Integer[] {4,9});
        expected.add(new Integer[]{6,6});

        Assert.assertEquals(expected.size(), factors.size());
        for (int i=0; i <factors.size(); i++){
            Assert.assertArrayEquals(factors.get(i), expected.get(i));
        }
    }

    @Test
    public void getFactorPairs_ok7() {
        List<Integer[]> factors = super.getFactorPairs(49,true);
        List<Integer[]> expected = new ArrayList<Integer[]>();
        expected.add(new Integer[]{-7,-7});
        expected.add(new Integer[] {-1,-49});
        expected.add(new Integer[]{1,49});
        expected.add(new Integer[] {7,7});

        Assert.assertEquals(expected.size(), factors.size());
        for (int i=0; i <factors.size(); i++){
            Assert.assertArrayEquals(factors.get(i), expected.get(i));
        }
    }

    @Test
    public void getFactorPairs_ok8() {
        List<Integer[]> factors = super.getFactorPairs(1260,true);
        List<Integer[]> expected = new ArrayList<Integer[]>();
        expected.add(new Integer[]{-35,-36});
        expected.add(new Integer[] {-30,-42});
        expected.add(new Integer[]{-28,-45});
        expected.add(new Integer[] {-21,-60});
        expected.add(new Integer[]{-20,-63});
        expected.add(new Integer[] {-18,-70});
        expected.add(new Integer[]{-15,-84});
        expected.add(new Integer[] {-14,-90});
        expected.add(new Integer[]{-12,-105});
        expected.add(new Integer[] {-10,-126});
        expected.add(new Integer[]{-9,-140});
        expected.add(new Integer[] {-7,-180});
        expected.add(new Integer[]{-6,-210});
        expected.add(new Integer[] {-5,-252});
        expected.add(new Integer[] {-4,-315});
        expected.add(new Integer[]{-3,-420});
        expected.add(new Integer[] {-2,-630});
        expected.add(new Integer[] {-1,-1260});
        expected.add(new Integer[]{1,1260});
        expected.add(new Integer[] {2,630});
        expected.add(new Integer[]{3,420});
        expected.add(new Integer[] {4,315});
        expected.add(new Integer[]{5,252});
        expected.add(new Integer[] {6,210});
        expected.add(new Integer[]{7,180});
        expected.add(new Integer[] {9,140});
        expected.add(new Integer[]{10,126});
        expected.add(new Integer[] {12,105});
        expected.add(new Integer[]{14,90});
        expected.add(new Integer[] {15,84});
        expected.add(new Integer[]{18,70});
        expected.add(new Integer[] {20,63});
        expected.add(new Integer[]{21,60});
        expected.add(new Integer[] {28,45});
        expected.add(new Integer[] {30,42});
        expected.add(new Integer[] {35,36});


        Assert.assertEquals(expected.size(), factors.size());
        for (int i=0; i <factors.size(); i++){
            Assert.assertArrayEquals(expected.get(i), factors.get(i));
        }
    }

    @Test
    public void getFactorPairs_ok9() {
        List<Integer[]> factors = super.getFactorPairs(1234567891,true);
        List<Integer[]> expected = new ArrayList<Integer[]>();
        expected.add(new Integer[]{-1,-1234567891});
        expected.add(new Integer[] {1,1234567891});

        Assert.assertEquals(expected.size(), factors.size());
        for (int i=0; i <factors.size(); i++){
            Assert.assertArrayEquals(factors.get(i), expected.get(i));
        }
    }
}
