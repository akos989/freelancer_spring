package com.company;

public enum States {
    MASSACHUSETTS("Massachusetts", "MA", true),
    MICHIGAN("Michigan", "MI", false);

    private final String full;
    private final String abbr;
    private final boolean originalColony;

    States(String full, String abbr, boolean originalColony) {
        this.full = full;
        this.abbr = abbr;
        this.originalColony = originalColony;
    }

    public String getFullName() {
        return full;
    }

    public String getAbbreviatedName() {
        return abbr;
    }

    public boolean isOriginalColony() {
        return originalColony;
    }

    @Override
    public String toString() {
        return "States{" +
                "full='" + full + '\'' +
                ", abbr='" + abbr + '\'' +
                ", originalColony=" + originalColony +
                '}';
    }
}