package theater;

/**
 * Class built for plays.
 */
public class Play {

    private String name;
    private String type;

    public Play(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Play() {
    }

    /**
     * Getter for name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for name.
     * @return the name
     */
    public String getType() {
        return type;
    }
}
