package com.koala.linkfilterapp.linkfilterapi.service.impl;

import com.koala.linkfilterapp.linkfilterapi.api.link.entity.Link;
import com.koala.linkfilterapp.linkfilterapi.service.link.impl.LinkValidationServiceImpl;
import io.swagger.models.auth.In;
import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.Tuple;
import java.util.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LinkValidationServiceTest {
    @InjectMocks
    LinkValidationServiceImpl service;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    private int gcd(int x, int y) {
        if (x == 0) {
            return y;
        }
        if (y == 0) {
            return x;
        }
        if (x > y) {
            return gcd(x % y, y);
        } else if (x < y) {
            return gcd(x, y % x);
        } else {
            return x;
        }
    }

    private void possibleSums(int n) {
        if (n == 0) {
            System.out.println(0);
        }
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 1; i < n; i++) {
            calculateSum(result, new ArrayList<>(),0, 1, n);
        }
        System.out.println(result);
    }

    private void calculateSum(List<List<Integer>> resultList, List<Integer> currentList, int start, int curSum, int target) {
        if (target == curSum) {
            resultList.add(currentList);
        }

        for (int i = start; i < target; ++i) {
            int temp_sum = curSum + i;
            if (temp_sum <= target) {
                currentList.add(i);
                calculateSum(resultList, currentList, i, temp_sum, target);
                currentList.remove(currentList.size() - 1);
            } else {
                return;
            }
        }
    }

    // Recursive function to print all combinations of numbers from `i` to `n`
    // having sum `n`. The `index` denotes the next free slot in the output array `out`
    private void printCombinations(int i, int n, int[] out, int index, List<List<Integer>> result)
    {
        // if the sum becomes `n`, print the combination
        if (n == 0)
        {
            result.add(Arrays.stream(out).limit(index)
                    .boxed().collect(Collectors.toList()));
        }

        // start from the previous element in the combination till `n`
        for (int j = i; j <= n; j++)
        {
            // place current element at the current index
            out[index] = j;

            // recur with a reduced sum
            printCombinations(j, n - j, out, index + 1, result);
        }
    }

    private void findCombinations(int target, int curSum, int next, List<Integer> currentList, List<List<Integer>> result) {
        // if the sum becomes `n`, print the combination
        if (curSum == target) {
            result.add(currentList);
        }

        for (int j = 1; j < target; j++) {
            currentList = new ArrayList<>();

            currentList.add(j);
            int temp = j + curSum;
            findCombinations(curSum, target, j, currentList, result);
        }
    }

    private void print_all_sum_rec(
            int target,
            int current_sum,
            int start, ArrayList<ArrayList<Integer>> output,
            ArrayList<Integer> result) {

        if (target == current_sum) {
            output.add((ArrayList)result.clone());
        }

        for (int i = start; i < target; ++i) {
            int temp_sum = current_sum + i;
            if (temp_sum <= target) {

                result.add(i);
                print_all_sum_rec(target, temp_sum, i, output, result);
                result.remove(result.size() - 1);
            } else {
                return;
            }
        }
    }

    double findMaxPassRatio(List<Pair<Integer,Integer>> classes, int extraStudents) {
        int i = 0;
        double maxDelta = 0;
        Pair maxPair = null;
        for (Pair<Integer, Integer> classRoom : classes) {
            double calculatedDelta = calcDelta(classRoom, extraStudents);
            if (calculatedDelta > maxDelta) {
                maxDelta = calculatedDelta;
                maxPair = classRoom;
            }
        }

        classes.remove(maxPair);
        classes.add(new Pair<>((int) maxPair.getKey() + extraStudents, (int) maxPair.getValue() + extraStudents));

        return classes.stream().map(cls -> (double) cls.getKey() / (double) cls.getValue()).mapToDouble(Double::doubleValue).sum() / classes.size();
    }

    double calcDelta(Pair<Integer, Integer> classRoom, int extraStudents) {
        double currentRatio = (double) classRoom.getKey() /  (double) classRoom.getValue();
        return ((double) (classRoom.getKey() + extraStudents) / (classRoom.getValue() + extraStudents ) - currentRatio);
    }

    @Test
    public void shouldFindMaxPassRatio() {
        List<Pair<Integer,Integer>> classes = new ArrayList<>();
        classes.add(new Pair<>(1,2));
        classes.add(new Pair<>(3,5));
        classes.add(new Pair<>(2,2));
        System.out.println(findMaxPassRatio(classes, 2));
    }

    @Test
    public void shouldTest() {
        Link link = new Link();
//        List<List<Integer>> result = new ArrayList<>();
//        printCombinations(1,5, new int[5], 0, result);
//
//        ArrayList<ArrayList<Integer>> output = new ArrayList<ArrayList<Integer>>();
//        ArrayList<Integer> result = new ArrayList<Integer>();
//        print_all_sum_rec(5, 0, 1, output, result);
//        System.out.println(output);
//        String test = "STATION STCD";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://google.com")
//                        .pathSegment("BLAH").pathSegment("TEST");
//        System.out.println(test.substring(0,6));
//        System.out.println(test.substring(1,2));
//        System.out.println(builder.build().encode().toString());
//        Map<String, String> map = new HashMap<>();
//        Map<String, String> map2 = new HashMap<>();
//
//        char[] str = "Mr John Smith    ".toCharArray();
//        char[] res = "                     ".toCharArray();
//        int y = 0;
//        for(int i = 0; i < 13; i++) {
//            char cur = str[i];
//            if(cur == ' ') {
//                res[y] = '%';
//                res[y+1] = '2';
//                res[y+2] = '0';
//                y+=3;
//            } else {
//                res[y] = str[i];
//                y++;
//            }
//        }
//
//        System.out.println("Equals: " + gcd(9,6));
//        long time = new Date().getTime();
//        Date upper = new Date((time) + 72 * 60000L);
//        Date lower = new Date((time) - 72 * 60000L);
//        List<String> testList = new ArrayList<>();
//        testList.add("b");
//        testList.add("a");
//        System.out.println(testList.stream().sorted().collect(Collectors.toList()));
//        System.out.format(StringUtils.leftPad("", 3,'0'));
//        System.out.println(upper.before(new Date()) + " " + lower.after(new Date())  );
//        if (false || false) {
//
//        }
//        List<Integer> testList = new ArrayList<>();
//        testList.add(1);
//        testList.add(2);
//        testList.add(5);
//
//        System.out.println(testList.stream().sorted().collect(Collectors.toList()));
//        System.out.println(lower);
//        System.out.println(upper);

//        System.out.println(test.split(" ")[0]);
////        String date = "19970516";
////        String formatted = String.format("%s-%s-%s", date.substring(0,4), date.substring(4,6), date.substring(6,8));
////        System.out.println(formatted);
////
//        List<Link> l1 = new ArrayList<>();
//        List<Link> l2 = new ArrayList<>();
//
////        LongAdder tieCode = new LongAdder();
////        for (int i = 0; i < 4; i++) {
////            System.out.println(tieCode);
////            tieCode.add(50);
////        }
////
//        l1.add(link);
//        l1.add(link);
//        l2.addAll(l1);
//
//        System.out.println(l1);
//        System.out.println(l2);
//
//        link.setUrl("Url");
//
//        System.out.println(l1);
//        System.out.println(l2);


    }
}
