// Author: Maddison Hickson
// Edited by: Emma Nelson
import akka.actor.Actor
import akka.actor.ActorRef
import scala.util.Random
import scala.collection.mutable.MutableList

// Gets a passenger from DoScanner and decides whether a passenger passes 
// or fails(20% chance they fail) the body scan and then sends this 
// information to the SecurityStation.
class BodyScanner(val id : Int, val securityStation : ActorRef) extends Actor {
	var hasPassed = true
	val random = new Random()
	
	//initializes by sending a reference of itself to securityStation
	override def preStart = {
		securityStation ! SendBodyScanner(self)
	}

	def receive = {	
		//receives a passenger from DocScanner to scan
		case ToLine(passenger) =>
			//decides whether the passenger fails or passes the scan
			if(random.nextInt(5) == 1) {
				hasPassed = false
				println("Passenger " + passenger + " has failed inspection.")
			} else {
				hasPassed = true
				println("Passenger " + passenger + " has passed inspection.")
			}
			//sends a tuple with the Passenger and whether or not a person passed the scan
			securityStation ! PersonToSecurityStation(new Tuple2(passenger : Int, hasPassed : Boolean))
			println("Passenger " + passenger + " has been sent to the Security Station by the BodyScanner.")
	}

	//before finishing it tells the securityStation it has closed
	override def postStop {
		securityStation ! ScannerClosed(id)
	}
}