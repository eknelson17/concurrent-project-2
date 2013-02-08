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
	val LENGTH = 1000

	def main(arg: Array[String]) = {
		// Setup time
		val startTime = System.currentTimeMillis
		val endTime = startTime + LENGTH

		// Setup the Actor system
		val system = ActorSystem("TSASystem")

		// Create and start the Controller
		val controller = system.actorOf(Props(new Controller(system, NLINES)))
		println("The Controller has been started.")

		// Send passengers
		val random = new Random()
		while (System.currentTimeMillis <= endTime) {
			if(random.nextInt(30) <= 1) {
				println("tsa sends a message to Controller to tell Controller to make passengers")
				controller ! SendPassengers
				println("More passengers have arrived at the airport.")
			}
			Thread.sleep(10);
		}

		controller ! EndDay
	}
}