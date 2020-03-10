package algorithm.src;

// Rectangle

// D--CD_mid--C
// |          |
//AD_mid     BC_mid
// |          |
// A--AB_mid--B

public class Rectangle {
    Complex A, B, C, D;
    Complex AB_mid, BC_mid, CD_mid, AD_mid, MIDDLE;
    double area;
    InputSpace space;
    OutputSpace output;

    public Rectangle(Complex a, Complex b, Complex c, Complex d, InputSpace space, OutputSpace output) {
        A = a;
        B = b;
        C = c;
        D = d;
        this.space = space;
        this.output = output;

        // TODO: tick d determined by size of rect

        AB_mid = new Complex((B.re + A.re) / 2, A.im);
        BC_mid = new Complex(B.re, (C.im + B.im) / 2);
        CD_mid = new Complex((C.re + D.re) / 2, C.im);
        AD_mid = new Complex(D.re, (D.im + A.im) / 2);
        MIDDLE = new Complex((BC_mid.re + AD_mid.re) / 2, (CD_mid.im + AB_mid.im) / 2);

        area = (B.re - A.re) * (C.im - B.im);
    }

    public String toString() {
        String rectString = "";
        rectString += "D: " + D.toString() + "   ";
        rectString += "C: " + C.toString() + "\n";
        rectString += "A: " + A.toString() + "   ";
        rectString += "B: " + B.toString() + "\n";
        rectString += "area: " + area;
        return rectString;
    }

    public boolean checkInside(Function f) {
        // d - step of "integration"
        // ! zmniejszenie d zwiększa mocno windingNumber, a przez to niedokładność
        // ! "odległosci od inta"
        // TODO: czyli chyba jednak nie można go ot tak dodawać, trzeba "całkować"
        double d = 1;

        // Starting number: A
        double x = A.re;
        double y = A.im;

        double windingNumber = 0;

        while (x < B.re) {
            space.addPoint(new Complex(x, y));
            double prev = f.solveFor(new Complex(x, y)).phase();
            x += d;
            double now = f.solveFor(new Complex(x, y)).phase();
            windingNumber += now - prev;
            System.out.println(now + " było " + prev);
        }

        while (y < C.im) {
            space.addPoint(new Complex(x, y));
            double prev = f.solveFor(new Complex(x, y)).phase();
            y += d;
            double now = f.solveFor(new Complex(x, y)).phase();
            windingNumber += now - prev;
            System.out.println(now + " było " + prev);
        }

        while (x > D.re) {
            space.addPoint(new Complex(x, y));
            double prev = f.solveFor(new Complex(x, y)).phase();
            x -= d;
            double now = f.solveFor(new Complex(x, y)).phase();
            windingNumber += now - prev;
            System.out.println(now + " było " + prev);
        }

        while (y > A.im) {
            space.addPoint(new Complex(x, y));
            double prev = f.solveFor(new Complex(x, y)).phase();
            y -= d;
            double now = f.solveFor(new Complex(x, y)).phase();
            windingNumber += now - prev;
            System.out.println(now + " było " + prev);
        }

        windingNumber = windingNumber / (2 * Math.PI);

        System.out.println("Winding number: " + windingNumber + "\n\n");
        /*
         * Wydaje mi się, ze windingNumber powinien być CAŁKOWITY. Nasz algorytm zbiera
         * też taki z przecinkiem i wtedy sprawdza nieodpowiednie prostokąty. Jak się
         * przyjrzysz tym protokątom to niektóre mają niecałkowity niezerowy i w nich na
         * pewno zera nie ma. nearZero powinno sprawdzić czy wN jest "blisko" swojego
         * inta, ale trzeba to dopracować
         */

        return nearZero(windingNumber - (int) windingNumber);
    }

    // Checks if number is within 0.001 of zero
    boolean nearZero(double f) {
        double epsilon = 0.001;
        return ((-epsilon < f) && (f < epsilon));
    }

    Rectangle[] getChildren() {
        Rectangle[] children = new Rectangle[4];
        children[0] = new Rectangle(A, AB_mid, MIDDLE, AD_mid, space);
        children[1] = new Rectangle(AB_mid, B, BC_mid, MIDDLE, space);
        children[2] = new Rectangle(MIDDLE, BC_mid, C, CD_mid, space);
        children[3] = new Rectangle(AD_mid, MIDDLE, CD_mid, D, space);
        return children;
    }

    public void solveInside(Function f) {
        System.out.println("Checking rectangle: \n" + toString() + "\n");
        checkInside(f);
    }

}
