// Author: Maddison Hickson
// Edited by: Emma Nelson
import akka.actor.Actor
import akka.actor.ActorRef
import scala.util.Random
import scala.collection.mutable.MutableList

// Immutable message sent to the Security Station with the passenger
// and the 
case class ToSecurityStation(passenger : (Int, Boolean))

// Immutable message sent to the Queue to ask for a 
case class NextPassenger

class BodyScanner(val nLines : Int, val securityStation : ActorRef) extends Actor {
	var hasPassed = true
	val random = new Random()

	def receive = {	
		case GetPassenger(passenger) =>
			if(random.nextInt(5) == 1) {
				hasPassed = false
				println("Passenger " + passenger + " has failed inspection.")
			} else {
				println("Passenger " + passenger + " has passed inspection.")
			}
			securityStation ! ToSecurityStation((passenger, hasPassed))
			println("Passenger " + passenger + " has been sent to the Security Station.")

			// TODO request the next passenger from the Queue

		// TODO add close functionality
	}
}