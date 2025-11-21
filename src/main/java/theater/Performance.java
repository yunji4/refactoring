package theater;

/**
 * Class representing a performance of a play.
 */
public class Performance {

    private String playID;
    private final int audience;

    public Performance(String playID, int audience) {
        this.playID = playID;
        this.audience = audience;
    }

    /**
     * Gets the play ID.
     * @return the play ID
     */
    public String getPlayID() {
        return playID;
    }

    /**
     * Getter for audiennce.
     * @return the number of audience
     */
    public int getAudience() {
        return audience;
    }
}
