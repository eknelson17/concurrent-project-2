// Author: Dan Lavoie
// Edited by: Emma Nelson
import akka.actor.*

//The Jail keeps a list of immutable passenger objects it is sent.
//At the end of the day, it receives a message to put its passengers
//in permanent lockup, and purges all of its passengers.
class Jail extends Actor {
	var passengers = new List.empty[ActorRef]
	def receive = {
		case Prisoner(passenger) =>
			passengers = passengers ++ passenger
			println("Passenger " + passenger + 
				" is sent to jail by a security station")
		case Close =>
			while(!passengers.isEmpty) {
				//While passengers are left, slice them out one at a time
				//and print who we've removed
				println("Passenger " + passengers.head + 
					" is being sent to permanent lockup.")
				passengers = passengers.slice(1, passengers.length)
			}
			println("All passengers in permanent lockup. Jail is closing for the day.")
	}
}