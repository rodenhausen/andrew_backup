package edu.arizona.biosemantics.oto.common.ontologylookup.search.knowledge;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import org.apache.log4j.Logger;
//import org.semanticweb.owlapi.model.OWLClass;


import org.semanticweb.owlapi.model.OWLClass;


import edu.arizona.biosemantics.oto.common.ontologylookup.search.OntologyLookupClient;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.data.Entity;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.data.FormalRelation;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.data.Quality;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.data.SimpleEntity;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.owlaccessor.OWLAccessorImpl;
//import ApplicationUtilities;
//import Utilities;
//import XML2EQ;
import edu.mit.jwi.IDictionary;


public class Dictionary {
	private static final Logger LOGGER = Logger.getLogger(Dictionary.class);   
	static final public String STOP = "a|about|above|across|after|along|also|although|amp|an|and|are|as|at|be|because|become|becomes|becoming|been|before|being|beneath|between|beyond|but|by|ca|can|could|did|do|does|doing|done|for|from|had|has|have|hence|here|how|if|in|into|inside|inward|is|it|its|may|might|more|most|near|no|not|of|off|on|onto|or|out|outside|outward|over|should|so|than|that|the|then|there|these|this|those|throughout|to|toward|towards|up|upward|was|were|what|when|where|which|why|with|within|without|would|yet|etc";
	static final public String NUMBERS = "zero|one|ones|first|two|second|three|third|thirds|four|fourth|fourths|quarter|five|fifth|fifths|six|sixth|sixths|seven|seventh|sevenths|eight|eighths|eighth|nine|ninths|ninth|tenths|tenth";
	static final public String FORBIDDEN ="to|and|or|nor"; //words in this list can not be treated as boundaries "to|a|b" etc.
	static final public String PRONOUN ="all|each|every|some|few|individual|both|other|another|either|neither";
	static final public String CHARACTER ="lengths|length|lengthed|width|widths|widthed|heights|height|character|characters|distribution|distributions|outline|outlines|profile|profiles|feature|features|form|forms|mechanism|mechanisms|nature|natures|shape|shapes|shaped|size|sizes|sized";//remove growth, for growth line. check 207, 3971
	static final public String PROPOSITION ="above|across|after|along|around|as|at|before|beneath|between|beyond|by|for|from|in|into|near|of|off|on|onto|out|outside|over|than|throughout|toward|towards|up|upward|with|without";
	static final public String CLUSTER = "group|groups|clusters|cluster|arrays|array|series|fascicles|fascicle|pairs|pair|rows|number|numbers|\\d+";
	static final public String SUBSTRUCTURE = "part|parts|area|areas|portion|portions";
	static final public String ADDITIONAL = "bearer|entity|bearer's|bearers'|entities|inhering|inheres|inhere|virtue|quality|having|exhibiting";

	//see http://phenoscape.svn.sourceforge.net/viewvc/phenoscape/trunk/vocab/character_slims.obo from Jim
	public static String patoupperclasses = "2-D shape|cellular quality|shape|size|position|closure|structure|count in organism|optical quality|composition|texture|physical quality of a process|behavioral quality|mobility|mass|quality of a solid";
	//spatial terms form BSPO
	public static ArrayList<String> spatialterms = new ArrayList<String>();
	
	//synonym rings
	public static String process="process|crest|ridge|ridges|tentacule|tentacules|shelf|shelves|flange|flanges|lamella|lamellae|lamina|laminae|projection|projections";
	public static String opening = "opening|foramina|foramen|foramens|perforation|orifice"; 
	public static String joint ="joint|articulation";
	public static String contact="connection|contact|interconnection";//Extendible
	
	public static Hashtable<String, String> synrings = new Hashtable<String, String>();
	static{
		synrings.put(process, "anatomical projection");
		synrings.put(opening, opening);
		synrings.put(joint, joint);
		synrings.put(contact, contact);
		
	}
	
	//others
	public static String binaryTvalues1 = "present|with";//added present/absent
	public static String binaryFvalues1 = "absent|without";
	public static String binaryTvalues2 = "true|yes|usually";//added present/absent
	public static String binaryFvalues2 = "false|no|rarely";
	public static String negation = "absent|lacking";
	public static String positionprep = "of|part_of|in|on|between";
	//Changed by Zilong
	public static String selfreference = "counterpart";//Extendible
	public static String prefixes = "post|pre|post-|pre-";
	
	//spatial
	public static String spatialtermptn="medioventral|";
	public static String singlewordspatialtermptn="medioventral|";
	public static String multiwordsspatialtermptn="";




	//By Zilong: Update by Hong: Zilong's modifications have all be over-written. 
	//sometimes, spatial terms could be used as adjectives to modify head nouns. 
	//Instead of directly using <spatial terms+head nouns> when searching the ontology,
	//the program should interpret the pattern as a part_of relation. 
	//eg. "anterior coracoid process" should be interpreted as "anterior region(part_of(coracoid))"
	//This list contains all identified head nouns
	//public static ArrayList<String> spatialHeadNoun = new ArrayList<String>();
	//eg. <"unossified", "ossification,absent">; ossified => with ossification => ossification = present
	//public static Hashtable<String, String> verbalizednouns = new Hashtable<String,String>();
	//this instance var is used to map spatial terms that are not in ontology to terms that are.
	/** The spatial maps. */
	//public static Hashtable<String, String> spatialMaps = new Hashtable<String, String>();
	
	//headnouns that may be used as part of a spatial term
	public static String spatialheadnouns ="axis|boudary|compartment boundary|compartment|gradient|margin|region|section|side|surface"; 
	//synonyms of the head noun 'region'. May include a syn list for each of the head noun.
	public static Hashtable<String, String> headnounsyns= new Hashtable<String, String> ();
	static{
		headnounsyns.put("region", "portion|end|segment|area|component|part");
	}
	
	public static Hashtable<String,Hashtable<String, String>> restrictedrelations = new Hashtable<String,Hashtable<String, String>>();

	public static Hashtable<String, String> resrelationQ = new Hashtable<String, String>();
	public static Hashtable<String, String> parentclass2label = new Hashtable<String, String>();
	public static Hashtable<String, String> measureantonyms = new Hashtable<String, String>();

	/** Holds direct quality interpretation	 */
	
	public static Hashtable<String,String> qualitymapping = new Hashtable<String,String>();
	
	/** special ontology classes **/
	public static String mcorganism="UBERON:0000468"; //multi-cellular organism
	
	public final static String baseiri="http://purl.obolibrary.org/obo/";
	public static String cellquality = baseiri+"PATO_0001396";
	public final static String provisionaliri = "http://purl.bioontology.org/ontology/provisional/";
	public static String partofiri = baseiri+"BFO_000050";
	public static FormalRelation partof = new FormalRelation();
	public static FormalRelation iheresin = new FormalRelation();
	public static FormalRelation bearerof = new FormalRelation();
	public static FormalRelation complementof = new FormalRelation("no", "complement_of", "PHENOSCAPE_complement_of", "", ""); //TODO add iri
	public static Entity dorsalregion = new SimpleEntity();
	public static Entity ventralregion = new SimpleEntity();
	public static Quality present = new Quality();
	public static Quality absent = new Quality();
	public static SimpleEntity anatomicalentity = new SimpleEntity();
	public static SimpleEntity anatomicalprojection = new SimpleEntity();
	
	static{
		partof.setSearchString("");
		partof.setString("");
		partof.setLabel("part_of");
		partof.setId("BFO:000050");
		partof.setClassIRI(baseiri+"BFO_000050");
		
		iheresin.setClassIRI(baseiri+"pato#inheres_in");
		iheresin.setSearchString("");
		iheresin.setId("BFO:0000052");
		iheresin.setLabel("inheres_in");
		iheresin.setString("");
		
		bearerof.setClassIRI(baseiri+"BFO_0000053");
		bearerof.setSearchString("");
		bearerof.setLabel("bearer_of");
		bearerof.setId("BFO:0000053"); 
		bearerof.setString("");
		
		dorsalregion.setClassIRI(baseiri+"BSPO_0000079");
		dorsalregion.setSearchString("");
		dorsalregion.setLabel("dorsal region");
		dorsalregion.setId("BSPO:0000079"); 
		dorsalregion.setString("");
		
		
		ventralregion.setClassIRI(baseiri+"BSPO_0000084");
		ventralregion.setSearchString("");
		ventralregion.setLabel("ventral region");
		ventralregion.setId("BSPO:0000084");
		ventralregion.setString("");
		
		present.setClassIRI(baseiri+"PATO_0000467");
		present.setSearchString("");
		present.setLabel("present");
		present.setId("PATO:0000467");
		present.setString("");
		
		absent.setClassIRI(baseiri+"PATO_0000462");
		absent.setSearchString("");
		absent.setLabel("absent");
		absent.setId("PATO:0000462");
		absent.setString("");
		

		anatomicalentity.setClassIRI(baseiri+"UBERON_0001062");
		anatomicalentity.setConfidenceScore(0.8f);
		anatomicalentity.setId("UBERON:0001062");
		anatomicalentity.setLabel("anatomical entity");
		anatomicalentity.setSearchString("");
		anatomicalentity.setString("");
		

		anatomicalprojection.setClassIRI("http://purl.obolibrary.org/obo/UBERON_0004529");
		anatomicalprojection.setConfidenceScore(1f);
		anatomicalprojection.setId("UBERON:0004529");
		anatomicalprojection.setLabel("anatomical projection");
		anatomicalprojection.setSearchString("");
		anatomicalprojection.setString("");
		
	}
	
	public static Hashtable<String, String> singulars = new Hashtable<String, String>();
	public static Hashtable<String, String> plurals = new Hashtable<String, String>();
	//private ArrayList<Hashtable<String, String>>  alladjectiveorgans = new ArrayList<Hashtable<String, String>> (); //one hashtable from an ontology
	public static IDictionary wordnetdict = new edu.mit.jwi.Dictionary(new File(OntologyLookupClient.dictdir));
	
	//to hold complement of relations
	public static Hashtable<String,String> complementRelations = new Hashtable<String,String>();

	static
	{
		complementRelations.put("in contact with", "separated from");
		complementRelations.put("fused with", "unfused from");
	}
	//to hold organ names and their adjective forms that are not covered by ontologies
	public static Hashtable<String, ArrayList<String>> organadjectives = new Hashtable<String, ArrayList<String>>();
	
	static{
		ArrayList<String> tmp = new ArrayList<String>();
		tmp.add("radial");
		organadjectives.put("radius", tmp);
	}
	
	//to hold organ names and their adjective forms that are not covered by ontologies
	public static Hashtable<String, ArrayList<String>> adjectiveorgans = new Hashtable<String, ArrayList<String>>();
	
		
	static{
		ArrayList<String> tmp = new ArrayList<String>();
		tmp.add("radius");
		adjectiveorgans.put("radial", tmp);
	}
	//to hold antonyms of measures
	// As per discussion with prof. this is not needed, we will use complement of  as of now.
	static
	{
//		measureantonyms.put("short", "long");
//		measureantonyms.put("long", "short");
//		measureantonyms.put("high", "low");
//		measureantonyms.put("low", "high");
//		measureantonyms.put("wide", "narrow");
//		measureantonyms.put("narrow", "wide");
//		measureantonyms.put("broad", "narrow");

	}
	//special cases for singulars and plurals
	static{
		//singluar = plural
		singulars.put("brit", "brit");
		singulars.put("buttress", "buttress");
		singulars.put("callus", "callus");
		singulars.put("forceps", "forceps");
		singulars.put("frons", "frons");
		singulars.put("fry", "fry");
		singulars.put("media", "media");
		singulars.put("series", "series");
		singulars.put("species", "species");
		singulars.put("sperm", "sperm");
		
		//special cases
		singulars.put("axis", "axis");
		singulars.put("axes", "axis");
		singulars.put("bases", "base");
		singulars.put("boss", "boss");
		singulars.put("grooves", "groove");
		singulars.put("interstices", "interstice");
		singulars.put("lens", "len");
		singulars.put("midnerves", "midnerve");
		singulars.put("process", "process");
		singulars.put("teeth", "tooth");
		singulars.put("valves", "valve");
		singulars.put("catenabe", "catena");
		singulars.put("fusules", "fusula");
		singulars.put("pseudocoelomata", "pseudocoelomates");
		singulars.put("setules", "setula");
		singulars.put("ephyre", "ephyra");
		singulars.put("ephyrae", "ephyra");
		singulars.put("ephyrula", "ephyra");
		singulars.put("fusules", "fusula");
		singulars.put("mollusks", "mollusca");
		singulars.put("molluscs", "mollusca");
		singulars.put("malli", "malleus");
		singulars.put("anthocyathia", "anthocyathus");
		singulars.put("perradia", "perradius");
		singulars.put("genera", "genus");
		singulars.put("latera", "latus");
		singulars.put("corpora", "corpus");
		singulars.put("incudes", "incus");
		singulars.put("parasides", "parapsis");
		singulars.put("coremata", "corematis");
		singulars.put("crepides", "crepis");
		singulars.put("falces", "falx");
		singulars.put("glochines", "glochis");
		singulars.put("irises", "iris");
		singulars.put("irides", "iris");
		singulars.put("proboscises", "proboscis");
		singulars.put("proglottides", "proglottis");
		singulars.put("pharynges", "pharynx");
		singulars.put("pharynxes", "pharynx");

		//pl = sing
		plurals.put("brit", "brit");
		plurals.put("buttress", "buttress");
		plurals.put("callus", "callus");
		plurals.put("forceps", "forceps");
		plurals.put("frons", "frons");
		plurals.put("fry", "fry");
		plurals.put("media", "media");
		plurals.put("series", "series");
		plurals.put("species", "species");
		plurals.put("sperm", "sperm");
		//special cases
		plurals.put("axis", "axes");
		plurals.put("base", "bases");	
		plurals.put("boss", "bosses");
		plurals.put("buttress", "buttresses");
		plurals.put("callus", "calluses");
		plurals.put("groove", "grooves");
		plurals.put("interstice", "interstices");
		plurals.put("len", "lens");
		plurals.put("midnerve", "midnerves");
		plurals.put("process", "processes");
		plurals.put("tooth", "teeth");
		plurals.put("valve", "valves");
		plurals.put("catena","catenabe");
		plurals.put("fusula","fusules");
		plurals.put("pseudocoelomates","pseudocoelomata");
		plurals.put("setula","setules");
		//when multiple pls, take one
		plurals.put("ephyra","ephyre");
		plurals.put("ephyra","ephyrae");
		plurals.put("ephyra","ephyrula");
		plurals.put("fusula","fusules");
		plurals.put("mollusca","mollusks");
		plurals.put("mollusca","molluscs");
		plurals.put("malleus","malli");
		plurals.put("anthocyathus","anthocyathia");
		plurals.put("perradius","perradia");
		plurals.put("genus","genera");
		plurals.put("latus","latera");
		plurals.put("corpus","corpora");
		plurals.put("incus","incudes");
		plurals.put("parapsis","parasides");
		plurals.put("corematis","coremata");
		plurals.put("crepis","crepides");
		plurals.put("falx","falces");
		plurals.put("glochis","glochines");
		plurals.put("iris","irises");
		plurals.put("iris","irides");
		plurals.put("proboscis","proboscises");
		plurals.put("proglottis","proglottides");
		plurals.put("pharynx","pharynges");
		plurals.put("pharynx","pharynxes");
	}
	//upper level quality classes 
	static{
		parentclass2label.put("PATO:0000186", "behavioral quality");
		parentclass2label.put("PATO:0001396", "cellular quality");
		parentclass2label.put("PATO:0000136", "closure");
		parentclass2label.put("PATO:0000025", "composition");
		parentclass2label.put("PATO:0000070", "count in organism");
		parentclass2label.put("PATO:0000125", "mass");
		parentclass2label.put("PATO:0000004", "mobility");
		parentclass2label.put("PATO:0001300", "optical quality");
		parentclass2label.put("PATO:0002062", "physical quality of a process");
		parentclass2label.put("PATO:0000140", "position");
		parentclass2label.put("PATO:0001546", "quality of a solid");
		parentclass2label.put("PATO:0000052", "shape");
		parentclass2label.put("PATO:0000117", "size");
		parentclass2label.put("PATO:0000141", "structure");
		parentclass2label.put("PATO:0000150", "texture");
	}
	
	// Direct quality interpretations
	static
	{
		qualitymapping.put("ball-like", "spherical");
		qualitymapping.put("subspehrical", "subcircular");
		qualitymapping.put("kite-shaped", "diamond shaped");
		qualitymapping.put("spike", "spindle-shaped");
		qualitymapping.put("crescentic", "crescent-shaped");
		qualitymapping.put("ossified", "osseus");
	}
	
	static String[] spatials = new String[] {"A-P","A-V","AA","adaxial-abaxial","anatomical","animal-vegetal","animal/vegetal","anterior","anterior-most","anterior-posterior","anterior/posterior","anteriormost","antero-dorsal","antero-lateral","antero-medial","antero-ventral","anterodorsal","anterolateral","anteromedial","anteroposterior","anteroventral","AP","apical","apical-basal","apical/basal","AV","axial","basal","caudal","central","cephalocaudal","compartment","contralateral","coronal","cranial","craniocaudal","cross-section","D-V","deep","deep-superficial","deep/superficial","dextro-sinister","distal","dorsal","dorsal-most","dorsal-ventral","dorsal/ventral","dorsalmost","dorso-lateral","dorso-medial","dorsolateral","dorsomedial","dorsoventral","DV","edge","frontal","horizontal","inferior","ipsilateral","L-R","lateral","left","left-right","left/right","longitudinal","lower","LR","M-L","M-R","margin","medial","medial-external","medial-lateral","medial-radial","median","medio-lateral","mediolateral","medioventral","midline","midsagittal","ML","MR","oral-aboral","parasagittal","peripheral","plane","posterior","posterior-most","posteriormost","postero-dorsal","postero-lateral","postero-medial","postero-ventral","posterodorsal","posterolateral","posteromedial","posteroventral","proximal","proximal-distal","proximal/distal","proximodistal","R-L","right","right-left","RL","rostral","rostral/caudal","rostrocaudal","sagittal","section","superficial","superior","surface","transverse","upper","upper-lower","upper/lower","ventral","ventral-most","ventralmost","ventro-lateral","ventro-medial","ventrolateral","ventromedial"};
	
	//load spatial terms and construct spatial term pattern
	static{
		try{
			
			for(String term: spatials){
				term = term.replaceAll("\\(.*?\\)", "").trim(); //remove "(obsolete)"
				if(term.length()>0){					
					spatialtermptn += term+"|";
					if(term.indexOf(" ")>0){
						multiwordsspatialtermptn += term+"|";
					}else{
						singlewordspatialtermptn += term+"|";
					}
				}
			}
			singlewordspatialtermptn = singlewordspatialtermptn.replaceFirst("\\|$", "");
			multiwordsspatialtermptn = multiwordsspatialtermptn.replaceFirst("\\|$", "");
			spatialtermptn = spatialtermptn.replaceFirst("\\|$", "");
			
			//sorting according to length of the string 
			String spatialarray[]=spatialtermptn.split("\\|");
			spatialtermptn="";

			String temp;
			for(int i=0;i<spatialarray.length-1;i++)
			{
				for(int j=i+1;j<spatialarray.length;j++)
				{
					if(spatialarray[i].split(" ").length<spatialarray[j].split(" ").length)
					{
						temp = spatialarray[i];
						spatialarray[i] = spatialarray[j];
						spatialarray[j] = temp;
					}
				}
			}
			
			for(int i=0;i<spatialarray.length;i++)
				{
				spatialtermptn+=spatialarray[i]+"|";
				spatialterms.add(spatialarray[i]);
				}
			
			spatialtermptn = spatialtermptn.replaceFirst("\\|$", "");

		}catch(Exception e){
			LOGGER.error("", e);
		}
		spatialterms.add("accessory");
		
	}
	//other spatial related info
	static{
		//spatialHeadNoun: TODO make it more flexible
		//spatialHeadNoun.add("coronoid");
		//spatialHeadNoun.add("process");
		//spatialHeadNoun.add("coracoid");
		
		//un-ed pattern, TODO make it more flexible
		//verbalizednouns.put("unossified", "ossification,absent");
		
		//spatialMaps.put("portion", "region");	
		//end is defined to be region => distal end => distal region
		//spatialMaps.put("end", "region");
		//spatialMaps.put("end", "margin");
		//spatialMaps.put("segment","region"); // to address basal segment => basal region(only when segment is preceded by spatial term)
	}
	//legal relational qualities for postcomposition of entities
	static{	
		resrelationQ.put("BFO:0000053","bearer_of");
		resrelationQ.put("RO:0002220","adjacent_to");
		resrelationQ.put("BSPO:0000096","anterior_to");
		resrelationQ.put("UBERON:anteriorly_connected_to","anteriorly_connected_to");
		resrelationQ.put("UBERON:attaches_to","attaches_to");
		resrelationQ.put("PHENOSCAPE:extends_from","extends_from");
		resrelationQ.put("RO:0002150","connected_to");
		resrelationQ.put("PATO:decreased_in_magnitude_relative_to","decreased_in_magnitude_relative_to");
		resrelationQ.put("BSPO:0000107","deep_to");
		resrelationQ.put("RO:0002202","develops_from");
		resrelationQ.put("BSPO:0000097","distal_to");
		resrelationQ.put("UBERON:distally_connected_to","distally_connected_to");
		resrelationQ.put("BSPO:0000098","dorsal_to");
		resrelationQ.put("UBERON:encloses","encloses");
		resrelationQ.put("PHENOSCAPE:extends_to","extends_to");
		resrelationQ.put("PATO:has_cross_section","has_cross_section");
		resrelationQ.put("UBERON:has_muscle_insertion","has_muscle_insertion");
		resrelationQ.put("UBERON:has_muscle_origin","has_muscle_origin");
		resrelationQ.put("BFO:0000051","has_part");
		resrelationQ.put("BSPO:0000123","in_anterior_side_of");
		resrelationQ.put("BSPO:0000125","in_distal_side_of");
		resrelationQ.put("UBERON:in_lateral_side_of","in_lateral_side_of");
		resrelationQ.put("BSPO:0000120","in_left_side_of");
		resrelationQ.put("UBERON:in_median_plane_of","in_median_plane_of");
		resrelationQ.put("BSPO:0000122","in_posterior_side_of");
		resrelationQ.put("BSPO:0000124","in_proximal_side_of");
		resrelationQ.put("BSPO:0000121","in_right_side_of");
		resrelationQ.put("PATO:increased_in_magnitude_relative_to","increased_in_magnitude_relative_to");
		resrelationQ.put("OBO_REL:located_in","located_in");
		resrelationQ.put("RO:0002131","overlaps");
		resrelationQ.put("BFO:0000050","part_of");
		resrelationQ.put("BSPO:passes_through","passes_through");
		resrelationQ.put("BSPO:0000099","posterior_to");
		resrelationQ.put("UBERON:posteriorly_connected_to","posteriorly_connected_to");
		resrelationQ.put("BSPO:0000100","proximal_to");
		resrelationQ.put("UBERON:proximally_connected_to","proximally connected to");
		resrelationQ.put("PATO:similar_in_magnitude_relative_to","similar_in_magnitude_relative_to");
		resrelationQ.put("RO:0002219","surrounded by");
		resrelationQ.put("RO:0002221","surrounds");
		resrelationQ.put("BSPO:0000102","ventral_to");
		resrelationQ.put("BSPO:0000103","vicinity_of");
		resrelationQ.put("PHENOSCAPE:serves_as_attachment_site_for","serves_as_attachment_site_for");
		resrelationQ.put("BFO:0000052","inheres_in");
		resrelationQ.put("PHENOSCAPE:complement_of","not");		
		
		Set<String> keys = resrelationQ.keySet();
		
		for(String key:keys)
		{
			String relation = resrelationQ.get(key).replaceAll("_", " ");
			relation = edu.arizona.biosemantics.oto.common.ontologylookup.search.utilities.Utilities.removeprepositions(relation.trim());
			Hashtable<String, String> temp = new Hashtable<String, String>();
			temp.put(resrelationQ.get(key), key);
			restrictedrelations.put(relation,temp);			
		}
		
	}
	
	//this should ideally not be needed, but in reality, many equivalent classes exist cross ontologies but are not treated so
	//the restrictedrelations for entity postcomposition sometimes have equivalent classes in PATO. PATO classes are need to create negated qualities
	public final static Hashtable<String, String> translateToPATO = new Hashtable<String, String>(); //used for restricted relationlist
	public static final String spatialOntoPrefix = "BSPO";
	static{
		    translateToPATO.put("BFO:0000053","PATO:0000001"); //bearer_of
		    translateToPATO.put("RO:0002220","PATO:0002259"); //adjacent to
			translateToPATO.put("BSPO:0000096","PATO:0001632");//anterior_to
			translateToPATO.put("UBERON:anteriorly_connected_to","PATO:0000001");
			translateToPATO.put("UBERON:attaches_to","PATO:0000001");//attaches_to
			translateToPATO.put("PHENOSCAPE:extends_from","PATO:0000001");
			translateToPATO.put("RO:0002150","PATO:0000001");
			translateToPATO.put("PATO:decreased_in_magnitude_relative_to","PATO:decreased_in_magnitude_relative_to");
			translateToPATO.put("BSPO:0000107","PATO:0000001");
			translateToPATO.put("RO:0002202","PATO:0000001");
			translateToPATO.put("BSPO:0000097","PATO:0001234"); //distal to
			translateToPATO.put("UBERON:distally_connected_to","PATO:0000001");
			translateToPATO.put("BSPO:0000098","PATO:0001233");//dorsal_to
			translateToPATO.put("UBERON:encloses","PATO:0000001");
			translateToPATO.put("PHENOSCAPE:extends_to","PATO:0000001");
			translateToPATO.put("PATO:has_cross_section","PATO:has_cross_section");
			translateToPATO.put("UBERON:has_muscle_insertion","has_muscle_insertion");
			translateToPATO.put("UBERON:has_muscle_origin","has_muscle_origin");
			translateToPATO.put("BFO:0000051","has_part");
			translateToPATO.put("BSPO:0000123","in_anterior_side_of");
			translateToPATO.put("BSPO:0000125","in_distal_side_of");
			translateToPATO.put("UBERON:in_lateral_side_of","in_lateral_side_of");
			translateToPATO.put("BSPO:0000120","in_left_side_of");
			translateToPATO.put("UBERON:in_median_plane_of","in_median_plane_of");
			translateToPATO.put("BSPO:0000122","in_posterior_side_of");
			translateToPATO.put("BSPO:0000124","in_proximal_side_of");
			translateToPATO.put("BSPO:0000121","in_right_side_of");
			translateToPATO.put("PATO:increased_in_magnitude_relative_to","PATO:increased_in_magnitude_relative_to");
			translateToPATO.put("OBO_REL:located_in","PATO:0002261"); //located in
			translateToPATO.put("RO:0002131","PATO:0001590"); //overlap with
			translateToPATO.put("BFO:0000050","part_of");
			translateToPATO.put("BSPO:passes_through","passes_through");
			translateToPATO.put("BSPO:0000099","PATO:0001633"); //posterior to
			translateToPATO.put("UBERON:posteriorly_connected_to","PATO:0000001");
			translateToPATO.put("BSPO:0000100","PATO:0001195"); //proximal to
			translateToPATO.put("UBERON:proximally_connected_to","proximally_connected_to");
			translateToPATO.put("PATO:similar_in_magnitude_relative_to","PATO:similar_in_magnitude_relative_to");
			translateToPATO.put("RO:0002219","surrounded_by");
			translateToPATO.put("RO:0002221","PATO:0001772"); //surrounding
			translateToPATO.put("BSPO:0000102","PATO:0001196"); //ventral to
			translateToPATO.put("BSPO:0000103","vicinity_of");
			translateToPATO.put("PHENOSCAPE:serves_as_attachment_site_for","serves_as_attachment_site_for");
			translateToPATO.put("BFO:0000052","PATO:inheres_in");
			translateToPATO.put("PHENOSCAPE:complement_of","PATO:0000001");	
	}
	
	//wordnet 
	static{
		try {
			wordnetdict.open();					
		} catch (IOException e) {
			LOGGER.error("", e);
		}
	}

	/**
	 * 
	 * @return a String of alternative pattern of all spatial headnouns and their synonyms
	 */
	public static String allSpatialHeadNouns(){
		String shn = Dictionary.spatialheadnouns+"|";
		String[] terms = Dictionary.spatialheadnouns.split("\\|");
		for(String term: terms){
			String syns = Dictionary.headnounsyns.get(term);
			if(syns!=null) shn +=syns +"|";
		}
		return shn.replaceAll("\\|+", "|").replaceAll("(^\\||\\|$)", "");
	}
	
	
}
