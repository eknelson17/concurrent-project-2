// Author: Emma Nelsons
import akka.actor.Actor
import akka.actor.ActorRef
import scala.util.Random
import scala.collection.mutable.MutableList

// Immutable message sent to the queue with the passenger
case class ToLine() { val passenger : Int }

// Gets a passenger from the Controller, decides whether or not the
// passenger's documentation is valid (80% chance it is) and sends them
// on to the next queue. If not, they are turned away.
class DocScanner(val nLines : Int, val queues : MutableList[ActorRef]) extends Actor {
	val numLines = nLines
	var nextLine = 0
	val random = new Random()
	
	def receive = {
		case GetPassenger(passenger) =>
			if(random.nextInt(100) > 20) {
				queues.get(nextLine) ! ToLine(passenger)
				println("Passenger " + passenger + " is sent to queue " + nextLine + " by the Doc Scanner.")
				nextLine = (nextLine + 1) % numLines
			} else {
				println("Passenger " + passenger + " has invalid documentation.")
			}
	}
}