package src;

public class Running {

    public static void main(String[] args) {

        Fibonacci fib = new Fibonacci();

        MyBigIntegers bigTest = fib.bigFibMatrix(75);
        long test = fib.fibMatrix(75);
        long bigFMResult = Long.parseLong(bigTest.getString());

        if (test == bigFMResult) {
            System.out.println("Success!!");
        } else {
            System.out.println("Broken... :(");
        }

    }

}