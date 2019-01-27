import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

class BussLISPTest {

    @Test
    void testCountChange() {
        String s =
                "(define count-change (lambda (amount)\n" +
                "  (cc amount 5)))\n" +
                "(define first-denomination (lambda (kinds-of-coins)\n" +
                "  (cond ((eq kinds-of-coins 1) 1)\n" +
                "        ((eq kinds-of-coins 2) 5)\n" +
                "        ((eq kinds-of-coins 3) 10)\n" +
                "        ((eq kinds-of-coins 4) 25)\n" +
                "        ((eq kinds-of-coins 5) 50))))\n" +
                "(define cc (lambda (amount kinds-of-coins)\n" +
                "  (cond ((eq amount 0) 1)\n" +
                "        ((or (< amount 0) (eq kinds-of-coins 0)) 0)\n" +
                "        ((quote t) (+ (cc amount\n" +
                "                     (- kinds-of-coins 1))\n" +
                "                 (cc (- amount\n" +
                "                        (first-denomination kinds-of-coins))\n" +
                "                     kinds-of-coins))))))\n" +
                "\n" +
                "(define or (lambda (a b)\n" +
                "             (cond ((null a) b)\n" +
                "                   ((quote t) a))))\n" +
                "(count-change 100)\n";

        System.setIn(new ByteArrayInputStream(s.getBytes()));
        BussLISP.main(null);
    }

    @Test
    void testMain() {
        String s = "(define last (lambda (lst) \n" +
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
                "(map square numbers)\n" +
                "(+ (* 3 5) (- 10 6))\n" +
                "(+ (* 3 (+ (* 2 4) (+ 3 5))) (+ (- 10 7) 6))\n" +
                "(define abs (lambda (x)\n" +
                "              (cond ((< x 0) (- 0 x))\n" +
                "                    ((quote t) x))))\n" +
                "(abs -3)\n" +
                "\n" +
                "(cons (quote a) (quote b))\n" +
                "(define A (lambda (x y)\n" +
                "            (cond ((eq y 0) 0)\n" +
                "                  ((eq x 0) (* 2 y))\n" +
                "                  ((eq y 1) 2)\n" +
                "                  ((quote t) (A (- x 1)\n" +
                "                                (A x (- y 1)))))))\n" +
                "(A 2 3)\n";

        System.setIn(new ByteArrayInputStream(s.getBytes()));
        BussLISP.main(null);
    }
}
