package shake_n_bacon;

import providedCode.DataCount;
import providedCode.DataCounter;
import providedCode.FileWordReader;
import providedCode.SimpleIterator;

import java.io.IOException;


/**
 * @author <name>
 * @UWNetID <uw net id>
 * @studentID <id number>
 * @email <email address>
 * <p/>
 * <p/>
 * This should be done using a *SINGLE* iterator. This means only 1
 * iterator being used in Correlator.java, *NOT* 1 iterator per
 * DataCounter (You should call dataCounter.getIterator() just once in
 * Correlator.java). Hint: Take advantage of DataCounter's method.
 * <p/>
 * Although you can share argument processing code with WordCount, it
 * will be easier to copy & paste it from WordCount and modify it here -
 * it is up to you. Since WordCount does not have states, making private
 * method public to share with Correlator is OK. In general, you are not
 * forbidden to make private method public, just make sure it does not
 * violate style guidelines.
 * <p/>
 * Make sure WordCount and Correlator do not print anything other than
 * what they are supposed to print (e.g. do not print timing info, input
 * size). To avoid this, copy these files into package writeupExperiment
 * and change it there as needed for your write-up experiments.
 */
public class Correlator {

    /*
     * Print error message and exit
     */
    private static void usage() {
        System.err
                .println("Usage: [-s | -o] <filename1> <filename2>");
        System.err.println("-s for hashtable with separate chaining");
        System.err.println("-o for hashtable with open addressing");
        System.exit(1);
    }

    private static void countWords(String file, DataCounter counter) {
        try {
            FileWordReader reader = new FileWordReader(file);
            String word = reader.nextWord();
            while (word != null) {
                counter.incCount(word);
                word = reader.nextWord();
            }
        } catch (IOException e) {
            System.err.println("Error processing " + file + " " + e);
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        // TODO: Compute this variance

        if (args.length != 3) {
            usage();
        }

        String firstArg = args[0].toLowerCase();
        DataCounter wordCount1 = null;
        DataCounter wordCount2 = null;
        if (firstArg.equals("-s")) {
            wordCount1 = new HashTable_SC(new StringComparator(),
                    new StringHasher());
            wordCount2 = new HashTable_SC(new StringComparator(),
                    new StringHasher());
        } else if (firstArg.equals("-o")) {
            wordCount1 = new HashTable_OA(new StringComparator(),
                    new StringHasher());
            wordCount2 = new HashTable_OA(new StringComparator(),
                    new StringHasher());
        } else {
            usage();
        }

        countWords(args[1], wordCount1);
        countWords(args[2], wordCount2);

        int totalWords1 = 0;

        // my one usage of .getIterator()
        SimpleIterator si = wordCount1.getIterator();
        DataCount[] data1 = new DataCount[wordCount1.getSize()];
        int count = 0;
        while (si.hasNext()) {
            data1[count] = si.next();
            totalWords1 += data1[count].count;
            count++;
        }

        int totalWords2 = 0;

        DataCount[] data2 = toArray(wordCount2.toString(), totalWords2);

        for (int i = 0; i < data1.length; i++) {

        }

        double variance = 0.0;
        // IMPORTANT: Do not change printing format. Just print the double.
        System.out.println(variance);
    }

    private static boolean inRange(int count, int totalWords) {
        double lowerBound = .0001;
        double upperBound = .01;
        double freq = count / (double) totalWords;
        return !(freq > upperBound || freq < lowerBound);
    }

    private static DataCount[] toArray(String s, int totalWords) {
        String[] data = s.split("\\(");
        DataCount[] dc = new DataCount[data.length];
        for (int i = 0; i < data.length; i++) {
            // data[i]: word, count)
            if (!data[i].isEmpty()) {
                String[] pair = data[i].split(",");
                // pair[0]:word
                // pair[1]: count)
                String word = pair[0];
                // trim space and closing parenthesis
                String count = pair[1].substring(1, pair[1].length() - 1);
                int iCount = Integer.parseInt(count);
                totalWords += iCount;
                dc[i] = new DataCount(word, iCount);
            }
        }
        return dc;
    }
}
