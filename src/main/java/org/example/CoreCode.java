package org.example;

/**
 * Enum representing various core codes.
 * Does not include core goals.
 */
public enum CoreCode {
    AHo("Philosophical and Theoretical Issues"),
    AHp("Arts and Literatures"),
    AHq("Nature of Languages"),
    AHr("Critical Creative Expression"),
    CCD("Diversities and Social Inequalities"),
    CCO("Our Common Future"),
    HST("Historical Analysis"),
    NS("Natural Sciences"),
    QQ("Quantitative Information"),
    QR("Quantitative and Formal Reasoning"),
    SCL("Social Analysis"),
    WC("College Writing"),
    WCd("Revision-Based Writing and Communication"),
    WCr("Discipline-Based Writing and Communication");

    private final String goal;

    CoreCode(String goal) {
        this.goal = goal;
    }

    /**
     * Returns the goal of the core code.
     * @return the goal of the core code.
     */
    public String getGoal() {
        return goal;
    }
}
