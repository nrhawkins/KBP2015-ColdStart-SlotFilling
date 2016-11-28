package kbp

import scala.xml.XML
import KBPQueryEntityType._

case class KBPQuery (val id: String, val name: String, val doc: String,
    val begOffset: Int, val endOffset: Int, val entityType: KBPQueryEntityType,
    val slotsToFill: Set[Slot] ){

  var aliases = List(name) 
  //def aliases():List[String] = name :: List[String]()
  //def aliases():List[String] = { aliases } 
  
  override def toString():String = id + "\t" + name
  
}

object KBPQuery {

  import KBPQueryEntityType._

  
  private def parseSingleKBPQueryFromXML(queryXML: scala.xml.Node): Option[KBPQuery] = {

    try{
	    val idText = queryXML.attribute("id") match 
	    		{case Some(id) if id.length ==1 => id(0).text
	    		 case None => throw new IllegalArgumentException("no id value for query in xml doc")
	    		}
	    val nameText = queryXML.\\("name").text
	    val docIDText = queryXML.\\("docid").text
	    val begText = queryXML.\\("beg").text
	    val begInt = begText.toInt
	    val endText = queryXML.\\("end").text
	    val endInt = endText.toInt
	    val entityTypeText = queryXML.\\("enttype").text
	    val entityType = entityTypeText match {
	      case "ORG" | "org" => ORG
	      case "PER" | "per" => PER
	      case "GPE" | "gpe" => GPE
	      case _ => throw new IllegalArgumentException("improper 'enttype' value in xml doc")
	    }
	val nodeIDText = queryXML.\\("nodeid").text.trim()
    val nodeId = if (nodeIDText.isEmpty || nodeIDText.startsWith("NIL")) None else Some(nodeIDText)
    val ignoreText = queryXML.\\("ignore").text
    val ignoreSlots = {
       val ignoreNames = ignoreText.split(" ").toSet
       Slot.getSlotTypesList(entityType).filter(slot => ignoreNames.contains(slot.name))
    }
	
	val slotText = queryXML.\\("slot").text.trim() 	            
    val slot0Text = queryXML.\\("slot0").text.trim() 	    
    val slot1Text = queryXML.\\("slot1").text.trim() 
	    
	//find slotsToFill by taking the difference between the global slots set
    // and the set specified in the xml doc
    val slotsToFill = entityType match{
	   case GPE => {
	        Slot.gpeSlots &~ ignoreSlots
	      }
       case ORG => {
          Slot.orgSlots &~ ignoreSlots
       }
       case PER => {
          Slot.personSlots &~ ignoreSlots
       }
    }
	
	//val slotsToFillColdStart = slotsToFill.filter(s => s.name == slotText)
    val slotsToFillColdStart = slotsToFill.filter(s => s.name == slot0Text)
    val numSlotsToFillColdStart = slotsToFillColdStart.size
        
    if(numSlotsToFillColdStart != 1) println("numSlotsToFillColdStart: " + numSlotsToFillColdStart)
        
    numSlotsToFillColdStart match{
      case 0 => None
      case 1 => {
        new Some(KBPQuery(idText,nameText,docIDText,begInt,endInt,entityType,slotsToFillColdStart))
        //val q = KBPQuery(idText,nameText,docIDText,begInt,endInt,entityType,slotsToFillColdStart)
        //val docIdsColdStart = q.docIds.filter(d => ColdStartCorpus.documents.contains(d)) 
        //if(docIdsColdStart.size > 0) q.docIds = List(docIDText) ::: docIdsColdStart 
        //else q.docIds = List(docIDText)
        //Some(q)
        }
      case _ => new Some(KBPQuery(idText,nameText,docIDText,begInt,endInt,entityType,slotsToFill))
        }        
	
	//new Some(KBPQuery(idText,nameText,docIDText,begInt,endInt,entityType,slotsToFill))
    
    }
    
    catch {
      case e: Exception => {
        println(e.getMessage())
        return None
        
      }
    }      
  }  

  def parseKBPQueries(pathToFile: String): List[KBPQuery] = {
    
     val xml = XML.loadFile(pathToFile)
     val queryXMLSeq = xml.\("query")
     
     val kbpQueryList = for( qXML <- queryXMLSeq) yield parseSingleKBPQueryFromXML(qXML)
     
     kbpQueryList.toList.flatten
  }
  
  def getAliases(queries: List[KBPQuery]): List[KBPQuery] = {
    
    val fmls = "([A-Za-z.-]+) ([A-Za-z.-]+) ([A-Za-z-]+) ([jJSs][Rr].{0,1})".r
    val fml = "([A-Za-z.-]+) ([A-Za-z.-]+) ([A-Za-z-]+)".r
    val fls = "([A-Za-z.-]+) ([A-Za-z-]+) ([jJSs][Rr].{0,1})".r
    val fl = "([A-Za-z.-]+) ([A-Za-z-]+)".r
    
    println("Getting Aliases: " + queries.size)
    
    queries.foreach(q =>
      
      q.entityType match {
        
        // assign aliases for PER
        case PER => { 
          
          //println("Found PER")
          
          q.name match {
            // want to add: fml, fl to alias list, query name (fmls) is already there
            case fmls(f,m,l,s) => {
              val fml = f + " " + m + " " + l  
              val fl = f + " " + l 
              q.aliases = q.aliases ::: List(fml, fl)
            }
            // want to add: fl to alias list, query name (fml) is already there
            case fml(f,m,l) => {
              //println("Found fml")
              val fl = f + " " + l 
              q.aliases = q.aliases ::: List(fl) 
            }
            // want to add: fl to alias list, query name (fls) is already there
            case fls(f,l,s) => {
              //println("Found fls")
              val fl = f + " " + l               
              q.aliases = q.aliases ::: List(fl) 
            }
            // no additions need to be made, query name (fl) is already there
            case fl(f,l) => 
              //println("Found fl")
            // no additions need to be made, just go with query name  
            case _ => 
          } 
        }    
        // no aliases defined for ORG
        case ORG => {          
          //println("Found ORG")
        }     
        // no aliases defined for GPE
        case GPE => {          
          //println("Found GPE")
        }          
        
      }
    )

    queries    
  }
  
}
