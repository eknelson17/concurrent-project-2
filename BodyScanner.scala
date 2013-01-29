// Author: Maddison Hickson

import akka.actor.*

// Immutable message sent to the Security Station with the passenger
// and the 
case class ToSecurityStation() { val passenger : (Int, Boolean) }

// Immutable message sent to the Queue to ask for a 
case class NextPassenger() { val passenger : (Int, Boolean) }

class BodyScanner(val nLines : Int) extends Actor {
	var hasPassed;
	val random = new Random()

	def receive = {
		case GetPassenger(passenger) =>
			if(random.next(5) = 1) {
				SecurityStation[ToSecurityStation] ! ToSecurityStation((passenger, false))
			}
			else{
				SecurityStation[ToSecurityStation] ! ToSecurityStation((passenger, true))
			}
	}
}