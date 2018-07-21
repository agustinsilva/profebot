package ar.com.profebot.Models;

public class PendingExercise {
    private String head;
    private String desc;

    public PendingExercise(String head, String desc) {
        this.head = head;
        this.desc = desc;
    }

    public String getHead() {
        return head;
    }

    public String getDesc() {
        return desc;
    }


}
