package opennomics.com.common.enums.QA;

public enum EnLineStringQAList {
	
	SMALLLENGTH("SL","SmallLength", 0),
	SELFENTITY("SE", "SelfEntity", 1),
	ENTITYDUPLICATED("ED", "EntityDuplicated", 2),
	ATTRIBUTEFIX("AF", "AttributeFix", 3),
	CONBREAK("CB","ConBreak", 4),
	CONOVERDEGREE("COV","ConOverDegree", 5),
	CONINTERSECTED("CI", "ConIntersected", 6),
	DUPLICATEDPOINT("DP", "DuplicatedPoint", 7),
	USELESSENTITY("UE", "UselessEntity", 8);
	
	String type;
	String typeName;
	int num;

	EnLineStringQAList(String type, String typeName, int num) {
		this.type = type;
		this.typeName = typeName;
		this.num = num;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public int getNum() {
		return num;
	}
	public void setAuth(int num) {
		this.num = num;
	}
}
