import scala.collection.mutable.MutableList
import akka.actor.ActorRef

// Send the scanners to the DocScanner so it knows about them
case class SendScanners(bodyScanners : MutableList[ActorRef], bagScanners : MutableList[ActorRef])

// Sent to the Controller to tell it to create passengers
case object SendPassengers

// Immutable message sent to the queue with the passenger
case class GetPassenger(passenger : Int)

// Immutable message sent to the queue with the passenger
case class ToLine(passenger : Int)

// Immutable message sent to the Queue to ask for a 
case class NextBag

// Immutable message sent to the Security Station with the passenger
// and the 
case class ToSecurityStation(passenger : (Int, Boolean))

// Immutable message sent to the Queue to ask for a 
case class NextPassenger

// Tells the controller it is the end of a day
case object EndDay