// Author: Maddison Hickson
// Edited By: Emma Nelson
import akka.actor.Actor
import akka.actor.ActorRef
import scala.util.Random

class BagScanner(val nLines : Int, val securityStation : ActorRef) extends Actor {
	var hasPassed = true
	val random = new Random()

	override def preStart = {
		securityStation ! SendBagScanner(self)
	}

	def receive = {	
		case GetPassenger(bag) =>
			if(random.nextInt(5) == 1) {
				hasPassed = false
				println("The bag belonging to Passenger " + bag + " has failed inspection.")
			} else {
				println("The bag belonging to Passenger " + bag + " has passed inspection.")
			}
			securityStation ! BagToSecurityStation(new Tuple2(bag : Int, hasPassed : Boolean))
			println("The bag belonging to Passenger " + bag + " was sent to the Security Station.")
	}

	override def postStop {
		securityStation ! ScannerClosed
	}
}