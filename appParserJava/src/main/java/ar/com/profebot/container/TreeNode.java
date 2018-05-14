package ar.com.profebot.container;

public class TreeNode {
	private String value;
	private Integer coefficient;
	private Integer exponent;
	private TreeNode leftNode;
	private TreeNode rightNode;
	
	public TreeNode(String value) {
		super();
		this.value = value;
		this.coefficient = 1;
		this.exponent = 1;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
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
		
		if (treeNode == null){ return "";};
		String expression = treeNode.toExpression();
		
		// Los casos que van entre parentesis es cuando un no aditivo es 
		// Hijo de un aditivo
		if ((treeNode.esAditivo() && this.esOperadorNoAditivo()) || this.esRaiz()){
			expression = "(" + expression + ")";
		}
		return expression;
		
	}
	
}
