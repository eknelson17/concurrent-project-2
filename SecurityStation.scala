//Author: Dan Lavoie
// Editied by: Emma Nelson
import akka.actor.Actor
import akka.actor.ActorRef
import scala.collection.mutable.MutableList

//The security station processes passengers who move through the system.
//It keeps track of bags and people sent through its line's scanners,
//and processes whether to send them through or put them in the jail.
//At the end of the day, it checks if it can close by waiting for its
//associated scanners to close.
class SecurityStation(val line : Int, val jail : ActorRef) extends Actor {
	val stationLine = line
	var passengers = new MutableList();
	var bagScanner = null
	var bodyScanner = null
	
	def receive = {
		case SendBagScanner(bs) =>
			bagScanner = bs

		case SendBodyScanner(bs) =>
			bodyScanner = bs

		case BagToSecurityStation(bag) =>
			// When the bag scanner sends a bag to the security station
			println("Bag " + bag._1 + " arrived at Security Station.")
			checkPassengerList(bag)

		case PersonToSecurityStation(passenger) =>
			// TODO is there a way to find out who sent the message so we know if it is a bag or person?
			// Also just a stub right now so I could test the program and get it to run. 
			println("Passenger " + passenger._1 + " arrived at Security Station.")
			checkPassengerList(passenger)

		case ScannerClosed(id) =>
			// When both scanners have sent the message, it can kill itself
			// As each scanner sends the message, it replies with a poisonpill or stop call
			println("Scanner closed")
	}

	def checkPassengerList(val passenger : Tuple2) = {
		// Check if the passenger or bag has already been partially processed.
		// To do this, iterate through the list. If there is a tuple with the same first int,
		// then an entry exists. Otherwise, it does not.

		//Create the boolean to check if an entry is found
		var entryFound = false
		var entryIndex = -1
		for (i <- 0 to passengers.length()) {
			if(passengers.get(i)._1 == passenger._1) {
				entryFound = true
				entryIndex = i
			}
		}
		// If we found the passenger in the list, finish processing them
		if (entryFound) {
			// Check if one of the entries didn't pass the security scan
			if(passengers.get(entryIndex)._2 == false || passenger._2 == false) {
				// Something didn't pass. To Jail!
				println("Passenger " + passenger._1 + " failed the security check and heads to jail.")
				// We need to remove the entry in the list. Since MutableList can't "remove()" an item,
				// we use the filterNot function to build a list of only elements not including passenger.
				passengers = passengers.filterNot(_._1 == passenger._1)
				// Send the passenger to jail
				jail ! Prisoner(passenger)
			}
			else {
				println("Passenger " + passenger._1 + " passed the security check and heads to a flight.")
				// We need to remove the entry in the list. Since MutableList can't "remove()" an item,
				// we use the filterNot function to build a list of only elements not including passenger.
				passengers = passengers.filterNot(_._1 == passenger._1)
			}
		}
		// If we didn't find them in the list, add them to the list
		else {
			passengers.add(passenger)
		}
	}
}