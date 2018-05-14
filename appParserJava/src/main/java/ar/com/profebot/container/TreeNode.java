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
}
