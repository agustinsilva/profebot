package ar.com.profebot.resolutor.container;

import ar.com.profebot.parser.container.TreeNode;

public class CancelOutStatus {

    private TreeNode numerator;
    private TreeNode denominator;
    private Boolean hasChanged;

    public CancelOutStatus(TreeNode numerator, TreeNode denominator, Boolean hasChanged) {
        this.numerator = numerator;
        this.denominator = denominator;
        this.hasChanged = hasChanged;

    }

    public TreeNode getNumerator() {
        return numerator;
    }

    public void setNumerator(TreeNode numerator) {
        this.numerator = numerator;
    }

    public TreeNode getDenominator() {
        return denominator;
    }

    public void setDenominator(TreeNode denominator) {
        this.denominator = denominator;
    }

    public Boolean getHasChanged() {
        return hasChanged;
    }

    public void setHasChanged(Boolean hasChanged) {
        this.hasChanged = hasChanged;
    }
}
