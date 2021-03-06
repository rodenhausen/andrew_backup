/**
 * 
 */
package edu.arizona.biosemantics.oto.common.ontologylookup.search.data;


/**
 * @author Hong Cui
 *
 */
public class EQProposals{
	EntityProposals entity;
	QualityProposals quality;
	private String sourceFile;
	String characterId;
	String characterText;
	String stateId;
	String stateText;
	private String type;
	float confidenceScore;
	
	/**
	 * 
	 */
	public EQProposals() {
	}
	
	public void setSource(String source){
		this.setSourceFile(source);
	}
	
	public void setCharacterId(String characterId){
		this.characterId = characterId;
	}
	
	public void setStateId(String stateId){
		this.stateId = stateId;
	}
	
	public void setEntity(EntityProposals entity){
		this.entity = entity;
	}
	
	public void setQuality(QualityProposals quality){
		this.quality = quality;
	}
	
	public void setStateText(String description){
		this.stateText = description;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	/*public void setRelatedEntity(Entity entity){
		this.relatedEntity = entity;
	}*/
	
	public String getSource(){
		return this.getSourceFile();
	}
	public String getCharacterId(){
		return this.characterId;
	}
	
	public String getStateId( ){
		return this.stateId  ;
	}
	
	public EntityProposals getEntity( ){
		return this.entity  ;
	}
	
	public QualityProposals getQuality( ){
		return this.quality  ;
	}
	
	public String getStateText(){
		return this.stateText;
	}
	
	public String getType() {
		// TODO Auto-generated method stub
		return this.type;
	}
	public String getCharacterText() {
		return characterText;
	}

	public void setCharacterText(String characterlabel) {
		this.characterText = characterlabel;
	}


	/*public Entity getRelatedEntity( ){
		return this.relatedEntity  ;
	}*/
	
	public float calculateConfidenceScore(){
		return this.entity.higestScore()*this.quality.higestScore();
	}
	
	public String toString(){
		
		return System.getProperty("line.separator")+
				"E proposals:"+this.entity.toString()+
				System.getProperty("line.separator")+
				"Q proposals:"+this.quality.toString();
		
	}
	
	public EQProposals clone(){
		EQProposals eq1 = new EQProposals();
		eq1.setCharacterId(this.getCharacterId());
		eq1.setEntity(this.getEntity());
		eq1.setQuality(this.getQuality());
		eq1.setSource(this.getSource());
		eq1.setStateId(this.getStateId()); //TODO: change it for states
		eq1.setStateText(this.getStateText());
		eq1.setType(this.getType());
		eq1.setCharacterText(this.getCharacterText());
		return eq1;
	}
	
	/**
	 * cross product between entity proposals and quality proposals
	 * @return
	 */
	public String EQStatementsInHTML(){
		StringBuffer sb = new StringBuffer();
		//TODO 
		return sb.toString();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public String getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}

}
