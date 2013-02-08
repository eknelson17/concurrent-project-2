// Author: Dan Lavoie
// Edited by: Emma Nelson, Maddison Hickson
import akka.actor.Actor
import akka.actor.ActorRef
import scala.collection.mutable.MutableList

//The Jail keeps a list of immutable passenger objects it is sent.
//At the end of the day, it receives a message to put its passengers
//in permanent lockup, and purges all of its passengers.
class Jail(val numLines : Int, val controller : ActorRef) extends Actor {
	var passengers = new MutableList[Int]()
	var numClosedLines = 0

	def receive = {
		//receives passenger from the SecurityStation to put in jail
		case Prisoner(passenger) =>
			println("Passenger " + passenger + 
				" is recieved by the jail")
			passengers = passengers += passenger
			println("Passenger " + passenger + 
				" is put in jail.")

		case LineClosed =>
			numClosedLines = numClosedLines + 1
			if(numLines == numClosedLines) {
				val size = passengers.length
				while(!passengers.isEmpty) {
					//While passengers are left, slice them out one at a time
					//and print who we've removed
					println("Passenger " + passengers.head + 
						" is being sent to permanent lockup.")
					passengers = passengers.slice(1, passengers.length)
					// Take some time to represent sending the passenger out of jail
					Thread.sleep(50)
				}
				println("All passengers in permanent lockup. Jail is closing for the day.")
				controller ! JailClosed
			}
	}
}