// Author: Emma Nelson

// Sent to the Controller to tell it to create passengers
case object SendPassengers

// Main file - starts everything, controls time,
// says when to send bursts of passengers, and
// tells the controller to close everything
object tsa extends Any {
	var NLINES = 5;

	def main(arg: Array[String]) = {
		var queues = List.empty[ActorRef]

		// Create all the queues and add them to a list to be sent to the DocScanner
		var queue
		for(i <- 1 to NLINES) {
			// TODO fix the following line to be correct for the Queue class
			queue = Actor.actorOf[Queue].start()
			queues = queue :: queues
		}

		// Create and start the DocScanner
		val docSanner = Actor.actorOf(DocScanner(NLINES, queues)).start()

		// Create and start the Controller
		// TODO add other needed params
		val controller = Actor.actorOf(Controller(docSanner)).start()
	}
}