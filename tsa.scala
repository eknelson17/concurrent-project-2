// Author: Emma Nelson
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import scala.collection.mutable.MutableList
import scala.util.Random

// Main file - starts everything, controls time,
// says when to send bursts of passengers, and
// tells the controller to close everything
object TSA {
	val NLINES = 5

	def main(arg: Array[String]) = {
		// Setup time
		val startTime = System.currentTimeMillis
		val endTime = startTime + 2000

		// Setup the Actor system
		val system = ActorSystem("TSASystem")

		// In order to do away with the queues, we will have to use scala routing
		// to find out about messages. Might need it for the docScanner too. -Emma

		// This might solve all the problems with the mailboxes if it is sent 
		// after the controller is told to stop generating passengers...
		// "You can also send an actor the akka.actor.PoisonPill message, 
		// which will stop the actor when the message is processed. 
		// PoisonPill is enqueued as ordinary messages and will be handled 
		// after messages that were already queued in the mailbox."

		// Create and start the Controller
		val controller = system.actorOf(Props(new Controller(system, NLINES)))
		println("The Controller has been started.")

		// Send passengers
		val random = new Random()
		while (System.currentTimeMillis <= endTime) {
			if(random.nextInt(1000) <= 1) {
				controller ! SendPassengers
				println("More passengers have arrived at the airport.")
			}
		}

		controller ! EndDay

		// I think the Controller might have to construct the other actors in order
		// for shut down to work, but I'm not sure yet. Looking into it. -Emma

		// Maybe we could just pass it the system? Can't send messages to a non-actor
		// so the main can't close the system. I'm gonna sleep on it...
	}
}