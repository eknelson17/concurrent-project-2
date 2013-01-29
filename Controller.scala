// Author: Emma Nelsons
import akka.actor.*

// Immutable message sent to the queue with the passenger
case class GetPassenger() { passenger : val }

// Creates and sends passengers into the system when the main tells 
// it to. Closes everything else as possible.
class Controller(var docScanner : ActorRef) extends Actor {
	val random = new Random()
	var passNum = 1
	
	def receive = {
		case SendPassengers =>
			var numPass = random.nextInt(100)
			for(i -> 1 to numPass) {
				docScanner ! GetPassenger(passNum)
				passNum = 1 + passNum
			}
		// TODO Insert Close operations when we have that figured out
	}
}