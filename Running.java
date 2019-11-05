public class Running {

    public static void main(String[] args) {
        MyBigIntegers A = new MyBigIntegers("100");
        MyBigIntegers B = new MyBigIntegers("-100");
        MyBigIntegers C = new MyBigIntegers();

        C = A.Plus(B);

        C.ToString();

    }
}