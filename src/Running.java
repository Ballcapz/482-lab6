package src;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;
import java.util.function.Function;

public class Running {

    static ThreadMXBean bean = ManagementFactory.getThreadMXBean();

    /* define constants */
    static int MAXVALUE = 2000000000;
    static long MINVALUE = -2000000000;
    private static int numberOfTrials = 2048;
    private static int MAXINPUTSIZE = 2000000;
    private static int HALFINPUTSIZE = 40;
    private static int MININPUTSIZE = 1;
    private static int SIZEINCREMENT = 2;

    static Random rand = new Random();

    // lookup table for recursive dynamic fib algorithm
    private static long[] memo;

    // static int SIZEINCREMENT = 10000000; // not using this since we are doubling
    // the size each time
    private static String ResultsFolderPath = "C:\\Users\\Zach\\Documents\\School\\Fall 2019\\482 Algorithms\\logs_Lab6\\"; // pathname
                                                                                                                            // to
                                                                                                                            // results
                                                                                                                            // folder
    private static FileWriter resultsFile;
    private static PrintWriter resultsWriter;

    public static void main(String args[]) {

        // verifyMyBigIntegers();

        // for (int i = 0; i < MAXVALUE; i++) {
        // MyBigIntegers result = Fibonacci.bigFibLoop(i);

        // }

        runFullExperiment("numBits-fibLoopBig-1.txt", MAXINPUTSIZE);
        runFullExperiment("numBits-fibLoopBig-2.txt", MAXINPUTSIZE);
        runFullExperiment("numBits-fibLoopBig-3.txt", MAXINPUTSIZE);

    }

    // verification function that makes sure we get the correct fib number
    // Basically a unit test by hand
    private static void verifyCorrectFibonacciNumber(Function<Integer, Long> fibFunction) {
        int expected = 4181;

        long result = fibFunction.apply(19);
        System.out.println("Fib number is: " + result);
        if (result == expected) {
            System.out.println("Fib number is correct");
        } else {
            System.out.println("BROKEN !!!!!!");
        }
    }

    private static void runFullExperiment(String resultsFileName, int maxInputSize) {
        try {
            resultsFile = new FileWriter(ResultsFolderPath + resultsFileName);
            resultsWriter = new PrintWriter(resultsFile);

        } catch (Exception e) {

            System.out.println(
                    "*****!!!!!  Had a problem opening the results file " + ResultsFolderPath + resultsFileName);
            return; // not very foolproof... but we do expect to be able to create/open the file...

        }

        ThreadCpuStopWatch TrialStopwatch = new ThreadCpuStopWatch(); // for timing an individual trial

        resultsWriter.println("#N(Size)        AverageTime         ratio"); // # marks a comment in gnuplot
                                                                            // data

        resultsWriter.flush();

        /*
         * for each size of input we want to test: in this case starting small and
         * doubling the size each time
         */
        double previousTime = 0;

        for (int inputSize = MININPUTSIZE; inputSize <= maxInputSize; inputSize *= SIZEINCREMENT) {

            // progress message...

            System.out.println("Running test for input size " + inputSize + " ... ");

            /* repeat for desired number of trials (for a specific size of input)... */

            long batchElapsedTime = 0;

            /*
             * force garbage collection before each batch of trials run so it is not
             * included in the time
             */

            // System.out.println("Collecting the trash...");
            System.gc();

            // instead of timing each individual trial, we will time the entire set of
            // trials (for a given input size)

            // and divide by the number of trials -- this reduces the impact of the amount
            // of time it takes to call the

            // stopwatch methods themselves

            // BatchStopwatch.start(); // comment this line if timing trials individually

            MyBigIntegers result = new MyBigIntegers();
            // long result = 0;
            long batchCount = 0;
            // run the trials
            // System.out.println("Timing Each sort individually wo gc every time
            // forced...");
            System.out.print("    Starting trials for input size " + inputSize + " ... ");
            for (long trial = 0; trial < numberOfTrials; trial++) {

                // long[] testList = createAscendingList(inputSize);

                /*
                 * force garbage collection before each trial run so it is not included in the
                 * time
                 */
                // System.gc();

                // force presorted lists
                // quickSort(testList);
                // System.gc();

                // foreach trial generate two new random numbers of input size (N)
                int bitLength = (int) (Math.floor(log2(inputSize)) + 1);
                // long[] num1 = new long[bitLength];
                // for (int i = 0; i < bitLength; i++) {
                // num1[i] = rand.nextInt(10);
                // }

                // MyBigIntegers testFibX = new MyBigIntegers(num1, 10);

                TrialStopwatch.start(); // *** uncomment this line if timing trials individually

                /* run the function we're testing on the trial input */

                ///////////////////////////////////////////
                /* DO BIDNESS */
                /////////////////////////////////////////

                result = Fibonacci.bigFibLoop(bitLength);

                ///////////////////////////////////////////
                /* END DO BIDNESS */
                /////////////////////////////////////////

                batchElapsedTime = batchElapsedTime + TrialStopwatch.elapsedTime(); // *** uncomment this line if timing
                                                                                    // trials individually

            }

            // batchElapsedTime = BatchStopwatch.elapsedTime(); // *** comment this line if
            // timing trials individually

            double averageTimePerTrialInBatch = (double) batchElapsedTime / (double) numberOfTrials; // calculate the
                                                                                                     // average time per
                                                                                                     // trial in this
                                                                                                     // batch

            double doublingRatio = 0;
            if (previousTime > 0) {
                doublingRatio = averageTimePerTrialInBatch / previousTime;
            }

            previousTime = averageTimePerTrialInBatch;
            long avCount = batchCount / numberOfTrials;

            /* print data for this size of input */
            resultsWriter.printf("%10d  %18.2f  %18.1f  %18s\n", inputSize, averageTimePerTrialInBatch, doublingRatio,
                    result.getString()); // might
            // as
            // well
            // make
            // the
            // columns look nice

            resultsWriter.flush();

            System.out.println(" ....done.");

        }
    }

    //
    //
    // verificttion
    private static void verifyMyBigIntegers() {
        long a = 123, b = 321, c = 1976, d = 1861;
        MyBigIntegers x = new MyBigIntegers("123");
        MyBigIntegers y = new MyBigIntegers("321");
        MyBigIntegers z = new MyBigIntegers("1976");
        MyBigIntegers w = new MyBigIntegers("1861");

        long res1 = a + b;
        MyBigIntegers res2 = new MyBigIntegers();
        res2 = x.Plus(y);

        if (res1 == Long.parseLong(res2.getString())) {
            System.out.println("First addition Success");
        } else {
            System.out.println("First addition BROKEN");
        }

        res1 = a + c;
        res2 = x.Plus(z);

        if (res1 == Long.parseLong(res2.getString())) {
            System.out.println("Second addition Success");
        } else {
            System.out.println("Second addition BROKEN");
        }

        res1 = a + d;
        res2 = x.Plus(w);

        if (res1 == Long.parseLong(res2.getString())) {
            System.out.println("Third addition Success");
        } else {
            System.out.println("Third addition BROKEN");
        }

        res1 = c + d;
        res2 = z.Plus(w);

        if (res1 == Long.parseLong(res2.getString())) {
            System.out.println("Fourth addition Success");
        } else {
            System.out.println("Fourth addition BROKEN");
        }

        // MULTIPLICATIONS
        res1 = a * b;
        res2 = x.Times(y);
        if (res1 == Long.parseLong(res2.getString())) {
            System.out.println("First mulitplication Success");
        } else {
            System.out.println("First mulitplication BROKEN");
        }

        res1 = a * c;
        res2 = x.Times(z);
        if (res1 == Long.parseLong(res2.getString())) {
            System.out.println("Second mulitplication Success");
        } else {
            System.out.println("Second mulitplication BROKEN");
        }

        res1 = a * d;
        res2 = x.Times(w);
        if (res1 == Long.parseLong(res2.getString())) {
            System.out.println("Third mulitplication Success");
        } else {
            System.out.println("Third mulitplication BROKEN");
        }

        res1 = c * d;
        res2 = z.Times(w);
        if (res1 == Long.parseLong(res2.getString())) {
            System.out.println("Fourth mulitplication Success");
        } else {
            System.out.println("Fourth mulitplication BROKEN");
        }

        //
        // Too Large to Compare
        MyBigIntegers tree1 = new MyBigIntegers(
                "3333333333333333333333333333333333333333338888888888888888888888888888888888888888888888888888");
        MyBigIntegers help1 = new MyBigIntegers("333333333");

        res2 = tree1.Plus(help1);
        res2.ToString();

        MyBigIntegers tree2 = new MyBigIntegers("333333333333333333333");
        MyBigIntegers help2 = new MyBigIntegers("333333333333333333333");

        res2 = tree2.Plus(help2);
        res2.ToString();

        MyBigIntegers tree3 = new MyBigIntegers("99999999999999999999999999999999999999999999999999999999");
        MyBigIntegers help3 = new MyBigIntegers("1");

        res2 = tree3.Plus(help3);
        res2.ToString();

        res2 = help3.Plus(tree3);
        res2.ToString();

        MyBigIntegers tree4 = new MyBigIntegers("363636363636363636363636363636363636");
        MyBigIntegers help4 = new MyBigIntegers("636363636363636363636363636363636363");

        res2 = tree4.Plus(help4);
        res2.ToString();
    }

    public static long log2(long x) {
        return (long) (Math.log(x) / Math.log(2));
    }
}