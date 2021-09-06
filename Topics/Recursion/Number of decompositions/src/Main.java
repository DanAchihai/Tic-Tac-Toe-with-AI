import java.util.*;

class Main {
    final private static String CRLF = System.lineSeparator();
    private static ArrayList<String[]> allDecompositions= new ArrayList<>();
    private static ArrayList<String> decompositionsOfBase; // initiated for each base

    public static void main(String[] args) {
        // put your code here
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        if (n <= 0) {
            return;
        }
        createDecompositions(n);
        System.out.println(String.join(CRLF, allDecompositions.get(n - 1)));
    }

    private static int getDecompositionMaxInt(String decomposition) {
        // a decomposition is a String composed by integers in descending order
        // the function returns the first integer of the decomposition
        return Integer.parseInt(decomposition.replaceAll("\\s+.+", ""));
    }

    private static void prefixDecompositions(int maxInt,
                                             int base) {
        String prefixAddend = String.valueOf(maxInt);
        String[] toBePrefixed = allDecompositions.get(base - 1); // the base decompositions
        for (String s : toBePrefixed) {
            if (maxInt < getDecompositionMaxInt(s)) {
                break;
            }
            decompositionsOfBase.add(String.join(" ", prefixAddend, s));
        }
    }

    private static void createDecompositions(int base) {
        // parameter n must be strictly positive
        if (1 == base) {
            allDecompositions.add("1".split(CRLF)); // register the base 1 decompositions
            return;
        }

        if (base > allDecompositions.size() + 1) {
            createDecompositions(base - 1);
        }

        decompositionsOfBase = new ArrayList<>();
        for (int i = 1; i < base; i++) {
            prefixDecompositions(i, base - i);
        }
        decompositionsOfBase.add(String.valueOf(base));

        allDecompositions.add(decompositionsOfBase.toArray(new String[0]));
    }

}
