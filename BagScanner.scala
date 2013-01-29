// Author: Maddison Hickson

import akka.actor.*

// Immutable message sent to the Security Station with the passenger
// and the 
case class ToSecurityStation() { val bag : (Int, Boolean) }

// Immutable message sent to the Queue to ask for a 
case class NextBag() {}

class BagScanner(val nLines : Int) extends Actor {
	var hasPassed;
	val random = new Random()

	def receive = {
		case Init() =>
		
		case GetBag(bag) =>
			if(random.next(5) = 1) {
				SecurityStation[ToSecurityStation] ! ToSecurityStation((bag, false))
			}
			else{
				SecurityStation[ToSecurityStation] ! ToSecurityStation((bag, true))
			}
	}
}