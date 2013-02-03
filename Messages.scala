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
// and the 
case class ToSecurityStation(passenger : Tuple2[Int, Boolean])

// Send a passenger to jail
case class Prisoner(passenger : Int)

// Tells the controller it is the end of a day
case object EndDay

// Close the jail by sending all the prisoners to permanent lockup
case object CloseJail

// Sent to Security Station to tell it one of the scanners has closed
// Once the second message is received, then the Security Station can close
case class ScannerClosed(id : Int)