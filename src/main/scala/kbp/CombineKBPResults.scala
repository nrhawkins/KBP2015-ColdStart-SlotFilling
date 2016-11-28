package kbp

import java.io._
import java.nio.file.{Paths, Files}

import scala.collection.JavaConversions._
import scala.io.Source

import com.typesafe.config.ConfigFactory


object CombineKBPResults {

  val config = ConfigFactory.load("kbp-2015.conf")
  //val outDir = config.getString("out-dir")
  val queriesFileName = config.getString("queries-file")
  val openieFileName = config.getString("openie-file")
  val implieFileName = config.getString("implie-file")
  val multirFileName = config.getString("multir-file")
  val outFileName = config.getString("out-file")
  val runID = config.getString("runID")
  
  case class kbpAnswer(id: String, rel: String, runID: String, provAll: String, slotFill: String,
      provSF: String, confScore: String, slotFillType: String)
  
      
  def main(args: Array[String]) {
  
    //val runID = "UWashington1_round1"
    //val runID = "UWashington1_round2"
    val detailed = false
    val outStream = new PrintStream(outFileName)
    
    // ---------------------------
    // Read Openie Slot Fills
    // ---------------------------
    val openieSF = {
     
      val inputFilename = openieFileName    
      // Does file exist?
      if (!Files.exists(Paths.get(inputFilename))) {
        System.out.println(s"Openie file $inputFilename doesn't exist!  " + s"Exiting...")
        sys.exit(1)
      } 
      // Read file, line by line
      Source.fromFile(inputFilename).getLines().map(line => {
        val tokens = line.trim.split("\t")
        if(tokens.size == 8){ 
          kbpAnswer(tokens(0), tokens(1), tokens(2), tokens(3), tokens(4), tokens(5), tokens(6), tokens(7))
        }
      }).toList
      
    }           

    // ---------------------------
    // Read Implie Slot Fills
    // ---------------------------
    val implieSF = {
     
      val inputFilename = implieFileName    
      // Does file exist?
      if (!Files.exists(Paths.get(inputFilename))) {
        System.out.println(s"Implie file $inputFilename doesn't exist!  " + s"Exiting...")
        sys.exit(1)
      } 
      // Read file, line by line
      Source.fromFile(inputFilename).getLines().map(line => {
        val tokens = line.trim.split("\t")
        if(tokens.size == 8){ 
          kbpAnswer(tokens(0), tokens(1), tokens(2), tokens(3), tokens(4), tokens(5), tokens(6), tokens(7))
        }
      }).toList
      
    }           

    // ---------------------------
    // Read Multir Slot Fills
    // ---------------------------
    val multirSF = {
     
      val inputFilename = multirFileName    
      // Does file exist?
      if (!Files.exists(Paths.get(inputFilename))) {
        System.out.println(s"Multir file $inputFilename doesn't exist!  " + s"Exiting...")
        sys.exit(1)
      } 
      // Read file, line by line
      Source.fromFile(inputFilename).getLines().map(line => {
        val tokens = line.trim.split("\t")
        if(tokens.size == 8){ 
          kbpAnswer(tokens(0), tokens(1), tokens(2), tokens(3), tokens(4), tokens(5), tokens(6), tokens(7))
        }
      }).toList
      
    }           
    
    // ---------------------------
    // Parse the Queries
    // ---------------------------
    
    val queries = KBPQuery.parseKBPQueries(queriesFileName)
    println("Number of queries: " + queries.size)
    
    queries.foreach(q => {
      
      val slotToFill = q.slotsToFill
      
      if( slotToFill.size == 1){
        val numAnswers = slotToFill.toList(0).maxResults         
      }      
      
    })
    
    
    
    
    
    
    
    
    
  }  
    
}
  
