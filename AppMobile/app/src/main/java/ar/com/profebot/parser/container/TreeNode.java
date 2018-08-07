package ar.com.profebot.parser.container;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private String value;
    private Integer coefficient;
    private Integer exponent;
    private Boolean unaryMinus;
    private Integer changeGroup;
    private Boolean explicitCoeff;

  //  private TreeNode leftNode; Los reemplazo por la lista, para unificar criterios
  //  private TreeNode rightNode;
    private List<TreeNode> args; // Usado en achatamiento

    //Constructor de nodo.
    public TreeNode(String value) {
        super();
        this.unaryMinus = false;
        this.coefficient = 1; // Por defecto
        this.exponent = 1; // Por defecto
        this.explicitCoeff = false;
        this.args = new ArrayList<>();
        this.args.add(null); // Left node
        this.args.add(null); // Right node
        setValue(value);
    }

    //Realiza el clon de un nodo.
    public TreeNode clone(){
        TreeNode node = new TreeNode(this.getValue());
        node.setCoefficient(this.getCoefficient());
        node.setExponent(this.getExponent());
        if(this.getLeftNode() != null) {
            node.setLeftNode(this.getLeftNode().clone());
        }
        if(this.getRightNode() != null) {
            node.setRightNode(this.getRightNode().clone());
        }
        List<TreeNode> otrosArgs = this.args.subList(2,this.args.size());
        if(this.getRightNode() != null && this.getLeftNode() != null) {
            for (TreeNode child : otrosArgs) {
                node.addChild(child.clone());
            }
        }
        return node;
    }

    public void addChild(TreeNode node) {
        this.args.add(node);
    }

    public TreeNode cloneDeep(){
        // TODO Revisar si amerita redefinir el clone
        return this.clone();
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
        return getChild(0);
    }
    public void setLeftNode(TreeNode leftNode) {
        setChild(0, leftNode);
    }
    public TreeNode getRightNode() {
        return getChild(1);
    }
    public void setRightNode(TreeNode rightNode) {
        setChild(1, rightNode);
    }

    @Override
    public String toString() {
        return "value=" + value + "; exp= " + toExpression() ;
    }

    public Boolean esAditivo(){
        return esSuma() || esResta();
    }

    public Boolean esSuma(){
        return "+".equals(this.getValue());
    }

    public Boolean esResta(){
        return "-".equals(this.getValue());
    }

    public Boolean esProducto(){
        return "*".equals(this.getValue());
    }

    public Boolean esMultiplicativo(){
        return esProducto() || esDivision();
    }

    public Boolean esDivision(){
        return "/".equals(this.getValue());
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

    public Integer getIntegerValue(){
        try {
            return Integer.parseInt(this.getValue());
        }catch (Exception e){
            return null;
        }
    }

    public Double getDoubleValue(){
        try {
            return Double.parseDouble(this.getValue());
        }catch (Exception e){
            return null;
        }
    }

    public String toExpression() {
        TreeNode leftNode = this.esRaiz()?null:this.getLeftNode(); // Las raices solo tienen termino derecho

        // Si es parentesis, o unaryMinus, envuelve
        if (this.isParenthesis() || this.isUnaryMinus()){
            return (this.isUnaryMinus()?"-":"") + "(" + this.getChild(0).toExpression()  + ")";
        }else{
            return getNodeExpression(leftNode)  + this.getValue()  + getNodeExpression(this.getRightNode());
        }
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

    public void updateVariableValues(){

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

    public void multiplyCoefficient(String value) {

        this.setCoefficient(this.getCoefficient() * Integer.parseInt(value));
        updateValue();
    }

    public void addExponent(String value) {

        this.setExponent(this.getExponent() * Integer.parseInt(value));
        updateValue();
    }
    /**
     * Se debería usar solo cuando cambia el exponente o el coeficiente de una X
     */
    //Actualiza el valor del nodo en funcion del coeficiente y el exponente por ejemplo si el
    //coeficiente es 3 y exponente es 2 cambiaria el valor a 3X^2
    private void updateValue(){
        String newValue = (coefficient!=null && coefficient!=1?coefficient.toString(): "")
                            + "X"
                            + (exponent!=null && exponent!=1? "^" + exponent.toString(): "");

        this.setValue(newValue);

    }

    public static TreeNode createConstant(Integer constant) {
        return new TreeNode(constant.toString());
    }

    public static TreeNode createOperator(String operator, TreeNode leftNode, TreeNode rightNode) {
        TreeNode newNode = new TreeNode(operator);
        newNode.setLeftNode(leftNode);
        newNode.setRightNode(rightNode);
        return newNode;
    }

    public static TreeNode createOperator(String operator, TreeNode leftNode, TreeNode rightNode, TreeNode extraNode) {
        TreeNode newNode = createOperator(operator, leftNode, rightNode);
        newNode.addChild(extraNode);
        return newNode;
    }

    public Integer getOperationResult() {
        Integer result = 0;
        if (esProducto()){
            result = 1;
            for(TreeNode child: this.getArgs()){
                result *= child.getIntegerValue();
            }
            return result;

        }else if (esDivision()){
            return getLeftNode().getIntegerValue() / getRightNode().getIntegerValue();
        }else if (esSuma()){
            for(TreeNode child: this.getArgs()){
                result += child.getIntegerValue();
            }
            return result;
        }else if (esResta()){
            return getLeftNode().getIntegerValue() - getRightNode().getIntegerValue();
        }else if (esPotencia()){
            return getLeftNode().getIntegerValue() ^ getRightNode().getIntegerValue();
        }else if (isParenthesis()){
            return this.getChild(0).getOperationResult();
        }

        throw new UnsupportedOperationException("No existe el operador: " + getValue() + ", Parseando: " + this.toExpression());
    }

    public List<TreeNode> getArgs() {
        return args;
    }

    public void setArgs(List<TreeNode> args){
        this.args = args;
    }

    public static TreeNode createOperator(String operator, List<TreeNode> operands) {
        TreeNode newNode = new TreeNode(operator);
        newNode.setArgs(operands);
        return newNode;
    }

    public static TreeNode createUnaryMinus(TreeNode node) {
        TreeNode newNode = new TreeNode("-");
        newNode.setLeftNode(node);
        newNode.setUnaryMinus(true);
        return newNode;
    }

    // TODO createParenthesis: parsear mejor estoen el toExpresion
    public static TreeNode createParenthesis(TreeNode node) {
        TreeNode newNode = new TreeNode("()");
        newNode.setLeftNode(node);
        return newNode;
    }

    // TODO createList: revisar como se parsearia esto
    public static TreeNode createList(List<TreeNode> nodes) {
        TreeNode newNode = new TreeNode("LIST");
        newNode.setArgs(nodes);
        return newNode;
    }

    public static TreeNode createPolynomialTerm(String x, TreeNode exponent, Integer coefficient) {
        return createOperator("^", new TreeNode(coefficient.toString() + x), exponent);
    }

    public static TreeNode createPolynomialTerm(String x, Integer exponent, Integer coefficient) {
        return new TreeNode(coefficient.toString() + x + "^" + exponent.toString());
    }

    public TreeNode getChild(int i) {
        if (i > this.args.size()-1){return null;}
        return this.args.get(i);
    }

    public void setChild(int i, TreeNode node){
        if (i > this.args.size()-1){return;}
        this.args.set(i, node);
    }

    public Boolean isUnaryMinus() {
        return unaryMinus;
    }

    public void setUnaryMinus(Boolean unaryMinus) {
        this.unaryMinus = unaryMinus;
    }

    public void removeChild(int i) {
        if (i > this.args.size()-1){return;}
        this.args.remove(i);
    }

    public Integer getChangeGroup() {
        return changeGroup;
    }

    public void setChangeGroup(Integer changeGroup) {
        this.changeGroup = changeGroup;
    }

    public Boolean getExplicitCoeff() {
        return explicitCoeff;
    }

    public void setExplicitCoeff(Boolean explicitCoeff) {
        this.explicitCoeff = explicitCoeff;
    }

    public boolean isParenthesis() { return "()".equals(this.getValue());    }

    public boolean isList() { return "LIST".equals(this.getValue());    }

    public Integer getExponent(boolean defaultOne) {
        return getExponent() == null && defaultOne? 1: getExponent();
    }
}
