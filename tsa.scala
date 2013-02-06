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
		val endTime = startTime + 1000

		// Setup the Actor system
		val system = ActorSystem("TSASystem")

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
	}
}