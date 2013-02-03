// Author: Maddison Hickson
// Edited by: Emma Nelson
import akka.actor.Actor
import akka.actor.ActorRef
import scala.util.Random
import scala.collection.mutable.MutableList

class BodyScanner(val id : Int, val securityStation : ActorRef) extends Actor {
	var hasPassed = true
	val random = new Random()

	override def preStart = {
		securityStation ! SendBodyScanner(self)
	}

	def receive = {	
		case GetPassenger(passenger) =>
			if(random.nextInt(5) == 1) {
				hasPassed = false
				println("Passenger " + passenger + " has failed inspection.")
			} else {
				println("Passenger " + passenger + " has passed inspection.")
			}
			securityStation ! ToSecurityStation(new Tuple2(passenger : Int, hasPassed : Boolean))
			println("Passenger " + passenger + " has been sent to the Security Station.")
	}

	override def postStop {
		securityStation ! ScannerClosed(id)
	}
}