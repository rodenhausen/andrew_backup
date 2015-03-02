package edu.arizona.biosemantics.oto.client.lite;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ws.rs.client.InvocationCallback;

import edu.arizona.biosemantics.oto.common.model.lite.Download;
import edu.arizona.biosemantics.oto.common.model.lite.Sentence;
import edu.arizona.biosemantics.oto.common.model.lite.Term;
import edu.arizona.biosemantics.oto.common.model.lite.Upload;
import edu.arizona.biosemantics.oto.common.model.lite.UploadResult;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		OTOLiteClient otoLiteClient = new OTOLiteClient("http://biosemantics.arizona.edu/OTOLite/");
		otoLiteClient.open();
		/*Future<Download> download = otoLiteClient.getDownload(new UploadResult(392, "secret"));
		Download d;
		try {
			d = download.get();
			System.out.println(d.isFinalized());
			System.out.println(d.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		otoLiteClient.close();*/
		
		Upload upload = new Upload();
		upload.setGlossaryType("plants");
		List<Sentence> sentences = new ArrayList<Sentence>();
		sentences.add(new Sentence(1, "1.txt", "some", "example"));
		upload.setSentences(sentences);
		
		List<Term> possStr = new ArrayList<Term>();
		List<Term> possCh = new ArrayList<Term>();
		List<Term> possOt = new ArrayList<Term>();
		possStr.add(new Term("long"));
		possStr.add(new Term("short"));
		possCh.add(new Term("red"));
		possCh.add(new Term("blue"));
		possCh.add(new Term("broad"));
		possCh.add(new Term("slender"));
		possCh.add(new Term("weak"));
		possCh.add(new Term("firm"));
		possCh.add(new Term("drooping"));
		possCh.add(new Term("bright"));
		possOt.add(new Term("dark"));
		possOt.add(new Term("flat"));
		possOt.add(new Term("bumpy"));
		possOt.add(new Term("smooth"));
		possOt.add(new Term("rough"));
		possOt.add(new Term("sharp"));
		possOt.add(new Term("narrow"));
		possOt.add(new Term("straight"));
		possOt.add(new Term("rigid"));
		upload.setPossibleCharacters(possCh);
		upload.setPossibleOtherTerms(possOt);
		upload.setPossibleStructures(possStr);
		
		try{
			Future<UploadResult> fur = otoLiteClient.putUpload(upload);
			UploadResult ur = fur.get();
			int uploadId = ur.getUploadId();
			System.out.println("uploadID: " + uploadId);
			String secret = ur.getSecret();
			System.out.println("secret: " + secret);
			
			
			/*otoLiteClient.putUpload(upload, new InvocationCallback<List<UploadResult>>(){

				@Override
				public void completed(List<UploadResult> arg0) {
					UploadResult ur = arg0.get(0);
					int uploadId = ur.getUploadId();
					System.out.println("uploadID: " + uploadId);
					String secret = ur.getSecret();
					System.out.println("secret: " + secret);
				}

				@Override
				public void failed(Throwable arg0) {
					arg0.printStackTrace();
					
				}
				
			});*/
		} catch(Exception e){
			e.printStackTrace();
		}
		
		/*Download download = otoLiteClient.download(uploadId);
		System.out.println(download.toString()); */
	}

}
