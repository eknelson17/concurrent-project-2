// Author: Maddison Hickson
// Edited By: Emma Nelson
import akka.actor.Actor
import akka.actor.ActorRef
import scala.util.Random

// Decides whether a passenger's bag passes or fails(20% chance they fail) 
// the baggage scan and then relays this information to the SecurityStation. 
class BagScanner(val nLines : Int, val securityStation : ActorRef) extends Actor {
	var hasPassed = true
	val random = new Random()

	override def preStart = {
		securityStation ! SendBagScanner(self)
	}

	def receive = {	
		//recieves a bag from DocScanner to scan
		case GetPassenger(bag) =>
			//decides whether the bag fails or passes the scan
			if(random.nextInt(5) == 1) {
				hasPassed = false
				println("The bag belonging to Passenger " + bag + " has failed inspection.")
			} else {
				println("The bag belonging to Passenger " + bag + " has passed inspection.")
			}
			//sends a tuple with the Passenger and whether or not the bag passed the scan
			securityStation ! BagToSecurityStation(new Tuple2(bag : Int, hasPassed : Boolean))
			println("The bag belonging to Passenger " + bag + " was sent to the Security Station.")
	}

	override def postStop {
		securityStation ! ScannerClosed
	}
}