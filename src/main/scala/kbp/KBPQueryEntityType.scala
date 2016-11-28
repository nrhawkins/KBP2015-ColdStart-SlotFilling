package kbp

object KBPQueryEntityType extends Enumeration{
  type KBPQueryEntityType = Value
  val ORG, PER, GPE = Value
  
  def fromString(str: String) = str.trim.toLowerCase match {
    case "per" | "person" => PER
    case "org" | "organization" => ORG
    case "gpe" | "geopoliticalorganization" => GPE
    case _ => throw new RuntimeException(s"Invalid KBPQueryEntityType: $str")
  }
  
  def toString(t: KBPQueryEntityType) = t match {
    case ORG => "ORG"
    case PER => "PER"
    case GPE => "GPE"
  }
}