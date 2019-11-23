package src;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class Fibonacci {

    public static MyBigIntegers bigFibLoop(int x) {
        // temp values to store in place
        MyBigIntegers twoPrevious = new MyBigIntegers("0");
        MyBigIntegers previous = new MyBigIntegers("1");
        MyBigIntegers current = new MyBigIntegers();
        // exit if fib will be 0
        if (x == 0)
            return twoPrevious;

        // loop until we reach the target number x while continually adding up
        // the previous two numbers in the sequence and continually replacing
        // each of the values after they are used
        for (int i = 2; i <= x; i++) {
            current = twoPrevious.Plus(previous);
            twoPrevious = previous;
            previous = current;
        }
        // return the final result
        return previous;
    }

    public static long fibLoop(int x) {
        // temp values to store in place
        long twoPrevious = 0, previous = 1, current;
        // exit if fib will be 0
        if (x == 0)
            return twoPrevious;

        // loop until we reach the target number x while continually adding up
        // the previous two numbers in the sequence and continually replacing
        // each of the values after they are used
        for (int i = 2; i <= x; i++) {
            current = twoPrevious + previous;
            twoPrevious = previous;
            previous = current;
        }
        // return the final result
        return previous;
    }

    ////////////////////////////////////////////////////////////////////////
    public static MyBigIntegers bigFibMatrix(long x) {

        MyBigIntegers F[][] = new MyBigIntegers[][] { { new MyBigIntegers("1"), new MyBigIntegers("1") },
                { new MyBigIntegers("1"), new MyBigIntegers("0") } };

        if (x == 0) {
            return new MyBigIntegers("0");
        }

        bigFibMatrixWorker(F, x - 1);

        return F[0][0];
    }

    static void bigMultiply(MyBigIntegers[][] F, MyBigIntegers[][] M) {
        // do the big multiplication
        MyBigIntegers Sub1 = new MyBigIntegers();
        MyBigIntegers Sub2 = new MyBigIntegers();

        Sub1 = F[0][0].Times(M[0][0]);
        Sub2 = F[0][1].Times(M[1][0]);
        MyBigIntegers x = Sub1.Plus(Sub2);
        MyBigIntegers y = (F[0][0].Times(M[0][1])).Plus(F[0][1].Times(M[1][1]));
        MyBigIntegers z = (F[1][0].Times(M[0][0])).Plus(F[1][1].Times(M[1][0]));
        MyBigIntegers w = (F[1][0].Times(M[0][1])).Plus(F[1][1].Times(M[1][1]));

        F[0][0] = x;
        F[0][1] = y;
        F[1][0] = z;
        F[1][1] = w;
    }

    static void bigFibMatrixWorker(MyBigIntegers[][] F, long x) {
        if (x <= 1)
            return;

        MyBigIntegers M[][] = new MyBigIntegers[][] { { new MyBigIntegers("1"), new MyBigIntegers("1") },
                { new MyBigIntegers("1"), new MyBigIntegers("0") } };

        bigFibMatrixWorker(F, x / 2);
        bigMultiply(F, F);

        if (x % 2 != 0) {
            bigMultiply(F, M);
        }
    }

    ////////////////////////////////////////////////////////////////////////
    public static long fibMatrix(long x) {
        // create new matrix of {{1, 1} {1, 0}}
        long F[][] = new long[][] { { 1, 1 }, { 1, 0 } };
        // get out if x == 0
        if (x == 0)
            return 0;
        // the function to put the matrix to the power of x
        fibMatrixWorker(F, x - 1);
        // return the result
        return F[0][0];
    }

    /*
     * Helper function that multiplies 2 matrices F and M of size 2*2, and puts the
     * multiplication result back to F[][] This is what does our matrix
     * multiplication for us
     */
    static void multiply(long F[][], long M[][]) {
        // do the multiplication
        long x = F[0][0] * M[0][0] + F[0][1] * M[1][0];
        long y = F[0][0] * M[0][1] + F[0][1] * M[1][1];
        long z = F[1][0] * M[0][0] + F[1][1] * M[1][0];
        long w = F[1][0] * M[0][1] + F[1][1] * M[1][1];

        // repopulate the matrix
        F[0][0] = x;
        F[0][1] = y;
        F[1][0] = z;
        F[1][1] = w;
    }

    /*
     * The actual worker function that calculates F[][] raise to the power n and
     * puts the result in F[][]
     */
    static void fibMatrixWorker(long F[][], long x) {
        // base case to exit if 1 || 0
        if (x <= 1)
            return;

        // new matrix of the same value to use for our multiplication
        long M[][] = new long[][] { { 1, 1 }, { 1, 0 } };

        // call the function recursively in half, then multiply the matrices
        fibMatrixWorker(F, x / 2);
        multiply(F, F);

        // if the number x is odd, multiply one last time to get the
        // correct answer
        if (x % 2 != 0) {
            multiply(F, M);
        }
    }

    ////////////////////////////////////////////////////////////////////////
    static long fibFormula(long x) {
        double phi = (1 + Math.sqrt(5)) / 2;
        return (long) Math.round(Math.pow(phi, x) / Math.sqrt(5));
    }

    static BigInteger fibFormulaBig(long x) {
        BigDecimal partPhi = new BigDecimal(1 / Math.sqrt(5));
        BigDecimal firstRoot = new BigDecimal((1 + Math.sqrt(5)) / 2);
        BigDecimal secondRoot = new BigDecimal((1 - Math.sqrt(5)) / 2);

        return (firstRoot.pow((int) x).multiply(partPhi)).subtract(partPhi.multiply(secondRoot.pow((int) x)))
                .toBigInteger();
    }

}