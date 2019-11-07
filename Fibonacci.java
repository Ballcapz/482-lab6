public class Fibonacci {

    public MyBigIntegers fibLoop(int x) {
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
}