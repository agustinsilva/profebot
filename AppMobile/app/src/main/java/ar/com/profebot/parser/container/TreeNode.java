package ar.com.profebot.parser.container;

public class TreeNode {
    private String value;
    private Integer coefficient;
    private Integer exponent;
    private TreeNode leftNode;
    private TreeNode rightNode;

    public TreeNode(String value) {
        super();
        this.coefficient = 1; // Por defecto
        this.exponent = 1; // Por defecto
        setValue(value);
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
        updateVariableValues(); // Si es X, actualiza valor, exponente y coeficiente
    }
    public Integer getCoefficient() {
        return coefficient;
    }
    public void setCoefficient(Integer coefficient) {
        this.coefficient = coefficient;
    }
    public Integer getExponent() {
        return exponent;
    }
    public void setExponent(Integer exponent) {
        this.exponent = exponent;
    }
    public TreeNode getLeftNode() {
        return leftNode;
    }
    public void setLeftNode(TreeNode leftNode) {
        this.leftNode = leftNode;
    }
    public TreeNode getRightNode() {
        return rightNode;
    }
    public void setRightNode(TreeNode rightNode) {
        this.rightNode = rightNode;
    }

    @Override
    public String toString() {
        return "TreeNode [value=" + value + "]";
    }

    public Boolean esAditivo(){
        return "+".equals(this.getValue()) || "-".equals(this.getValue());
    }

    public Boolean esProducto(){
        return "*".equals(this.getValue());
    }

    public Boolean esMultiplicativo(){
        return "*".equals(this.getValue()) || "/".equals(this.getValue());
    }

    public Boolean esPotencia(){
        return "^".equals(this.getValue());
    }

    public Boolean esRaiz(){
        return "R".equals(this.getValue());
    }

    public Boolean esOperadorNoAditivo(){
        return this.esMultiplicativo() || this.esPotencia() || this.esRaiz();
    }

    public Boolean esOperador(){
        return this.esOperadorNoAditivo() || this.esAditivo();
    }

    public String toExpression() {
        TreeNode leftNode = this.esRaiz()?null:this.getLeftNode(); // Las raices solo tienen termino derecho
        return getNodeExpression(leftNode)  + this.getValue()  + getNodeExpression(this.getRightNode());
    }

    private String getNodeExpression(TreeNode treeNode){

        if (treeNode == null) return "";
        String expression = treeNode.toExpression();

        // Los casos que van entre parentesis es cuando un "no aditivo" (*, ^ o R) es
        // padre de otro operador
        Boolean usaParentesis = (treeNode.esOperador() && this.esOperadorNoAditivo()) || this.esRaiz();

        // Caso especial, si los 2 son multiplicativo, va sin parentesis
        if (usaParentesis && treeNode.esProducto() && this.esProducto()){
            usaParentesis = false; // padre: * hijo: *
        }
        if (usaParentesis){
            expression = "(" + expression + ")";
        }
        return expression;
    }

    private void updateVariableValues(){

        if (this.value == null || !this.value.contains("X")){return;}

        // Extraigo coeficiente  y exponente
        int posX = this.value.indexOf("X");
        if (posX > 0){
            // Existe coeficiente, sino estaria en la primer posicion
            String coefficientString = this.value.substring(0, posX);
            if ("-".equals(coefficientString)){coefficientString = "-1";}
            setCoefficient(Integer.parseInt(coefficientString));
        }
        if (posX < this.value.length()-1){
            // Existe exponente, sino estaria en la ultima posicion
            // El exponente viene despues del X y ^
            setExponent(Integer.parseInt(this.value.substring(posX + 2)));
        }
    }
}
