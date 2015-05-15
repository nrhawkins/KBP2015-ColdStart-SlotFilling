package kbp;

//import java.io.File;
//import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

//import org.apache.commons.io.IOUtils;

import edu.knowitall.collection.immutable.Interval;
import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
//import edu.stanford.nlp.time.TimeAnnotations.TimexAnnotation;
import edu.stanford.nlp.time.Timex;
import edu.stanford.nlp.util.CoreMap;
//import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefChain.CorefMention;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
//import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefClusterIdAnnotation;
//import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefGraphAnnotation;
//import edu.stanford.nlp.ling.CoreAnnotations.*;
//import edu.stanford.nlp.ling.CoreLabel;
//import edu.stanford.nlp.pipeline.Annotation;
//import edu.stanford.nlp.pipeline.StanfordCoreNLP;
//import edu.stanford.nlp.time.TimeAnnotations.TimexAnnotation;
//import edu.stanford.nlp.time.Timex;
//import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.IntTuple;
//import edu.stanford.nlp.util.Pair;
//import edu.washington.cs.knowitall.kbp2014.multir.slotfiller.util.DocUtils;



public class StanfordAnnotatorHelperMethods {
	
	//private final StanfordCoreNLP basicPipeline;
	//private final  StanfordCoreNLP suTimePipeline;
	private final  StanfordCoreNLP corefPipeline;
	//private final StanfordCoreNLP chinesePipeline;
	//private String filePath = "/homes/gws/jgilme1/docs/";
	private Map<String,Annotation> corefAnnotationMap;
	private Map<String,Annotation> suTimeAnnotationMap;
	
	
	public StanfordAnnotatorHelperMethods(){

		
		//Properties basicProps = new Properties();
		//basicProps.put("annotators", "tokenize, cleanxml, ssplit");
		//basicProps.put("annotators", "tokenize, cleanxml, ssplit, pos, lemma, ner, parse, dcoref");
		//this.basicPipeline = new StanfordCoreNLP(basicProps);
		
		/*Properties suTimeProps = new Properties();
		suTimeProps.put("annotators", "tokenize, ssplit, pos, lemma, cleanxml, ner");
		suTimeProps.put("sutime.binders", "0");
		suTimeProps.put("clean.datetags","datetime|date|dateline");
		this.suTimePipeline = new StanfordCoreNLP(suTimeProps);
		*/
		
		Properties corefProps = new Properties();
		corefProps.put("annotators", "tokenize, cleanxml, ssplit, pos, lemma, ner, parse, dcoref");
	    //corefProps.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
	    //corefProps.put("clean.allowflawedxml", "true");
	    //corefProps.put("ner.useSUTime", "false");
	    //clean all xml tags
		this.corefPipeline = new StanfordCoreNLP(corefProps);
        
		
		/*Properties chineseProps = new Properties(); 	    
	    chineseProps.put("annotators", "segment, ssplit, pos, ner");
	    chineseProps.put("outputFormat", "xml");
	    chineseProps.put("customAnnotatorClass.segment", 
    			"edu.stanford.nlp.pipeline.ChineseSegmenterAnnotator");
	    chineseProps.put("segment.model", 
    			"edu/stanford/nlp/models/segmenter/chinese/ctb.gz");
	    chineseProps.put("segment.sighanCorporaDict", 
    			"edu/stanford/nlp/models/segmenter/chinese");
	    chineseProps.put("segment.serDictionary", 
    			"edu/stanford/nlp/models/segmenter/chinese/dict-chris6.ser.gz");
	    chineseProps.put("segment.sighanPostProcessing", "true");
	    chineseProps.put("ssplit.boundaryTokenRegex", "[.]|[!?]+|[。]|[！？]+");
	    chineseProps.put("pos.model", 
    			"edu/stanford/nlp/models/pos-tagger/chinese-distsim/chinese-distsim.tagger");
	    chineseProps.put("ner.model", 
    			"edu/stanford/nlp/models/ner/chinese.misc.distsim.crf.ser.gz");
	    chineseProps.put("ner.applyNumericClassifiers", "false");
	    chineseProps.put("ner.useSUTime", "false");
	    chineseProps.put("encoding", "utf-8");
	    chineseProps.put("inputEncoding", "utf-8");
	    chineseProps.put("outputEncoding", "utf-8");
	    chineseProps.put("parse.model", 
    			"edu/stanford/nlp/models/lexparser/chinesePCFG.ser.gz");
	    
		this.chinesePipeline = new StanfordCoreNLP(chineseProps);
        */
		
		corefAnnotationMap = new HashMap<String,Annotation>();
		suTimeAnnotationMap = new HashMap<String,Annotation>();

	}
	
	//public StanfordCoreNLP getBasicPipeline(){return basicPipeline;}
	//public StanfordCoreNLP getChinesePipeline(){return chinesePipeline;}
	public StanfordCoreNLP getCorefPipeline(){return corefPipeline;}
	
	public static void main(String[] args) throws FileNotFoundException, IOException{
		StanfordAnnotatorHelperMethods sh = new StanfordAnnotatorHelperMethods();
		//Annotation annotation = new Annotation("John Smith ate lunch.");
		//sh.corefPipeline.annotate(annotation);
		//for (Integer key: annotation.get(CorefChainAnnotation.class) .keySet()) {
	    //  for (CorefMention mention: annotation.get(CorefChainAnnotation.class).get(key).getMentionsInTextualOrder()) {
		//		System.out.println(mention.mentionSpan);
		//	}
		//}
	    //sh.runSuTime("testXMLDoc");
		
	}
	
	public void clearHashMaps(){
		corefAnnotationMap.clear();
		suTimeAnnotationMap.clear();
	}
	
	/*public void runSuTime(String docID) throws FileNotFoundException, IOException{
		Annotation document;
		if(suTimeAnnotationMap.containsKey(docID)){
			document = suTimeAnnotationMap.get(docID);
		}
		else{
		  String filePathPlusDocId = this.filePath+docID;
		  FileInputStream in = new FileInputStream(new File(filePathPlusDocId));
		  String fileString = IOUtils.toString(in,"UTF-8");
		  in.close();
		
		  document = new Annotation(fileString);
		  suTimePipeline.annotate(document);
		  suTimeAnnotationMap.put(docID, document);
		}
		
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    for(CoreMap sentence: sentences){
	    	for(CoreLabel token: sentence.get(TokensAnnotation.class)){
	    		String word = token.get(TextAnnotation.class);
	    		String ne = token.get(NamedEntityTagAnnotation.class);
	    		String net = token.get(NormalizedNamedEntityTagAnnotation.class);
	    		Timex tt = token.get(TimexAnnotation.class);
	    		String tts = "";
	    		if(tt != null){
	    			tts = tt.value();
	    		}
	    		System.out.println(word+ " " + ne + " " + net + " " + tts);
	    	}
	    }
	    
	    String s =document.get(NamedEntityTagAnnotation.class);
	    System.out.println(s);

	}
	*/
	
	private String normalizeTimex(Timex t){
		if(t.timexType() == "DATE"){
	      String timexString = t.value();
	      if (timexString == null) return "";
	      String formattedString = normalizeDate(timexString);
		  return formattedString;
		}
		else{
			return "";
		}
	}
	
	private String normalizeDate(String dateString){
		  String formattedString = null;
	      if(Pattern.matches("\\w{4}", dateString)){
	    	  formattedString = dateString +"-XX-XX";
	      }
	      else if(Pattern.matches("\\w{2}-\\w{2}",dateString)){
	    	  formattedString = "XXXX-" + dateString; 
	      }
	      else if(Pattern.matches("\\w{4}-\\w{2}", dateString)){
	    	  formattedString = dateString + "-XX";
	      }
		  
	      if(formattedString == null){
	    	  return dateString;
	      }
	      else{
	    	  return formattedString;
	      }
	}
	

	
	/*public String getNormalizedDate(Interval charInterval, String docId, String originalString) throws IOException{
		Annotation document;
		if(suTimeAnnotationMap.containsKey(docId)){
			document = suTimeAnnotationMap.get(docId);
		}
		else{
			String xmlDoc = SolrHelper.getRawDoc(docId);
			if(xmlDoc.trim().isEmpty()){
				return originalString;
			}
			document = new Annotation(xmlDoc);
			suTimePipeline.annotate(document);
			suTimeAnnotationMap.put(docId, document);
		}
	
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		    for(CoreMap sentence: sentences){
		    	for(CoreLabel token: sentence.get(TokensAnnotation.class)){
		    		Timex tt = token.get(TimexAnnotation.class);
		    		if(charInterval.intersects(Interval.closed(token.beginPosition(), token.endPosition()))){
		    			if(tt != null && tt.value() != null){
		    				return normalizeTimex(tt);
		    			}
		    		}
		    	}
		    }
	       return normalizeDate(originalString);
	}*/
	
	
	/*public List<CorefMention> getCorefMentions(String xmlString, Interval interval) {
		
      Annotation document = new Annotation(xmlString);

      corefPipeline.annotate(document);

      Map<Integer, CorefChain> graph = document.get(CorefChainAnnotation.class);
  
      for(Integer i : graph.keySet()){
        System.out.println("GROUP " + i);
        CorefChain x = graph.get(i);
        for( CorefMention m : x.getMentionsInTextualOrder()){
          System.out.println(m.mentionSpan);
        }
      }

      Integer corefClusterID = null;

      List<CoreMap> sentences = document.get(SentencesAnnotation.class);
      for(CoreMap sentence : sentences){
        for(CoreLabel token : sentence.get(TokensAnnotation.class)){

        }
      }
      List<Pair<IntTuple,IntTuple>> x = document.get(CorefGraphAnnotation.class);


      for(CoreMap sentence: sentences){
        for(CoreLabel token: sentence.get(TokensAnnotation.class)){
          if(token.beginPosition() == interval.start()){
            corefClusterID = token.get(CorefClusterIdAnnotation.class);
          }
        }
      }

      if(corefClusterID != null){
        return graph.get(corefClusterID).getMentionsInTextualOrder();
      }
      else{
        return new ArrayList<CorefMention>();
      }

   }
   */

/**
* Provides a lookup method for taking corefMentions and finding their NER tagged
* substrings.
* @param annotatedDocument
* @param position
* @return
*/
private Interval getNamedEntityAtPosition(Annotation annotatedDocument, IntTuple position, KBPQueryEntityType entityType){

  return Interval.open(0, 1);
}


private CoreLabel getTokenBeginningAtByteOffset(Annotation annotatedDocument, Integer beg){

  List<CoreMap> sentences = annotatedDocument.get(SentencesAnnotation.class);
  for(CoreMap sentence : sentences){
    for(CoreLabel token : sentence.get(TokensAnnotation.class)){
      if(token.beginPosition() == beg ){
        return token;
      }
    }
  }
  return null;
}

/**
* Given the information from a CorefMention determine the byte offsets
* of the whole mention and return as a knowitall Interval.
* @param document
* @param sentNum
* @param startIndex
* @param endIndex
* @return
*/

private Interval getCharIntervalFromCorefMention(Annotation document, Integer sentNum, Integer startIndex, Integer endIndex){

  List<CoreMap> sentences = document.get(SentencesAnnotation.class);
  CoreMap sentence = sentences.get(sentNum-1);
  List<CoreLabel> tokens = sentence.get(TokensAnnotation.class);
  List<CoreLabel> spanningTokens = new ArrayList<CoreLabel>();
  
  for(int i = startIndex; i < endIndex; i++){
    spanningTokens.add(tokens.get(i-1));
  }

  return Interval.closed(spanningTokens.get(0).beginPosition(),spanningTokens.get(spanningTokens.size()-1).endPosition());

}

/*public Interval getIntervalOfKBPEntityMention(String kbpEntityString, Interval originalInterval, String docID){

	Annotation document;

	if(corefAnnotationMap.containsKey(docID)){
      document = corefAnnotationMap.get(docID);
    }
    else{
      String xmlDoc = SolrHelper.getRawDoc(docID);
      if(xmlDoc.trim().isEmpty()){
        return null;
      }

      document = new Annotation(xmlDoc);

      try{
        System.out.println("Annotating document "+ docID);
        System.out.println("Document has size " + DocUtils.docLength(docID));
        corefPipeline.annotate(document);
        System.out.println("Done Annotating document "+ docID);
        corefAnnotationMap.put(docID, document);
      }
      catch (Exception e){
        if(corefAnnotationMap.containsKey(docID)){
          corefAnnotationMap.remove(docID);
        }
        return null;
      }
    }

    //get token of possible coref mention
    CoreLabel token = getTokenBeginningAtByteOffset(document, originalInterval.start());
    if(token == null){
      return null;
    }
    Integer corefID = token.get(CorefClusterIdAnnotation.class);
    if(corefID == null){
      return null;
    }
    Map<Integer, CorefChain> graph = document.get(CorefChainAnnotation.class);
    List<CorefMention> mentionsInOrder = graph.get(corefID).getMentionsInTextualOrder();

    for(CorefMention corefMention : mentionsInOrder){
      if (corefMention.mentionSpan.trim().toLowerCase().equals(kbpEntityString.trim().toLowerCase())){
        // this is a match and the originalInterval corefers to the kbpEntityString
        // return the proper interval of this mention of the kbpEntityString
        return getCharIntervalFromCorefMention(document,corefMention.sentNum,corefMention.startIndex,corefMention.endIndex);
      }
    }

    return null;
  }
  */

  public Boolean inSameCorefChainKBPEntityMentionAndQueryName(String kbpEntityString, String arg1String, Interval originalInterval, Annotation document){

    // Search through each coref chain looking for arg1 (specified by originalInterval) and kbpEntityString   

	//Boolean containsArg1String = false;
	//Boolean containsEntityString = false;
	  Boolean inSameCorefChain = false;
	  
    //System.out.println("SHM:" +  arg1String + " " + kbpEntityString );
	
	for (Integer key: document.get(CorefChainAnnotation.class) .keySet()) {
		
		Boolean containsArg1String = false;
		Boolean containsEntityString = false;
		inSameCorefChain = false;
		
      for (CorefMention mention: document.get(CorefChainAnnotation.class).get(key).getMentionsInTextualOrder()) {
			
    	  if (mention.mentionSpan.trim().toLowerCase().equals(kbpEntityString.trim().toLowerCase()))
    	  {  containsEntityString = true;
    	     //System.out.println("Contains Entity String");
    	  }
    	  if (mention.mentionSpan.trim().toLowerCase().equals(arg1String.trim().toLowerCase()))
    	  {  containsArg1String = true; 
    	     //System.out.println("Contains Arg1 String");
    	  }	  
          if(containsEntityString & containsArg1String) {
        	  //System.out.println("Contains Both Strings");
        	  inSameCorefChain = true; 
        	  break;
          }
    	  
      }
      if(inSameCorefChain) break;
    }  	  
	  
    //Map<Integer, CorefChain> graph = document.get(CorefChainAnnotation.class);
    //System.out.println("Stanford: mentions: " + graph.size());
    //System.out.println("Stanford: mentions: " + graph.keySet());
    //List<CorefMention> mentionsInOrder = graph.get(corefID).getMentionsInTextualOrder();
    
    //for(CorefMention corefMention : mentionsInOrder){
      //System.out.println("Stanford: mention:" + corefMention.toString() ); 	
      //if (corefMention.mentionSpan.trim().toLowerCase().equals(kbpEntityString.trim().toLowerCase())){
        // this is a match and the originalInterval corefers to the kbpEntityString
        // return the proper interval of this mention of the kbpEntityString
    	 
       // return getCharIntervalFromCorefMention(document,corefMention.sentNum,corefMention.startIndex,corefMention.endIndex);
      //}
   // }

    return inSameCorefChain;
  }


}
