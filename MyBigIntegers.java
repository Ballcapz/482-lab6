import java.util.Arrays;

import sun.util.logging.resources.logging;

public class MyBigIntegers {
    static long defaultBase = 10;
    long base = defaultBase;
    public long[] arr;
    int sign = 1;
    int len = 50;

    // constructors
    public MyBigIntegers() {
        arr = new long[len];
    }

    public MyBigIntegers(String s) {
        if (s.charAt(0) == '-') {
            sign = -1;
            s = s.substring(1);
        } else {
            sign = 1;
        }
        len = s.length();
        arr = new long[len];
        // if string is 12345 ,
        // store it as 54321
        for (int i = 0; i < len; i++) {
            arr[i] = Long.parseLong(s.charAt(len - i - 1) + "");
        }
    }

    private MyBigIntegers(long arr[], long base) {
        this.arr = arr;
        this.base = base;
        this.len = arr.length;
    }

    public void ToString() {
        MyBigIntegers numToPrint = truncate(this);
        String stringToPrint = "";

        if (this.sign == -1) {
            stringToPrint = "-";
        }

        for (int i = numToPrint.len - 1; i >= 0; i--) {
            stringToPrint += numToPrint.arr[i] + "";
        }

        System.out.println(stringToPrint);
    }

    // addition
    public MyBigIntegers Plus(MyBigIntegers x) {
        MyBigIntegers result = new MyBigIntegers();
        // normal addition
        if (this.sign == 1 && x.sign == 1) {
            // ADD
            result = Add(this, x);
            result.sign = 1;
            return result;
        } else if (this.sign == -1 && x.sign == 1) {
            // x - this
            result = x.Minus(this);
        } else if (this.sign == 1 && x.sign == -1) {
            // this - x
            result = this.Minus(x);
        } else {
            result = Add(this, x);
            result.sign = -1;
        }
        return result;
    }

    private static MyBigIntegers Add(MyBigIntegers a, MyBigIntegers b) {
        MyBigIntegers result;
        int size = a.len > b.len ? a.len + 1 : b.len + 1;

        long[] resArr = new long[size];

        int i = 0;
        long carry = 0, sum = 0;
        while (i < a.len && i < b.len) {
            sum = a.arr[i] + b.arr[i] + carry;
            resArr[i] += sum % a.base;
            carry = sum / a.base;
            i++;
        }
        while (i < a.len) {
            sum = a.arr[i] + carry;
            resArr[i] += sum % a.base;
            carry = sum / a.base;
            i++;
        }
        while (i < b.len) {
            sum = b.arr[i] + carry;
            resArr[i] += sum % a.base;
            carry = sum / a.base;
            i++;
        }
        if (carry != 0) {
            resArr[i] += carry;
        }
        result = new MyBigIntegers(truncate(resArr), a.base);

        return truncate(result);
    }

    public MyBigIntegers Minus(MyBigIntegers x) {
        MyBigIntegers result = new MyBigIntegers();
        // some logic to check signs and such here in the future
        if (this.len > x.len) {
            MyBigIntegers equalizedX = equalize(x, this.len - x.len);
            result = Subtract(this, equalizedX);
        } else if (this.len < x.len) {
            MyBigIntegers equalized = equalize(this, x.len - this.len);
            result = Subtract(equalized, x);
        } else {
            result = Subtract(this, x);
        }
        return result;
    }

    // a - b
    public static MyBigIntegers Subtract(MyBigIntegers a, MyBigIntegers b) {
        int size = a.len > b.len ? a.len : b.len;
        MyBigIntegers result;
        long[] resArr = new long[size];
        long[] min = new long[a.len];
        System.arraycopy(a.arr, 0, min, 0, a.len);
        int i = 0;
        long borrow = a.base;
        while (i < a.len && i < b.len) {
            if (min[i] > b.arr[i]) {
                resArr[i] = min[i] - b.arr[i];
            } else {
                min[i] += borrow;
                --min[i + 1];
                resArr[i] = min[i] - b.arr[i];
            }
            i++;
        }
        while (i < a.len) {
            resArr[i] = min[i];
            i++;
        }

        result = new MyBigIntegers(resArr, a.base);
        result.sign = a.sign;
        return truncate(result);
    }

    // class sized helpers
    private static long[] truncate(long arr[]) {
        int length = arr.length;
        boolean isZero = true;
        // no zeros to truncate
        if (arr[length - 1] != 0) {
            return arr;
        }

        for (int i = length - 1; i > 0; i--) {
            if (arr[i] != 0) {
                isZero = false;
            }
            if (arr[i] == 0 && arr[i - 1] != 0) {
                return Arrays.copyOfRange(arr, 0, i);
            }
        }
        return isZero ? Arrays.copyOfRange(arr, 0, 1) : arr;
    }

    private static MyBigIntegers truncate(MyBigIntegers x) {
        x.arr = truncate(x.arr);
        x.len = x.arr.length;
        return x;
    }

    private static MyBigIntegers equalize(MyBigIntegers a, int zeros) {
        int totLength = a.len + zeros;
        long[] tmp = new long[totLength];
        for (int i = 0; i < totLength; i++) {
            if (i < a.len) {
                tmp[i] = a.arr[i];
            } else {
                tmp[i] = 0;
            }
        }

        return new MyBigIntegers(tmp, a.base);
    }
}