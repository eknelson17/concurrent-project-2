// Author: Maddison Hickson
// Edited By: Emma Nelson
import akka.actor.Actor
import akka.actor.ActorRef
import scala.util.Random

// Immutable message sent to the Queue to ask for a 
case class NextBag

class BagScanner(val nLines : Int, val securityStation : ActorRef) extends Actor {
	var hasPassed = true
	val random = new Random()

	def receive = {	
		case GetPassenger(bag) =>
			if(random.nextInt(5) == 1) {
				hasPassed = false
			}
			securityStation ! ToSecurityStation((bag, hasPassed))
	}
}