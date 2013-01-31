// Author: Emma Nelson
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import scala.collection.mutable.MutableList
import scala.util.Random

// Sent to the Controller to tell it to create passengers
case object SendPassengers

// Tells the controller it is the end of a day
case object EndDay

// Main file - starts everything, controls time,
// says when to send bursts of passengers, and
// tells the controller to close everything
object tsa {
	var NLINES = 5

	def main(arg: Array[String]) = {
		// Setup time - currently set to run 24 hours in 1.44 minutes (I think)
		val startTime = System.currentTimeMillis
		val endTime = startTime + 86400

		// Setup the Actor system
		val system = ActorSystem("TSASystem")
		val jail = system.actorOf(Props(new Jail()))
		println("The Jail has been started.")

		var queues = MutableList[ActorRef]()

		// Create all the queues and add them to a list to be sent to the DocScanner
		for(i <- 1 to NLINES) {
			// TODO fix the following line to be correct for the Queue class
			// Passed i so it knows what number it is
			val queue = system.actorOf(Props(new Queue(i)))
			println("Queue " + i + " has been started.")
			queues = queues.+=(queue)
		}

		// In order to do away with the queues, we will have to use scala routing
		// to find out about messages. Might need it for the docScanner too. -Emma

		// This might solve all the problems with the mailboxes if it is sent 
		// after the controller is told to stop generating passengers...
		// "You can also send an actor the akka.actor.PoisonPill message, 
		// which will stop the actor when the message is processed. 
		// PoisonPill is enqueued as ordinary messages and will be handled 
		// after messages that were already queued in the mailbox."

		// Create and start the DocScanner
		val docScanner = system.actorOf(Props(new DocScanner(NLINES, queues)))
		println("The DocScanner has been started.")

		// Create and start the Controller
		// TODO add other needed params
		val controller = system.actorOf(Props(new Controller(docScanner, jail)))
		println("The Controller has been started.")

		// Send passengers - 20% chance per millisecond
		while (System.currentTimeMillis != endTime) {
			if(random.nextInt(100) <= 20) {
				controller ! SendPassengers
				println("More passengers have arrived at the airport.")
			}
		}

		controller ! EndDay

		// I think the Controller might have to construct the other actors in order
		// for shut down to work, but I'm not sure yet. Looking into it. -Emma
	}
}