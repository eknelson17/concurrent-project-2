// Author: Emma Nelson
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import scala.collection.mutable.MutableList

// Sent to the Controller to tell it to create passengers
case object SendPassengers

// Main file - starts everything, controls time,
// says when to send bursts of passengers, and
// tells the controller to close everything
object tsa {
	var NLINES = 5

	def main(arg: Array[String]) = {
		val system = ActorSystem("TSASystem")
		val jail = system.actorOf(Props(new Jail()))

		var queues = MutableList[ActorRef]()

		// Create all the queues and add them to a list to be sent to the DocScanner
		for(i <- 1 to NLINES) {
			// TODO fix the following line to be correct for the Queue class
			val queue = system.actorOf(Props(new Queue()))
			queues = queues.+=(queue)
		}

		// Create and start the DocScanner
		val docScanner = system.actorOf(Props(new DocScanner(NLINES, queues)))

		// Create and start the Controller
		// TODO add other needed params
		val controller = system.actorOf(Props(new Controller(docScanner, jail)))
	}
}