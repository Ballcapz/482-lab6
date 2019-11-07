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

    // helper mainly to verify results
    public String getString() {
        MyBigIntegers numToRet = truncate(this);
        String stringToRet = "";

        if (this.sign == -1) {
            stringToRet = "-";
        }

        for (int i = numToRet.len - 1; i >= 0; i--) {
            stringToRet += numToRet.arr[i] + "";
        }

        return stringToRet;
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

    // subtraction
    public MyBigIntegers Minus(MyBigIntegers x) {
        MyBigIntegers result = new MyBigIntegers();

        // if the second value is larger than the number we are subtracting from
        if (this.len < x.len) {
            result.sign = -1;
            MyBigIntegers eqTh = equalize(x, x.len - this.len);
            result = Subtract(x, eqTh);
        } else {
            result.sign = 1;
            MyBigIntegers eqX = equalize(x, this.len - x.len);
            result = Subtract(this, eqX);
        }
        return result;
    }

    // a - b ( a always greater than b, b padded out)
    public static MyBigIntegers Subtract(MyBigIntegers a, MyBigIntegers b) {
        int size = a.len > b.len ? a.len : b.len;
        MyBigIntegers result;
        long[] resultArr = new long[size];
        long[] minuend;
        minuend = new long[a.len];
        System.arraycopy(a.arr, 0, minuend, 0, a.len);
        int i = 0;
        long borrow = a.base;
        while (i < a.len && i < b.len) {
            if (minuend[i] >= b.arr[i])
                resultArr[i] = minuend[i] - b.arr[i];
            else {
                minuend[i] += borrow;
                --minuend[i + 1];
                resultArr[i] = minuend[i] - b.arr[i];
            }
            i++;
        }
        while (i < a.len) {
            resultArr[i] = minuend[i];
            i++;
        }
        result = new MyBigIntegers(resultArr, a.base);
        return truncate(result);
    }

    // Multiplication
    public MyBigIntegers Times(MyBigIntegers x) {
        return Multiply(this, x);
    }

    public MyBigIntegers Multiply(MyBigIntegers a, MyBigIntegers b) {
        MyBigIntegers result = new MyBigIntegers();
        for (int i = 0; i < b.len; i++) {
            long carry = 0;
            long[] res = new long[a.len + 1];
            for (int j = 0; j < a.len; j++) {
                long product = b.arr[i] * a.arr[j];
                product += carry;
                carry = product / a.base;
                res[j] = product % a.base;
            }
            if (carry != 0) {
                res[a.len] = carry;
            }
            MyBigIntegers nextToAdd = new MyBigIntegers(rightShiftBy(res, i), a.base);
            result = Add(result, nextToAdd);
        }
        result.base = a.base;
        if (b.sign == -1 || a.sign == -1)
            result.sign = -1;

        return result;
    }

    public static long[] rightShiftBy(long[] arr, int times) {
        long[] res = new long[arr.length + times];
        for (int i = arr.length - 1; i >= 0; i--) {
            res[i + times] = arr[i];
        }
        while (times > 0) {
            times--;
            res[times] = 0;
        }
        return res;
    }

    // public MyBigIntegers TimesFaster(MyBigIntegers x) {
    // return MultiplyFaster(this, x);
    // }

    // // multiply (karatsuba version)
    // public MyBigIntegers MultiplyFaster(MyBigIntegers a, MyBigIntegers b) {
    // int size1 = a.len;
    // int size2 = b.len;
    // int max = Math.max(size1, size2);

    // if (max < 10) {
    // return Multiply(a, b);
    // }

    // // max / 2 rounded
    // max = (max / 2) + (max % 2);

    // // multiplier
    // long multiplier = (long)Math.pow(10, max);

    // // do the stuff
    // MyBigIntegers i = a / multiplier;
    // long j = a - (i * multiplier);
    // long k
    // long l

    // }

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