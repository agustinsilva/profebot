package ar.com.profebot.parser.container;

import java.util.ArrayList;
import java.util.List;

import ar.com.profebot.resolutor.utils.TreeUtils;

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

    // Para generar los temrinos invalidos
    TreeNode parentNode = null;
    Integer childIndex = null;

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
        node.setUnaryMinus(this.unaryMinus);
        node.setChangeGroup(this.changeGroup);
        node.setExplicitCoeff(this.explicitCoeff);

        if(this.getLeftNode() != null) {
            node.setLeftNode(this.getLeftNode().clone());
        }
        if(this.getRightNode() != null) {
            node.setRightNode(this.getRightNode().clone());
        }
        if ( this.args.size()>2) {
            List<TreeNode> otrosArgs = this.args.subList(2, this.args.size());
            if (this.getRightNode() != null && this.getLeftNode() != null) {
                for (TreeNode child : otrosArgs) {
                    if (child!=null) {
                        node.addChild(child.clone());
                    }
                }
            }
        }
        return node;
    }

    public void addChild(TreeNode node) {
        this.args.add(node);
    }

    public TreeNode cloneDeep(){
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

        if (TreeUtils.isNthRootTerm(this)){
            if (this.esProducto() && TreeUtils.isConstant(this.getLeftNode())){
                return this.getLeftNode().getIntegerValue();
            }else{
                return 1;
            }
        }else {
            return coefficient;
        }
    }
    public void setCoefficient(Integer coefficient) {
        this.coefficient = coefficient;
    }
    public Integer getExponent() {

        if (TreeUtils.isNthRootTerm(this)){
            if (this.esProducto()){
                if (this.getRightNode().esPotencia()){
                    return this.getRightNode().getRightNode().getIntegerValue();
                }else{
                    return 1; // Un producto sin potencia
                }
            }else if (this.esPotencia()){
                return this.getRightNode().getIntegerValue();
            }else{
                return 1;
            }
        }else if (TreeUtils.isSymbolFraction(this, false)) {
            return this.getLeftNode().getExponent();
        }else {
            return exponent;
        }
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
        return this.esMultiplicativo() || this.esPotencia();
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

        if (this.isList()){
            // Muestra la lista
            String list = "";
            int numChilds = 0;
            for(TreeNode child: this.getArgs()){
                if (child != null) {
                    list += child.toExpression() + ",";
                    numChilds++;
                }
            }
            if (numChilds == 1){
                list = list.substring(0, list.length() - 1);
            }else {
                list = "[" + list.substring(0, list.length() - 1) + "]";
            }
            return list;
        }
        // Si es un signo menos que afecta solo a un termino que no es aditivo, va sin parentesis
        else if (this.esResta() && this.getRightNode()==null && !this.getLeftNode().esAditivo()){
            return "-" + this.getContent().toExpression()  + "";
        }
        // Si es parentesis, o unaryMinus, envuelve
        else if (this.isParenthesis() || this.isUnaryMinus()){

            if (this.isUnaryMinus()){
                if (TreeUtils.isConstant(this.getContent())){
                    return TreeUtils.negate(this.getContent()).toExpression();
                }else{
                    return "-(" + this.getContent().toExpression()  + ")";
                }
            }else{
                return (this.isUnaryMinus()?"-":"") + "(" + this.getContent().toExpression()  + ")";
            }

        }else if (this.getArgs().size() > 2){
            // Si tiene mas de 2 hijos, se parsea distinto
            String childExpression = "";
            for(TreeNode child: this.getArgs()){
                if (childExpression.length() != 0){childExpression+=this.getValue();}
                childExpression+= getNodeExpression(child, false);
            }

            return childExpression.replaceAll("\\+\\-", "-");

        }else{

            // Evito que quede "+-"
            String exp = getNodeExpression(leftNode, false)
                     + this.getValue()
                     + getNodeExpression(this.getRightNode(), true);

            return exp.replaceAll("\\+\\-", "-");
        }
    }

    private String getNodeExpression(TreeNode treeNode, Boolean eshijoDerecho){

        if (treeNode == null) return "";
        String expression = treeNode.toExpression();

        // Los casos que van entre parentesis es cuando un "no aditivo" (*, ^ o R) es
        // padre de otro operador
        Boolean usaParentesis = (treeNode.esOperador() && this.esOperadorNoAditivo()) || this.esRaiz();

        // Caso especial, si los 2 son multiplicativo, va sin parentesis
        if (usaParentesis && treeNode.esProducto() && this.esProducto()){
            usaParentesis = false; // padre: * hijo: *
        }

        // Caso especial, si el padre es un signo menos, y el hijo DERECHO es un operador, se envuelve entre parentesis
        else if (this.esResta() && eshijoDerecho && treeNode.esOperador()) {
            usaParentesis = true; // padre: - hijo: operador. Ej: -(5*8+3)
        }
            // Caso especial, si el padre es una potencia, y el hijo izquierdo es una X con ieficiente != 1
        else if (this.esPotencia() && !eshijoDerecho &&
                ((treeNode.getCoefficient() != null && treeNode.getCoefficient() != 1) ||
                        (treeNode.getExponent() != null && treeNode.getExponent() != 1))){

            usaParentesis = true; // padre: ^ hijo: X c/Coef. Ej: (3X)^3
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

        if(TreeUtils.isSymbolFraction(this,false)) {
            this.getLeftNode().multiplyCoefficient(value);
        }
        else{
            this.setCoefficient(this.getCoefficient() * Integer.parseInt(value));
            updateValue();
        }
    }

    public void addExponent(String value) {

        this.setExponent(this.getExponent() * Integer.parseInt(value));
        updateValue();
    }

    public void updateValue(){
        updateValue(false, false);
    }

    public void updateValue(boolean explicitExp){
        updateValue(false, explicitExp);
    }

    /**
     * Se debería usar solo cuando cambia el exponente o el coeficiente de una X
     */
    //Actualiza el valor del nodo en funcion del coeficiente y el exponente por ejemplo si el
    //coeficiente es 3 y exponente es 2 cambiaria el valor a 3X^2
    public void updateValue(boolean explicitCoeff, boolean explicitExp){
        String coefficientString = "";
        if (coefficient != null && coefficient != 1 && coefficient != -1) {
            coefficientString = coefficient.toString();
        }else if (coefficient == -1){
            coefficientString = "-";
        }

            String newValue = coefficientString
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
        if (TreeUtils.isConstant(this)){
            return getIntegerValue();
        } else if (esProducto()){
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
            return (int)Math.pow((double)getLeftNode().getIntegerValue(), (double)getRightNode().getIntegerValue());
        }else if (isParenthesis()){
            return this.getChild(0).getOperationResult();
        }else if (isUnaryMinus()){
            return this.getChild(0).getOperationResult() * (-1);
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

    public static TreeNode createParenthesis(TreeNode node) {
        TreeNode newNode = new TreeNode("()");
        newNode.setLeftNode(node);
        return newNode;
    }

    public static TreeNode createList(List<TreeNode> nodes) {
        TreeNode newNode = new TreeNode("LIST");
        newNode.setArgs(nodes);
        return newNode;
    }

    public static TreeNode createPolynomialTerm(String x, TreeNode exponent, Integer coefficient) {

        String coefficientStr = (coefficient !=null && !coefficient.equals(1)? coefficient.toString() : "");
        return createOperator("^", new TreeNode(coefficientStr + x), exponent);
    }

    public static TreeNode createPolynomialTerm(String x, Integer exponent, Integer coefficient) {
        return createPolynomialTerm(x, exponent, coefficient, false);
    }

    public static TreeNode createPolynomialTerm(String x, Integer exponent, Integer coefficient, boolean explicitExp) {
        String exponentStr = (exponent==1 && !explicitExp? "":"^" + exponent.toString());
        String coefficientStr = "";
        if(coefficient != null && !coefficient.equals(1) && !coefficient.equals(-1)){
            coefficientStr = coefficient.toString();
        }else if(coefficient != null && coefficient.equals(-1)){
            coefficientStr = "-";
        }
        return new TreeNode(coefficientStr + x + exponentStr);
    }

    public static TreeNode createPolynomialFractionTerm(String x, Integer exponent, TreeNode fractionCoefficient) {

        Integer coefficientNumerator = fractionCoefficient.getLeftNode().getIntegerValue();
        Integer coefficientDenominator = fractionCoefficient.getRightNode().getIntegerValue();

        TreeNode symbolNode = createPolynomialTerm(x, exponent, coefficientNumerator);
        TreeNode denominatorNode = createConstant(coefficientDenominator);
        return createOperator("/", symbolNode, denominatorNode);
    }

    public TreeNode getChild(int i) {
        if (i > this.args.size()-1){return null;}
        return this.args.get(i);
    }

    public void setChild(int i, TreeNode node){
        if (i > this.args.size()-1){return;}
        try {
            this.args.set(i, node);
        }catch(Exception e){
            e.getMessage();
        }
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

    public TreeNode getContent() {
        return this.getChild(0);
    }

    public void setContent(TreeNode node) {
        this.setChild(0, node);
    }

    /**
     * Devuelve la base, si es una X devuelve
     * @return
     */
    public String getBase() {
        if (TreeUtils.isSymbol(this) || TreeUtils.isSymbolFraction(this, true)) {
            return "X";

        }else if (TreeUtils.isNthRootTerm(this)){
            if (this.esProducto()){
                if (this.getRightNode().esPotencia()){
                    return this.getRightNode().getBase();
                }else{
                    return this.getRightNode().toExpression();
                }
            }else if (this.esPotencia()){
                return this.getLeftNode().toExpression();
            }else{
                return this.toExpression();
            }
        }else{
            return this.toExpression();
        }
    }

    public void assignParentData(TreeNode parentNode, Integer childIndex){
        this.parentNode = parentNode;
        this.childIndex = childIndex;

        // Y asigna a sus hijos
        for( int i =0; i < this.getArgs().size(); i++){
            TreeNode child = this.getChild(i);
            if (child != null){
                child.assignParentData(this, i);
            }
        }
    }

    public TreeNode getParentNode() {
        return parentNode;
    }

    public Integer getChildIndex() {
        return childIndex;
    }

}
