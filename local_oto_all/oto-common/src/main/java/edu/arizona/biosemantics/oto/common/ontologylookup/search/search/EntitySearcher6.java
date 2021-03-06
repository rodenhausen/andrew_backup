/**
 * 
 */
package edu.arizona.biosemantics.oto.common.ontologylookup.search.search;

import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import edu.arizona.biosemantics.oto.common.ontologylookup.search.OntologyLookupClient;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.data.CompositeEntity;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.data.EntityProposals;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.data.FormalConcept;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.data.FormalRelation;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.data.REntity;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.data.SimpleEntity;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.knowledge.Dictionary;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.utilities.Utilities;

/**
 * @author Hong Cui
 *
 * use wild cards 
 */
public class EntitySearcher6 extends EntitySearcher {
	private static final Logger LOGGER = Logger.getLogger(EntitySearcher6.class);   
	boolean debug = true;
	private static Hashtable<String, ArrayList<EntityProposals>> cache = new Hashtable<String, ArrayList<EntityProposals>>();
	private static ArrayList<String> nomatchcache = new ArrayList<String>();
	/**
	 * 
	 */
	public EntitySearcher6(OntologyLookupClient OLC){
		super(OLC);
	}

	/* (non-Javadoc)
	 * @see EntitySearcher#searchEntity(org.jdom.Element, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)
	 */
	/**
	 * test cases:patterns.xml_s413c5e3e-7941-44e3-8be6-b17e6193752e.xml (manual)
	 */
	@Override
	public ArrayList<EntityProposals> searchEntity(
			String entityphrase, String elocatorphrase,
			String originalentityphrase, String prep) {
		LOGGER.debug("EntitySearcher6: search '"+entityphrase+"[orig="+originalentityphrase+"]'");
		
		//search cache
		if(EntitySearcher6.nomatchcache.contains(entityphrase+"+"+elocatorphrase)) return null;
		if(EntitySearcher6.cache.get(entityphrase+"+"+elocatorphrase)!=null) return EntitySearcher6.cache.get(entityphrase+"+"+elocatorphrase);
		
		//still not find a match, remove the last term in the entityphrase, when what is then left is not just a spatial term 
		//"humeral deltopectoral crest apex" => "humeral deltopectoral crest"	
		//TODO "some part" of humerus; "some quality"
		//the last token could be a number (index)
		//Changed by Zilong:
		//enhanced entity format condition to exclude the spatial terms: in order to solve the problem that 
		//"rostral tubule" will match "anterior side" because rostral is synonymous with anterior

		String[] entitylocators = null;
		if(elocatorphrase.length()>0) entitylocators = elocatorphrase.split("\\s*,\\s*");

		ArrayList<SimpleEntity> entityls = new ArrayList<SimpleEntity>();
		//entityl.setString(elocatorphrase);
		if(entitylocators!=null) {
			ArrayList<FormalConcept> result = new TermSearcher(OLC).searchTerm(elocatorphrase, "entity"); //should it call EntitySearcherOriginal? decided not to.
			if(result!=null){
				//entityl = result;
				LOGGER.debug("search for locator '"+elocatorphrase+"' found match: ");
				for(FormalConcept fc: result){
					entityls.add((SimpleEntity)fc);
					LOGGER.debug(".."+fc.toString());
				}
			}else{ //entity locator not matched
				LOGGER.debug("search for locator '"+elocatorphrase+"' found no match");
			}
		}

		String aentityphrase = entityphrase.replaceFirst("^\\(\\?:", "").replaceFirst("\\)$", "");				
		String[] tokens = aentityphrase.split("\\s+"); //(?:(?:humerus|humeral) (?:shaft))
		if(tokens.length>=2){ //to prevent "rostral tubule" from entering the subsequent process 
			String shortened = aentityphrase.substring(0, aentityphrase.lastIndexOf(" ")).trim();		

			if(!shortened.matches(".*?\\b("+Dictionary.spatialtermptn+")$")){
				//SimpleEntity sentity = (SimpleEntity) new TermSearcher().searchTerm(shortened, "entity");
				//search shortened and other strings with the same starting words
				ArrayList<FormalConcept> shortentities = new TermSearcher(OLC).searchTerm(shortened, "entity");
				if(shortentities!=null){
					LOGGER.debug("search for entity '"+shortened+"' found match, forming proposals...");
					//construct anatomicalentity
					SimpleEntity anatomicalentity = Dictionary.anatomicalentity;
					anatomicalentity.setString(aentityphrase.substring(aentityphrase.lastIndexOf(" ")).trim());
					//construct relation
					FormalRelation rel =  Dictionary.partof;
					rel.setConfidenceScore((float)1.0);

					EntityProposals ep = new EntityProposals(); 
					ep.setPhrase(originalentityphrase);
					boolean found = false;
					/*for(FormalConcept sentityfc: shortentities){
						//if sentity part_of entityl holds, then sentity's conf score = 1 and return the result
						SimpleEntity sentity = (SimpleEntity)sentityfc;
						if(sentity.isOntologized() && entityls!=null){
							for(FormalConcept fc: entityls){
								SimpleEntity entityl = (SimpleEntity)fc;
								if(SearchMain.elk!=null && SearchMain.elk.isSubclassOfWithPart(entityl.getClassIRI(), sentity.getClassIRI())){
									LOGGER.debug("'"+entityl.getLabel() +"' and '"+sentity.getLabel() + "' are related, increase confidence score");
									found = true;
									sentity.setConfidenceScore(1f);
									CompositeEntity centity = new CompositeEntity();
									centity.addEntity(anatomicalentity);//anatomical entity ...								
									centity.addParentEntity(new REntity(rel, sentity)); // part of sentity ...
									centity.addParentEntity(new REntity(rel, entityl)); //part of entityl
									//ep.setPhrase(sentity.getString());
									ep.add(centity);//add one proposal with anatomical entity
									LOGGER.debug("add a proposal with anatomical entity:"+centity.toString());
								}
							}
						}
					}*/
					if(found){
						ArrayList<EntityProposals> entities = new ArrayList<EntityProposals>();
						//entities.add(ep);
						Utilities.addEntityProposals(entities, ep);
						LOGGER.debug("EntitySearcher6 returns:");
						for(EntityProposals aep: entities){
							LOGGER.debug("..EntityProposals: "+aep.toString());
						}
						
						//caching
						if(entities==null) EntitySearcher6.nomatchcache.add(entityphrase+"+"+elocatorphrase);
						else EntitySearcher6.cache.put(entityphrase+"+"+elocatorphrase, entities);
						
						return entities;
					}
					//else, record results that meet certain criteria
					LOGGER.debug("entity and entity locator (if exists) are not related");
					ArrayList<EntityProposals> entities = null;
					found = false;
					for(FormalConcept sentityfc: shortentities){				
						SimpleEntity sentity = (SimpleEntity)sentityfc;
						//consider only the matches that are one word longer and don't have of/and in the labels
						String label = sentity.getLabel();
						String added = label.replaceFirst(shortened, "").trim();
						if(label.indexOf(" of ")>=0 || label.indexOf(" and ")>=0 || added.indexOf(" ")>0) continue;

						if(sentity.getId().compareTo(Dictionary.mcorganism)==0){
							//too general "body scale", try to search for "scale"
							//TODO: multi-cellular organism is too general a syn for body. "body" could mean something more restricted depending on the context.
							//TODO: change labels to ids
						}

						//entity
						//if(entityl.getString().length()>0){
						if(elocatorphrase.length()>0){
							for(FormalConcept fc: entityls){
								found = true;
								SimpleEntity entityl = (SimpleEntity)fc;
								//relation & entity locator
								CompositeEntity centity = new CompositeEntity();
								centity.addEntity(anatomicalentity);
								centity.addParentEntity(new REntity(rel, sentity));
								centity.addParentEntity(new REntity(rel, entityl));
								centity.setString(originalentityphrase);
								//ep.setPhrase(sentity.getString());
								//ep.setPhrase(originalentityphrase);
								ep.add(centity); //add one
								LOGGER.debug("add a proposal with anatomical entity:"+centity.toString());
								/*centity = new CompositeEntity();
								centity.addEntity(sentity);								
								centity.addParentEntity(new REntity(rel, entityl));
								centity.setString(originalentityphrase);
								ep.add(centity); //add the other	
								LOGGER.debug("add a proposal without anatomical entity:"+centity.toString());*/
								
							}
						}else{
							//EntityProposals entities = new EntityProposals();
							found = true;
							CompositeEntity centity = new CompositeEntity();
							centity.addEntity(anatomicalentity);
							centity.addParentEntity(new REntity(rel, sentity));
							centity.setString(originalentityphrase);
							//ep.setPhrase(sentity.getString());
							ep.setPhrase(originalentityphrase);
							ep.add(centity); //add one
							LOGGER.debug("add a proposal with anatomical entity:"+centity.toString());
							/*ep.add(sentity); //add the other
							LOGGER.debug("add a proposal without anatomical entity:"+sentity.toString());*/
						
						}

					}
					//entities.add(ep);
					if(found){
						if(entities==null) entities = new ArrayList<EntityProposals>();
						Utilities.addEntityProposals(entities, ep);
						LOGGER.debug("EntitySearcher6 returns:");
						for(EntityProposals aep: entities){
							LOGGER.debug("..EntityProposals: "+aep.toString());
						}
						
						//caching
						if(entities==null) EntitySearcher6.nomatchcache.add(entityphrase+"+"+elocatorphrase);
						else EntitySearcher6.cache.put(entityphrase+"+"+elocatorphrase, entities);
						return entities;
					}
				}				
						
				
				
				//if failed, try wildcard 
				
				//ArrayList<FormalConcept> sentities = TermSearcher.regexpSearchTerm(shortened+"\\b.*", "entity"); //candidate matches for the same entity
				ArrayList<FormalConcept> sentities = new TermSearcher(OLC).searchTerm(shortened+"\\b.*", "entity"); //candidate matches for the same entity
				if(sentities!=null){
					LOGGER.debug("search for entity '"+shortened+"\\b.*' found match, forming proposals...");
					//construct anatomicalentity
					/*SimpleEntity anatomicalentity = new SimpleEntity();
					anatomicalentity.setClassIRI("http://purl.obolibrary.org/obo/UBERON_0001062");
					anatomicalentity.setConfidenceScore(0.8f);
					anatomicalentity.setId("UBERON:0001062");
					anatomicalentity.setLabel("anatomical entity");
					anatomicalentity.setString(aentityphrase.substring(aentityphrase.lastIndexOf(" ")).trim());
					anatomicalentity.setXMLid(structid);*/
					//construct relation
					FormalRelation rel =  Dictionary.partof;
					rel.setConfidenceScore((float)1.0);

					EntityProposals ep = new EntityProposals(); 
					ep.setPhrase(originalentityphrase);
					boolean found = false;
					/*for(FormalConcept sentityfc: sentities){
						//if sentity part_of entityl holds, then sentity's conf score = 1 and return the result
						SimpleEntity sentity = (SimpleEntity)sentityfc;
						if(sentity.isOntologized() && entityls!=null){
							for(FormalConcept fc: entityls){
								SimpleEntity entityl = (SimpleEntity)fc;
								if(SearchMain.elk!=null && SearchMain.elk.isSubclassOfWithPart(entityl.getClassIRI(), sentity.getClassIRI())){
									LOGGER.debug("'"+entityl.getLabel() +"' and '"+sentity.getLabel() + "' are related, increase confidence score");
									found = true;
									sentity.setConfidenceScore(1f);
									CompositeEntity centity = new CompositeEntity();
									centity.addEntity(sentity);
									centity.addParentEntity(new REntity(rel, entityl)); //part of entityl
									centity.setString(originalentityphrase);
									ep.add(centity); //add the other proposal without anatomical entity	
									LOGGER.debug("add the other proposal without anatomical entity:"+centity.toString());		
								}
							}
						}
					}*/
					if(found){
						ArrayList<EntityProposals> entities = new ArrayList<EntityProposals>();
						//entities.add(ep);
						Utilities.addEntityProposals(entities, ep);
						LOGGER.debug("EntitySearcher6 returns:");
						for(EntityProposals aep: entities){
							LOGGER.debug("..EntityProposals: "+aep.toString());
						}
						
						//caching
						if(entities==null) EntitySearcher6.nomatchcache.add(entityphrase+"+"+elocatorphrase);
						else EntitySearcher6.cache.put(entityphrase+"+"+elocatorphrase, entities);
						
						return entities;
					}
					//else, record results that meet certain criteria
					LOGGER.debug("entity and entity locator (if exists) are not related");
					ArrayList<EntityProposals> entities = null;
					found = false;
					for(FormalConcept sentityfc: sentities){				
						SimpleEntity sentity = (SimpleEntity)sentityfc;
						//consider only the matches that are one word longer and don't have of/and in the labels
						String label = sentity.getLabel();
						String added = label.replaceFirst(shortened, "").trim();
						if(label.indexOf(" of ")>=0 || label.indexOf(" and ")>=0 || added.indexOf(" ")>0) continue;

						if(sentity.getId().compareTo(Dictionary.mcorganism)==0){
							//too general "body scale", try to search for "scale"
							//TODO: multi-cellular organism is too general a syn for body. "body" could mean something more restricted depending on the context.
							//TODO: change labels to ids
						}

						//entity
						//if(entityl.getString().length()>0){
						if(elocatorphrase.length()>0){
							for(FormalConcept fc: entityls){
								SimpleEntity entityl = (SimpleEntity)fc;
								//relation & entity locator
								/*CompositeEntity centity = new CompositeEntity();
								centity.addEntity(anatomicalentity);
								centity.addParentEntity(new REntity(rel, sentity));
								centity.addParentEntity(new REntity(rel, entityl));
								centity.setString(originalentityphrase);
								//ep.setPhrase(sentity.getString());
								//ep.setPhrase(originalentityphrase);
								ep.add(centity); //add one
								LOGGER.debug("add a proposal with anatomical entity:"+centity.toString());*/
								CompositeEntity centity = new CompositeEntity();
								centity.addEntity(sentity);								
								centity.addParentEntity(new REntity(rel, entityl));
								centity.setString(originalentityphrase);
								ep.add(centity); //add the other	
								LOGGER.debug("add a proposal without anatomical entity:"+centity.toString());
								found = true;
							}
						}else{
							//EntityProposals entities = new EntityProposals();
							/*CompositeEntity centity = new CompositeEntity();
							centity.addEntity(anatomicalentity);
							centity.addParentEntity(new REntity(rel, sentity));
							centity.setString(originalentityphrase);
							//ep.setPhrase(sentity.getString());
							ep.setPhrase(originalentityphrase);
							ep.add(centity); //add one
							LOGGER.debug("add a proposal with anatomical entity:"+centity.toString());*/
							ep.add(sentity); //add the other
							LOGGER.debug("add a proposal without anatomical entity:"+sentity.toString());
							found = true;
						}

					}
					//entities.add(ep);
					if(found){
						if(entities==null) entities = new ArrayList<EntityProposals>();
						Utilities.addEntityProposals(entities, ep);
						LOGGER.debug("EntitySearcher6 returns:");
						for(EntityProposals aep: entities){
							LOGGER.debug("..EntityProposals: "+aep.toString());
						}
						
						//caching
						if(entities==null) EntitySearcher6.nomatchcache.add(entityphrase+"+"+elocatorphrase);
						else EntitySearcher6.cache.put(entityphrase+"+"+elocatorphrase, entities);
						return entities;
					}
				}				
			}			
		}
		EntitySearcher6.nomatchcache.add(entityphrase+"+"+elocatorphrase);
		LOGGER.debug("EntitySearch6 found no match");
		return null;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
