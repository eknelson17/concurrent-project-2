// Author: Emma Nelson
import scala.collection.mutable.MutableList
import akka.actor.ActorRef

// Send the scanners to the DocScanner so it knows about them
case class SendScanners(bodyScanners : MutableList[ActorRef], bagScanners : MutableList[ActorRef])

case class SendBagScanner(bagScanner : ActorRef)
case class SendBodyScanner(bodyScanner : ActorRef)

// Sent to the Controller to tell it to create passengers
case object SendPassengers

// Immutable message sent to the queue with the passenger
case class GetPassenger(passenger : Int)

// Immutable message sent to the queue with the passenger
case class ToLine(passenger : Int)

// Immutable message sent to the Security Station with the passenger
case class PersonToSecurityStation(passenger : Tuple2[Int, Boolean])

// Immutable message sent to the Security Station with the bag
case class BagToSecurityStation(bag : Tuple2[Int, Boolean])

// Send a passenger to jail
case class Prisoner(passenger : Int)

// Tells the controller it is the end of a day
case object EndDay

// Tells the jail a line has closed
case object LineClosed

// Sent to Security Station to tell it one of the scanners has closed
// Once the second message is received, then the Security Station can close
case class ScannerClosed(id : Int)