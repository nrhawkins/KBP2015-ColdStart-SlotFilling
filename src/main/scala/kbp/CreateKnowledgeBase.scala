package kbp

import scala.collection.JavaConversions._
import edu.knowitall.openie._


object CreateKnowledgeBase {

  def main(args: Array[String]) {
  
    val runID = "UWashington1"
    val detailed = false

    //for(file <- myDirectory.listFiles if file.getName endsWith ".txt"){
      // process the file
    //}
    
    println("KBP 2015!")

    val sentence = "U.S. President Barack Obama, born in Hawaii, spoke at the White House on Saturday about hula dancing, while his wife, Michelle looked on."
      
    val openie = new OpenIE() 
      
    val extraction = openie.extract(sentence)
 
    println("Length extraction: " + extraction.size)

    extraction.foreach(e => println(e))
    
    println(extraction(0).extraction.arg1.displayText)
    println(extraction(0).extraction.arg1.offsets)
    println(extraction(0).extraction.rel)
    println(extraction(0).extraction.arg2s.size)
    println(extraction(0).extraction.arg2s)
    println(extraction(0).extraction.context)
    println(extraction(0).extraction.negated)
    println(extraction(0).extraction.passive)
    
  }  
    
}
  
