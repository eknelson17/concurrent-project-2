// Author: Maddison Hickson
// Edited by: Emma Nelson
import akka.actor.Actor
import akka.actor.ActorRef
import scala.util.Random
import scala.collection.mutable.MutableList

// Decides whether a passenger passes or fails(20% chance they fail) 
// the body scan and then sends this information to the SecurityStation.
class BodyScanner(val id : Int, val securityStation : ActorRef) extends Actor {
	var hasPassed = true
	val random = new Random()
	
	override def preStart = {
		securityStation ! SendBodyScanner(self)
	}

	//recieves a passenger from DocScanner to scan
	def receive = {	
		case GetPassenger(passenger) =>
			//decides whether the passenger fails or passes the scan
			if(random.nextInt(5) == 1) {
				hasPassed = false
				println("Passenger " + passenger + " has failed inspection.")
			} else {
				println("Passenger " + passenger + " has passed inspection.")
			}
			//sends a tuple with the Passenger and whether or not a person passed the scan
			securityStation ! PersonToSecurityStation(new Tuple2(passenger : Int, hasPassed : Boolean))
			println("Passenger " + passenger + " has been sent to the Security Station.")
	}

	override def postStop {
		securityStation ! ScannerClosed(id)
	}
}