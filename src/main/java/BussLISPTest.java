import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

public class BussLISPTest {

    @Test
    public void testMain() {
        System.setIn(new ByteArrayInputStream((
                "(define last (lambda (lst) \n" +
                        "               (cond ((null (cdr lst)) (car lst))\n" +
                        "                     ((quote t) (last (cdr lst))))))\n" +
                        "(last (quote (a b c)))\n" +
                        "(define l (quote (((hotdogs))(and)(pickle)relish) ))\n" +
                        "(car (car l))\n" +
                        "(define * (lambda (a b)\n" +
                        "  (cond ((eq b 0) 0)\n" +
                        "        ((quote t) (+ a (* a (- b 1)))))))\n" +
                        "(* 12 20)\n" +
                        "(define add1 (lambda (a) (+ a 1)))\n" +
                        "(add1 10)\n" +
                        "(- 10 2)\n" +
                        "(< 10 2)\n" +
                        "(< 10 10)\n" +
                        "(> 10 10)\n" +
                        "(< 10 11)\n" +
                        "(< 3 3)\n" +
                        "(define remainder (lambda (a b)\n" +
                        "    (cond ((< a b) a)\n" +
                        "  ((quote t) (remainder (- a b) b)))))\n" +
                        "(remainder 13 3)\n" +
                        "(define ultimo (lambda (r t) (+ 1 t)))\n" +
                        "(ultimo 13 17)\n" +
                        "(define square (lambda (x) (* x x)))\n" +
                        "(square 4)\n" +
                        "(square (+ 2 5))\n" +
                        "(define sum-of-squares (lambda (x y)\n" +
                        "  (+ (square x) (square y))))\n" +
                        "(sum-of-squares 3 4)\n" +
                        "(define f (lambda (a)\n" +
                        "  (sum-of-squares (+ a 1) (* a 2))))\n" +
                        "(f 5)\n" +
                        "(define factorial (lambda (n)\n" +
                        "    (cond ((eq n 1) 1)\n" +
                        "          ((quote t) (* n (factorial (- n 1)))))))\n" +
                        "(factorial 3)\n" +
                        "(define gcd (lambda (a b)\n" +
                        "\t      (cond ((eq b 0) a)\n" +
                        "\t\t    ((quote t) (gcd b (remainder a b))))))\n" +
                        "(gcd 48 180)\n" +
                        "(define map (lambda (f lst)\n" +
                        "              (cond ((null lst) lst)\n" +
                        "                    ((quote t) (cons (f (car lst))\n" +
                        "                                     (map f (cdr lst)))))))\n" +
                                "(define numbers (cons 1 (cons 2 (cons 3 nil))))" +
                        "(map square numbers)\n"

        ).getBytes()));
        BussLISP.main(null);
    }
}
