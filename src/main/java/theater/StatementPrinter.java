package theater;

import static theater.Constants.*;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

/**
 * This class generates a statement for a given invoice of performances.
 */
public class StatementPrinter {
    private static Map<String, Play> plays;
    private Invoice invoice;

    public StatementPrinter(Invoice invoice, Map<String, Play> plays) {
        this.invoice = invoice;
        this.plays = plays;
    }

    /**
     * Returns a formatted statement of the invoice associated with this printer.
     * @return the formatted statement
     * @throws IllegalArgumentException if one of the play types is not known
     */
    public String statement() throws IllegalArgumentException {
        int totalAmount = 0;
        int results = 0;
        final StringBuilder result = new StringBuilder("Statement for "
                + invoice.getCustomer() + System.lineSeparator());

        for (Performance performance : invoice.getPerformances()) {

            final int reslt = getThisAmount(performance, getPlay(performance));

            // add volume credits
            results = getVolumeCredits(performance, results);

            // print line for this order
            result.append(String.format("  %s: %s (%s seats)%n", getPlay(performance).getName(),
                    NumberFormat.getCurrencyInstance(Locale.US).format(reslt / PERCENT_FACTOR), performance.getAudience()));
            totalAmount += reslt;
        }
        result.append(String.format("Amount owed is %s%n", usd(totalAmount)));
        result.append(String.format("You earned %s credits%n", results));
        return result.toString();
    }

    private static String usd(int totalAmount) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(totalAmount / PERCENT_FACTOR);
    }

    private static int getVolumeCredits(Performance performance, int volumeCredits) {
        volumeCredits += Math.max(performance.getAudience() - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);
        // add extra credit for every five comedy attendees
        if ("comedy".equals(getPlay(performance).getType())) {
            volumeCredits += performance.getAudience() / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
        }
        return volumeCredits;
    }

    private static Play getPlay(Performance performance) {
        return plays.get(performance.getPlayID());
    }

    private static int getThisAmount(Performance performance, Play play) {
        final int thisAmount = getAmount(performance);
        return thisAmount;
    }

    private static int getAmount(Performance performance) {
        int thisAmount = 0;
        final Play play;
        play = getPlay(performance);
        switch (play.getType()) {
            case "tragedy":
                thisAmount = PASTORAL_BASE_AMOUNT;
                if (performance.getAudience() > Constants.TRAGEDY_AUDIENCE_THRESHOLD) {
                    thisAmount += TRAGEDY_OVER_BASE_CAPACITY_PER_PERSON * (performance.getAudience()
                            - TRAGEDY_AUDIENCE_THRESHOLD);
                }
                break;
            case "comedy":
                thisAmount = Constants.COMEDY_BASE_AMOUNT;
                if (performance.getAudience() > Constants.COMEDY_AUDIENCE_THRESHOLD) {
                    thisAmount += Constants.COMEDY_OVER_BASE_CAPACITY_AMOUNT
                            + (Constants.COMEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (performance.getAudience() - Constants.COMEDY_AUDIENCE_THRESHOLD));
                }
                thisAmount += Constants.COMEDY_AMOUNT_PER_AUDIENCE * performance.getAudience();
                break;
            default:
                throw new IllegalArgumentException(String.format("unknown type: %s", play.getType()));
        }
        return thisAmount;
    }
}
