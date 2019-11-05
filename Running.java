public class Running {

    public static void main(String[] args) {
        MyBigIntegers A = new MyBigIntegers("1000");
        MyBigIntegers B = new MyBigIntegers("100");
        MyBigIntegers C = new MyBigIntegers();
        MyBigIntegers D = new MyBigIntegers();
        C = A.Plus(B);
        D = B.Minus(A);

        C.ToString();
        D.ToString();

    }
}