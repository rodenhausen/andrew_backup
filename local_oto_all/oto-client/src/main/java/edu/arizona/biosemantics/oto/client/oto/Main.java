package edu.arizona.biosemantics.oto.client.oto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import edu.arizona.biosemantics.oto.common.model.GlossaryDictionaryEntry;
import edu.arizona.biosemantics.oto.oto.rest.GlossaryDictionaryEntryData;
import edu.arizona.biosemantics.oto.oto.rest.LoginData;
import edu.arizona.biosemantics.oto.oto.rest.NameContextData;
import edu.arizona.biosemantics.oto.oto.rest.TermOrderData;




public class Main {

	/**
	 * @param args
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		//OTOClient otoClient = new OTOClient("http://biosemantics.arizona.edu/OTO/");
		
		OTOClient otoClient = new OTOClient("http://localhost:8082/local_oto/");

		otoClient.open();
		
		LoginData loginData = new LoginData("astocksto@gmail.com", "butter");
		
		GlossaryDictionaryEntryData data = new GlossaryDictionaryEntryData(loginData, "a beautiful definition.");
		
		Future<GlossaryDictionaryEntry> result = otoClient.putAndGetGlossaryDictionaryEntry("Plant", "stuff", "unsure", data);
		System.out.println(result.get().toString());
		
		/*Future<String> result = otoClient.createDataset("supertest", "nothing", loginData);
		System.out.println("Got result: " + result.get());*/

		/*NameContextData content = new NameContextData();
		content.setLoginData(loginData);
		content.addEntry("A", "aaaaaaa");
		content.addEntry("B", "bbbb");
		content.addEntry("C", "ccccccc");
		content.addEntry("D", "dddddddddddddd");
		content.addEntry("E", "eee");
		content.addEntry("F", "ffff");
		content.addEntry("G", "gg");
		
		Future<String> result = otoClient.structureHierarchy("supertest", content);
		
		System.out.println("Got result: " + result.get());*/
		
		
		/*TermOrderData termOrderData = new TermOrderData();
		termOrderData.setLoginData(loginData);
		ArrayList<String> list1 = new ArrayList<String>();
		list1.add("test");
		list1.add("number");
		list1.add("one");
		list1.add("here");
		ArrayList<String> list2 = new ArrayList<String>();
		list2.add("this");
		list2.add("is");
		list2.add("number");
		list2.add("two");
		ArrayList<String> list3 = new ArrayList<String>();
		list3.add("and");
		list3.add("here's");
		list3.add("the");
		list3.add("third");
		termOrderData.addEntry("First", list1);
		termOrderData.addEntry("Second", list2);
		termOrderData.addEntry("Third", list3);
		
		System.out.println("Sending " + termOrderData);
		
		Future<String> result2 = otoClient.termOrder("thegreatestdataset", termOrderData);
		
		System.out.println("Got second result: " + result2.get());*/
		
		otoClient.close();
		
		//GlossaryDictionaryEntry result = otoClient.getGlossaryDictionaryEntry("Plant", "round22", "shape");
		
		//System.out.println(result.getTermID());
		//System.out.println(result.getDefinition());
		//otoClient.getCategories();
		
		
		/*List<Category> categories = otoClient.getCategories();
		System.out.println(categories);*/
		//"plant_gloss_for_iplant", "Plant"
		//GlossaryDownload download = otoClient.download("Plant");
		//System.out.println(download.toString());
		/*GlossaryDownload download = otoClient.download("Plant", "latest");
		System.out.println(download.toString());
		for(TermCategory termCategory : download.getTermCategories()) {
			System.out.println(
					termCategory.getCategory() + " " + termCategory.getTerm() + " " + 
			termCategory.isHasSyn());
		}*/
	}

}
